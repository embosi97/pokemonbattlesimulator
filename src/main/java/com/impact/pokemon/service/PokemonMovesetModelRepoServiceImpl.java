package com.impact.pokemon.service;

import com.impact.pokemon.model.PokemonModel;
import com.impact.pokemon.repository.PokemonMovesetRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PokemonMovesetModelRepoServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(PokemonMovesetModelRepoServiceImpl.class);

    private final PokemonMovesetRepo pokemonMovesetRepo;

    private final PokemonMoveFetcher pokemonMoveFetcher;

    @Autowired
    public PokemonMovesetModelRepoServiceImpl(PokemonMovesetRepo pokemonMovesetRepo, PokemonMoveFetcher pokemonMoveFetcher) {
        this.pokemonMovesetRepo = pokemonMovesetRepo;
        this.pokemonMoveFetcher = pokemonMoveFetcher;
    }

    private boolean doesPokemonHaveSavedMoveset(PokemonModel pokemonModel) {
        return pokemonMovesetRepo.findByPokedexNumber(pokemonModel.getPokedexNumber()).isEmpty();
    }
}
