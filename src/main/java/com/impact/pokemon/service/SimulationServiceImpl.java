package com.impact.pokemon.service;

import com.impact.pokemon.data.PokemonData;
import com.impact.pokemon.enums.PokemonTypeEnum;
import com.impact.pokemon.model.PokemonModel;
import com.impact.pokemon.model.SimulationModel;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

@Getter
@Service
public class SimulationServiceImpl implements SimulationService {

    private final HashMap<String, PokemonModel> pokemonData;

    public SimulationServiceImpl() throws IOException, CsvValidationException {
        this.pokemonData = PokemonData.PokemonDataMapper();
    }

    //battle logic
    @Override
    public SimulationModel startSimulation(PokemonModel p1, PokemonModel p2) {
        PokemonModel pokemon1 = new PokemonModel(p1); //used a copy constructor to fix state issues (ex. it wouldn't change the hp for the same pokemon combos if done consecutively
        PokemonModel pokemon2 = new PokemonModel(p2);

        if (!pokemon1.getNameValue().equalsIgnoreCase(pokemon2.getNameValue())) { //cant be the same pokemon!
            int numberOfTurnsWon = 0; //counts number of turns till one of the pokemon die
            int specialsUsedCounter = 0; //counts the number of specials used in the match/battle

            PokemonModel attacker;
            PokemonModel defender;

            if (pokemon1.getSpeedValue() > pokemon2.getSpeedValue()) { //faster pokemon hits first always
                attacker = pokemon1;
                defender = pokemon2;
            } else if (pokemon2.getSpeedValue() > pokemon1.getSpeedValue()) {
                attacker = pokemon2;
                defender = pokemon1;
            } else {
                //random if speed is equal
                Random random = new Random();
                if (random.nextBoolean()) {
                    attacker = pokemon1;
                    defender = pokemon2;
                } else {
                    attacker = pokemon2;
                    defender = pokemon1;
                }
            }

            Random random = new Random();
            while (pokemon1.getHealth() > 0 && pokemon2.getHealth() > 0) {
                numberOfTurnsWon++;
                boolean useSpecialAttack = random.nextBoolean();
                double effectivenessValue = PokemonTypeEnum.getEffectivenessModifier(attacker.getPokemonType(), defender.getPokemonType());
                int damage;
                boolean isCriticalHit = random.nextDouble() < 0.0625; //random chance for a critical hit (x2 damage)

                if (useSpecialAttack) {
                    specialsUsedCounter++;
                    damage = (int) (50 * (attacker.getSpecialAttack() / (double) defender.getSpecialDefense()) * effectivenessValue);
                } else {
                    damage = (int) (50 * (attacker.getAttackValue() / (double) defender.getDefenseValue()) * effectivenessValue);
                }

                damage = (int) (damage * (0.85 + (random.nextDouble() * 0.3))); //dmg variance like in the games

                if (isCriticalHit) {
                    damage *= 2; //boom
                }

                defender.setHealth(defender.getHealth() - damage); //hp down

                //checks if the defender is defeated
                if (defender.getHealth() <= 0) {
                    return new SimulationModel(attacker, defender, attacker.getHealth(), numberOfTurnsWon, specialsUsedCounter);
                }
                //now switch turns
                PokemonModel temp = attacker;
                attacker = defender;
                defender = temp;
            }
        }
        return null;
    }
}