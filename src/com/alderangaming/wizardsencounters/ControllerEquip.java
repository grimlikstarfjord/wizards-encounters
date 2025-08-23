package com.alderangaming.wizardsencounters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ControllerEquip extends Activity
{

//	private OwnedItems ownedItems;
	private Player player;
	private int equipSlot = -1;
	private int equipType = -1;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		if (!DBHandler.isOpen(getApplicationContext()))
			DBHandler.open(getApplicationContext());

		Bundle b = getIntent().getExtras();

		//ownedItems = (OwnedItems) b.getSerializable("ownedItems");
		player = (Player) b.getSerializable("player");
		equipSlot = b.getInt("slot");
		equipType = b.getInt("type");

		setContentView(R.layout.equip);

		setupViews();

		makeItemsList();

		clearCurItem();
		clearNewItem();

		updateCurItem();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (DBHandler.updatePlayer(player))
			{
				DBHandler.close();
			}

			Intent resultIntent = new Intent();
			Bundle b = new Bundle();

		//	b.putSerializable("ownedItems", ownedItems);
			b.putSerializable("player", player);

			resultIntent.putExtras(b);

			setResult(Activity.RESULT_OK, resultIntent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setupViews()
	{
		backButton = (Button) findViewById(R.id.equipBackButton);
		backButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (DBHandler.updatePlayer(player))
				{
					DBHandler.close();
				}

				Intent resultIntent = new Intent();
				Bundle b = new Bundle();

			//	b.putSerializable("ownedItems", ownedItems);
				b.putSerializable("player", player);

				resultIntent.putExtras(b);

				setResult(Activity.RESULT_OK, resultIntent);
				finish();
			}

		});
		equipButton = (Button) findViewById(R.id.equipButton);
		equipButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				player.equipItemBySlot(itemsList.get(itemsListIndex).id(), equipSlot);

				clearCurItem();
								
				updateCurItem();
				
				updateItemsList();
				
			}
		});

		equipTextView = (TextView) findViewById(R.id.EquipLabel);

		equipTextView.setText("Equip " + DefinitionGlobal.EQUIP_SLOT_NAMES[equipSlot]);

		curItemName = (TextView) findViewById(R.id.curItemName);
		curItemStat1 = (TextView) findViewById(R.id.curItemStat1);
		curItemStat2 = (TextView) findViewById(R.id.curItemStat2);
		curItemStat3 = (TextView) findViewById(R.id.curItemStat3);
		curItemStat4 = (TextView) findViewById(R.id.curItemStat4);
		curItemDescription = (TextView) findViewById(R.id.curItemDescription);
		newItemDescription = (TextView) findViewById(R.id.newItemDescription);
		newItemName = (TextView) findViewById(R.id.newItemName);
		newItemStat1 = (TextView) findViewById(R.id.newItemStat1);
		newItemStat2 = (TextView) findViewById(R.id.newItemStat2);
		newItemStat3 = (TextView) findViewById(R.id.newItemStat3);
		newItemStat4 = (TextView) findViewById(R.id.newItemStat4);
		curItemImage = (ImageView) findViewById(R.id.curItemImage);
		newItemImage = (ImageView) findViewById(R.id.newItemImage);

		itemsListView = (ListView) findViewById(R.id.equipListView);
		itemsListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3)
			{
				clearNewItem();
				
				itemsListIndex = position;
				newItemName.setText(itemsList.get(position).name());

				updateNewItem(itemsList.get(position).id());
			}
		});

	}

	private void clearCurItem()
	{
		curItemName.setText("(none)");
		curItemImage.setImageResource(0);
		curItemStat1.setText("");
		curItemStat2.setText("");
		curItemStat3.setText("");
		curItemStat4.setText("");
		curItemDescription.setText("");
	}

	private void clearNewItem()
	{
		newItemName.setText("");
		newItemImage.setImageResource(0);
		newItemStat1.setText("");
		newItemStat2.setText("");
		newItemStat3.setText("");
		newItemStat4.setText("");
		newItemDescription.setText("");
	}

	private void updateItemsList()
	{
		if (equipType == DefinitionGlobal.ITEM_TYPE_ITEM)
		{
			itemsList = OwnedItems.getItemsToSell();
		}
		else
			itemsList = OwnedItems.getAllItemsForTypeSlot(equipType, equipSlot);

		String[] eqNames = new String[itemsList.size()];
		String[] eqDescriptions = new String[itemsList.size()];
		int[] eqImages = new int[itemsList.size()];

		equipAdapterData.clear();

		for (int i = 0; i < eqNames.length; i++)
		{
			if (equipType == DefinitionGlobal.ITEM_TYPE_ITEM)
			{
				eqNames[i] =
					itemsList.get(i).name();
				
				//this sometimes causes charges text to double display
				// + " (" + itemsList.get(i).chargesLeft() + "/" + itemsList.get(i).chargesPurchased() + ")"
			}
			else
				eqNames[i] = itemsList.get(i).name();

			eqDescriptions[i] = itemsList.get(i).description();
			eqImages[i] = itemsList.get(i).imageResource();

			equipAdapterData.add(Helper.createEquipMap(eqNames[i], eqDescriptions[i], eqImages[i]));
		}

		if (equipListAdapter != null)
		{
			equipListAdapter.notifyDataSetChanged();
			itemsListView.invalidate();
		}
	}

	private void makeItemsList()
	{
		String[] fromKeys = new String[]
		{ "equipName", "equipDescription", "equipImage" };

		int[] toIds = new int[]
		{ R.id.equipListName, R.id.equipListDescription, R.id.equipListImage };

		updateItemsList();

		equipListAdapter = new SimpleAdapter(this, equipAdapterData, R.layout.equiplistitem, fromKeys, toIds);
		itemsListView.setAdapter(equipListAdapter);

		updateItemsList();
	}

	private void updateNewItem(int itemId)
	{
		if (equipSlot == DefinitionGlobal.EQUIP_SLOT_WEAPON[0])
		{
			newItemStat1.setText("Dmg: " + DefinitionWeapons.WEAPON_MIN_DAMAGE[itemId]
				+ "-" + DefinitionWeapons.WEAPON_MAX_DAMAGE[itemId]);
			newItemStat2.setText("Hit %: " + DefinitionWeapons.WEAPON_HIT_CHANCE[itemId]);
			newItemStat3.setText("Crit %: "
				+ DefinitionWeapons.WEAPON_CRIT_CHANCE[itemId]);
			newItemStat4.setText("Stun %: "
				+ DefinitionWeapons.WEAPON_STUN_CHANCE[itemId]);
			newItemDescription.setText(DefinitionWeapons.WEAPON_DESCRIPTIONS[itemId]);
			
			newItemImage.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_WEAPON,
				itemId));
		}
		else if (equipSlot == DefinitionGlobal.EQUIP_SLOT_HELM[0]
			|| equipSlot == DefinitionGlobal.EQUIP_SLOT_CHEST[0]
			|| equipSlot == DefinitionGlobal.EQUIP_SLOT_SHOES[0]
			|| equipSlot == DefinitionGlobal.EQUIP_SLOT_TRINKET[0])
		{
			newItemImage.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_ARMOR,
				itemId));
			
			newItemStat1.setText("Block: "
				+ DefinitionArmor.ARMOR_BLOCK_DAMAGE[itemId][0] + "-"
				+ DefinitionArmor.ARMOR_BLOCK_DAMAGE[itemId][1]);

			ArrayList<String> mods = new ArrayList<String>();

			if (DefinitionArmor.ARMOR_MODIFIES[itemId][0] > 0)
				mods.add("Dodge: +" + DefinitionArmor.ARMOR_MODIFIES[itemId][0]);

			if (DefinitionArmor.ARMOR_MODIFIES[itemId][1] > 0)
				mods.add("Init: +" + DefinitionArmor.ARMOR_MODIFIES[itemId][1]);

			if (DefinitionArmor.ARMOR_MODIFIES[itemId][2] > 0)
				mods.add("Abl %: +" + DefinitionArmor.ARMOR_MODIFIES[itemId][2]);

			if (DefinitionArmor.ARMOR_MODIFIES[itemId][3] > 0)
				mods.add("Strength: +" + DefinitionArmor.ARMOR_MODIFIES[itemId][3]);

			if (DefinitionArmor.ARMOR_MODIFIES[itemId][4] > 0)
				mods.add("Reaction: +" + DefinitionArmor.ARMOR_MODIFIES[itemId][4]);

			if (DefinitionArmor.ARMOR_MODIFIES[itemId][5] > 0)
				mods.add("Knowledge: +" + DefinitionArmor.ARMOR_MODIFIES[itemId][5]);

			if (DefinitionArmor.ARMOR_MODIFIES[itemId][6] > 0)
				mods.add("Magelore: +" + DefinitionArmor.ARMOR_MODIFIES[itemId][6]);

			if (DefinitionArmor.ARMOR_MODIFIES[itemId][7] > 0)
				mods.add("Luck: +" + DefinitionArmor.ARMOR_MODIFIES[itemId][7]);

			if (mods.size() > 0)
				newItemStat2.setText(mods.get(0));

			if (mods.size() > 1)
				newItemStat3.setText(mods.get(1));

			if (mods.size() > 2)
				newItemStat4.setText(mods.get(2));

			String runeText = "";
			String addHPText = "";
			String addAPText = "";

			if (DefinitionArmor.ARMOR_GRANTS_RUNE_ID[itemId] > 0)
			{
				runeText =
					"Grants Rune: "
						+ DefinitionRunes.runeData[DefinitionArmor.ARMOR_GRANTS_RUNE_ID[itemId]][DefinitionRunes.RUNE_NAMES][0];
			}
			if (DefinitionArmor.ARMOR_ADDS_HP[itemId] > 0)
			{
				addHPText = ", +" + DefinitionArmor.ARMOR_ADDS_HP[itemId] + " HP";
			}
			if (DefinitionArmor.ARMOR_ADDS_AP[itemId] > 0)
			{
				addAPText = ", +" + DefinitionArmor.ARMOR_ADDS_AP[itemId] + " AP";
			}

			newItemDescription.setText(runeText+DefinitionArmor.ARMOR_DESCRIPTIONS[itemId] + addHPText + addAPText);
		}

		else if (equipSlot == DefinitionGlobal.EQUIP_SLOT_ITEM[0]
			|| equipSlot == DefinitionGlobal.EQUIP_SLOT_ITEM[1])
		{
			newItemName.setText(DefinitionItems.ITEM_NAME[itemId]);
			newItemDescription.setText(DefinitionItems.ITEM_DESCRIPTION[itemId]);
			newItemImage.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_ITEM,
				itemId));
		}
	
	}
	
	private void updateCurItem()
	{
		if (equipSlot == DefinitionGlobal.EQUIP_SLOT_WEAPON[0])
		{
			if (player.equippedWeapon() < 0)
			{
				clearCurItem();
			}
			else
			{
				curItemName.setText(DefinitionWeapons.WEAPON_NAMES[player.equippedWeapon()]);

				curItemStat1.setText("Dmg: " + DefinitionWeapons.WEAPON_MIN_DAMAGE[player.equippedWeapon()] + "-"
					+ DefinitionWeapons.WEAPON_MAX_DAMAGE[player.equippedWeapon()]);
				curItemStat2.setText("Hit %: " + DefinitionWeapons.WEAPON_HIT_CHANCE[player.equippedWeapon()]);
				curItemStat3.setText("Crit %: " + DefinitionWeapons.WEAPON_CRIT_CHANCE[player.equippedWeapon()]);
				curItemStat4.setText("Stun %: " + DefinitionWeapons.WEAPON_STUN_CHANCE[player.equippedWeapon()]);
				curItemDescription.setText(DefinitionWeapons.WEAPON_DESCRIPTIONS[player.equippedWeapon()]);

				curItemImage.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_WEAPON,
					player.equippedWeapon()));
			}
		}
		else if (equipSlot == DefinitionGlobal.EQUIP_SLOT_HELM[0])
		{
			if (player.equippedArmorSlot1() < 0)
			{
				clearCurItem();
			}
			else
			{
				
				curItemName.setText(DefinitionArmor.ARMOR_NAMES[player.equippedArmorSlot1()]);

				curItemStat1.setText("Block: " + DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot1()][0]
					+ "-" + DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot1()][1]);

				ArrayList<String> mods = new ArrayList<String>();

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][0] > 0)
					mods.add("Dodge: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][0]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][1] > 0)
					mods.add("Init: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][1]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][2] > 0)
					mods.add("Abl %: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][2]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][3] > 0)
					mods.add("Strength: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][3]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][4] > 0)
					mods.add("Reaction: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][4]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][5] > 0)
					mods.add("Knowledge: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][5]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][6] > 0)
					mods.add("Magelore: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][6]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][7] > 0)
					mods.add("Luck: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][7]);

				if (mods.size() > 0)
					curItemStat2.setText(mods.get(0));

				if (mods.size() > 1)
					curItemStat3.setText(mods.get(1));

				if (mods.size() > 2)
					curItemStat4.setText(mods.get(2));

				String runeText = "";
				String addHPText = "";
				String addAPText = "";

				if (DefinitionArmor.ARMOR_GRANTS_RUNE_ID[player.equippedArmorSlot1()] > 0)
				{
					runeText =
						", Grants Rune: "
							+ DefinitionRunes.runeData[DefinitionArmor.ARMOR_GRANTS_RUNE_ID[player.equippedArmorSlot1()]][DefinitionRunes.RUNE_NAMES][0];
				}
				if (DefinitionArmor.ARMOR_ADDS_HP[player.equippedArmorSlot1()] > 0)
				{
					addHPText = ", +" + DefinitionArmor.ARMOR_ADDS_HP[player.equippedArmorSlot1()] + " HP";
				}
				if (DefinitionArmor.ARMOR_ADDS_AP[player.equippedArmorSlot1()] > 0)
				{
					addAPText = ", +" + DefinitionArmor.ARMOR_ADDS_AP[player.equippedArmorSlot1()] + " AP";
				}

				curItemDescription.setText(DefinitionArmor.ARMOR_DESCRIPTIONS[player.equippedArmorSlot1()] + runeText
					+ addHPText + addAPText);

				curItemImage.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_ARMOR,
					player.equippedArmorSlot1()));
			}
		}
		else if (equipSlot == DefinitionGlobal.EQUIP_SLOT_CHEST[0])
		{
			if (player.equippedArmorSlot2() < 0)
			{
				clearCurItem();
			}
			else
			{
				
				curItemName.setText(DefinitionArmor.ARMOR_NAMES[player.equippedArmorSlot2()]);

				curItemStat1.setText("Block: " + DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot2()][0]
					+ "-" + DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot2()][1]);

				ArrayList<String> mods = new ArrayList<String>();

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][0] > 0)
					mods.add("Dodge: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][0]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][1] > 0)
					mods.add("Init: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][1]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][2] > 0)
					mods.add("Abl %: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][2]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][3] > 0)
					mods.add("Strength: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][3]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][4] > 0)
					mods.add("Reaction: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][4]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][5] > 0)
					mods.add("Knowledge: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][5]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][6] > 0)
					mods.add("Magelore: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][6]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][7] > 0)
					mods.add("Luck: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][7]);

				if (mods.size() > 0)
					curItemStat2.setText(mods.get(0));

				if (mods.size() > 1)
					curItemStat3.setText(mods.get(1));

				if (mods.size() > 2)
					curItemStat4.setText(mods.get(2));

				String runeText = "";
				String addHPText = "";
				String addAPText = "";

				if (DefinitionArmor.ARMOR_GRANTS_RUNE_ID[player.equippedArmorSlot2()] > 0)
				{
					runeText =
						", Grants Rune: "
							+ DefinitionRunes.runeData[DefinitionArmor.ARMOR_GRANTS_RUNE_ID[player.equippedArmorSlot2()]][DefinitionRunes.RUNE_NAMES][0];
				}
				if (DefinitionArmor.ARMOR_ADDS_HP[player.equippedArmorSlot2()] > 0)
				{
					addHPText = ", +" + DefinitionArmor.ARMOR_ADDS_HP[player.equippedArmorSlot2()] + " HP";
				}
				if (DefinitionArmor.ARMOR_ADDS_AP[player.equippedArmorSlot2()] > 0)
				{
					addAPText = ", +" + DefinitionArmor.ARMOR_ADDS_AP[player.equippedArmorSlot2()] + " AP";
				}

				curItemDescription.setText(DefinitionArmor.ARMOR_DESCRIPTIONS[player.equippedArmorSlot2()] + runeText
					+ addHPText + addAPText);

				curItemImage.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_ARMOR,
					player.equippedArmorSlot2()));
			}
		}
		else if (equipSlot == DefinitionGlobal.EQUIP_SLOT_SHOES[0])
		{
			if (player.equippedArmorSlot3() < 0)
			{
				clearCurItem();
			}
			else
			{
				
				curItemName.setText(DefinitionArmor.ARMOR_NAMES[player.equippedArmorSlot3()]);

				curItemStat1.setText("Block: " + DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot3()][0]
					+ "-" + DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot3()][1]);

				ArrayList<String> mods = new ArrayList<String>();

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][0] > 0)
					mods.add("Dodge: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][0]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][1] > 0)
					mods.add("Init: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][1]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][2] > 0)
					mods.add("Abl %: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][2]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][3] > 0)
					mods.add("Strength: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][3]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][4] > 0)
					mods.add("Reaction: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][4]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][5] > 0)
					mods.add("Knowledge: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][5]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][6] > 0)
					mods.add("Magelore: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][6]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][7] > 0)
					mods.add("Luck: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][7]);

				if (mods.size() > 0)
					curItemStat2.setText(mods.get(0));

				if (mods.size() > 1)
					curItemStat3.setText(mods.get(1));

				if (mods.size() > 2)
					curItemStat4.setText(mods.get(2));

				String runeText = "";
				String addHPText = "";
				String addAPText = "";

				if (DefinitionArmor.ARMOR_GRANTS_RUNE_ID[player.equippedArmorSlot3()] > 0)
				{
					runeText =
						", Grants Rune: "
							+ DefinitionRunes.runeData[DefinitionArmor.ARMOR_GRANTS_RUNE_ID[player.equippedArmorSlot3()]][DefinitionRunes.RUNE_NAMES][0];
				}
				if (DefinitionArmor.ARMOR_ADDS_HP[player.equippedArmorSlot3()] > 0)
				{
					addHPText = ", +" + DefinitionArmor.ARMOR_ADDS_HP[player.equippedArmorSlot3()] + " HP";
				}
				if (DefinitionArmor.ARMOR_ADDS_AP[player.equippedArmorSlot3()] > 0)
				{
					addAPText = ", +" + DefinitionArmor.ARMOR_ADDS_AP[player.equippedArmorSlot3()] + " AP";
				}

				curItemDescription.setText(DefinitionArmor.ARMOR_DESCRIPTIONS[player.equippedArmorSlot3()] + runeText
					+ addHPText + addAPText);

				curItemImage.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_ARMOR,
					player.equippedArmorSlot3()));
			}
		}
		else if (equipSlot == DefinitionGlobal.EQUIP_SLOT_TRINKET[0])
		{
			if (player.equippedArmorSlot4() < 0)
			{
				clearCurItem();
			}
			else
			{
				
				curItemName.setText(DefinitionArmor.ARMOR_NAMES[player.equippedArmorSlot4()]);

				curItemStat1.setText("Block: " + DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot4()][0]
					+ "-" + DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot4()][1]);

				ArrayList<String> mods = new ArrayList<String>();

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][0] > 0)
					mods.add("Dodge: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][0]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][1] > 0)
					mods.add("Init: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][1]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][2] > 0)
					mods.add("Abl %: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][2]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][3] > 0)
					mods.add("Strength: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][3]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][4] > 0)
					mods.add("Reaction: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][4]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][5] > 0)
					mods.add("Knowledge: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][5]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][6] > 0)
					mods.add("Magelore: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][6]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][7] > 0)
					mods.add("Luck: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][7]);

				if (mods.size() > 0)
					curItemStat2.setText(mods.get(0));

				if (mods.size() > 1)
					curItemStat3.setText(mods.get(1));

				if (mods.size() > 2)
					curItemStat4.setText(mods.get(2));

				String runeText = "";
				String addHPText = "";
				String addAPText = "";

				if (DefinitionArmor.ARMOR_GRANTS_RUNE_ID[player.equippedArmorSlot4()] > 0)
				{
					runeText =
						"Grants Rune: "
							+ DefinitionRunes.runeData[DefinitionArmor.ARMOR_GRANTS_RUNE_ID[player.equippedArmorSlot4()]][DefinitionRunes.RUNE_NAMES][0];
				}
				if (DefinitionArmor.ARMOR_ADDS_HP[player.equippedArmorSlot4()] > 0)
				{
					addHPText = ", +" + DefinitionArmor.ARMOR_ADDS_HP[player.equippedArmorSlot4()] + " HP";
				}
				if (DefinitionArmor.ARMOR_ADDS_AP[player.equippedArmorSlot4()] > 0)
				{
					addAPText = ", +" + DefinitionArmor.ARMOR_ADDS_AP[player.equippedArmorSlot4()] + " AP";
				}

				curItemDescription.setText(runeText+DefinitionArmor.ARMOR_DESCRIPTIONS[player.equippedArmorSlot4()] + addHPText + addAPText);

				curItemImage.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_ARMOR,
					player.equippedArmorSlot4()));
			}
		}
		else if (equipSlot == DefinitionGlobal.EQUIP_SLOT_ITEM[0])
		{
			if (player.equippedItemSlot1() < 0)
			{
				clearCurItem();
			}
			else
			{
				curItemName.setText(DefinitionItems.ITEM_NAME[player.equippedItemSlot1()]);
				curItemDescription.setText(DefinitionItems.ITEM_DESCRIPTION[player.equippedItemSlot1()]);
				curItemImage.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_ITEM,
					player.equippedItemSlot1()));
			}
		}
		else if (equipSlot == DefinitionGlobal.EQUIP_SLOT_ITEM[1])
		{
			if (player.equippedItemSlot2() < 0)
			{
				clearCurItem();
			}
			else
			{
				curItemName.setText(DefinitionItems.ITEM_NAME[player.equippedItemSlot2()]);
				curItemDescription.setText(DefinitionItems.ITEM_DESCRIPTION[player.equippedItemSlot2()]);
				curItemImage.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_ITEM,
					player.equippedItemSlot2()));
			}
		}
	}

	Button backButton;
	TextView equipTextView;
	ImageView curItemImage;
	ImageView newItemImage;
	TextView curItemName;
	TextView curItemStat1;
	TextView curItemStat2;
	TextView curItemStat3;
	TextView curItemStat4;
	TextView newItemStat1;
	TextView newItemStat2;
	TextView newItemStat3;
	TextView newItemStat4;
	TextView newItemDescription;
	TextView curItemDescription;
	TextView newItemName;
	Button equipButton;
	ListView itemsListView;
	ArrayList<StoreItem> itemsList;
	int itemsListIndex;

	SimpleAdapter equipListAdapter = null;
	List<Map<String, Object>> equipAdapterData = new ArrayList<Map<String, Object>>();
}
