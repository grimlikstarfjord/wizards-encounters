package com.alderangaming.wizardsencounters;

import java.io.Serializable;

import android.content.Context;

public class ItemRune extends StoreItem implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7652177613758232076L;

	private int _apCost;
	private int _successChance;

	private int _dealWeaponDamageMin = 0;
	private int _dealWeaponDamageMax = 0;
	private int _dealWeaponDamageMinBonus = 0;
	private int _dealWeaponDamageMaxBonus = 0;

	private boolean _stuns = false;
	private int _stunTurns = 0;
	private int _stunActor = -1;
	private int _stunOnlyIfSourceEffectActive = -1;
	private int _stunOnlyIfTargetEffectActive = -1;

	private int _dealStatIsHpPercentDamageSourceOrTargetFlag = -1;
	private int _dealStatIsHpPercentDamageStatId = -1;

	private double _dealMultipleStatBasedDamageMult = 0;
	private int _dealMultipleStatBasedDamageStat1 = -1;
	private int _dealMultipleStatBasedDamageStat2 = -1;

	private int _castingSpecialID = -1;

	private int[] _appliesEffectSource = new int[0];
	private int[] _appliesEffectTarget = new int[0];

	private int _sapAPBasedOnStatID = -1;
	private int _sapAPActorStatUsedFlag = -1;
	private int _sapAPAndTransferToSourceFlag = 0;

	private int _comboActiveEffectRequirementID = -1;
	private int _comboActiveEffectActor = -1;

	private int _revealsTargetInfo = 0;

	private int _modifyAPPercentAmount = 0;
	private int _modifyAPPercentAmountBonus = 0;
	private int _modifyAPActor = -1;

	private int _modifyHPPercentAmount = 0;
	private int _modifyHPPercentAmountBonus;
	private int _modifyHPActor;

	private boolean _casts = false;
	private int _castingTurnsMin = 0;
	private int _castingTurnsMax = 0;
	private int _castingDamageOnTurnFlag = 0;
	private int _castingDamageOnEndFlag = 0;
	private int _castingApplyEffectOnEndFlag = 0;
	private int _castingApplyCounterOnEndFlag = 0;
	private int _castingModifyHPPerTurnFlag = 0;
	private int _castingModifyAPPerTurnFlag = 0;

	private int _counterAbsorbDamageTurns = 0;
	private int _counterAbsorbDamageMin = 0;
	private int _counterAbsorbDamageMax = 0;
	private int _counterAbsorbDamageMinBonus = 0;
	private int _counterAbsorbDamageMaxBonus = 0;
	private int _counterAbsorbDamageStatIsPercentStatId = -1;
	private int _counterAbsorbDamageStatIsPercentStatMult = -1;

	private int _counterModifyStatTurns = 0;
	private int _counterModifyStatSourceOrTargetFlag = -1;
	private int _counterModifyStatId = -1;
	private int _counterModifyStatMult = 0;

	private int _counterModifyHPMaxHPBasedTurns = 0;
	private int _counterModifyHPMaxHPBasedAmount = 0;
	private int _counterModifyHPMaxHPBasedSourceOrTargetFlag = 0;

	private int _counterModifyHPStatBasedTimesWpnDmgTurns = 0;
	private int _counterModifyHPStatBasedTimesWpnDmgStatId = 0;
	private double _counterModifyHPStatBasedTimesWpnDmgMult = 0;

	private int _counterModifyWeaponDamageTurns = 0;
	private int _counterModifyWeaponDamageSourceOrTargetFlag = -1;
	private int _counterModifyWeaponDamageAmount = 0;
	private int _counterModifyWeapondamageAmountBonus = 0;

	private int _counterDotModifyHPWeaponDamageBasedTurns = 0;
	private int _counterDotModifyHPWeaponDamageBasedMinAmount = 0;
	private int _counterDotModifyHPWeaponDamageBasedMinAmountBonus = 0;
	private int _counterDotModifyHPWeaponDamageBasedMaxAmount = 0;
	private int _counterDotModifyHPWeaponDamageBasedMaxAmountBonus = 0;

	private int _counterReflectDamageTurns = 0;
	private int _counterReflectDamagePercentAmt = 0;
	private int _counterReflectDamagePercentAmtBonus = 0;

	private int _counterDotModifyStatPerTurnSourceOrTargetStatForTurns = 0;
	private int _counterDotModifyStatPerTurnStatIdForTurns = 0;
	private int _counterDotModifyStatPerTurnSourceOrTargetStat = 0;
	private int _counterDotModifyStatPerTurnStatId = 0;
	private int _counterDotModifyStatPerTurnAmount = 0;

	private int _counterDotModifyHPStatIsPercentWithMultTurns = 0;
	private int _counterDotModifyHPStatIsPercentWithMultSourceOrTargetFlag = -1;
	private int _counterDotModifyHPStatIsPercentWithMultStatId = -1;
	private int _counterDotModifyHPStatIsPercentWithMultMult = 0;

	public ItemRune(int t, int i, Context c)
	{
		super(t, i);

		makeSelf(i);

		setOnlyForClasses(Helper.getClassesByRuneId(i));
		setName((String) DefinitionRunes.runeData[i][DefinitionRunes.RUNE_NAMES][0]);
		setDescription((String) DefinitionRunes.runeData[i][DefinitionRunes.RUNE_DESCRIPTION][0]);

		setAvailForANewPlayer((Integer) DefinitionRunes.runeData[i][DefinitionRunes.RUNE_AVAIL_FOR_NEW_PLAYER][0]);
		setValue((Integer) DefinitionRunes.runeData[i][DefinitionRunes.RUNE_VALUE][0]);
		setCost(value());
		setMinLevel((Integer) DefinitionRunes.runeData[i][DefinitionRunes.RUNE_MIN_LEVEL][0]);
		setImageName((String) DefinitionRunes.runeData[i][DefinitionRunes.RUNE_ANIMATION_IMAGE][0],
			(String) DefinitionRunes.runeData[i][DefinitionRunes.RUNE_ANIMATION_IMAGE][0], c);
	}

	private void makeSelf(int id)
	{
		//System.out.println("id:"+id);
		
		_apCost = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_AP_COST][0];
		_successChance = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_SUCCESS_CHANCE][0];

		_dealWeaponDamageMin = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_DEALS_WEAPON_DAMAGE][0];
		_dealWeaponDamageMax = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_DEALS_WEAPON_DAMAGE][1];
		_dealWeaponDamageMinBonus = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_DEALS_WEAPON_DAMAGE][2];
		_dealWeaponDamageMaxBonus = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_DEALS_WEAPON_DAMAGE][3];

		_stunTurns = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_STUNS][0];
		
		
		if (_stunTurns > 0)
			_stuns = true;

		_stunActor = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_STUNS][1];
		_stunOnlyIfSourceEffectActive = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_STUNS][2];
		_stunOnlyIfTargetEffectActive = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_STUNS][3];
		
		
		_dealStatIsHpPercentDamageSourceOrTargetFlag =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_DEALS_STAT_IS_HP_PERCENT_DAMAGE][0];
		_dealStatIsHpPercentDamageStatId =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_DEALS_STAT_IS_HP_PERCENT_DAMAGE][1];

		_dealMultipleStatBasedDamageMult =
			Double.parseDouble(DefinitionRunes.runeData[id][DefinitionRunes.RUNE_DEALS_MULTIPLE_STAT_DAMAGE][0].toString());
		_dealMultipleStatBasedDamageStat1 =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_DEALS_MULTIPLE_STAT_DAMAGE][1];
		_dealMultipleStatBasedDamageStat2 =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_DEALS_MULTIPLE_STAT_DAMAGE][2];
		
		_appliesEffectSource =
			new int[DefinitionRunes.runeData[id][DefinitionRunes.RUNE_APPLIES_SOURCE_EFFECTID].length];
		for (int a = 0; a < _appliesEffectSource.length; a++)
		{
			_appliesEffectSource[a] =
				(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_APPLIES_SOURCE_EFFECTID][a];
		}

		_appliesEffectTarget =
			new int[DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_APPLIES_TARGET_EFFECTID].length];
		for (int a = 0; a < _appliesEffectTarget.length; a++)
		{
			_appliesEffectTarget[a] =
				(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_APPLIES_TARGET_EFFECTID][a];
		}

		_sapAPBasedOnStatID = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_SAP_AP_BASED_ON_STAT][0];
		_sapAPActorStatUsedFlag = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_SAP_AP_BASED_ON_STAT][1];
		_sapAPAndTransferToSourceFlag =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_SAP_AP_BASED_ON_STAT][2];

		_comboActiveEffectRequirementID =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COMBO_BONUS_ACTIVE_EFFECT_REQUIREMENT][0];
		_comboActiveEffectActor =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COMBO_BONUS_ACTIVE_EFFECT_REQUIREMENT][1];

		_revealsTargetInfo = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_REVEALS_TARGET_INFO][0];

		_modifyAPActor = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_MODIFY_AP][0];
		_modifyAPPercentAmount = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_MODIFY_AP][1];
		_modifyAPPercentAmountBonus = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_MODIFY_AP][2];

		_modifyHPActor = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_MODIFY_HP][0];
		_modifyHPPercentAmount = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_MODIFY_HP][1];
		_modifyHPPercentAmountBonus = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_MODIFY_HP][2];

		_casts = false;
		_castingTurnsMin = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_CASTS][0];
		_castingTurnsMax = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_CASTS][1];
		_castingDamageOnTurnFlag = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_CASTS][2];
		_castingDamageOnEndFlag = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_CASTS][3];
		_castingApplyEffectOnEndFlag = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_CASTS][4];
		_castingApplyCounterOnEndFlag = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_CASTS][5];
		_castingSpecialID = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_CASTS][6];
		_castingModifyHPPerTurnFlag = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_CASTS][7];
		_castingModifyAPPerTurnFlag = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_CASTS][8];

		_counterAbsorbDamageTurns =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_ABSORBS_DAMAGE][0];
		_counterAbsorbDamageMin =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_ABSORBS_DAMAGE][1];
		_counterAbsorbDamageMax =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_ABSORBS_DAMAGE][2];
		_counterAbsorbDamageMinBonus =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_ABSORBS_DAMAGE][3];
		_counterAbsorbDamageMaxBonus =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_ABSORBS_DAMAGE][4];
		_counterAbsorbDamageStatIsPercentStatId =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_ABSORBS_DAMAGE][5];
		_counterAbsorbDamageStatIsPercentStatMult =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_ABSORBS_DAMAGE][6];

		_counterModifyStatTurns = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_MODIFY_STAT][0];
		_counterModifyStatSourceOrTargetFlag =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_MODIFY_STAT][1];
		_counterModifyStatId = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_MODIFY_STAT][2];
		_counterModifyStatMult = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_MODIFY_STAT][3];

		_counterModifyHPMaxHPBasedTurns =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_MODIFY_CURRENT_HP][0];
		_counterModifyHPMaxHPBasedSourceOrTargetFlag =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_MODIFY_CURRENT_HP][1];
		_counterModifyHPMaxHPBasedAmount =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_MODIFY_CURRENT_HP][2];

		_counterModifyHPStatBasedTimesWpnDmgTurns =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_REGEN_HP_STAT_BASED][0];
		_counterModifyHPStatBasedTimesWpnDmgStatId =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_REGEN_HP_STAT_BASED][1];
		_counterModifyHPStatBasedTimesWpnDmgMult =
			Double.parseDouble(DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_REGEN_HP_STAT_BASED][2].toString());

		_counterModifyWeaponDamageTurns =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_MODIFY_WEAPON_DAMAGE][0];
		_counterModifyWeaponDamageSourceOrTargetFlag =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_MODIFY_WEAPON_DAMAGE][1];
		_counterModifyWeaponDamageAmount =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_MODIFY_WEAPON_DAMAGE][2];
		_counterModifyWeapondamageAmountBonus =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_MODIFY_WEAPON_DAMAGE][3];

		_counterDotModifyHPWeaponDamageBasedTurns =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_DEAL_DOT_DAMAGE][0];
		_counterDotModifyHPWeaponDamageBasedMinAmount =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_DEAL_DOT_DAMAGE][1];
		_counterDotModifyHPWeaponDamageBasedMaxAmount =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_DEAL_DOT_DAMAGE][2];
		_counterDotModifyHPWeaponDamageBasedMinAmountBonus =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_DEAL_DOT_DAMAGE][3];
		_counterDotModifyHPWeaponDamageBasedMaxAmountBonus =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_DEAL_DOT_DAMAGE][4];

		_counterReflectDamageTurns =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_REFLECT_DAMAGE][0];
		_counterReflectDamagePercentAmt =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_REFLECT_DAMAGE][1];
		_counterReflectDamagePercentAmtBonus =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_REFLECT_DAMAGE][2];

		_counterDotModifyStatPerTurnSourceOrTargetStatForTurns =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_DOT_FOR_STAT_TURNS_MOD_STAT][0];
		_counterDotModifyStatPerTurnStatIdForTurns =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_DOT_FOR_STAT_TURNS_MOD_STAT][1];
		_counterDotModifyStatPerTurnSourceOrTargetStat =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_DOT_FOR_STAT_TURNS_MOD_STAT][2];
		_counterDotModifyStatPerTurnStatId =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_DOT_FOR_STAT_TURNS_MOD_STAT][3];
		_counterDotModifyStatPerTurnAmount =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_DOT_FOR_STAT_TURNS_MOD_STAT][4];

		_counterDotModifyHPStatIsPercentWithMultSourceOrTargetFlag =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_DOT_MOD_HP_STAT_MULT_BASED][0];
		_counterDotModifyHPStatIsPercentWithMultTurns =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_DOT_MOD_HP_STAT_MULT_BASED][1];
		_counterDotModifyHPStatIsPercentWithMultStatId =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_DOT_MOD_HP_STAT_MULT_BASED][2];
		_counterDotModifyHPStatIsPercentWithMultMult =
			(Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_COUNTER_DOT_MOD_HP_STAT_MULT_BASED][3];
	}

	public int apCost()
	{
		return _apCost;
	}

	public int successChance()
	{
		return _successChance;
	}

	public int dealWeaponDamageMin()
	{
		return _dealWeaponDamageMin;
	}

	public int dealWeaponDamageMax()
	{
		return _dealWeaponDamageMax;
	}

	public int dealWeaponDamageMinBonus()
	{
		return _dealWeaponDamageMinBonus;
	}

	public int dealWeaponDamageMaxBonus()
	{
		return _dealWeaponDamageMaxBonus;
	}

	private boolean _stuns()
	{
		return _stuns;
	}

	public int stunTurns()
	{
		return _stunTurns;
	}

	public int stunActor()
	{
		return _stunActor;
	}

	public int stunOnlyIfSourceEffectActive()
	{
		return _stunOnlyIfSourceEffectActive;
	}

	public int stunOnlyIfTargetEffectActive()
	{
		return _stunOnlyIfTargetEffectActive;
	}

	public int dealStatIsHpPercentDamageSourceOrTargetFlag()
	{
		return _dealStatIsHpPercentDamageSourceOrTargetFlag;
	}

	public int dealStatIsHpPercentDamageStatId()
	{
		return _dealStatIsHpPercentDamageStatId;
	}
	
	public int castingSpecialID()
	{
		return _castingSpecialID;
	}

	public int[] appliesEffectSource()
	{
		return _appliesEffectSource;
	}

	public int[] appliesEffectTarget()
	{
		return _appliesEffectTarget;
	}

	public int sapAPBasedOnStatID()
	{
		return _sapAPBasedOnStatID;
	}

	public int sapAPActorStatUsedFlag()
	{
		return _sapAPActorStatUsedFlag;
	}

	public int sapAPAndTransferToSourceFlag()
	{
		return _sapAPAndTransferToSourceFlag;
	}

	public int comboActiveEffectRequirementID()
	{
		return _comboActiveEffectRequirementID;
	}

	public int comboActiveEffectActor()
	{
		return _comboActiveEffectActor;
	}

	public int revealsTargetInfo()
	{
		return _revealsTargetInfo;
	}

	public int modifyAPPercentAmount()
	{
		return _modifyAPPercentAmount;
	}

	public int modifyAPPercentAmountBonus()
	{
		return _modifyAPPercentAmountBonus;
	}

	public int modifyAPActor()
	{
		return _modifyAPActor;
	}

	public int modifyHPPercentAmount()
	{
		return _modifyHPPercentAmount;
	}

	public int modifyHPPercentAmountBonus()
	{
		return _modifyHPPercentAmountBonus;
	};

	public int modifyHPActor()
	{
		return _modifyHPActor;
	};

	private boolean casts()
	{
		return _casts;
	}

	public int castingTurnsMin()
	{
		return _castingTurnsMin;
	}

	public int castingTurnsMax()
	{
		return _castingTurnsMax;
	}

	public int castingDamageOnTurnFlag()
	{
		return _castingDamageOnTurnFlag;
	}

	public int castingDamageOnEndFlag()
	{
		return _castingDamageOnEndFlag;
	}

	public int castingApplyEffectOnEndFlag()
	{
		return _castingApplyEffectOnEndFlag;
	}

	public int castingApplyCounterOnEndFlag()
	{
		return _castingApplyCounterOnEndFlag;
	}

	public int castingModifyHPPerTurnFlag()
	{
		return _castingModifyHPPerTurnFlag;
	}

	public int castingModifyAPPerTurnFlag()
	{
		return _castingModifyAPPerTurnFlag;
	}

	public double dealMultipleStatBasedDamageMult()
	{
		return _dealMultipleStatBasedDamageMult;
	}

	public int dealMultipleStatBasedDamageStat1()
	{
		return _dealMultipleStatBasedDamageStat1;
	}

	public int dealMultipleStatBasedDamageStat2()
	{
		return _dealMultipleStatBasedDamageStat2;
	}

	public int counterAbsorbDamageTurns()
	{
		return _counterAbsorbDamageTurns;
	}

	public int counterAbsorbDamageMin()
	{
		return _counterAbsorbDamageMin;
	}

	public int counterAbsorbDamageMax()
	{
		return _counterAbsorbDamageMax;
	}

	public int counterAbsorbDamageMinBonus()
	{
		return _counterAbsorbDamageMinBonus;
	}

	public int counterAbsorbDamageMaxBonus()
	{
		return _counterAbsorbDamageMaxBonus;
	}

	public int counterAbsorbDamageStatIsPercentStatId()
	{
		return _counterAbsorbDamageStatIsPercentStatId;
	}

	public int counterAbsorbDamageStatIsPercentStatMult()
	{
		return _counterAbsorbDamageStatIsPercentStatMult;
	}

	public int counterModifyStatTurns()
	{
		return _counterModifyStatTurns;
	}

	public int counterModifyStatSourceOrTargetFlag()
	{
		return _counterModifyStatSourceOrTargetFlag;
	}

	public int counterModifyStatId()
	{
		return _counterModifyStatId;
	}

	public int counterModifyStatMult()
	{
		return _counterModifyStatMult;
	}

	public int counterModifyHPMaxHPBasedTurns()
	{
		return _counterModifyHPMaxHPBasedTurns;
	}

	public int counterModifyHPMaxHPBasedAmount()
	{
		return _counterModifyHPMaxHPBasedAmount;
	}

	public int counterModifyHPMaxHPBasedSourceOrTargetFlag()
	{
		return _counterModifyHPMaxHPBasedSourceOrTargetFlag;
	}

	public int counterModifyHPStatBasedTimesWpnDmgTurns()
	{
		return _counterModifyHPStatBasedTimesWpnDmgTurns;
	}

	public int counterModifyHPStatBasedTimesWpnDmgStatId()
	{
		return _counterModifyHPStatBasedTimesWpnDmgStatId;
	}

	public double counterModifyHPStatBasedTimesWpnDmgMult()
	{
		return _counterModifyHPStatBasedTimesWpnDmgMult;
	}

	public int counterModifyWeaponDamageTurns()
	{
		return _counterModifyWeaponDamageTurns;
	}

	public int counterModifyWeaponDamageSourceOrTargetFlag()
	{
		return _counterModifyWeaponDamageSourceOrTargetFlag;
	}

	public int counterModifyWeaponDamageAmount()
	{
		return _counterModifyWeaponDamageAmount;
	}

	public int counterModifyWeapondamageAmountBonus()
	{
		return _counterModifyWeapondamageAmountBonus;
	}

	public int counterDotModifyHPWeaponDamageBasedTurns()
	{
		return _counterDotModifyHPWeaponDamageBasedTurns;
	}

	public int counterDotModifyHPWeaponDamageBasedMinAmount()
	{
		return _counterDotModifyHPWeaponDamageBasedMinAmount;
	}

	public int counterDotModifyHPWeaponDamageBasedMinAmountBonus()
	{
		return _counterDotModifyHPWeaponDamageBasedMinAmountBonus;
	}

	public int counterDotModifyHPWeaponDamageBasedMaxAmount()
	{
		return _counterDotModifyHPWeaponDamageBasedMaxAmount;
	}

	public int counterDotModifyHPWeaponDamageBasedMaxAmountBonus()
	{
		return _counterDotModifyHPWeaponDamageBasedMaxAmountBonus;
	}

	public int counterReflectDamageTurns()
	{
		return _counterReflectDamageTurns;
	}

	public int counterReflectDamagePercentAmt()
	{
		return _counterReflectDamagePercentAmt;
	}

	public int counterReflectDamagePercentAmtBonus()
	{
		return _counterReflectDamagePercentAmtBonus;
	}

	public int counterDotModifyStatPerTurnSourceOrTargetStatForTurns()
	{
		return _counterDotModifyStatPerTurnSourceOrTargetStatForTurns;
	}

	public int counterDotModifyStatPerTurnStatIdForTurns()
	{
		return _counterDotModifyStatPerTurnStatIdForTurns;
	}

	public int counterDotModifyStatPerTurnSourceOrTargetStat()
	{
		return _counterDotModifyStatPerTurnSourceOrTargetStat;
	}

	public int counterDotModifyStatPerTurnStatId()
	{
		return _counterDotModifyStatPerTurnStatId;
	}

	public int counterDotModifyStatPerTurnAmount()
	{
		return _counterDotModifyStatPerTurnAmount;
	}

	public int counterDotModifyHPStatIsPercentWithMultTurns()
	{
		return _counterDotModifyHPStatIsPercentWithMultTurns;
	}

	public int counterDotModifyHPStatIsPercentWithMultSourceOrTargetFlag()
	{
		return _counterDotModifyHPStatIsPercentWithMultSourceOrTargetFlag;
	}

	public int counterDotModifyHPStatIsPercentWithMultStatId()
	{
		return _counterDotModifyHPStatIsPercentWithMultStatId;
	}

	public int counterDotModifyHPStatIsPercentWithMultMult()
	{
		return _counterDotModifyHPStatIsPercentWithMultMult;
	}
}
