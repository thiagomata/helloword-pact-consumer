package com.thiagomata.pact.hello.consumer.models;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class GreetingTest {

    @Test
    public void testGetters() {
        Greeting g = new Greeting(123,"abc" );
        Greeting g2 = new Greeting(123,"abc" );
        Assert.assertEquals(
            "Test the greeting id getter",
            123,
            g.getId()
        );
        Assert.assertEquals(
            "Test the greeting content getter",
            "abc",
            g.getContent()
        );
        Assert.assertEquals(
            "Testing hash method",
            g.hashCode(),
            g2.hashCode()
        );
    }

    @Test
    public void testEquals() {
        Greeting g = new Greeting(123, "abc");
        Greeting g2 = new Greeting(123, "abc");
        Assert.assertTrue(
                "Testing equals method",
                g.equals(g2)
        );
    }

    @Test
    public void testToString() {
        Greeting g = new Greeting(123, "abc");
        Assert.assertEquals(
                "Testing string version",
                "{\"id\":123,\"content\":\"abc\"}",
                g.toString()
        );

    }
}