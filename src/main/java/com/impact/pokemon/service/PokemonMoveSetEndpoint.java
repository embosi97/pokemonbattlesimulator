package com.impact.pokemon.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.impact.pokemon.enums.PokemonTypeEnum;
import com.impact.pokemon.model.PokemonModel;
import com.impact.pokemon.model.PokemonMoveModel;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PokemonMoveSetEndpoint {

    @Value("${graphQLApi.url}")
    private String graphQLApi;

    private static final Logger logger = LoggerFactory.getLogger(PokemonMoveSetEndpoint.class);

    private final com.fasterxml.jackson.databind.JsonNode movesJson; //no need to read it multiple times anymore

    private final ObjectMapper objectMapper;

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    private final AtomicInteger moveId = new AtomicInteger(0);

    public PokemonMoveSetEndpoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        try {
            movesJson = objectMapper.readTree(PokemonMoveSetEndpoint.class.getResourceAsStream("/data/moves.json"));
        } catch (IOException e) {
            logger.error("Error loading moves.json", e);
            throw new RuntimeException("Error loading moves.json", e);
        }
    }

    private synchronized String fetchMoveSetDataByPokemon(String pokemonName) {

        //using distinct on to remove dupes (on move_id)
        String query = String.format(
                "{ pokemon_v2_pokemon(where: {name: {_eq: \\\"%s\\\"}}) { id name pokemon_v2_pokemonmoves(distinct_on: [move_id]) { pokemon_v2_move { name id } } } }",
                pokemonName
        );

        //JSON Payload for GraphQL query
        String graphqlQuery = String.format("{\"query\": \"%s\"}", query);

        HttpResponse<JsonNode> response;

        Unirest.setTimeouts(10000, 10000);

        try {
            logger.info("Making request for Pokemon: " + pokemonName); //no rate limiter for this API
            response = Unirest.post(graphQLApi)
                    .header("Content-Type", "application/json")
                    .body(graphqlQuery)
                    .asJson();

            logger.info("Response Code for this request was: " + response.getCode());

            if (response.getCode() != 200) {
                logger.error("Failed to fetch data for Pokemon " + pokemonName + ": " + response.getBody());
                throw new RuntimeException("Failed to fetch data: " + response.getBody());
            }
        } catch (UnirestException e) {
            logger.error("Error during API call for Pokemon: " + pokemonName, e);
            throw new RuntimeException("Error during API call", e);
        }

        return response.getBody().toString();
    }

    public List<PokemonMoveModel> fetchMovesForPokemon(PokemonModel pokemonModel) {

        String pokemonData = fetchMoveSetDataByPokemon(pokemonModel.getNameValue());

        com.fasterxml.jackson.databind.JsonNode pokemonNode;

        try {
            //parse the JSON data for the Pokemon's move set from the GraphQL API
            pokemonNode = objectMapper.readTree(pokemonData);

        } catch (IOException e) {
            logger.error("Error parsing JSON data for Pokemon: " + pokemonModel.getNameValue(), e);
            throw new RuntimeException("Error parsing JSON data", e);
        }

        //retrieve moves for Pokemon from GraphQL response
        com.fasterxml.jackson.databind.JsonNode movesNode = pokemonNode.at("/data/pokemon_v2_pokemon/0/pokemon_v2_pokemonmoves");

        if (movesNode == null || movesNode.isEmpty()) {
            String message = "No moves found for Pokemon: " + pokemonModel.getNameValue();
            logger.error(message);
            throw new RuntimeException(message);
        }

        List<CompletableFuture<PokemonMoveModel>> futures = StreamSupport.stream(movesNode.spliterator(), false)
                .map(moveNode ->
                {
                    String moveName = moveNode.at("/pokemon_v2_move/name").asText().replaceAll("-", "");
                    return CompletableFuture.supplyAsync(() -> processMove(pokemonModel, moveName, movesJson), executor);
                })
                .toList();

        List<PokemonMoveModel> moveDetailsList = futures.stream()
                .map(this::getMoveDetailsFromFuture)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        logger.info("Move set retrieved for " + pokemonModel.getNameValue());
        return moveDetailsList;
    }

    @PreDestroy
    private void shutdownExecutor() {
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

    private PokemonMoveModel getMoveDetailsFromFuture(CompletableFuture<PokemonMoveModel> future) {
        try {
            return future.join();
        } catch (Exception e) {
            logger.error("Error processing move for Pokemon", e);
            return null;
        }
    }

    private PokemonMoveModel processMove(PokemonModel pokemonModel, String moveName, com.fasterxml.jackson.databind.JsonNode movesJson) {

        com.fasterxml.jackson.databind.JsonNode moveDetails = movesJson.get(moveName);

        if (moveDetails != null) {

            PokemonTypeEnum damageType = PokemonTypeEnum.valueOf(moveDetails.get("type").asText().toUpperCase());

            //only adding moves relevant to the Pokemon (their primary/secondary types and NORMAL dmg attacks
            if (isRelevantMove(pokemonModel, damageType)) {

                return PokemonMoveModel.builder()
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
