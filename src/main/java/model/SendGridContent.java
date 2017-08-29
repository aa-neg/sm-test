package model;

import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class SendGridContent {
    //Only support plain text in this iteration
    public String type = "text/plain";
    public String value;

    public SendGridContent(String value) {
        this.value = value;
    }
}