package com.impact.pokemon.service;

import com.impact.pokemon.model.PokemonModel;
import com.impact.pokemon.model.PokemonMovesetModel;
import com.impact.pokemon.repository.PokemonModelRepo;
import com.impact.pokemon.repository.PokemonMovesetRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PokemonMovesetModelRepoServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(PokemonMovesetModelRepoServiceImpl.class);

    private final PokemonMovesetRepo pokemonMovesetRepo;

    private final PokemonModelRepo pokemonModelRepo;

    private final PokemonMoveFetcher pokemonMoveFetcher;

    @Autowired
    public PokemonMovesetModelRepoServiceImpl(PokemonMovesetRepo pokemonMovesetRepo, PokemonModelRepo pokemonModelRepo, PokemonMoveFetcher pokemonMoveFetcher) {
        this.pokemonMovesetRepo = pokemonMovesetRepo;
        this.pokemonModelRepo = pokemonModelRepo;
        this.pokemonMoveFetcher = pokemonMoveFetcher;
    }

    private boolean isPokemonMoveSetDataEmpty() {
        return pokemonMovesetRepo.count() == 0;
    }

    public Map<Integer, List<PokemonMovesetModel>> getPokemonMovesetsByPokedex(PokemonModel pokemonModel1,
                                                                               PokemonModel pokemonModel2) {
        if (isPokemonMoveSetDataEmpty()) {
            logger.info("Populating the database with Pokemon Move Set data.");
            populateMoveSetDatabase(pokemonModelRepo.findAll());
        }

        //retrieve move sets for both Pokemon
        return Stream.of(pokemonModel1.getPokedexNumber(), pokemonModel2.getPokedexNumber())
                .collect(Collectors.toMap(
                        pokedexNumber -> pokedexNumber,
                        pokemonMovesetRepo::findByPokedexNumber
                ));
    }

    private void populateMoveSetDatabase(List<PokemonModel> pokemonModelList) {

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Future<List<PokemonMovesetModel>>> futures = new ArrayList<>();

        for (PokemonModel pokemonModel : pokemonModelList) {
            futures.add(executor.submit(() -> pokemonMoveFetcher.fetchMovesForPokemon(pokemonModel)));
        }

        List<PokemonMovesetModel> allMovesets = new ArrayList<>();

        for (Future<List<PokemonMovesetModel>> future : futures) {
            try {
                List<PokemonMovesetModel> pokemonMoveset = future.get();
                if (!pokemonMoveset.isEmpty()) {
                    allMovesets.addAll(pokemonMoveset);
                }
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error saving Pokemon movesets:", e);
            }
        }

        //saving all movesets in one batch, if any exist
        if (!allMovesets.isEmpty()) {
            try {
                pokemonMovesetRepo.saveAll(allMovesets);
                logger.info("Successfully saved all Pokemon movesets.");
            } catch (Exception e) {
                logger.error("Error during batch save to repository", e);
            }
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();  //forcing shutdown if tasks didn't finish in a min
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
