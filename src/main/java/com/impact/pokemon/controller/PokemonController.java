package com.impact.pokemon.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.impact.pokemon.model.PokemonModel;
import com.impact.pokemon.model.SimulationModel;
import com.impact.pokemon.repository.PokemonMovesetRepo;
import com.impact.pokemon.service.PokemonModelRepoServiceImpl;
import com.impact.pokemon.service.PokemonMoveFetcher;
import com.impact.pokemon.service.PokemonMovesetModelRepoServiceImpl;
import com.impact.pokemon.service.SimulationServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/")
public class PokemonController {

    private static final Logger logger = LoggerFactory.getLogger(PokemonController.class);

    @Autowired
    private final SimulationServiceImpl simService;

    @Autowired
    private final PokemonMoveFetcher pokemonMoveFetcher;

    @GetMapping("/attack")
    public ResponseEntity<String> simulateBattle(
            @RequestParam("player") String pokemon1,
            @RequestParam("computer") String pokemon2) {

        logger.info("Retrieving Pokemon data from Database!");

        List<Optional<PokemonModel>> pokemonList =
                simService.getPokemonModelRepoService().getPokemonByName(pokemon1.toLowerCase(), pokemon2.toLowerCase());

        Optional<PokemonModel> pokemonModel1 = pokemonList.get(0);
        Optional<PokemonModel> pokemonModel2 = pokemonList.get(1);


        if (pokemonModel1.isEmpty() || pokemonModel2.isEmpty()) {
            logger.error("One or both Pokemon names are invalid: " + pokemon1 + ", " + pokemon2);
            return ResponseEntity.badRequest().body("<h1>Invalid Pokemon names provided.</h1><button onclick='window.history.back()'>Back</button>");
        }

        pokemonMoveFetcher.fetchMovesForPokemon(pokemonModel1.get());

        logger.info(String.format("%s and %s enter the battle. Starting simulation!",
                StringUtils.capitalize(pokemonModel1.get().getNameValue()), StringUtils.capitalize(pokemonModel2.get().getNameValue())));

        SimulationModel result = simService.startSimulation(pokemonModel1.get(), pokemonModel2.get());

        if (result == null || result.winner() == null) {
            logger.error("Simulation result is null or incomplete.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("<h1>An error occurred during the simulation.</h1><button onclick='window.history.back()'>Back</button>");
        }

        logger.info(String.format("%s defeated %s with %d health points remaining!",
                StringUtils.capitalize(result.winner().getNameValue()), StringUtils.capitalize(result.loser().getNameValue()), result.winner().getHealth()));

        ObjectMapper objectMapper = new ObjectMapper();

        //Sending JSON
        try {
            return ResponseEntity.ok(objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(result));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/attack/random")
    public ResponseEntity<String> simulateRandomizedBattle() {

        logger.info("Retrieving Pokemon data from Database!");

        List<Optional<PokemonModel>> pokemonList =
                simService.getPokemonModelRepoService().getRandomPokemon();

        Optional<PokemonModel> pokemonModel1 = pokemonList.get(0);
        Optional<PokemonModel> pokemonModel2 = pokemonList.get(1);

        if (pokemonModel1.isEmpty() || pokemonModel2.isEmpty()) {
            logger.error("There was a failure in the database");
            return ResponseEntity.badRequest().body("<h1>There was a failure in the database. Please try again.</h1><button onclick='window.history.back()'>Back</button>");
        }

        logger.info(String.format("%s and %s enter the battle. Starting simulation!",
                StringUtils.capitalize(pokemonModel1.get().getNameValue()), StringUtils.capitalize(pokemonModel2.get().getNameValue())));

        SimulationModel result = simService.startSimulation(pokemonModel1.get(), pokemonModel2.get());

        if (result == null || result.winner() == null) {
            logger.error("Simulation result is null or incomplete.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("<h1>An error occurred during the simulation.</h1><button onclick='window.history.back()'>Back</button>");
        }

        logger.info(String.format("%s defeated %s with %d health points remaining!",
                StringUtils.capitalize(result.winner().getNameValue()), StringUtils.capitalize(result.loser().getNameValue()), result.winner().getHealth()));

        ObjectMapper objectMapper = new ObjectMapper();

        //Sending JSON
        try {
            return ResponseEntity.ok(objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(result));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}