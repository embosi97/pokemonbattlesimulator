package com.impact.pokemon.repository;

import com.impact.pokemon.model.PokemonMovesetModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PokemonMovesetRepo extends MongoRepository<PokemonMovesetModel, Integer> {

    List<PokemonMovesetModel> findByPokedexNumber(int pokedexId);

}
