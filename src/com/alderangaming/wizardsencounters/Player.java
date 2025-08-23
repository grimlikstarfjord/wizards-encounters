package com.alderangaming.wizardsencounters;

import java.io.Serializable;
import java.util.ArrayList;

import android.util.Log;

public class Player extends Actor implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1130005457718893206L;

	private int _playerID = -1;
	private int _playerClass = -1;
	private int _equippedWeapon = -1;
	private int _equippedArmorSlot1 = -1;
	private int _equippedArmorSlot2 = -1;
	private int _equippedArmorSlot3 = -1;
	private int _equippedArmorSlot4 = -1;
	private int _equippedItemSlot1 = -1;
	private int _equippedItemSlot2 = -1;
	private int[] _equippedStatBonuses = new int[8];

	private int _currentRound = -1;
	private int _currentFight = -1;
	private boolean _inRound = false;
	private boolean _playerAttackWait = false;
	private boolean _blocking = false;
	private int _blockingAmount = -1;

	private int _item1ModCritAmount = 0;
	private int _item1ModCritTurns = 0;
	private int _item2ModCritAmount = 0;
	private int _item2ModCritTurns = 0;

	private int _item1ModDamageTakenAmount = 0;
	private int _item1ModDamageTakenTurns = 0;
	private int _item2ModDamageTakenAmount = 0;
	private int _item2ModDamageTakenTurns = 0;

	private int _item1ModDodgeAmount = 0;
	private int _item1ModDodgeTurns = 0;
	private int _item2ModDodgeAmount = 0;
	private int _item2ModDodgeTurns = 0;

	private int _itemApplyHPPercentGainAfterTurnsTurns = 0;
	private int _itemApplyHPPercentGainAfterTurnsAmount = 0;

	private int _freeAbilityCharge = 0;

	private ArrayList<Integer> foughtMonstersInRound = new ArrayList<Integer>();

	public Player(int i, String n, int c, boolean isNew)
	{
		_playerID = i;
		setName(n);
		setActorType(0);
		_playerClass = c;

		if (isNew)
		{
			setRank(1);
			setCurrentRound(1);
			setCurrentFight(0);
			_inRound = false;
		}
	}
	
	public void resetFlags()
	{
		super.resetFlags();
		_itemApplyHPPercentGainAfterTurnsTurns = 0;
		_item1ModDamageTakenTurns = 0;
		_item2ModDamageTakenTurns = 0;
		_item1ModCritTurns = 0;
		_item2ModCritTurns = 0;
		_freeAbilityCharge = 0;		
	}
	
	public int numEquippedItems()
	{
		int num = 0;
		if(_equippedItemSlot1 > -1)
			num++;
		
		if(_equippedItemSlot2 > -1)
			num++;
		
		return num;
	}

	public int maxHP()
	{
		int modHP = 0;
		if (_equippedArmorSlot1 != -1)
			modHP += DefinitionArmor.ARMOR_ADDS_HP[_equippedArmorSlot1];

		if (_equippedArmorSlot2 != -1)
			modHP += DefinitionArmor.ARMOR_ADDS_HP[_equippedArmorSlot2];

		if (_equippedArmorSlot3 != -1)
			modHP += DefinitionArmor.ARMOR_ADDS_HP[_equippedArmorSlot3];

		if (_equippedArmorSlot4 != -1)
			modHP += DefinitionArmor.ARMOR_ADDS_HP[_equippedArmorSlot4];

		return DefinitionClasses.CLASS_HP_TREE[_playerClass][super.rank()-1] + modHP;
	}

	public int maxAP()
	{
		int modAP = 0;
		if (_equippedArmorSlot1 != -1)
			modAP += DefinitionArmor.ARMOR_ADDS_AP[_equippedArmorSlot1];

		if (_equippedArmorSlot2 != -1)
			modAP += DefinitionArmor.ARMOR_ADDS_AP[_equippedArmorSlot2];

		if (_equippedArmorSlot3 != -1)
			modAP += DefinitionArmor.ARMOR_ADDS_AP[_equippedArmorSlot3];

		if (_equippedArmorSlot4 != -1)
			modAP += DefinitionArmor.ARMOR_ADDS_AP[_equippedArmorSlot4];

		// set test char data
		if (name().equals("ttt"))
		{
			modAP += 500;
		}

		return DefinitionClassType.CLASS_TYPE_AP_TREE_BY_LEVEL[DefinitionClasses.CLASS_TYPE[_playerClass]][super.rank()-1]
			+ modAP;
	}

	public void setRank(int r)
	{
		if(r > DefinitionClasses.CLASS_BASE_HIT_CHANCE_BY_LEVEL[0].length)
			return;
		
		super.setRank(r);
		updateStats();
	}

	public void updateStats()
	{
		super.setBaseStrength(DefinitionClasses.CLASS_BASE_STATS[_playerClass][0]);
		super.setBaseReaction(DefinitionClasses.CLASS_BASE_STATS[_playerClass][1]);
		super.setBaseKnowledge(DefinitionClasses.CLASS_BASE_STATS[_playerClass][2]);
		super.setBaseMagelore(DefinitionClasses.CLASS_BASE_STATS[_playerClass][3]);
		super.setBaseLuck(DefinitionClasses.CLASS_BASE_STATS[_playerClass][4]);

		super.set_baseDodgeChance(DefinitionClasses.CLASS_BASE_DODGE_CHANCE_BY_LEVEL[_playerClass][super.rank()-1]);
		super.setBaseHitChance(DefinitionClasses.CLASS_BASE_HIT_CHANCE_BY_LEVEL[_playerClass][super.rank()-1]);
	}

	public ArrayList<ReturnData> advanceTurn()
	{
		// decrement active item use counters

		if (_itemApplyHPPercentGainAfterTurnsTurns > 0)
		{
			_itemApplyHPPercentGainAfterTurnsTurns--;
			if (_itemApplyHPPercentGainAfterTurnsTurns == 0)
			{
				updateHP(Helper.getPercentFromInt(_itemApplyHPPercentGainAfterTurnsAmount, maxHP()));
			}
		}

		if (_item1ModCritTurns > 0)
		{
			_item1ModCritTurns--;
			if (_item1ModCritTurns < 0)
				_item1ModCritAmount = 0;
		}
		if (_item2ModCritTurns > 0)
		{
			_item2ModCritTurns--;
			if (_item2ModCritTurns < 0)
				_item2ModCritAmount = 0;
		}
		if (_item1ModDamageTakenTurns > 0)
		{
			_item1ModDamageTakenTurns--;
			if (_item1ModDamageTakenTurns < 0)
				_item1ModDamageTakenAmount = 0;
		}
		if (_item2ModDamageTakenTurns > 0)
		{
			_item2ModDamageTakenTurns--;
			if (_item2ModDamageTakenTurns < 0)
				_item2ModDamageTakenAmount = 0;
		}
		if (_item1ModDodgeTurns > 0)
		{
			_item1ModDodgeTurns--;
			if (_item1ModDodgeTurns < 0)
				_item1ModDodgeAmount = 0;
		}
		if (_item2ModDodgeTurns > 0)
		{
			_item2ModDodgeTurns--;
			if (_item2ModDodgeTurns < 0)
				_item2ModDodgeAmount = 0;
		}

		ArrayList<ReturnData> abilityOffense = super.advanceTurn();

		return abilityOffense;
	}

	public void rechargeItemsAtStartOfRound()
	{
		OwnedItems.rechargeItems(_equippedItemSlot1, _equippedItemSlot2);
	}

	public int initiative()
	{
		
		/*
		int bonus = 0;
		
		if (_equippedArmorSlot1 >= 0)
			bonus +=
				((ItemArmor) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot1))
					.modifyInitiative();

		if (_equippedArmorSlot2 >= 0)
			bonus +=
				((ItemArmor) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot2))
					.modifyInitiative();

		if (_equippedArmorSlot3 >= 0)
			bonus +=
				((ItemArmor) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot3))
					.modifyInitiative();

		if (_equippedArmorSlot4 >= 0)
			bonus +=
				((ItemArmor) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot4))
					.modifyInitiative();

		int sum = super.baseInitiative() + bonus;

		sum = Helper.getStatMod(reacDiff(), sum);

		return sum;
		
		*/
		
		int sum = super.baseInitiative() +  _equippedStatBonuses[1];

		return Helper.getStatMod(reacDiff(), sum);

	}

	public int[] blockDmg()
	{
		int[] bl = new int[4];
		int bonusMin = 0;
		int bonusMax = 0;

		if (_equippedArmorSlot1 >= 0)
		{
			bonusMin +=
				((ItemArmor) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot1))
					.blockMin();
			bonusMax +=
				((ItemArmor) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot1))
					.blockMax();
		}
		if (_equippedArmorSlot2 >= 0)
		{
			bonusMin +=
				((ItemArmor) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot2))
					.blockMin();
			bonusMax +=
				((ItemArmor) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot2))
					.blockMax();
		}
		if (_equippedArmorSlot3 >= 0)
		{
			bonusMin +=
				((ItemArmor) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot3))
					.blockMin();
			bonusMax +=
				((ItemArmor) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot3))
					.blockMax();
		}
		if (_equippedArmorSlot4 >= 0)
		{
			bonusMin +=
				((ItemArmor) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot4))
					.blockMin();
			bonusMax +=
				((ItemArmor) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot4))
					.blockMax();
		}

		int rndBlock = Helper.getRandomIntFromRange(bonusMin, bonusMax);
		bl[0] = rndBlock + super.abilityAbsorbDamage();
		bl[1] = bonusMin;
		bl[2] = bonusMax;
		bl[3] = super.abilityAbsorbDamage();

		return bl;
	}

	public int[] attackTypeHitChances()
	{
		int[] atHits =
			new int[((ItemWeapon) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_WEAPON, _equippedWeapon))
				.attackTypeHitMods().size()];

		for (int a = 0; a < atHits.length; a++)
		{
			atHits[a] =
				((ItemWeapon) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_WEAPON, _equippedWeapon))
					.attackTypeHitMods().get(a);
		}
		return atHits;
	}

	public int hitChance()
	{
		int wp = 0;

		if (_equippedWeapon >= 0)
		{
			wp =
				((ItemWeapon) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_WEAPON, _equippedWeapon))
					.baseHitChance();
		}

		int sum = wp + super.modHitChanceAbility() + super.modHitChanceEffect() + super.baseHitChance();

		sum = Helper.getStatMod(knowDiff(), sum);

		return sum;

	}

	public int dodgeChance()
	{
		/*
		int bonus = 0;
		if (_equippedArmorSlot1 >= 0)
			bonus +=
				((ItemArmor) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot1))
					.modifyDodge();

		if (_equippedArmorSlot2 >= 0)
			bonus +=
				((ItemArmor) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot2))
					.modifyDodge();

		if (_equippedArmorSlot3 >= 0)
		{
			Log.d(
				"armorinfo",
				"dodge for slot3 (id"
					+ _equippedArmorSlot3
					+ ") is "
					+ ((ItemArmor) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot3))
						.modifyDodge());

			bonus +=
				((ItemArmor) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot3))
					.modifyDodge();
		}

		if (_equippedArmorSlot4 >= 0)
			bonus +=
				((ItemArmor) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot4))
					.modifyDodge();

		int sum =
			bonus + super.modDodgeChance() + super.baseDodgeChance() + _item1ModDodgeAmount + _item2ModDodgeAmount;

		sum = Helper.getStatMod(reacDiff(), sum);

		return sum;
		
		*/
		
		int sum = super.modDodgeChance() + super.baseDodgeChance() + _item1ModDodgeAmount + _item2ModDodgeAmount + _equippedStatBonuses[0];
		
		return Helper.getStatMod(reacDiff(), sum);
	}

	public int playerID()
	{
		return _playerID;
	}

	public int playerClass()
	{
		return _playerClass;
	}

	public int equippedWeapon()
	{
		return _equippedWeapon;
	}

	public void setEquippedWeapon(int ownedItemID)
	{
		Log.d("equipthing", "new weapon equipped: " + ownedItemID);
		this._equippedWeapon = ownedItemID;
	}

	public int equippedArmorSlot1()
	{
		return _equippedArmorSlot1;
	}

	public void setEquippedArmorSlot1(int ownedItemID)
	{
		Log.d("equipthing", "new helm equipped: " + ownedItemID);

		this._equippedArmorSlot1 = ownedItemID;
	}

	public int equippedArmorSlot2()
	{
		return _equippedArmorSlot2;
	}

	public void setEquippedArmorSlot2(int ownedItemID)
	{
		Log.d("equipthing", "new chest equipped: " + ownedItemID);

		this._equippedArmorSlot2 = ownedItemID;
	}

	public int equippedArmorSlot3()
	{
		return _equippedArmorSlot3;
	}

	public void setEquippedArmorSlot3(int ownedItemID)
	{
		Log.d("equipthing", "new shoes equipped: " + ownedItemID);

		this._equippedArmorSlot3 = ownedItemID;
	}

	public int equippedArmorSlot4()
	{
		return _equippedArmorSlot4;
	}

	public void setEquippedArmorSlot4(int ownedItemID)
	{
		Log.d("equipthing", "new trinket equipped: " + ownedItemID);		
	
		//check if previous trinket granted us an ability, if it did, remove the active ability
		//this does not affect owned items where a duplicate owned rune should still be available
		if(_equippedArmorSlot4 >= 0 && DefinitionArmor.ARMOR_GRANTS_RUNE_ID[_equippedArmorSlot4] >= 0)
		{
			removeActiveAbility(DefinitionArmor.ARMOR_GRANTS_RUNE_ID[_equippedArmorSlot4]);
		}

		_equippedArmorSlot4 = ownedItemID;
	}

	public int equippedItemSlot1()
	{
		return _equippedItemSlot1;
	}

	public void setEquippedItemSlot1(int ownedItemID)
	{
		Log.d("equipthing", "new item1 equipped: " + ownedItemID);
		this._equippedItemSlot1 = ownedItemID;
	}

	public int equippedItemSlot2()
	{
		return _equippedItemSlot2;
	}

	public void setEquippedItemSlot2(int ownedItemID)
	{
		Log.d("equipthing", "new item2 equipped: " + ownedItemID);
		this._equippedItemSlot2 = ownedItemID;
	}

	public void removeEquippedItem(StoreItem thing)
	{
		if (thing.itemType() == DefinitionGlobal.ITEM_TYPE_WEAPON && _equippedWeapon == thing.id())
			_equippedWeapon = -1;

		else if (thing.itemType() == DefinitionGlobal.ITEM_TYPE_ARMOR && _equippedArmorSlot1 == thing.id())
			_equippedArmorSlot1 = -1;

		else if (thing.itemType() == DefinitionGlobal.ITEM_TYPE_ARMOR && _equippedArmorSlot2 == thing.id())
			_equippedArmorSlot2 = -1;

		else if (thing.itemType() == DefinitionGlobal.ITEM_TYPE_ARMOR && _equippedArmorSlot3 == thing.id())
			_equippedArmorSlot3 = -1;

		else if (thing.itemType() == DefinitionGlobal.ITEM_TYPE_ARMOR && _equippedArmorSlot4 == thing.id())
			_equippedArmorSlot4 = -1;

		else if (thing.itemType() == DefinitionGlobal.ITEM_TYPE_ITEM && _equippedItemSlot1 == thing.id())
			_equippedItemSlot1 = -1;

		else if (thing.itemType() == DefinitionGlobal.ITEM_TYPE_ITEM && _equippedItemSlot2 == thing.id())
			_equippedItemSlot2 = -1;

		else if (thing.itemType() == DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY)
		{
			if (super.checkPlayerHasAbility(thing.id()))
				super.removeActiveAbility(thing.id());
		}
	}

	public boolean hasItemEquipped(int itemType, int id)
	{
		if (itemType == DefinitionGlobal.ITEM_TYPE_WEAPON && _equippedWeapon == id)
			return true;

		else if (itemType == DefinitionGlobal.ITEM_TYPE_ARMOR && _equippedArmorSlot1 == id)
			return true;

		else if (itemType == DefinitionGlobal.ITEM_TYPE_ARMOR && _equippedArmorSlot2 == id)
			return true;

		else if (itemType == DefinitionGlobal.ITEM_TYPE_ARMOR && _equippedArmorSlot3 == id)
			return true;

		else if (itemType == DefinitionGlobal.ITEM_TYPE_ARMOR && _equippedArmorSlot4 == id)
			return true;

		else if (itemType == DefinitionGlobal.ITEM_TYPE_ITEM && _equippedItemSlot1 == id)
			return true;

		else if (itemType == DefinitionGlobal.ITEM_TYPE_ITEM && _equippedItemSlot2 == id)
			return true;

		else if (itemType == DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY)
		{
			if (super.checkPlayerHasAbility(id))
				return true;
		}

		return false;
	}

	public void setDefaultEquippedItems(int[] itemIds)
	{
		// weapon
		if (itemIds[0] >= 0 && _equippedWeapon < 0)
			setEquippedWeapon(itemIds[0]);

		// helm
		if (itemIds[1] >= 0 && _equippedArmorSlot1 < 0)
			setEquippedArmorSlot1(itemIds[1]);

		// chest
		if (itemIds[2] >= 0 && _equippedArmorSlot2 < 0)
			setEquippedArmorSlot2(itemIds[2]);

		// shoes
		if (itemIds[3] >= 0 && _equippedArmorSlot3 < 0)
			setEquippedArmorSlot3(itemIds[3]);

		// trinket
		if (itemIds[4] >= 0 && _equippedArmorSlot4 < 0)
			setEquippedArmorSlot4(itemIds[4]);

		// item 1
		if (itemIds[5] >= 0 && _equippedItemSlot1 < 0)
			setEquippedItemSlot1(itemIds[5]);

		// item 2
		if (itemIds[6] >= 0 && _equippedItemSlot2 < 0)
			setEquippedItemSlot2(itemIds[6]);

		// runes
		if (getActiveAbilities().length < 1)
		{
			for (int a = 0; a < DefinitionClasses.CLASS_STARTING_RUNES[playerClass()].length; a++)
			{
				addActiveAbility(DefinitionClasses.CLASS_STARTING_RUNES[playerClass()][a]);
			}
		}
		
		updateEquippedArmorStatBonuses();
	}

	public void equipItemBySlot(int defId, int slot)
	{
		Log.d("equip", "attempting to equip " + defId + " into slot " + slot);

		if (Helper.intArrayContainsInt(DefinitionGlobal.EQUIP_SLOT_WEAPON, slot))
		{
			setEquippedWeapon(defId);
			Log.d("equip", "equipped weapon id " + defId);
		}
		else if (Helper.intArrayContainsInt(DefinitionGlobal.EQUIP_SLOT_HELM, slot))
		{
			setEquippedArmorSlot1(defId);

			Log.d("equip", "equipped helm id " + defId);
		}
		else if (Helper.intArrayContainsInt(DefinitionGlobal.EQUIP_SLOT_CHEST, slot))
		{
			setEquippedArmorSlot2(defId);
			Log.d("equip", "equipped chest id " + defId);
		}
		else if (Helper.intArrayContainsInt(DefinitionGlobal.EQUIP_SLOT_SHOES, slot))
		{
			setEquippedArmorSlot3(defId);
			Log.d("equip", "equipped shoes id " + defId);
		}
		else if (Helper.intArrayContainsInt(DefinitionGlobal.EQUIP_SLOT_TRINKET, slot))
		{
			setEquippedArmorSlot4(defId);
			Log.d("equip", "equipped trinket id " + defId);
		}
		else if (Helper.intArrayContainsInt(DefinitionGlobal.EQUIP_SLOT_ITEM, slot))
		{
			if (DefinitionGlobal.EQUIP_SLOT_ITEM[0] == slot)
			{
				setEquippedItemSlot1(defId);
				Log.d("equip", "equipped item1 id " + defId);
			}

			else
			{
				setEquippedItemSlot2(defId);
				Log.d("equip", "equipped item2 id " + defId);
			}
		}
		
		updateEquippedArmorStatBonuses();
	}

	public int currentRound()
	{
		return _currentRound;
	}

	public void setCurrentRound(int currentRound)
	{
		this._currentRound = currentRound;
	}

	public int currentFight()
	{
		return _currentFight;
	}

	public void updateCurrentFight()
	{
		this._currentFight++;
	}

	public void updateCurrentRound()
	{
		this._currentRound++;
		this._currentFight = 1;
	}

	public boolean inRound()
	{
		return _inRound;
	}

	public void setInRound(boolean _inRound)
	{
		if (_currentFight < 1)
			_currentFight = 1;

		this._inRound = _inRound;
	}

	public boolean playerAttackWait()
	{
		return _playerAttackWait;
	}

	public void setPlayerAttackWait(boolean _playerAttackWait)
	{
		this._playerAttackWait = _playerAttackWait;
	}

	public void setBlockingFlag(int attackTypeBlockAmount)
	{
		if (attackTypeBlockAmount < 0)
		{
			_blocking = false;
			_blockingAmount = 0;
		}

		_blocking = true;
		_blockingAmount = attackTypeBlockAmount;
		Log.d("block", "Player set blocking with amount " + attackTypeBlockAmount);
	}
	
	public int blockingChance()
	{
		int blockchancesum = 0;
		if (equippedArmorSlot1() >= 0)
		{
			blockchancesum += DefinitionArmor.ARMOR_BLOCK_DAMAGE[equippedArmorSlot1()][0];
		}

		if (equippedArmorSlot2() >= 0)
		{
			blockchancesum += DefinitionArmor.ARMOR_BLOCK_DAMAGE[equippedArmorSlot2()][0];
		}

		if (equippedArmorSlot3() >= 0)
		{
			blockchancesum += DefinitionArmor.ARMOR_BLOCK_DAMAGE[equippedArmorSlot3()][0];
		}

		if (equippedArmorSlot4() >= 0)
		{
			blockchancesum += DefinitionArmor.ARMOR_BLOCK_DAMAGE[equippedArmorSlot4()][0];
		}
		
		return blockchancesum;
	}

	public int blockingAmount()
	{
		int blocksum = 0;
		if (equippedArmorSlot1() >= 0)
		{
			blocksum +=
				Helper.getRandomIntFromRange(DefinitionArmor.ARMOR_BLOCK_DAMAGE[equippedArmorSlot1()][2],
					DefinitionArmor.ARMOR_BLOCK_DAMAGE[equippedArmorSlot1()][1]);
		}

		if (equippedArmorSlot2() >= 0)
		{
			blocksum +=
				Helper.getRandomIntFromRange(DefinitionArmor.ARMOR_BLOCK_DAMAGE[equippedArmorSlot2()][2],
					DefinitionArmor.ARMOR_BLOCK_DAMAGE[equippedArmorSlot2()][1]);
		}

		if (equippedArmorSlot3() >= 0)
		{
			blocksum +=
				Helper.getRandomIntFromRange(DefinitionArmor.ARMOR_BLOCK_DAMAGE[equippedArmorSlot3()][2],
					DefinitionArmor.ARMOR_BLOCK_DAMAGE[equippedArmorSlot3()][1]);
		}

		if (equippedArmorSlot4() >= 0)
		{
			blocksum +=
				Helper.getRandomIntFromRange(DefinitionArmor.ARMOR_BLOCK_DAMAGE[equippedArmorSlot4()][2],
					DefinitionArmor.ARMOR_BLOCK_DAMAGE[equippedArmorSlot4()][1]);
		}

		return _blockingAmount + blocksum;
	}

	public boolean blocking()
	{
		return _blocking;
	}

	public void clearBlockingFlag()
	{
		_blocking = false;
	}

	public void addFreeAbility()
	{
		_freeAbilityCharge++;
	}
	
	public void updateEquippedArmorStatBonuses()
	{	
		for(int a = 0; a < _equippedStatBonuses.length; a++)
			_equippedStatBonuses[a] = 0;
		
		
		for(int a = 0; a < _equippedStatBonuses.length; a++)
		{
			if(equippedArmorSlot1() >= 0)
			{
				_equippedStatBonuses[a] += DefinitionArmor.ARMOR_MODIFIES[equippedArmorSlot1()][a];
			}
			
			if(equippedArmorSlot2() >= 0)
			{
				_equippedStatBonuses[a] += DefinitionArmor.ARMOR_MODIFIES[equippedArmorSlot2()][a];
			}
			
			if(equippedArmorSlot3() >= 0)
			{
				_equippedStatBonuses[a] +=  DefinitionArmor.ARMOR_MODIFIES[equippedArmorSlot3()][a];
			}
			
			if(equippedArmorSlot4() >= 0)
			{
				_equippedStatBonuses[a] +=  DefinitionArmor.ARMOR_MODIFIES[equippedArmorSlot4()][a];
			}
		}
	}
	
	public int execDiff()
	{
		return super.execDiff() + _equippedStatBonuses[3];
	}

	public int reacDiff()
	{
		return super.reacDiff()+ _equippedStatBonuses[4];
	}

	public int knowDiff()
	{
		return super.knowDiff()+ _equippedStatBonuses[5];
	}

	public int mageDiff()
	{
		return super.mageDiff() + _equippedStatBonuses[6];
	}

	public int luckDiff()
	{
		return super.luckDiff()+ _equippedStatBonuses[7];
	}
	
	public int exec()
	{
		return super.exec() + + _equippedStatBonuses[3];
	}
	
	public int reaction()
	{
		return super.reaction() + + _equippedStatBonuses[4];
	}
	
	public int knowledge()
	{
		return super.knowledge() + + _equippedStatBonuses[5];
	}
	
	public int magelore()
	{				
		return super.magelore() + _equippedStatBonuses[6];
	}
	
	public int luck()
	{
		return super.luck() + + _equippedStatBonuses[7];
	}

	public boolean hasFreeAbility()
	{
		if (_freeAbilityCharge <= 0)
		{
			return false;
		}

		return true;
	}

	public void itemApplyHPPercentGainAfterTurns(int percentAmt, int turns)
	{
		_itemApplyHPPercentGainAfterTurnsAmount = percentAmt;
		_itemApplyHPPercentGainAfterTurnsTurns = turns;
	}

	public boolean useFreeAbility()
	{
		_freeAbilityCharge--;
		if (_freeAbilityCharge < 0)
		{
			_freeAbilityCharge = 0;
			return false;
		}

		return true;
	}

	public int getRunChance()
	{
		int sum = DefinitionGlobal.RUN_CHANCE + reaction() + luck();

		sum = Helper.getStatMod(reacDiff(), sum);

		return sum;
	}

	public int getMaxDamage()
	{
		int[] dmgData = getDamageRange();

		int modDmg = dmgData[1];

		int sum = modDmg + super.getDamageCounterBonus(modDmg);

		sum = Helper.getStatMod(execDiff(), sum);

		return sum;
	}

	public int[] tryHit(int attackType)
	{
		// HIT: dmg amt, crit flag, stun flag
		// MISS: -1, roll, hit chance
		int[] returnData = new int[3];
		returnData[0] = 0;
		returnData[1] = 0;
		returnData[2] = 0;

		// mindmg, maxdmg, hit%, stun%, crit%
		int[] stats =
			((ItemWeapon) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_WEAPON, _equippedWeapon))
				.getModWeaponStatsWithAttackType(attackType);

		// check if this is a block
		if (stats[0] == -999)
		{
			returnData[0] = -999;
			returnData[1] = stats[1];
			return returnData;
		}
		else
		{
			_blocking = false;
			_blockingAmount = 0;
			returnData[0] = 1;
		}

		// do not check for miss if there is a pending weapon damage
		// bonus:
		
		//check for miss
		if (!(_counterModifyWeaponDamageTurns > 0))
		{
			int roll = Helper.randomInt(101);
			if (roll >= hitChance() + stats[2])
			{
				returnData[0] = -1;
				returnData[1] = roll;
				returnData[2] = hitChance() + stats[2];
				return returnData;
			}
		}

		int dmgAmt = getDamage();

		// do not check for crit or stun if there is a pending weapon damage
		// bonus:
		if (!(_counterModifyWeaponDamageTurns > 0))
		{
			// check if crit
			if (100 - Helper.randomInt(101) < critChance() + stats[4])
			{
				dmgAmt = 2 * stats[1];
				returnData[0] = dmgAmt;
				returnData[1] = 1;
				// check if stun and crit
				if (100 - Helper.randomInt(101) < stunChance() + stats[3])
				{
					returnData[2] = 1;
				}

				return returnData;
			}

			// check if stun
			if (100 - Helper.randomInt(101) < stunChance() + stats[3])
			{
				returnData[2] = 1;
			}
		}

		dmgAmt = Helper.getStatMod(execDiff(), dmgAmt);

		returnData[0] = dmgAmt;
		return returnData;
	}

	public int getDamage()
	{
		int[] dmgData = getDamageRange();
		
		int modDmg = Helper.getStatMod(execDiff(), dmgData[4]);

		int sum = modDmg + super.getDamageCounterBonus(modDmg);

		return sum;
	}

	public int[] getDamageRange()
	{
		int[] dmg = new int[5];
		int minDmg = DefinitionWeapons.WEAPON_MIN_DAMAGE[_equippedWeapon];
		int maxDmg = DefinitionWeapons.WEAPON_MAX_DAMAGE[_equippedWeapon];

		dmg[0] = minDmg + super.modHitDamage(minDmg)[0];
		dmg[1] = maxDmg + super.modHitDamage(maxDmg)[0];
		dmg[2] = super.modHitDamage(minDmg)[1];
		dmg[3] = super.modHitDamage(minDmg)[2];
		dmg[4] = Helper.getRandomIntFromRange(dmg[0], dmg[1] + 1);

		return dmg;
	}

	public int[] getPlayerHitStats(int attackType)
	{
		return ((ItemWeapon) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_WEAPON, _equippedWeapon))
			.getModWeaponStatsWithAttackType(attackType);
	}

	public ArrayList<Integer> foughtMonstersInRound()
	{
		return foughtMonstersInRound;
	}

	public void addMonsterInRound(int monsterID)
	{
		foughtMonstersInRound.add(monsterID);
	}

	public void resetFoughtMonstersInRound()
	{
		foughtMonstersInRound.clear();
	}

	public void setCurrentFight(int f)
	{
		_currentFight = f;
	}

	public int getEquippedItemBySlot(int s)
	{
		if (_equippedWeapon >= 0
			&& OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_WEAPON, _equippedWeapon) != null
			&& OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_WEAPON, _equippedWeapon).equippedSlot() == s)
			return _equippedWeapon;

		else if (_equippedArmorSlot1 >= 0
			&& OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot1) != null
			&& OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot1).equippedSlot() == s)
			return _equippedArmorSlot1;

		else if (_equippedArmorSlot2 >= 0
			&& OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot2) != null
			&& OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot2).equippedSlot() == s)
			return _equippedArmorSlot2;

		else if (_equippedArmorSlot3 >= 0
			&& OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot3) != null
			&& OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot3).equippedSlot() == s)
			return _equippedArmorSlot3;

		else if (_equippedArmorSlot4 >= 0
			&& OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot4) != null
			&& OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ARMOR, _equippedArmorSlot4).equippedSlot() == s)
			return _equippedArmorSlot4;

		else if (_equippedItemSlot1 >= 0
			&& OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ITEM, _equippedItemSlot1) != null
			&& OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ITEM, _equippedItemSlot1).equippedSlot() == s)
			return _equippedItemSlot1;

		else if (_equippedItemSlot2 >= 0
			&& OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ITEM, _equippedItemSlot2) != null
			&& OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ITEM, _equippedItemSlot2).equippedSlot() == s)
			return _equippedItemSlot2;

		else
			return -1;
	}

	public void itemModifyCrit(int amt, int turns)
	{
		if (_item1ModCritTurns <= 0)
		{
			_item1ModCritTurns = turns;
			_item1ModCritAmount = amt;
		}
		else
		{
			_item2ModCritTurns = turns;
			_item2ModCritAmount = amt;
		}
	}

	public void itemModifyDodge(int amt, int turns)
	{
		if (_item1ModDodgeTurns <= 0)
		{
			_item1ModDodgeTurns = turns;
			_item1ModDodgeAmount = amt;
		}
		else
		{
			_item2ModDodgeTurns = turns;
			_item2ModDodgeAmount = amt;
		}
	}

	public void itemModifyDamageTaken(int amt, int turns)
	{
		if (_item1ModDamageTakenTurns <= 0)
		{
			_item1ModDamageTakenTurns = turns;
			_item1ModDamageTakenAmount = amt;
		}
		else
		{
			_item2ModDamageTakenTurns = turns;
			_item2ModDamageTakenAmount = amt;
		}
	}

	public int updateHP(int amt)
	{
		if (amt > 0)
			return super.updateHP(amt);

		int newAmt = amt;
		if (_item1ModDamageTakenTurns >= 0)
		{
			newAmt += Helper.getPercentFromInt(_item1ModDamageTakenAmount, amt);
		}
		if (_item2ModDamageTakenTurns >= 0)
		{
			newAmt += Helper.getPercentFromInt(_item2ModDamageTakenAmount, amt);
		}
		if (newAmt > 0)
			newAmt = 0;

		return super.updateHP(newAmt);

	}

	public int stunChance()
	{

		int modStunAmt = 0;

		if (_equippedWeapon != -1)
		{
			modStunAmt += DefinitionWeapons.WEAPON_STUN_CHANCE[_equippedWeapon];
		}

		return super.stunChance() + modStunAmt;
		//return 75;
	}

	public int critChance()
	{
		int modCritAmt = 0;
		if (_item1ModCritTurns > 0)
			modCritAmt += _item1ModCritAmount;

		if (_item2ModCritTurns > 0)
			modCritAmt += _item2ModCritAmount;

		if (_equippedWeapon != -1)
		{
			modCritAmt += DefinitionWeapons.WEAPON_CRIT_CHANCE[_equippedWeapon];
		}

		return super.critChance() + modCritAmt;
	}

}
