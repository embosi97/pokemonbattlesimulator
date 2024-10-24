package com.impact.pokemon.enums;

import lombok.Getter;

@Getter
public enum PokemonMovesEnum {

    N0_ADDITIONAL_EFFECT("No additional effect.", 0f),
    ABSORB("The user restores half the HP it lost.", 0f),
    ACID("The target is attacked with a spray of acid. Has a 10% chance to lower the target's Special Defense.", 10f),
    AERIAL_ACE("The user attacks the target with wings or blades. This move never misses.", 0f),
    AGILITY("The user increases its Speed stat.", 0f),
    AIR_CUTTER("The user attacks with a blade of air. Critical hits land more easily.", 0f),
    ALPHALAPSE("The user unleashes a powerful wave of sound that disrupts opponents' senses.", 0f),
    AMNESIA("The user temporarily forgets its pain, raising its Special Defense.", 0f),
    ANCIENT_POWER("The user attacks with ancient power. May raise all of the user's stats.", 10f),
    AQUA_JET("The user strikes first, launching a quick attack with water.", 0f),
    AQUA_RING("The user surrounds itself with a veil of water, restoring HP each turn.", 0f),
    AROMATHERAPY("The user releases a soothing scent that heals all status conditions in the party.", 0f),
    BARRAGE("The user strikes the target with a flurry of blows, hitting 2-5 times in one turn.", 0f),
    BITE("The user bites the target. May cause flinching.", 0f),
    BLIZZARD("A vicious snowstorm is summoned to strike the target. Has a 10% chance to freeze the target.", 10f),
    BODY_SLAM("The user slams into the target with its whole body. May cause paralysis.", 30f),
    BOUNCE("The user jumps up high, then strikes the target on the next turn. May cause paralysis.", 30f),
    BRINE("The user attacks with a wave. It does double damage if the target's HP is half or less.", 0f),
    BULLET_SEED("The user attacks by shooting seeds at the target. Hits 2-5 times in one turn.", 0f),
    CALM_MIND("The user quietly focuses its mind to raise its Special Attack and Special Defense.", 0f),
    CATCH("The user tries to catch the opponent's Pokémon and add it to its own team.", 0f),
    CHARGE_BEAM("The user attacks with an electric beam. May raise the user's Special Attack.", 70f),
    CHARM("The user winks at the target, lowering its Attack stat.", 0f),
    CHESTNUT("The user attacks with a sharp, spiky chestnut. May lower the target's Speed.", 0f),
    CLOSE_COMBAT("The user attacks the target with a flurry of close-range blows. The user lowers its own Defense and Special Defense.", 0f),
    CONFUSE("Confuses the target, causing it to potentially hurt itself in its confusion.", 20f),
    CONFUSION("The target is hit by a shock of brainwaves. May confuse the target.", 10f),
    CONSTRICT("The user wraps its body around the target. May lower the target's Speed.", 0f),
    CONVERSION("The user changes its type to match one of its moves.", 0f),
    COIL("The user coils its body, raising its Attack, Defense, and Accuracy.", 0f),
    COURT_CHANGE("The user switches the effects of terrain with its ally.", 0f),
    CRUNCH("The user bites the target. May lower the target's Defense.", 20f),
    CUT("The target is slashed with sharp claws. It may also cut down small trees.", 0f),
    DIVE("The user dives underwater on the first turn and attacks on the second.", 0f),
    DRAIN_PUNCH("The user punches the target. The user recovers half the HP lost by the target.", 0f),
    DRAGON_BREATH("The user attacks with a blast of dragon energy. May cause paralysis.", 30f),
    DRAGON_CLAW("The user attacks with claws made of dragon energy.", 0f),
    DRAGON_DANCE("The user dances, raising its Attack and Speed stats.", 0f),
    DRAGON_TAIL("The user attacks by swinging its tail. The target is forced to switch out.", 0f),
    DYNAMIC_PUNCH("The user throws a punch that confuses the target. It always hits if the target is confused.", 100f),
    EARTHQUAKE("The user strikes the ground, causing a massive quake that damages all nearby foes.", 0f),
    ECHOED_VOICE("The user attacks in consecutive turns. Damage increases with each consecutive hit.", 0f),
    EGG_BOMB("The user launches a bomb made of eggs at the target.", 0f),
    ELECTRO_BALL("The user attacks with an electrified ball. The more the user outspeeds the target, the greater the damage.", 0f),
    EMBER("The target is attacked with a fiery blast. Has a 10% chance to burn the target.", 10f),
    EMERGENCY_EXIT("The user flees from battle, allowing an ally to take its place.", 0f),
    ENDEAVOR("The user attacks by matching the target's HP. If the target is at full health, it will be knocked out.", 0f),
    ENTRAINMENT("The user entrains the target, copying its ability.", 0f),
    FAINT_ATTACK("The user attacks without fail. The target cannot evade the attack.", 0f),
    FAKE_OUT("The user strikes first, causing flinching. This move only works on the first turn.", 100f),
    FALSE_SWIPE("The user attacks but leaves the target with at least 1 HP.", 0f),
    FELL_STINGER("The user attacks with a stinger. If the target faints, the user's Attack stat is raised sharply.", 0f),
    FLY("The user soars up high, avoiding all attacks. The next turn, it attacks.", 0f),
    FOCUS_ENERGY("The user focuses its energy to raise the critical hit ratio of its next attack.", 0f),
    FOCUS_PUNCH("The user focuses its energy into a punch. It can only be used if the user is not attacked during the turn.", 0f),
    FOLLOW_ME("The user draws attention to itself, forcing all opposing Pokémon to target it.", 0f),
    FORESTS("The user creates a patch of forest that will lower the Speed of Pokémon in it.", 0f),
    FURIOUS_SWIPES("The user swipes at the target 2-5 times in one turn. Each hit has a chance to land critical damage.", 0f),
    GASTRO_ACID("The user attacks with acidic fluid that lowers the target's Special Defense.", 0f),
    GIGA_DRAIN("The user drains the target's energy to restore its own HP. The user gains half the HP lost by the target.", 0f),
    GUST("A gust of wind is summoned to attack the target. This move has no effect if the target is not in the air.", 0f),
    HAIL("The user summons a hailstorm that damages all Pokémon except Ice types for 5 turns.", 0f),
    HAMMER_ARM("The user attacks with a strong arm. The user lowers its own Speed.", 0f),
    HEAL_BELL("The user rings a bell that heals all party Pokémon from status conditions.", 0f),
    HEAL_ORDER("The user restores the HP of itself and its allies.", 0f),
    HEAL_BLOCK("The user blocks healing effects for 5 turns.", 0f),
    HELPING_HAND("The user helps its ally, increasing the power of its next move.", 0f),
    HIGH_JUMP_KICK("The user jumps high and kicks. If the user misses, it takes damage.", 0f),
    HORN_ATTACK("The user attacks with a horn. May cause flinching.", 0f),
    HURRICANE("The user attacks with a violent wind. Has a 30% chance to confuse the target.", 30f),
    ICY_WIND("The user attacks with icy winds that lower the target's Speed.", 0f),
    IMPRISON("The user prevents the target from using moves known by the user.", 0f),
    INSTRUCT("The user instructs its ally to use a move they just used again.", 0f),
    IRON_HEAD("The user attacks with its head, which is made of iron. May cause flinching.", 30f),
    JUMP_KICK("The user jumps to attack the target. If it misses, the user takes damage.", 0f),
    KNOCK_OFF("The user knocks off the target's held item.", 0f),
    LAST_RESORT("The user attacks, but only works if the user has not used any other moves during the turn.", 0f),
    LEAF_BLADE("The user attacks with a sharp blade of grass. Critical hits land more easily.", 0f),
    LEER("The user lowers the target's Defense stat.", 0f),
    LEECH_SEED("The user sows seeds that drain HP from the target.", 0f),
    LIGHT_SCREEN("The user creates a barrier that reduces damage from Special Attacks for 5 turns.", 0f),
    LUCKY_CHANT("The user chants an incantation that prevents team from losing its next turn.", 0f),
    MAGNET_BOMB("The user attacks with a magnetic force. This move never misses.", 0f),
    MEGA_DRAIN("The user drains energy from the target to restore its own HP.", 0f),
    MEGA_KICK("The user attacks with a powerful kick. It has a high chance to land critical hits.", 0f),
    MEGA_PUNCH("The user attacks with a powerful punch. It has a high chance to land critical hits.", 0f),
    METAL_CLAW("The user slashes the target with sharp claws. May raise the user's Attack.", 10f),
    METEOR_MASH("The user attacks with meteoric force. May raise the user's Attack.", 20f),
    MIMIC("The user copies the last move used by the opponent.", 0f),
    MINIMIZE("The user makes itself smaller, raising its evasion.", 0f),
    MIRROR_COAT("The user retaliates against special moves. This move does double the damage.", 0f),
    MUD_SLAP("The user slaps the target with mud. May lower the target's Accuracy.", 20f),
    NATURE_POWER("The user transforms into a powerful move depending on the environment.", 0f),
    NIGHT_SHADE("The user attacks with a dark shadow. The damage is equal to the user's level.", 0f),
    NOBLE_ROAR("The user emits a noble roar that lowers the target's Attack.", 0f),
    OUTRAGE("The user rampages for 2-3 turns. The user is confused afterward.", 0f),
    OVERHEAT("The user unleashes a blast of fire. The user loses some Special Attack after using this move.", 0f),
    PAINT_SPLASH("The user attacks by splashing paint. If the target faints, the user gets a higher Attack stat.", 0f),
    POUND("The target is pounded with a soft body part.", 0f),
    POWDER("The user scatters powder that can cause status conditions.", 0f),
    PREPARE("The user prepares to attack on the next turn.", 0f),
    PROTECT("The user protects itself from all effects of moves during this turn.", 0f),
    PURSUIT("The user attacks the target if it switches out. If not, it deals damage anyway.", 0f),
    PSYCHIC("The user attacks the target with psychic power. May lower the target's Special Defense.", 10f),
    PSYCHO_CUT("The user cuts the target with psychic blades. Critical hits land more easily.", 0f),
    QUICK_ATTACK("The user strikes first, hitting the target before it can react.", 0f),
    RAGE("The user rages, gaining strength for each time it is hit. Damage increases for each hit.", 0f),
    RAIN_DANCE("The user summons a rainstorm that boosts Water-type moves for 5 turns.", 0f),
    RAZOR_LEAF("The user attacks with sharp leaves. Critical hits land more easily.", 0f),
    RECOVER("The user restores up to half its maximum HP.", 0f),
    REPEATED_SWIPES("The user strikes the target multiple times. Each hit has a chance to land critical hits.", 0f),
    REVENGE("The user attacks after being hit. It doubles damage if the user was hit first.", 0f),
    ROAR("The user roars, forcing the opponent to switch out.", 0f),
    ROCK_SLIDE("The user attacks with falling rocks. May cause flinching.", 30f),
    ROLL_OUT("The user attacks consecutively, increasing power with each hit.", 0f),
    ROUND("The user sings a song. Damage increases for each consecutive use by the user or an ally.", 0f),
    SCARY_FACE("The user frightens the target, lowering its Speed.", 0f),
    SCRATCH("The user scratches the target. A simple, weak attack.", 0f),
    SEED_FLARE("The user attacks with a burst of seeds. May lower the target's Special Defense.", 20f),
    SELF_DESTRUCT("The user self-destructs, causing massive damage to all nearby Pokémon.", 0f),
    SHADOW_BALL("The user attacks with a shadowy orb. May lower the target's Special Defense.", 20f),
    SHADOW_CLAW("The user attacks with claws made of shadow. Critical hits land more easily.", 0f),
    SHOCK_WAVE("The user attacks with an electric wave. This move never misses.", 0f),
    SIMPLIFY("The user simplifies its form, raising its Speed.", 0f),
    SKETCH("The user copies the last move used by the target.", 0f),
    SLAM("The user slams into the target with its body.", 0f),
    SLASH("The user slashes the target with sharp claws. Critical hits land more easily.", 0f),
    SLEEP_TALK("The user can use a move while asleep.", 0f),
    SLEEPY_JUMP("The user performs a jump that may confuse or induce sleep.", 0f),
    SMELLING_SALTS("The user attacks the target. Deals double damage if the target is paralyzed.", 0f),
    SMOG("The user releases a cloud of poisonous gas. May poison the target.", 30f),
    SNATCH("The user steals the effects of moves that the target used.", 0f),
    SNORE("The user attacks while it snores. It can only be used if the user is asleep.", 0f),
    SOLAR_BEAM("The user stores energy for one turn, then unleashes it on the next. It may cause paralysis.", 0f),
    SOOTHE("The user soothes the target with its body, healing any status condition.", 0f),
    SPARK("The user generates a burst of electricity. May cause paralysis.", 30f),
    SPITE("The user lowers the PP of the target's last move by 2.", 0f),
    STOMP("The user stomps on the target. May cause flinching.", 30f),
    STRUGGLE("If a Pokémon is out of PP for all of its moves, it must use Struggle.", 0f),
    SUBMISSION("The user tackles the target, causing recoil damage.", 0f),
    SUPERPOWER("The user attacks with great force, lowering its own Attack and Defense.", 0f),
    SURF("The user rides a wave to attack the target. Deals damage to all nearby Pokémon.", 0f),
    SWAGGER("The user increases the target's Attack but confuses it.", 0f),
    SWORDS_DANCE("The user sharpens its claws to increase its Attack.", 0f),
    TACKLE("A physical attack in which the user charges and slams into the target.", 0f),
    TAIL_WHIP("The user wags its tail to lower the target's Defense.", 0f),
    TANGLED("The user tangles the target in vines, making it unable to switch out.", 0f),
    TATTER("The user attacks the target with a tattered scarf, reducing the target's Attack.", 0f),
    THUNDERBOLT("The user attacks with a bolt of electricity. May cause paralysis.", 10f),
    THUNDER_WAVE("The user releases a wave of electricity that paralyzes the target.", 0f),
    TOXIC("The user poisons the target. The poison worsens each turn.", 0f),
    TRICK_ROOM("The user creates a bizarre area where Pokémon move last for five turns.", 0f),
    TWISTER("The user attacks with a twisting cyclone. May cause flinching.", 20f),
    UPROAR("The user attacks in an uproar. It keeps going for 2-5 turns.", 0f),
    VINE_WHIP("The user attacks by whipping the target with vines. Hits 2 times.", 0f),
    VOLT_TACKLE("The user charges at the target. The user takes recoil damage.", 0f),
    WATER_GUN("The target is blasted with a water gun.", 0f),
    WHIRLWIND("The user whirls up a vicious wind that forces the opponent to switch out.", 0f),
    WING_ATTACK("The user attacks with its wings. May cause flinching.", 0f),
    WORK_UP("The user raises its Attack and Special Attack.", 0f),
    ZEN_HEADBUTT("The user attacks with a headbutt that has a chance to cause flinching.", 20f);

    private final String shortDesc;
    private final float percentage;

    PokemonMovesEnum(String shortDesc, float percentage) {
        this.shortDesc = shortDesc;
        this.percentage = percentage;
    }

    public static PokemonMovesEnum fromShortDesc(String shortDesc) {
        for (PokemonMovesEnum se : PokemonMovesEnum.values()) {
            if (se.name().equalsIgnoreCase(shortDesc)) {
                return se;
            }
        }
        return null;
    }
}
