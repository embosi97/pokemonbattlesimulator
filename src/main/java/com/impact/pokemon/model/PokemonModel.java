package com.impact.pokemon.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect
public class PokemonModel {
    /*
     Name,Type,Total,HitPoints,Attack,Defense,SpecialAttack,SpecialDefense,Speed,Generation,Legendary
     */
    public PokemonModel(PokemonModel other) {
        this.nameValue = other.nameValue;
        this.pokemonType = other.pokemonType;
        this.health = other.health;
        this.attackValue = other.attackValue;
        this.defenseValue = other.defenseValue;
        this.specialAttack = other.specialAttack;
        this.specialDefense = other.specialDefense;
        this.speedValue = other.speedValue;
        this.generation = other.generation;
        this.isLegendary = other.isLegendary;
    }

    private String nameValue;

    private Character pokemonType;

    private int totalStats;

    private int health;

    private int attackValue;

    private int defenseValue;

    private int specialAttack;

    private int specialDefense;

    private int speedValue;

    private int generation;

    private boolean isLegendary;
}
