package com.mfpe.broker;

import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@MicronautTest
class MarketsControllerTest {

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    @Client("/")
    RxHttpClient client;

    @Test
    void returnListOfMarketsTest() {
        final List result = client.toBlocking().retrieve("/markets", List.class);
        assertEquals(6, result.size());

        final List<LinkedHashMap<String, String>> markets = result;
        assertThat(markets)
                .extracting(entry -> entry.get("value"))
                .containsExactlyInAnyOrder("APPLE", "AMAZON", "FB", "GOOGLE", "NETFLIX", "TESLA");
    }


}
