package dev.mstefanov.learncrypto.service;

import dev.mstefanov.learncrypto.model.Question;
import dev.mstefanov.learncrypto.model.binding.QuizSubmission;

import java.util.List;

public interface QuizService {

    List<Question> getAllQuestions();

    /** Picks a random subset of questions for one quiz round. */
    List<Question> makeQuiz();

    /** Grades a submission server-side against the stored answers. */
    QuizResult score(QuizSubmission submission);

    void initQuestions();
}
