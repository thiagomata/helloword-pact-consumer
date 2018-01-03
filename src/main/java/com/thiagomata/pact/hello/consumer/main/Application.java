package com.thiagomata.pact.hello.consumer.main;

import com.thiagomata.pact.hello.consumer.configuration.ProviderConfiguration;
import com.thiagomata.pact.hello.consumer.consumer.DummyConsumer;

public class Application {
    public static String mainData() {
        try {
            DummyConsumer consumer = new DummyConsumer(ProviderConfiguration.HOST, ProviderConfiguration.HOST_PORT);
            String data = consumer.getGreeting().getContent();
        return data;
        } catch (Exception e) {
            return "error";
        }
    }
    public static void main(String[] args) {
        System.out.println(mainData());
    }
}