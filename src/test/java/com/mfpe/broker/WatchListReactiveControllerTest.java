package com.mfpe.broker;

import com.mfpe.broker.model.Symbol;
import com.mfpe.broker.model.WatchList;
import com.mfpe.broker.store.InMemoryAccountStore;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.reactivex.Single;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WatchListReactiveControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(WatchListReactiveControllerTest.class);
    private static final UUID TEST_ACCOUNT_ID = WatchListReactiveController.ACCOUNT_ID;

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    @Client("/account/watchlist-reactive")
    RxHttpClient client;

    @Inject
    InMemoryAccountStore store;

    @Test
    @Order(1)
    void returnsEmptyWatchListForAccountTest() {
        Single<WatchList> result = client.retrieve(HttpRequest.GET("/"), WatchList.class).singleOrError();
        assertTrue(result.blockingGet().getSymbols().isEmpty());
        assertTrue(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
    }

    @Test
    @Order(2)
    void returnsWatchListForAccountTest() {
        List<Symbol> symbols = Stream.of("APPLE", "GOOGLE")
                .map(Symbol::new)
                .collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);

        WatchList result = client.toBlocking().retrieve("/", WatchList.class);
        assertEquals(2, result.getSymbols().size());
        assertEquals(2, store.getWatchList(TEST_ACCOUNT_ID).getSymbols().size());
    }

    @Test
    @Order(3)
    void canUpdateWatchListForAccountTest(){
        List<Symbol> symbols = Stream.of("APPLE", "GOOGLE")
                .map(Symbol::new)
                .collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);

        HttpResponse<Object> added = client.toBlocking().exchange(HttpRequest.PUT("/", watchList));
        assertEquals(HttpStatus.OK, added.getStatus());
        assertEquals(watchList, store.getWatchList(TEST_ACCOUNT_ID));
    }

    @Test
    @Order(4)
    void canDeleteWatchListForAccountTest(){
        List<Symbol> symbols = Stream.of("APPLE", "GOOGLE")
                .map(Symbol::new)
                .collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);

        assertFalse(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());

        HttpResponse<Object> deleted = client.toBlocking().exchange(HttpRequest.DELETE("/" + TEST_ACCOUNT_ID));

        assertEquals(HttpStatus.OK, deleted.getStatus());
        assertTrue(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
    }

    @Test
    @Order(5)
    void returnsWatchListForAccountAsSingleTest() {
        List<Symbol> symbols = Stream.of("APPLE", "GOOGLE")
                .map(Symbol::new)
                .collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);

        WatchList result = client.toBlocking().retrieve("/", WatchList.class);
        assertEquals(2, result.getSymbols().size());
        assertEquals(2, store.getWatchList(TEST_ACCOUNT_ID).getSymbols().size());
    }

}
