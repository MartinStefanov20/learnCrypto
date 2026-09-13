package dev.mstefanov.learncrypto.web;

import dev.mstefanov.learncrypto.model.binding.QuizSubmission;
import dev.mstefanov.learncrypto.service.QuizResult;
import dev.mstefanov.learncrypto.service.QuizService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/quiz")
    public String makeQuizForUser(Model model) {
        model.addAttribute("questions", quizService.makeQuiz());
        return "quiz";
    }

    @PostMapping("/submit")
    public String finishQuiz(@ModelAttribute QuizSubmission submission, Principal principal, Model model) {
        QuizResult result = quizService.score(submission);

        model.addAttribute("username", principal.getName());
        model.addAttribute("result", result);
        return "result";
    }

    /** Direct GET on /result (e.g. after a refresh) just starts a new quiz. */
    @GetMapping("/result")
    public String result() {
        return "redirect:/quiz";
    }
}
