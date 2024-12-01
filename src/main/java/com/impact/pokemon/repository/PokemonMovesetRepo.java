package com.impact.pokemon.repository;

import com.impact.pokemon.model.PokemonMoveModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PokemonMovesetRepo extends MongoRepository<PokemonMoveModel, Integer> {

    List<PokemonMoveModel> findByPokedexNumber(int pokedexId);

}
