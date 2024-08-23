package com.impact.pokemon.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonAutoDetect
public class SimulationModel {

    private final PokemonModel winner;
    private final PokemonModel loser;
    private final int remainingHealthForWinner;
    private final int numberOfTurnsWon;
    private final int specialAttacksUsed;

}
