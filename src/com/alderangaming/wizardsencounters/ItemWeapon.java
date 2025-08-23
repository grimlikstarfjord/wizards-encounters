package com.alderangaming.wizardsencounters;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

public class ItemWeapon extends StoreItem implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5010255769714870565L;

	private int _baseMaxDamage = 0;
	private int _baseMinDamage = 0;
	private int _baseHitChance = 0;
	private int _baseStunChance = 0;
	private int _baseCritChance = 0;

	private ArrayList<WeaponAttackType> _attackTypes = new ArrayList<WeaponAttackType>();

	public ItemWeapon(int t, int i, Context c)
	{
		super(t, i);

		setName(DefinitionWeapons.WEAPON_NAMES[i]);
		setDescription(DefinitionWeapons.WEAPON_DESCRIPTIONS[i]);
		setImageName(DefinitionWeapons.WEAPON_IMAGE[i], DefinitionWeapons.WEAPON_IMAGE[i], c);
		setAvailForANewPlayer(DefinitionWeapons.WEAPON_AVAIL_FOR_NEW_PLAYER[i]);
		setValue(DefinitionWeapons.WEAPON_GOLD_VALUE[i]);
		setCost(value());
		setMinLevel(DefinitionWeapons.WEAPON_MIN_LEVEL_TO_USE[i]);
		setShowsInStore(DefinitionWeapons.WEAPON_SHOWS_IN_STORE[i]);
		setOnlyForClasses(DefinitionWeapons.WEAPON_ONLY_FOR_CLASSES[i]);

		makeSelf(i);

	}

	private void makeSelf(int i)
	{
		// setup attack types for this weapon
		for (int a = 0; a < DefinitionWeapons.WEAPON_ATTACK_TYPES[i].length; a++)
		{
			int typeID = DefinitionWeapons.WEAPON_ATTACK_TYPES[i][a];
			WeaponAttackType s = new WeaponAttackType();
			s.id = typeID;
			s.name = DefinitionAttackTypes.ATTACK_TYPE_NAMES[typeID];
			s.minDamage = DefinitionAttackTypes.ATTACK_TYPE_MIN_DAMAGE[typeID];
			s.maxDamage = DefinitionAttackTypes.ATTACK_TYPE_MAX_DAMAGE[typeID];
			s.hitChance = DefinitionAttackTypes.ATTACK_TYPE_HIT_CHANCE[typeID];
			s.stunChance = DefinitionAttackTypes.ATTACK_TYPE_STUN_CHANCE[typeID];
			s.critChance = DefinitionAttackTypes.ATTACK_TYPE_CRIT_CHANCE[typeID];
			s.blockAmount = DefinitionAttackTypes.ATTACK_TYPE_BLOCK_NEXT_DAMAGE_PERCENT[typeID];
			_attackTypes.add(s);

			Log.d("block", "making attackType " + a + " has blockAmt: " + s.blockAmount);
		}

		_baseMaxDamage = DefinitionWeapons.WEAPON_MAX_DAMAGE[i];
		_baseMinDamage = DefinitionWeapons.WEAPON_MIN_DAMAGE[i];
		_baseHitChance = DefinitionWeapons.WEAPON_HIT_CHANCE[i];
		_baseStunChance = DefinitionWeapons.WEAPON_STUN_CHANCE[i];
		_baseCritChance = DefinitionWeapons.WEAPON_CRIT_CHANCE[i];
		
	}

	public int[] getDamageRange()
	{
		int[] rng = new int[2];
		rng[0] = getModWeaponStatsWithAttackType(-1)[0];
		rng[1] = getModWeaponStatsWithAttackType(-1)[1];
		return rng;
	}

	public int[] getModWeaponStatsWithAttackType(int at)
	{
		int[] stats = new int[5];
		
		
		// mindmg, maxdmg, hit%, stun%, crit%

		if (at != -1)
		{			
			Log.d("block", "attack type " + at + " used");

			if (_attackTypes.get(at).minDamage < 0)
			{
				// this is a block
				stats[0] = -1;
				stats[1] = _attackTypes.get(at).blockAmount;
				Log.d("block", "blockAmount found for " + _attackTypes.get(at).name + " is " + stats[1]);

				return stats;
			}

			stats[0] = _baseMinDamage + _attackTypes.get(at).minDamage;
			stats[1] = _baseMaxDamage + _attackTypes.get(at).maxDamage;
			stats[2] = _baseHitChance + _attackTypes.get(at).hitChance;
			stats[3] = _baseStunChance + _attackTypes.get(at).stunChance;
			stats[4] = _baseCritChance + _attackTypes.get(at).critChance;

		}

		if (at == -1)
		{
			// go through all attack types to get min and max
			int min = 99999;
			int max = 0;
			for (int a = 0; a < _attackTypes.size(); a++)
			{
				if (_attackTypes.get(a).minDamage < min)
					min = _attackTypes.get(a).minDamage;

				if (_attackTypes.get(a).maxDamage > max)
					max = _attackTypes.get(a).maxDamage;
			}
			stats[0] = _baseMinDamage + min;
			stats[1] = _baseMaxDamage + max;
		}

		return stats;
	}

	public int baseHitChance()
	{
		return _baseHitChance;
	}

	public ArrayList<Integer> attackTypeHitMods()
	{
		ArrayList<Integer> atHits = new ArrayList<Integer>();
		for (int a = 0; a < _attackTypes.size(); a++)
		{
			atHits.add(_attackTypes.get(a).hitChance);
		}
		return atHits;
	}

	public String[] getAttackTypeNames()
	{
		String[] names = new String[_attackTypes.size()];
		for (int a = 0; a < names.length; a++)
		{
			names[a] = _attackTypes.get(a).name;
		}
		return names;
	}

	public class WeaponAttackType implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 6215532645666358241L;
		String name = "";
		int id = -1;
		int minDamage = 0;
		int maxDamage = 0;
		int hitChance = 0;
		int stunChance = 0;
		int critChance = 0;
		int blockAmount = 0;
	}

	public WeaponAttackType getAttackTypeByIndex(int i)
	{
		return _attackTypes.get(i);
	}

	public WeaponAttackType[] attackTypes()
	{
		WeaponAttackType[] at = new WeaponAttackType[_attackTypes.size()];
		for (int a = 0; a < at.length; a++)
		{
			at[a] = _attackTypes.get(a);
		}
		return at;
	}
}
