package com.alderangaming.wizardsencounters;

import java.io.Serializable;

import android.util.Log;

public class CombatEffect implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8550307165640802775L;
	private int _id = -1;
	private int _turnsRemaining = 0;			
	private boolean _stuns = false;
	private boolean _blocksAbilities = false;
	private boolean _makesImmuneToEffects = false;
	private int _modHPPerTurn = 0;
	private int _modAPPerTurn = 0;
	private int _modHitChance = 0;
	private int _modHitDamage = 0;
	private int _modCritChance = 0;
	private int _modStrength = 0;
	private int _modReaction = 0;
	private int _modKnowledge = 0;
	private int _modMagelore = 0;
	private int _modLuck = 0;
	private int _modDodge = 0;
	private String _name = "";
	private String _description = "";
	private String _image = "";
	private int _imageResource = 0;
	
	public CombatEffect(int id)
	{
		_id = id;
		updateSelf();
	}
	
	private void updateSelf()
	{
		_stuns = DefinitionEffects.EFFECT_STUNS[_id];
		_name = DefinitionEffects.EFFECT_NAMES[_id];
		_image = DefinitionEffects.EFFECT_IMAGE[_id];
		_description = DefinitionEffects.EFFECT_DESCRIPTIONS[_id];
		_blocksAbilities = DefinitionEffects.EFFECT_BLOCKS_ABILITIES[_id];
		_makesImmuneToEffects = DefinitionEffects.EFFECT_MAKES_IMMUNE_TO_EFFECTS[_id];
		_modHPPerTurn = DefinitionEffects.EFFECT_MOD_HP_PER_TURN[_id];
		_modAPPerTurn = DefinitionEffects.EFFECT_MOD_AP_PER_TURN[_id];
		_modHitChance = DefinitionEffects.EFFECT_MOD_HIT_CHANCE[_id];
		_modHitDamage = DefinitionEffects.EFFECT_MOD_HIT_DAMAGE[_id];
		_modCritChance = DefinitionEffects.EFFECT_MOD_CRIT_CHANCE[_id];
		_modStrength = DefinitionEffects.EFFECT_MOD_STRENGTH[_id];
		_modReaction = DefinitionEffects.EFFECT_MOD_REACTION[_id];
		_modKnowledge = DefinitionEffects.EFFECT_MOD_KNOWLEDGE[_id];
		_modMagelore = DefinitionEffects.EFFECT_MOD_MAGELORE[_id];
		_modLuck = DefinitionEffects.EFFECT_MOD_LUCK[_id];
		_modDodge = DefinitionEffects.EFFECT_MOD_DODGE[_id];
		_turnsRemaining = DefinitionEffects.EFFECT_LASTS_TURNS[_id];
		
		Log.d("combat","New effect created: "+_name);
	}
	
	public int id()
	{
		return _id;
	}
	
	public void setTurnsRemaining(int t)
	{
		_turnsRemaining = t;
	}
	
	public String name()
	{
		return _name;
		
	}
	
	public String description()
	{
		return _description;
	}
	
	public String image()
	{
		return _image;
	}
	
	public int imageResource()
	{
		return _imageResource;
	}
	
	public void setImageResource(int r)
	{
		_imageResource = r;
		Log.d("combat","New effect image resource set for "+_name+": "+r);
	}

	public int turnsRemaining()
	{
		return _turnsRemaining;
	}
	
	public void updateTurns()
	{
		_turnsRemaining--;
	}

	public boolean stuns()
	{
		return _stuns;
	}

	public boolean blocksAbilities()
	{
		return _blocksAbilities;
	}
	
	public int modHPPerTurn()
	{
		return _modHPPerTurn;
	}

	public int modAPPerTurn()
	{
		return _modAPPerTurn;
	}

	public int modHitChance()
	{
		return _modHitChance;
	}
	public int modHitDamage()
	{
		return _modHitDamage;
	}

	public int modCritChance()
	{
		return _modCritChance;
	}

	public int modStrength()
	{
		return _modStrength;
	}

	public int modReaction()
	{
		return _modReaction;
	}
	
	public int modKnowledge()
	{
		return _modKnowledge;
	}
	
	public int modMagelore()
	{
		return _modMagelore;
	}
	
	public int modLuck()
	{
		return _modLuck;
	}

	public int modDodge()
	{
		return _modDodge;
	}

	public boolean makesImmuneToEffects()
	{
		return _makesImmuneToEffects;
	}		
	
	public void addTurn()
	{
		_turnsRemaining++;
	}
	
}
