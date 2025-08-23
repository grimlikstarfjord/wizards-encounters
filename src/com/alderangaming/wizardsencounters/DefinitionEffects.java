package com.alderangaming.wizardsencounters;

public class DefinitionEffects
{

	public static String[] EFFECT_NAMES = {
		"Bleeding",
		"Blessed",
		"Blinded",
		"Casting",
		"Chaotic",
		"Clumsy",
		"Confused",
		"Cursed",
		"Dazed",
		"Demoralized",
		"Dexterous",
		"Disciplined",
		"Entranced",
		"Exhausted",
		"Fatigued",
		"Focused",
		"Hardened Skin",
		"Lucky",
		"Perceptive",
		"Raging",
		"Smoldering",
		"Stunned",
		"Taunting",
		"Underdog",
		"Unstable",
		"Vulnerable"
	};

	public static String[] EFFECT_DESCRIPTIONS = {
		"-6% HP/turn",
		"Regen 5% HP/turn",
		"-50% Hit Chance",
		"Turn skipped",
		"+50% Crit; -30% Hit Chance",
		"-10% Dodge Chance",
		"Unable to use Abilities",
		"-10% HP/turn",
		"-50% Reaction",
		"-50% Execution",
		"+20% Dodge Chance",
		"Regen 1-% AP/turn",
		"-10% AP/turn",
		"-30% Hit Damage",
		"-10% HP/turn",
		"Immune to negative effects",
		"Absorb 50% dmg",
		"+10 to all rolls",
		"+30% Knowledge",
		"+10% Crit Chance",
		"-2% HP/turn",
		"turn skipped",
		"+20% Hit Chance",
		"+50% Hit Damage",
		"-30% Chance to Hit",
		"-30% Dodge Chance"
	};
	
	public static String[] EFFECT_IMAGE = {
		"bleeding",
		"blessed",
		"blinded",
		"casting",
		"chaotic",
		"clumsy",
		"confused",
		"cursed",
		"dazed",
		"demoralized",
		"dexterous",
		"disciplined",
		"entranced",
		"exhausted",
		"fatigued",
		"focused",
		"hardenedskin",
		"lucky",
		"perceptive",
		"raging",
		"smoldering",
		"stunned",
		"taunting",
		"underdog",
		"unstable",
		"vulnerable"
	};

	public static int[] EFFECT_LASTS_TURNS =
	{ 5, 5, 5, 0, 5, 50, 7, 8, 6, 5, 5, 10, 4, 7, 100, 10, 6, 7, 20, 6, 6, 0, 5, 5, 4, 6 };

	public static boolean[] EFFECT_STUNS =
	{ false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false,
		false, false, false, false, false, true, false, false, false, false };

	public static boolean[] EFFECT_BLOCKS_ABILITIES =
	{ false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false,
		false, false, false, false, false, false, false, false, false, false };

	public static boolean[] EFFECT_MAKES_IMMUNE_TO_EFFECTS =
	{ false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true,
		false, false, false, false, false, false, false, false, false, false };

	public static int[] EFFECT_MOD_HP_PER_TURN =
	{ -6, 5, 0, 0, 0, 0, 0, -10, 0, 0, 0, 0, 0, 0, -10, 0, 0, 0, 0, 0, -2, 0, 0, 0, 0, 0 };

	public static int[] EFFECT_MOD_AP_PER_TURN =
	{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	public static int[] EFFECT_MOD_HIT_CHANCE =
	{ 0, 0, -50, 0, -30, 0, 0, 0, 0, 0, 0, 0, 0, -30, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0, 30, 0 };

	public static int[] EFFECT_MOD_HIT_DAMAGE =
	{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -50, 0, 0, 0, 0, 0, 0, 50, 0, 0 };

	public static int[] EFFECT_MOD_CRIT_CHANCE =
	{ 0, 0, 0, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0 };

	public static int[] EFFECT_MOD_STRENGTH =
	{ 0, 0, 0, 0, 0, 0, 0, 0, 0, -50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	public static int[] EFFECT_MOD_REACTION =
	{ 0, 0, 0, 0, 0, 0, 0, 0, -50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	public static int[] EFFECT_MOD_KNOWLEDGE =
	{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0 };
	
	public static int[] EFFECT_MOD_MAGELORE =
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0 };
	
	public static int[] EFFECT_MOD_LUCK =
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0 };
	
	public static int[] EFFECT_MOD_DODGE =
	{ 0, 0, 0, 0, 0, -10, 0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -30, };
}
