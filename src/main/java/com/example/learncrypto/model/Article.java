package com.example.learncrypto.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
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
