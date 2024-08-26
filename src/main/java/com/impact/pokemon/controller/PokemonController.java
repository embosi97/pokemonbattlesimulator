package com.impact.pokemon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.impact.pokemon.model.PokemonModel;
import com.impact.pokemon.model.SimulationModel;
import com.impact.pokemon.service.SimulationServiceImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@AllArgsConstructor
@RestController
@RequestMapping("/")
public class PokemonController {

    private static final Logger logger = LoggerFactory.getLogger(PokemonController.class);

    @Autowired
    private final SimulationServiceImpl simService;

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/attack")
    public ResponseEntity<String> simulateBattle(
            @RequestParam("player") String pokemon1,
            @RequestParam("computer") String pokemon2) throws IOException {

        Resource bannerRaw = resourceLoader.getResource("classpath:public/banner.html");

        if (!bannerRaw.exists()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("<h1>Banner resource not found.</h1>");
        }

        logger.info("Retrieving Pokemon data!!!");

        PokemonModel pokemonModel1 = simService.getPokemonData().get(pokemon1.toLowerCase());
        PokemonModel pokemonModel2 = simService.getPokemonData().get(pokemon2.toLowerCase());

        if (pokemonModel1 == null || pokemonModel2 == null) {
            logger.error("One or both Pokémon names are invalid: " + pokemon1 + ", " + pokemon2);
            return ResponseEntity.badRequest().body("<h1>Invalid Pokemon names provided.</h1><button onclick='window.history.back()'>Back</button>");
        }

        logger.info("Starting simulation!!!");

        //new state each time so that back button works
        SimulationModel result = simService.startSimulation(pokemonModel1, pokemonModel2);

        if (result == null || result.getWinner() == null) {
            logger.error("Simulation result is null or incomplete.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("<h1>An error occurred during the simulation.</h1><button onclick='window.history.back()'>Back</button>");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        //Sending JSON instead of raw html
        return ResponseEntity.ok(objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(result));
    }

}