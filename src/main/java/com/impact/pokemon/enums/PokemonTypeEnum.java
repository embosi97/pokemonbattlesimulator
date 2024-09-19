package com.impact.pokemon.enums;

import lombok.Getter;

@Getter
public enum PokemonTypeEnum {

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

    //Gets the multiplier based on attacker's type and the opponent's type
    public static double getEffectivenessModifier(PokemonTypeEnum type1, PokemonTypeEnum type2) {
        switch (type1.getType()) {
            case 'n': //Normal
                switch (type2.getType()) {
                    case 'k':
                    case 'l':
                        return 0.5;
                    case 'h':
                        return 0.0;
                }
                break;
            case 'f': //Fire
                switch (type2.getType()) {
                    case 'r':
                    case 'i':
                    case 'b':
                    case 'l':
                        return 2.0;
                    case 'k':
                    case 'f':
                    case 'w':
                    case 'j':
                    case 'x':
                        return 0.5;
                }
                break;
            case 'w': //Water
                switch (type2.getType()) {
                    case 'f':
                    case 'r':
                    case 'k':
                        return 2.0;
                    case 'w':
                    case 'j':
                    case 'l':
                        return 0.5;
                }
                break;
            case 'r': //Grass
                switch (type2.getType()) {
                    case 'w':
                    case 'd':
                    case 'k':
                        return 2.0;
                    case 'f':
                    case 'r':
                    case 'y':
                    case 'p':
                    case 'b':
                    case 'l':
                        return 0.5;
                }
                break;
            case 'e': //Electric
                switch (type2.getType()) {
                    case 'w':
                    case 'y':
                        return 2.0;
                    case 'd':
                        return 0.0;
                    case 'r':
                    case 'j':
                    case 'e':
                    case 'l':
                        return 0.5;
                }
                break;
            case 'i': //Ice
                switch (type2.getType()) {
                    case 'r':
                    case 'd':
                    case 'y':
                    case 'j':
                        return 2.0;
                    case 'f':
                    case 'w':
                    case 'i':
                    case 'l':
                        return 0.5;
                }
                break;
            case 't': //Fighting
                switch (type2.getType()) {
                    case 'n':
                    case 'k':
                    case 'l':
                    case 'a':
                        return 2.0;
                    case 'p':
                    case 'y':
                    case 'b':
                    case 'x':
                        return 0.5;
                    case 'h':
                        return 0.0;
                }
                break;
            case 'p': //Poison
                switch (type2.getType()) {
                    case 'r':
                    case 'x':
                        return 2.0;
                    case 'p':
                    case 'g':
                    case 'k':
                    case 'l':
                    case 'b':
                        return 0.5;
                    case 'd':
                        return 0.0;
                }
                break;
            case 'd': //Ground
                switch (type2.getType()) {
                    case 'f':
                    case 'e':
                    case 'k':
                    case 'p':
                        return 2.0;
                    case 'r':
                    case 'b':
                        return 0.5;
                    case 'y':
                        return 0.0;
                }
                break;
            case 'y': //Flying
                switch (type2.getType()) {
                    case 'r':
                    case 'b':
                    case 'p':
                        return 2.0;
                    case 'k':
                    case 'e':
                    case 'l':
                        return 0.5;
                }
                break;
            case 's': //Psychic
                switch (type2.getType()) {
                    case 't':
                    case 'p':
                    case 'b':
                        return 2.0;
                    case 's':
                    case 'l':
                        return 0.5;
                    case 'a':
                        return 0.0;
                }
                break;
            case 'b': //Bug
                switch (type2.getType()) {
                    case 'r':
                    case 'p':
                    case 's':
                    case 'x':
                        return 2.0;
                    case 'f':
                    case 'y':
                    case 'k':
                    case 'b':
                    case 'l':
                        return 0.5;
                    case 'h':
                        return 0.0;
                }
                break;
            case 'k': //Rock
                switch (type2.getType()) {
                    case 'f':
                    case 'i':
                    case 'y':
                    case 'b':
                    case 'l':
                        return 2.0;
                    case 'k':
                    case 'g':
                        return 0.5;
                }
                break;
            case 'h': //Ghost
                switch (type2.getType()) {
                    case 's':
                    case 'h':
                        return 2.0;
                    case 'd':
                        return 0.0;
                    case 'l':
                        return 0.5;
                }
                break;
            case 'j': //Dragon
                switch (type2.getType()) {
                    case 'j', 'l':
                        return 0.5;
                    case 'x':
                        return 0.0;
                }
                break;
            case 'a': //Dark
                switch (type2.getType()) {
                    case 's':
                    case 'h':
                        return 2.0;
                    case 't':
                    case 'a':
                    case 'x':
                        return 0.5;
                }
                break;
            case 'l': //Steel
                switch (type2.getType()) {
                    case 'r':
                    case 'i':
                    case 'x':
                        return 2.0;
                    case 'f':
                    case 'w':
                    case 'e':
                    case 'l':
                    case 'j':
                        return 0.5;
                    case 'd':
                        return 0.0;
                }
                break;
            case 'x': //Fairy
                switch (type2.getType()) {
                    case 't':
                    case 'd':
                    case 'a':
                        return 2.0;
                    case 'f':
                    case 'p':
                    case 'l':
                        return 0.5;
                }
                break;
        }
        //Default modifier
        return 1.0;
    }
}