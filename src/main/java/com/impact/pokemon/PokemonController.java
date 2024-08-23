package com.impact.pokemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@RestController
public class PokemonController {

    private static final Logger logger = LoggerFactory.getLogger(PokemonController.class);

    @Resource
    private PokemonData data;

    @GetMapping("attack")
    public Map<String, Object> attack(String pokemonA, String pokemonB) throws IOException {
        logger.info("Requested pokemonA: {}, pokemonB: {}", pokemonA, pokemonB);

        // This is just an example of how to read the file contents into a List. Change or refactor as needed
        List<String> pokemon = Files.readAllLines(data.getFile().toPath());

        // This is just an example of a response that is hardcoded - Change or refactor as needed
        return Map.of(
                "winner", "Bulbasaur",
                "hitPoints", 120);
    }
}
