package com.ikkerens.filecaptest.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Path("/")
public class UIResource {
    @GET
    @Produces("text/html")
    public String hello() {
        // Simply serve index.html from the WAR resources
        final String content;
        try (final InputStream is = getClass().getResourceAsStream("/index.html")) {
            if (is == null)
                throw new IOException("Internal resource not found");

            try (Scanner scanner = new Scanner(is, StandardCharsets.UTF_8).useDelimiter("\\A")) {
                content = scanner.hasNext() ? scanner.next() : "";
            }
        } catch (final IOException e) {
            throw new WebApplicationException("Internal server error occurred", e, Response.Status.INTERNAL_SERVER_ERROR);
        }
        return content;
    }
}