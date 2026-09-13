package dev.mstefanov.learncrypto;

import dev.mstefanov.learncrypto.service.CourseService;
import dev.mstefanov.learncrypto.service.QuizService;
import dev.mstefanov.learncrypto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInit implements CommandLineRunner {

    private final UserService userService;
    private final CourseService courseService;
    private final QuizService quizService;

    @Autowired
    public ApplicationInit(UserService userService, CourseService courseService, QuizService quizService) {
        this.userService = userService;
        this.courseService = courseService;
        this.quizService = quizService;
    }

    @Override
    public void run(String... args) throws Exception {
        userService.initUsers();
        courseService.initCourses();
        quizService.initQuestions();
    }
}
