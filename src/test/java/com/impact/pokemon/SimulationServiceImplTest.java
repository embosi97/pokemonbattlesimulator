package com.impact.pokemon;

import com.impact.pokemon.enums.PokemonTypeEnum;
import com.impact.pokemon.model.PokemonModel;
import com.impact.pokemon.model.SimulationModel;
import com.impact.pokemon.model.TurnWrapper;
import com.impact.pokemon.service.SimulationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class SimulationServiceImplTest {

    @InjectMocks
    private SimulationServiceImpl simulationService;

    @Mock
    private HashMap<String, PokemonModel> pokemonData;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); //for pokemon data
    }

    @Test
    public void testStartSimulation_Pokemon1Wins() {

        PokemonModel pokemon1 =
                buildDummyPokemonModel("Pikachu", PokemonTypeEnum.ELECTRIC, 1000, 735, 95, 40, 50, 50, 90);

        PokemonModel pokemon2 =
                buildDummyPokemonModel("Squirtle", PokemonTypeEnum.WATER, 1000, 135, 49, 40, 50, 50, 50);


        when(pokemonData.get("pikachu")).thenReturn(pokemon1);
        when(pokemonData.get("squirtle")).thenReturn(pokemon2);

        SimulationModel result = simulationService.startSimulation(pokemon1, pokemon2);

        assertNotNull(result);
        assertEquals(pokemon1.getNameValue(), result.winner().getNameValue());
        assertTrue(result.loser().getHealth() <= 0);
    }

    @Test
    public void testStartSimulation_Pokemon2Wins() {

        PokemonModel pokemon1 =
                buildDummyPokemonModel("Charmander", PokemonTypeEnum.FIRE, 1000, 135, 35, 40, 50, 50, 20);

        PokemonModel pokemon2 =
                buildDummyPokemonModel("Squirtle", PokemonTypeEnum.WATER, 1000, 335, 85, 40, 50, 50, 1000);

        when(pokemonData.get("charmander")).thenReturn(pokemon1);
        when(pokemonData.get("squirtle")).thenReturn(pokemon2);

        SimulationModel result = simulationService.startSimulation(pokemon1, pokemon2);

        assertNotNull(result);
        assertEquals(pokemon2.getNameValue(), result.winner().getNameValue());
        assertTrue(result.loser().getHealth() <= 0);
    }

    @Test
    public void testStartSimulation_SamePokemon() {

        PokemonModel pokemon =
                buildDummyPokemonModel("Pikachu", PokemonTypeEnum.ELECTRIC, 1000, 735, 95, 40, 50, 50, 90);

        when(pokemonData.get("pikachu")).thenReturn(pokemon);

        SimulationModel result = simulationService.startSimulation(pokemon, pokemon);

        assertNull(result);
    }

    @Test
    public void testSwitchingTurns() {

        PokemonModel pokemon1 =
                buildDummyPokemonModel("Pikachu", PokemonTypeEnum.ELECTRIC, 1000, 735, 95, 40, 50, 50, 90);

        PokemonModel pokemon2 =
                buildDummyPokemonModel("Squirtle", PokemonTypeEnum.WATER, 1000, 135, 49, 40, 50, 50, 50);

        TurnWrapper turnWrapper = new TurnWrapper(pokemon1, pokemon2);

        simulationService.switchingTurns(turnWrapper);

        assertEquals(turnWrapper.attacker.getNameValue(), pokemon2.getNameValue());
        assertEquals(turnWrapper.defender.getNameValue(), pokemon1.getNameValue());
    }


    public PokemonModel buildDummyPokemonModel(String name, PokemonTypeEnum pokemonTypeEnum, int totalStats, int health, int attackValue,
                                               int defenseValue, int specialAtt, int specialDef, int speed) {
        return PokemonModel.builder()
                .nameValue(name)
                .pokemonType(pokemonTypeEnum)
                .totalStats(totalStats)
                .health(health)
                .attackValue(attackValue)
                .defenseValue(defenseValue)
                .specialAttack(specialAtt)
                .specialDefense(specialDef)
                .speedValue(speed)
                .generation(1)
                .isLegendary(false)
                .build();
    }
}