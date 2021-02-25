package com.mfpe;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class MnStockBrokerTest {

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    @Client("/")
    RxHttpClient client;

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

    @Test
    void testHelloResponse(){
        var result = client.toBlocking().retrieve("/hello");
        assertEquals(result, "Hola soy el servicio");
    }

    @Test
    void testGreetingDe(){
        var result = client.toBlocking().retrieve("/de");
        assertEquals(result, "Hola soy de");
    }

    @Test
    void testGreetingEn(){
        var result = client.toBlocking().retrieve("/en");
        assertEquals(result, "Hola soy en");
    }

//    @Test
//    void testGreetingAsJson(){
//        var result = client.toBlocking().retrieve("/json", ObjectNode.class);
//        assertEquals(result.toString(), "{\"my_text\":\"Hello World\",\"id\":1,\"time_utc\":\"2021-02-25T02:33:28.467463300Z\"}");
//    }

}
