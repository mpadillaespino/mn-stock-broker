package com.mfpe.broker.persistence.jpa;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuotesRepository extends CrudRepository<QuoteEntity, Integer> {

    @Override
    List<QuoteEntity> findAll();

    Optional<QuoteEntity> findBySymbol(SymbolEntity entity);

    // Ordering
    List<QuoteDTO> listOrderByVolumeDesc();
    List<QuoteDTO> listOrderByVolumeAsc();

}
