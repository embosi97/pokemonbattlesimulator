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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PokemonModelRepoServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(PokemonModelRepoServiceImpl.class);

    private final PokemonModelRepo pokemonModelRepo;

    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Autowired
    public PokemonModelRepoServiceImpl(PokemonModelRepo pokemonModelRepo) {
        this.pokemonModelRepo = pokemonModelRepo;
    }

    private boolean doesPokemonDataExist() {
        return pokemonModelRepo.count() == 0;
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

    private void populatePokemonDatabase() {

        final InputStream inputStream = PokemonModelRepoServiceImpl.class.getResourceAsStream("/data/pokemongen1to5.csv");

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {

            csvReader.readNext(); //skip header

            //read all lines into a list
            List<String[]> allLines = new ArrayList<>();
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                allLines.add(nextLine);
            }

            List<CompletableFuture<PokemonModel>> futures = new ArrayList<>();

            for (String[] line : allLines) {
                futures.add(CompletableFuture.supplyAsync(() -> createPokemonModel(line), executor));
            }

            //results from futures
            List<PokemonModel> pokemonModelList = futures.stream().
                    map(future -> {
                        try {
                            return future.join();
                        } catch (CompletionException e) {
                            logger.error("Error creating Pokemon model: " + e.getCause());
                            return null;
                        }
                    }) //wait for all to complete and get results
                    .filter(Objects::nonNull)
                    .toList();

            pokemonModelRepo.saveAll(pokemonModelList);

            logger.info("All Pokemon Models were saved.");

        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        } finally {
            executorShutdown();
        }
    }

    private void executorShutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    //using Lombok's builder method
    private PokemonModel createPokemonModel(String[] line) {
        return PokemonModel.builder()
                .pokedexNumber(Integer.parseInt(line[0]))
                .nameValue(filterSpecialCharactersFromName(line[1].toLowerCase()))
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

    private String filterSpecialCharactersFromName(String pokemonName) {
        return pokemonName
                .replaceAll("[.']", "")
                .replaceAll("\\s", "-")
                .replace("â™‚", "a-m") //29
                .replace("â™€", "a-f"); //32
    }

    private Optional<PokemonTypeEnum> checkSecondaryType(String secondaryType) {
        if (secondaryType.equalsIgnoreCase("NULL")) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(PokemonTypeEnum.fromValue(secondaryType));
        }
    }

    private PokemonTypeEnum returnSecondaryEnum(String secondaryType) {
        return checkSecondaryType(secondaryType).orElse(null);
    }
}