package dev.mstefanov.learncrypto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Question extends BaseEntity {

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false)
    private String optionA;

    @Column(nullable = false)
    private String optionB;

    @Column(nullable = false)
    private String optionC;

    /** Index of the correct option: 1 = A, 2 = B, 3 = C. Never sent to the browser. */
    @Column(nullable = false)
    private int ans;

    public Question(String title, String optionA, String optionB, String optionC, int ans) {
        this.title = title;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.ans = ans;
    }
}
