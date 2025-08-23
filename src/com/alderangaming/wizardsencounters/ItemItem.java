package com.alderangaming.wizardsencounters;

import java.io.Serializable;

import android.content.Context;

public class ItemItem extends StoreItem implements Serializable
{
	private static final long serialVersionUID = 4860911328701311614L;
			
	/* def amounts */
	private int _appliedOverNumTurns = -1;
	private int _regainHPPercentOfMax = -1;	
	private int _modifyCritPercent = -1;
	private int _modifyDamageTakenDecreasePercent = -1;
	private int _modifyDodgePercent = -1;
	private int _applyMonsterAnimationID = -1;
	private int _modifyDecreaseMonsterRank = -1;
	private int _killMonsterPercentChance = -1;
	private int _modifyMonsterHPLessPercentOfMax = -1;
	private int _warpToRound = -1;	
	private int _increaseChargeCostMultiple = 0;
	
	/* flags */
	private int _skipsCurrentFightFlag = 0;
	private int _rechargesAtEndOfRoundFlag = 0;
	private int _appliedOverNumTurnsFlag = 0;
	private int _applyStunFlag = 0;
	private int _removesPlayerEffectsFlag = 0;
	private int _abilitiesAreFreeFlag = 0;
	private int _monsterCannotAttackFlag = 0;
	private int _restartFightFlag = 0;
	

	public ItemItem(int t, int i, Context c)
	{
		super(t, i);
		
		makeSelf(i);
		setName(DefinitionItems.ITEM_NAME[i]);
		setDescription(DefinitionItems.ITEM_DESCRIPTION[i]);
		setImageName(DefinitionItems.ITEM_IMAGE_NAME[i], DefinitionItems.ITEM_IMAGE_NAME[i], c);
		setMinLevel(DefinitionItems.ITEM_MIN_LEVEL_TO_USE[i]);
		setValue(DefinitionItems.ITEM_BASE_VALUE[i]);
		setShowsInStore(DefinitionItems.ITEM_SHOWS_IN_STORE[i]);		
		
		setMaxCharges(DefinitionItems.ITEM_MAX_CHARGE[i]);
		
	}
	
	private void makeSelf(int i)
	{
		_increaseChargeCostMultiple = DefinitionItems.ITEM_INCREASE_CHARGE_COST_MULTIPLE[i];
		_appliedOverNumTurns = DefinitionItems.ITEM_APPLIED_OVER_NUM_TURNS[i][1];
		_regainHPPercentOfMax = DefinitionItems.ITEM_REGAIN_HEALTH_PERCENT_OF_MAX[i];	
		_modifyCritPercent = DefinitionItems.ITEM_MODIFY_CRIT_PERCENT[i];
		_modifyDamageTakenDecreasePercent = DefinitionItems.ITEM_MODIFY_DAMAGE_TAKEN_DECREASE_PERCENT[i];
		_modifyDodgePercent = DefinitionItems.ITEM_MODIFY_DODGE_PERCENT[i];
		_applyMonsterAnimationID = DefinitionItems.ITEM_APPLY_MONSTER_ANIMATION_ID[i];
		_modifyDecreaseMonsterRank = DefinitionItems.ITEM_MODIFY_MONSTER_RANK[i];
		_killMonsterPercentChance = DefinitionItems.ITEM_KILL_MONSTER_PERCENT_CHANCE[i];
		_modifyMonsterHPLessPercentOfMax = DefinitionItems.ITEM_MODIFY_MONSTER_HP_LESS_PERCENT_MAX[i];
		_warpToRound = DefinitionItems.ITEM_WARP_TO_ROUND[i];	
		
		
		
		/* flags */
		_skipsCurrentFightFlag = DefinitionItems.ITEM_SKIPS_FIGHT_FLAG[i];
		_rechargesAtEndOfRoundFlag = DefinitionItems.ITEM_RECHARGES_AT_END_OF_ROUND_FLAG[i];
		_appliedOverNumTurnsFlag = DefinitionItems.ITEM_APPLIED_OVER_NUM_TURNS[i][0];
		_applyStunFlag = DefinitionItems.ITEM_APPLY_STUN_FLAG[i];
		_removesPlayerEffectsFlag = DefinitionItems.ITEM_REMOVE_PLAYER_EFFECTS_FLAG[i];
		_abilitiesAreFreeFlag = DefinitionItems.ITEM_ABILITIES_ARE_FREE_FLAG[i];
		_monsterCannotAttackFlag = DefinitionItems.ITEM_MONSTER_CANNOT_ATTACK_FLAG[i];
		_restartFightFlag = DefinitionItems.ITEM_RESTART_FIGHT_FLAG[i];
	}
	
	
	public int increaseChargeCostMultiple(){ return _increaseChargeCostMultiple;}
	public int appliedOverNumTurns(){ return _appliedOverNumTurns ;}
	public int regainHPPercentOfMax(){ return _regainHPPercentOfMax ;}	
	public int modifyCritPercent(){ return _modifyCritPercent ;}
	public int modifyDamageTakenDecreasePercent(){ return _modifyDamageTakenDecreasePercent ;}
	public int modifyDodgePercent(){ return _modifyDodgePercent ;}
	public int applyMonsterAnimationID(){ return _applyMonsterAnimationID ;}
	public int modifyDecreaseMonsterRank(){ return _modifyDecreaseMonsterRank ;}
	public int killMonsterPercentChance(){ return _killMonsterPercentChance ;}
	public int modifyMonsterHPLessPercentOfMax(){ return _modifyMonsterHPLessPercentOfMax ;}
	public int warpToRound(){ return _warpToRound ;}	
	
	/* flags */
	public int skipsCurrentFightFlag(){ return _skipsCurrentFightFlag ;}
	public int rechargesAtEndOfRoundFlag(){ return _rechargesAtEndOfRoundFlag ;}
	public int appliedOverNumTurnsFlag(){ return _appliedOverNumTurnsFlag ;}
	public int applyStunFlag(){ return _applyStunFlag ;}
	public int removesPlayerEffectsFlag(){ return _removesPlayerEffectsFlag ;}
	public int abilitiesAreFreeFlag(){ return _abilitiesAreFreeFlag ;}
	public int monsterCannotAttackFlag(){ return _monsterCannotAttackFlag ;}
	public int restartFightFlag(){ return _restartFightFlag ;}

}
