package dev.mstefanov.learncrypto.service;

/** Outcome of a graded quiz. */
public record QuizResult(int correct, int total) {

    public int wrong() {
        return total - correct;
    }
}
