package com.alderangaming.wizardsencounters.vfx;

import android.util.SparseArray;
import com.alderangaming.wizardsencounters.DefinitionMonsters;

/**
 * Maps monster IDs (indexes used to construct Monster) to per-monster render
 * params.
 */
public final class MonsterRenderRegistry {
    private static final SparseArray<MonsterRenderParams> MAP = new SparseArray<MonsterRenderParams>();
    private static boolean initialized;
    private static String cachedReadableMap;

    private MonsterRenderRegistry() {
    }

    /*
     * Monster ID reference (index : name)
     * 0: Hideous Aunt
     * 1: Screeching Cow
     * 2: Crazed Axe Dwarf
     * 3: Treeman
     * 4: Floating Goo
     * 5: Leaping Hag
     * 6: Blissful Undead
     * 7: Gun-Toting Cultist
     * 8: Evil Beardie
     * 9: Inconsiderate Hobo
     * 10: Depressed Zombie
     * 11: Grumpy Gnome
     * 12: Tainted Snowglob
     * 13: Pete The Slug
     * 14: Ghastly Woman
     * 15: Scented Ghost
     * 16: Vengeful Franks
     * 17: Fry Brain
     * 18: Disguised Cultist
     * 19: Icy Beast
     * 20: Snowman of Pain
     * 21: Nasty Nat
     * 22: Juvenile Genie
     * 23: Little Green Punk
     * 24: Deadly Crustacean
     * 25: Neon Man
     * 26: Nightmare Dwarf
     * 27: Smarty Rock
     * 28: Evil Crusader
     * 29: Hinnish Witch
     * 30: Marge of Wench
     * 31: Ghost of Night
     * 32: Slam Lance
     * 33: Tiny Terror
     * 34: Occular Destroid
     * 35: Rejected Wizard
     * 36: Toxic Mud-Girl
     * 37: Hanz Deathmarch
     * 38: Widdled Peon
     * 39: Aggrivated Libgarch
     * 40: Homely Bewhiler
     * 41: Pontificated Jax
     * 42: Red Tailed Jerk
     * 43: Beeward
     * 44: Series 65
     * 45: Snoonsnarch
     * 46: Ghensorn
     * 47: Eye Slore
     * 48: Zomzludge
     * 49: Red Golem
     * 50: Terry Bull
     * 51: False Face
     * 52: Rodney Trebek
     * 53: Bantraxer
     * 54: Transaxle
     * 55: Moxinox
     * 56: Sneakasaur
     * 57: Dreaded Morth
     * 58: Transmuted Acorn
     * 59: Barber Bear
     * 60: Madame Rosie
     * 61: Planet Wielder
     * 62: Skullchin
     * 63: JiffJammer
     * 64: Dogmanicus
     * 65: Boncho
     * 66: Grievous Toad
     * 67: Pegasaur
     * 68: Slithery Sam
     * 69: Slam Lance
     * 70: Corrupt Ranger
     * 71: Stephalice
     * 72: Sag Rag
     * 73: Purple Death
     * 74: Gross Gary
     * 75: Decrepit Aviary
     */

    public static MonsterRenderParams get(int monsterId) {
        ensure();
        MonsterRenderParams p = MAP.get(monsterId);
        return p == null ? MonsterRenderParams.defaults() : p;
    }

    public static void register(int monsterId, MonsterRenderParams params) {
        if (params == null)
            return;
        MAP.put(monsterId, params);
    }

    private static void ensure() {
        if (initialized)
            return;
        initialized = true;
        // Seed defaults for all monsters (scale 1, no offset)
        try {
            int n = DefinitionMonsters.MONSTER_NAMES.length;
            for (int i = 0; i < n; i++) {
                MAP.put(i, MonsterRenderParams.defaults());
            }

            MonsterRenderParams hideousaunt = new MonsterRenderParams();
            hideousaunt.scaleX = 0.6f;
            hideousaunt.scaleY = 0.6f;
            hideousaunt.offsetYDp = 0f;
            register(0, hideousaunt);
        } catch (Throwable ignored) {
        }
    }

}
