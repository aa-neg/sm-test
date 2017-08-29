package model;

import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Required;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cz.jirutka.validator.collection.constraints.EachPattern;
import lombok.Data;
import model.Email;

@Data
public class SendGridPersonalizations {
    public List<Email> to;

    public List<Email> cc;

    public List<Email> bcc;

    public String subject;
}