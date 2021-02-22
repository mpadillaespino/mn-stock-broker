package com.mfpe.broker;

import com.mfpe.broker.model.Quote;
import com.mfpe.broker.model.Symbol;
import com.mfpe.broker.store.InMemoryStore;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;


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

    private BigDecimal randomValue() {
        return BigDecimal
                .valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
    }


}
