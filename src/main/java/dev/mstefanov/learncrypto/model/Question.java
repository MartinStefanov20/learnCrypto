package dev.mstefanov.learncrypto.model;

import lombok.*;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Question extends BaseEntity {

    private String title;

    private String optionA;

    private String optionB;

    private String optionC;

    private int ans;

    private int choice;

}
