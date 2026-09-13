package dev.mstefanov.learncrypto.service;

import dev.mstefanov.learncrypto.model.Question;
import dev.mstefanov.learncrypto.model.binding.QuizSubmission;
import dev.mstefanov.learncrypto.repository.QuestionRepository;
import dev.mstefanov.learncrypto.service.impl.QuizServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.when;

/**
 * Pure unit test of {@link QuizServiceImpl#score(QuizSubmission)}; the repository is mocked.
 */
@ExtendWith(MockitoExtension.class)
class QuizScoringTest {

    private static final Question Q1 = question(1L, 2);
    private static final Question Q2 = question(2L, 1);
    private static final Question Q3 = question(3L, 3);

    @Mock
    private QuestionRepository questionRepository;

    private QuizServiceImpl quizService;

    @BeforeEach
    void setUp() {
        quizService = new QuizServiceImpl(questionRepository);
        // Return whichever of the three known questions were asked for; unknown ids are simply absent.
        when(questionRepository.findAllById(anyIterable())).thenAnswer(invocation -> {
            Iterable<Long> ids = invocation.getArgument(0);
            List<Long> requested = new ArrayList<>();
            ids.forEach(requested::add);
            return Stream.of(Q1, Q2, Q3).filter(q -> requested.contains(q.getId())).toList();
        });
    }

    @Test
    void allCorrect() {
        QuizResult result = quizService.score(submission(answer(1L, 2), answer(2L, 1), answer(3L, 3)));

        assertThat(result.correct()).isEqualTo(3);
        assertThat(result.total()).isEqualTo(3);
        assertThat(result.wrong()).isZero();
    }

    @Test
    void allWrong() {
        QuizResult result = quizService.score(submission(answer(1L, 1), answer(2L, 3), answer(3L, 2)));

        assertThat(result.correct()).isZero();
        assertThat(result.total()).isEqualTo(3);
        assertThat(result.wrong()).isEqualTo(3);
    }

    @Test
    void skippedQuestionCountsAsWrong() {
        QuizResult result = quizService.score(submission(answer(1L, 2), answer(2L, null), answer(3L, 3)));

        assertThat(result.correct()).isEqualTo(2);
        assertThat(result.total()).isEqualTo(3);
        assertThat(result.wrong()).isEqualTo(1);
    }

    @Test
    void unknownQuestionIdIsIgnored() {
        QuizResult result = quizService.score(submission(answer(1L, 2), answer(999L, 1)));

        assertThat(result.correct()).isEqualTo(1);
        assertThat(result.total()).isEqualTo(1);
        assertThat(result.wrong()).isZero();
    }

    @Test
    void emptySubmissionScoresZeroOfZero() {
        assertThat(quizService.score(new QuizSubmission())).isEqualTo(new QuizResult(0, 0));

        QuizSubmission nullAnswers = new QuizSubmission();
        nullAnswers.setAnswers(null);
        assertThat(quizService.score(nullAnswers)).isEqualTo(new QuizResult(0, 0));
    }

    private static Question question(long id, int correctOption) {
        Question q = new Question("Q" + id, "A", "B", "C", correctOption);
        q.setId(id);
        return q;
    }

    private static QuizSubmission submission(QuizSubmission.QuizAnswer... answers) {
        QuizSubmission s = new QuizSubmission();
        s.setAnswers(List.of(answers));
        return s;
    }

    private static QuizSubmission.QuizAnswer answer(Long questionId, Integer choice) {
        QuizSubmission.QuizAnswer a = new QuizSubmission.QuizAnswer();
        a.setQuestionId(questionId);
        a.setChoice(choice);
        return a;
    }
}
