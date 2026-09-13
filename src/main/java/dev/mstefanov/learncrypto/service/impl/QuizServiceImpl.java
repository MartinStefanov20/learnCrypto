package dev.mstefanov.learncrypto.service.impl;

import dev.mstefanov.learncrypto.model.Question;
import dev.mstefanov.learncrypto.model.binding.QuizSubmission;
import dev.mstefanov.learncrypto.repository.QuestionRepository;
import dev.mstefanov.learncrypto.service.QuizResult;
import dev.mstefanov.learncrypto.service.QuizService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService {

    /** Number of questions shown per quiz round. */
    static final int QUESTIONS_PER_QUIZ = 5;

    private final QuestionRepository questionRepository;

    public QuizServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public List<Question> getAllQuestions() {
        return this.questionRepository.findAll();
    }

    @Override
    public List<Question> makeQuiz() {
        List<Question> all = new ArrayList<>(getAllQuestions());
        Collections.shuffle(all);
        return all.subList(0, Math.min(QUESTIONS_PER_QUIZ, all.size()));
    }

    @Override
    @Transactional(readOnly = true)
    public QuizResult score(QuizSubmission submission) {
        List<QuizSubmission.QuizAnswer> answers = submission.getAnswers() == null
                ? List.of() : submission.getAnswers();

        List<Long> ids = answers.stream()
                .map(QuizSubmission.QuizAnswer::getQuestionId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<Long, Question> byId = questionRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Question::getId, Function.identity()));

        int correct = 0;
        for (QuizSubmission.QuizAnswer answer : answers) {
            Question question = byId.get(answer.getQuestionId());
            if (question != null && answer.getChoice() != null && answer.getChoice() == question.getAns()) {
                correct++;
            }
        }
        return new QuizResult(correct, byId.size());
    }

    @Override
    public void initQuestions() {
        if (questionRepository.count() > 0) {
            return;
        }
        questionRepository.saveAll(List.of(
                new Question("Who created the cryptocurrency Ethereum?",
                        "Satoshi Nakamoto", "Vitalik Buterin", "Andreas Antonopoulos", 2),
                new Question("What is the maximum number of bitcoins that will ever exist?",
                        "21 million", "100 million", "There is no limit", 1),
                new Question("What is a blockchain?",
                        "A type of bank account", "A shared ledger of transactions grouped into linked blocks",
                        "A programming language for smart contracts", 2),
                new Question("What does a crypto wallet's private key do?",
                        "Proves ownership and signs transactions", "Shows your balance to others",
                        "Speeds up the network", 1),
                new Question("What is a stablecoin?",
                        "A coin whose price is fixed by mining difficulty",
                        "A coin designed to track the value of an asset such as the US dollar",
                        "A coin that cannot be sold", 2),
                new Question("What is the process of adding new blocks to the Bitcoin blockchain called?",
                        "Staking", "Minting", "Mining", 3),
                new Question("Under which name did the author of the Bitcoin whitepaper publish it?",
                        "Hal Finney", "Satoshi Nakamoto", "Nick Szabo", 2),
                new Question("What is a smart contract?",
                        "A legal contract signed online", "A program that runs on a blockchain when its conditions are met",
                        "An exchange trading fee agreement", 2)
        ));
    }
}
