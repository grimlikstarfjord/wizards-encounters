package com.alderangaming.wizardsencounters;

import java.io.Serializable;

import android.content.Context;

public class ItemItem extends StoreItem implements Serializable
{
	private static final long serialVersionUID = 4860911328701311614L;

	private int _applyMonsterAnimationID = -1;
	private int _killMonsterPercentChance = -1;
	private int _warpToRound = -1;
	private int _increaseChargeCostMultiple = 0;
	private int _skipsCurrentFightFlag = 0;
	private int _rechargesAtEndOfRoundFlag = 0;
	private int _removesPlayerEffectsFlag = 0;
	private int _abilitiesAreFreeFlag = 0;
	private int _restartFightFlag = 0;

	private int _appliedOverNumTurnsAbsolute = 0;
	private int _appliedOverNumTurnsLevelFlag = 0;
	private int _modifyHPPercentOfMaxSelf = 0;
	private int _modifyHPPercentOfMaxMonster = 0;
	private int _modifyAPPercentOfMaxSelf = 0;
	private int _modifyAPPercentOfMaxMonster = 0;
	private int _modifyCritPercentSelf = 0;
	private int _modifyCritPercentMonster = 0;
	private int _modifyDamageTakenPercentSelf = 0;
	private int _modifyDamageTakenPercentMonster = 0;
	private int _modifyDodgePercentSelf = 0;
	private int _modifyDodgePercentMonster = 0;
	private int _stunForTurnsSelf = 0;
	private int _stunForTurnsMonster = 0;
	private int _changeMonsterImageFlag = 0;
	private String _changeMonsterImage = "";
	private int _modifyAttackPowerPercentSelf = 0;
	private int _modifyAttackPowerPercentMonster = 0;
	private int _applyHPPercentGainAfterTurnsAmount = 0;
	private int _applyHPPercentGainAfterTurnsTurns = 0;

	public ItemItem(int t, int i, Context c)
	{
		super(t, i);

		makeSelf(i);
		setName((String) DefinitionItems.itemdata[i][DefinitionItems.ITEM_NAME][0]);
		setDescription((String) DefinitionItems.itemdata[i][DefinitionItems.ITEM_DESCRIPTION][0]);
		setImageName((String) DefinitionItems.itemdata[i][DefinitionItems.ITEM_IMAGE_NAME][0],
			(String) DefinitionItems.itemdata[i][DefinitionItems.ITEM_IMAGE_NAME][0], c);
		setMinLevel((Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_MIN_LEVEL_TO_USE][0]);
		setValue((Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_BASE_VALUE][0]);
		setShowsInStore((Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_SHOWS_IN_STORE][0]);

		setMaxCharges((Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_MAX_CHARGE][0]);

	}

	private void makeSelf(int i)
	{
		_increaseChargeCostMultiple =
			(Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_INCREASE_CHARGE_COST_MULTIPLE][0];

		_rechargesAtEndOfRoundFlag =
			(Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_RECHARGES_AT_END_OF_ROUND_FLAG][0];

		_appliedOverNumTurnsAbsolute =
			(Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_APPLIED_OVER_NUM_TURNS][0];
		_appliedOverNumTurnsLevelFlag =
			(Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_APPLIED_OVER_NUM_TURNS][1];

		_modifyHPPercentOfMaxSelf =
			(Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_MODIFY_HP_PERCENT_OF_MAX][0];
		_modifyHPPercentOfMaxMonster =
			(Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_MODIFY_HP_PERCENT_OF_MAX][1];

		_modifyAPPercentOfMaxSelf =
			(Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_MODIFY_AP_PERCENT_OF_MAX][0];
		_modifyAPPercentOfMaxMonster =
			(Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_MODIFY_AP_PERCENT_OF_MAX][1];

		_skipsCurrentFightFlag = (Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_SKIPS_FIGHT_FLAG][0];

		_modifyCritPercentSelf = (Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_MODIFY_CRIT_PERCENT][0];
		_modifyCritPercentMonster = (Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_MODIFY_CRIT_PERCENT][1];

		_modifyDamageTakenPercentSelf =
			(Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_MODIFY_DAMAGE_TAKEN_PERCENT][0];
		_modifyDamageTakenPercentMonster =
			(Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_MODIFY_DAMAGE_TAKEN_PERCENT][1];

		_modifyDodgePercentSelf = (Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_MODIFY_DODGE_PERCENT][0];
		_modifyDodgePercentMonster =
			(Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_MODIFY_DODGE_PERCENT][1];

		_stunForTurnsSelf = (Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_STUN_FOR_TURNS][0];
		_stunForTurnsMonster = (Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_STUN_FOR_TURNS][1];

		_removesPlayerEffectsFlag =
			(Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_REMOVE_PLAYER_EFFECTS_FLAG][0];

		_abilitiesAreFreeFlag = (Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_ABILITIES_ARE_FREE_FLAG][0];

		_applyMonsterAnimationID =
			(Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_APPLY_MONSTER_ANIMATION_ID][0];

		_changeMonsterImageFlag =
			(Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_TEMP_CHANGE_MONSTER_IMAGE][0];
		_changeMonsterImage = (String) DefinitionItems.itemdata[i][DefinitionItems.ITEM_TEMP_CHANGE_MONSTER_IMAGE][1];
		_modifyAttackPowerPercentSelf =
			(Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_MODIFY_ATTACK_POWER][0];
		_modifyAttackPowerPercentMonster =
			(Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_MODIFY_ATTACK_POWER][1];
		_killMonsterPercentChance =
			(Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_KILL_MONSTER_PERCENT_CHANCE][0];
		_restartFightFlag = (Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_RESTART_FIGHT_FLAG][0];
		_warpToRound = (Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_WARP_TO_ROUND][0];
		_applyHPPercentGainAfterTurnsAmount =
			(Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_APPLY_HP_GAIN_PERCENT_AFTER_NUM_TURNS][0];
		_applyHPPercentGainAfterTurnsTurns =
			(Integer) DefinitionItems.itemdata[i][DefinitionItems.ITEM_APPLY_HP_GAIN_PERCENT_AFTER_NUM_TURNS][1];

	}

	public int increaseChargeCostMultiple()
	{
		return _increaseChargeCostMultiple;
	}

	public int applyMonsterAnimationID()
	{
		return _applyMonsterAnimationID;
	}

	public int killMonsterPercentChance()
	{
		return _killMonsterPercentChance;
	}

	public int warpToRound()
	{
		return _warpToRound;
	}

	public int skipsCurrentFightFlag()
	{
		return _skipsCurrentFightFlag;
	}

	public int rechargesAtEndOfRoundFlag()
	{
		return _rechargesAtEndOfRoundFlag;
	}

	public int removesPlayerEffectsFlag()
	{
		return _removesPlayerEffectsFlag;
	}

	public int abilitiesAreFreeFlag()
	{
		return _abilitiesAreFreeFlag;
	}

	public int restartFightFlag()
	{
		return _restartFightFlag;
	}

	public int appliedOverNumTurnsAbsolute()
	{
		return _appliedOverNumTurnsAbsolute;
	}

	public int appliedOverNumTurnsLevelFlag()
	{
		return _appliedOverNumTurnsLevelFlag;
	}

	public int modifyHPPercentOfMaxSelf()
	{
		return _modifyHPPercentOfMaxSelf;
	}

	public int modifyHPPercentOfMaxMonster()
	{
		return _modifyHPPercentOfMaxMonster;
	}

	public int modifyAPPercentOfMaxSelf()
	{
		return _modifyAPPercentOfMaxSelf;
	}

	public int modifyAPPercentOfMaxMonster()
	{
		return _modifyAPPercentOfMaxMonster;
	}

	public int modifyCritPercentSelf()
	{
		return _modifyCritPercentSelf;
	}

	public int modifyCritPercentMonster()
	{
		return _modifyCritPercentMonster;
	}

	public int modifyDamageTakenPercentSelf()
	{
		return _modifyDamageTakenPercentSelf;
	}

	public int modifyDamageTakenPercentMonster()
	{
		return _modifyDamageTakenPercentMonster;
	}

	public int modifyDodgePercentSelf()
	{
		return _modifyDodgePercentSelf;
	}

	public int modifyDodgePercentMonster()
	{
		return _modifyDodgePercentMonster;
	}

	public int stunForTurnsSelf()
	{
		return _stunForTurnsSelf;
	}

	public int stunForTurnsMonster()
	{
		return _stunForTurnsMonster;
	}

	public int changeMonsterImageFlag()
	{
		return _changeMonsterImageFlag;
	}

	public String changeMonsterImage()
	{
		return _changeMonsterImage;
	};

	public int modifyAttackPowerPercentSelf()
	{
		return _modifyAttackPowerPercentSelf;
	}

	public int modifyAttackPowerPercentMonster()
	{
		return _modifyAttackPowerPercentMonster;
	}

	public int applyHPPercentGainAfterTurnsAmount()
	{
		return _applyHPPercentGainAfterTurnsAmount;
	}

	public int applyHPPercentGainAfterTurnsTurns()
	{
		return _applyHPPercentGainAfterTurnsTurns;
	}

}
