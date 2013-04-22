package com.dateme.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * User: b0noI
 * Date: 30.03.13
 * Time: 20:00
 */
public class TwitterBot {

    private static final String NOTIFICATION_KEY = "notification";

    private static final String REPLACE_NICK_FIELD = "###NICK###";

    private static final AccessToken ACCESS_TOKEN =
            new AccessToken(ITwitterSettings.USER_TOKEN, ITwitterSettings.USER_SECRET);

    private static final Twitter TWITTER = TwitterFactory.getSingleton();

    private TwitterBot() {
        TWITTER.setOAuthConsumer(ITwitterSettings.CONSUMER_KEY,
                ITwitterSettings.CONSUMER_SECRET);
        TWITTER.setOAuthAccessToken(ACCESS_TOKEN);
    }

    public static TwitterBot getInstance() {
        return InstanceHolder.getInstance();
    }

    public synchronized void sendNotification(long id) throws TwitterException {

        try {
            TWITTER.showUser(id);
        } catch (TwitterException e) {
            return;
        }
        String notification = getNotificationMessage();
        notification = notification.replace(REPLACE_NICK_FIELD, getUserNick(id));
        TWITTER.updateStatus(notification);
    }

    public String getUserNick(long id) throws TwitterException {
        return TWITTER.showUser(id).getScreenName();
    }

    private String getNotificationMessage() {
        ResourceBundle resource =
                ResourceBundle.getBundle("com.dateme.bundles.notification", Locale.US);
        return resource.getString(NOTIFICATION_KEY);
    }

    private static class InstanceHolder {

        private static TwitterBot INSTANCE = new TwitterBot();

        public static TwitterBot getInstance() {
            return InstanceHolder.INSTANCE;
        }

    }

}
