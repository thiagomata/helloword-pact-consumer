package com.thiagomata.pact.hello.consumer.utils;

import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.model.generators.Category;
import au.com.dius.pact.model.generators.RandomIntGenerator;
import io.gatling.jsonpath.Parser$;
import org.apache.commons.lang3.StringUtils;

public class HelloWorldPactDslJsonBody extends PactDslJsonBody {

    private String matcherKey(String name) {
        String key = this.rootPath + name;
        if (!name.equals("*") && !name.matches(Parser$.MODULE$.FieldRegex().toString())) {
            key = StringUtils.stripEnd(this.rootPath, ".") + "['" + name + "']";
        }
        return key;
    }

    public PactDslJsonBody numberType(String name, Integer min, Integer max) {
        this.getGenerators().addGenerator(Category.BODY, matcherKey(name), new RandomIntGenerator(min, max));
        return numberType(name, Math.round( ( min + max ) / 2 ));
    }

    public PactDslJsonBody integerType(String name, Integer min, Integer max) {
        RandomIntGenerator randomGenerator = new RandomIntGenerator(min, max);
        this.getGenerators().addGenerator(Category.BODY, matcherKey(name), randomGenerator );
        return integerType(name, Math.round( ( min + max ) / 2 ) );
    }

}
