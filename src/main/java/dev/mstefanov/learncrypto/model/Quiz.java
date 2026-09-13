package dev.mstefanov.learncrypto.model;

import lombok.*;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;

@Entity
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
