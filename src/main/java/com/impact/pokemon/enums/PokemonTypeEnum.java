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

    public static double getEffectivenessModifier(Character type1, Character type2) {
        switch (type1) {
            case 'f':
                switch (type2) {
                    case 'g':
                        return 2.0;
                    case 'w':
                        return 0.5;
                    case 'e':
                        return 1.0;
                }
                break;
            case 'w':
                switch (type2) {
                    case 'f':
                        return 2.0;
                    case 'e', 'g':
                        return 0.5;
                }
                break;
            case 'g':
                switch (type2) {
                    case 'e':
                        return 2.0;
                    case 'f':
                        return 0.5;
                    case 'w':
                        return 1.0;
                }
                break;
            case 'e':
                switch (type2) {
                    case 'w':
                        return 2.0;
                    case 'g':
                        return 0.5;
                    case 'f':
                        return 1.0;
                }
                break;
        }
        return 1.0; // Default effectiveness is neutral (1.0)
    }
}