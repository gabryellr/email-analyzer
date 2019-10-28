package com.github.gabryelrock.emailanalyzer.monitor;

import com.github.gabryelrock.emailanalyzer.service.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class EmailMonitor {

    private Mail mail;

    @Autowired
    public EmailMonitor(Mail mail) {
        this.mail = mail;
    }

    @Scheduled(cron = "0 * * ? * *")
    public void getEmailsReceivedToday() {
        mail.getEmailsReceivedToday();
    }

}

