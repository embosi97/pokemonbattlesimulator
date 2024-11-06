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
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class PokemonMovesetModelRepoServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(PokemonMovesetModelRepoServiceImpl.class);

    private final PokemonMovesetRepo pokemonMovesetRepo;

    private final PokemonModelRepo pokemonModelRepo;

    private final PokemonMoveSetEndpoint pokemonMoveSetEndpoint;

    private final ExecutorService executor = Executors.newFixedThreadPool(20);

    private final Map<Integer, List<PokemonMovesetModel>> cache = new HashMap<>(); //for pokemon that don't have moveset data

    @Autowired
    public PokemonMovesetModelRepoServiceImpl(PokemonMovesetRepo pokemonMovesetRepo, PokemonModelRepo pokemonModelRepo, PokemonMoveSetEndpoint pokemonMoveSetEndpoint) {
        this.pokemonMovesetRepo = pokemonMovesetRepo;
        this.pokemonModelRepo = pokemonModelRepo;
        this.pokemonMoveSetEndpoint = pokemonMoveSetEndpoint;
    }

    private boolean isPokemonMoveSetDataEmpty() {
        return pokemonMovesetRepo.count() == 0;
    }

    @PostConstruct
    public void init() {
        if (isPokemonMoveSetDataEmpty()) {
            populateMoveSetDatabase(pokemonModelRepo.findAll());
        }
    }

    public Map<Integer, List<PokemonMovesetModel>> getPokemonMovesetsByPokedex(Set<PokemonModel> pokemonModelSet) {

        Map<Integer, List<PokemonMovesetModel>> map = pokemonModelSet.parallelStream()  //concurrency (faster)
                .map(pokemonModel -> {

                    int pokedexNumber = pokemonModel.getPokedexNumber();
                    List<PokemonMovesetModel> pokemonMovesetModelList;

                    if (cache.containsKey(pokedexNumber)) {
                        pokemonMovesetModelList = cache.get(pokedexNumber);
                    } else {
                        pokemonMovesetModelList = fetchExistingMoveSetOrRetrieveNewOne(pokemonModel);
                    }

                    return new AbstractMap.SimpleEntry<>(pokedexNumber, pokemonMovesetModelList);
                })
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())  //filter out empty move sets
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)); //convert to standard Map<>

        List<PokemonMovesetModel> newMoveSets = map.values().stream()
                .flatMap(List::stream)
                .filter(moveset -> cache.containsKey(moveset.getPokedexNumber())) //only the move sets in the cache will be saved
                .collect(Collectors.toList());

        if (!newMoveSets.isEmpty()) {
            pokemonMovesetRepo.saveAll(newMoveSets);
        }

        return map;
    }

    private List<PokemonMovesetModel> fetchExistingMoveSetOrRetrieveNewOne(PokemonModel pokemonModel) {

        int pokedexId = pokemonModel.getPokedexNumber();

        if (cache.containsKey(pokedexId)) {
            return cache.get(pokedexId);
        }

        List<PokemonMovesetModel> pokemonMovesetModelList =
                pokemonMovesetRepo.findByPokedexNumber(pokemonModel.getPokedexNumber());

        if (pokemonMovesetModelList.isEmpty()) {
            pokemonMovesetModelList = fetchAndProcessMoves(pokemonModel);
            cache.put(pokedexId, pokemonMovesetModelList);
        }

        return pokemonMovesetModelList;
    }


    private void populateMoveSetDatabase(List<PokemonModel> pokemonModelList) {

        final int batchSize = 20;

        for (int i = 0; i < pokemonModelList.size(); i += batchSize) {

            List<CompletableFuture<List<PokemonMovesetModel>>> futures =
                    pokemonModelList.subList(i, Math.min(i + batchSize, pokemonModelList.size())).stream()
                            .map(pokemonModel -> CompletableFuture.supplyAsync(() -> fetchAndProcessMoves(pokemonModel), executor))
                            .toList();

            try {

                CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                        .orTimeout(60, TimeUnit.SECONDS);

                int finalI = i;

                //wait for all futures and then handle the result in a single CompletableFuture
                allFutures.thenAccept(voidResult -> processAndSavePokemonMovesetsByBatch(futures, finalI)).join();  //waits until saveAll completes

            } catch (Exception e) {
                logger.error("Error processing batch at index {}: ", i, e);
            }
        }

        executorShutdown();
    }

    private void executorShutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void processAndSavePokemonMovesetsByBatch(List<CompletableFuture<List<PokemonMovesetModel>>> futures, int batchId) {

        List<PokemonMovesetModel> allMovesets = futures.stream()
                .flatMap(future -> future.join().stream())
                .collect(Collectors.toList());

        if (!allMovesets.isEmpty()) {
            pokemonMovesetRepo.insert(allMovesets); //insert is faster than saveAll for bulk saves
            logger.info("Batch of movesets saved for batch starting index {}", batchId);
        }
    }

    private List<PokemonMovesetModel> fetchAndProcessMoves(PokemonModel pokemonModel) {
        try {
            return pokemonMoveSetEndpoint.fetchMovesForPokemon(pokemonModel);
        } catch (Exception e) {
            logger.error("Failed to fetch moves for Pokemon: {}", pokemonModel.getPokedexNumber(), e);
            return Collections.emptyList();
        }
    }

}
