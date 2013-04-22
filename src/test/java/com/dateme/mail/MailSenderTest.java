package com.dateme.mail;

import com.dateme.client.rest.ProfileClient;
import com.dateme.common.Status;
import com.dateme.common.UserProfile;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

/**
 * User: b0noI
 * Date: 30.03.13
 * Time: 21:59
 */
public class MailSenderTest {

    private static final long TWITTER_ID = 21795879;

    private MailSender mailSender;

    @Before
    public void setUp() throws Exception {
        mailSender = MailSender.getInstance();
        UserProfile userProfile = new UserProfile(TWITTER_ID, "456", "viacheslav@b0noi.com", Locale.US);
        userProfile.addConnection(225, Status.SEX);
        ProfileClient.postProfile(userProfile);
    }

    @Test
    public void testSendNotification() throws Exception {
        mailSender.sendNotification(TWITTER_ID);
    }
}
