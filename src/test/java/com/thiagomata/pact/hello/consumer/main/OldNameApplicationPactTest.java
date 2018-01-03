package com.thiagomata.pact.hello.consumer.main;

import au.com.dius.pact.consumer.ConsumerPactBuilder;
import au.com.dius.pact.consumer.PactVerificationResult;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.model.MockProviderConfig;
import au.com.dius.pact.model.RequestResponsePact;
import com.thiagomata.pact.hello.consumer.consumer.DummyConsumer;
import com.thiagomata.pact.hello.consumer.models.Greeting;
import com.thiagomata.pact.hello.consumer.utils.HelloWorldPactDslJsonBody;
import org.junit.Assert;
import scala.tools.jline_embedded.internal.Log;

import static au.com.dius.pact.consumer.ConsumerPactRunnerKt.runConsumerTest;
import static org.junit.Assert.assertEquals;

public class OldNameApplicationPactTest {


    /**
     * This PACT call using the anonymous functions
     * does not allow to the creation of the restrictions
     * of the max and min.
     *
     * In fact, just running this pact will remove all the
     * restrictions of max and min from your pact definition.
     *
     * Best using the standard approach as can be see in the
     * NameApplicationPactTest
     *
     * @see NameApplicationPactTest
     * @throws Throwable
     */
    // @Test
    public void NOTUSETHIStestNamePact() throws Throwable {

        /**
         * Let the Pact define the mock server address and port
         */
        MockProviderConfig config = MockProviderConfig.createDefault();

        /**
         * This approach does not work with the anonymous function!!
         * The anonymous function will not accept this max and min rules
         * and will remove all the others max and min that are being
         * defined in the other methods!!
         */
        final DslPart body = new HelloWorldPactDslJsonBody()
                .integerType("id",1000,2000)
                .stringType("content", "Hello johny");

        Log.info(body.getGenerators().toString());

        /**
         * Creating the mock server
         *
         * Define the expected input
         *  Using relative address
         *  The provider address will be automatically created
         *  The provider port will be automatically created
         * Define the expected output
         *  Keep the id as a undefined integer
         *  Set the content to the test
         */
        RequestResponsePact pact = ConsumerPactBuilder
                .consumer("hello_world_consumer")
                .hasPactWith("hello_world_provider")
                .uponReceiving("a named request of hello world")
                .path("/hello-world")
                .matchQuery("name","johny")
                .method("GET")
                .willRespondWith()
                .body( body )
                .toPact();

        /**
         * Create the mock server into the defined config and with the
         * pact result prepared
         */
        PactVerificationResult result = runConsumerTest(
                pact,
                config,
                mockServer -> {

                    Log.debug("inside mock server");
                    /**
                     * Pass the mock server configuration to the consumer classes
                     */
                    DummyConsumer consumer = new DummyConsumer(
                            mockServer.getUrl(),
                            mockServer.getPort(),
                            "johny"
                    );

                    /**
                     * Now, when the code internally fires to the
                     * mockServer we should get the expected answer
                     */
                    Greeting greeting = consumer.getGreeting();

                    Log.debug(greeting);

                    Assert.assertNotNull(
                            "Greeting id should not be null",
                            greeting.getId()
                    );

                    /**
                     * Currently I am not able to define a rule into the
                     * DSL Matching methods to assure that the value should
                     * be bigger than 0
                     */
                    Assert.assertTrue( greeting.getId() > 0 ); // <=================================================

                    assertEquals(
                            "Validate expected default greeting content",
                            "Hello johny",
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
        );

        /**
         * If some Assert inside of the anonymous functions fails
         * it will not automatically throw a failure.
         *
         * We need to capture the error from the result
         */
        if (result instanceof PactVerificationResult.Error) {
            throw ((PactVerificationResult.Error) result).getError();
        }

        assertEquals(PactVerificationResult.Ok.INSTANCE, result);
    }
}