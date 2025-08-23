package com.alderangaming.wizardsencounters;

import java.io.Serializable;

import android.content.Context;
import android.util.Log;

public class ItemArmor extends StoreItem implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 767299361947834227L;

	private int _minDropRound = 0;
	private int _maxDropRound = 0;
	private int _chanceToDrop = 0;
	private int _blockMin = 0;
	private int _blockMax = 0;
	private int _blockChance = 0;
	private int _modifyDodge = 0;
	private int _modifyInitiative = 0;
	private int _modifyAbilitySuccess = 0;
	private int _modifyStrength = 0;
	private int _modifyAgility = 0;
	private int _modifyIntelligence = 0;
	private int _givesRuneID = -1;
	private int _modifyHP = 0;
	private int _modifyAP = 0;

	public ItemArmor(int t, int i, Context c)
	{
		super(t, i);
		makeSelf(i);
		
		setName(DefinitionArmor.ARMOR_NAMES[i]);
		setDescription(DefinitionArmor.ARMOR_DESCRIPTIONS[i]);
		setAvailForANewPlayer(DefinitionArmor.ARMOR_AVAILABLE_FOR_NEW_PLAYER[i]);
		setImageName(DefinitionArmor.ARMOR_IMAGE[i], DefinitionArmor.ARMOR_IMAGE[i], c);
		
		setValue(DefinitionArmor.ARMOR_GOLD_VALUE[i]);
		setCost(value());
		
		setMinLevel( DefinitionArmor.ARMOR_MIN_LEVEL_TO_USE[i]);
		setShowsInStore(DefinitionArmor.ARMOR_SHOWS_IN_STORE[i]);
		setOnlyForClasses(DefinitionArmor.ARMOR_FOR_CLASS_ONLY[i]);
	}

	private void makeSelf(int i)
	{		

		_minDropRound = DefinitionArmor.ARMOR_DROPS[i][0];
		_maxDropRound = DefinitionArmor.ARMOR_DROPS[i][1];
		_chanceToDrop = DefinitionArmor.ARMOR_DROPS[i][2];
		_blockChance = DefinitionArmor.ARMOR_BLOCK_DAMAGE[i][0];
		_blockMin = DefinitionArmor.ARMOR_BLOCK_DAMAGE[i][1];
		_blockMax = DefinitionArmor.ARMOR_BLOCK_DAMAGE[i][2];
		_modifyDodge = DefinitionArmor.ARMOR_MODIFIES[i][0];
		
		Log.d("armorinfo","armor object "+ DefinitionArmor.ARMOR_NAMES[i]+" set modifydodge: "+_modifyDodge);
		
		_modifyInitiative = DefinitionArmor.ARMOR_MODIFIES[i][1];
		_modifyAbilitySuccess = DefinitionArmor.ARMOR_MODIFIES[i][2];
		_modifyStrength = DefinitionArmor.ARMOR_MODIFIES[i][3];
		_modifyAgility = DefinitionArmor.ARMOR_MODIFIES[i][4];
		_modifyIntelligence = DefinitionArmor.ARMOR_MODIFIES[i][5];
		_givesRuneID = DefinitionArmor.ARMOR_GRANTS_RUNE_ID[i];
		_modifyHP = DefinitionArmor.ARMOR_ADDS_HP[i];
		_modifyAP = DefinitionArmor.ARMOR_ADDS_AP[i];
		
	}

	public int minDropRound()
	{
		return _minDropRound;
	}
	
	public int maxDropRound()
	{
		return _maxDropRound;
	}

	public int chanceToDrop()
	{
		return _chanceToDrop;
	}
	
	public int blockChance()
	{
		return _blockChance;
	}

	public int blockMin()
	{
		return _blockMin;
	}

	public int blockMax()
	{
		return _blockMax;
	}

	public int modifyDodge()
	{
		return _modifyDodge;
	}

	public int modifyInitiative()
	{
		return _modifyInitiative;
	}

	public int modifyAbilitySuccess()
	{
		return _modifyAbilitySuccess;
	}

	public int modifyStrength()
	{
		return _modifyStrength;
	}

	public int modifyAgility()
	{
		return _modifyAgility;
	}

	public int modifyIntelligence()
	{
		return _modifyIntelligence;
	}

	public int givesRuneID()
	{
		return _givesRuneID;
	}

	public int modifyHP()
	{
		return _modifyHP;
	}

	public int modifyAP()
	{
		return _modifyAP;
	}

}
