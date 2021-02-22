package com.mfpe.broker;

import com.mfpe.broker.error.CustomError;
import com.mfpe.broker.model.Quote;
import com.mfpe.broker.model.Symbol;
import com.mfpe.broker.store.InMemoryStore;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@MicronautTest
class QuotesControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(QuotesControllerTest.class);

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    @Client("/")
    RxHttpClient client;

    @Inject
    InMemoryStore store;

    @Test
    void returnQuotePerSymbolTest() {
        final Quote apple = Quote.builder()
                .symbol(new Symbol("APPLE"))
                .bid(randomValue())
                .ask(randomValue())
                .lastPrice(randomValue())
                .volume(randomValue())
                .build();

        store.update(apple);

        Quote result = client.toBlocking().retrieve(HttpRequest.GET("/quotes/APPLE"), Quote.class);
        LOG.info("Result: {}", result);
        assertThat(apple).isEqualToComparingFieldByField(result);
    }

    @Test
    void returnNotFoundOrUnsupportedSymbolTest(){
        try {
            client.toBlocking().retrieve(HttpRequest.GET("/quotes/UNSUPPORTED"));
        } catch (HttpClientResponseException e) {
            LOG.info("Body: {}", e.getResponse().getBody());
            assertEquals(HttpStatus.NOT_FOUND, e.getResponse().getStatus());
            Optional<CustomError> customError = e.getResponse().getBody(CustomError.class);
            assertTrue(customError.isPresent());
            assertEquals(404, customError.get().getStatus());
            assertEquals("NOT_FOUND", customError.get().getError());
            assertEquals("quote symbol not available", customError.get().getMessage());
            assertEquals("/quotes/UNSUPPORTED", customError.get().getPath());
        }
    }

    private BigDecimal randomValue() {
        return BigDecimal
                .valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
    }


}
