package com.impact.pokemon.repository;

import com.impact.pokemon.model.PokemonMovesetModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PokemonMovesetRepo extends MongoRepository<PokemonMovesetModel, Integer> {

    List<Optional<PokemonMovesetModel>> findByPokedexNumber(int pokedexId);

}
