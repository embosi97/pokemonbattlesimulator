package com.impact.pokemon.service;

import com.impact.pokemon.enums.PokemonTypeEnum;
import com.impact.pokemon.model.PokemonModel;
import com.impact.pokemon.repository.PokemonModelRepo;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
public class PokemonModelRepoServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(PokemonModelRepoServiceImpl.class);
    private final PokemonModelRepo pokemonModelRepo;

    @Autowired
    public PokemonModelRepoServiceImpl(PokemonModelRepo pokemonModelRepo) {
        this.pokemonModelRepo = pokemonModelRepo;
    }

    private boolean doesPokemonDataExist() {
        return pokemonModelRepo.count() > 0;
    }

    public List<Optional<PokemonModel>> getPokemonByName(String pokemon1, String pokemon2) throws CsvValidationException, IOException {
        if (!doesPokemonDataExist()) {
            logger.info("Populating the database with Pokemon data.");
            populatePokemonDatabase();
        }
        return new ArrayList<>(List.of(pokemonModelRepo.findByNameValue(pokemon1.toLowerCase()), pokemonModelRepo.findByNameValue(pokemon2.toLowerCase())));
    }

    public void populatePokemonDatabase() throws IOException, CsvValidationException {
        List<PokemonModel> pokemonModelList = new ArrayList<>();
        final InputStream inputStream = PokemonModelRepoServiceImpl.class.getResourceAsStream("/data/pokemondata.csv");
        CSVReader csvReader = new CSVReader(new InputStreamReader(Objects.requireNonNull(inputStream)));
        csvReader.readNext();
        String[] nextLine;
        while ((nextLine = csvReader.readNext()) != null) {

            PokemonModel pokemonModel = PokemonModel.builder()
                    .pokedexNumber(Integer.parseInt(nextLine[0]))
                    .nameValue(nextLine[1].toLowerCase())
                    .pokemonType(Objects.requireNonNull(PokemonTypeEnum.fromValue(nextLine[2].toUpperCase(Locale.ROOT))))
                    .totalStats(Integer.parseInt(nextLine[3]))
                    .health(Integer.parseInt(nextLine[4]))
                    .attackValue(Integer.parseInt(nextLine[5]))
                    .defenseValue(Integer.parseInt(nextLine[6]))
                    .specialAttack(Integer.parseInt(nextLine[7]))
                    .specialDefense(Integer.parseInt(nextLine[8]))
                    .speedValue(Integer.parseInt(nextLine[9]))
                    .generation(Integer.parseInt(nextLine[10]))
                    .isLegendary(Boolean.parseBoolean(nextLine[11]))
                    .build();

            pokemonModelList.add(pokemonModel);
        }
        csvReader.close();
        pokemonModelRepo.saveAll(pokemonModelList);
    }
}
