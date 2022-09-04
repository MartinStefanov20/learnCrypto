package com.example.learncrypto;

import com.example.learncrypto.service.CourseService;
import com.example.learncrypto.service.QuizService;
import com.example.learncrypto.service.UserService;
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
