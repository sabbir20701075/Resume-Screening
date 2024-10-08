package com.example.resumescreening;

import com.apollographql.apollo.ApolloClient;
import okhttp3.OkHttpClient;

public class ApolloClientManager {
    private static ApolloClient apolloClient;

    public static ApolloClient getApolloClient() {
        if (apolloClient == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            apolloClient = ApolloClient.builder()
                    .serverUrl("https://api.spacex.land/graphql/") // Using SpaceX GraphQL API as an example
                    .okHttpClient(okHttpClient)
                    .build();
        }
        return apolloClient;
    }
}
