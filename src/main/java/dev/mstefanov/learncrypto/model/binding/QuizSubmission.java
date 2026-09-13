package dev.mstefanov.learncrypto.model.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/** Form backing object for the quiz: one entry per displayed question. */
@Getter
@Setter
@NoArgsConstructor
public class QuizSubmission {

    private List<QuizAnswer> answers = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    public static class QuizAnswer {
        private Long questionId;
        /** 1 = A, 2 = B, 3 = C; null when the user skipped the question. */
        private Integer choice;
    }
}
