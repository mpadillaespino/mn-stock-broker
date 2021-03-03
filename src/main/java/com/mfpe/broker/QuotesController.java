package com.mfpe.broker;

import com.mfpe.broker.error.CustomError;
import com.mfpe.broker.model.Quote;
import com.mfpe.broker.persistence.jpa.QuoteDTO;
import com.mfpe.broker.persistence.jpa.QuoteEntity;
import com.mfpe.broker.persistence.jpa.QuotesRepository;
import com.mfpe.broker.persistence.jpa.SymbolEntity;
import com.mfpe.broker.store.InMemoryStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.Optional;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/quotes")
public class QuotesController {

    private final InMemoryStore store;
    private final QuotesRepository quotesRepository;

    public QuotesController(InMemoryStore store, QuotesRepository quotesRepository) {
        this.store = store;
        this.quotesRepository = quotesRepository;
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

    @Get("/jpa")
    public List<QuoteEntity> getAllQuotesJPA(){
        return quotesRepository.findAll();
    }

    @Operation(summary = "returns a quote for the given symbol from the database via JPA.")
    @Get("/jpa/{symbol}")
    public HttpResponse getQuoteJPA(@PathVariable String symbol) {
        Optional<QuoteEntity> maybeQuote = quotesRepository.findBySymbol(new SymbolEntity(symbol));
        if(maybeQuote.isEmpty()){
            CustomError notFound = CustomError
                    .builder()
                    .status(HttpStatus.NOT_FOUND.getCode())
                    .error(HttpStatus.NOT_FOUND.name())
                    .message("quote symbol not available in db.")
                    .path("/quotes/" + symbol + "/jpa")
                    .build();
            return HttpResponse.notFound(notFound);
        }
        return HttpResponse.ok(maybeQuote.get());
    }

    @Get("/jpa/ordered/desc")
    public List<QuoteDTO> ordered(){
        return quotesRepository.listOrderByVolumeDesc();
    }

    @Get("/jpa/ordered/asc")
    public List<QuoteDTO> orderedAsc(){
        return quotesRepository.listOrderByVolumeAsc();
    }

}
