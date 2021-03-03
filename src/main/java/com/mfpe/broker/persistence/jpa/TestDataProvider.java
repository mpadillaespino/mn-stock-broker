package com.mfpe.broker.persistence.jpa;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import io.netty.util.internal.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.Random;
import java.util.stream.Stream;

//Used to insert data into db on startup
@Singleton
@Requires(notEnv = Environment.TEST)
public class TestDataProvider {

    private static final Logger LOG = LoggerFactory.getLogger(TestDataProvider.class);
    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
    private final SymbolsRepository symbolsRepository;
    private final QuotesRepository quotesRepository;

    public TestDataProvider(SymbolsRepository symbolsRepository, QuotesRepository quotesRepository) {
        this.symbolsRepository = symbolsRepository;
        this.quotesRepository = quotesRepository;
    }

    @EventListener
    public void init(StartupEvent event){
        if (symbolsRepository.findAll().isEmpty()){
            LOG.info("Adding test data as database was found!");
            Stream.of("APPLE", "AMAZON","GOOGLE")
                    .map(SymbolEntity::new)
                    .forEach(symbolsRepository::save);
        }
        if(quotesRepository.findAll().isEmpty()){
            LOG.info("Adding test data as empty database was found!");
            symbolsRepository.findAll().forEach(symbolEntity -> {
                var quote = new QuoteEntity();
                quote.setSymbol(symbolEntity);
                quote.setAsk(randomValue());
                quote.setBid(randomValue());
                quote.setLastPrice(randomValue());
                quote.setVolume(randomValue());
                quotesRepository.save(quote);
            });
        }

    }

    private BigDecimal randomValue() {
        return BigDecimal.valueOf(RANDOM.nextDouble(1,100));
    }
}
