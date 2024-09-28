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

        //Parse the move list from the JSON response
        JsonNode pokemonNode;
        JsonNode movesJson;

        try {
            //Parse the JSON for the Pokémon and move details
            pokemonNode = objectMapper.readTree(pokemonData);
            movesJson = objectMapper.readTree(PokemonMoveFetcher.class.getResourceAsStream("/data/moves.json"));

        } catch (IOException e) {
            throw new RuntimeException("Error parsing JSON data", e);
        }
        //Retrieving entire pokemon move set
        JsonNode movesNode = pokemonNode.get("moves");

        //List to hold the detailed move information
        List<PokemonMovesetModel> moveDetailsList = new ArrayList<>();

        //Iterate through the Pokémon's moves and retrieve details from moves.json
        for (JsonNode moveNode : movesNode) {

            String moveName = moveNode.get("move").get("name").asText().replaceAll("-", "");

            JsonNode moveDetails = movesJson.get(moveName);

            if (moveDetails != null) {

                PokemonTypeEnum damageType = PokemonTypeEnum.valueOf(movesJson.get(moveName).get("type").asText().toUpperCase());

                //Only adding moves relevant to the Pokemon, no learned moves (ex. Pikachu learning fighting type attacks)
                if (damageType == pokemonModel.getPokemonType() ||
                        damageType == PokemonTypeEnum.NORMAL ||
                        pokemonModel.getSecondaryType() != null &&
                                damageType == pokemonModel.getSecondaryType()) {

                    PokemonMovesetModel moveDetail = PokemonMovesetModel.builder()
                            .moveName(moveName)
                            .pokedexNumber(pokemonModel.getPokedexNumber())
                            .power(moveDetails.get("basePower").asInt())
                            .accuracy(moveDetails.get("accuracy").asInt())
                            .effect(moveDetails.get("shortDesc").textValue().toLowerCase())
                            .damageType(damageType)
                            .isNormalType("normal".equalsIgnoreCase(moveDetails.get("category").textValue()))
                            .build();

                    moveDetailsList.add(moveDetail);
                }
            } else {
                logger.info(String.format("Move %s was not found in the move list", moveName.toUpperCase()));
            }
        }
        return moveDetailsList;
    }

}
