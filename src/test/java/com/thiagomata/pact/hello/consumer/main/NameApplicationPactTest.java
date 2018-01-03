package com.thiagomata.pact.hello.consumer.main;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import com.thiagomata.pact.hello.consumer.consumer.DummyConsumer;
import com.thiagomata.pact.hello.consumer.models.Greeting;
import com.thiagomata.pact.hello.consumer.utils.HelloWorldPactDslJsonBody;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import scala.tools.jline_embedded.internal.Log;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

public class NameApplicationPactTest {


    @Rule
    public PactProviderRuleMk2 mockProvider = new PactProviderRuleMk2("hello_world_provider", this);

    @Pact(consumer = "hello_world_consumer", provider = "hello_world_provider")
    public RequestResponsePact requestHelloWorldWithName(PactDslWithProvider builder) throws Exception {
        final DslPart body = new HelloWorldPactDslJsonBody()
                .numberType("id",1000,2000)
                .stringType("content", "Hello bob");

        return builder
                .uponReceiving("Request hello-world with name")
                .path("/hello-world")
                .method("GET")
                .matchQuery("name","bob")
                .willRespondWith()
                .status(200)
                .body(body)
                .toPact();
    }

    @Test
    @PactVerification(fragment = "requestHelloWorldWithName")
    public void testRequestHelloWorld() throws IOException, InterruptedException, URISyntaxException {

        /**
         * Pass the mock server configuration to the consumer classes
         */
        DummyConsumer consumer = new DummyConsumer(
                mockProvider.getMockServer().getUrl(),
                mockProvider.getPort(),
                "bob"
        );

        /**
         * Now, when the code internally fires to the
         * mockServer we should get the expected answer
         */
        Greeting greeting = consumer.getGreeting();

        Log.info(greeting);

        Assert.assertNotNull(
                "Greeting id should not be null",
                greeting.getId()
        );

        /**
         * Currently I am not able to define a rule into the
         * DSL Matching methods to assure that the value should
         * be bigger than 0
         */
        Assert.assertTrue( greeting.getId() > 0 );

        assertEquals(
                "Validate expected default greeting content",
                "Hello bob",
                greeting.getContent()
        );

        Log.debug("status code = " + consumer.getStatusCode() );

        Assert.assertTrue(
                "test consumer status code",
                consumer.getStatusCode().equals(
                        200
                )
        );
    }
}