package com.impact.pokemon;

import com.impact.pokemon.model.PokemonModel;
import com.impact.pokemon.model.SimulationModel;
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
        PokemonModel pokemon1 = new PokemonModel();

        pokemon1.setNameValue("Pikachu");
        pokemon1.setPokemonType('e');
        pokemon1.setHealth(735);
        pokemon1.setAttackValue(55);
        pokemon1.setDefenseValue(40);
        pokemon1.setSpecialAttack(50);
        pokemon1.setSpecialDefense(50);
        pokemon1.setSpeedValue(90);

        PokemonModel pokemon2 = new PokemonModel();
        pokemon2.setNameValue("Squirtle");
        pokemon2.setPokemonType('w');
        pokemon2.setHealth(745);
        pokemon2.setAttackValue(49);
        pokemon2.setDefenseValue(49);
        pokemon2.setSpecialAttack(65);
        pokemon2.setSpecialDefense(65);
        pokemon2.setSpeedValue(45);

        when(pokemonData.get("pikachu")).thenReturn(pokemon1);
        when(pokemonData.get("bulbasaur")).thenReturn(pokemon2);

        SimulationModel result = simulationService.startSimulation(pokemon1, pokemon2);

        assertNotNull(result);
        assertEquals(pokemon1.getNameValue(), result.getWinner().getNameValue());
        assertTrue(result.getLoser().getHealth() <= 0);
    }

    @Test
    public void testStartSimulation_Pokemon2Wins() {
        PokemonModel pokemon1 = new PokemonModel();
        pokemon1.setHealth(50);
        pokemon1.setAttackValue(30);
        pokemon1.setDefenseValue(40);
        pokemon1.setSpeedValue(70);
        pokemon1.setPokemonType('f');
        pokemon1.setNameValue("Charmander");

        PokemonModel pokemon2 = new PokemonModel();
        pokemon2.setHealth(1000);
        pokemon2.setPokemonType('w');
        pokemon2.setAttackValue(50);
        pokemon2.setDefenseValue(20);
        pokemon2.setSpeedValue(60);
        pokemon2.setNameValue("Squirtle");

        when(pokemonData.get("charmander")).thenReturn(pokemon1);
        when(pokemonData.get("squirtle")).thenReturn(pokemon2);

        SimulationModel result = simulationService.startSimulation(pokemon1, pokemon2);

        assertNotNull(result);
        assertEquals(pokemon2.getNameValue(), result.getWinner().getNameValue());
        assertTrue(result.getLoser().getHealth() <= 0);
    }

    @Test
    public void testStartSimulation_SamePokemon() {
        PokemonModel pokemon = new PokemonModel();
        pokemon.setHealth(100);
        pokemon.setAttackValue(50);
        pokemon.setDefenseValue(30);
        pokemon.setSpeedValue(60);
        pokemon.setPokemonType('e');
        pokemon.setNameValue("Pikachu");

        when(pokemonData.get("pikachu")).thenReturn(pokemon);

        SimulationModel result = simulationService.startSimulation(pokemon, pokemon);

        assertNull(result);
    }
}
