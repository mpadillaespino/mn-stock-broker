package com.mfpe.broker;

import com.mfpe.broker.model.Symbol;
import com.mfpe.broker.model.WatchList;
import com.mfpe.broker.store.InMemoryAccountStore;
import io.micronaut.http.*;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
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

import static io.micronaut.http.HttpRequest.*;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WatchListControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerTest.class);
    private static final UUID TEST_ACCOUNT_ID = WatchListController.ACCOUNT_ID;
    public static final String ACCOUNT_WATCHLIST = "/account/watchlist";

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    @Client("/")
    RxHttpClient client;

    @Inject
    InMemoryAccountStore store;

    private BearerAccessRefreshToken givenMyUserIsLoggedIn(){
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("my-user", "secret");
        var login = HttpRequest.POST("/login", credentials);
        var respose = client.toBlocking().exchange(login, BearerAccessRefreshToken.class);
        assertEquals(HttpStatus.OK, respose.getStatus());
        BearerAccessRefreshToken token = respose.body();
        assertNotNull(token);
        assertEquals("my-user", token.getUsername());
        LOG.info("Login Bearer Token {} expires in {}", token.getAccessToken(), token.getExpiresIn());
        return token;
    }


    @Test
    @Order(1)
    void returnsEmptyWatchListForAccountTest() {
        var token = givenMyUserIsLoggedIn();
        var request = GET(ACCOUNT_WATCHLIST)
                .accept(MediaType.APPLICATION_JSON)
                .bearerAuth(token.getAccessToken());
        WatchList result = client.toBlocking().retrieve(request, WatchList.class);
        assertTrue(result.getSymbols().isEmpty());
        assertTrue(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
    }

    @Test
    @Order(2)
    void returnsWatchListForAccountTest() {
        var token = givenMyUserIsLoggedIn();

        List<Symbol> symbols = Stream.of("APPLE", "GOOGLE")
                .map(Symbol::new)
                .collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);

        var request = GET(ACCOUNT_WATCHLIST)
                .accept(MediaType.APPLICATION_JSON)
                .bearerAuth(token.getAccessToken());

        WatchList result = client.toBlocking().retrieve(request, WatchList.class);
        assertEquals(2, result.getSymbols().size());
        assertEquals(2, store.getWatchList(TEST_ACCOUNT_ID).getSymbols().size());
    }

    @Test
    @Order(3)
    void canUpdateWatchListForAccountTest(){
        var token = givenMyUserIsLoggedIn();

        List<Symbol> symbols = Stream.of("APPLE", "GOOGLE")
                .map(Symbol::new)
                .collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);

        var request = PUT(ACCOUNT_WATCHLIST, watchList)
                .accept(MediaType.APPLICATION_JSON)
                .bearerAuth(token.getAccessToken());

        HttpResponse<Object> added = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.OK, added.getStatus());
        assertEquals(watchList, store.getWatchList(TEST_ACCOUNT_ID));
    }

    @Test
    @Order(4)
    void canDeleteWatchListForAccountTest(){
        var token = givenMyUserIsLoggedIn();

        List<Symbol> symbols = Stream.of("APPLE", "GOOGLE")
                .map(Symbol::new)
                .collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);

        assertFalse(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());

        var request = DELETE("/account/watchlist/" + TEST_ACCOUNT_ID)
                .accept(MediaType.APPLICATION_JSON)
                .bearerAuth(token.getAccessToken());

        HttpResponse<Object> deleted = client.toBlocking().exchange(request);

        assertEquals(HttpStatus.OK, deleted.getStatus());
        assertTrue(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
    }

    @Test
    @Order(5)
    void unAuthorizedAccessTest(){
        try {
            client.toBlocking().retrieve(ACCOUNT_WATCHLIST);
            fail("shuold fail is no exception is thrown.");
        } catch (HttpClientResponseException e){
            assertEquals(HttpStatus.UNAUTHORIZED, e.getStatus());
        }
    }

}
