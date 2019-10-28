package com.github.gabryelrock.emailanalyzer.service;

import com.github.gabryelrock.emailanalyzer.config.EmailProperties;
import com.github.gabryelrock.emailanalyzer.model.Email;
import com.github.gabryelrock.emailanalyzer.repository.MailRepository;
import com.sun.mail.imap.IMAPFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class Mail {

    @Value("${props.property.server}")
    private String propsPropertyServer;

    @Value("${props.property.server.protocol}")
    private String propsPropertyServerProtocol;

    @Value("${protocol.google.user}")
    private String protocolGoogleUser;

    @Value("${protocol.google.password}")
    private String protocolGooglePassword;

    @Value("${store.folder}")
    private String folder;

    private IMAPFolder imapFolder;
    private Store store;
    private MailRepository mailRepository;

    @Autowired
    public Mail(EmailProperties emailProperties, MailRepository mailRepository) {
        store = emailProperties.configure();
        this.mailRepository = mailRepository;
    }

    public List<Email> getEmailsReceivedToday() {
        List<Email> listOfEmailsReceived = new ArrayList<>();

        try {

            imapFolder = (IMAPFolder) store.getFolder(folder);

            LocalDateTime startFilter = LocalDateTime.now().toLocalDate().atStartOfDay();
            Date date = Date.from(startFilter.atZone(ZoneId.systemDefault()).toInstant());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            Date endDate = new Date();
            calendar.setTime(endDate);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);

            ReceivedDateTerm term = new ReceivedDateTerm(ComparisonTerm.EQ, new Date(calendar.getTimeInMillis()));

            if (!imapFolder.isOpen())
                imapFolder.open(Folder.READ_WRITE);
            Message[] messages = imapFolder.search(term);
            int cont = 1;

            for (int i = messages.length - 1; i >= 0; i--) {

                Message msg = messages[i];

                if (!msg.getFlags().contains(Flags.Flag.SEEN)) {

                    Email email = Email.builder()
                            .subject(msg.getSubject())
                            .from(msg.getFrom()[0])
                            .to(msg.getAllRecipients()[0])
                            .dateReceived(msg.getReceivedDate())
                            .build();

                    listOfEmailsReceived.add(email);
                    msg.setFlag(Flags.Flag.SEEN, true);
                }
                mailRepository.saveEmailsToday(listOfEmailsReceived.size());

            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return listOfEmailsReceived;
    }
}
