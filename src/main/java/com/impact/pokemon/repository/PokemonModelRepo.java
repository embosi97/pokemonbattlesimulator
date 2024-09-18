package com.impact.pokemon.repository;

import com.impact.pokemon.model.PokemonModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PokemonModelRepo extends MongoRepository<PokemonModel, Integer> {

    Optional<PokemonModel> findByNameValue(String name);

}
