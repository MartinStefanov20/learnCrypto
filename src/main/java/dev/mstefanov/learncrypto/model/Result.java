package dev.mstefanov.learncrypto.model;

import lombok.*;

import jakarta.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Result extends BaseEntity{

    private int totalCorrect;
}
