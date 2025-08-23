package com.alderangaming.wizardsencounters;

public class DefinitionClassType
{

	public static final String[] CLASS_TYPE_NAME =
	{ "Brawler", "Survivalist", "Spellczar", "Reaper", "Shield" };
	
	
	public static final String[] CLASS_TYPE_DESCRIPTION = {
		"Class starts combat with 0 AP, gains AP only when he successfully hits with an attack",
		"Class starts combat with full AP, counts +1 per turn in combat",
		"Class retains AP from previous battle, only gains back AP after end of full round",
		"AP starts full every battle, reduces by 1 each turn.",
		"Begins combat with full AP. AP is used as a shield before HP is taken away."
	};
	
	public static final int[][] CLASS_TYPE_AP_TREE_BY_LEVEL = {
		{0, 5, 6, 7, 9, 10, 10, 10, 10, 10, 10, 10, 10},
		{0, 5, 9, 12, 16, 18, 20, 20, 20, 20, 20, 20, 20},
		{0, 3, 6, 10, 17, 25, 37, 48, 65, 79, 86, 96, 110},
		{0, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10},
		{0, 3, 6, 10, 17, 25, 37, 48, 65, 79, 86, 96, 110}
	};
	
	
	/*
	 * "{start combat will full ap flag, start combat with no ap flag, gain ap
	 * on hit amount, mod ap per turn amount, recharge ap on round start flag,
	 * ap is shield flag}"
	 */
	public static final int[][] CLASS_TYPE_AP_REGEN =
	{
	{ 0, 1, 1, 0, 0, 0 },
	{ 1, 0, 0, 1, 0, 0 },
	{ 0, 0, 0, 0, 1, 0 },
	{ 1, 0, 0, -1, 0, 0 },
	{ 1, 0, 0, 0, 0, 1 } };

}
