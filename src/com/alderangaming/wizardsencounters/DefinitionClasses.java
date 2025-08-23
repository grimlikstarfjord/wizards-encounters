package com.alderangaming.wizardsencounters;

public class DefinitionClasses
{

	public static final String[] CLASS_NAMES =
	{ "Squire", "Huntsman", "Wizard", "Reaper", "Gambler", "Battle Monk", "Mage Inquisitor", "Assailant", "Barbarian",
		"Dartsmith", "Martial Artist", "Dark Psychic", "Dreambuilder", "Templar", "Spiteful Bard" };
	public static final String[] CLASS_DESCRIPTIONS =
		{
			"The squire is a young lad who maintains a balance of all attributes, yet a master of no one particular skill. He can use most non-magic based abilities.",
			"The Huntsman has lived most of his life hunting monsters. He has extensive knowledge and has developed very high reaction skill.",
			"The Young Wizard has been taught the basics of combat wizardry, and thus has extensive magelore.  He can use any ability, although his weapon attacks are quite weak.",
			"Nobody really knows who the Reaper is. Some say he is a banished warlord from another dimension. Others believe he is the direct descendant of the exalted Ford King. He usually resides among the garbage and roams aimlessly in the night, searching for meaning, and perhaps love. The Reaper mysteriously does not shave or bathe, as it is believed to bring him great willpower.",
			"The Gambler is a social risk taker, and has the ability to magically imbue anything he touches with his silky fingers.  He is tall, skinny and has an unusually high voice.",
			"The Battle Monk is out to rid the world of evil. He has strength, knowledge, and lots of divine luck on his side in the battlefield. The monk is not able to use dark abilities.",
			"The Mage Inquisitor is a very holy class, bound by the tradition of his religion.  He draws from his unwavering faith in order to harm those who would be forcefully betrothed to him or his people.",
			"The assailant is the scum of the underworld, known for dirty jokes and nasty tactics to defeat his foes.  He learned his moves from the smugglers of the world.",
			"The barbarian is a huge hulking person, who regularly feasts on many huge meals a day with corn.  His abilities focus more on brute strength and non tactics, allowing him to produce rage and overpower his foes.",
			"The Dartsmith is a peculiar person, who holds many interesting gadgets within their possession.  Intrigued with luck and machinery, the Dartsmith regularly has an ace up his sleeve for the task that needs to be done. ",
			"The Martial Artist is a very disciplined person, who uses patience and understand to confuse and intimidate his enemies.",
			"The Dark Psychic draws power from their ancestors, who were wronged by their slave masters.  They can produce many fearful reverberation and chants.",
			"A master of dreams, the Dreambuilder can rearrange the mind of their enemies.  They use incredible mind power to dig deep and destroy.",
			"Holy in nature, but a warrior by strength, the Templar shows their courage through their faith and ability.",
			"The Spiteful Bard was betrayed by their love, vowing to never love again. He uses the powers of bad music for revenge." };

	public static final int[] CLASS_AVAIL_FOR_NEW_PLAYER =
	{ 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };

	public static final int[] CLASS_SHOWS_IN_STORE =
	{ 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, };

	// weapon, armor1, armor2, armor3, armor4, item1, item2, rune1, rune2
	public static final int[][] CLASS_PRELOADED_ITEMS =
	{
	{ 0, 0, 31, 62, 97, -1, -1, 1, 12 },
	{ 0, 0, 31, 62, 97, -1, -1, 0, 11 },
	{ 0, 0, 31, 62, 97, -1, -1, 2, 18 },
	{ 6, 0, 31, 62, 97, -1, -1, 3, 8 },
	{ 8, 0, 31, 62, 97, -1, -1, 7, 57 },
	{ 10, 0, 31, 62, 97, -1, -1, 1, 9 },
	{ 14, 0, 31, 62, 97, -1, -1, 23, 28 },
	{ 13, 0, 31, 62, 97, -1, -1, 43, 46 },
	{ 15, 0, 31, 62, 97, -1, -1, 5, 52 },
	{ 16, 0, 31, 62, 97, -1, -1, 39, 26 },
	{ 17, 0, 31, 62, 97, -1, -1, 4, 53 },
	{ 18, 0, 31, 62, 97, -1, -1, 27, 37 },
	{ 19, 0, 31, 62, 97, -1, -1, 13, 55 },
	{ 20, 0, 31, 62, 97, -1, -1, 23, 24 },
	{ 21, 0, 31, 62, 97, -1, -1, 60, 41 }, };

	public static final int[][] CLASS_STARTING_RUNES =
	{
	{ 1, 12 },
	{ 0, 11, 27 },
	{ 2, 18, 22, 27, 29 },
	{ 3, 8, 42 },
	{ 7, 57 },
	{ 1, 9, 30 },
	{ 23, 28, 31 },
	{ 43, 46 },
	{ 5, 52 },
	{ 39, 26 },
	{ 4, 53 },
	{ 27, 37, 49 },
	{ 13, 55 },
	{ 23, 24 },
	{ 60, 41, 33 } };

	public static final int[][] CLASS_HP_TREE =
	{
	{ 80, 100, 125 },
	{ 70, 85, 100 },
	{ 50, 65, 80 },
	{ 70, 90, 115 },
	{ 85, 105, 130 },
	{ 90, 110, 140 },
	{ 60, 70, 85 },
	{ 75, 90, 105 },
	{ 100, 125, 160 },
	{ 75, 90, 105 },
	{ 70, 90, 115 },
	{ 65, 75, 90 },
	{ 65, 75, 90 },
	{ 85, 105, 130 },
	{ 50, 65, 75 }, };

	public static final int[] CLASS_TYPE =
	{ 0, 1, 2, 3, 4, 0, 2, 3, 0, 1, 0, 2, 3, 1, 4 };

	public static final int[][] CLASS_BASE_STATS =
	{
	{ 6, 3, 2, 2, 3 },
	{ 3, 6, 4, 2, 1 },
	{ 1, 1, 5, 9, 2 },
	{ 4, 3, 1, 2, 6 },
	{ 2, 1, 4, 3, 8 },
	{ 6, 4, 4, 2, 1 },
	{ 2, 2, 5, 6, 1 },
	{ 3, 4, 4, 1, 4 },
	{ 9, 4, 1, 1, 2 },
	{ 2, 3, 6, 1, 6 },
	{ 6, 8, 2, 1, 2 },
	{ 1, 1, 6, 8, 2 },
	{ 2, 2, 5, 4, 2 },
	{ 3, 1, 6, 3, 3 },
	{ 2, 3, 4, 4, 4 } };

	public static final int[][] CLASS_BASE_HIT_CHANCE_BY_LEVEL =
	{
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 } };

	public static final int[][] CLASS_BASE_DODGE_CHANCE_BY_LEVEL =
	{
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 },
	{ 20, 20, 20, 20 } };

	public static final int[][] CLASS_DROPS =
	{
	{ -1, -1, 0, 0 },
	{ 5, 5, 30, 2 },
	{ 5, 5, 30, 2 },
	{ 5, 5, 30, 2 },
	{ 10, 10, 30, 2 },
	{ 10, 10, 30, 2 },
	{ 15, 15, 30, 2 },
	{ 15, 15, 30, 2 },
	{ 15, 15, 30, 2 },
	{ 10, 10, 30, 2 },
	{ 15, 15, 30, 2 },
	{ 10, 10, 30, 2 },
	{ 5, 5, 30, 2 },
	{ 15, 15, 30, 2 },
	{ 15, 15, 30, 2 }, };
}
