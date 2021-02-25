package com.mfpe.broker;

import com.mfpe.broker.error.CustomError;
import com.mfpe.broker.model.Quote;
import com.mfpe.broker.store.InMemoryStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;

import java.util.Optional;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/quotes")
public class QuotesController {

    private final InMemoryStore store;

    public QuotesController(InMemoryStore store) {
        this.store = store;
    }

    @Operation(summary = "returns a quote for the given symbol.")
    @Get("/{symbol}")
    public HttpResponse getQuote(@PathVariable String symbol) {
        Optional<Quote> maybeQuote = store.fetchQuote(symbol);
        if(maybeQuote.isEmpty()){
            CustomError notFound = CustomError
                    .builder()
                    .status(HttpStatus.NOT_FOUND.getCode())
                    .error(HttpStatus.NOT_FOUND.name())
                    .message("quote symbol not available")
                    .path("/quotes/" + symbol)
                    .build();
            return HttpResponse.notFound(notFound);
        }
        return HttpResponse.ok(maybeQuote.get());
    }

}
