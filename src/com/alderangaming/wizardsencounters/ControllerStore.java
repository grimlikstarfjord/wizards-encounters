package com.alderangaming.wizardsencounters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.Toast;

public class ControllerStore extends Activity
{
	// private OwnedItems OwnedItems;

	private ArrayList<Player> savedPlayers = null;
	private StoreItems storeItems;
	private int equipTypeNameIndex = 0;

	private int selectedBuyItemIndex = -1;
	private int selectedSellItemIndex = -1;
	
	
	private int sortState = 0; // 0=usable+affordable, 1=usable, 2=all

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Bundle b = getIntent().getExtras();

		if (!DBHandler.isOpen(getApplicationContext()))
			DBHandler.open(getApplicationContext());

		savedPlayers = (ArrayList<Player>) b.getSerializable("savedPlayers");
		// OwnedItems = (OwnedItems) b.getSerializable("OwnedItems");

		setContentView(R.layout.store);

		setupStore();
		setupViews();
		setupAdapters();
		updateData();
	}

	private void setupStore()
	{
		storeItems = new StoreItems(getApplicationContext());
	}

	private void setupViews()
	{

		storeCurrentTypeText = (TextView) findViewById(R.id.storeCurrentTypeText);
		storeSortButton = (Button) findViewById(R.id.storeSortButton);
		storeSortButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				sortState++;
				if(sortState > 2)
					sortState = 0;
				
				updateViews();
				updateBuyListData();
			}

		});
		storeCurrentGoldText2 = (TextView) findViewById(R.id.storeCurrentGoldText2);
		storeCurrentGoldText = (TextView) findViewById(R.id.storeCurrentGoldText);
		storeInventoryTitleText = (TextView) findViewById(R.id.storeCurrentPlayerText);

		storeInventoryTitleText.setText("Select Item To Sell");

		storeDrawerButton = (Button) findViewById(R.id.storeDrawerButton);
		storeTopLayout = (LinearLayout) findViewById(R.id.storeTopLayout);
		storeTopSortLayout = (LinearLayout) findViewById(R.id.storeTopSortLayout);
		storeDrawer = (SlidingDrawer) findViewById(R.id.storeDrawer);
		storeDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener()
		{
			@Override
			public void onDrawerOpened()
			{
				storeCurrentTypeText.setVisibility(View.INVISIBLE);
				storeTopSortLayout.setVisibility(View.INVISIBLE);
				storeDrawerButton.setBackgroundResource(R.drawable.buttoncloseinventory);
			}
		});
		storeDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener()
		{
			@Override
			public void onDrawerClosed()
			{
				storeCurrentTypeText.setVisibility(View.VISIBLE);
				storeTopSortLayout.setVisibility(View.VISIBLE);
				storeDrawerButton.setBackgroundResource(R.drawable.buttonopeninventory);
			}
		});

		storeLeaveButton = (Button) findViewById(R.id.storeLeaveButton);
		storeLeaveButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (DBHandler.updateAllPlayers(savedPlayers))
				{
					DBHandler.close();
				}

				Intent resultIntent = new Intent();
				Bundle b = new Bundle();

				// b.putSerializable("OwnedItems", OwnedItems);

				resultIntent.putExtras(b);

				setResult(Activity.RESULT_OK, resultIntent);
				finish();
			}

		});

		storeNextTypeButton = (Button) findViewById(R.id.storeNextTypeButton);
		storeNextTypeButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				equipTypeNameIndex++;
				if (equipTypeNameIndex >= DefinitionGlobal.EQUIP_TYPE_NAMES.length)
				{
					equipTypeNameIndex = 0;
				}

				if (DefinitionGlobal.EQUIP_TYPE_INDEX[equipTypeNameIndex] == DefinitionGlobal.ITEM_TYPE_ITEM)
				{
					storeSellButton.setBackgroundResource(R.drawable.buttonrecharge);
					storeInventoryTitleText.setText("Select Item To Recharge");
				}
				else if (DefinitionGlobal.EQUIP_TYPE_INDEX[equipTypeNameIndex] == DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY)
				{
					storeSellButton.setBackgroundResource(R.drawable.buttonselldisabled);
					storeInventoryTitleText.setText("Owned Ability Runes");
				}
				else if (DefinitionGlobal.EQUIP_TYPE_INDEX[equipTypeNameIndex] == DefinitionGlobal.ITEM_TYPE_PLAYER_CLASS)
				{
					storeSellButton.setBackgroundResource(R.drawable.buttonselldisabled);
					storeInventoryTitleText.setText("Owned Class Runes");
				}
				else
				{
					storeSellButton.setBackgroundResource(R.drawable.buttonsell);
					storeInventoryTitleText.setText("Select Item To Sell");
				}

				updateData();
			}

		});

		storeBuyButton = (Button) findViewById(R.id.storeBuyButton);
		storeBuyButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (selectedBuyItemIndex < 0)
				{
					showToast("no item selected");
				}
				else
				{
					buySomething();
					clearSelection();
				}
			}
		});

		storeSellButton = (Button) findViewById(R.id.storeSellButton);
		storeSellButton.setBackgroundResource(R.drawable.buttonsell);
		storeSellButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Log.d("store", "sell button clicked");
				if (selectedSellItemIndex < 0)
				{
					showToast("no item selected");
				}
				else if (sellItemsList.get(selectedSellItemIndex).bound)
				{
					showToast("You cannot sell this item - it is bound to you.");
				}
				else if (sellItemsList.get(selectedSellItemIndex).itemType == DefinitionGlobal.ITEM_TYPE_ITEM)
				{
					if (OwnedItems.getChargesOfItemId(sellItemsList.get(selectedSellItemIndex).id)[0] >= OwnedItems
						.getChargesOfItemId(sellItemsList.get(selectedSellItemIndex).id)[1])
					{
						showToast("This item is already fully charged.");
					}
					else
					{
						Log.d(
							"store",
							"owned gold:" + OwnedItems.gold() + " cost: "
								+ OwnedItems.costToRechargeItem(sellItemsList.get(selectedSellItemIndex).id));
						if (OwnedItems.gold() >= OwnedItems.costToRechargeItem(sellItemsList.get(selectedSellItemIndex).id))
						{
							OwnedItems.payForRechargeItem(sellItemsList.get(selectedSellItemIndex).id);
							OwnedItems.updateGold(-OwnedItems.costToRechargeItem(sellItemsList
								.get(selectedSellItemIndex).id));
							DBHandler.updateGlobalStats(OwnedItems.gold());
							DBHandler.updateOwnedItems(OwnedItems.getOwnedItems());
							SoundManager.playSound(SoundManager.RECHARGESOUND, true);
							showToast("Item gained 1 charge.");
							updateData();
						}
						else
						{
							showToast("You can't afford to recharge this item.");
						}
					}

				}
				else if (sellItemsList.get(selectedSellItemIndex).itemType == DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY
					|| sellItemsList.get(selectedSellItemIndex).itemType == DefinitionGlobal.ITEM_TYPE_PLAYER_CLASS)
				{
					showToast("Runes cannot be sold.");
				}
				else
				{

					sellSomething();
					clearSelection();
				}
			}
		});

		storeToBuyList = (ListView) findViewById(R.id.storeToBuyList);
		storeToBuyList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3)
			{
				for (int a = 0; a < parent.getChildCount(); a++)
				{
					parent.getChildAt(a).setBackgroundColor(Color.DKGRAY);
				}

				view.setBackgroundColor(Color.BLUE);

				selectedBuyItemIndex = position;
			}
		});

		storeToSellList = (ListView) findViewById(R.id.storeToSellList);
		storeToSellList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3)
			{
				for (int a = 0; a < parent.getChildCount(); a++)
				{
					parent.getChildAt(a).setBackgroundColor(Color.DKGRAY);
				}

				view.setBackgroundColor(Color.BLUE);

				selectedSellItemIndex = position;
			}
		});
	}

	private void clearSelection()
	{
		for (int a = 0; a < storeToBuyList.getChildCount(); a++)
		{
			storeToBuyList.getChildAt(a).setBackgroundColor(Color.DKGRAY);
		}

		for (int a = 0; a < storeToSellList.getChildCount(); a++)
		{
			storeToSellList.getChildAt(a).setBackgroundColor(Color.DKGRAY);
		}
	}

	private void showToast(String text)
	{
		Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	private void buySomething()
	{
		StoreItem itemToBuy = buyItemsList.get(selectedBuyItemIndex);

		Log.d("store", "tried to buy " + itemToBuy.name());

		// storeItems.updateStoreInventory(OwnedItems);

		if (OwnedItems.gold() < itemToBuy.cost())
		{
			showToast("Not enough gold. Item cost " + itemToBuy.cost());
			Log.d("store", "not enough gold");
			return;
		}
		
		SoundManager.playSound(SoundManager.BUYSOUND, true);

		showToast("Item purchased!");

		// deduct gold
		OwnedItems.updateGold(-itemToBuy.cost());

		itemToBuy.setChargesPurchased(itemToBuy.chargesPurchased() + 1);

		// add to OwnedItems
		OwnedItems.addOwnedItems(new StoreItem[]
		{ itemToBuy });
		OwnedItems.addChargeToItem(itemToBuy);

		// save to db
		if (DBHandler.updateGlobalStats(OwnedItems.gold()))
		{
			Log.d("store", "updated global stats with " + OwnedItems.gold() + " gold");
		}
		else
		{
			Log.d("store", "error saving current gold to GlobalStats");
		}

		if (DBHandler.updateOwnedItems(OwnedItems.getOwnedItems()))
		{
			Log.d("store", "database updated, purchased item saved: " + itemToBuy.name());
			Log.d("store", "player now owns " + OwnedItems.getAmountOwnedOfTypeId(itemToBuy.itemType(), itemToBuy.id())
				+ " of them");
		}
		else
		{
			Log.d("store", "error saving new purchased items to db!!!");
		}

		updateData();
	}

	private void sellIt(SellItem itemToSell)
	{
		// storeItems.addItemBackToStore(OwnedItems.getItemByTypeId(itemToSell.itemType,
		// itemToSell.id));
		
		SoundManager.playSound(SoundManager.SELLSOUND, true);

		// add gold
		OwnedItems.updateGold(itemToSell.price);

		// remove one from OwnedItems
		OwnedItems.sellItem(itemToSell.itemType, itemToSell.id, this);

		// save to db
		if (DBHandler.updateGlobalStats(OwnedItems.gold()))
		{
			Log.d("store", "updated global stats with " + OwnedItems.gold() + " gold");
		}
		else
		{
			Log.d("store", "error saving current gold to GlobalStats");
		}
		if (DBHandler.updateOwnedItems(OwnedItems.getOwnedItems()))
		{
			Log.d("store", "database updated, sold item: " + itemToSell.name);
			Log.d("store", "player now owns " + OwnedItems.getAmountOwnedOfTypeId(itemToSell.itemType, itemToSell.id)
				+ " of them");
		}
		else
		{
			Log.d("store", "error saving new purchased items to db!!!");
		}

		showToast("Item sold!");
		updateData();
	}

	private void sellSomething()
	{
		final SellItem itemToSell = sellItemsList.get(selectedSellItemIndex);

		String eqPlayers = "";
		for (int a = 0; a < savedPlayers.size(); a++)
		{
			if (savedPlayers.get(a).hasItemEquipped(itemToSell.itemType, itemToSell.id))
			{
				if (eqPlayers.equals(""))
					eqPlayers = savedPlayers.get(a).name();

				else
					eqPlayers += ", " + savedPlayers.get(a).name();
			}
		}
		if (!eqPlayers.equals(""))
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder
				.setMessage(
					"Warning: This item is currently equipped on: " + eqPlayers
						+ ". Selling this will remove it from them and equip the default item. Sell it?")
				.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						for (int a = 0; a < savedPlayers.size(); a++)
						{
							savedPlayers.get(a).removeEquippedItem(
								OwnedItems.getItemByTypeId(itemToSell.itemType, itemToSell.id));
						}

						sellIt(itemToSell);
					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						return;
					}
				});
			AlertDialog alert = builder.create();
			alert.show();
		}
		else
		{
			sellIt(itemToSell);
		}

	}

	private void setupAdapters()
	{
		// updateData();

		String[] storeKeys = new String[]
		{ "storeName", "storeDescription", "storeImage", "storeLevel", "storeValue", "storeStats", "storeClasses" };

		int[] storeIds =
			new int[]
			{ R.id.storeListName, R.id.storeListDescription, R.id.storeListImage, R.id.storeListLevel,
				R.id.storeListCost, R.id.storeListItemStats, R.id.storeListItemClasses };

		toSellAdapter = new SimpleAdapter(this, toSellAdapterData, R.layout.storelistitem, storeKeys, storeIds);
		toBuyAdapter = new SimpleAdapter(this, toBuyAdapterData, R.layout.storelistitem, storeKeys, storeIds);

		storeToSellList.setAdapter(toSellAdapter);
		storeToBuyList.setAdapter(toBuyAdapter);

		// updateData();
	}

	private void updateSellListData()
	{
		sellItemsList.clear();
		ArrayList<StoreItem> sellTheseThings = null;

		if (DefinitionGlobal.EQUIP_TYPE_INDEX[equipTypeNameIndex] != DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY
			&& DefinitionGlobal.EQUIP_TYPE_INDEX[equipTypeNameIndex] != DefinitionGlobal.ITEM_TYPE_ITEM
			&& DefinitionGlobal.EQUIP_TYPE_INDEX[equipTypeNameIndex] != DefinitionGlobal.ITEM_TYPE_PLAYER_CLASS)
		{
			sellTheseThings = OwnedItems.getAllItemsForSlot(equipTypeNameIndex);
		}
		else if (DefinitionGlobal.EQUIP_TYPE_INDEX[equipTypeNameIndex] == DefinitionGlobal.ITEM_TYPE_ITEM)
		{
			sellTheseThings = OwnedItems.getItemsToSell();
		}
		else if (DefinitionGlobal.EQUIP_TYPE_INDEX[equipTypeNameIndex] == DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY)
		{
			sellTheseThings = OwnedItems.getAllItemsOfType(DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY);
		}
		else if (DefinitionGlobal.EQUIP_TYPE_INDEX[equipTypeNameIndex] == DefinitionGlobal.ITEM_TYPE_PLAYER_CLASS)
		{
			sellTheseThings = OwnedItems.getAllItemsOfType(DefinitionGlobal.ITEM_TYPE_PLAYER_CLASS);
		}

		for (int a = 0; a < sellTheseThings.size(); a++)
		{
			SellItem sellItem = new SellItem();
			sellItem.name = sellTheseThings.get(a).name();
			sellItem.description = sellTheseThings.get(a).description();
			sellItem.id = sellTheseThings.get(a).id();
			sellItem.itemType = sellTheseThings.get(a).itemType();
			sellItem.level = sellTheseThings.get(a).minLevel();
			sellItem.price = sellTheseThings.get(a).value();
			sellItem.image = sellTheseThings.get(a).imageResource();
			sellItem.bound = sellTheseThings.get(a).bound();
			
			sellItemsList.add(sellItem);
		}

		toSellAdapterData.clear();

		for (int a = 0; a < sellItemsList.size(); a++)
		{

			String statsString = "";
			String classData = "";
			if (sellItemsList.get(a).itemType == DefinitionGlobal.ITEM_TYPE_WEAPON)
			{
				String atTypes = "[";
				for (int b = 0; b < DefinitionWeapons.WEAPON_ATTACK_TYPES[sellItemsList.get(a).id].length; b++)
				{
					if (b != 0)
						atTypes += ", ";
					atTypes +=
						DefinitionAttackTypes.ATTACK_TYPE_NAMES[DefinitionWeapons.WEAPON_ATTACK_TYPES[sellItemsList
							.get(a).id][b]];
				}
				atTypes += "]";
				String dmg =
					"Dmg: " + DefinitionWeapons.WEAPON_MIN_DAMAGE[sellItemsList.get(a).id] + "-"
						+ DefinitionWeapons.WEAPON_MAX_DAMAGE[sellItemsList.get(a).id];
				String hit = "Hit: " + DefinitionWeapons.WEAPON_HIT_CHANCE[sellItemsList.get(a).id] + "%";
				String stun = "Stun: " + DefinitionWeapons.WEAPON_STUN_CHANCE[sellItemsList.get(a).id] + "%";
				String crit = "Crit: " + DefinitionWeapons.WEAPON_CRIT_CHANCE[sellItemsList.get(a).id] + "%";
				statsString = atTypes + " " + dmg + " " + hit + " " + stun + " " + crit;
			}
			else if (sellItemsList.get(a).itemType == DefinitionGlobal.ITEM_TYPE_ARMOR)
			{
				String block = "";
				if (DefinitionArmor.ARMOR_BLOCK_DAMAGE[sellItemsList.get(a).id][2] > 0)
				{
					block =
						DefinitionArmor.ARMOR_BLOCK_DAMAGE[sellItemsList.get(a).id][0]+"% to Block: " + DefinitionArmor.ARMOR_BLOCK_DAMAGE[sellItemsList.get(a).id][1] + "-"
							+ DefinitionArmor.ARMOR_BLOCK_DAMAGE[sellItemsList.get(a).id][2] + " Wpn Dmg";
				}
				String modDodge = "";
				if (DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][0] > 0)
				{
					modDodge = "+" + DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][0] + "% Dodge";
				}
				String modInit = "";
				if (DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][1] > 0)
				{
					modInit = "+" + DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][1] + "% Init";
				}
				String modStr = "";
				if (DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][3] > 0)
				{
					modStr = "+" + DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][3] + " EXEC";
				}
				else if (DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][3] < 0)
				{
					modStr = "-" + DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][3] + " EXEC";
				}
				String modReac = "";
				if (DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][4] > 0)
				{
					modReac = "+" + DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][4] + " REAC";
				}
				else if (DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][4] < 0)
				{
					modReac = "-" + DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][4] + " REAC";
				}
				String modKnow = "";
				if (DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][5] > 0)
				{
					modKnow = "+" + DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][5] + " KNOW";
				}
				else if (DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][5] < 0)
				{
					modKnow = "-" + DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][5] + " KNOW";
				}
				String modMage = "";
				if (DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][6] > 0)
				{
					modMage = "+" + DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][6] + " MAGE";
				}
				else if (DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][6] < 0)
				{
					modMage = "-" + DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][6] + " MAGE";
				}
				String modLuck = "";
				if (DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][7] > 0)
				{
					modLuck = "+" + DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][7] + " LUCK";
				}
				else if (DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][7] < 0)
				{
					modLuck = "-" + DefinitionArmor.ARMOR_MODIFIES[sellItemsList.get(a).id][7] + " LUCK";
				}
				String modHp = "";
				if (DefinitionArmor.ARMOR_ADDS_HP[sellItemsList.get(a).id] > 0)
				{
					modHp = "+" + DefinitionArmor.ARMOR_ADDS_HP[sellItemsList.get(a).id] + " HP";
				}
				String modAp = "";
				if (DefinitionArmor.ARMOR_ADDS_AP[sellItemsList.get(a).id] > 0)
				{
					modAp = "+" + DefinitionArmor.ARMOR_ADDS_AP[sellItemsList.get(a).id] + " AP";
				}
				String abil = "";
				if (DefinitionArmor.ARMOR_GRANTS_RUNE_ID[sellItemsList.get(a).id] >= 0)
				{
					abil =
						"Grants Rune: "
							+ DefinitionRunes.runeData[DefinitionArmor.ARMOR_GRANTS_RUNE_ID[sellItemsList.get(a).id]][DefinitionRunes.RUNE_NAMES][0]
							+ " AP Cost: "
							+ DefinitionRunes.runeData[DefinitionArmor.ARMOR_GRANTS_RUNE_ID[sellItemsList.get(a).id]][DefinitionRunes.RUNE_AP_COST][0];
				}
				statsString =
					block + " " + modDodge + " " + modInit + " " + modStr + " " + modReac + " " + modKnow + " "
						+ modMage + " " + modLuck + " " + modHp + " " + modAp + " " + abil;

			}
			else if (sellItemsList.get(a).itemType == DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY)
			{
				statsString =
					"AP Cost: " + DefinitionRunes.runeData[sellItemsList.get(a).id][DefinitionRunes.RUNE_AP_COST][0];
			}
			else if (sellItemsList.get(a).itemType == DefinitionGlobal.ITEM_TYPE_PLAYER_CLASS)
			{
				statsString =
					"Class Type: "
						+ DefinitionClassType.CLASS_TYPE_NAME[DefinitionClasses.CLASS_TYPE[sellItemsList.get(a).id]];
			}

			classData =
				"[Classes: " + Helper.getAllowedClassesString(sellItemsList.get(a).itemType, sellItemsList.get(a).id)
					+ "]";

			toSellAdapterData.add(Helper.createStoreMap(sellItemsList.get(a).name, sellItemsList.get(a).description,
				sellItemsList.get(a).image, "lvl " + sellItemsList.get(a).level + "",
				sellItemsList.get(a).price + "gp", statsString, classData));
		}

		toSellAdapter.notifyDataSetChanged();
		storeToSellList.invalidate();
	}

	private void updateBuyListData()
	{
		Log.d("store", "cleared buy list");
		buyItemsList.clear();
		toBuyAdapterData.clear();

		// load data for non-ability, non-items
		if (DefinitionGlobal.EQUIP_TYPE_INDEX[equipTypeNameIndex] != DefinitionGlobal.ITEM_TYPE_ITEM
			&& DefinitionGlobal.EQUIP_TYPE_INDEX[equipTypeNameIndex] != DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY
			&& DefinitionGlobal.EQUIP_TYPE_INDEX[equipTypeNameIndex] != DefinitionGlobal.ITEM_TYPE_PLAYER_CLASS)
		{
			buyItemsList = storeItems.getUnownedStoreItemsBySlot(equipTypeNameIndex, sortState, savedPlayers);
		}
		// abilities
		else if (DefinitionGlobal.EQUIP_TYPE_INDEX[equipTypeNameIndex] == DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY)
		{
			buyItemsList = storeItems.getStoreItemsByType(DefinitionGlobal.EQUIP_TYPE_INDEX[equipTypeNameIndex], sortState, savedPlayers);
		}
		// items
		else if (DefinitionGlobal.EQUIP_TYPE_INDEX[equipTypeNameIndex] == DefinitionGlobal.ITEM_TYPE_ITEM)
		{
			buyItemsList = storeItems.getItemsForSale(equipTypeNameIndex, sortState);
		}
		// class runes
		else if (DefinitionGlobal.EQUIP_TYPE_INDEX[equipTypeNameIndex] == DefinitionGlobal.ITEM_TYPE_PLAYER_CLASS)
		{
			buyItemsList = storeItems.getClassRunesForSale(sortState);
		}

		for (int a = 0; a < buyItemsList.size(); a++)
		{
			Log.d("store", "added " + buyItemsList.get(a).name() + " to buy list");
			String statsString = "";
			String classData = "";
			if (buyItemsList.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_WEAPON)
			{
				String atTypes = "[";
				for (int b = 0; b < DefinitionWeapons.WEAPON_ATTACK_TYPES[buyItemsList.get(a).id()].length; b++)
				{
					if (b != 0)
						atTypes += ", ";
					atTypes +=
						DefinitionAttackTypes.ATTACK_TYPE_NAMES[DefinitionWeapons.WEAPON_ATTACK_TYPES[buyItemsList.get(
							a).id()][b]];
				}
				atTypes += "]";
				String dmg =
					"Dmg: " + DefinitionWeapons.WEAPON_MIN_DAMAGE[buyItemsList.get(a).id()] + "-"
						+ DefinitionWeapons.WEAPON_MAX_DAMAGE[buyItemsList.get(a).id()];
				String hit = "Hit: " + DefinitionWeapons.WEAPON_HIT_CHANCE[buyItemsList.get(a).id()] + "%";
				String stun = "Stun: " + DefinitionWeapons.WEAPON_STUN_CHANCE[buyItemsList.get(a).id()] + "%";
				String crit = "Crit: " + DefinitionWeapons.WEAPON_CRIT_CHANCE[buyItemsList.get(a).id()] + "%";
				statsString = atTypes + " " + dmg + " " + hit + " " + stun + " " + crit;
			}
			else if (buyItemsList.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_ARMOR)
			{
				String block = "";
				if (DefinitionArmor.ARMOR_BLOCK_DAMAGE[buyItemsList.get(a).id()][2] > 0)
				{
					block =
							 DefinitionArmor.ARMOR_BLOCK_DAMAGE[buyItemsList.get(a).id()][0]+"% to Block: " + DefinitionArmor.ARMOR_BLOCK_DAMAGE[buyItemsList.get(a).id()][1] + "-"
							+ DefinitionArmor.ARMOR_BLOCK_DAMAGE[buyItemsList.get(a).id()][2] + " Wpn Dmg";
				}
				String modDodge = "";
				if (DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][0] > 0)
				{
					modDodge = "+" + DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][0] + "% Dodge";
				}
				String modInit = "";
				if (DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][1] > 0)
				{
					modInit = "+" + DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][1] + "% Init";
				}
				String modStr = "";
				if (DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][3] > 0)
				{
					modStr = "+" + DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][3] + " EXEC";
				}
				else if (DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][3] < 0)
				{
					modStr = "-" + DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][3] + " EXEC";
				}
				String modReac = "";
				if (DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][4] > 0)
				{
					modReac = "+" + DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][4] + " REAC";
				}
				else if (DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][4] < 0)
				{
					modReac = "-" + DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][4] + " REAC";
				}
				String modKnow = "";
				if (DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][5] > 0)
				{
					modKnow = "+" + DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][5] + " KNOW";
				}
				else if (DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][5] < 0)
				{
					modKnow = "-" + DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][5] + " KNOW";
				}
				String modMage = "";
				if (DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][6] > 0)
				{
					modMage = "+" + DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][6] + " MAGE";
				}
				else if (DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][6] < 0)
				{
					modMage = "-" + DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][6] + " MAGE";
				}
				String modLuck = "";
				if (DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][7] > 0)
				{
					modLuck = "+" + DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][7] + " LUCK";
				}
				else if (DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][7] < 0)
				{
					modLuck = "-" + DefinitionArmor.ARMOR_MODIFIES[buyItemsList.get(a).id()][7] + " LUCK";
				}
				String modHp = "";
				if (DefinitionArmor.ARMOR_ADDS_HP[buyItemsList.get(a).id()] > 0)
				{
					modHp = "[+" + DefinitionArmor.ARMOR_ADDS_HP[buyItemsList.get(a).id()] + " HP]";
				}
				String modAp = "";
				if (DefinitionArmor.ARMOR_ADDS_AP[buyItemsList.get(a).id()] > 0)
				{
					modAp = "[+" + DefinitionArmor.ARMOR_ADDS_AP[buyItemsList.get(a).id()] + " AP]";
				}
				String abil = "";
				if (DefinitionArmor.ARMOR_GRANTS_RUNE_ID[buyItemsList.get(a).id()] >= 0)
				{
					abil =
						"[Grants Rune: "
							+ DefinitionRunes.runeData[DefinitionArmor.ARMOR_GRANTS_RUNE_ID[buyItemsList.get(a).id()]][DefinitionRunes.RUNE_NAMES][0]
							+ ", AP Cost: "
							+ DefinitionRunes.runeData[DefinitionArmor.ARMOR_GRANTS_RUNE_ID[buyItemsList.get(a).id()]][DefinitionRunes.RUNE_AP_COST][0]
							+ "]";
				}
				statsString =
					block + " " + modDodge + " " + modInit + " " + modStr + " " + modReac + " " + modKnow + " "
						+ modMage + " " + modLuck + " " + modHp + " " + modAp + " " + abil;
			}
			else if (buyItemsList.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY)
			{
				statsString =
					"AP Cost: " + DefinitionRunes.runeData[buyItemsList.get(a).id()][DefinitionRunes.RUNE_AP_COST][0];
			}
			else if (buyItemsList.get(a).itemType() == DefinitionGlobal.ITEM_TYPE_PLAYER_CLASS)
			{
				statsString =
					"Class Type: "
						+ DefinitionClassType.CLASS_TYPE_NAME[DefinitionClasses.CLASS_TYPE[buyItemsList.get(a).id()]];
				buyItemsList.get(a).setCost(DefinitionGlobal.CLASS_RUNE_COST);
			}

			classData =
				"[Classes: " + Helper.getAllowedClassesString(buyItemsList.get(a).itemType(), buyItemsList.get(a).id())
					+ "]";

			toBuyAdapterData.add(Helper.createStoreMap(buyItemsList.get(a).name(), buyItemsList.get(a).description(),
				buyItemsList.get(a).imageResource(), "lvl " + buyItemsList.get(a).minLevel() + "", buyItemsList.get(a)
					.cost() + "gp", statsString, classData));
		}

		if (toBuyAdapter != null)
		{
			toBuyAdapter.notifyDataSetChanged();
			storeToBuyList.invalidate();
		}
	}

	private void updateData()
	{
		updateSellListData();
		updateBuyListData();
		updateViews();
	}

	private void updateViews()
	{
		if (equipTypeNameIndex + 1 >= DefinitionGlobal.EQUIP_TYPE_NAMES.length)
		{
			storeNextTypeButton.setText(DefinitionGlobal.EQUIP_TYPE_NAMES[0] + " >");
		}
		else
		{
			storeNextTypeButton.setText(DefinitionGlobal.EQUIP_TYPE_NAMES[equipTypeNameIndex + 1] + " >");
		}
		
		if(sortState == 0)
		{
			storeSortButton.setText("Only Usable + Affordable");
		}
		else if(sortState == 1)
		{
			storeSortButton.setText("Only Usable");
		}
		else
		{
			storeSortButton.setText("Showing All");
		}

		storeCurrentTypeText.setText(DefinitionGlobal.EQUIP_TYPE_NAMES[equipTypeNameIndex]);

		storeCurrentGoldText.setText(OwnedItems.gold() + "gp");
		storeCurrentGoldText2.setText(OwnedItems.gold() + "gp");
	}

	public class SellItem
	{
		String name = "";
		String description = "";
		int id = 0;
		int itemType = 0;
		int level = 0;
		int price = 0;
		int image = 0;
		boolean bound = false;
	}

	private LinearLayout storeTopLayout;
	private LinearLayout storeTopSortLayout;
	private SlidingDrawer storeDrawer;

	private Button storeDrawerButton;
	private Button storeLeaveButton;
	private Button storeNextTypeButton;
	private Button storeBuyButton;
	private Button storeSellButton;
	private Button storeSortButton;

	private TextView storeCurrentTypeText;
	private TextView storeCurrentGoldText;
	private TextView storeCurrentGoldText2;
	private TextView storeInventoryTitleText;

	private ListView storeToBuyList;
	private ListView storeToSellList;

	private SimpleAdapter toSellAdapter;
	private SimpleAdapter toBuyAdapter;

	private List<Map<String, Object>> toSellAdapterData = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> toBuyAdapterData = new ArrayList<Map<String, Object>>();

	ArrayList<SellItem> sellItemsList = new ArrayList<SellItem>();
	ArrayList<StoreItem> buyItemsList = new ArrayList<StoreItem>();
}
