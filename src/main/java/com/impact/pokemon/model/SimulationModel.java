package com.impact.pokemon.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

//Use a record when an object’s only purpose is to contain public data
@JsonAutoDetect
public record SimulationModel(PokemonModel winner, PokemonModel loser, int remainingHealthForWinner,
                              int numberOfTurnsWon, int specialAttacksUsed) {
}
