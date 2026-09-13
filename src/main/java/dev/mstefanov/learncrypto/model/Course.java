package dev.mstefanov.learncrypto.model;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Course extends BaseEntity{

    @NotNull
    @Size(max = 45)
    private String title;

    @NotNull
    @Column(length = 4500)
    private String description;

    @OneToMany
    private List<Article> articles;
}
