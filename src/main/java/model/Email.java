package model;

import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class Email {
    //Very basic email validation
    @NotNull
    @Pattern(regexp = "[\\w.-]+@[a-zA-Z.]+")
    public String email;

    public Email(String email) {
        this.email = email;
    }

    public Email() {
    }
}