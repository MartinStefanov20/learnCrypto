package dev.mstefanov.learncrypto.model;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.util.List;

import static dev.mstefanov.learncrypto.messages.ValidationErrorMessages.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="users")
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    @Size(min = 4, max = 30, message = INCORRECT_USERNAME_LENGTH)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    @Size(min = 3, max = 20, message = INCORRECT_FIRST_NAME_LENGTH)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Size(min = 3, max = 20, message = INCORRECT_LAST_NAME_LENGTH)
    private String lastName;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private List<Role> roles;
}
