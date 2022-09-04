package com.example.learncrypto.service.impl;

import com.example.learncrypto.model.Question;
import com.example.learncrypto.model.Quiz;
import com.example.learncrypto.model.User;
import com.example.learncrypto.repository.QuestionRepository;
import com.example.learncrypto.repository.QuizRepository;
import com.example.learncrypto.repository.ResultRepository;
import com.example.learncrypto.service.QuizService;
import com.example.learncrypto.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final ResultRepository resultRepository;
    private final UserService userService;

    public QuizServiceImpl(QuizRepository quizRepository, QuestionRepository questionRepository, ResultRepository resultRepository, UserService userService) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.resultRepository = resultRepository;
        this.userService = userService;
    }


    @Override
    public List<Question> getAllQuestions() {
        return this.questionRepository.findAll();
    }

    @Override
    public Quiz makeQuiz() {
        List<Question> allQuestions = this.getAllQuestions();
        List<Question> questionForQuiz = new ArrayList<Question>();

        Random random = new Random();

        for(int i=0; i<5; i++) {
            int rand = random.nextInt(allQuestions.size());
            questionForQuiz.add(allQuestions.get(rand));
            allQuestions.remove(rand);
        }

        Quiz quiz = new Quiz();

        quiz.setQuestions(questionForQuiz);

        return quiz;
    }

    @Override
    public Quiz findOneById(Long id) {
        return quizRepository.findOneById(id);
    }

    @Override
    public Quiz findQuizForUserWithUsername(String name){

        return this.quizRepository.findOneByUserUsername(name);
    }

    @Override
    public Integer getResult(Long id) {
        int correct = 0;

        Quiz quiz = this.findOneById(id);

        for(Question question: quiz.getQuestions())
            if(question.getAns() == question.getChoice())
                correct++;

        return correct;
    }


    @Override
    public void saveQuiz(Quiz quiz, String username) {
        User user = this.userService.getUserByUsername(username);
        quiz.setUser(user);
        this.quizRepository.save(quiz);
    }

    @Override
    public void setResult(Long id, Integer score){
        this.quizRepository.findOneById(id).setCorrect(score);
    }


    @Override
    public List<Quiz> getTopScore() {

        List<Quiz> quizes = quizRepository.findAll(Sort.by(Sort.Direction.DESC, "correct"));

        return quizes;
    }

    @Override
    public void initQuestions(){
        Question question1 = new Question();
        question1.setTitle("Who created the cryptocurrency Ethereum?");
        question1.setOptionA("Satoshi Nakamoto");
        question1.setOptionB("Vitalik Buterin");
        question1.setOptionC("Andreas Antonopoulos");
        question1.setAns(1);

        Question question2 = new Question();
        question2.setTitle("Who created the cryptocurrency Ethereum?");
        question2.setOptionA("Satoshi Nakamoto");
        question2.setOptionB("Vitalik Buterin");
        question2.setOptionC("Andreas Antonopoulos");
        question2.setAns(1);

        Question question3 = new Question();
        question3.setTitle("Who created the cryptocurrency Ethereum?");
        question3.setOptionA("Satoshi Nakamoto");
        question3.setOptionB("Vitalik Buterin");
        question3.setOptionC("Andreas Antonopoulos");
        question3.setAns(1);

        Question question4 = new Question();
        question4.setTitle("Who created the cryptocurrency Ethereum?");
        question4.setOptionA("Satoshi Nakamoto");
        question4.setOptionB("Vitalik Buterin");
        question4.setOptionC("Andreas Antonopoulos");
        question4.setAns(1);

        Question question5 = new Question();
        question5.setTitle("Who created the cryptocurrency Ethereum?");
        question5.setOptionA("Satoshi Nakamoto");
        question5.setOptionB("Vitalik Buterin");
        question5.setOptionC("Andreas Antonopoulos");
        question5.setAns(1);

        Question question6 = new Question();
        question6.setTitle("Who created the cryptocurrency Ethereum?");
        question6.setOptionA("Satoshi Nakamoto");
        question6.setOptionB("Vitalik Buterin");
        question6.setOptionC("Andreas Antonopoulos");
        question6.setAns(1);

        questionRepository.saveAll(List.of(question1, question2, question3, question4, question5, question6));

    }
}
