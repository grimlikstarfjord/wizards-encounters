package com.alderangaming.wizardsencounters;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.view.animation.Animation;

public class Monster extends Actor implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1661998091146571896L;

	private int _monsterID = -1;

	private int _goldMin, _goldMax, _experience = 0;
	private int _baseMinDamage, _baseMaxDamage = 0;

	private int _modifyAttackPowerAmount = 0;
	private int _modifyAttackPowerTurns = 0;

	private int _doAbilitiesInOrderFlag = 0;
	private int _preferToHitPercent = 0;

	private String _imageName;
	private int _imageResource = -1;
	private int _tempImageResource = -1;
	private int _tempImageTurns = 0;

	private String _health = "";

	private int _noAbilityInARow = 0;

	private boolean _isAnimating = false;
	private int _onAnimationIndex = -1;
	private Animation animation = null;
	private boolean _animationRepeats = false;
	private ArrayList<Integer> monsterAnimations = new ArrayList<Integer>();

	public Monster(int mID, Context context)
	{
		_monsterID = mID;
		setActorType(1);
		loadMonsterDefaults(context);
	}
	
	public boolean animationRepeats()
	{
		return _animationRepeats;
	}

	public int getNextAnimation()
	{
		_onAnimationIndex++;

		if (_onAnimationIndex >= monsterAnimations.size())
			_onAnimationIndex = 0;

		if (monsterAnimations.size() < 1)
			return -1;

		return monsterAnimations.get(_onAnimationIndex);
	}

	private void loadMonsterDefaults(Context context)
	{
		super.setName(DefinitionMonsters.MONSTER_NAMES[_monsterID]);
		super.setCurrentAP(DefinitionMonsters.MONSTER_BASE_AP[_monsterID]);
		super.setCurrentHP(DefinitionMonsters.MONSTER_BASE_HP[_monsterID]);
		_imageName = DefinitionMonsters.MONSTER_IMAGE[_monsterID];
		super.setBaseStrength(DefinitionMonsters.MONSTER_BASE_STRENGTH[_monsterID]);
		super.setBaseReaction(DefinitionMonsters.MONSTER_BASE_REACTION[_monsterID]);
		super.setBaseKnowledge(DefinitionMonsters.MONSTER_BASE_KNOWLEDGE[_monsterID]);
		super.setBaseMagelore(DefinitionMonsters.MONSTER_BASE_MAGELORE[_monsterID]);
		super.setBaseLuck(DefinitionMonsters.MONSTER_BASE_LUCK[_monsterID]);
		_baseMinDamage = DefinitionMonsters.MONSTER_MIN_DAMAGE[_monsterID];
		_baseMaxDamage = DefinitionMonsters.MONSTER_MAX_DAMAGE[_monsterID];
		super.setBaseHitChance(DefinitionMonsters.MONSTER_HIT_CHANCE[_monsterID]);
		super.setRank(DefinitionMonsters.MONSTER_ROUND[_monsterID]);
		_goldMin = DefinitionMonsters.MONSTER_GOLD_DROP[_monsterID][0];
		_goldMax = DefinitionMonsters.MONSTER_GOLD_DROP[_monsterID][1];
		for (int a = 0; a < DefinitionMonsters.MONSTER_MOVE_TYPES[_monsterID].length; a++)
		{
			monsterAnimations.add(DefinitionMonsters.MONSTER_MOVE_TYPES[_monsterID][a]);
			if (DefinitionMonsters.MONSTER_MOVE_TYPES[_monsterID][a] == Animator.MONSTER_FLOATS)
			{
				_animationRepeats = true;
			}
		}
		addAbilities(_monsterID, context);
	}

	public int maxAP()
	{
		return DefinitionMonsters.MONSTER_BASE_AP[_monsterID];
	}

	public int maxHP()
	{
		return DefinitionMonsters.MONSTER_BASE_HP[_monsterID];
	}

	private void addAbilities(int id, Context context)
	{
		for (int a = 0; a < DefinitionMonsters.MONSTER_ABILITIES[id].length; a++)
		{
			super.addActiveAbility(DefinitionMonsters.MONSTER_ABILITIES[id][a]);
		}
		_doAbilitiesInOrderFlag = DefinitionMonsters.MONSTER_ABILITIES_DO_IN_ORDER_FLAG[id];
		_preferToHitPercent = DefinitionMonsters.MONSTER_PREFER_TO_HIT[id];
	}

	public int getGoldDrop()
	{
		int sum = Helper.getRandomIntFromRange(_goldMax, _goldMin);

		sum = Helper.getStatMod(luckDiff(), sum);
		
		sum = (int) Math.round((double)sum * DefinitionGlobal.MONSTER_DROP_GOLD_MULTIPLE);

		return sum;
	}

	public int preferToHitPercent()
	{
		return _preferToHitPercent;
	}

	public int doAbilitiesInOrder()
	{
		return _doAbilitiesInOrderFlag;
	}

	public void setTempImage(int r, int t)
	{
		_tempImageResource = r;
		_tempImageTurns = t;
	}

	public int getUseAbilityId(int playerHpPercent, int playerAp)
	{
		int useAbilityId = -1;

		// see if monster needs to use regen ability
		/*
		 * special flag: 1 = heal 2 = do not use if hp < 20% 3 = do not use if
		 * player hp %< monster hp% 4 = do not use if hp < 40% 5 = use when
		 * player AP > monster AP *
		 */
		int specialAbilityId = -1;
		int randomAbilityId = -1;
		boolean tryToUse = false;

		for (int a = 0; a < getActiveAbilities().length; a++)
		{
			if ((Integer) DefinitionRunes.runeData[getActiveAbilityByIndex(a)][DefinitionRunes.SPECIAL_FLAG][0] > 0)
			{
				specialAbilityId = getActiveAbilityByIndex(a);
				break;
			}
		}

		// if we have a special ability, is it appropriate to use?
		if (specialAbilityId > 0)
		{
			// check for regain hp
			if (specialAbilityId == 1)
			{
				if (Math.floor((double) currentHP() / (double) maxHP()) <= DefinitionGlobal.MONSTER_USE_HP_ABILITY_PERCENT_BELOW)
				{
					tryToUse = true;
				}
			}
			// do not use if hp < 20%
			else if (specialAbilityId == 2)
			{
				if (Math.floor((double) currentHP() / (double) maxHP()) > DefinitionGlobal.MONSTER_USE_HP_ABILITY_PERCENT_BELOW)
				{
					tryToUse = true;
				}
			}
			// do not use if player hp % < our hp %
			else if (specialAbilityId == 3)
			{
				if (playerHpPercent >= (int) Math.floor((double) currentHP() / (double) maxHP()))
				{
					tryToUse = true;
				}
			}
			// do not use if hp < 40$=%
			else if (specialAbilityId == 4)
			{
				if (Math.floor((double) currentHP() / (double) maxHP()) > 2 * DefinitionGlobal.MONSTER_USE_HP_ABILITY_PERCENT_BELOW)
				{
					tryToUse = true;
				}
			}
			// use if playerAp > MonsterAp
			else if (specialAbilityId == 5)
			{
				if (playerAp > currentAP())
				{
					tryToUse = true;
				}
			}

			if (tryToUse == true
				&& (Integer) DefinitionRunes.runeData[specialAbilityId][DefinitionRunes.RUNE_AP_COST][0] <= currentAP())
			{
				updateAP(-(Integer) DefinitionRunes.runeData[specialAbilityId][DefinitionRunes.RUNE_AP_COST][0]);
				resetNoAbilityInARow();
				return specialAbilityId;
			}
		}

		// force monster to attack with weapon if they have an active weapon
		// modifier
		if (shouldUseWeaponAttack())
			return -1;

		// try to select a random regular one in Global Defined tries
		// check that we can afford it
		// we should never use a special ability when selected in this way

		// does monster want to use an offensive ability?
		// first see if you satisfy prefer to hit percent
		if (Helper.randomInt(100) > preferToHitPercent())
		{
			return -1;
		}

		for (int a = 0; a < DefinitionGlobal.NUM_TIMES_MONSTER_TRIES_RANDOM_ABILITY; a++)
		{
			randomAbilityId = Helper.getRandomIntFromIntArray(getActiveAbilities());

			if (randomAbilityId != specialAbilityId
				&& (Integer) DefinitionRunes.runeData[randomAbilityId][DefinitionRunes.RUNE_AP_COST][0] <= currentAP())
			{

				// check that monster reuse ability they may have already
				// recently used
				if (!canUseAbility(randomAbilityId))
					continue;

				updateAP(-(Integer) DefinitionRunes.runeData[randomAbilityId][DefinitionRunes.RUNE_AP_COST][0]);
				resetNoAbilityInARow();
				return randomAbilityId;
			}
		}

		return useAbilityId;
	}

	public int tempImageResource()
	{
		return _tempImageResource;
	}

	public int tempImageTurns()
	{
		return _tempImageTurns;
	}

	public void setImageResource(int r)
	{
		_imageResource = r;
	}

	public int imageResource()
	{
		return _imageResource;
	}

	public int monsterID()
	{
		return _monsterID;
	}

	public int experience()
	{
		return _experience;
	}

	public String imageName()
	{
		return _imageName;
	}

	public String health()
	{
		updateHealth();
		return _health;
	}

	public void updateHealth()
	{

		double monsterHealth = 100 * ((double) currentHP() / (double) maxHP());

		if (monsterHealth > 75)
		{
			_health = "Healthy";
		}
		else if (monsterHealth > 50)
		{
			_health = "Wounded";
		}
		else if (monsterHealth > 25)
		{
			_health = "Bloodied";
		}
		else
		{
			_health = "Dying";
		}
	}

	public void resetNoAbilityInARow()
	{
		_noAbilityInARow = 0;
	}

	public void updateNoAbilityInARow()
	{
		_noAbilityInARow++;
	}

	public int noAbilityInARow()
	{
		return _noAbilityInARow;
	}

	public int[] tryHit()
	{
		// HIT: dmg amt, crit flag, stun flag
		// MISS: -1, roll, hit chance
		int[] returnData = new int[3];
		returnData[0] = 0;
		returnData[1] = 0;
		returnData[2] = 0;

		int roll = Helper.randomInt(101);
		if (roll >= hitChance())
		{
			returnData[0] = -1;
			returnData[1] = roll;
			returnData[2] = hitChance();
			return returnData;
		}

		int dmgAmt = 0;
		// check if crit
		if (100 - Helper.randomInt(101) < critChance())
		{
			dmgAmt = 2 * _baseMaxDamage;
			returnData[0] = dmgAmt;
			returnData[1] = 1;
			// check if stun and crit
			if (100 - Helper.randomInt(101) < stunChance())
			{
				returnData[2] = 1;
			}

			return returnData;
		}

		dmgAmt = getDamage();

		if (dmgAmt < 0)
			dmgAmt = 0;

		// check if stun
		if (100 - Helper.randomInt(101) < stunChance())
		{
			returnData[2] = 1;
		}

		returnData[0] = dmgAmt;
		return returnData;
	}

	public void modifyAttackPowerByPercentTurns(int percent, int turns)
	{
		if (percent < 0)
		{
			int amt = Helper.getPercentFromInt(-percent, getDamage());
			_modifyAttackPowerAmount = amt * -1;
		}

		_modifyAttackPowerTurns = turns;
	}

	public int[] getHitRange()
	{
		int[] dmg = new int[2];
		dmg[0] = _baseMinDamage;
		dmg[1] = _baseMaxDamage;
		return dmg;
	}

	public void resetFlags()
	{
		super.resetFlags();
		_modifyAttackPowerTurns = 0;
	}

	public ArrayList<ReturnData> advanceTurn()
	{
		_modifyAttackPowerTurns--;
		if (_modifyAttackPowerTurns < 0)
		{
			_modifyAttackPowerTurns = 0;
			_modifyAttackPowerAmount = 0;
		}

		_tempImageTurns--;
		if (_tempImageTurns < 0)
		{
			_tempImageTurns = 0;
			_tempImageResource = 0;
		}

		return super.advanceTurn();
	}

	public int getMaxDamage()
	{
		int dmgAmt = _baseMaxDamage;
		int modAmt = modHitDamage(dmgAmt)[0];

		int sum = dmgAmt + modAmt + getDamageCounterBonus(dmgAmt + modAmt) + _modifyAttackPowerAmount;

		sum = Helper.getStatMod(execDiff(), sum);

		return sum;
	}

	public int getDamage()
	{
		int dmgAmt = Helper.getRandomIntFromRange(_baseMaxDamage + 1, _baseMinDamage);
		int modAmt = modHitDamage(dmgAmt)[0];

		int sum = dmgAmt + modAmt + getDamageCounterBonus(dmgAmt + modAmt) + _modifyAttackPowerAmount;

		sum = Helper.getStatMod(execDiff(), sum);

		return sum;
	}

	public int hitChance()
	{
		int sum = super.baseHitChance();

		sum = Helper.getStatMod(knowDiff(), sum);

		return sum;
	}

	public int initiative()
	{
		int sum = super.baseInitiative();

		sum = Helper.getStatMod(reacDiff(), sum);

		return sum;
	}

	public boolean isAnimating()
	{
		return _isAnimating;
	}

	public void stopAnimating()
	{
		try
		{
			animation.cancel();
			_isAnimating = false;
		}
		catch (Exception e)
		{

		}
	}

	public void setAnimating(Animation a, boolean b)
	{
		if (b)
		{
			animation = a;
			//animation.reset();
			_isAnimating = true;
		}
		else
		{
			try
			{
				
				_isAnimating = false;
			}
			catch (Exception e)
			{

			}
		}

		_isAnimating = b;
	}
}
