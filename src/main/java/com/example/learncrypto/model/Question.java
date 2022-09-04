package com.example.learncrypto.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
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
