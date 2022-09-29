package com.example.learncrypto.web;

import com.example.learncrypto.model.Quiz;
import com.example.learncrypto.service.QuizService;
import com.example.learncrypto.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class QuizController {

    private final QuizService quizService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public QuizController(QuizService quizService, UserService userService, ModelMapper modelMapper) {
        this.quizService = quizService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/quiz")
    public String makeQuizForUser(Model model) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Quiz quiz = this.quizService.makeQuiz();

        model.addAttribute("quiz", quiz);

//        quizService.saveQuiz(quiz, username);

        return "quiz";
    }

    @PostMapping("/submit")
    public String finishQuiz(@ModelAttribute Quiz quiz, Model model) {

//        Integer correct = quizService.getResult(quiz.getId());
//        quizService.setResult(quiz.getId(), correct);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);

        return "result";
    }

    @GetMapping("/score")
    public String score(Model model) {
        List<Quiz> quizes = quizService.getTopScore();
        model.addAttribute("quizes", quizes);

        return "scoreboard.html";
    }





}
