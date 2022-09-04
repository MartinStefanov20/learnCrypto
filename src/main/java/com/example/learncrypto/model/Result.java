package com.example.learncrypto.model;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Result extends BaseEntity{

    private int totalCorrect;
}
