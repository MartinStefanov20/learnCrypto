package dev.mstefanov.learncrypto.model;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Article extends BaseEntity{

    @NotNull
    @Size(max = 45)
    private String name;

    @NotNull
    @Column(length = 4500)
    private String description;

}
