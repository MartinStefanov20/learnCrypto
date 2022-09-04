package com.example.learncrypto.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
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
