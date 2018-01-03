package com.thiagomata.pact.hello.consumer.consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thiagomata.pact.hello.consumer.models.Greeting;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import scala.tools.jline_embedded.internal.Log;

import java.io.IOException;
import java.net.URISyntaxException;

public class DummyConsumer {

    private final String path = "/hello-world";

    private final String host;

    private final Integer port;

    private final String name;

    private HttpResponse response;

    public DummyConsumer(String host, Integer port, String name) {
        Log.info("host="+host);
        Log.info("port="+port);
        this.host = host;
        this.port = port;
        this.name = name;
    }

    public DummyConsumer(String host, Integer port) {
        this.host = host;
        this.port = port;
        this.name = null;
    }

    public Greeting getGreeting() throws IOException, URISyntaxException {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(this.getJsonResponse(), Greeting.class);
    }

    public String getJsonResponse() throws IOException, URISyntaxException {
        String response = this.getEntityAsString(this.getResponse());
        Log.debug("response = " + response);
        return response;
    }

    private String getEntityAsString(HttpResponse response) throws IOException {
        return EntityUtils.toString(response.getEntity());
    }

    private HttpResponse getResponse() throws IOException, URISyntaxException {

        if( this.response != null ) {
            return this.response;
        }

        String url;

        if (!this.host.toLowerCase().matches("^\\w+://.*")) {
            url = "http://" + this.host;
        } else {
            url = this.host;
        }

        URIBuilder initialBuilder = new URIBuilder( url ).
                setPath( this.path ).
                setPort( this.port );


        URIBuilder finalBuilder;
        if( null != this.name ) {
            finalBuilder = initialBuilder.setParameter("name", this.name );
        } else {
            finalBuilder = initialBuilder;
        }

        Log.debug(finalBuilder.getHost());
        Log.debug(finalBuilder.getPath());
        Log.debug(finalBuilder.getQueryParams());
        HttpGet get = new HttpGet(finalBuilder.build());
        Log.debug("REQUEST: " + get.toString());

        HttpClient client = HttpClientBuilder.create().build();
        this.response = client.execute(get);
        return this.response;
    }

    public Integer getStatusCode() throws IOException, URISyntaxException {
        return this.getResponse().getStatusLine().getStatusCode();
    }
}
