package com.thiagomata.pact.hello.consumer.main;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import com.thiagomata.pact.hello.consumer.configuration.ProviderConfiguration;
import com.thiagomata.pact.hello.consumer.utils.HelloWorldPactDslJsonBody;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class MainApplicationPactTest {


    @Rule
    public PactProviderRuleMk2 mockProvider = new PactProviderRuleMk2(
            "hello_world_provider",
            ProviderConfiguration.HOST,
            ProviderConfiguration.HOST_PORT,
            this);

    @Pact(consumer = "hello_world_consumer", provider = "hello_world_provider")
    public RequestResponsePact requestHelloWorldWithoutName(PactDslWithProvider builder) throws Exception {
        final DslPart body = new HelloWorldPactDslJsonBody()
                .numberType("id",1000,2000)
                .stringType("content", "Hello world");

        return builder
                .uponReceiving("Request hello-world with name")
                .path("/hello-world")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(body)
                .toPact();
    }

    @Test
    @PactVerification(fragment = "requestHelloWorldWithoutName")
    public void testRequestHelloWorld() throws IOException, InterruptedException, URISyntaxException {
        Assert.assertEquals(
                "show main data",
                "Hello world",
                Application.mainData()
        );
    }
}