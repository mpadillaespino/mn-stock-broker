package com.mfpe.broker.persistence.jpa;

import java.util.List;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface SymbolsRepository extends CrudRepository<SymbolEntity, String> {

    @Override
    List<SymbolEntity> findAll();

}