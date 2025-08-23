package com.alderangaming.wizardsencounters;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

public class OwnedItems
{

	private static ArrayList<StoreItem> ownedItems = new ArrayList<StoreItem>();
	private static ArrayList<StoreItem> tempEarnedItems = new ArrayList<StoreItem>();
	private static int _gold = 0;

	public static StoreItem[] getOwnedItems()
	{
		StoreItem[] items = new StoreItem[ownedItems.size()];
		for (int a = 0; a < items.length; a++)
		{
			items[a] = ownedItems.get(a);
		}
		return items;
	}

	public static void addTempItems(ArrayList<StoreItem> items)
	{
		for (int a = 0; a < items.size(); a++)
			tempEarnedItems.add(items.get(a));
	}

	public static void saveTempItemsToOwnedItems()
	{
		StoreItem[] toadd = new StoreItem[tempEarnedItems.size()];
		for (int a = 0; a < toadd.length; a++)
		{
			toadd[a] = tempEarnedItems.get(a);
		}

		addOwnedItems(toadd);
	}

	public static void addTempItem(StoreItem item)
	{
		tempEarnedItems.add(item);
	}

	public static StoreItem[] getTempItems()
	{
		StoreItem[] items = new StoreItem[tempEarnedItems.size()];
		for (int a = 0; a < tempEarnedItems.size(); a++)
		{
			items[a] = tempEarnedItems.get(a);
		}
		return items;
	}

	public static int getAmountOwnedOfTypeId(int type, int id)
	{
		int amt = 0;
		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (ownedItems.get(a).itemType() == type && ownedItems.get(a).id() == id)
				amt = ownedItems.get(a).amount();
		}
		return amt;
	}

	public static boolean getPlayerOwnsThis(int type, int id)
	{
		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (ownedItems.get(a).itemType() == type && ownedItems.get(a).id() == id)
			{
				return true;
			}
		}
		return false;
	}

	public static StoreItem getPlayerOwnsThis(StoreItem i)
	{
		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (ownedItems.get(a).itemType() == i.itemType() && ownedItems.get(a).id() == i.id())
			{
				return ownedItems.get(a);
			}
		}
		return null;
	}

	public static String getItemEnhancedNameByTypeId(int type, int id)
	{
		String s = "";
		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_ITEM && ownedItems.get(a).id() == id)
			{
				StoreItem i = ownedItems.get(a);

				if (i.maxCharges() > 1)
				{
					int amtPurchased = i.chargesPurchased();
					String num = " I";

					if (amtPurchased == 1)
						num = " I (" + i.chargesLeft() + "/" + amtPurchased + ")";
					else if (amtPurchased == 2)
						num = " II (" + i.chargesLeft() + "/" + amtPurchased + ")";
					else if (amtPurchased == 3)
						num = " III (" + i.chargesLeft() + "/" + amtPurchased + ")";
					else if (amtPurchased == 4)
						num = " IV (" + i.chargesLeft() + "/" + amtPurchased + ")";
					else if (amtPurchased == 5)
						num = " V (" + i.chargesLeft() + "/" + amtPurchased + ")";
					else if (amtPurchased == 6)
						num = " VI (" + i.chargesLeft() + "/" + amtPurchased + ")";
					else if (amtPurchased == 7)
						num = " VII (" + i.chargesLeft() + "/" + amtPurchased + ")";
					else if (amtPurchased == 8)
						num = " VIII (" + i.chargesLeft() + "/" + amtPurchased + ")";
					else if (amtPurchased == 9)
						num = " IX (" + i.chargesLeft() + "/" + amtPurchased + ")";
					else if (amtPurchased == 10)
						num = " X (" + i.chargesLeft() + "/" + amtPurchased + ")";

					s = DefinitionItems.itemdata[i.id()][DefinitionItems.ITEM_NAME][0] + num;
				}
				else
				{
					s =
						DefinitionItems.itemdata[i.id()][DefinitionItems.ITEM_NAME][0] + " (" + i.chargesLeft() + "/"
							+ i.chargesPurchased() + ")";
				}
			}
		}
		return s;
	}

	public static ArrayList<StoreItem> getItemsToSell()
	{
		ArrayList<StoreItem> items = new ArrayList<StoreItem>();
		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_ITEM)
			{
				StoreItem i = ownedItems.get(a);

				if (i.maxCharges() > 1)
				{
					int amtPurchased = i.chargesPurchased();
					String num = " I";

					if (amtPurchased == 1)
						num = " I (" + i.chargesLeft() + "/" + amtPurchased + ")";
					else if (amtPurchased == 2)
						num = " II (" + i.chargesLeft() + "/" + amtPurchased + ")";
					else if (amtPurchased == 3)
						num = " III (" + i.chargesLeft() + "/" + amtPurchased + ")";
					else if (amtPurchased == 4)
						num = " IV (" + i.chargesLeft() + "/" + amtPurchased + ")";
					else if (amtPurchased == 5)
						num = " V (" + i.chargesLeft() + "/" + amtPurchased + ")";
					else if (amtPurchased == 6)
						num = " VI (" + i.chargesLeft() + "/" + amtPurchased + ")";
					else if (amtPurchased == 7)
						num = " VII (" + i.chargesLeft() + "/" + amtPurchased + ")";
					else if (amtPurchased == 8)
						num = " VIII (" + i.chargesLeft() + "/" + amtPurchased + ")";
					else if (amtPurchased == 9)
						num = " IX (" + i.chargesLeft() + "/" + amtPurchased + ")";
					else if (amtPurchased == 10)
						num = " X (" + i.chargesLeft() + "/" + amtPurchased + ")";

					i.setName(DefinitionItems.itemdata[i.id()][DefinitionItems.ITEM_NAME][0] + num);
				}
				else
				{
					i.setName(DefinitionItems.itemdata[i.id()][DefinitionItems.ITEM_NAME][0] + " (" + i.chargesLeft()
						+ "/" + i.chargesPurchased() + ")");
				}

				items.add(i);
			}
		}
		return items;
	}

	public static void addChargeToItem(StoreItem i)
	{
		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (ownedItems.get(a).id() == i.id() && ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_ITEM)
			{
				ownedItems.get(a).addCharge();
				if (ownedItems.get(a).chargesLeft() > ownedItems.get(a).chargesPurchased())
					ownedItems.get(a).rechargeAll();
			}
		}
	}

	public static StoreItem[] addOwnedItems(StoreItem[] items)
	{
		for (int a = 0; a < items.length; a++)
		{
			boolean added = false;

			// check - do we already have this?
			for (int b = 0; b < ownedItems.size(); b++)
			{
				// how to handle items we already own?
				if (ownedItems.get(b).itemType() == items[a].itemType() && ownedItems.get(b).id() == items[a].id())
				{
					// check - do not add multiple of same classes or items
					if (items[a].itemType() == DefinitionGlobal.ITEM_TYPE_PLAYER_CLASS)
					{
						Log.d("owneditems", "did not add item: already had " + items[a].name());
						added = true;
						break;
					}

					// do not add multiple of same items, upgrade existing
					// charges if applicable
					else if (items[a].itemType() == DefinitionGlobal.ITEM_TYPE_ITEM)
					{
						ownedItems.get(b).setChargesPurchased(items[a].chargesPurchased());

						added = true;
						Log.d("owneditems", "updated charges for " + ownedItems.get(b).name());
					}

					// check - do not add multiple of bound equipment
					else if (items[a].availForANewPlayer() == 1)
					{
						Log.d("owneditems", "did not add starting item: already had " + items[a].name());
						added = true;
						break;
					}

					// increment amounts of other items we already have
					else
					{
						Log.d("owneditems", "did not add " + items[a].name() + " amount is now "
							+ (ownedItems.get(b).amount() + 1));
						ownedItems.get(b).setAmount(ownedItems.get(b).amount() + 1);
						added = true;
						break;
					}
				}
			}

			// brand new
			if (!added)
			{

				if (items[a].amount() < 1)
					items[a].setAmount(1);

				ownedItems.add(items[a]);

				Log.d("owneditems", "added item: " + items[a].name() + " (" + items[a].amount() + ")");
			}
		}

		StoreItem[] si = new StoreItem[ownedItems.size()];
		for (int a = 0; a < ownedItems.size(); a++)
		{
			si[a] = ownedItems.get(a);
		}
		return si;
	}

	public static void addStartingRunesForClass(int cls, Context context)
	{
		Log.d("equipabilities", "addStartingRunesforClass: " + cls);

		addOwnedItems(Helper.getStartingRunesForClass(cls, context));

	}

	public static int getIndexOfItemTypeId(int type, int id)
	{
		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (ownedItems.get(a).itemType() == type && ownedItems.get(a).id() == id)
				return a;
		}
		return -1;
	}

	public static void payForRechargeItem(int itemId)
	{
		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_ITEM && ownedItems.get(a).id() == itemId)
				ownedItems.get(a).addCharge();
		}
	}

	public static int costToRechargeItem(int itemId)
	{
		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_ITEM && ownedItems.get(a).id() == itemId)
				return Math.round((float) ownedItems.get(a).value() * DefinitionGlobal.ITEM_RECHARGE_PERCENT_OF_COST);
		}
		return 0;
	}

	public static void setChargesPucrchasedOfItemByOwnedIndex(int index, int c)
	{
		ownedItems.get(index).setChargesPurchased(c);
	}
	
	public static void giveItemStartingCharge(int itemId)
	{
		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_ITEM
				&& ownedItems.get(a).id() == itemId)
				ownedItems.get(a).addCharge();
		}
	}

	public static void rechargeItems(int item1, int item2)
	{
		if (item1 >= 0)
		{
			if ((ItemItem) getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ITEM, item1) != null)
				if (((ItemItem) getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ITEM, item1)).rechargesAtEndOfRoundFlag() == 1)
				{
					for (int a = 0; a < ownedItems.size(); a++)
					{
						if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_ITEM
							&& ownedItems.get(a).id() == item1)
							ownedItems.get(a).rechargeAll();
					}
				}

		}

		if (item2 >= 0)
		{
			if ((ItemItem) getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ITEM, item2) != null)
				if (((ItemItem) getItemByTypeId(DefinitionGlobal.ITEM_TYPE_ITEM, item2)).rechargesAtEndOfRoundFlag() == 1)
				{
					for (int a = 0; a < ownedItems.size(); a++)
					{
						if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_ITEM
							&& ownedItems.get(a).id() == item2)
							ownedItems.get(a).rechargeAll();
					}
				}
		}
	}

	public static int getItemImage(int itemType, int id)
	{

		int img = 0;

		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (ownedItems.get(a).itemType() == itemType && ownedItems.get(a).id() == id)
			{
				if (ownedItems.get(a).animationImageResource() >= 0)
					img = ownedItems.get(a).animationImageResource();
				else
					img = ownedItems.get(a).imageResource();

				break;
			}
		}
		return img;
	}

	public static void useItemCharge(int itemId)
	{
		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_ITEM && ownedItems.get(a).id() == itemId)
			{
				ownedItems.get(a).useCharge();
				Log.d("UseItem", "used charge for "
					+ DefinitionItems.itemdata[ownedItems.get(a).id()][DefinitionItems.ITEM_NAME][0] + ", "
					+ ownedItems.get(a).chargesLeft() + " left");
			}
		}
	}

	public static StoreItem getItemByTypeId(int itemType, int id)
	{
		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (ownedItems.get(a).itemType() == itemType && ownedItems.get(a).id() == id)
			{
				return ownedItems.get(a);
			}
		}

		return null;
	}

	public static int[] getChargesOfItemId(int itemId)
	{
		int[] charges =
		{ 0, 0 };
		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_ITEM && ownedItems.get(a).id() == itemId)
			{
				charges[0] = ownedItems.get(a).chargesLeft();
				charges[1] = ownedItems.get(a).chargesPurchased();
				break;
			}
		}
		return charges;
	}

	public static void addAllItems(Context context)
	{
		// weapons
		for (int a = 0; a < DefinitionWeapons.WEAPON_NAMES.length; a++)
		{
			ownedItems.add(new ItemWeapon(DefinitionGlobal.ITEM_TYPE_WEAPON, a, context));
		}

		// armor
		for (int a = 0; a < DefinitionArmor.ARMOR_NAMES.length; a++)
		{
			ownedItems.add(new ItemArmor(DefinitionGlobal.ITEM_TYPE_ARMOR, a, context));
		}

		// items
		for (int a = 0; a < DefinitionItems.itemdata.length; a++)
		{
			ownedItems.add(new ItemItem(DefinitionGlobal.ITEM_TYPE_ITEM, a, context));
		}

		// abilities
		for (int a = 0; a < DefinitionRunes.runeData.length; a++)
		{
			ownedItems.add(new ItemRune(DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY, a, context));
		}

		// classes
		for (int a = 0; a < DefinitionClasses.CLASS_NAMES.length; a++)
		{
			ownedItems.add(new ItemClass(DefinitionGlobal.ITEM_TYPE_PLAYER_CLASS, a, context));
		}

		_gold = 50000;
	}

	public static void addDefaultItems(int[] itemIds, Context context)
	{
		boolean hadWeapon = false;
		boolean hadHelm = false;
		boolean hadChest = false;
		boolean hadShoes = false;
		boolean hadTrinket = false;
		boolean hadItem1 = false;
		boolean hadItem2 = false;
		boolean hadRune1 = false;
		boolean hadRune2 = false;

		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_WEAPON)
			{
				if (ownedItems.get(a).id() == itemIds[0])
					hadWeapon = true;
			}

			if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_ARMOR)
			{
				if (ownedItems.get(a).id() == itemIds[1])
					hadHelm = true;
			}

			if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_ARMOR)
			{
				if (ownedItems.get(a).id() == itemIds[2])
					hadChest = true;
			}

			if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_ARMOR)
			{
				if (ownedItems.get(a).id() == itemIds[3])
					hadShoes = true;
			}

			if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_ARMOR)
			{
				if (ownedItems.get(a).id() == itemIds[4])
					hadTrinket = true;
			}

			if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_ITEM)
			{
				if (ownedItems.get(a).id() == itemIds[5])
					hadItem1 = true;
			}

			if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_ITEM)
			{
				if (ownedItems.get(a).id() == itemIds[6])
					hadItem2 = true;
			}

			if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY)
			{
				if (ownedItems.get(a).id() == itemIds[7])
					hadRune1 = true;
			}

			if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY)
			{
				if (ownedItems.get(a).id() == itemIds[8])
					hadRune2 = true;
			}
		}

		if (!hadWeapon && itemIds[0] >= 0)
			ownedItems.add(new ItemWeapon(DefinitionGlobal.ITEM_TYPE_WEAPON, itemIds[0], context));

		if (!hadHelm && itemIds[1] >= 0)
			ownedItems.add(new ItemArmor(DefinitionGlobal.ITEM_TYPE_ARMOR, itemIds[1], context));

		if (!hadChest && itemIds[2] >= 0)
			ownedItems.add(new ItemArmor(DefinitionGlobal.ITEM_TYPE_ARMOR, itemIds[2], context));

		if (!hadShoes && itemIds[3] >= 0)
			ownedItems.add(new ItemArmor(DefinitionGlobal.ITEM_TYPE_ARMOR, itemIds[3], context));

		if (!hadTrinket && itemIds[4] >= 0)
			ownedItems.add(new ItemArmor(DefinitionGlobal.ITEM_TYPE_ARMOR, itemIds[4], context));

		if (!hadItem1 && itemIds[5] >= 0)
			ownedItems.add(new ItemItem(DefinitionGlobal.ITEM_TYPE_ITEM, itemIds[5], context));

		if (!hadItem2 && itemIds[6] >= 0)
			ownedItems.add(new ItemItem(DefinitionGlobal.ITEM_TYPE_ITEM, itemIds[6], context));

		if (!hadRune1 && itemIds[7] >= 0)
			ownedItems.add(new ItemRune(DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY, itemIds[7], context));

		if (!hadRune2 && itemIds[8] >= 0)
			ownedItems.add(new ItemRune(DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY, itemIds[8], context));

	}

	public static void sellItem(int itemType, int id, Context context)
	{
		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (ownedItems.get(a).itemType() == itemType && ownedItems.get(a).id() == id)
			{
				ownedItems.remove(a);
			}
		}
	}

	public static StoreItem getItemClone(int itemType, int id, Context context)
	{
		StoreItem i = null;

		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (ownedItems.get(a).itemType() == itemType && ownedItems.get(a).id() == id)
			{

				Log.d("owneditems", "getting a clone: " + ownedItems.get(a).name() + ", type " + itemType + ", id "
					+ id);

				if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_ARMOR)
				{
					i = new ItemArmor(DefinitionGlobal.ITEM_TYPE_ARMOR, ownedItems.get(a).id(), context);
				}
				else if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_WEAPON)
				{
					i = new ItemWeapon(DefinitionGlobal.ITEM_TYPE_WEAPON, ownedItems.get(a).id(), context);
				}
				else if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_ITEM)
				{
					i = new ItemItem(DefinitionGlobal.ITEM_TYPE_ITEM, ownedItems.get(a).id(), context);
					i.setChargesPurchased(ownedItems.get(a).chargesPurchased());
				}
				else if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY)
				{
					i = new ItemRune(DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY, ownedItems.get(a).id(), context);
				}

				Log.d("owneditems", "there are " + ownedItems.get(a).amount() + " owned");

				break;
			}
		}

		return i;
	}

	public static ItemRune removeItemRune(int id)
	{
		ItemRune r = null;
		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY && ownedItems.get(a).id() == id)
			{
				r = (ItemRune) ownedItems.remove(a);
			}
		}
		return r;
	}

	public static ArrayList<ItemRune> getRunesForClass(Player player)
	{
		ArrayList<Integer> runeIds = new ArrayList<Integer>();
		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (player.checkPlayerHasAbility(ownedItems.get(a).id()))
				continue;

			if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY)
			{
				if (ownedItems.get(a).canUseWithClassId(player.playerClass()))
				{
					runeIds.add(a);
				}
			}
		}

		ArrayList<ItemRune> runes = new ArrayList<ItemRune>();
		for (int a = 0; a < runeIds.size(); a++)
		{
			runes.add((ItemRune) ownedItems.get(runeIds.get(a)));
		}

		return runes;
	}

	public static ArrayList<StoreItem> getAllItemsForSlot(int slot)
	{
		Log.d("ownedItems", "getAllItemsForSlot (" + slot + ")...");

		ArrayList<StoreItem> items = new ArrayList<StoreItem>();

		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (ownedItems.get(a).equippableSlots() == null)
				continue;

			if (Helper.intArrayContainsInt(ownedItems.get(a).equippableSlots(), slot))
			{
				items.add(ownedItems.get(a));
				Log.d("ownedItems", "added " + ownedItems.get(a).name());
			}
		}
		return items;
	}

	public static ArrayList<StoreItem> getAllItemsForTypeSlot(int type, int slot, int sortState, int minRank, int pClass)
	{
		Log.d("owneditems", "getallitemsforslot: type=" + type + ", slot=" + slot);

		ArrayList<StoreItem> items = new ArrayList<StoreItem>();

		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_ARMOR)
			{
				Log.d("owneditems", "storeItems, id: " + ownedItems.get(a).name() + ", " + ownedItems.get(a).id());

				if (Helper.intArrayContainsInt(DefinitionArmor.ARMOR_SLOT[ownedItems.get(a).id()], slot))
				{
					boolean classAllowed = true;
					if (sortState == 1)
					{
						if (DefinitionArmor.ARMOR_MIN_LEVEL_TO_USE[ownedItems.get(a).id()] > minRank)
							continue;

						classAllowed = false;
						for (int b = 0; b < DefinitionArmor.ARMOR_FOR_CLASS_ONLY[ownedItems.get(a).id()].length; b++)
						{
							if (DefinitionArmor.ARMOR_FOR_CLASS_ONLY[ownedItems.get(a).id()][b] == -1
								|| DefinitionArmor.ARMOR_FOR_CLASS_ONLY[ownedItems.get(a).id()][b] == pClass)
							{
								classAllowed = true;
								break;
							}
						}
					}
					if (classAllowed)
						items.add(ownedItems.get(a));
				}
			}
			else if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_WEAPON)
			{
				if (Helper.intArrayContainsInt(DefinitionGlobal.EQUIP_SLOT_WEAPON, slot))
				{
					boolean classAllowed = true;
					if (sortState == 1)
					{
						if (DefinitionWeapons.WEAPON_MIN_LEVEL_TO_USE[ownedItems.get(a).id()] > minRank)
							continue;

						classAllowed = false;
						for (int b = 0; b < DefinitionWeapons.WEAPON_ONLY_FOR_CLASSES[ownedItems.get(a).id()].length; b++)
						{
							if (DefinitionWeapons.WEAPON_ONLY_FOR_CLASSES[ownedItems.get(a).id()][b] == -1
								|| DefinitionWeapons.WEAPON_ONLY_FOR_CLASSES[ownedItems.get(a).id()][b] == pClass)
							{
								classAllowed = true;
								break;
							}
						}
					}
					if (classAllowed)
						items.add(ownedItems.get(a));
				}
			}
			else if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_ITEM)
			{
				if (Helper.intArrayContainsInt(DefinitionGlobal.EQUIP_SLOT_ITEM, slot))
				{
					items.add(ownedItems.get(a));
				}
			}
		}

		return items;
	}
	
	public static ArrayList<Integer> getAllClassIds()
	{
		ArrayList<Integer> classIds = new ArrayList<Integer>();
		
		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_PLAYER_CLASS)
			{
				classIds.add(ownedItems.get(a).id());
			}
		}

		return classIds;
	}

	public static ArrayList<ItemClass> getAllClasses()
	{
		ArrayList<ItemClass> items = new ArrayList<ItemClass>();

		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (ownedItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_PLAYER_CLASS)
			{
				items.add((ItemClass) ownedItems.get(a));
			}
		}

		return items;
	}

	public static ArrayList<StoreItem> getAllItemsOfType(int t)
	{
		ArrayList<StoreItem> items = new ArrayList<StoreItem>();

		for (int a = 0; a < ownedItems.size(); a++)
		{
			if (ownedItems.get(a).itemType() == t)
			{
				items.add(ownedItems.get(a));
			}
		}

		return items;
	}

	public static int gold()
	{
		return _gold;
	}

	public static void setGold(int gold)
	{
		_gold = gold;
	}

	public static void updateGold(int amt)
	{
		_gold += amt;
	}

}
