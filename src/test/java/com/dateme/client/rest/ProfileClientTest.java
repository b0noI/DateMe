package com.dateme.client.rest;

import com.dateme.common.Status;
import com.dateme.common.UserProfile;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertNotNull;

/**
 * User: b0noI
 * Date: 17.02.13
 * Time: 19:04
 */
public class ProfileClientTest {

    @Test
    public void testPostProfile() throws Exception {
        UserProfile userProfile = new UserProfile(1223,"4562", "ds2@asf.com", Locale.US);
        userProfile.addConnection(225, Status.SEX);
        ProfileClient.postProfile(userProfile);
    }

    @Test
    public void testRemoveProfile() throws Exception {
        UserProfile userProfile = new UserProfile(1224,"43562", "ds2@asf.com", Locale.US);
        userProfile.addConnection(225, Status.SEX);
        ProfileClient.postProfile(userProfile);
        ProfileClient.removeProfile(userProfile);
    }

    @Test
    public void testGetProfile() throws Exception {
        UserProfile userProfile = new UserProfile(1223,"456", "ds@asf.com", Locale.US);
        userProfile.addConnection(225, Status.SEX);
        ProfileClient.postProfile(userProfile);
        UserProfile newUserProfile = ProfileClient.getUserProfile(userProfile);
        assertNotNull(newUserProfile);
    }

    @Test
    public void testAddConnection() throws Exception {
        UserProfile userProfile = new UserProfile(421345274,"Zw8Vj0pZ", "ds@asf.com", Locale.US);
        UserProfile myProfile = new UserProfile(21795879,"Zw8Vj0pZ", "viacheslav@b0noi.com", Locale.US);
        ProfileClient.postProfile(myProfile);
        userProfile.addConnection(21795879, Status.TALK);
        ProfileClient.postProfile(userProfile);
        ProfileClient.removeProfile(myProfile);
    }

}
