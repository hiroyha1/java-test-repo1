package com.example;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.DeviceCodeCredential;
import com.azure.identity.DeviceCodeCredentialBuilder;
import com.azure.identity.DeviceCodeInfo;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import okhttp3.Request;

public class Graph {
    private static Properties _properties;
    private static DeviceCodeCredential _deviceCodeCredential;
    private static GraphServiceClient<Request> _graphClient;

    public static void initializeGraphForUserAuth(Properties properties, Consumer<DeviceCodeInfo> challenge) throws Exception {
        if (properties == null) {
            throw new Exception("Properties cannot be null.");
        }

        _properties = properties;
        
        final String clientId = properties.getProperty("app.clientId");
        final String tenantId = properties.getProperty("app.tenantId");
        final List<String> graphUserScopes = Arrays.asList(properties.getProperty("app.graphUserScopes").split(","));

        _deviceCodeCredential = new DeviceCodeCredentialBuilder()
            .clientId(clientId)
            .tenantId(tenantId)
            .challengeConsumer(challenge)
            .build();

        final TokenCredentialAuthProvider authProvider = new TokenCredentialAuthProvider(graphUserScopes, _deviceCodeCredential);

        _graphClient = GraphServiceClient.<Request>builder()
            .authenticationProvider(authProvider)
            .buildClient();
     }

     public static String getUserToken() throws Exception {
        if (_deviceCodeCredential == null) {
            throw new Exception("Grpah has not been initialized for user auth");
        }
        final String[] graphUserScopes = _properties.getProperty("app.graphUserScopes").split(",");
        final TokenRequestContext context = new TokenRequestContext();
        context.addScopes(graphUserScopes);
        final AccessToken token = _deviceCodeCredential.getToken(context).block();
        return token.getToken();
     }

     public static User getUser() throws Exception {
        if (_graphClient == null) {
            throw new Exception("Graph has not been initialized for user auth");
        }
    
        return _graphClient.me()
            .buildRequest()
            .select("displayName,mail,userPrincipalName")
            .get();
     }
}
