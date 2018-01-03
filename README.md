# helloword-pact-consumer

This project it is a study about how to use the PACT https://docs.pact.io/ into the Java https://github.com/DiUS/pact-jvm.
    
## Consumer

This is the PACT consumer. It *does not required* the existence of the server to run the tests. The junit should update the 
PACT based on the pact described into the unitary tests.

## Tests

Thanks to the PACT we can tests local methods of the project that consumes the services of the provider without the need of the provider be accessible ( or even exists ).
    The expected provider answer is defined and the PACT creates a mocked server to the tests.
    
## Rules and Bugs

We have some problems creating some restrictions about max and min values into the pact. You can read about it over here https://stackoverflow.com/questions/48068975/using-java-to-create-pact-i-am-not-able-to-set-the-min-value-of-the-numbertype-i
     TL;DR The approach that best worked with the rules definition was not use the anonymous version of the PACT to Java. At least, for now.
    
