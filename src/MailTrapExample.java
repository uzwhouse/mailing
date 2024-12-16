import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Properties;

public class MailTrapExample {
    private static final String fromUsername = "mailTrapUser";
    private static final String password = "mailTrapPassword";
    private static final String toUsername = "recipient_email@gmail.com";
    private static final String audioPath = "files/music.mp3";
    private static final String videoPath = "files/video.mp4";
    private static final String imagePath = "files/photo.jpg";

    public static void main(String[] args) throws Exception {
        Properties properties = getProperties();
        Session session = getSession(properties);
        Message message = getMessage(session);

        Multipart multipart = new MimeMultipart();
        String audioBody = """
                <h1 style="color:red">MUSIC Body of mail here</h1>
                <audio controls="controls" autoplay="autoplay">
                <source src="data:audio/wav;base64, %s"/>
                </audio>
                """;
        BodyPart audioBodyPart = getBody(audioPath, audioBody);
        multipart.addBodyPart(audioBodyPart);

        String videoBody = """
                <h1 style="color:red">VIDEO Body of mail here</h1>
                <video width="320" height="240" controls>
                <source src="data:video/mp4;base64, %s">
                </video>
                """;
        BodyPart videoBodyPart = getBody(videoPath, videoBody);
        multipart.addBodyPart(videoBodyPart);

        String imageBody = """
                <div>
                <h1 style="color:red">PHOTO Body of mail here</h1>
                <img src="data:image/jpg;base64,%s" width=1000>
                </div>
                """;
        BodyPart imageBodyPart = getBody(imagePath, imageBody);
        multipart.addBodyPart(imageBodyPart);


        message.setContent(multipart);
        Transport.send(message);
        System.out.println("Done");
    }

    private static BodyPart getBody(String filePath, String body) throws Exception {
        BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(body.formatted(getBase64(filePath)), "text/html");
//        file biriktirib yuborish
//        bodyPart.setFileName(filePath.substring(6));
//        bodyPart.setDataHandler(new DataHandler(new FileDataSource(filePath)));
        return bodyPart;
    }

    private static String getBase64(String filePath) throws Exception {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(Files.readAllBytes(Path.of(filePath)));
    }

    private static Message getMessage(Session session) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromUsername));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toUsername));
        message.setSubject("Hello !!!");
        return message;
    }

    private static Session getSession(Properties properties) {
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromUsername, password);
            }
        });
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "sandbox.smtp.mailtrap.io");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        return properties;
    }
}