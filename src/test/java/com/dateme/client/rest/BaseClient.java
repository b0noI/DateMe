package com.dateme.client.rest;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;

/**
 * User: b0noI
 * Date: 17.02.13
 * Time: 18:58
 */
class BaseClient {

    protected static final Client CLIENT = Client.create();

    protected static final Gson GSON = new Gson();

}
