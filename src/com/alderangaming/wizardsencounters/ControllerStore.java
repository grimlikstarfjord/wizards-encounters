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

	private ArrayList<Player> savedPlayers;
	private StoreItems storeItems;
	private int equipTypeNameIndex = 0;

	private int selectedBuyItemIndex = -1;
	private int selectedSellItemIndex = -1;

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
		storeSortNameText = (TextView) findViewById(R.id.storeSortNameText);
		storeSortLevelText = (TextView) findViewById(R.id.storeSortLevelText);
		storeSortGoldText = (TextView) findViewById(R.id.storeSortGoldText);
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
				storeDrawerButton.setText("Close Inventory");
			}
		});
		storeDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener()
		{
			@Override
			public void onDrawerClosed()
			{
				storeCurrentTypeText.setVisibility(View.VISIBLE);
				storeTopSortLayout.setVisibility(View.VISIBLE);
				storeDrawerButton.setText("Open Inventory");
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
					storeSellButton.setText("Recharge");
					storeInventoryTitleText.setText("Select Item To Recharge");
				}
				else if (DefinitionGlobal.EQUIP_TYPE_INDEX[equipTypeNameIndex] == DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY)
				{
					storeSellButton.setText("Sell");
					storeInventoryTitleText.setText("Owned Ability Runes");
				}
				else if (DefinitionGlobal.EQUIP_TYPE_INDEX[equipTypeNameIndex] == DefinitionGlobal.ITEM_TYPE_PLAYER_CLASS)
				{
					storeInventoryTitleText.setText("Owned Class Runes");
				}
				else
				{
					storeSellButton.setText("Sell");
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
		storeSellButton.setText("Sell");
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
		
		Log.d("store","tried to buy "+itemToBuy.name());

		// storeItems.updateStoreInventory(OwnedItems);

		// TODO implement dialog
		if (OwnedItems.gold() < itemToBuy.cost())
		{
			showToast("Not enough gold. Item cost "+itemToBuy.cost());
			Log.d("store","not enough gold");
			return;
		}
		
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
		{ "storeName", "storeDescription", "storeImage", "storeLevel", "storeValue" };

		int[] storeIds = new int[]
		{ R.id.storeListName, R.id.storeListDescription, R.id.storeListImage, R.id.storeListLevel, R.id.storeListCost };

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
			sellItemsList.add(sellItem);
		}

		toSellAdapterData.clear();

		for (int a = 0; a < sellItemsList.size(); a++)
		{
			toSellAdapterData.add(Helper.createStoreMap(sellItemsList.get(a).name, sellItemsList.get(a).description,
				sellItemsList.get(a).image, sellItemsList.get(a).level + "", sellItemsList.get(a).price + ""));
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
			buyItemsList = storeItems.getUnownedStoreItemsBySlot(equipTypeNameIndex);
		}
		// abilities
		else if (DefinitionGlobal.EQUIP_TYPE_INDEX[equipTypeNameIndex] == DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY)
		{
			buyItemsList = storeItems.getStoreItemsByType(DefinitionGlobal.EQUIP_TYPE_INDEX[equipTypeNameIndex]);
		}
		// items
		else if (DefinitionGlobal.EQUIP_TYPE_INDEX[equipTypeNameIndex] == DefinitionGlobal.ITEM_TYPE_ITEM)
		{
			buyItemsList = storeItems.getItemsForSale(equipTypeNameIndex);
		}
		// class runes
		else if (DefinitionGlobal.EQUIP_TYPE_INDEX[equipTypeNameIndex] == DefinitionGlobal.ITEM_TYPE_PLAYER_CLASS)
		{
			buyItemsList = storeItems.getClassRunesForSale();
		}

		for (int a = 0; a < buyItemsList.size(); a++)
		{
			Log.d("store", "added " + buyItemsList.get(a).name() + " to buy list");
			
			toBuyAdapterData.add(Helper.createStoreMap(buyItemsList.get(a).name(), buyItemsList.get(a).description(),
				buyItemsList.get(a).imageResource(), buyItemsList.get(a).minLevel() + "", buyItemsList.get(a).cost()
					+ ""));
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

	private TextView storeCurrentTypeText;
	private TextView storeSortNameText;
	private TextView storeSortLevelText;
	private TextView storeSortGoldText;
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
