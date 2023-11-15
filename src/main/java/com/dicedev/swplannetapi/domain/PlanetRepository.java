package com.dicedev.swplannetapi.domain;

import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface PlanetRepository extends CrudRepository<Planet, Long>, QueryByExampleExecutor<Planet> {

    Optional<Planet> findByName(String name);

    @Override
    <S extends Planet> Iterable<S> findAll(Example<S> example);
}