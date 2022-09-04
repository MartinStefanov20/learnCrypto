package com.example.learncrypto.service;

import com.example.learncrypto.model.Question;
import com.example.learncrypto.model.Quiz;

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
