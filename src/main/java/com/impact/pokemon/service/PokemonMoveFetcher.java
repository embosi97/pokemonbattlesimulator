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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class PokemonMoveFetcher {

    @Value("${pokeApi.url}")
    private String pokeApiUrl;

    private static final Logger logger = LoggerFactory.getLogger(PokemonMoveFetcher.class);

    private final ObjectMapper objectMapper;

    public PokemonMoveFetcher(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private String fetchPokemonData(int pokemonId) {
        HttpResponse<String> response;
        try {
            response = Unirest.get(pokeApiUrl +
                    URLEncoder.encode(String.valueOf(pokemonId), StandardCharsets.UTF_8)).asString();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
        return response.getBody();
    }

    public List<PokemonMovesetModel> fetchMovesForPokemon(PokemonModel pokemonModel) {
        String pokemonData = fetchPokemonData(pokemonModel.getPokedexNumber());

        JsonNode pokemonNode;
        JsonNode movesJson;

        try {
            //parse the JSON for the Pokémon and move details
            pokemonNode = objectMapper.readTree(pokemonData);
            movesJson = objectMapper.readTree(PokemonMoveFetcher.class.getResourceAsStream("/data/moves.json"));

        } catch (IOException e) {
            throw new RuntimeException("Error parsing JSON data", e);
        }

        //retrieving entire Pokemon move set
        JsonNode movesNode = pokemonNode.get("moves");

        //list to hold the detailed move information
        List<Future<PokemonMovesetModel>> futures = new ArrayList<>();

        //thread pool
        ExecutorService executor = Executors.newFixedThreadPool(10);

        //iterate through the Pokemon's moves and submit tasks to fetch and process move details
        for (JsonNode moveNode : movesNode) {

            String moveName = moveNode.get("move").get("name").asText().replaceAll("-", "");

            //submit each move processing task to the executor service
            futures.add(executor.submit(() -> processMove(pokemonModel, moveName, movesJson)));
        }

        //shutdown the executor service after tasks are submitted
        executor.shutdown();

        //collect the results from the futures
        List<PokemonMovesetModel> moveDetailsList = new ArrayList<>();
        for (Future<PokemonMovesetModel> future : futures) {
            try {
                PokemonMovesetModel moveDetail = future.get();
                if (moveDetail != null) {
                    moveDetailsList.add(moveDetail);
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        return moveDetailsList;
    }

    private PokemonMovesetModel processMove(PokemonModel pokemonModel, String moveName, JsonNode movesJson) {

        JsonNode moveDetails = movesJson.get(moveName);

        if (moveDetails != null) {
            PokemonTypeEnum damageType = PokemonTypeEnum.valueOf(moveDetails.get("type").asText().toUpperCase());

            //only adding moves relevant to the Pokemon
            if (damageType == pokemonModel.getPokemonType() ||
                    damageType == PokemonTypeEnum.NORMAL ||
                    (pokemonModel.getSecondaryType() != null && damageType == pokemonModel.getSecondaryType())) {

                return PokemonMovesetModel.builder()
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

}
