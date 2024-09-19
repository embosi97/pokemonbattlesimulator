package com.impact.pokemon.service;

import com.impact.pokemon.enums.PokemonTypeEnum;
import com.impact.pokemon.model.PokemonModel;
import com.impact.pokemon.model.SimulationModel;
import com.impact.pokemon.model.TurnWrapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Getter
@Service
public class SimulationServiceImpl implements SimulationService {

    private final PokemonModelRepoServiceImpl pokemonModelRepoService;

    @Autowired
    public SimulationServiceImpl(PokemonModelRepoServiceImpl pokemonModelRepoService) {
        this.pokemonModelRepoService = pokemonModelRepoService;
    }

    //battle logic
    @Override
    public SimulationModel startSimulation(PokemonModel pokemon1, PokemonModel pokemon2) {

        if (!pokemon1.getNameValue().equalsIgnoreCase(pokemon2.getNameValue())) { //can't be the same pokemon!

            int numberOfTurnsWon = 0; //counts number of turns till one of the pokemon die
            int specialsUsedCounter = 0; //counts the number of specials used in the match/battle

            TurnWrapper turnWrapper = getTurnWrapper(pokemon1, pokemon2);

            return commenceFight(turnWrapper, numberOfTurnsWon, specialsUsedCounter);
        }
        return null;
    }

    private SimulationModel commenceFight(TurnWrapper turnWrapper, int numberOfTurnsWon, int specialsUsedCounter) {
        Random random = new Random();
        while (true) {
            numberOfTurnsWon++;
            boolean useSpecialAttack = random.nextBoolean();
            double effectivenessValue = PokemonTypeEnum.getEffectivenessModifier(turnWrapper.getAttacker().getPokemonType(), turnWrapper.getDefender().getPokemonType());
            int damage;
            if(effectivenessValue != 0.0) {
                boolean isCriticalHit = random.nextDouble() < 0.0625; //random chance for a critical hit (x2 damage)

                if (useSpecialAttack) {
                    specialsUsedCounter++;
                    damage = (int) (50 * (turnWrapper.getAttacker().getSpecialAttack() / (double) turnWrapper.getDefender().getSpecialDefense()) * effectivenessValue);
                } else {
                    damage = (int) (50 * (turnWrapper.getAttacker().getAttackValue() / (double) turnWrapper.getDefender().getDefenseValue()) * effectivenessValue);
                }

                damage = (int) (damage * (0.85 + (random.nextDouble() * 0.3))); //dmg variance like in the games (85% to 115% to increase the RNG)

                if (isCriticalHit) {
                    damage *= 2; //boom
                }
            }
            else {
                damage = 0;
            }

            turnWrapper.getDefender()
                    .setHealth(turnWrapper.getDefender().getHealth() - damage); //hp down

            //checks if the defender is defeated
            if (turnWrapper.getDefender().getHealth() <= 0) {
                return new SimulationModel(turnWrapper.getAttacker(), turnWrapper.getDefender(),
                        turnWrapper.getAttacker().getHealth(), numberOfTurnsWon, specialsUsedCounter);
            }
            //now switch turns
            switchingTurns(turnWrapper);
        }
    }

    private static TurnWrapper getTurnWrapper(PokemonModel pokemon1, PokemonModel pokemon2) {
        TurnWrapper turnWrapper;

        if (pokemon1.getSpeedValue() > pokemon2.getSpeedValue()) { //faster pokemon hits first always
            turnWrapper = new TurnWrapper(pokemon1, pokemon2);
        } else if (pokemon2.getSpeedValue() > pokemon1.getSpeedValue()) {
            turnWrapper = new TurnWrapper(pokemon2, pokemon1);
        } else {
            //random if speed is equal
            Random random = new Random();
            if (random.nextBoolean()) {
                turnWrapper = new TurnWrapper(pokemon1, pokemon2);
            } else {
                turnWrapper = new TurnWrapper(pokemon2, pokemon1);
            }
        }
        return turnWrapper;
    }

    public void switchingTurns(TurnWrapper turnWrapper) {
        PokemonModel temp = turnWrapper.attacker;
        turnWrapper.attacker = turnWrapper.defender;
        turnWrapper.defender = temp;
    }
}