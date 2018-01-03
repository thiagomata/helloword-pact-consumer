package com.thiagomata.pact.hello.consumer.configuration;

public class ProviderConfiguration {
    public final static String HOST = "localhost";
    public final static int HOST_PORT = 8081;
    public static final String SERVICE_URL = "http://" + HOST + ":" + HOST_PORT;
}
