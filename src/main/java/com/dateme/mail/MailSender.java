package com.dateme.mail;

import com.dateme.common.UserProfile;
import com.dateme.db.DBHelper;
import com.dateme.twitter.TwitterBot;
import twitter4j.TwitterException;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * User: b0noI
 * Date: 30.03.13
 * Time: 21:25
 */
public class MailSender {

    private static final String SUBJECT_KEY = "mail.subject";

    private static final String BODY_KEY = "mail.body";

    private static final String REPLACE_NICK_FIELD = "###NICK###";

    private static final DBHelper DB_HELPER = DBHelper.getInstance();

    private MailSender() { }

    public static MailSender getInstance() {
        return InstanceHolder.getInstance();
    }

    public boolean sendNotification(long id) {
        UserProfile userProfile = DB_HELPER.getProfile(id);
        if (userProfile == null || userProfile.mail == null)
            return false;
        String mail = userProfile.mail;
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.yandex.ru");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");

        Authenticator auth = new SMTPAuthenticator();
        Session session = Session.getDefaultInstance(props, auth);
        Message simpleMessage = new MimeMessage(session);

        InternetAddress fromAddress = null;
        InternetAddress toAddress = null;
        try {
            fromAddress = new InternetAddress(IMailSettings.FROM);
            toAddress = new InternetAddress(mail);
        } catch (AddressException e) {
            e.printStackTrace();
            DB_HELPER.log(e.toString());
        }

        try {
            simpleMessage.setFrom(fromAddress);
            simpleMessage.setRecipient(Message.RecipientType.TO, toAddress);
            simpleMessage.setSubject(getSubject());
            simpleMessage.setText(getBody(userProfile.getProfileID()));

            Transport.send(simpleMessage);
        } catch (MessagingException e) {
            DB_HELPER.log(e.toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String getSubject() {
        ResourceBundle resource =
                ResourceBundle.getBundle("com.dateme.bundles.notification", Locale.US);
        return resource.getString(SUBJECT_KEY);
    }

    private String getBody(long id) {
        String nick = "";
        try {
            nick = TwitterBot.getInstance().getUserNick(id);
        } catch (TwitterException e) {
            e.printStackTrace();
            DB_HELPER.log(e.toString());
        }
        ResourceBundle resource =
                ResourceBundle.getBundle("com.dateme.bundles.notification", Locale.US);
        return resource.getString(BODY_KEY).replace(REPLACE_NICK_FIELD, nick);
    }

    private static class InstanceHolder {

        private static MailSender INSTANCE = new MailSender();

        public static MailSender getInstance() {
            return INSTANCE;
        }

    }

    private class SMTPAuthenticator extends javax.mail.Authenticator
    {

        public PasswordAuthentication getPasswordAuthentication()
        {
            String username = IMailSettings.LOGIN;
            String password = IMailSettings.PASSWORD;
            return new PasswordAuthentication(username, password);
        }

    }

}
