package com.dateme.twitter;

import org.junit.Before;
import org.junit.Test;

/**
 * User: b0noI
 * Date: 30.03.13
 * Time: 20:27
 */
public class TwitterBotTest {

    private static final long TWITTER_ID = 21795879;

    private TwitterBot twitterBot;

    @Before
    public void setUp() throws Exception {
        twitterBot = TwitterBot.getInstance();
    }

    @Test
    public void testSendNotification() throws Exception {
        twitterBot.sendNotification(TWITTER_ID);
    }
}
