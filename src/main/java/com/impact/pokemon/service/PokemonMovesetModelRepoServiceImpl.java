package com.impact.pokemon.service;

import com.impact.pokemon.model.PokemonModel;
import com.impact.pokemon.model.PokemonMovesetModel;
import com.impact.pokemon.repository.PokemonModelRepo;
import com.impact.pokemon.repository.PokemonMovesetRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    @PostConstruct
    public void init() {
//        if (isPokemonMoveSetDataEmpty()) {
//            logger.info("Populating the database with Pokemon Move Set data on startup.");
//            populateMoveSetDatabase(pokemonModelRepo.findAll());
//        }
    }

    public Map<Integer, List<PokemonMovesetModel>> getPokemonMovesetsByPokedex(PokemonModel pokemonModel1,
                                                                               PokemonModel pokemonModel2) {
        //retrieve move sets for both Pokemon
        return Stream.of(pokemonModel1.getPokedexNumber(), pokemonModel2.getPokedexNumber())
                .collect(Collectors.toMap(
                        pokedexNumber -> pokedexNumber,
                        pokemonMovesetRepo::findByPokedexNumber
                ));
    }

    private void populateMoveSetDatabase(List<PokemonModel> pokemonModelList) {

        //fixed batch size
        final int batchSize = 10;

        //fixed thread pool
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        //saving by batches
        for (int i = 0; i < pokemonModelList.size(); i += batchSize) {

            //current subList based on batch size
            List<PokemonModel> batch = pokemonModelList.subList(i, Math.min(i + batchSize, pokemonModelList.size()));

            //list of CompletableFutures for all Pokemon models in the current batch
            List<CompletableFuture<List<PokemonMovesetModel>>> futures = batch.stream()
                    .map(pokemonModel -> CompletableFuture.supplyAsync(() -> {
                        logger.info("Starting to fetch moves for Pokemon with Pokedex Number: {}", pokemonModel.getPokedexNumber());
                        List<PokemonMovesetModel> moves = Collections.emptyList();
                        try {
                            moves = pokemonMoveFetcher.fetchMovesForPokemon(pokemonModel);
                            logger.info("Successfully fetched moves for Pokemon with Pokedex Number: {}", pokemonModel.getPokedexNumber());
                            logger.debug("Fetched moves: {}", moves);
                        } catch (Exception e) {
                            logger.error("Error fetching moves for Pokemon with Pokedex Number: {}", pokemonModel.getPokedexNumber(), e);
                        }
                        return moves;
                    }, executor))
                    .toList();

            List<PokemonMovesetModel> allMovesets = futures.stream()
                    .flatMap(future -> {
                        try {
                            return future.get().stream(); //waits for each future to complete
                        } catch (InterruptedException | ExecutionException e) {
                            logger.error("Error retrieving movesets:", e);
                            return Stream.empty();
                        }
                    })
                    .collect(Collectors.toList());

            //saving all move sets in one batch for the current batch, if any exist
            if (!allMovesets.isEmpty()) {
                try {
                    pokemonMovesetRepo.saveAll(allMovesets);
                    logger.info("Successfully saved all Pokemon move set models for batch starting at index " + i + "!");
                } catch (Exception e) {
                    logger.error("Error during batch save to repository", e);
                }
            }
        }

        executor.shutdown();

        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) { //shuts down if it takes more than a minute
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
