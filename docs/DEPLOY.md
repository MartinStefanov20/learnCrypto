# Deploying to Google Cloud Run

Learn Crypto is a single stateless container: Spring Boot with an **in-memory H2 database** and an
in-process Caffeine cache. There is no database, bucket or queue to provision. The only optional
input is a CoinGecko demo API key.

> **State resets on every cold start.** Demo accounts, seeded courses and quiz questions are
> recreated at boot; users who register on the live demo disappear when the instance is recycled
> (`--min-instances 0`). That is intended for a portfolio demo, not for real user data.

## Prerequisites

- A GCP project with billing enabled and the `gcloud` CLI authenticated (`gcloud auth login`).
- APIs: `gcloud services enable run.googleapis.com artifactregistry.googleapis.com cloudbuild.googleapis.com`
- Region used throughout: `europe-west3` (Frankfurt).

## 1. First manual deploy (build from source)

The quickest path lets Cloud Build build the `Dockerfile` in this repository and deploy it:

```bash
gcloud config set project YOUR_PROJECT_ID
gcloud run deploy learncrypto \
  --source . \
  --region europe-west3 \
  --allow-unauthenticated \
  --port 8080 \
  --memory 512Mi --cpu 1 --cpu-boost \
  --min-instances 0 --max-instances 1 \
  --set-env-vars COINGECKO_API_KEY=      # optional, leave empty for the public endpoint
```

The command prints the service URL. Check it:

```bash
URL=$(gcloud run services describe learncrypto --region europe-west3 --format 'value(status.url)')
curl -fsS "$URL/actuator/health"   # {"status":"UP"}
```

The image starts in roughly 1.3 s inside the container thanks to the AppCDS archive baked into
the image, so `--min-instances 0` (scale to zero, no idle cost) is comfortable together with
`--cpu-boost`.

## 2. Automated deploys from GitHub Actions

`.github/workflows/deploy-cloud-run.yml` builds the image, pushes it to Artifact Registry and
deploys on every push to `main` that touches `src/**`, `pom.xml` or the `Dockerfile`. It uses
**Workload Identity Federation**, so no service-account key is stored in GitHub.

One-time setup:

```bash
# Artifact Registry repository for the images
gcloud artifacts repositories create apps --repository-format docker --location europe-west3

# Deployer service account
gcloud iam service-accounts create github-deployer
SA=github-deployer@YOUR_PROJECT_ID.iam.gserviceaccount.com
for role in roles/run.admin roles/artifactregistry.writer roles/iam.serviceAccountUser; do
  gcloud projects add-iam-policy-binding YOUR_PROJECT_ID --member "serviceAccount:$SA" --role "$role"
done

# Workload Identity pool + GitHub OIDC provider (restricted to this repository)
gcloud iam workload-identity-pools create github --location global
gcloud iam workload-identity-pools providers create-oidc github \
  --location global --workload-identity-pool github \
  --issuer-uri https://token.actions.githubusercontent.com \
  --attribute-mapping "google.subject=assertion.sub,attribute.repository=assertion.repository" \
  --attribute-condition "assertion.repository == 'MartinStefanov20/learnCrypto'"
PROJECT_NUMBER=$(gcloud projects describe YOUR_PROJECT_ID --format 'value(projectNumber)')
gcloud iam service-accounts add-iam-policy-binding "$SA" \
  --role roles/iam.workloadIdentityUser \
  --member "principalSet://iam.googleapis.com/projects/$PROJECT_NUMBER/locations/global/workloadIdentityPools/github/attribute.repository/MartinStefanov20/learnCrypto"
```

GitHub repository secrets:

| Secret                | Value                                                                                   |
|-----------------------|-----------------------------------------------------------------------------------------|
| `GCP_PROJECT`         | `YOUR_PROJECT_ID`                                                                       |
| `WIF_PROVIDER`        | `projects/PROJECT_NUMBER/locations/global/workloadIdentityPools/github/providers/github` |
| `WIF_SERVICE_ACCOUNT` | `github-deployer@YOUR_PROJECT_ID.iam.gserviceaccount.com`                               |
| `COINGECKO_API_KEY`   | optional; leave unset to use the public CoinGecko endpoint                               |

Create a GitHub *environment* named `production` (the workflow references it) if you want
manual approval before deploys.

## Runtime configuration

| Variable             | Default                              | Purpose                                            |
|----------------------|--------------------------------------|----------------------------------------------------|
| `PORT`               | `8080`                               | Injected by Cloud Run; mapped to `server.port`.    |
| `COINGECKO_API_KEY`  | empty                                | Adds the `x-cg-demo-api-key` header when set.      |
| `COINGECKO_BASE_URL` | `https://api.coingecko.com/api/v3`   | Override for tests or a proxy.                     |
| `JAVA_TOOL_OPTIONS`  | set in the `Dockerfile`              | AppCDS archive, SerialGC, 70 % RAM, 512 KiB stacks. |

Do **not** activate the `local` Spring profile in production: it exposes the H2 web console.

## Enabling automatic deploys

Add the `GCP_PROJECT`, `WIF_PROVIDER` and `WIF_SERVICE_ACCOUNT` repository secrets, then set the repository variable `CLOUD_RUN_DEPLOY=true` (Settings → Secrets and variables → Actions → Variables). Until then the deploy job is skipped.
