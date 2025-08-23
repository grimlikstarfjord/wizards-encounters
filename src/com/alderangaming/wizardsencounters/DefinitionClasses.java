package com.alderangaming.wizardsencounters;

public class DefinitionClasses
{

	public static final String[] CLASS_NAMES = {
		"Squire",
		"Huntsman",
		"Wizard",
		"Reaper",
		"Gambler",
		"Battle Monk",
		"Inqusitor",
		"Assailant",
		"Barbarian",
		"Dartsmith",
		"Martial Artist",
		"Dark Psychic",
		"Dreambuilder",
		"Templar",
		"Spiteful Bard"
	};
	public static final String[] CLASS_DESCRIPTIONS = {
		"The squire is a young lad who maintains a balance of all attributes, yet a master of no one particular skill. He can use most non-magic based abilities.",
		"The Huntsman has lived most of his life hunting monsters. He has extensive knowledge and has developed very high reaction skill.",
		"The Young Wizard has been taught the basics of combat wizardry, and thus has extensive magelore.  He can use any ability, although his weapon attacks are quite weak.",
		"The Disgruntled Farmer has decided to take his anger out on the monster world. His farming background gives him knowledge, and his anger gives him strength.",
		"The Gambler is a risk taker, and has the ability to magically imbue anything he touches.  This ability has created a reckless being of rage",
		"The Battle Monk is out to rid the world of evil. He has strength, knowledge, and lots of divine luck on his side in the battlefield. The monk is not able to use dark abilities.",
		"The Inquisitor is a very holy class, bind by the grace of his religion.  He relies on his faith in order to harm those who would be trove him.",
		"The assailant is the scum of the underworld, known for dirty tricks and tactics to defeat his foes.  He learned his moves from the smugglers of the world.",
		"The barbarian is a huge hulking person, who regularly feasts on many huge meals a day.  His abilities focus more on brute strength and non tactics, allowing him to produce rage and overpower his foes.",
		"The Dartsmith is a peculiar person, who holds many interesting gadgets within their possession.  Intrigued with luck and machinery, the dartsmith regularly has an ace up his sleeve for the task that needs to be done.", 
		"The Martial Artist is a very disciplined person, who uses patience and understand to confuse and intimidate his enemies.",
		"The Dark Psychic draws power from their ancestors, who were wronged by their slave masters.  They can produce many fearful reverberation and chants.",
		"A master of dreams, the Dreambuilder can rearrange the mind of their enemies.  They use incredible mind power to dig deep and destroy.",
		"Holy in nature, but a warrior by strength, the Templar shows their courage through their faith and ability.",
		"The Spiteful Bard was betrayed by their love, vowing to never love again, and use their powers of music for revenge."
		};
	
	public static final int[] CLASS_AVAIL_FOR_NEW_PLAYER = {
		1,
		1,
		1,
		1,
		1,
		1, 
		1,
		1,
		1,
		1,
		1,
		1,
		1,
		1,
		1
	};
	
	public static final int[] CLASS_SHOWS_IN_STORE = {
		0,
		0,
		0,
		0,
		0,
		0,
		0,
		0,
		0,
		0,
		0,
		0,
		0,
		0,
		0
	};
	
	//weapon, armor1, armor2, armor3, armor4, item1, item2, rune1, rune2
	public static final int[][] CLASS_PRELOADED_ITEMS = {
		{0, 0, 1, 2, 3, -1, -1, 4, -1},
		{0, 0, 1, 2, 3, -1, -1, 4, -1},
		{0, 0, 1, 2, 3, -1, -1, 2, -1},
		{0, 0, 1, 2, 3, -1, -1, 0, -1},
		{0, 0, 1, 2, 3, -1, -1, 3, -1},
		{0, 0, 1, 2, 3, -1, -1, 1, -1},
		{0, 0, 1, 2, 3, -1, -1, 1, -1},
		{0, 0, 1, 2, 3, -1, -1, 1, -1},
		{0, 0, 1, 2, 3, -1, -1, 1, -1},
		{0, 0, 1, 2, 3, -1, -1, 1, -1},
		{0, 0, 1, 2, 3, -1, -1, 1, -1},
		{0, 0, 1, 2, 3, -1, -1, 1, -1},
		{0, 0, 1, 2, 3, -1, -1, 1, -1},
		{0, 0, 1, 2, 3, -1, -1, 1, -1},
		{0, 0, 1, 2, 3, -1, -1, 1, -1}
	};
	
	public static final int[][] CLASS_STARTING_RUNES = {
		{4},
		{4},
		{2},
		{0},
		{3},
		{1},
		{1},
		{1},
		{1},
		{1},
		{1},
		{1},
		{1},
		{1},
		{1}
	};	
	
	public static final int[][] CLASS_HP_TREE = {
    //lvl0,  1, 2,  3,  4,  5...
		{0, 30, 36, 44, 53, 64, 77, 93, 112, 135, 162, 195, 234},
		{0, 30, 36, 44, 53, 64, 77, 93, 112, 135, 162, 195, 234},
		{0, 30, 36, 44, 53, 64, 77, 93, 112, 135, 162, 195, 234},
		{0, 30, 36, 44, 53, 64, 77, 93, 112, 135, 162, 195, 234},
		{0, 30, 36, 44, 53, 64, 77, 93, 112, 135, 162, 195, 234},
		{0, 30, 36, 44, 53, 64, 77, 93, 112, 135, 162, 195, 234},
		{0, 30, 36, 44, 53, 64, 77, 93, 112, 135, 162, 195, 234},
		{0, 30, 36, 44, 53, 64, 77, 93, 112, 135, 162, 195, 234},
		{0, 30, 36, 44, 53, 64, 77, 93, 112, 135, 162, 195, 234},
		{0, 30, 36, 44, 53, 64, 77, 93, 112, 135, 162, 195, 234},
		{0, 30, 36, 44, 53, 64, 77, 93, 112, 135, 162, 195, 234},
		{0, 30, 36, 44, 53, 64, 77, 93, 112, 135, 162, 195, 234},
		{0, 30, 36, 44, 53, 64, 77, 93, 112, 135, 162, 195, 234},
		{0, 30, 36, 44, 53, 64, 77, 93, 112, 135, 162, 195, 234},
		{0, 30, 36, 44, 53, 64, 77, 93, 112, 135, 162, 195, 234}
	};
	
	public static final int[] CLASS_TYPE = {
		0,
		1,
		2,
		3,
		4,
		0,
		2,
		3,
		0,
		1,
		0,
		2,
		3,
		4,
		1
	};
	
	public static final int[][] CLASS_BASE_STATS = {
		{5, 5, 5, 5, 5},
		{5, 5, 5, 5, 5},
		{5, 5, 5, 5, 5},
		{5, 5, 5, 5, 5},
		{5, 5, 5, 5, 5},
		{5, 5, 5, 5, 5},
		{5, 5, 5, 5, 5},
		{5, 5, 5, 5, 5},
		{5, 5, 5, 5, 5},
		{5, 5, 5, 5, 5},
		{5, 5, 5, 5, 5},
		{5, 5, 5, 5, 5},
		{5, 5, 5, 5, 5},
		{5, 5, 5, 5, 5},
		{5, 5, 5, 5, 5}
	};	
	
	public static final int[][] CLASS_BASE_HIT_CHANCE_BY_LEVEL = {
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}	
	};

	public static final int[][] CLASS_BASE_DODGE_CHANCE_BY_LEVEL = {
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}, 
		{20, 20, 20, 20}
	};
	
	public static final int[][] CLASS_DROPS = {
		{-1, -1, 0},
		{5, 5, 30},
		{10, 10, 30},
		{15, 15, 30},
		{10, 10, 30},
		{15, 15, 30},
		{5, 5, 30},
		{15, 15, 30},
		{10, 10, 30},
		{15, 15, 30},
		{5, 5, 30},
		{15, 15, 30},
		{10, 10, 30},
		{15, 15, 30},
		{5, 5, 30}
	};
}
