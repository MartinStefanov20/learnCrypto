package com.example.learncrypto.model;

import com.example.learncrypto.utils.roleValidator.ValidateRole;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static com.example.learncrypto.messages.ValidationErrorMessages.INVALID_ROLE;

@Data
@Entity

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Role extends BaseEntity{

    @NotNull
    @ValidateRole(acceptedValues={"ROLE_USER", "ROLE_ADMIN"}, message = INVALID_ROLE)
    private String name;
    @NotNull
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;
}
