package com.impact.pokemon.enums;

import lombok.Getter;

@Getter
public enum PokemonTypeEnum {

    //Supported Pokemon types (Generation 1 to 5)
    NORMAL('n'),
    FIRE('f'),
    WATER('w'),
    GRASS('r'),
    ELECTRIC('e'),
    ICE('i'),
    FIGHTING('t'),
    POISON('p'),
    GROUND('d'),
    FLYING('y'),
    PSYCHIC('s'),
    BUG('b'),
    ROCK('k'),
    GHOST('h'),
    DRAGON('j'),
    DARK('a'),
    STEEL('l'),
    FAIRY('x');

    private final char type;

    PokemonTypeEnum(char type) {
        this.type = type;
    }

    public static PokemonTypeEnum fromValue(String key) {
        for (PokemonTypeEnum se : PokemonTypeEnum.values()) {
            if (se.name().equalsIgnoreCase(key)) {
                return se;
            }
        }
        return null;
    }

    //Gets the multiplier based on attacker's type and the opponent's type
    public static double getEffectivenessModifier(PokemonTypeEnum attacker, PokemonTypeEnum defender) {
        switch (attacker.getType()) {
            case 'n': // Normal
                switch (defender.getType()) {
                    case 'k': // Rock
                    case 'l': // Steel
                        return 0.5;
                    case 'h': // Ghost
                        return 0.0;
                }
                break;
            case 'f': // Fire
                switch (defender.getType()) {
                    case 'r': // Grass
                    case 'i': // Ice
                    case 'b': // Bug
                    case 'l': // Steel
                        return 2.0;
                    case 'f': // Fire
                    case 'w': // Water
                    case 'k': // Rock
                    case 'j': // Dragon
                        return 0.5;
                }
                break;
            case 'w': // Water
                switch (defender.getType()) {
                    case 'f': // Fire
                    case 'd': // Ground
                    case 'k': // Rock
                        return 2.0;
                    case 'w': // Water
                    case 'r': // Grass
                    case 'j': // Dragon
                        return 0.5;
                }
                break;
            case 'r': // Grass
                switch (defender.getType()) {
                    case 'w': // Water
                    case 'd': // Ground
                    case 'k': // Rock
                        return 2.0;
                    case 'f': // Fire
                    case 'r': // Grass
                    case 'y': // Flying
                    case 'p': // Poison
                    case 'b': // Bug
                    case 'l': // Steel
                        return 0.5;
                }
                break;
            case 'e': // Electric
                switch (defender.getType()) {
                    case 'w': // Water
                    case 'y': // Flying
                        return 2.0;
                    case 'd': // Ground
                        return 0.0;
                    case 'r': // Grass
                    case 'j': // Dragon
                    case 'e': // Electric
                        return 0.5;
                }
                break;
            case 'i': // Ice
                switch (defender.getType()) {
                    case 'r': // Grass
                    case 'd': // Ground
                    case 'y': // Flying
                    case 'j': // Dragon
                        return 2.0;
                    case 'f': // Fire
                    case 'w': // Water
                    case 'i': // Ice
                    case 'l': // Steel
                        return 0.5;
                }
                break;
            case 't': // Fighting
                switch (defender.getType()) {
                    case 'n': // Normal
                    case 'k': // Rock
                    case 'l': // Steel
                    case 'a': // Dark
                        return 2.0;
                    case 'p': // Poison
                    case 'y': // Flying
                    case 'b': // Bug
                    case 'x': // Fairy
                        return 0.5;
                    case 'h': // Ghost
                        return 0.0;
                }
                break;
            case 'p': // Poison
                switch (defender.getType()) {
                    case 'r': // Grass
                    case 'x': // Fairy
                        return 2.0;
                    case 'p': // Poison
                    case 'g': // Ground
                    case 'k': // Rock
                    case 'l': // Steel
                        return 0.5;
                }
                break;
            case 'd': // Ground
                switch (defender.getType()) {
                    case 'f': // Fire
                    case 'e': // Electric
                    case 'k': // Rock
                    case 'p': // Poison
                        return 2.0;
                    case 'r': // Grass
                    case 'b': // Bug
                        return 0.5;
                    case 'y': // Flying
                        return 0.0;
                }
                break;
            case 'y': // Flying
                switch (defender.getType()) {
                    case 'r': // Grass
                    case 'b': // Bug
                    case 'p': // Fighting
                    case 'k': // Rock
                        return 2.0;
                    case 'e': // Electric
                    case 'l': // Steel
                        return 0.5;
                }
                break;
            case 's': // Psychic
                switch (defender.getType()) {
                    case 't': // Fighting
                    case 'p': // Poison
                        return 2.0;
                    case 's': // Psychic
                    case 'l': // Steel
                        return 0.5;
                    case 'a': // Dark
                        return 0.0;
                }
                break;
            case 'b': // Bug
                switch (defender.getType()) {
                    case 'r': // Grass
                    case 'p': // Psychic
                    case 'a': // Dark
                        return 2.0;
                    case 'f': // Fire
                    case 'y': // Flying
                    case 'k': // Rock
                        return 0.5;
                    case 'h': // Ghost
                        return 0.5;
                }
                break;
            case 'k': // Rock
                switch (defender.getType()) {
                    case 'f': // Fire
                    case 'i': // Ice
                    case 'y': // Flying
                    case 'b': // Bug
                        return 2.0;
                    case 'k': // Rock
                    case 'g': // Ground
                        return 0.5;
                }
                break;
            case 'h': // Ghost
                switch (defender.getType()) {
                    case 's': // Psychic
                    case 'h': // Ghost
                        return 2.0;
                    case 'n': // Normal
                        return 0.0;
                    case 'd': // Dark
                        return 0.5;
                }
                break;
            case 'j': // Dragon
                switch (defender.getType()) {
                    case 'j': // Dragon
                        return 2.0;
                    case 'x': // Fairy
                        return 0.0;
                    case 'l': // Steel
                        return 0.5;
                }
                break;
            case 'a': // Dark
                switch (defender.getType()) {
                    case 's': // Psychic
                    case 'h': // Ghost
                        return 2.0;
                    case 't': // Fighting
                    case 'a': // Dark
                    case 'x': // Fairy
                        return 0.5;
                }
                break;
            case 'l': // Steel
                switch (defender.getType()) {
                    case 'r': // Grass
                    case 'i': // Ice
                    case 'x': // Fairy
                        return 2.0;
                    case 'f': // Fire
                    case 'w': // Water
                    case 'e': // Electric
                    case 'l': // Steel
                        return 0.5;
                }
                break;
            case 'x': // Fairy
                switch (defender.getType()) {
                    case 't': // Fighting
                    case 'd': // Dragon
                    case 'a': // Dark
                        return 2.0;
                    case 'f': // Fire
                    case 'p': // Poison
                    case 'l': // Steel
                        return 0.5;
                }
                break;
        }
        // Default modifier
        return 1.0;
    }
}