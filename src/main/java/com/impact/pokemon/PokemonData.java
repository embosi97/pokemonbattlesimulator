package com.impact.pokemon;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * !! Feel free to change everything about this !!
 * This could be a class to hold all the Pokemon objects loaded from CSV,
 * but there are many ways to do it.
 */
@Component
public class PokemonData {
    private final File file;

    PokemonData() throws IOException {
        file = new ClassPathResource("data/pokemon.csv").getFile();
    }

    File getFile() {
        return file;
    }
}
