package com.example.learncrypto.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Quiz extends BaseEntity {

    @OneToMany
    private List<Question> questions;

    @OneToOne
    private User user;

    private int correct;


}
