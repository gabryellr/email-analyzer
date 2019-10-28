package com.github.gabryelrock.emailanalyzer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

@Component
public class EmailProperties {

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

    public Store configure() {

        Store store = null;
        Properties properties = System.getProperties();
        properties.setProperty(propsPropertyServer, propsPropertyServerProtocol);

        Session session = Session.getDefaultInstance(properties, null);

        try {
            store = session.getStore(propsPropertyServerProtocol);
            store.connect("imap.googlemail.com", protocolGoogleUser, protocolGooglePassword);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return store;

    }
}
