# syntax=docker/dockerfile:1.7
#
# Multi-stage build for the Learn Crypto Spring Boot app.
#
#   build   - compiles and packages the fat jar with Maven (dependencies cached via BuildKit)
#   extract - splits the jar into Spring Boot layers using the *runtime* JVM image
#   runtime - small JRE image, non-root, layered COPYs, AppCDS archive trained in place
#
# The AppCDS archive is created in the final stage with the very same JVM build and file layout
# that later runs the app (CDS validates JVM build, class path, jar sizes and mtimes).

ARG RUNTIME_IMAGE=eclipse-temurin:21-jre-alpine

# ---------------------------------------------------------------------------------------------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

# Resolve dependencies first so this layer is reused as long as pom.xml does not change.
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN --mount=type=cache,target=/root/.m2 ./mvnw -B -q dependency:go-offline

COPY src src
RUN --mount=type=cache,target=/root/.m2 ./mvnw -B -q -DskipTests package \
    && cp target/*.jar app.jar

# ---------------------------------------------------------------------------------------------
FROM ${RUNTIME_IMAGE} AS extract
WORKDIR /extract
COPY --from=build /workspace/app.jar app.jar
RUN java -Djarmode=tools -jar app.jar extract --layers --launcher --destination extracted

# ---------------------------------------------------------------------------------------------
FROM ${RUNTIME_IMAGE} AS runtime

# Shared JVM tuning for a 512 MiB / 1 vCPU container (Cloud Run "small"). The training run below
# uses the same flags so the archived class metadata matches the production JVM configuration.
ARG JVM_FLAGS="-XX:MaxRAMPercentage=70 -XX:+UseSerialGC -XX:TieredStopAtLevel=1 -Xss512k -XX:+ExitOnOutOfMemoryError"

RUN addgroup -S app && adduser -S -G app -h /app app
WORKDIR /app

# Least-changing layers first: third-party jars, then the Spring Boot loader, snapshot deps
# (empty for release builds) and finally our own classes and resources.
COPY --from=extract --chown=app:app /extract/extracted/dependencies/ ./
COPY --from=extract --chown=app:app /extract/extracted/spring-boot-loader/ ./
COPY --from=extract --chown=app:app /extract/extracted/snapshot-dependencies/ ./
COPY --from=extract --chown=app:app /extract/extracted/application/ ./

# AppCDS training run: start the application once (H2 in-memory, no external services needed),
# exit right after the context has refreshed, and dump the loaded classes into app.jsa.
# The archive stays root-owned and world-readable; no chown -R (that would duplicate every file
# of the layers above into this one).
RUN java ${JVM_FLAGS} \
        -XX:ArchiveClassesAtExit=/app/app.jsa \
        -Dspring.context.exit=onRefresh \
        -Dspring.main.banner-mode=off \
        -Dlogging.level.root=WARN \
        org.springframework.boot.loader.launch.JarLauncher

USER app

ENV JAVA_TOOL_OPTIONS="-XX:SharedArchiveFile=/app/app.jsa -Xshare:auto ${JVM_FLAGS}"
# Cloud Run injects PORT; application.properties maps server.port=${PORT:8080}.
EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
