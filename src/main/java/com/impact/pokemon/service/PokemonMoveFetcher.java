package com.impact.pokemon.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.impact.pokemon.enums.PokemonTypeEnum;
import com.impact.pokemon.model.PokemonModel;
import com.impact.pokemon.model.PokemonMovesetModel;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class PokemonMoveFetcher {

    @Value("${pokeApi.url}")
    private String pokeApiUrl;

    private static final Logger logger = LoggerFactory.getLogger(PokemonMoveFetcher.class);

    private final ObjectMapper objectMapper;

    private final AtomicInteger moveId = new AtomicInteger(0);

    public PokemonMoveFetcher(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    //synchronized method so threads can only hit the API one at a time
    private synchronized String fetchPokemonData(int pokemonId) {
        HttpResponse<String> response;
        try {
            logger.info("Making request for Pokemon ID " + pokemonId);
            response = Unirest.get(pokeApiUrl +
                    URLEncoder.encode(String.valueOf(pokemonId), StandardCharsets.UTF_8)).asString();
            logger.info("Response Code: " + response.getCode());

            if (response.getCode() != 200) {
                logger.error("Failed to fetch data for Pokémon with ID " + pokemonId + ": " + response.getBody());
                throw new RuntimeException("Failed to fetch data: " + response.getBody());
            }
        } catch (UnirestException e) {
            logger.error("Error during API call for Pokémon ID: " + pokemonId, e);
            throw new RuntimeException("Error during API call", e);
        }
        return response.getBody();
    }

    public List<PokemonMovesetModel> fetchMovesForPokemon(PokemonModel pokemonModel) {

        String pokemonData = fetchPokemonData(pokemonModel.getPokedexNumber());

        JsonNode pokemonNode;
        JsonNode movesJson;

        try {
            //parse JSON for Pokemon data
            pokemonNode = objectMapper.readTree(pokemonData);
            movesJson = objectMapper.readTree(PokemonMoveFetcher.class.getResourceAsStream("/data/moves.json"));

        } catch (IOException e) {
            logger.error("Error parsing JSON data for Pokémon: " + pokemonModel.getNameValue(), e);
            throw new RuntimeException("Error parsing JSON data", e);
        }

        //retrieving moves
        JsonNode movesNode = pokemonNode.get("moves");

        if (movesNode == null || movesNode.isEmpty()) {
            String message = "No moves found for Pokémon: " + pokemonModel.getNameValue();
            logger.error(message);
            throw new RuntimeException(message);
        }

        ExecutorService executor = Executors.newFixedThreadPool(5);

        List<CompletableFuture<PokemonMovesetModel>> futures = new ArrayList<>();

        for (JsonNode moveNode : movesNode) {
            String moveName = moveNode.get("move").get("name").asText().replaceAll("-", "");
            futures.add(CompletableFuture.supplyAsync(() -> processMove(pokemonModel, moveName, movesJson), executor));
        }

        List<PokemonMovesetModel> moveDetailsList = futures.stream()
                .map(future -> {
                    try {
                        return future.join(); //waits for other thread to finish
                    } catch (Exception e) {
                        logger.error("Error processing move for Pokémon: " + pokemonModel.getNameValue(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        executor.shutdown();

        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        logger.info("Move set retrieved for " + pokemonModel.getNameValue());

        return moveDetailsList;
    }


    private PokemonMovesetModel processMove(PokemonModel pokemonModel, String moveName, JsonNode movesJson) {

        JsonNode moveDetails = movesJson.get(moveName);

        if (moveDetails != null) {

            PokemonTypeEnum damageType = PokemonTypeEnum.valueOf(moveDetails.get("type").asText().toUpperCase());

            //only adding moves relevant to the Pokemon (their primary/secondary types and NORMAL dmg attacks
            if (isRelevantMove(pokemonModel, damageType)) {

                return PokemonMovesetModel.builder()
                        .id(moveId.incrementAndGet())
                        .moveName(moveName)
                        .pokedexNumber(pokemonModel.getPokedexNumber())
                        .power(moveDetails.get("basePower").asInt())
                        .accuracy(moveDetails.get("accuracy").asInt())
                        .effect(moveDetails.get("shortDesc").textValue().toLowerCase())
                        .damageType(damageType)
                        .isNormalType("normal".equalsIgnoreCase(moveDetails.get("category").textValue()))
                        .build();
            }
        } else {
            logger.info(String.format("Move %s was not found in the move list", moveName.toUpperCase()));
        }

        return null;
    }

    private boolean isRelevantMove(PokemonModel pokemonModel, PokemonTypeEnum damageType) {
        return (damageType == pokemonModel.getPokemonType() ||
                damageType == PokemonTypeEnum.NORMAL ||
                (pokemonModel.getSecondaryType() != null && damageType == pokemonModel.getSecondaryType()));
    }

}
