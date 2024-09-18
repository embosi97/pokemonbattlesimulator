package com.impact.pokemon.enums;

import lombok.Getter;

@Getter
public enum PokemonTypeEnum {

    FIRE('f'),
    WATER('w'),
    GRASS('g'),
    ELECTRIC('e');

    private final Character type;

    PokemonTypeEnum(Character type) {
        this.type = type;
    }

    public static PokemonTypeEnum fromValue(String key) {
        for (PokemonTypeEnum se : PokemonTypeEnum.values()) {
            if (se.name().equals(key)) {
                return se;
            }
        }
        return null;
    }

    //gets the multiplier based on attacker's type and the opponent's type
    public static double getEffectivenessModifier(PokemonTypeEnum type1, PokemonTypeEnum type2) {
        switch (type1.getType()) {
            case 'f':
                switch (type2.getType()) {
                    case 'g':
                        return 2.0;
                    case 'w':
                        return 0.5;
                    case 'e':
                        return 1.0;
                }
                break;
            case 'w':
                switch (type2.getType()) {
                    case 'f':
                        return 2.0;
                    case 'e', 'g':
                        return 0.5;
                }
                break;
            case 'g':
                switch (type2.getType()) {
                    case 'e':
                        return 2.0;
                    case 'f':
                        return 0.5;
                    case 'w':
                        return 1.0;
                }
                break;
            case 'e':
                switch (type2.getType()) {
                    case 'w':
                        return 2.0;
                    case 'g':
                        return 0.5;
                    case 'f':
                        return 1.0;
                }
                break;
        }
        //default modifier (no multiplier applied to dmg)
        return 1.0;
    }
}