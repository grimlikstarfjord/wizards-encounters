package com.alderangaming.wizardsencounters;

import java.io.Serializable;
import java.util.ArrayList;

import android.util.Log;

public abstract class Actor implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8996666682297682729L;

	private String _name = "";
	private int _rank = -1;
	private ArrayList<Integer> _activeAbilities = new ArrayList<Integer>();
	private ArrayList<CombatEffect> _activeEffects = new ArrayList<CombatEffect>();

	public void removeEffects()
	{
		_activeEffects.clear();
	}

	private int _actorType = -1;

	private int _currentHP = 0;
	private int _currentAP = 0;

	private int _baseStrength = 0;
	private int _baseReaction = 0;
	private int _baseKnowledge = 0;
	private int _baseMagelore = 0;
	private int _baseLuck = 0;
	private int _baseHitChance = 0;
	private int _baseCritChance = 0;
	private int _baseStunChance = 0;
	private int _baseInitiative = 0;
	private int _baseDodgeChance = 0;

	private int _abilityAbsorbDamage = 0;
	private int _abilityModHitDamage = 0;
	private int _abilityModStrength = 0;
	private int _abilityModKnowledge = 0;
	private int _abilityModReaction = 0;
	private int _abilityModMagelore = 0;
	private int _abilityModLuck = 0;
	private int _abilityModHitChance = 0;
	private int _abilityModCritChance = 0;
	private int _abilityModStunChance = 0;

	private int _counterModStrength = 0;
	private int _counterModKnowledge = 0;
	private int _counterModReaction = 0;
	private int _counterModMagelore = 0;
	private int _counterModLuck = 0;

	private int _counterModStrength2 = 0;
	private int _counterModKnowledge2 = 0;
	private int _counterModReaction2 = 0;
	private int _counterModMagelore2 = 0;
	private int _counterModLuck2 = 0;

	private boolean _stunned = false;
	private boolean _dead = false;
	private boolean _cannotUseAbilities = false;
	private boolean _immuneToEffects = false;
	private boolean _casting = false;
	private int _castingTurnsLeft = 0;

	private ItemRune _currentCastingAbility = null;

	public void resetFlags()
	{
		_stunned = false;
		_dead = false;
		_cannotUseAbilities = false;
		_immuneToEffects = false;
		_casting = false;
	}

	private int _activeCastingAbility = -1;

	private int _effectModHPTurn = 0;
	private int _effectModAPTurn = 0;
	private int _effectModHitChance = 0;
	private int _effectModCritChance = 0;
	private int _effectModHitDamage = 0;
	private int _effectModStrength = 0;
	private int _effectModReaction = 0;
	private int _effectModKnowledge = 0;
	private int _effectModMagelore = 0;
	private int _effectModLuck = 0;
	private int _effectModDodge = 0;
	private int _effectModStunChance = 0;

	private int _counterAbsorbDamageMin = 0;
	private int _counterAbsorbDamageMax = 0;
	private int _counterAbsorbDamageTurns = 0;

	private int _counterModifyHPStatBasedTurns = 0;
	private int _counterModifyHPStatBasedId = 0;
	private int _counterModifyHPStatBasedMult = 0;

	private int _counterDotModifyHPMaxHPBasedHPTurns = 0;
	private int _counterDotModifyHPMaxHPBasedHPAmount = 0;

	private int _counterDotModifyHPStatBasedTurns = 0;
	private int _counterDotModifyHPStatBasedAmount = 0;

	private int _counterModifyWeaponDamageTurns = 0;
	private int _counterModifyWeaponDamageAmount = 0;
	private int _counterModifyWeaponDamageAmountBonus = 0;
	private int _counterModifyWeaponDamageBonusEffectActiveId = -1;

	private String _counterDotModifyHPWeaponDamageBasedName = "";
	private int _counterDotModifyHPWeaponDamageBasedTurns = 0;
	private int _counterDotModifyHPWeaponDamageBasedMinAmount = 0;
	private int _counterDotModifyHPWeaponDamageBasedMinAmountBonus = 0;
	private int _counterDotModifyHPWeaponDamageBasedMaxAmount = 0;
	private int _counterDotModifyHPWeaponDamageBasedMaxAmountBonus = 0;
	private int _counterDotModifyHPWeaponDamageBasedEffectActiveId = 0;
	private int _counterDotModifyHPWeaponBasedSourceAmount = 0;

	private int _counterReflectDamageTurns = 0;
	private int _counterReflectPercentAmount = 0;

	private int _counterModifyStatForStatTurnsTurns = 0;
	private int _counterModifyStatForStatTurnsStatId = 0;
	private int _counterModifyStatForStatTurnsAmount = 0;
	
	private int _counterModifyHPStatIsPercentWithMultTurns = 0;
	private int _counterModifyHPStatIsPercentWithMultAmount = 0;

	/* combat instance counters */
	private int _noAbilityInARow = 0;
	private int _missesInARow = 0;
	private int _stunsInARow = 0;

	public int getDamageCounterBonus(int baseDmg)
	{
		// return active counter bonus
		int dmg = 0;
		if (_counterModifyWeaponDamageTurns > 0)
		{
			if (isEffectActive(_counterModifyWeaponDamageBonusEffectActiveId))
			{
				dmg += Helper.getPercentFromInt(_counterModifyWeaponDamageAmountBonus, baseDmg);
			}
			else
			{
				dmg += Helper.getPercentFromInt(_counterModifyWeaponDamageAmount, baseDmg);
			}
		}
		return dmg;
	}

	public abstract int initiative();

	public abstract int hitChance();

	public abstract int getDamage();

	public void modifyAttackPower(int amount, int turns)
	{
		// TODO did I do this?
	}

	public void setName(String n)
	{
		_name = n;
	}

	public String name()
	{
		return _name;
	}

	public void setRank(int r)
	{
		_rank = r;
	}

	public int rank()
	{
		return _rank;
	}

	public int abilityAbsorbDamage()
	{
		return _abilityAbsorbDamage;
	}

	public int modHitChanceEffect()
	{
		return _effectModHitChance;
	}

	public int modHitChanceAbility()
	{
		return _abilityModHitChance;
	}

	public void setBaseHitChance(int h)
	{
		_baseHitChance = h;
	}

	public void setBaseCritChance(int c)
	{
		_baseCritChance = c;
	}

	public void setBaseStunChance(int c)
	{
		_baseStunChance = c;
	}

	public void setBaseStrength(int s)
	{
		_baseStrength = s;
	}

	public void setBaseReaction(int a)
	{
		_baseReaction = a;
	}

	public void setBaseKnowledge(int i)
	{
		_baseKnowledge = i;
	}

	public void setBaseMagelore(int i)
	{
		_baseMagelore = i;
	}

	public void setBaseLuck(int i)
	{
		_baseLuck = i;
	}

	public void createActiveAbilities()
	{
		if (_activeAbilities.size() > 0)
			return;
	}

	public String[] getActiveAbilityNames()
	{
		String[] abNames = new String[_activeAbilities.size()];
		for (int a = 0; a < abNames.length; a++)
		{
			abNames[a] = (String) DefinitionRunes.runeData[_activeAbilities.get(a)][DefinitionRunes.RUNE_NAMES][0];
		}

		return abNames;
	}

	public int getActiveAbilityByIndex(int ind)
	{
		return _activeAbilities.get(ind);
	}

	public int[] getActiveAbilities()
	{
		int[] act = new int[_activeAbilities.size()];
		for (int a = 0; a < act.length; a++)
		{
			act[a] = _activeAbilities.get(a);
		}
		return act;
	}

	public int removeActiveAbility(int id)
	{
		int r = -1;
		for (int a = 0; a < _activeAbilities.size(); a++)
		{
			if (_activeAbilities.get(a) == id)
				r = _activeAbilities.remove(a);
		}
		return r;
	}

	public void addActiveAbility(int rune)
	{
		_activeAbilities.add(rune);
	}

	public boolean checkPlayerHasAbility(int id)
	{
		for (int a = 0; a < _activeAbilities.size(); a++)
			if (_activeAbilities.get(a) == id)
				return true;

		return false;
	}

	private void clearModifiers()
	{
		_stunned = false;
		_cannotUseAbilities = false;
		_immuneToEffects = false;

		_abilityAbsorbDamage = 0;
		_abilityModHitDamage = 0;
		_abilityModStrength = 0;
		_abilityModKnowledge = 0;
		_abilityModReaction = 0;
		_abilityModHitChance = 0;
		_abilityModCritChance = 0;
		_abilityModStunChance = 0;

		_effectModHPTurn = 0;
		_effectModAPTurn = 0;
		_effectModHitDamage = 0;
		_effectModHitChance = 0;
		_effectModCritChance = 0;
		_effectModStunChance = 0;
		_effectModStrength = 0;
		_effectModReaction = 0;
		_effectModKnowledge = 0;
		_effectModDodge = 0;
	}

	public ArrayList<ReturnData> advanceTurn()
	{
		ArrayList<ReturnData> returnData = new ArrayList<ReturnData>();

		clearModifiers();

		returnData = updateAbilityCounters();

		returnData = updateEffectCounters(returnData);

		returnData = updateCastingCounter(returnData);

		return returnData;
	}

	public ItemRune currentCastingAbility()
	{
		return _currentCastingAbility;
	}

	public int castingTurnsLeft()
	{
		return _castingTurnsLeft;
	}

	private ArrayList<ReturnData> updateCastingCounter(ArrayList<ReturnData> returnData)
	{
		if (!_casting)
			return returnData;

		if (_castingTurnsLeft < 0)
		{
			_casting = false;
			_castingTurnsLeft = 0;
			_currentCastingAbility = null;
		}

		ReturnData rd = new ReturnData();
		rd.whatHappend = name() + " is casting... (" + _castingTurnsLeft + ")";
		returnData.add(rd);

		_castingTurnsLeft--;

		return returnData;
	}

	public class ReturnData
	{
		String whatHappend = "";
	}

	private ArrayList<ReturnData> updateAbilityCounters()
	{
		// TODO create the returnData for these abilities

		ArrayList<ReturnData> returnDataArray = new ArrayList<ReturnData>();
		
		_counterModifyHPStatIsPercentWithMultTurns--;
		if(_counterModifyHPStatIsPercentWithMultTurns < 0)
		{
			_counterModifyHPStatIsPercentWithMultTurns = 0;
			_counterModifyHPStatIsPercentWithMultAmount = 0;
		}
		else
		{
			updateHP(_counterModifyHPStatIsPercentWithMultAmount);
			
			ReturnData rd = new ReturnData();			
			if(_counterModifyHPStatIsPercentWithMultAmount > 0)
			{
				rd.whatHappend = name() + " recovered " + _counterModifyHPStatIsPercentWithMultAmount + " HP.";
			}
			else
			{
				rd.whatHappend = name() + " lost " + _counterModifyHPStatIsPercentWithMultAmount + " HP.";
			}
			returnDataArray.add(rd);
		}
		

		_counterModifyStatForStatTurnsTurns--;
		if (_counterModifyStatForStatTurnsTurns < 0)
		{
			_counterModStrength2 = 0;
			_counterModReaction2 = 0;
			_counterModKnowledge2 = 0;
			_counterModMagelore2 = 0;
			_counterModLuck2 = 0;
		}
		else
		{
			if (_counterModifyStatForStatTurnsStatId == 0 || _counterModifyStatForStatTurnsStatId == 99)
			{
				_counterModStrength2 += _counterModifyStatForStatTurnsAmount;

				ReturnData rd = new ReturnData();
				rd.whatHappend = name() + "s Strength was modified by " + _counterModifyStatForStatTurnsAmount + ".";
				returnDataArray.add(rd);
			}
			if (_counterModifyStatForStatTurnsStatId == 1 || _counterModifyStatForStatTurnsStatId == 99)
			{
				_counterModReaction2 += _counterModifyStatForStatTurnsAmount;

				ReturnData rd = new ReturnData();
				rd.whatHappend = name() + "s Reaction was modified by " + _counterModifyStatForStatTurnsAmount + ".";
				returnDataArray.add(rd);
			}
			if (_counterModifyStatForStatTurnsStatId == 2 || _counterModifyStatForStatTurnsStatId == 99)
			{
				_counterModKnowledge2 += _counterModifyStatForStatTurnsAmount;

				ReturnData rd = new ReturnData();
				rd.whatHappend = name() + "s Knowledge was modified by " + _counterModifyStatForStatTurnsAmount + ".";
				returnDataArray.add(rd);
			}
			if (_counterModifyStatForStatTurnsStatId == 3 || _counterModifyStatForStatTurnsStatId == 99)
			{
				_counterModMagelore2 += _counterModifyStatForStatTurnsAmount;

				ReturnData rd = new ReturnData();
				rd.whatHappend = name() + "s Magelore was modified by " + _counterModifyStatForStatTurnsAmount + ".";
				returnDataArray.add(rd);
			}
			if (_counterModifyStatForStatTurnsStatId == 4 || _counterModifyStatForStatTurnsStatId == 99)
			{
				_counterModLuck2 += _counterModifyStatForStatTurnsAmount;

				ReturnData rd = new ReturnData();
				rd.whatHappend = name() + "s Luck was modified by " + _counterModifyStatForStatTurnsAmount + ".";
				returnDataArray.add(rd);
			}
		}

		_counterAbsorbDamageTurns--;
		if (_counterAbsorbDamageTurns < 0)
		{
			_counterAbsorbDamageTurns = 0;
			_counterAbsorbDamageMin = 0;
			_counterAbsorbDamageMax = 0;
		}

		_counterModifyHPStatBasedTurns--;
		if (_counterModifyHPStatBasedTurns < 0)
			_counterModifyHPStatBasedTurns = 0;
		else
		{
			if (_counterModifyHPStatBasedId == 0)
			{
				_counterModStrength += strength() * _counterModifyHPStatBasedMult;

				ReturnData rd = new ReturnData();
				rd.whatHappend =
					name() + "s Strength was modified by " + (strength() * _counterModifyHPStatBasedMult) + ".";
				returnDataArray.add(rd);
			}

			if (_counterModifyHPStatBasedId == 1)
			{
				_counterModReaction += reaction() * _counterModifyHPStatBasedMult;

				ReturnData rd = new ReturnData();
				rd.whatHappend =
					name() + "s Reaction was modified by " + (reaction() * _counterModifyHPStatBasedMult) + ".";
				returnDataArray.add(rd);
			}

			if (_counterModifyHPStatBasedId == 2)
			{
				_counterModKnowledge += knowledge() * _counterModifyHPStatBasedMult;

				ReturnData rd = new ReturnData();
				rd.whatHappend =
					name() + "s Knowledge was modified by " + (knowledge() * _counterModifyHPStatBasedMult) + ".";
				returnDataArray.add(rd);
			}

			if (_counterModifyHPStatBasedId == 3)
			{
				_counterModMagelore += magelore() * _counterModifyHPStatBasedMult;
				ReturnData rd = new ReturnData();
				rd.whatHappend =
					name() + "s Magelore was modified by " + (magelore() * _counterModifyHPStatBasedMult) + ".";
				returnDataArray.add(rd);
			}

			if (_counterModifyHPStatBasedId == 4)
			{
				_counterModLuck += luck() * _counterModifyHPStatBasedMult;

				ReturnData rd = new ReturnData();
				rd.whatHappend = name() + "s Luck was modified by " + (luck() * _counterModifyHPStatBasedMult) + ".";
				returnDataArray.add(rd);
			}

		}

		_counterDotModifyHPMaxHPBasedHPTurns--;
		if (_counterDotModifyHPMaxHPBasedHPTurns < 0)
		{
			_counterDotModifyHPMaxHPBasedHPTurns = 0;
		}
		else
		{
			updateHP(_counterDotModifyHPMaxHPBasedHPAmount);
		}

		_counterDotModifyHPStatBasedTurns--;
		if (_counterDotModifyHPStatBasedTurns < 0)
		{
			_counterDotModifyHPStatBasedTurns = 0;
		}
		else
		{
			updateHP(_counterDotModifyHPStatBasedAmount);
		}

		_counterModifyWeaponDamageTurns--;
		if (_counterModifyWeaponDamageTurns < 0)
		{
			_counterModifyWeaponDamageTurns = 0;
			_counterModifyWeaponDamageAmount = 0;
			_counterModifyWeaponDamageAmountBonus = 0;
		}

		_counterDotModifyHPWeaponDamageBasedTurns--;
		if (_counterDotModifyHPWeaponDamageBasedTurns < 0)
		{
			_counterDotModifyHPWeaponDamageBasedTurns = 0;
			_counterDotModifyHPWeaponDamageBasedMinAmount = 0;
			_counterDotModifyHPWeaponDamageBasedMaxAmount = 0;
			_counterDotModifyHPWeaponDamageBasedMinAmountBonus = 0;
			_counterDotModifyHPWeaponDamageBasedMaxAmountBonus = 0;
			_counterDotModifyHPWeaponDamageBasedEffectActiveId = 0;
		}
		else
		{
			ReturnData rd = new ReturnData();

			int amt = 0;
			int mult = 0;
			// check for bonus
			if (isEffectActive(_counterDotModifyHPWeaponDamageBasedEffectActiveId))
			{
				mult =
					Helper.getRandomIntFromRange(_counterDotModifyHPWeaponDamageBasedMaxAmountBonus,
						_counterDotModifyHPWeaponDamageBasedMinAmountBonus);
			}
			else
			{
				mult =
					Helper.getRandomIntFromRange(_counterDotModifyHPWeaponDamageBasedMaxAmount,
						_counterDotModifyHPWeaponDamageBasedMinAmount);
			}

			amt = Helper.getPercentFromInt(mult, _counterDotModifyHPWeaponBasedSourceAmount);

			updateHP(amt);

			if (amt < 0)
				rd.whatHappend =
					name() + " took " + amt + " damage from " + _counterDotModifyHPWeaponDamageBasedName + ".";
			else if (amt > 0)
				rd.whatHappend =
					name() + " gained " + amt + "HP from " + _counterDotModifyHPWeaponDamageBasedName + ".";
			else
				rd.whatHappend = _counterDotModifyHPWeaponDamageBasedName + " didn't work right";

			returnDataArray.add(rd);
		}

		_counterReflectDamageTurns--;
		if (_counterReflectDamageTurns < 0)
		{
			_counterReflectPercentAmount = 0;
			_counterReflectDamageTurns = 0;
		}

		return returnDataArray;

	}

	private ArrayList<ReturnData> updateEffectCounters(ArrayList<ReturnData> returnData)
	{

		// TODO create returnData for these effect updates

		// first update turns
		for (int a = 0; a < _activeEffects.size(); a++)
		{
			// update turns
			_activeEffects.get(a).updateTurns();

			if (_activeEffects.get(a).turnsRemaining() <= 0)
			{
				_activeEffects.remove(a);
			}
		}
		for (int a = 0; a < _activeEffects.size(); a++)
		{
			if (_activeEffects.get(a).stuns())
			{
				_stunned = true;
			}

			if (_activeEffects.get(a).blocksAbilities())
			{
				_cannotUseAbilities = true;
			}

			if (_activeEffects.get(a).makesImmuneToEffects())
			{
				_immuneToEffects = true;
			}

			if (_activeEffects.get(a).modHPPerTurn() != 0)
			{
				String text = " gained ";
				if (_activeEffects.get(a).modHPPerTurn() < 0)
					text = " lost ";

				ReturnData rd = new ReturnData();
				rd.whatHappend =
					name() + text + updateHP(Helper.getPercentFromInt(_activeEffects.get(a).modHPPerTurn(), maxHP()))
						+ " HP from being " + _activeEffects.get(a) + ".";
			}

			if (_activeEffects.get(a).modAPPerTurn() != 0)
			{
				String text = " gained ";
				if (_activeEffects.get(a).modAPPerTurn() < 0)
					text = " lost ";

				ReturnData rd = new ReturnData();
				rd.whatHappend =
					name() + text + updateAP(Helper.getPercentFromInt(_activeEffects.get(a).modAPPerTurn(), maxAP()))
						+ " AP from being " + _activeEffects.get(a) + ".";
			}

			_effectModHitChance += _activeEffects.get(a).modHitChance();
			_effectModHitDamage += _activeEffects.get(a).modHitDamage();
			_effectModCritChance += _activeEffects.get(a).modCritChance();
			_effectModStrength += _activeEffects.get(a).modStrength();
			_effectModReaction += _activeEffects.get(a).modReaction();
			_effectModKnowledge += _activeEffects.get(a).modKnowledge();
			_effectModMagelore += _activeEffects.get(a).modMagelore();
			_effectModLuck += _activeEffects.get(a).modLuck();
			_effectModDodge += _activeEffects.get(a).modDodge();

		}

		return returnData;
	}

	public ArrayList<CombatEffect> getActiveEffects()
	{
		return _activeEffects;
	}

	public CombatEffect getActiveEffectByIndex(int ind)
	{
		return _activeEffects.get(ind);
	}

	public CombatEffect getActiveEffectByEffectId(int id)
	{
		for (int a = 0; a < _activeEffects.size(); a++)
		{
			if (_activeEffects.get(a).id() == id)
				return _activeEffects.get(a);
		}
		return null;
	}

	public void addStunCount(int turns)
	{

		if (isEffectActive(Helper.getEffectIdByName("Stunned")))
		{
			CombatEffect e = getActiveEffectByEffectId(Helper.getEffectIdByName("Stunned"));
			for (int a = 0; a < turns; a++)
				e.addTurn();

			_stunsInARow++;
		}
		else
		{
			CombatEffect e = new CombatEffect(Helper.getEffectIdByName("Stunned"));
			_activeEffects.add(e);
			for (int a = 0; a < turns; a++)
				e.addTurn();

			_stunsInARow = 1;
		}
	}

	public void addActiveEffect(int effectId)
	{
		// check if effect already active
		if (isEffectActive(effectId))
			return;

		CombatEffect e = new CombatEffect(effectId);
		if (e.id() == Helper.getEffectIdByName("Casting"))
		{
			e.setTurnsRemaining(_castingTurnsLeft);
		}
		_activeEffects.add(e);
	}

	public void setLoadedAbilities(int[] abils)
	{
		for (int a = 0; a < abils.length; a++)
		{
			_activeAbilities.add(abils[a]);
		}
	}

	public boolean stunned()
	{
		return _stunned;
	}

	public void setStunned(boolean _stunned)
	{
		this._stunned = _stunned;
	}

	public boolean cannotUseAbilities()
	{
		return _cannotUseAbilities;
	}

	public boolean immuneToEffects()
	{
		return _immuneToEffects;
	}

	public int modDodgeChance()
	{
		return _effectModDodge;
	}

	public int[] modHitDamage(int dmgAmt)
	{
		int[] dmgmods = new int[3];

		int effBonus = (int) (Math.round(dmgAmt * (_effectModHitDamage * 0.01)));
		int ablBonus = (int) (Math.round(dmgAmt * (_abilityModHitDamage * 0.01)));

		dmgmods[0] = effBonus + ablBonus;
		dmgmods[1] = effBonus;
		dmgmods[2] = ablBonus;

		return dmgmods;
	}

	public int critChance()
	{
		int sum = _baseCritChance + _effectModCritChance + _abilityModCritChance;

		sum = Helper.getStatMod(knowDiff(), sum);

		return sum;
	}

	public int stunChance()
	{

		int sum = _baseStunChance + _effectModStunChance + _abilityModStunChance;

		sum = Helper.getStatMod(luckDiff(), sum);

		return sum;
	}

	public boolean isEffectActive(int effectId)
	{
		for (int a = 0; a < _activeEffects.size(); a++)
		{
			if (_activeEffects.get(a).id() == effectId)
				return true;
		}
		return false;
	}

	public int execDiff()
	{
		return strength() - _baseStrength;
	}

	public int reacDiff()
	{
		return reaction() - _baseReaction;
	}

	public int knowDiff()
	{
		return knowledge() - _baseKnowledge;
	}

	public int mageDiff()
	{
		return magelore() - _baseMagelore;
	}

	public int luckDiff()
	{
		return luck() - _baseLuck;
	}

	public int strength()
	{
		return _baseStrength + _effectModStrength + _abilityModStrength + _counterModStrength + _counterModStrength2;
	}

	public int reaction()
	{
		return _baseReaction + _effectModReaction + _abilityModReaction + _counterModReaction + _counterModReaction2;
	}

	public int knowledge()
	{
		return _baseKnowledge + _effectModKnowledge + _abilityModKnowledge + _counterModKnowledge
			+ _counterModKnowledge2;
	}

	public int magelore()
	{
		return _baseMagelore + _effectModMagelore + _abilityModMagelore + _counterModMagelore + _counterModMagelore2;
	}

	public int luck()
	{
		return _baseLuck + _effectModLuck + _abilityModLuck + _counterModLuck + _counterModLuck2;
	}

	public void setCounterAbsorbDamage(int turns, int min, int max)
	{
		_counterAbsorbDamageTurns = turns;
		_counterAbsorbDamageMin = min;
		_counterAbsorbDamageMin = min;
	}

	public void setcounterModifyHPStatBased(int turns, int stat, int mult)
	{
		_counterModifyHPStatBasedTurns = turns;
		_counterModifyHPStatBasedId = stat;
		_counterModifyHPStatBasedMult = mult;

		if (stat == 0)
			_counterModStrength += (mult * strength());

		if (stat == 1)
			_counterModReaction += (mult * reaction());

		if (stat == 2)
			_counterModKnowledge += (mult * knowledge());

		if (stat == 3)
			_counterModMagelore += (mult * magelore());

		if (stat == 4)
			_counterModLuck += (mult * luck());

	}

	public void setCounterModifyHPStatTimesWpnDmgBased(int turns, int amt)
	{
		_counterDotModifyHPStatBasedTurns = turns;
		_counterDotModifyHPStatBasedAmount = amt;
	}

	public void setCounterModifyWeaponDamage(int turns, int amt, int amtbonus, int effectId)
	{
		_counterModifyWeaponDamageTurns = turns;
		_counterModifyWeaponDamageAmount = amt;
		_counterModifyWeaponDamageAmountBonus = amtbonus;
		_counterModifyWeaponDamageBonusEffectActiveId = effectId;
	}

	public void setCounterDotModifyHPWeaponDamageBased(int turns, int minamt, int maxamt, int minamtbonus,
		int maxamtbonus, int effectId, String name, int sourceDamageAmount)
	{
		_counterDotModifyHPWeaponDamageBasedName = name;
		_counterDotModifyHPWeaponDamageBasedTurns = turns;
		_counterDotModifyHPWeaponDamageBasedMinAmount = minamt;
		_counterDotModifyHPWeaponDamageBasedMinAmountBonus = minamtbonus;
		_counterDotModifyHPWeaponDamageBasedMaxAmount = maxamt;
		_counterDotModifyHPWeaponDamageBasedMaxAmountBonus = maxamtbonus;
		_counterDotModifyHPWeaponDamageBasedEffectActiveId = effectId;
		_counterDotModifyHPWeaponBasedSourceAmount = sourceDamageAmount;
	}

	public void setCounterDotModifyHPMaxHPBasedHP(int turns, int percentAmount)
	{
		_counterDotModifyHPMaxHPBasedHPTurns = turns;
		_counterDotModifyHPMaxHPBasedHPAmount = Helper.getPercentFromInt(percentAmount, maxHP());
	}

	public int getCounterAbsorbDamageAmount(int hitAmount)
	{
		int percentAbsAmt = 0;
		int absAmt = 0;
		if (_counterAbsorbDamageTurns > 0)
		{
			percentAbsAmt = Helper.getRandomIntFromRange(_counterAbsorbDamageMax + 1, _counterAbsorbDamageMin);
			absAmt = Helper.getPercentFromInt(percentAbsAmt, hitAmount);
		}
		return absAmt;
	}

	public void setCounterModifyStatForStatTurns(int turns, int statIdToMod, int amt)
	{
		_counterModifyStatForStatTurnsTurns = turns;
		_counterModifyStatForStatTurnsStatId = statIdToMod;
		_counterModifyStatForStatTurnsAmount = 0;
	}
	
	public void setCounterModifyHPStatIsPercentWithMult(int turns,int amt)
	{
		_counterModifyHPStatIsPercentWithMultTurns = turns;
		_counterModifyHPStatIsPercentWithMultAmount = amt;
	}

	public void setCounterReflectDamage(int turns, int percentAmount)
	{
		_counterReflectDamageTurns += turns;
		_counterReflectPercentAmount = percentAmount;
	}

	public int getReflectedDamage(int dmgAmt)
	{
		int refAmt = 0;
		if (_counterReflectDamageTurns > 0)
		{
			refAmt = Helper.getPercentFromInt(_counterReflectPercentAmount, dmgAmt);
		}
		return refAmt;
	}

	public int updateAP(int amt)
	{
		int orgAP = _currentAP;

		_currentAP += amt;

		if (_currentAP < 0)
			_currentAP = 0;

		if (_currentAP > maxAP())
			_currentAP = maxAP();

		return _currentAP - orgAP;
	}

	public int updateHP(int amt)
	{
		Log.d("combat", name() + "'s HP was modified by " + amt);

		int orgHP = _currentHP;

		_currentHP += amt;

		if (_currentHP < 0)
			_currentHP = 0;

		if (_currentHP > maxHP())
			_currentHP = maxHP();

		return _currentHP - orgHP;
	}

	public boolean dead()
	{
		return _dead;
	}

	public void setDead(boolean d)
	{

		_dead = d;
	}

	public int currentHP()
	{
		return _currentHP;
	}

	public abstract int maxHP();

	public abstract int maxAP();

	public int currentAP()
	{
		return _currentAP;
	}

	public void setCurrentHP(int a)
	{
		this._currentHP = a;
	}

	public void setCurrentAP(int a)
	{
		this._currentAP = a;
	}

	public boolean casting()
	{
		return _casting;
	}

	public void stopCasting()
	{
		_casting = false;
		_currentCastingAbility = null;
		_castingTurnsLeft = 0;
	}

	public void startCasting(ItemRune castingAbility, Actor source)
	{

		_casting = true;
		_currentCastingAbility = castingAbility;

		_castingTurnsLeft =
			Helper.getRandomIntFromRange(_currentCastingAbility.castingTurnsMax(),
				_currentCastingAbility.castingTurnsMin());

		if (castingAbility.castingSpecialID() > 0)
		{
			int[] ints = Helper.getSpecialCastingInt(castingAbility.castingSpecialID(), source, null);
			if (ints[1] > 0)
			{
				_castingTurnsLeft = ints[1];
			}
		}

		addActiveEffect(Helper.getEffectIdByName("Casting"));
	}

	public int activeCastingAbility()
	{
		return _activeCastingAbility;
	}

	public void setActiveCastingAbility(int _activeCastingAbility)
	{
		this._activeCastingAbility = _activeCastingAbility;
	}

	public void updateMissesInARow()
	{
		_missesInARow++;
	}

	public void resetMissesInARow()
	{
		_missesInARow = 0;
	}

	public int noAbilityInARow()
	{
		return _noAbilityInARow;
	}

	public void resetNoAbilityInARow()
	{
		_noAbilityInARow = 0;
	}

	public void updateNoAbilityInARow()
	{
		_noAbilityInARow++;
	}

	public int actorType()
	{
		return _actorType;
	}

	public int baseHitChance()
	{
		return _baseHitChance;
	}

	public int baseDodgeChance()
	{
		return _baseDodgeChance;
	}

	public void set_baseDodgeChance(int _baseDodgeChance)
	{
		this._baseDodgeChance = _baseDodgeChance;
	}

	public int baseInitiative()
	{
		return _baseInitiative;
	}

	public void set_baseInitiative(int _baseInitiative)
	{
		this._baseInitiative = _baseInitiative;
	}

}
