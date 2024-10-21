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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PokemonModelRepoServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(PokemonModelRepoServiceImpl.class);

    private final PokemonModelRepo pokemonModelRepo;

    @Autowired
    public PokemonModelRepoServiceImpl(PokemonModelRepo pokemonModelRepo) {
        this.pokemonModelRepo = pokemonModelRepo;
    }

    private boolean doesPokemonDataExist() {
        return pokemonModelRepo.count() <= 0;
    }

    public List<Optional<PokemonModel>> getPokemonByName(String pokemon1, String pokemon2) {
        if (doesPokemonDataExist()) {
            logger.info("Populating the database with Pokemon data.");
            populatePokemonDatabase();
        }
        return Stream.of(pokemon1, pokemon2)
                .map(pokemonModelRepo::findByNameValue)
                .collect(Collectors.toList());
    }

    public List<Optional<PokemonModel>> getRandomPokemon() {
        if (doesPokemonDataExist()) {
            logger.info("Populating the database with Pokemon data.");
            populatePokemonDatabase();
        }
        int dbCount = Long.valueOf(pokemonModelRepo.count()).intValue();
        Random random = new Random();

        Set<Integer> randomIds = new HashSet<>();

        //2 unique id values
        while (randomIds.size() < 2) {
            randomIds.add(random.nextInt(dbCount) + 1);
        }

        return randomIds.stream()
                .map(pokemonModelRepo::findById)
                .collect(Collectors.toList());
    }

    public void populatePokemonDatabase() {

        final InputStream inputStream = PokemonModelRepoServiceImpl.class.getResourceAsStream("/data/pokemongen1to5.csv");
        CSVReader csvReader = new CSVReader(new InputStreamReader(Objects.requireNonNull(inputStream)));

        List<PokemonModel> pokemonModelList = new ArrayList<>();

        try {
            csvReader.readNext(); //skip header

            //read all lines into a list
            List<String[]> allLines = new ArrayList<>();
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                allLines.add(nextLine);
            }

            csvReader.close();

            //thread pool
            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            List<Future<PokemonModel>> futures = new ArrayList<>();

            for (String[] line : allLines) {
                futures.add(executor.submit(() -> createPokemonModel(line)));
            }

            //results from futures
            for (Future<PokemonModel> future : futures) {
                try {
                    PokemonModel pokemonModel = future.get();
                    if (pokemonModel != null) {
                        pokemonModelList.add(pokemonModel);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

            executor.shutdown();

        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }

        pokemonModelRepo.saveAll(pokemonModelList);
    }

    //using Lombok's builder method
    private PokemonModel createPokemonModel(String[] line) {
        return PokemonModel.builder()
                .pokedexNumber(Integer.parseInt(line[0]))
                .nameValue(line[1].toLowerCase())
                .pokemonType(Objects.requireNonNull(PokemonTypeEnum.fromValue(line[2].toUpperCase(Locale.ROOT))))
                .secondaryType(returnSecondaryEnum(line[3].toUpperCase(Locale.ROOT)))
                .totalStats(Integer.parseInt(line[4]))
                .health(Integer.parseInt(line[5]))
                .attackValue(Integer.parseInt(line[6]))
                .defenseValue(Integer.parseInt(line[7]))
                .specialAttack(Integer.parseInt(line[8]))
                .specialDefense(Integer.parseInt(line[9]))
                .speedValue(Integer.parseInt(line[10]))
                .generation(Integer.parseInt(line[11]))
                .isLegendary(Boolean.parseBoolean(line[12]))
                .build();
    }

    public Optional<PokemonTypeEnum> checkSecondaryType(String secondaryType) {
        if (secondaryType.equalsIgnoreCase("NULL")) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(PokemonTypeEnum.fromValue(secondaryType));
        }
    }

    public PokemonTypeEnum returnSecondaryEnum(String secondaryType) {
        return checkSecondaryType(secondaryType).orElse(null);
    }
}