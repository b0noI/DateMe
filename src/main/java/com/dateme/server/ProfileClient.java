package com.dateme.server;

import com.dateme.common.Connection;
import com.dateme.common.UserProfile;
import com.dateme.db.DBHelper;
import com.dateme.db.DataBase;
import com.dateme.mail.MailSender;
import com.dateme.twitter.TwitterBot;
import com.google.gson.Gson;
import twitter4j.TwitterException;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * User: b0noI
 * Date: 13.02.13
 * Time: 19:48
 */
@Path("/profile")
public class ProfileClient extends BaseClient{

    @POST
    @Path("/update")
    public Response updateProfile(String jsonUserProfile) {
        UserProfile userProfile = GSON.fromJson(jsonUserProfile, UserProfile.class);
        DB.updateProfile(userProfile, userProfile.getKey());
        DB.log("Staring update process for: " + userProfile.getNick());
//        DB.removeProfile(userProfile.getProfileID(), userProfile.getKey());
        for (Connection connection : userProfile.getProfileConnections()) {
            DB.updateConnection(connection, userProfile.getKey());
            if (connection.getStatuses().size() > 0) {
                DB.log("Adding status for: " + connection.getConnectorId());
                if (!DB.isNotified(connection.getConnectorId())) {
                    DB.log("NOTIFIED");
                    try {
                        TwitterBot.getInstance().sendNotification(connection.getConnectorId());
                        DB.setNotified(connection.getConnectorId());
                    } catch (TwitterException e) {
                        e.printStackTrace();
                        DB.log("notify problem: " + e.toString());
                    } finally {
                        if (MailSender.getInstance().sendNotification(connection.getConnectorId()))
                            DB.setNotified(connection.getConnectorId());
                    }
                }
            }
        }
//        checkMach(userProfile);

        String result = "Profile updates : ";
        return Response.status(201).entity(result).build();
    }

    @Path("/remove")
    @POST
    public Response removeProfile(String jsonUserProfile) {
        UserProfile userProfile = GSON.fromJson(jsonUserProfile, UserProfile.class);
        DB.removeProfile(userProfile.getProfileID(), userProfile.getKey());
        String result = "Profile updates : " + userProfile.getProfileID();
        return Response.status(201).entity(result).build();
    }

    @Path("/get/{id}/{key}")
    @GET
    public String getProfile(@PathParam("id") String id,
                               @PathParam("key") String key) {
        UserProfile userProfile = DB.getProfile(Long.parseLong(id), key);
        return GSON.toJson(userProfile);
    }

    private void checkMach(UserProfile userProfile) {
        for (Connection connection : userProfile.getProfileConnections()) {
            if (connection.isMatch()) {
                // TODO send match information
            }
        }
    }

}
