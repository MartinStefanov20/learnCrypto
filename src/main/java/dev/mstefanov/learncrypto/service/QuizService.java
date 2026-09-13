package dev.mstefanov.learncrypto.service;

import dev.mstefanov.learncrypto.model.Question;
import dev.mstefanov.learncrypto.model.Quiz;

import java.util.List;

public interface QuizService {

    List<Question> getAllQuestions();

    Quiz findQuizForUserWithUsername(String name);

    Quiz makeQuiz();

    Quiz findOneById(Long id);

    Integer getResult(Long id);

    void saveQuiz(Quiz quiz, String username);

    List<Quiz> getTopScore();

    void setResult(Long id, Integer score);

    void initQuestions();
}
