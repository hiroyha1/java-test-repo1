package com.example;

import java.io.IOException;
import java.util.Properties;

import com.microsoft.graph.models.User;

// https://learn.microsoft.com/en-us/graph/tutorials/java?tabs=aad
public class Main {
    public static void main(String[] args) {
        final Properties oAuthProperties = new Properties();
        try {
            oAuthProperties.load(Main.class.getResourceAsStream("oAuth.properties"));
        } catch (IOException e) {
            System.out.println("Unable to read OAuth configuration. Make sure you have a properly formatted oAuth.properties file. See README for details.");
            return;
        }

        initializeGraph(oAuthProperties);
        displayAccessToken();
        greetUser();
    }

    private static void initializeGraph(Properties properties) {
        try {
            Graph.initializeGraphForUserAuth(properties, challenge -> System.out.println(challenge.getMessage()));
        } catch (Exception e)
        {
            System.out.println("Error initializing Graph for user auth");
            System.out.println(e.getMessage());
        }
    }

    private static void displayAccessToken() {
        try {
            final String accessToken = Graph.getUserToken();
            System.out.println("Access token: " + accessToken);
        } catch (Exception e) {
            System.out.println("Error getting access token");
            System.out.println(e.getMessage());
        }
    }

    private static void greetUser() {
        try {
            final User user = Graph.getUser();
            final String email = user.mail == null ? user.userPrincipalName : user.mail;
            System.out.println("Hello, " + user.displayName + "!");
            System.out.println("Email: " + email);
        } catch (Exception e) {
            System.out.println("Error getting user");
            System.out.println(e.getMessage());
        }
    }
}