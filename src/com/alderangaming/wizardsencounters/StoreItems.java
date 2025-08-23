package com.alderangaming.wizardsencounters;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

public class StoreItems
{

	private ArrayList<StoreItem> _storeItems = new ArrayList<StoreItem>();

	public StoreItems(Context context)
	{
		addDefaultStoreItems(context);
	}

	public void addItemBackToStore(StoreItem s)
	{
		_storeItems.add(s);
	}

	private void addDefaultStoreItems(Context context)
	{
		// weapons
		for (int a = 0; a < DefinitionWeapons.WEAPON_NAMES.length; a++)
		{
			if (DefinitionWeapons.WEAPON_SHOWS_IN_STORE[a] == 1)
			{
				ItemWeapon w = new ItemWeapon(DefinitionGlobal.ITEM_TYPE_WEAPON, a, context);
				_storeItems.add(w);
				Log.d("store", "added to store: " + w.name());
			}
		}

		// armor
		for (int a = 0; a < DefinitionArmor.ARMOR_NAMES.length; a++)
		{
			if (DefinitionArmor.ARMOR_SHOWS_IN_STORE[a] == 1)
			{
				ItemArmor r = new ItemArmor(DefinitionGlobal.ITEM_TYPE_ARMOR, a, context);
				_storeItems.add(r);
				Log.d("store", "added to store: " + r.name());
			}
		}

		// items
		for (int a = 0; a < DefinitionItems.itemdata.length; a++)
		{

			if ((Integer) DefinitionItems.itemdata[a][DefinitionItems.ITEM_SHOWS_IN_STORE][0] == 1)
			{
				ItemItem i = new ItemItem(DefinitionGlobal.ITEM_TYPE_ITEM, a, context);

				// update prices and charges data for this item based on what
				// player owns
				StoreItem playerItem = OwnedItems.getPlayerOwnsThis(i);
				if (playerItem != null)
				{
					playerItem = (ItemItem) playerItem;

					if (playerItem.chargesPurchased() < i.maxCharges())
					{
						// we can show an upgraded charge version of this item

						_storeItems.add(i);

						Log.d("store", "added to store: " + i.name());
					}
				}
				else
				{
					i.setChargesLeft(1);
					i.setChargesPurchased(1);
					i.setCost(i.value() * 1 * i.increaseChargeCostMultiple());

					_storeItems.add(i);
				}
			}
		}

		// runes
		for (int a = 0; a < DefinitionRunes.runeData.length; a++)
		{
			// if player owns this item, don't add it
			if (OwnedItems.getPlayerOwnsThis(DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY, a))
				continue;

			if ((Integer) DefinitionRunes.runeData[a][DefinitionRunes.RUNE_SHOWS_IN_STORE][0] == 1)
			{
				ItemRune r = new ItemRune(DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY, a, context);
				_storeItems.add(r);
				Log.d("store", "added to store: " + r.name());
			}
		}

		// class runes
		for (int a = 0; a < DefinitionClasses.CLASS_NAMES.length; a++)
		{
			// if player owns this item, don't add it
			if (OwnedItems.getPlayerOwnsThis(DefinitionGlobal.ITEM_TYPE_PLAYER_CLASS, a))
				continue;

			if (DefinitionClasses.CLASS_SHOWS_IN_STORE[a] == 1)
			{
				ItemClass r = new ItemClass(DefinitionGlobal.ITEM_TYPE_PLAYER_CLASS, a, context);
				_storeItems.add(r);
				Log.d("store", "added to store: " + r.name());
			}
		}
	}

	public void addStoreItems(ArrayList<StoreItem> items)
	{
		for (int a = 0; a < items.size(); a++)
		{
			_storeItems.add(items.get(a));
		}
	}

	// returns list of abilities for sale in store
	public ArrayList<StoreItem> getStoreItemsByType(int type, int sortState, ArrayList<Player> savedPlayers)
	{
		ArrayList<StoreItem> st = new ArrayList<StoreItem>();
		for (int a = 0; a < _storeItems.size(); a++)
		{
			if (_storeItems.get(a).itemType() == type)
				if (!OwnedItems.getPlayerOwnsThis(_storeItems.get(a).itemType(), _storeItems.get(a).id()))
				{
					boolean okToAdd = true;

					// show only avail for classes
					if (sortState == 0 || sortState == 1)
					{
						okToAdd = false;
						
						for (int b = 0; b < savedPlayers.size(); b++)
						{
							if (_storeItems.get(a).canUseWithClassId(-1)
								|| _storeItems.get(a).canUseWithClassId(savedPlayers.get(b).playerClass()))
							{
								okToAdd = true;
								if (sortState == 0)
								{
									okToAdd = false;
									if (_storeItems.get(a).cost() <= OwnedItems.gold())
									{
										okToAdd = true;
									}
								}

								break;
							}
						}

						if (sortState == 0)
						{
							if (_storeItems.get(a).cost() <= OwnedItems.gold())
								okToAdd = true;
						}
					}

					if (okToAdd)
						st.add(_storeItems.get(a));
				}
		}

		return st;
	}

	public ArrayList<StoreItem> getUnownedStoreItemsBySlot(int slot, int sortState, ArrayList<Player> savedPlayers)
	{

		// sortState 0=usable classes and affordable, 1=usable classes, 2=all

		ArrayList<StoreItem> st = new ArrayList<StoreItem>();
		for (int a = 0; a < _storeItems.size(); a++)
		{
			if (Helper.intArrayContainsInt(_storeItems.get(a).equippableSlots(), slot))
			{
				// do not return items that the player already owns
				Log.d("store", "checking - does player own " + _storeItems.get(a).name() + " ?");
				if (!OwnedItems.getPlayerOwnsThis(_storeItems.get(a).itemType(), _storeItems.get(a).id()))
				{
					Log.d("store", ".. NO .. adding to unowned list");

					boolean okToAdd = true;

					// do not add items outside of owned classes
					if (sortState == 0 || sortState == 1)
					{
						okToAdd = false;
						for (int b = 0; b < savedPlayers.size(); b++)
						{
							if (_storeItems.get(a).canUseWithClassId(-1)
								|| _storeItems.get(a).canUseWithClassId(savedPlayers.get(b).playerClass()))
							{
								okToAdd = true;
								if (sortState == 0)
								{
									okToAdd = false;
									if (_storeItems.get(a).cost() <= OwnedItems.gold())
									{
										okToAdd = true;
									}
								}

								break;
							}
						}
					}

					if (okToAdd)
						st.add(_storeItems.get(a));
				}
				else
				{
					Log.d("store", ".. YES .. skipping");
				}
			}
		}

		return st;
	}

	public ArrayList<StoreItem> getClassRunesForSale(int sortState)
	{
		ArrayList<StoreItem> st = new ArrayList<StoreItem>();
		for (int a = 0; a < _storeItems.size(); a++)
		{
			if (_storeItems.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_PLAYER_CLASS)
			{
				if (OwnedItems.getPlayerOwnsThis(DefinitionGlobal.ITEM_TYPE_PLAYER_CLASS, _storeItems.get(a).id()))
				{
					continue;
				}
				else
				{
					boolean okToAdd = true;

					// show only affordable
					if (sortState == 0)
					{
						okToAdd = false;
						if (_storeItems.get(a).cost() <= OwnedItems.gold())
							okToAdd = true;
					}

					if (okToAdd)
						st.add(_storeItems.get(a));
				}
			}
		}
		return st;
	}

	public ArrayList<StoreItem> getItemsForSale(int slot, int sortState)
	{
		ArrayList<StoreItem> st = new ArrayList<StoreItem>();
		for (int a = 0; a < _storeItems.size(); a++)
		{
			if (Helper.intArrayContainsInt(_storeItems.get(a).equippableSlots(), slot))
			{
				StoreItem i = _storeItems.get(a);
				int amtPurchased = 0;
				int mult =
					(Integer) DefinitionItems.itemdata[i.id()][DefinitionItems.ITEM_INCREASE_CHARGE_COST_MULTIPLE][0];

				if (OwnedItems.getPlayerOwnsThis(i.itemType(), i.id()))
				{
					amtPurchased = OwnedItems.getChargesOfItemId(i.id())[1];
				}

				if (amtPurchased >= i.maxCharges())
					continue;

				int cost = i.value();
				if (i.maxCharges() > 1)
				{
					String num = " I";

					if (amtPurchased == 1)
						num = " II";
					else if (amtPurchased == 2)
						num = " III";
					else if (amtPurchased == 3)
						num = " IV";
					else if (amtPurchased == 4)
						num = " V";
					else if (amtPurchased == 5)
						num = " VI";
					else if (amtPurchased == 6)
						num = " VII";
					else if (amtPurchased == 7)
						num = " VIII";
					else if (amtPurchased == 8)
						num = " IX";
					else if (amtPurchased == 9)
						num = " X";

					if (amtPurchased > 0)
						cost = i.value() * (amtPurchased) * mult;

					i.setName(DefinitionItems.itemdata[i.id()][DefinitionItems.ITEM_NAME][0] + num);
				}

				i.setCost(cost);
				i.setChargesPurchased(amtPurchased);

				// show only affordable
				boolean okToAdd = true;
				if (sortState == 0)
				{
					okToAdd = false;
					if (i.cost() <= OwnedItems.gold())
						okToAdd = true;
				}

				if (okToAdd)
					st.add(i);
			}
		}

		return st;
	}

}
