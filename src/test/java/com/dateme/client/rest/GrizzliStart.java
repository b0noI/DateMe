package com.dateme.client.rest;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import org.glassfish.grizzly.http.server.HttpServer;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * User: b0noI
 * Date: 17.02.13
 * Time: 19:12
 */
public class GrizzliStart {

    public static final URI BASE_URI = ServerSettings.getUrl();

        protected static HttpServer startServer() throws IOException {
            System.out.println("Starting grizzly...");
            ResourceConfig rc = new PackagesResourceConfig("com.dateme.server");
            return GrizzlyServerFactory.createHttpServer(BASE_URI, rc);
        }

        public static void main(String[] args) throws IOException {
            HttpServer httpServer = startServer();
            System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nTry out %shelloworld\nHit enter to stop it...",
                BASE_URI, BASE_URI));
            System.in.read();
            httpServer.stop();
        }

}
