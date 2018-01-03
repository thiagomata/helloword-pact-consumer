package com.thiagomata.pact.hello.consumer.models;

import com.google.gson.Gson;

public class Greeting {

    private final long id;
    private final String content;

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public boolean equals(Greeting obj) {
        return (
            this.getId() == obj.getId() &&
            this.getContent() == obj.getContent()
        );
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + content.hashCode();
        return result;
    }

    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}