package com.alderangaming.wizardsencounters;

public class DefinitionClassType
{

	public static final String[] CLASS_TYPE_NAME =
	{ "Brawler", "Survivalist", "Spellczar", "Reaper", "Shield" };

	public static final String[] CLASS_TYPE_DESCRIPTION =
	{ "Class starts combat with 0 AP, gains AP only when he successfully hits with an attack",
		"Class starts combat with 0 AP, gains 1 AP per turn",
		"Class retains AP from previous battle, only gains back AP after end of full round",
		"AP starts full every battle, reduces by 1 each turn.",
		"Begins combat with full AP. AP is used as a shield before HP is taken away." };

	public static final int[][] CLASS_TYPE_AP_TREE_BY_LEVEL =
	{
		{5, 6, 7, 10},
		{ 5, 5, 6, 7},
		{ 35, 42, 50, 58},
		{15, 15, 15, 15},
		{15, 20, 25, 30}, };

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
