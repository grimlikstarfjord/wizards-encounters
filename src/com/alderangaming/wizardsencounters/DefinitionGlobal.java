package com.alderangaming.wizardsencounters;

public class DefinitionGlobal
{

	public static final boolean BETA_BUILD = false;
	public static final int DEFAULT_STARTING_GOLD = 50;
	public static final int DEBUG_REBUILD_DB = 0;
	public static final String TESTCHAR = "qsstt";
	public static final String TESTCHAR2 = "ttqss";
	public static final String TESTCHAR3 = "aasst";
	public static final String TESTCHAR4 = "qqsst";

	public static final int TUTORIAL = 0;
	public static final int SETTINGS = 1;
	public static final int ABOUT = 2;
	
	public static final int WEAPON_COST_MIN_DMG_MULT = 4;
	public static final int WEAPON_COST_MAX_DMG_MULT = 13;
	public static final int WEAPON_COST_HIT_MULT = 1;
	public static final int WEAPON_COST_STUN_MULT = 2;
	public static final int WEAPON_COST_CRIT_MULT = 3;
	public static final int WEAPON_COST_ATT_TYPE_HIT_MULT = 1;
	public static final int WEAPON_COST_ATT_TYPE_STUN_MULT = 2;
	public static final int WEAPON_COST_ATT_TYPE_CRIT_MULT = 3;
	public static final int WEAPON_COST_ATT_TYPE_BLOCK_MULT = 1;
	
	public static final double MONSTER_DROP_GOLD_MULTIPLE = 2.5;
	public static final int CHANCE_TO_GET_GOLD_FROM_CHEST = 25;
	public static final int MAX_PERCENT_OF_CHEST_COST_OF_GOLD_IN_CHEST = 200;
	
	public static final int ITEM_VALUE_DIVISOR = 3;
	public static final float ITEM_RECHARGE_PERCENT_OF_COST = 0.7f;

	public static final int DEFAULT_MAX_ABILITIES_ALLOWED = 8;

	public static final int REGEN_HP_AT_FIGHT_START = 25;
	public static final int REGEN_HP_AT_BOSS_START = 75;
	public static final int MONSTER_USE_HP_ABILITY_PERCENT_BELOW = 20;
	public static final int NUM_TIMES_MONSTER_TRIES_RANDOM_ABILITY = 3;
	
	public static final double DODGE_TIME = 0.8;
	public static final int RUN_CHANCE = 20;
	public static final int ARMOR_BLOCKS_CHANCE = 25;
	public static final int CLASS_RUNE_COST = 1500;

	public static final int[] CHEST_OPEN_COST =
	//	{ 0, 0,0,0,0,0 };
	{ 0, 20, 35, 60, 90, 150 };
	public static final int CHANCE_FREE_CHEST_EMPTY = 50;
	public static final int MAX_CHESTS_TO_OPEN = 2;

	public static final int ITEM_TYPE_PLAYER_CLASS = 0;
	public static final int ITEM_TYPE_WEAPON = 1;
	public static final int ITEM_TYPE_ARMOR = 2;
	public static final int ITEM_TYPE_ITEM = 3;
	public static final int ITEM_TYPE_RUNE_ABILITY = 4;
	public static String[] ITEM_TYPE_NAMES =
	{ "Class Rune", "Weapon", "Armor", "Item", "Ability Rune" };

	public static String[] EQUIP_TYPE_NAMES =
	{ "Helm", "Weapon", "Armor", "Shoes", "Trinket", "Item", "Ability Rune", "Class Rune" };

	public static int[] EQUIP_TYPE_INDEX =
	{ ITEM_TYPE_ARMOR, ITEM_TYPE_WEAPON, ITEM_TYPE_ARMOR, ITEM_TYPE_ARMOR, ITEM_TYPE_ARMOR, ITEM_TYPE_ITEM,
		ITEM_TYPE_RUNE_ABILITY, ITEM_TYPE_PLAYER_CLASS };

	public static String[] EQUIP_SLOT_NAMES =
	{ "Helm", "Weapon", "Armor", "Shoes", "Trinket", "Item 1", "Item 2" };

	public static final int[] EQUIP_SLOT_HELM =
	{ 0 };
	public static final int[] EQUIP_SLOT_WEAPON =
	{ 1 };
	public static final int[] EQUIP_SLOT_CHEST =
	{ 2 };
	public static final int[] EQUIP_SLOT_SHOES =
	{ 3 };
	public static final int[] EQUIP_SLOT_TRINKET =
	{ 4 };
	public static final int[] EQUIP_SLOT_ITEM =
	{ 5, 6 };
	
	public static final int LOG_TYPE_DEFAULT = -1;
	public static final int LOG_TYPE_PLAYER_TAKE_HIT_DMG = 0;
	public static final int LOG_TYPE_PLAYER_USE_ABILITY = 1;
	public static final int LOG_TYPE_PLAYER_TAKE_ABILITY_DMG = 2;
	public static final int LOG_TYPE_MONSTER_USE_ABILITY = 3;
	public static final int LOG_TYPE_MONSTER_TAKE_ABILITY_DMG = 4;
	public static final int LOG_TYPE_PLAYER_STUNNED = 5;
	public static final int LOG_TYPE_MONSTER_STUNNED = 6;
	public static final int LOG_TYPE_PLAYER_GAINS_EFFECT_GOOD = 7;
	public static final int LOG_TYPE_MONSTER_GAINS_EFFECT = 8;
	public static final int LOG_TYPE_MONSTER_MISSES = 9;
	public static final int LOG_TYPE_PLAYER_DODGES = 10;
	public static final int LOG_TYPE_PLAYER_MISSES = 11;
	public static final int LOG_TYPE_MONSTER_TAKE_HIT_DMG = 12;
	public static final int LOG_TYPE_PLAYER_GAINS_HP = 13;
	public static final int LOG_TYPE_PLAYER_LOSES_HP = 14;
	public static final int LOG_TYPE_PLAYER_GAINS_AP = 15;
	public static final int LOG_TYPE_PLAYER_LOSES_AP = 16;
	public static final int LOG_TYPE_MONSTER_GAINS_HP = 17;
	public static final int LOG_TYPE_MONSTER_LOSES_HP = 18;
	public static final int LOG_TYPE_MONSTER_GAINS_AP = 19;
	public static final int LOG_TYPE_MONSTER_LOSES_AP = 20;
	public static final int LOG_TYPE_ACTION_FAILED = 21;
	public static final int LOG_TYPE_SOMETHING_BAD_FOR_PLAYER = 22;
	public static final int LOG_TYPE_SOMETHING_GOOD_FOR_PLAYER = 23;
	public static final int LOG_TYPE_PLAYER_USE_ITEM = 24;
	public static final int LOG_TYPE_PLAYER_GAINS_EFFECT_BAD = 25;
	public static final int LOG_TYPE_IS_CASTING = 26;
	public static final int LOG_TYPE_LOOT = 27;
	public static final int LOG_TYPE_MONSTER_TAKE_CRIT_DMG = 28;
	public static final int LOG_TYPE_PLAYER_TAKE_CRIT_DMG = 29;
	
}
