package com.impact.pokemon.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.impact.pokemon.enums.PokemonTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Id;

@AllArgsConstructor
@Getter
@Builder
@JsonAutoDetect
public class PokemonMovesetModel {

    @Id
    private Long id;

    private int pokedexNumber;

    private String moveName;

    private int accuracy;

    private int power;

    private PokemonTypeEnum damageType;

    private String effect;

    private boolean isNormalType;

}
