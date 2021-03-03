package com.mfpe.broker.persistence.jpa;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Slice;
import io.micronaut.data.repository.CrudRepository;

import java.math.BigDecimal;
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

    // Filter
    List<QuoteDTO> findByVolumeGreaterThan(BigDecimal volume);
    List<QuoteDTO> findByVolumeGreaterThanOrderByVolumeAsc(BigDecimal volume);

    // Pagination
    List<QuoteDTO> findByVolumeGreaterThan(BigDecimal volume, Pageable pageable);
    Slice<QuoteDTO> list(Pageable pageable);

}
