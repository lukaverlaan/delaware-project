package com.example._026javag03.util.login;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService {

    private static final String USERNAME = "dev.lukavln@gmail.com";
    private static final String PASSWORD = "jkfryubckqyjlwiy";

    public static void stuurWachtwoord(String naarEmail, String wachtwoord) {

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

        try {

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(naarEmail)
            );

            message.setSubject("Uw login gegevens");

            String html = """
                    <html>
                        <body style="font-family: Arial, sans-serif;">
                            <h2>Welkom bij delaware!</h2>
                            
                            <p>Uw tijdelijk wachtwoord is:</p>
                            
                            <p style="font-size:18px;">
                                <b>%s</b>
                            </p>
                            
                            <p>Gelieve dit wachtwoord bij de eerste login te wijzigen.</p>
                        </body>
                    </html>
                    """.formatted(wachtwoord);

            message.setContent(html, "text/html; charset=utf-8");

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}