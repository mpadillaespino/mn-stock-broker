package com.mfpe.broker;

import com.mfpe.broker.model.Symbol;
import com.mfpe.broker.persistence.jpa.SymbolEntity;
import com.mfpe.broker.persistence.jpa.SymbolsRepository;
import com.mfpe.broker.store.InMemoryStore;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.List;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/markets")
public class MarketsController {

    private final SymbolsRepository symbolsRepository;
    private final InMemoryStore store;

    public MarketsController(SymbolsRepository symbolsRepository, InMemoryStore store) {
        this.symbolsRepository = symbolsRepository;
        this.store = store;
    }

    @Get("/")
    public List<Symbol> all(){
        return store.getAllSymbols();
    }

    @Get("/jpa")
    public List<SymbolEntity> allViaJPA(){
        return symbolsRepository.findAll();
    }

}
