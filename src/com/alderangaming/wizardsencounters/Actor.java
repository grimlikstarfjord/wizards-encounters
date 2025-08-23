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
	private ArrayList<UsedAbility> _usedAbilities = new ArrayList<UsedAbility>();

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
	private boolean _immuneToBadEffects = false;
	private boolean _casting = false;
	private int _castingTurnsLeft = 0;

	private ItemRune _currentCastingAbility = null;

	public void resetFlags()
	{
		_stunned = false;
		_dead = false;
		_cannotUseAbilities = false;
		_immuneToBadEffects = false;
		_casting = false;
		clearCounters();
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
	private int _counterAbsorbDamageCharges = 0;

	// private int _counterModifyHPStatBasedTurns = 0;
	// private int _counterModifyHPStatBasedId = 0;
	// private int _counterModifyHPStatBasedMult = 0;

	private int _counterModifyStatTurns = 0;
	private int _counterModifyStatId = 0;
	private double _counterModifyStatPercent = 0;

	private int _counterDotModifyHPMaxHPBasedHPTurns = 0;
	private int _counterDotModifyHPMaxHPBasedHPAmount = 0;
	private String _counterDotModifyHPMaxHPBasedHPName = "";

	private int _counterDotModifyHPStatBasedTurns = 0;
	private int _counterDotModifyHPStatBasedAmount = 0;
	private String _counterDotModifyHPStatBasedName = "";

	protected int _counterModifyWeaponDamageTurns = 0;
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

	private int _counterDotModifyHPStatTimesWpnDmgBasedTurns = 0;
	private int _counterDotModifyHPStatTimesWpnDmgBasedAmount = 0;
	private String _counterDotModifyHPStatTimesWpnDmgBasedName = "";

	private int _counterReflectDamageTurns = 0;
	private int _counterReflectPercentAmount = 0;

	private int _counterModifyStatForStatTurnsTurns = 0;
	private int _counterModifyStatForStatTurnsStatId = 0;
	private int _counterModifyStatForStatTurnsAmount = 0;

	private int _counterModifyHPStatIsPercentWithMultTurns = 0;
	private int _counterModifyHPStatIsPercentWithMultAmount = 0;
	private String _counterModifyHPStatIsPercentWithMultName = "";

	/* combat instance counters */
	private int _noAbilityInARow = 0;

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

		Log.d("modifier", name() + ", counterModifyWeaponDamageTurns: " + _counterModifyWeaponDamageTurns + ", %="
			+ _counterModifyWeaponDamageAmount + ", baseDmg=" + baseDmg + ", +amt = " + dmg);

		return dmg;
	}

	public abstract int initiative();

	public abstract int hitChance();

	public abstract int getDamage();

	public abstract int getMaxDamage();

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
		if (!_activeAbilities.contains(rune))
			_activeAbilities.add(rune);
	}

	public boolean checkPlayerHasAbility(int id)
	{
		return _activeAbilities.contains(id);
	}

	private void clearModifiers()
	{
		_stunned = false;
		_cannotUseAbilities = false;
		_immuneToBadEffects = false;

		_abilityAbsorbDamage = 0;
		_abilityModHitDamage = 0;
		_abilityModStrength = 0;
		_abilityModKnowledge = 0;
		_abilityModReaction = 0;
		_abilityModMagelore = 0;
		_abilityModLuck = 0;
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
		_effectModMagelore = 0;
		_effectModLuck = 0;
		_effectModDodge = 0;
	}

	private void clearCounters()
	{
		_counterModifyStatTurns = 0;
		_counterDotModifyHPMaxHPBasedHPTurns = 0;
		_counterDotModifyHPStatBasedTurns = 0;
		_counterModifyWeaponDamageTurns = 0;
		_counterDotModifyHPWeaponDamageBasedTurns = 0;
		_counterDotModifyHPStatTimesWpnDmgBasedTurns = 0;
		_counterReflectDamageTurns = 0;
		_counterModifyStatForStatTurnsTurns = 0;
		_counterModifyHPStatIsPercentWithMultTurns = 0;
		_counterModStrength = 0;
		_counterModKnowledge = 0;
		_counterModReaction = 0;
		_counterModMagelore = 0;
		_counterModLuck = 0;
		_counterModStrength2 = 0;
		_counterModKnowledge2 = 0;
		_counterModReaction2 = 0;
		_counterModMagelore2 = 0;
		_counterModLuck2 = 0;
	}
	
	public boolean shouldUseWeaponAttack()
	{
		if(_counterModifyWeaponDamageTurns > 0)
			return true;
		
		return false;
	}

	public ArrayList<ReturnData> advanceTurn()
	{
		Log.d("combat", "advancing turn for Actor " + name() + "...");

		ArrayList<ReturnData> returnData = new ArrayList<ReturnData>();

		clearModifiers();

		returnData.addAll(updateAbilityCounters());

		returnData.addAll(updateEffectCounters());

		returnData.addAll(updateCastingCounter());

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

	private ArrayList<ReturnData> updateCastingCounter()
	{
		ArrayList<ReturnData> rdArray = new ArrayList<ReturnData>();

		if (!_casting)
			return rdArray;

		if (_castingTurnsLeft <= 0)
		{
			_casting = false;
			_castingTurnsLeft = 0;
			_currentCastingAbility = null;
			return rdArray;
		}

		else
		{
			ReturnData rd = new ReturnData();
			rd.whatHappend = name() + " is casting... (" + _castingTurnsLeft + ")";
			rd.logType = DefinitionGlobal.LOG_TYPE_IS_CASTING;
			rdArray.add(rd);

			_castingTurnsLeft--;

			return rdArray;
		}
	}

	public class ReturnData
	{
		String whatHappend = "";
		int logType = -1;
	}

	private class UsedAbility
	{
		public int abilID = -1;
		public int turnsUsed = -1;
	}

	public boolean canUseAbility(int abilId)
	{
		for (int a = 0; a < _usedAbilities.size(); a++)
			if (_usedAbilities.get(a).abilID == abilId)
				return false;

		// never found it, add it
		UsedAbility u = new UsedAbility();
		u.abilID = abilId;
		u.turnsUsed = (Integer) DefinitionRunes.runeData[abilId][DefinitionRunes.RUNE_REUSE_WAIT_TURNS][0];
		_usedAbilities.add(u);

		return true;
	}

	private ArrayList<ReturnData> updateAbilityCounters()
	{
		// countdown used abilities counters
		for (int a = 0; a < _usedAbilities.size(); a++)
		{
			_usedAbilities.get(a).turnsUsed--;
			if (_usedAbilities.get(a).turnsUsed <= 0)
				_usedAbilities.remove(a);
		}

		String actionName = name();
		String actionNamePossessive = name() + "'s";
		if (_actorType == 0)
		{
			actionName = "You";
			actionNamePossessive = "Your";
		}

		ArrayList<ReturnData> returnDataArray = new ArrayList<ReturnData>();

		_counterDotModifyHPStatTimesWpnDmgBasedTurns--;
		if (_counterDotModifyHPStatTimesWpnDmgBasedTurns < 0)
		{
			_counterDotModifyHPStatTimesWpnDmgBasedTurns = 0;
			_counterDotModifyHPStatTimesWpnDmgBasedAmount = 0;
		}
		else
		{
			updateHP(_counterDotModifyHPStatTimesWpnDmgBasedAmount);

			ReturnData rd = new ReturnData();
			if (_counterDotModifyHPStatTimesWpnDmgBasedAmount > 0)
			{
				rd.whatHappend =
					actionName + " recovered " + _counterDotModifyHPStatTimesWpnDmgBasedAmount + " HP from "
						+ _counterDotModifyHPStatTimesWpnDmgBasedName + ".";

				if (_actorType == 0)
					rd.logType = DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_HP;
				else
					rd.logType = DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_HP;
			}
			else
			{
				rd.whatHappend =
					actionName + " lost " + -_counterDotModifyHPStatTimesWpnDmgBasedAmount + " HP from "
						+ _counterDotModifyHPStatTimesWpnDmgBasedName + ".";

				if (_actorType == 0)
					rd.logType = DefinitionGlobal.LOG_TYPE_PLAYER_LOSES_HP;
				else
					rd.logType = DefinitionGlobal.LOG_TYPE_MONSTER_LOSES_HP;
			}
			returnDataArray.add(rd);
		}
		
		_counterReflectDamageTurns--;
		if (_counterReflectDamageTurns < 0)
		{
			_counterReflectPercentAmount = 0;
			_counterReflectDamageTurns = 0;
		}

		_counterModifyHPStatIsPercentWithMultTurns--;
		if (_counterModifyHPStatIsPercentWithMultTurns < 0)
		{
			_counterModifyHPStatIsPercentWithMultTurns = 0;
			_counterModifyHPStatIsPercentWithMultAmount = 0;
		}
		else
		{
			updateHP(_counterModifyHPStatIsPercentWithMultAmount);

			ReturnData rd = new ReturnData();
			if (_counterModifyHPStatIsPercentWithMultAmount > 0)
			{
				rd.whatHappend =
					actionName + " recovered " + _counterModifyHPStatIsPercentWithMultAmount + " HP from "
						+ _counterModifyHPStatIsPercentWithMultName + ".";

				if (_actorType == 0)
					rd.logType = DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_HP;
				else
					rd.logType = DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_HP;
			}
			else
			{
				rd.whatHappend =
					actionName + " lost " + -_counterModifyHPStatIsPercentWithMultAmount + " HP from "
						+ _counterModifyHPStatIsPercentWithMultName + ".";

				if (_actorType == 0)
					rd.logType = DefinitionGlobal.LOG_TYPE_PLAYER_LOSES_HP;
				else
					rd.logType = DefinitionGlobal.LOG_TYPE_MONSTER_LOSES_HP;
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

				if (_counterModifyStatForStatTurnsAmount != 0)
				{
					ReturnData rd = new ReturnData();
					rd.whatHappend =
						actionNamePossessive + " Execution was modified by " + _counterModifyStatForStatTurnsAmount
							+ ".";
					rd.logType = DefinitionGlobal.LOG_TYPE_DEFAULT;

					returnDataArray.add(rd);

				}
			}
			if (_counterModifyStatForStatTurnsStatId == 1 || _counterModifyStatForStatTurnsStatId == 99)
			{
				_counterModReaction2 += _counterModifyStatForStatTurnsAmount;

				if (_counterModifyStatForStatTurnsAmount != 0)
				{
					ReturnData rd = new ReturnData();
					rd.whatHappend =
						actionNamePossessive + " Reaction was modified by " + _counterModifyStatForStatTurnsAmount
							+ ".";
					rd.logType = DefinitionGlobal.LOG_TYPE_DEFAULT;
					returnDataArray.add(rd);
				}
			}
			if (_counterModifyStatForStatTurnsStatId == 2 || _counterModifyStatForStatTurnsStatId == 99)
			{
				_counterModKnowledge2 += _counterModifyStatForStatTurnsAmount;

				if (_counterModifyStatForStatTurnsAmount != 0)
				{
					ReturnData rd = new ReturnData();
					rd.whatHappend =
						actionNamePossessive + " Knowledge was modified by " + _counterModifyStatForStatTurnsAmount
							+ ".";
					rd.logType = DefinitionGlobal.LOG_TYPE_DEFAULT;
					returnDataArray.add(rd);
				}
			}
			if (_counterModifyStatForStatTurnsStatId == 3 || _counterModifyStatForStatTurnsStatId == 99)
			{
				_counterModMagelore2 += _counterModifyStatForStatTurnsAmount;

				if (_counterModifyStatForStatTurnsAmount != 0)
				{
					ReturnData rd = new ReturnData();
					rd.whatHappend =
						actionNamePossessive + " Magelore was modified by " + _counterModifyStatForStatTurnsAmount
							+ ".";
					rd.logType = DefinitionGlobal.LOG_TYPE_DEFAULT;
					returnDataArray.add(rd);
				}
			}
			if (_counterModifyStatForStatTurnsStatId == 4 || _counterModifyStatForStatTurnsStatId == 99)
			{
				_counterModLuck2 += _counterModifyStatForStatTurnsAmount;

				if (_counterModifyStatForStatTurnsAmount != 0)
				{
					ReturnData rd = new ReturnData();
					rd.whatHappend =
						actionNamePossessive + " Luck was modified by " + _counterModifyStatForStatTurnsAmount + ".";
					rd.logType = DefinitionGlobal.LOG_TYPE_DEFAULT;
					returnDataArray.add(rd);
				}
			}
		}

		_counterModifyStatTurns--;
		if (_counterModifyStatTurns < 0)
			_counterModifyStatTurns = 0;
		else
		{
			if (_counterModifyStatId == 0)
			{
				_counterModStrength += Math.round((double) exec() * _counterModifyStatPercent);

				if (_counterModStrength != 0)
				{
					ReturnData rd = new ReturnData();
					rd.whatHappend =
						actionNamePossessive + " Strength was modified by "
							+ (Math.round((double) exec() * _counterModifyStatPercent)) + ".";
					rd.logType = DefinitionGlobal.LOG_TYPE_DEFAULT;
					returnDataArray.add(rd);
				}
			}

			if (_counterModifyStatId == 1)
			{
				_counterModReaction += Math.round((double) reaction() * _counterModifyStatPercent);

				if (_counterModReaction != 0)
				{
					ReturnData rd = new ReturnData();
					rd.whatHappend =
						actionNamePossessive + " Reaction was modified by "
							+ (Math.round((double) reaction() * _counterModifyStatPercent)) + ".";
					rd.logType = DefinitionGlobal.LOG_TYPE_DEFAULT;
					returnDataArray.add(rd);
				}
			}

			if (_counterModifyStatId == 2)
			{
				_counterModKnowledge += Math.round((double) knowledge() * _counterModifyStatPercent);

				if (_counterModKnowledge != 0)
				{
					ReturnData rd = new ReturnData();
					rd.whatHappend =
						actionNamePossessive + " Knowledge was modified by "
							+ (Math.round((double) knowledge() * _counterModifyStatPercent)) + ".";
					rd.logType = DefinitionGlobal.LOG_TYPE_DEFAULT;
					returnDataArray.add(rd);
				}
			}

			if (_counterModifyStatId == 3)
			{
				_counterModMagelore += Math.round((double) magelore() * _counterModifyStatPercent);

				if (_counterModMagelore != 0)
				{
					ReturnData rd = new ReturnData();
					rd.whatHappend =
						actionNamePossessive + " Magelore was modified by "
							+ (Math.round((double) magelore() * _counterModifyStatPercent)) + ".";
					rd.logType = DefinitionGlobal.LOG_TYPE_DEFAULT;
					returnDataArray.add(rd);
				}
			}

			if (_counterModifyStatId == 4)
			{
				_counterModLuck += Math.round((double) luck() * _counterModifyStatPercent);

				if (_counterModLuck != 0)
				{
					ReturnData rd = new ReturnData();
					rd.whatHappend =
						actionNamePossessive + " Luck was modified by "
							+ (Math.round((double) luck() * _counterModifyStatPercent)) + ".";
					rd.logType = DefinitionGlobal.LOG_TYPE_DEFAULT;
					returnDataArray.add(rd);
				}
			}

		}

		_counterDotModifyHPMaxHPBasedHPTurns--;
		if (_counterDotModifyHPMaxHPBasedHPTurns < 0)
		{
			_counterDotModifyHPMaxHPBasedHPTurns = 0;
		}
		else
		{
			ReturnData rd = new ReturnData();

			if (_counterDotModifyHPMaxHPBasedHPAmount < 0)
			{

				if (_actorType == 0)
					rd.logType = DefinitionGlobal.LOG_TYPE_PLAYER_LOSES_HP;
				else
					rd.logType = DefinitionGlobal.LOG_TYPE_MONSTER_LOSES_HP;

				rd.whatHappend =
					actionName + " lost " + -_counterDotModifyHPMaxHPBasedHPAmount + " HP from "
						+ _counterDotModifyHPMaxHPBasedHPName + ".";
				returnDataArray.add(rd);
			}
			else if (_counterDotModifyHPMaxHPBasedHPAmount > 0)
			{
				if (_actorType == 0)
					rd.logType = DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_HP;
				else
					rd.logType = DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_HP;

				rd.whatHappend =
					actionName + " gained " + _counterDotModifyHPMaxHPBasedHPAmount + " HP from "
						+ _counterDotModifyHPMaxHPBasedHPName + ".";
				returnDataArray.add(rd);
			}

			updateHP(_counterDotModifyHPMaxHPBasedHPAmount);
		}

		_counterDotModifyHPStatBasedTurns--;
		if (_counterDotModifyHPStatBasedTurns < 0)
		{
			_counterDotModifyHPStatBasedTurns = 0;
		}
		else
		{
			ReturnData rd = new ReturnData();

			if (_counterDotModifyHPStatBasedAmount < 0)
			{
				if (_actorType == 0)
					rd.logType = DefinitionGlobal.LOG_TYPE_PLAYER_LOSES_HP;
				else
					rd.logType = DefinitionGlobal.LOG_TYPE_MONSTER_LOSES_HP;

				rd.whatHappend =
					actionName + " lost " + -_counterDotModifyHPStatBasedAmount + " HP from "
						+ _counterDotModifyHPMaxHPBasedHPName + ".";
			}
			else
			{
				if (_actorType == 0)
					rd.logType = DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_HP;
				else
					rd.logType = DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_HP;

				rd.whatHappend =
					actionName + " gained " + _counterDotModifyHPStatBasedAmount + " HP from "
						+ _counterDotModifyHPStatBasedName + ".";
			}

			returnDataArray.add(rd);

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
			{
				if (_actorType == 0)
					rd.logType = DefinitionGlobal.LOG_TYPE_PLAYER_LOSES_HP;
				else
					rd.logType = DefinitionGlobal.LOG_TYPE_MONSTER_LOSES_HP;

				rd.whatHappend =
					actionName + " took " + (-amt) + " damage from " + _counterDotModifyHPWeaponDamageBasedName + ".";
			}
			else if (amt > 0)
			{
				if (_actorType == 0)
					rd.logType = DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_HP;
				else
					rd.logType = DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_HP;

				rd.whatHappend =
					actionName + " gained " + amt + "HP from " + _counterDotModifyHPWeaponDamageBasedName + ".";

			}
			else
				rd.whatHappend =
					_counterDotModifyHPWeaponDamageBasedName + " didn't work right. mult=" + mult + ", ctrsrcAmt="
						+ _counterDotModifyHPWeaponBasedSourceAmount + ", finalAmt=" + amt;

			returnDataArray.add(rd);
		}

		return returnDataArray;

	}

	private ArrayList<ReturnData> updateEffectCounters()
	{
		ArrayList<ReturnData> rdArray = new ArrayList<ReturnData>();
		String targetName = name();

		if (_actorType == 0)
			targetName = "You";

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

			if (_activeEffects.get(a).makesImmuneToBadEffects())
			{
				_immuneToBadEffects = true;
			}

			if (_activeEffects.get(a).modHPPerTurn() != 0)
			{
				int amt = Helper.getPercentFromInt(_activeEffects.get(a).modHPPerTurn(), maxHP());
				if (amt == 0)
					if (_activeEffects.get(a).modHPPerTurn() < 0)
						amt = -1;
					else
						amt = 1;

				int result = updateHP(amt);
				if (result < 0)
					result = -result;

				if (result != 0)
				{
					ReturnData rd = new ReturnData();

					String text = "gained";
					if (amt < 0)
					{
						text = "lost";
						if (_actorType == 0)
							rd.logType = DefinitionGlobal.LOG_TYPE_PLAYER_LOSES_HP;
						else
							rd.logType = DefinitionGlobal.LOG_TYPE_MONSTER_LOSES_HP;
					}
					else
					{
						if (_actorType == 0)
							rd.logType = DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_HP;
						else
							rd.logType = DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_HP;
					}

					rd.whatHappend =
						targetName + " " + text + " " + result + " HP from being " + _activeEffects.get(a).name() + ".";

					rdArray.add(rd);
				}
			}

			if (_activeEffects.get(a).modAPPerTurn() != 0)
			{
				int amt = Helper.getPercentFromInt(_activeEffects.get(a).modAPPerTurn(), maxAP());
				if (amt == 0)
				{
					if (_activeEffects.get(a).modAPPerTurn() < 0)
						amt = -1;
					else
						amt = 1;
				}

				String text = " gained ";

				int result = updateAP(amt);
				if (result < 0)
					result = -result;

				if (result != 0)
				{
					ReturnData rd = new ReturnData();

					if (amt < 0)
					{
						text = " lost ";
						if (_actorType == 0)
							rd.logType = DefinitionGlobal.LOG_TYPE_PLAYER_LOSES_AP;
						else
							rd.logType = DefinitionGlobal.LOG_TYPE_MONSTER_LOSES_AP;
					}
					else
					{
						if (_actorType == 0)
							rd.logType = DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_AP;
						else
							rd.logType = DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_AP;
					}

					rd.whatHappend =
						targetName + text + result + " AP from being " + _activeEffects.get(a).name() + ".";
					rdArray.add(rd);
				}
			}

			_effectModHitChance += _activeEffects.get(a).modHitChance();
			_effectModHitDamage += _activeEffects.get(a).modHitDamage();
			_effectModCritChance += _activeEffects.get(a).modCritChance();
			_effectModDodge += _activeEffects.get(a).modDodge();

			_effectModStrength += _activeEffects.get(a).modStrength();
			_effectModReaction += _activeEffects.get(a).modReaction();
			_effectModKnowledge += _activeEffects.get(a).modKnowledge();
			_effectModMagelore += _activeEffects.get(a).modMagelore();
			_effectModLuck += _activeEffects.get(a).modLuck();

		}

		return rdArray;
	}

	public ArrayList<CombatEffect> getActiveEffects()
	{
		return _activeEffects;
	}

	public CombatEffect getActiveEffectByIndex(int ind)
	{
		return _activeEffects.get(ind);
	}

	public boolean hasActiveEffect(int effectId)
	{
		for (int a = 0; a < _activeEffects.size(); a++)
		{
			if (_activeEffects.get(a).id() == effectId)
				return true;
		}
		return false;
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

		}
		else
		{
			CombatEffect e = new CombatEffect(Helper.getEffectIdByName("Stunned"));
			_activeEffects.add(e);
			
			//add 1 extra for initial stun
			e.addTurn();
			
			for (int a = 0; a < turns; a++)
				e.addTurn();

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

	public boolean immuneToBadEffects()
	{
		return _immuneToBadEffects;
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
		return exec() - _baseStrength;
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

	public int exec()
	{

		int effMod = Helper.getPercentFromInt(_effectModStrength, _baseStrength);
		int ablMod = Helper.getPercentFromInt(_abilityModStrength, _baseStrength);
		int ctr1Mod = Helper.getPercentFromInt(_counterModStrength, _baseStrength);
		int ctr2Mod = _counterModStrength2;

		return _baseStrength + effMod + ablMod + ctr1Mod + ctr2Mod;
	}

	public int reaction()
	{
		int effMod = Helper.getPercentFromInt(_effectModReaction, _baseReaction);
		int ablMod = Helper.getPercentFromInt(_abilityModReaction, _baseReaction);
		int ctr1Mod = Helper.getPercentFromInt(_counterModReaction, _baseReaction);
		int ctr2Mod = _counterModReaction2;

		return _baseReaction + effMod + ablMod + ctr1Mod + ctr2Mod;
	}

	public int knowledge()
	{

		int effMod = Helper.getPercentFromInt(_effectModKnowledge, _baseKnowledge);
		int ablMod = Helper.getPercentFromInt(_abilityModKnowledge, _baseKnowledge);
		int ctr1Mod = Helper.getPercentFromInt(_counterModKnowledge, _baseKnowledge);
		int ctr2Mod = _counterModKnowledge2;

		return _baseKnowledge + effMod + ablMod + ctr1Mod + ctr2Mod;
	}

	public int magelore()
	{

		int effMod = Helper.getPercentFromInt(_effectModMagelore, _baseMagelore);
		int ablMod = Helper.getPercentFromInt(_abilityModMagelore, _baseMagelore);
		int ctr1Mod = Helper.getPercentFromInt(_counterModMagelore, _baseMagelore);
		int ctr2Mod = _counterModMagelore2;

		return _baseMagelore + effMod + ablMod + ctr1Mod + ctr2Mod;

	}

	public int luck()
	{
		int effMod = Helper.getPercentFromInt(_effectModLuck, _baseLuck);
		int ablMod = Helper.getPercentFromInt(_abilityModLuck, _baseLuck);
		int ctr1Mod = Helper.getPercentFromInt(_counterModLuck, _baseLuck);
		int ctr2Mod = _counterModLuck2;

		return _baseLuck + effMod + ablMod + ctr1Mod + ctr2Mod;

	}

	public void setCounterAbsorbDamage(int turns, int min, int max)
	{
		_counterAbsorbDamageCharges = turns;
		_counterAbsorbDamageMin = min;
		_counterAbsorbDamageMax = max;
	}

	public void setCounterModifyStat(int turns, int stat, double percent)
	{
		_counterModifyStatTurns = turns;
		_counterModifyStatId = stat;
		_counterModifyStatPercent = percent;

		if (stat == 0)
			_counterModStrength += (percent * exec());

		if (stat == 1)
			_counterModReaction += (percent * reaction());

		if (stat == 2)
			_counterModKnowledge += (percent * knowledge());

		if (stat == 3)
			_counterModMagelore += (percent * magelore());

		if (stat == 4)
			_counterModLuck += (percent * luck());

	}

	public void setCounterModifyHPStatTimesWpnDmgBased(int turns, int amt, String sourceName)
	{
		_counterDotModifyHPStatTimesWpnDmgBasedTurns = turns;
		_counterDotModifyHPStatTimesWpnDmgBasedAmount = amt;
		_counterDotModifyHPStatTimesWpnDmgBasedName = sourceName;
	}

	public void setCounterModifyWeaponDamage(int turns, int amt, int amtbonus, int effectId)
	{
		Log.d("setabilitycounter", name() + ": " + turns + "," + amt + "," + amtbonus + "," + effectId);

		_counterModifyWeaponDamageTurns = turns + 1;
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

	public void setCounterDotModifyHPMaxHPBasedHP(int turns, int amount, String sourceName)
	{
		_counterDotModifyHPMaxHPBasedHPTurns = turns;
		_counterDotModifyHPMaxHPBasedHPAmount = amount;
		_counterDotModifyHPMaxHPBasedHPName = sourceName;
	}

	public int getCounterAbsorbDamageAmount(int hitAmount)
	{
		int percentAbsAmt = 0;
		int absAmt = 0;
		if (_counterAbsorbDamageCharges > 0)
		{
			percentAbsAmt = Helper.getRandomIntFromRange(_counterAbsorbDamageMax + 1, _counterAbsorbDamageMin);
			absAmt = Helper.getPercentFromInt(percentAbsAmt, hitAmount);
			if (absAmt < 0)
				absAmt = 0;

			_counterAbsorbDamageCharges--;
			if (_counterAbsorbDamageCharges < 0)
			{
				_counterAbsorbDamageCharges = 0;
				_counterAbsorbDamageMin = 0;
				_counterAbsorbDamageMax = 0;
			}
		}

		Log.d("absorbtest", "getCountAbsDmgAmt: turns=" + _counterAbsorbDamageCharges + ", hitAmt=" + hitAmount
			+ ", absAmt=" + absAmt);

		return absAmt;
	}

	public void setCounterModifyStatForStatTurns(int turns, int statIdToMod, int amt)
	{
		_counterModifyStatForStatTurnsTurns = turns;
		_counterModifyStatForStatTurnsStatId = statIdToMod;
		_counterModifyStatForStatTurnsAmount = amt;
	}

	public void setCounterModifyHPStatIsPercentWithMult(int turns, int amt, String name)
	{
		_counterModifyHPStatIsPercentWithMultTurns = turns;
		_counterModifyHPStatIsPercentWithMultAmount = amt;
		_counterModifyHPStatIsPercentWithMultName = name;
	}

	public void setCounterReflectDamage(int turns, int percentAmount)
	{
		_counterReflectDamageTurns = turns;
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

		if (castingAbility.castingTurnsMin() == 99)
		{
			if (castingAbility.castingTurnsMax() == 0)
				_castingTurnsLeft = source.exec();

			else if (castingAbility.castingTurnsMax() == 1)
				_castingTurnsLeft = source.reaction();

			else if (castingAbility.castingTurnsMax() == 2)
				_castingTurnsLeft = source.knowledge();

			else if (castingAbility.castingTurnsMax() == 3)
				_castingTurnsLeft = source.magelore();

			else if (castingAbility.castingTurnsMax() == 4)
				_castingTurnsLeft = source.luck();
		}
		else
		{
			_castingTurnsLeft =
				Helper.getRandomIntFromRange(_currentCastingAbility.castingTurnsMax(),
					_currentCastingAbility.castingTurnsMin());
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

	public void setActorType(int i)
	{
		_actorType = i;
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
