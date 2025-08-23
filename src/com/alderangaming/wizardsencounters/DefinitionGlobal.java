package com.alderangaming.wizardsencounters;

public class DefinitionGlobal
{

	public static final int DEBUG_REBUILD_DB = 0;
	
	public static final int TUTORIAL = 0;
	public static final int SETTINGS = 1;
	public static final int ABOUT = 2;
	
	public static final int DEFAULT_MAX_ABILITIES_ALLOWED = 8;
	
	public static final int DEFAULT_STARTING_GOLD = 5000;
	public static final double DODGE_TIME = 0.8;
	public static final int RUN_CHANCE = 20;
	
	public static final int[] CHEST_OPEN_COST = {0, 5, 10, 20, 50, 100};
	public static final int CHANCE_FREE_CHEST_EMPTY = 50;
	public static final int MAX_CHESTS_TO_OPEN = 2;
	
	public static final int ITEM_TYPE_PLAYER_CLASS = 0;
	public static final int ITEM_TYPE_WEAPON = 1;
	public static final int ITEM_TYPE_ARMOR = 2;
	public static final int ITEM_TYPE_ITEM = 3;
	public static final int ITEM_TYPE_RUNE_ABILITY = 4;
	
	public static String[] EQUIP_TYPE_NAMES =
	{ "Helm Armor", "Weapon", "Chest Armor", "Shoes", "Trinket", "Item", "Ability Rune", "Class Rune"};
	
	public static int[] EQUIP_TYPE_INDEX = 
	{ ITEM_TYPE_ARMOR, ITEM_TYPE_WEAPON, ITEM_TYPE_ARMOR, ITEM_TYPE_ARMOR, ITEM_TYPE_ARMOR, ITEM_TYPE_ITEM, ITEM_TYPE_RUNE_ABILITY, ITEM_TYPE_PLAYER_CLASS};
	
	public static String[] EQUIP_SLOT_NAMES =
	{ "Helm Armor", "Weapon", "Chest Armor", "Shoes", "Trinket", "Item 1", "Item 2"};

	public static final int[] EQUIP_SLOT_HELM = {0};
	public static final int[] EQUIP_SLOT_WEAPON = {1};
	public static final int[] EQUIP_SLOT_CHEST = {2};
	public static final int[] EQUIP_SLOT_SHOES = {3};
	public static final int[] EQUIP_SLOT_TRINKET = {4};
	public static final int[] EQUIP_SLOT_ITEM = {5, 6};
	
	

}
