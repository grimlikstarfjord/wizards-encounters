package com.alderangaming.wizardsencounters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

public class Helper
{

	public static int[] getRuneIdsForClassAndRank(int cls, int rank)
	{
		int numRunes = DefinitionRunes.runeData.length;
		ArrayList<Integer> clsRunes = new ArrayList<Integer>();
		for (int a = 0; a < numRunes; a++)
		{
			for (int b = 0; b < DefinitionRunes.runeData[a][DefinitionRunes.RUNE_ONLY_FOR_CLASSES].length; b++)
			{
				if ((Integer) DefinitionRunes.runeData[a][DefinitionRunes.RUNE_ONLY_FOR_CLASSES][b] == cls)
				{
					if ((Integer) DefinitionRunes.runeData[a][DefinitionRunes.RUNE_AVAIL_FOR_NEW_PLAYER][0] == 1)
					{
						clsRunes.add(a);
						continue;
					}
				}
			}
		}

		int[] r = new int[clsRunes.size()];
		for (int i = 0; i < clsRunes.size(); i++)
		{
			r[i] = clsRunes.get(i);
		}
		return r;
	}
	
	public static int getStatMod(int statDiff, int amt)
	{
		
		if (statDiff != 0)
		{
			double mult = statDiff / amt;
			int multAmt = (int) (mult * 100);
			if(multAmt < -50)
			{
				multAmt = -50;
			}
			if(multAmt > 50)
			{
				multAmt = 50;
			}
			
			Log.d("statmod", "a stat offset modified "+amt+" by " + multAmt + "%");

			int number = Helper.getPercentFromInt((multAmt), amt);

			Log.d("statmod", multAmt + "% of " + amt + " is " + number);

			amt  = amt + number;

			Log.d("statmod", "new amt is " + amt);
		}
		
		return amt;
	}

	public static RelativeLayout.LayoutParams getRandomButtonLayout(RelativeLayout combatLayout)
	{
		RelativeLayout.LayoutParams lp =
			new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		int xSize = combatLayout.getWidth();
		int ySize = combatLayout.getHeight();

		int xLoc = Helper.randomInt(xSize);
		int yLoc = Helper.randomInt(ySize);

		if (xLoc < 50)
			xLoc = 50;

		if (xLoc > xSize - 50)
			xLoc = xSize - 50;

		if (yLoc > ySize - 50)
			yLoc = ySize - 50;

		if (yLoc < 50)
			yLoc = 50;

		lp.leftMargin = Helper.randomInt(xLoc);
		lp.topMargin = 40 + Helper.randomInt(yLoc);

		return lp;
	}

	public static int getSpecialDamageAmount(int flagID, int wpnDmg, Actor source, Actor target)
	{
		int amt = 0;
		if (flagID == 0)
		{
			// determine damage based on target's stats
			amt = (int) ((0.5 * target.rank()) * (target.strength() + target.knowledge() + target.reaction()));
		}
		else if (flagID == 1)
		{
			// deal wpn dmg * (source strength / 5)
			amt = (int) (wpnDmg * (source.strength() / 5));
		}
		else if (flagID == 2)
		{
			// deal 0.5x (target str * target level)
			amt = (int) ((0.5 * (source.strength() * source.rank())));
		}
		else if (flagID == 3)
		{
			// deal 2x (player reaction * target intel)
			amt = (int) (2 * (source.reaction() * target.knowledge()));
		}
		else if (flagID == 4)
		{
			// deal (int * wpn dmg)/4
			amt = (int) ((source.knowledge() * wpnDmg) / 4);
		}

		if (amt < 1)
			amt = 1;

		return amt;
	}

	public static int getSpecialAbsorbAmount(int flagID, Actor source, Actor target)
	{
		int amt = 0;
		if (flagID == 0)
		{
			// absorb 4x source int
			amt = (int) 4 * source.knowledge();
		}
		return amt;
	}

	public static int getSpecialRegainHPAmount(int flagID, Actor source, Actor target)
	{
		int amt = 0;
		if (flagID == 0)
		{
			// regain 2x int
			amt = 2 * source.knowledge();
		}
		return amt;
	}

	public static int getRandomIntFromRange(int int1, int int2)
	{
		int range = 0;
		int min = 0;

		if (int1 > int2)
		{
			range = int1 - int2;
			min = int2;
		}

		else if (int2 > int1)
		{
			range = int2 - int1;
			min = int1;
		}
		else
		{
			// they are equal
			return int1;
		}

		return (int) randomInt(range) + min;
	}

	public static int[] getSpecialCastingInt(int flagID, Actor source, Actor target)
	{
		int[] ints = new int[2];
		ints[0] = -1;
		ints[1] = -1;

		if (flagID == 0)
		{
			// apply a random effect to monster on each turn
			// get a random effectID
			ints[0] = randomInt(DefinitionEffects.EFFECT_NAMES.length);
		}
		if (flagID == 1)
		{
			// get casting turns = int
			ints[1] = source.knowledge();
		}

		return ints;
	}

	public static int getPercentFromInt(int percent, int amt)
	{

		float blockPercent = (float) (0.01 * percent);

		float amtFloat = blockPercent * amt;

		return Math.round(amtFloat);
	}

	public static SpannableString getSpanString(String newLog)
	{

		SpannableString spanText = new SpannableString(newLog);
		int spanColor = Color.WHITE;
		if (newLog.contains("dealt") || newLog.contains("Stunned"))
		{
			spanColor = Color.parseColor("#FFFF9900");
		}
		else if (newLog.contains("has spotted"))
		{
			spanColor = Color.parseColor("#FFFF3300");
		}
		else if (newLog.contains("You are"))
		{
			spanColor = Color.parseColor("#FFFFD700");
		}
		else if (newLog.contains("and Stunned you"))
		{
			spanColor = Color.parseColor("#FFFF9900");
		}
		else if (newLog.contains("hits you for"))
		{
			spanColor = Color.parseColor("#FF30C1AE");
		}
		else if (newLog.contains("You CRIT"))
		{
			spanColor = Color.parseColor("#FFFF99CC");
		}
		else if (newLog.contains("has found you"))
		{
			spanColor = Color.parseColor("#FF30C1AE");
		}
		else if (newLog.contains("CRIT you"))
		{
			spanColor = Color.parseColor("#FF30C1AE");
		}
		else if (newLog.contains("absorbed"))
		{
			spanColor = Color.parseColor("#FFB4EEB4");
		}
		else if (newLog.contains("dodged") || newLog.contains("monster missed"))
		{
			spanColor = Color.parseColor("#FF2493D2");
		}
		else if (newLog.contains("Using ability"))
		{
			spanColor = Color.parseColor("#FF54FF9F");
		}
		else if (newLog.contains("dealt you") && newLog.contains("damage"))
		{
			spanColor = Color.parseColor("#FFFFB6C1");
		}
		else if (newLog.contains("You found") && newLog.contains("gold!"))
		{
			spanColor = Color.parseColor("#FFE9FA27");
		}
		else if (newLog.contains("gold!"))
		{
			spanColor = Color.parseColor("#FFE9FA27");
		}
		else if (newLog.contains("gained") || newLog.contains("Forever!"))
		{
			spanColor = Color.parseColor("#FF32CD32");
		}
		else if (newLog.contains("recovered"))
		{
			spanColor = Color.parseColor("#FF76EE00");
		}
		else if (newLog.contains("is Stunned"))
		{
			spanColor = Color.parseColor("#FFFF9900");
		}
		else
		{
			spanColor = Color.parseColor("#FFFFFFFF");
		}

		spanText.setSpan(new ForegroundColorSpan(spanColor), 0, newLog.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		return spanText;
	}

	public static int[] getClassesByRuneId(int id)
	{
		int[] ar = new int[DefinitionRunes.runeData[id][DefinitionRunes.RUNE_ONLY_FOR_CLASSES].length];
		for (int a = 0; a < ar.length; a++)
		{
			ar[a] = (Integer) DefinitionRunes.runeData[id][DefinitionRunes.RUNE_ONLY_FOR_CLASSES][a];
		}
		return ar;
	}

	public static boolean intArrayContainsInt(int[] intarray, int i)
	{
		for (int a = 0; a < intarray.length; a++)
		{
			if (intarray[a] == i)
				return true;
		}
		return false;
	}

	public static boolean arrayContainsItem(StoreItem[] ar1, StoreItem item)
	{

		for (int a = 0; a < ar1.length; a++)
		{
			if (ar1[a].itemType() == item.itemType() && ar1[a].id() == item.id())
				return true;
		}

		return false;
	}

	public static ItemRune[] getStartingRunesForClass(int cls, Context context)
	{
		int[] runeIds = getRuneIdsForClassAndRank(cls, 1);
		ItemRune[] startingClassRunes = new ItemRune[runeIds.length];

		for (int a = 0; a < runeIds.length; a++)
		{
			ItemRune r = new ItemRune(DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY, runeIds[a], context);
			startingClassRunes[a] = r;
		}

		return startingClassRunes;
	}

	public static ItemWeapon[] getStartingPlayerWeaponsForClass(int classID, Context context)
	{
		int[] wpIds = new int[DefinitionWeapons.WEAPON_NAMES.length];
		ArrayList<ItemWeapon> startingWeapons = new ArrayList<ItemWeapon>();

		for (int a = 0; a < wpIds.length; a++)
		{
			if (DefinitionWeapons.WEAPON_AVAIL_FOR_NEW_PLAYER[a] == 1)
			{
				for (int b = 0; b < DefinitionWeapons.WEAPON_ONLY_FOR_CLASSES[a].length; b++)
				{
					if (DefinitionWeapons.WEAPON_ONLY_FOR_CLASSES[a][b] == classID)
					{
						ItemWeapon w = new ItemWeapon(DefinitionGlobal.ITEM_TYPE_WEAPON, a, context);

						startingWeapons.add(w);
					}
				}
			}
		}

		ItemWeapon[] wp = new ItemWeapon[startingWeapons.size()];
		for (int i = 0; i < startingWeapons.size(); i++)
		{
			wp[i] = startingWeapons.get(i);
		}

		return wp;
	}

	public static ItemArmor[] getStartingPlayerArmorForClass(int classID, Context context)
	{
		int[] armorIds = new int[DefinitionArmor.ARMOR_NAMES.length];
		ArrayList<ItemArmor> startingArmor = new ArrayList<ItemArmor>();

		for (int a = 0; a < armorIds.length; a++)
		{
			if (DefinitionArmor.ARMOR_AVAILABLE_FOR_NEW_PLAYER[a] == 1)
			{
				for (int b = 0; b < DefinitionArmor.ARMOR_FOR_CLASS_ONLY[a].length; b++)
				{
					if (DefinitionArmor.ARMOR_FOR_CLASS_ONLY[a][b] == classID)
					{
						ItemArmor arm = new ItemArmor(DefinitionGlobal.ITEM_TYPE_ARMOR, a, context);
						startingArmor.add(arm);
					}
				}
			}
		}
		ItemArmor[] rm = new ItemArmor[startingArmor.size()];
		for (int i = 0; i < rm.length; i++)
		{
			rm[i] = startingArmor.get(i);
		}

		return rm;
	}

	public static ItemClass[] getStartingPlayerClasses(Context context)
	{

		ArrayList<Integer> freeClassIds = new ArrayList<Integer>();

		for (int a = 0; a < DefinitionClasses.CLASS_NAMES.length; a++)
		{
			if (DefinitionClasses.CLASS_AVAIL_FOR_NEW_PLAYER[a] == 1)
				freeClassIds.add(a);
		}

		ItemClass[] freeClasses = new ItemClass[freeClassIds.size()];

		for (int a = 0; a < freeClassIds.size(); a++)
		{
			ItemClass p = new ItemClass(DefinitionGlobal.ITEM_TYPE_PLAYER_CLASS, freeClassIds.get(a), context);

			freeClasses[a] = p;
		}

		return freeClasses;
	}

	public static int getWinningPercent(ArrayList<Integer> percents)
	{
		int choice = -1;

		// if percentages are equal, pick one at random
		boolean same = true;
		for (int a = 0; a < percents.size(); a++)
		{
			for (int b = 0; b < percents.size(); b++)
			{
				if (a != b)
				{
					same = false;
					break;
				}
			}
		}

		// this will go through all choices and stop at last one that meets roll
		// requirement
		int roll = randomInt(100);
		for (int a = 0; a < percents.size(); a++)
		{
			int last = 0;
			int i = percents.get(a) + last;

			if (roll > last && roll < i)
			{
				choice = a;
			}
			last = last + percents.get(a);
		}

		if (choice < 0 || same == true)
			choice = getRandomIntFromRange(0, percents.size());

		return choice;
	}

	public static ArrayList<String> getNameAmountsFromStoreItemArray(ArrayList<StoreItem> items)
	{
		ArrayList<String> names = new ArrayList<String>();

		for (int a = 0; a < items.size(); a++)
		{
			names.add(items.get(a).nameAndAmount());
		}

		return names;
	}

	public static int randomPosNeg()
	{
		if (randomInt(100) < 50)
			return -1;

		else
			return 1;
	}

	public static int getCastingStatDamage(ItemRune ability, boolean isBonus, Actor source, Actor target)
	{
		int dmg = 0;

		if (ability.dealStatIsHpPercentDamageSourceOrTargetFlag() == 0)
		{

			// deal damage based on player's stat
			int amt = 0;
			int statId = ability.dealStatIsHpPercentDamageStatId();

			if (statId == 0)
			{
				// strength
				amt = source.strength();
			}
			else if (statId == 1)
			{
				amt = source.reaction();
			}
			else if(statId == 2)
			{
				amt = source.knowledge();
			}
			else if(statId == 3)
			{
				amt = source.magelore();
			}
			else if(statId == 4)
			{
				amt = source.luck();
			}

			dmg = (int) Math.round((0.01 * amt) * target.maxHP());			

			
		}
		else
		{
			// deal damage based on monster's stat
			int amt = 0;
			int statId = ability.dealStatIsHpPercentDamageStatId();
			if (statId == 0)
			{
				// strength
				amt = target.strength();
			}
			else if (statId == 1)
			{
				amt = target.reaction();
			}
			else if(statId == 2)
			{
				amt = target.knowledge();
			}			
			else if(statId == 3)
			{
				amt = target.magelore();
			}			
			else if(statId == 4)
			{
				amt = target.luck();
			}
			dmg = (int) Math.round((0.01 * amt) * target.maxHP());		
			
		}
		return dmg;
	}

	public static int getCastingDamage(ItemRune ability, boolean isBonus)
	{
		int dmg = getRandomIntFromRange(ability.dealWeaponDamageMax(), ability.dealWeaponDamageMin());
		if (isBonus)
			dmg = getRandomIntFromRange(ability.dealWeaponDamageMaxBonus(), ability.dealWeaponDamageMinBonus());

		return dmg;
	}

	public static boolean checkForBonus(ItemRune ability, Actor source, Actor target)
	{
		// check for bonus
		if (ability.comboActiveEffectRequirementID() >= 0)
		{
			if (ability.comboActiveEffectActor() == 0)
			{
				if (source.isEffectActive(ability.comboActiveEffectRequirementID()))
					return true;
			}
			else
			{
				if (target.isEffectActive(ability.comboActiveEffectRequirementID()))
					return true;
			}
		}
		return false;
	}

	public static boolean itemIsOfStartingType(StoreItem item)
	{

		// check weapons
		if (item.itemType() == DefinitionGlobal.ITEM_TYPE_WEAPON)
		{
			if (DefinitionWeapons.WEAPON_AVAIL_FOR_NEW_PLAYER[item.id()] == 1)
			{
				return true;
			}
		}
		// check armor
		else if (item.itemType() == DefinitionGlobal.ITEM_TYPE_ARMOR)
		{
			if (DefinitionArmor.ARMOR_AVAILABLE_FOR_NEW_PLAYER[item.id()] == 1)
			{
				return true;
			}
		}
		// check ability runes
		else if (item.itemType() == DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY)
		{
			for (int a = 0; a < DefinitionClasses.CLASS_NAMES.length; a++)
			{
				for (int b = 0; b < DefinitionClasses.CLASS_STARTING_RUNES[a].length; b++)
				{
					if (DefinitionClasses.CLASS_STARTING_RUNES[a][b] == item.id())
						return true;
				}
			}
		}
		return false;
	}

	public static int[] getPossibleMonstersForFight(int[] lvls, ArrayList<Integer> foughtMonsters)
	{
		// lvls = 1 2
		// monster rank = 1 1 1 2 2 3
		ArrayList<Integer> m = new ArrayList<Integer>();

		for (int c = 0; c < lvls.length; c++)
		{
			for (int a = 0; a < DefinitionMonsters.MONSTER_ROUND.length; a++)
			{
				if (DefinitionMonsters.MONSTER_ROUND[a] == lvls[c] && foughtMonsters.indexOf(a) < 0)
				{
					m.add(a);
					Log.d("combat", "added possible monster for fight: id " + a);

				}
			}
		}

		int[] mIds = new int[m.size()];
		for (int b = 0; b < m.size(); b++)
		{
			mIds[b] = m.get(b);
		}

		return mIds;
	}

	public static int getBossMonsterIDForRound(int round)
	{
		int id = 0;
		for (int a = 0; a < DefinitionMonsters.MONSTER_NAMES.length; a++)
		{
			if (DefinitionMonsters.MONSTER_ROUND[a] == round)
				id = a;
		}
		return id;
	}

	public static Map<String, Object> createPlayerMap(CharSequence Name, CharSequence Class, CharSequence Round,
		CharSequence Rank)
	{
		Map<String, Object> row = new HashMap<String, Object>();
		row.put("Name", Name);
		row.put("Class", Class);
		row.put("Round", Round);
		row.put("Rank", Rank);
		return row;
	}

	public static Map<String, Object> createMap(CharSequence Name, CharSequence APCost, CharSequence Description)
	{
		Map<String, Object> row = new HashMap<String, Object>();
		row.put("Name", Name);
		row.put("APCost", "Cost: " + APCost);
		row.put("Description", Description);
		return row;
	}

	public static Map<String, Object> createAttackTypeMap(CharSequence Name, CharSequence Stat1, CharSequence Stat2)
	{
		Map<String, Object> row = new HashMap<String, Object>();
		row.put("Name", Name);
		row.put("Stat1", Stat1);
		row.put("Stat2", Stat2);
		return row;
	}

	public static Map<String, Object> createEffectMap(CharSequence Name, CharSequence EfTurns,
		CharSequence Description, int EfImage)
	{
		Map<String, Object> row = new HashMap<String, Object>();
		row.put("effectName", Name);
		row.put("effectTurns", EfTurns);
		row.put("effectDescription", Description);
		row.put("effectImage", EfImage);
		return row;
	}

	public static Map<String, Object> createStoreMap(CharSequence storeName, CharSequence storeDescription,
		int storeImage, CharSequence storeLevel, CharSequence storeValue)
	{
		Map<String, Object> row = new HashMap<String, Object>();
		row.put("storeName", storeName);
		row.put("storeDescription", storeDescription);
		row.put("storeImage", storeImage);
		row.put("storeLevel", storeLevel);
		row.put("storeValue", storeValue);
		return row;
	}

	public static Map<String, Object> createEquipMap(CharSequence Name, CharSequence Description, int EfImage)
	{
		Map<String, Object> row = new HashMap<String, Object>();
		row.put("equipName", Name);
		row.put("equipDescription", Description);
		row.put("equipImage", EfImage);
		return row;
	}

	public static int getEffectIdByName(String name)
	{
		int id = -1;
		for (int a = 0; a < DefinitionEffects.EFFECT_NAMES.length; a++)
		{
			if (DefinitionEffects.EFFECT_NAMES[a].equals(name))
				id = a;
		}
		return id;
	}

	public static int randomInt(int range)
	{
		return (int) Math.floor(Math.random() * range);
	}

	public static int getRandomIntFromIntArray(int[] arr)
	{
		int ind = (int) Math.floor(Math.random() * arr.length);
		return arr[ind];
	}

	public static int getRandomIntFromIntArray(ArrayList<Integer> arr)
	{
		int ind = (int) Math.floor(Math.random() * arr.size());
		return arr.get(ind);
	}

	public static ArrayList<StoreItem> getRandomDropsForRound(int round, int value, Context context)
	{
		ArrayList<StoreItem> drops = new ArrayList<StoreItem>();

		int valueMin = value - Helper.getPercentFromInt(50, value);
		int valueMax = value + Helper.getPercentFromInt(50, value);

		if (valueMax < 11)
			valueMax = 10;

		Log.d("loot", "pulling from loot range " + valueMin + " to " + valueMax);

		// weapon drops
		for (int a = 0; a < DefinitionWeapons.WEAPON_NAMES.length; a++)
		{
			if (DefinitionWeapons.WEAPON_DROPS[a][0] <= round && DefinitionWeapons.WEAPON_DROPS[a][1] >= round)
			{
				if (randomInt(100) < DefinitionWeapons.WEAPON_DROPS[a][2])
				{
					ItemWeapon w = new ItemWeapon(DefinitionGlobal.ITEM_TYPE_WEAPON, a, context);
					drops.add(w);
				}
			}
		}

		// armor drops
		for (int a = 0; a < DefinitionArmor.ARMOR_NAMES.length; a++)
		{
			if (DefinitionArmor.ARMOR_DROPS[a][0] <= round && DefinitionArmor.ARMOR_DROPS[a][1] >= round)
			{
				if (randomInt(100) < DefinitionArmor.ARMOR_DROPS[a][2])
				{
					ItemArmor ar = new ItemArmor(DefinitionGlobal.ITEM_TYPE_ARMOR, a, context);
					drops.add(ar);
				}
			}
		}

		// item drops
		for (int a = 0; a < DefinitionItems.ITEM_NAME.length; a++)
		{
			if (DefinitionItems.ITEM_DROPS[a][0] <= round && DefinitionItems.ITEM_DROPS[a][1] >= round)
			{
				if (randomInt(100) < DefinitionItems.ITEM_DROPS[a][2])
				{
					ItemItem ar = new ItemItem(DefinitionGlobal.ITEM_TYPE_ITEM, a, context);
					drops.add(ar);
				}
			}
		}

		// ability rune drops
		for (int a = 0; a < DefinitionRunes.runeData.length; a++)
		{
			if ((Integer) DefinitionRunes.runeData[a][DefinitionRunes.RUNE_DROPS][0] <= round
				&& (Integer) DefinitionRunes.runeData[a][DefinitionRunes.RUNE_DROPS][1] >= round)
			{
				if (randomInt(100) < (Integer) DefinitionRunes.runeData[a][DefinitionRunes.RUNE_DROPS][2])
				{
					ItemRune ar = new ItemRune(DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY, a, context);
					drops.add(ar);
				}
			}
		}

		// class rune drops
		for (int a = 0; a < DefinitionClasses.CLASS_NAMES.length; a++)
		{
			if (DefinitionClasses.CLASS_DROPS[a][0] <= round && DefinitionClasses.CLASS_DROPS[a][1] >= round)
			{
				if (randomInt(100) < DefinitionClasses.CLASS_DROPS[a][2])
				{
					ItemItem ar = new ItemItem(DefinitionGlobal.ITEM_TYPE_ITEM, a, context);
					drops.add(ar);
				}
			}
		}

		return drops;
	}
}
