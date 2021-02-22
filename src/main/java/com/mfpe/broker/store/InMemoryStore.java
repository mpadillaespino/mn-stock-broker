package com.mfpe.broker.store;

import com.mfpe.broker.model.Quote;
import com.mfpe.broker.model.Symbol;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class InMemoryStore {

    private final List<Symbol> symbols;
    private final Map<String, Quote> cachedQuotes = new HashMap<>();

    public InMemoryStore() {
        symbols = Stream.of("APPLE", "AMAZON", "FB", "GOOGLE", "NETFLIX", "TESLA")
                .map(Symbol::new)
                .collect(Collectors.toList());
        symbols.forEach(symbol -> cachedQuotes.put(symbol.getValue(), randomQuote(symbol)));
    }

    private Quote randomQuote(Symbol symbol) {
        return Quote.builder()
                .symbol(symbol)
                .bid(randomValue())
                .ask(randomValue())
                .lastPrice(randomValue())
                .volume(randomValue())
                .build();
    }

    public List<Symbol> getAllSymbols() {
        return symbols;
    }

    public Optional<Quote> fetchQuote(String symbol) {
        return Optional.ofNullable(cachedQuotes.get(symbol));
    }

    private BigDecimal randomValue() {
        return BigDecimal
                .valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
    }

    public void update(Quote quote) {
        cachedQuotes.put(quote.getSymbol().getValue(), quote);
    }

}
