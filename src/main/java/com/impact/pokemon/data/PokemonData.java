package com.impact.pokemon.data;

import com.impact.pokemon.enums.PokemonTypeEnum;
import com.impact.pokemon.model.PokemonModel;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

/**
 * !! Feel free to change everything about this !!
 * This could be a class to hold all the Pokemon objects loaded from CSV,
 * but there are many ways to do it.
 */
public class PokemonData {

    public static HashMap<String, PokemonModel> PokemonDataMapper() throws IOException, CsvValidationException {
        HashMap<String, PokemonModel> map = new HashMap<>();
        final File file = new File("src/main/resources/data/pokemon.csv");
        CSVReader csvReader = new CSVReader(new FileReader(file));
        csvReader.readNext();
        String[] nextLine;
        while ((nextLine = csvReader.readNext()) != null) {
            PokemonModel pokemonModel = new PokemonModel(
                    nextLine[1],
                    Objects.requireNonNull(PokemonTypeEnum.fromValue(nextLine[2].toUpperCase(Locale.ROOT))).getType(),
                    Integer.parseInt(nextLine[3]),
                    Integer.parseInt(nextLine[4]),
                    Integer.parseInt(nextLine[5]),
                    Integer.parseInt(nextLine[6]),
                    Integer.parseInt(nextLine[7]),
                    Integer.parseInt(nextLine[8]),
                    Integer.parseInt(nextLine[9]),
                    Integer.parseInt(nextLine[10]),
                    Boolean.parseBoolean(nextLine[11])
            );
            map.put(pokemonModel.getNameValue().toLowerCase(), pokemonModel);

        }
        csvReader.close();
        return map;
    }
}