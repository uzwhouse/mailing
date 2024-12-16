import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class GMailExample {
    private static final String fromUsername = "your_email@gmail.com";
    //     https://myaccount.google.com/security -> app passwords -> custom -> nameForPassword -> generate (TURN ON 2-step Verification)
    private static final String password = "qwertyuiopasdfghjkl";
    private static final String to = "recipient_email@gmail.com";

    public static void main(String[] args) throws Exception {
        Properties properties = getProperties();
        Session session = getSession(properties);
        Message message = getMessage(session);

        message.setSubject("HELLO ");
        message.setContent("Hello World", "text/html");


        Transport.send(message);
        System.out.println("Done");
    }

    private static Message getMessage(Session session) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromUsername));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        return message;
    }

    private static Session getSession(Properties properties) {
        return Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromUsername, password);
            }
        });
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        return properties;
    }


}
