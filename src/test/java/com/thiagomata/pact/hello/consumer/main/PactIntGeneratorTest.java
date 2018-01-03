package com.thiagomata.pact.hello.consumer.main;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import au.com.dius.pact.model.generators.Category;
import au.com.dius.pact.model.generators.RandomIntGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Rule;
import org.junit.Test;
import scala.tools.jline_embedded.internal.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

// import org.codehaus.jackson.map.ObjectMapper;

public class PactIntGeneratorTest {

    @Rule
    public PactProviderRuleMk2 mockProvider = new PactProviderRuleMk2("providerA", "localhost", 8090, this);

    @Pact(consumer = "consumerA", provider = "providerA")
    public RequestResponsePact requestA(PactDslWithProvider builder) throws Exception {
        final DslPart body = new PactDslJsonBody()
                .numberType("id",50)
                .stringType("content", "Hello johny");

        body.getGenerators()
                .addGenerator(Category.BODY, ".id", new RandomIntGenerator(10, 100));

        return builder
                .uponReceiving("(GET) /foo")
                .path("/foo")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(body)
                .toPact();
    }

    @Test
    @PactVerification(fragment = "requestA")
    public void testRequestA() throws IOException, InterruptedException {
        //given:
        final ObjectMapper objectMapper = new ObjectMapper();

        //when:
        String url = "http://localhost:8090/foo";
        Log.info(url);
        final InputStream json = new URL(url).openConnection().getInputStream();
        final Map response = objectMapper.readValue(json, HashMap.class);
        Log.info(response);

        //then:
        assertThat(((Integer) response.get("id")) > 0, is(true));
        //and:
        assertThat(response.get("content"), is(equalTo("Hello johny")));
    }
}