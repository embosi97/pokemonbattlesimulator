package com.impact.pokemon.controller;

import com.impact.pokemon.data.PokemonData;
import com.impact.pokemon.model.PokemonModel;
import com.impact.pokemon.model.SimulationModel;
import com.impact.pokemon.service.SimulationServiceImpl;
import com.opencsv.exceptions.CsvValidationException;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
            @RequestParam("computer") String pokemon2) throws CsvValidationException, IOException {

        Resource bannerRaw = resourceLoader.getResource("classpath:public/banner.html");

        if (!bannerRaw.exists()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("<h1>Banner resource not found.</h1>");
        }

        String bannerHtml;
        try {
            Path bannerPath = Paths.get(bannerRaw.getURI());
            bannerHtml = Files.readString(bannerPath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("<h1>Failed to read banner resource.</h1>");
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

        String htmlResponse = "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                bannerHtml +
                "<title>Pokémon Battle Result</title>" +
                "<link href='https://cdn.jsdelivr.net/npm/bulma@0.9.4/css/bulma.min.css' rel='stylesheet'>" +
                "<style>" +
                "body { text-align: center; font-family: Arial, sans-serif; }" +
                ".battle-container { display: flex; justify-content: center; align-items: center; margin-top: 50px; }" +
                ".pokemon-card { margin: 20px; }" +
                ".pokemon-card img { max-width: 150px; }" +
                ".result { font-size: 1.5em; margin-top: 30px; }" +
                ".back-button { margin-top: 20px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h1 class='title'>Pokemon Battle Result</h1>" +
                "<div class='battle-container'>" +
                "<div class='pokemon-card'>" +
                "<h1><strong>" + pokemonModel1.getNameValue() + "</strong></h1>" +
                "<img src='https://img.pokemondb.net/sprites/home/normal/" + pokemonModel1.getNameValue().toLowerCase() + ".png' alt='" + pokemonModel1.getNameValue() + "'>" +
                "</div>" +
                "<div class='pokemon-card'>" +
                "<h1><strong>" + pokemonModel2.getNameValue() + "</strong></h1>" +
                "<img src='https://img.pokemondb.net/sprites/home/normal/" + pokemonModel2.getNameValue().toLowerCase() + ".png' alt='" + pokemonModel2.getNameValue() + "'>" +
                "</div>" +
                "</div>" +
                "<div class='result'>" +
                "<p><strong>Winner:</strong> " + result.getWinner().getNameValue() + "!</p>" +
                "<p><strong>Remaining HP:</strong> " + result.getWinner().getHealth() + "/" + PokemonData.PokemonDataMapper().get(result.getWinner().getNameValue().toLowerCase()).getHealth() + (result.getNumberOfTurnsWon() == 1 ? " Perfect!" : "") + "</p>" +
                "<p><strong>" + result.getWinner().getNameValue() + " won in " + result.getNumberOfTurnsWon() + " turn(s)!</strong></p>" +
                "<p><strong>" + result.getWinner().getNameValue() + (result.getWinner().isLegendary() ? " is classified as a" : " is not classified as a") + " Legendary Pokemon.</strong></p>" +
                "<p><strong>" + result.getWinner().getNameValue() + " used " + result.getSpecialAttacksUsed() + " Special Attacks.</strong></p>" +
                "</div>" +
                "<div class='back-button'>" +
                "<button class='button is-link' onclick='window.history.back()'>Back</button>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        return ResponseEntity.ok(htmlResponse);
    }

}