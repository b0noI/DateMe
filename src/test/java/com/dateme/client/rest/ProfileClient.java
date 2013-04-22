package com.dateme.client.rest;

import com.dateme.common.UserProfile;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * User: b0noI
 * Date: 17.02.13
 * Time: 18:58
 */
public class ProfileClient extends BaseClient {

    private static final String POST_PROFILE_URL = ServerSettings.getUrl() + "/profile/update";

    private static final String REMOVE_PROFILE_URL = ServerSettings.getUrl() + "/profile/remove";

    private static final String GET_PROFILE_URL = ServerSettings.getUrl() + "/profile/get/%ID%/%KEY%";

    public static void postProfile(UserProfile userProfile){
        post(POST_PROFILE_URL, userProfile);
    }

    public static void removeProfile(UserProfile userProfile){
        post(REMOVE_PROFILE_URL, userProfile);
    }

    public static UserProfile getUserProfile(UserProfile userProfile){

        WebResource webResource = CLIENT
                .resource(GET_PROFILE_URL
                        .replace("%ID%", String.valueOf(userProfile.getProfileID()))
                        .replace("%KEY%", userProfile.getKey()));

        ClientResponse response = webResource.accept("application/json")
                .get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        String output = response.getEntity(String.class);
        userProfile = GSON.fromJson(output, UserProfile.class);
        System.out.println("Output from Server .... \n");
        System.out.println(output);
        return userProfile;
    }

    private static void post(String url, UserProfile userProfile){
        WebResource webResource = CLIENT
                .resource(url);

        String input = GSON.toJson(userProfile);

        ClientResponse response = webResource
                .post(ClientResponse.class, input);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        System.out.println("Output from Server .... \n");
        String output = response.getEntity(String.class);
        System.out.println(output);
    }

}
