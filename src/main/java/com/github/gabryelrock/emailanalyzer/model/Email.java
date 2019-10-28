package com.github.gabryelrock.emailanalyzer.model;

import lombok.*;

import javax.mail.Address;
import java.util.Date;

@Getter
@Builder
public class Email {

    private String subject;
    private Address from;
    private Address to;
    private Date dateReceived;

}
