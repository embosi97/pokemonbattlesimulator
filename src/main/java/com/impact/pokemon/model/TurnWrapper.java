package com.impact.pokemon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TurnWrapper {
    public PokemonModel attacker;
    public PokemonModel defender;

}