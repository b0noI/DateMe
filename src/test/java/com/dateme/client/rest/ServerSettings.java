package com.dateme.client.rest;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * User: b0noI
 * Date: 17.02.13
 * Time: 18:57
 */
class ServerSettings {

    private static final boolean isLocalMode = false;

    private static final String SERVER_URL = "http://dateme.j.rsnx.ru/REST";

    private static final int PORT = 9996;

    public static URI getUrl(){
        if (isLocalMode)
            return UriBuilder.fromUri("http://localhost").port(PORT).build();
        else
            return UriBuilder.fromUri(SERVER_URL).build();
    }

}
