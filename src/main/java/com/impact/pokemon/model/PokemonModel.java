package com.impact.pokemon.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.impact.pokemon.enums.PokemonTypeEnum;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect
@Document(collection = "PokemonCollection")
public class PokemonModel {

    @Id
    private ObjectId id;

    private String nameValue;

    private PokemonTypeEnum pokemonType;

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
