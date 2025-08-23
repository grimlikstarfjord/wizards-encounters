package com.alderangaming.wizardsencounters;

public class DefinitionItems
{

	
	public static final int ITEM_NAME = 0;
	public static final int ITEM_DESCRIPTION = 1;
	public static final int ITEM_SOUND_CLIP = 2;	
	public static final int ITEM_IMAGE_NAME = 3;
	public static final int ITEM_SHOWS_IN_STORE = 4;
	public static final int ITEM_DROPS = 5;
	public static final int ITEM_MIN_LEVEL_TO_USE = 6;
	public static final int ITEM_BASE_VALUE = 7;
	public static final int ITEM_INCREASE_CHARGE_COST_MULTIPLE = 8;
	public static final int ITEM_MAX_CHARGE = 9;
	public static final int ITEM_RECHARGES_AT_END_OF_ROUND_FLAG = 10;
	public static final int ITEM_APPLIED_OVER_NUM_TURNS = 11;
	public static final int ITEM_MODIFY_HP_PERCENT_OF_MAX = 12;
	public static final int ITEM_MODIFY_AP_PERCENT_OF_MAX = 13;
	public static final int ITEM_SKIPS_FIGHT_FLAG = 14;
	public static final int ITEM_MODIFY_CRIT_PERCENT = 15;
	public static final int ITEM_MODIFY_DAMAGE_TAKEN_PERCENT = 16;
	public static final int ITEM_MODIFY_DODGE_PERCENT = 17;
	public static final int ITEM_STUN_FOR_TURNS = 18;
	public static final int ITEM_REMOVE_PLAYER_EFFECTS_FLAG = 19;
	public static final int ITEM_ABILITIES_ARE_FREE_FLAG = 20;
	public static final int ITEM_APPLY_MONSTER_ANIMATION_ID = 21;
	public static final int ITEM_TEMP_CHANGE_MONSTER_IMAGE = 22;
	public static final int ITEM_MODIFY_ATTACK_POWER = 23;
	public static final int ITEM_KILL_MONSTER_PERCENT_CHANCE = 24;
	public static final int ITEM_RESTART_FIGHT_FLAG = 25;
	public static final int ITEM_WARP_TO_ROUND = 26;
	public static final int ITEM_APPLY_HP_GAIN_PERCENT_AFTER_NUM_TURNS = 27;
	
	public static final Object[][][] itemdata = {
		
		{{"Health Potion"},	{"Replenish 50% max health. Recharges at start of Round."},	{0},	{"redpotion"},	{1},	{1, 99, 5, 2},	{1},	{40},	{4},	{5},	{1},	{0, 0},	{50, 0},	{0, 0},	{0},	{0, 0},	{0, 0},	{0, 0},	{0, 0},	{0},	{0},	{-1},	{0,""},	{0, 0},	{0},	{0},	{-1},	{0, 0}},
		{{"Cloud"},	{"Hop on your cloud and bypass the current fight."},	{SoundManager.CLOUD},	{"cloud"},	{1},	{1, 99, 5, 2},	{1},	{130},	{0},	{1},	{0},	{0, 0},	{0, 0},	{0, 0},	{1},	{0, 0},	{0, 0},	{0, 0},	{0, 0},	{0},	{0},	{-1},	{0,""},	{0, 0},	{0},	{0},	{-1},	{0, 0}},
		{{"Sharpening Stone"},	{"+10% Crit Chance for current fight. Recharges at start of Round."},	{0},	{"wishingstone"},	{1},	{1, 99, 5, 2},	{1},	{20},	{2},	{5},	{1},	{99, 0},	{0, 0},	{0, 0},	{0},	{10, 0},	{0, 0},	{0, 0},	{0, 0},	{0},	{0},	{-1},	{0,""},	{0, 0},	{0},	{0},	{-1},	{0, 0}},
		{{"Energy Drink"},	{"Next Hit will Crit. Recharges at start of Round."},	{0},	{"energydrink"},	{1},	{1, 99, 5, 2},	{1},	{35},	{2},	{10},	{1},	{1, 0},	{0, 0},	{0, 0},	{0},	{100, 0},	{0, 0},	{0, 0},	{0, 0},	{0},	{0},	{-1},	{0,""},	{0, 0},	{0},	{0},	{-1},	{0, 0}},
		{{"Stoneskin Potion"},	{"Reduces damage for next 5 turns by 25%. Recharges at start of Round."},	{0},	{"stoneskinpotion"},	{1},	{1, 99, 5, 2},	{1},	{30},	{4},	{5},	{1},	{5, 0},	{0, 0},	{0, 0},	{0},	{0, 0},	{-25, 0},	{0, 0},	{0, 0},	{0},	{0},	{-1},	{0,""},	{0, 0},	{0},	{0},	{-1},	{0, 0}},
		{{"Potion of Quickness"},	{"Increases dodge chance for next 5 turns by 15%. Recharges at end of Round."},	{0},	{"quicknesspotion"},	{1},	{1, 99, 5, 2},	{1},	{100},	{2},	{5},	{1},	{5, 0},	{0, 0},	{0, 0},	{0},	{0, 0},	{0, 0},	{15,0},	{0, 0},	{0},	{0},	{-1},	{0,""},	{0, 0},	{0},	{0},	{-1},	{0, 0}},
		{{"Green Flute"},	{"Warp to round 6"},	{SoundManager.FLUTE},	{"greenflute"},	{1},	{6, 99, 20, 5},	{1},	{400},	{0},	{1},	{0},	{0, 0},	{0, 0},	{0, 0},	{0},	{0, 0},	{0, 0},	{0, 0},	{0, 0},	{0},	{0},	{-1},	{0,""},	{0, 0},	{0},	{0},	{5},	{0, 0}},
		{{"Blue Flute"},	{"Warp to round 11"},	{SoundManager.FLUTE},	{"blueflute"},	{1},	{11, 99, 20, 5},	{1},	{600},	{0},	{1},	{0},	{0, 0},	{0, 0},	{0, 0},	{0},	{0, 0},	{0, 0},	{0, 0},	{0, 0},	{0},	{0},	{-1},	{0,""},	{0, 0},	{0},	{0},	{10},	{0, 0}},
		{{"Red Flute"},	{"Warp to round 15"},	{SoundManager.FLUTE},	{"redflute"},	{1},	{16, 99, 20, 5},	{1},	{800},	{0},	{1},	{0},	{0, 0},	{0, 0},	{0, 0},	{0},	{0, 0},	{0, 0},	{0, 0},	{0, 0},	{0},	{0},	{-1},	{0,""},	{0, 0},	{0},	{0},	{15},	{0, 0}},
		{{"Black Flute"},	{"Warp to round 20"},	{SoundManager.FLUTE},	{"blackflute"},	{0},	{21, 99, 20, 99},	{1},	{1000},	{0},	{1},	{0},	{0, 0},	{0, 0},	{0, 0},	{0},	{0, 0},	{0, 0},	{0, 0},	{0, 0},	{0},	{0},	{-1},	{0,""},	{0, 0},	{0},	{0},	{20},	{0, 0}},
		{{"Palette Cleanser"},	{"Removes all active player effects. Recharges at end of Round."},	{0},	{"vanishingcream"},	{1},	{1, 99, 5, 2},	{1},	{25},	{2},	{5},	{1},	{0, 0},	{0, 0},	{0, 0},	{0},	{0, 0},	{0, 0},	{0, 0},	{0, 0},	{1},	{0},	{-1},	{0,""},	{0, 0},	{0},	{0},	{-1},	{0, 0}},
		{{"Lightning Bolt"},	{"Current monster shrinks and attacks with 10% power for 5 turns."},	{0},	{"lightningbolt"},	{1},	{1, 99, 5, 2},	{1},	{40},	{0},	{1},	{0},	{5, 0},	{0, 0},	{0, 0},	{0},	{0, 0},	{0, 0},	{0, 0},	{0, 0},	{0},	{0},	{Animator.SHRINK},	{0,""},	{0, -90},	{0},	{0},	{-1},	{0, 0}},
		{{"Anvil"},	{"Stuns the enemy for 1 turn. Recharges at start of Round."},	{0},	{"anvil"},	{1},	{1, 99, 5, 2},	{1},	{10},	{3},	{5},	{1},	{0, 0},	{0, 0},	{0, 0},	{0},	{0, 0},	{0, 0},	{0, 0},	{0, 1},	{0},	{0},	{-1},	{0,""},	{0, 0},	{0},	{0},	{-1},	{0, 0}},
		{{"Treacherous Plume "},	{"Monster has 50% chance of exploding."},	{0},	{"keytothemountain"},	{1},	{1, 99, 5, 2},	{1},	{135},	{3},	{4},	{0},	{0, 0},	{0, 0},	{0, 0},	{0},	{0, 0},	{0, 0},	{0, 0},	{0, 0},	{0},	{0},	{-1},	{0,""},	{0, 0},	{50},	{0},	{-1},	{0, 0}},
		{{"Magic Mirror"},	{"Transforms monster into a harmless rabbit for 3 turns."},	{SoundManager.MAGIC_MIRROR},	{"magicmirror"},	{1},	{1, 99, 5, 2},	{1},	{80},	{2},	{3},	{0},	{3, 0},	{0, 0},	{0, 0},	{0},	{0, 0},	{0, 0},	{0, 0},	{0, 0},	{0},	{0},	{2},	{1,"rabbit"},	{0, 0},	{0},	{0},	{-1},	{0, 0}},
		{{"Time Shift Device"},	{"Restart current fight at full health."},	{0},	{"ward"},	{1},	{1, 99, 5, 2},	{1},	{100},	{0},	{1},	{0},	{0, 0},	{100, 0},	{0, 0},	{0},	{0, 0},	{0, 0},	{0, 0},	{0, 0},	{0},	{0},	{-1},	{0,""},	{0, 0},	{0},	{1},	{-1},	{0, 0}},
		{{"Big Bomb"},	{"Takes out 50% of monster's max health. You take 5% damage from the fallout."},	{SoundManager.SOUND_TYPE_EXPLOSION_SMALL},	{"spacerock"},	{1},	{1, 99, 5, 2},	{1},	{65},	{3},	{4},	{0},	{0, 0},	{-5, -50},	{0, 0},	{0},	{0, 0},	{0, 0},	{0, 0},	{0, 0},	{0},	{0},	{-1},	{0,""},	{0, 0},	{0},	{0},	{-1},	{0, 0}},
		{{"Proton Trap"},	{"Holds monster in an indefinate state where it cannot attack for 5 turns."},	{0},	{"protontrap"},	{1},	{1, 99, 5, 2},	{1},	{80},	{0},	{1},	{0},	{0, 0},	{0, 0},	{0, 0},	{0},	{0, 0},	{0, 0},	{0, 0},	{0, 5},	{0},	{0},	 {Animator.RAISE_AND_ROTATE},	{0,""},	{0, 0},	{0},	{0},	{-1},	{0, 0}},
		{{"Grimroot"},	{"A magical herb that allows you to perform one free ability. Recharges at start of Round."},	{0},	{"grimroot"},	{1},	{1, 99, 5, 2},	{1},	{20},	{3},	{5},	{1},	{1, 0},	{0, 0},	{0, 0},	{0},	{0, 0},	{0, 0},	{0, 0},	{0, 0},	{0},	{1},	{-1},	{0,""},	{0, 0},	{0},	{0},	{-1},	{0, 0}},
		{{"Mage Nectar"},	{"Regain 50% AP. Recharges at start of Round."},	{0},	{"bluepotion"},	{1},	{1, 99, 5, 2},	{1},	{10},	{3},	{5},	{1},	{0, 0},	{0, 0},	{50, 0},	{0},	{0, 0},	{0, 0},	{0, 0},	{0, 0},	{0},	{0},	{-1},	{0,""},	{0, 0},	{0},	{0},	{-1},	{0, 0}},
		{{"Daredevil's Sleepingbag"},	{"Take a nice 5 turn nap during combat. You are stunned while asleep, but wake up with 80% hp."},	{0},	{"rest"},	{1},	{1, 99, 5, 2},	{1},	{55},	{2},	{3},	{0},	{0, 0},	{0, 0},	{0, 0},	{0},	{0, 0},	{0, 0},	{0, 0},	{5, 0},	{0},	{0},	{-1},	{0,""},	{0, 0},	{0},	{0},	{-1},	{80, 5}},
		{{"Toxic Spray"},	{"You spray acid towards the monster, but some gets on you too. It takes 25% dmg and is stunned, you take 10%."},	{0},	{"toxicspray"},	{1},	{1, 99, 5, 2},	{1},	{15},	{2},	{3},	{0},	{0, 0},	{-10, -25},	{0, 0},	{0},	{0, 0},	{0, 0},	{0, 0},	{0, 1},	{0},	{0},	{-1},	{0,""},	{0, 0},	{0},	{0},	{-1},	{0, 0}},
		{{"GMO Grimroot"},	{"Genetically  modified Grimroot that grants 3 free abilities. You take an unintended amount of damage as a side effect."},	{0},	{"gmogrimroot"},	{1},	{1, 99, 5, 2},	{1},	{65},	{3},	{3},	{0},	{3, 0},	{-10, 0},	{0, 0},	{0},	{0, 0},	{0, 0},	{0, 0},	{0, 0},	{0},	{1},	{-1},	{0,""},	{0, 0},	{0},	{0},	{-1},	{0, 0}},
		
	};
}
