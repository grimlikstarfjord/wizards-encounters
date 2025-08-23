package com.alderangaming.wizardsencounters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ControllerEquip extends Activity
{

	// private OwnedItems ownedItems;
	private Player player;
	private int equipSlot = -1;
	private int equipType = -1;
	private boolean itemSelected = false;
	private Dialog hDialog;
	private int sortState = 1; // 0 = all, 1 = usable

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		if (!DBHandler.isOpen(getApplicationContext()))
			DBHandler.open(getApplicationContext());

		Bundle b = getIntent().getExtras();

		// ownedItems = (OwnedItems) b.getSerializable("ownedItems");
		player = (Player) b.getSerializable("player");
		equipSlot = b.getInt("slot");
		equipType = b.getInt("type");

		setContentView(R.layout.equip);

		setupHelpDialog();

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

			// b.putSerializable("ownedItems", ownedItems);
			b.putSerializable("player", player);

			resultIntent.putExtras(b);

			setResult(Activity.RESULT_OK, resultIntent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void moveNewLayout()
	{
		SoundManager.playSound(SoundManager.EQUIP_ITEM, true);
		Animation a = Animator.getAnimation(Animator.MOVE_EQUIP);
		a.setAnimationListener(new AnimationListener()
		{

			@Override
			public void onAnimationEnd(Animation animation)
			{

				clearNewItem();

				clearCurItem();

				updateCurItem();

				updateItemsList();

			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{

			}

			@Override
			public void onAnimationStart(Animation animation)
			{
				equipTopLayout.bringToFront();
			}

		});
		equipTopLayout.startAnimation(a);
		newItemImage.startAnimation(a);
	}

	private void showHelp()
	{
		hDialog.show();
	}

	private void setupHelpDialog()
	{
		AlertDialog.Builder aboutDialog = new AlertDialog.Builder(this);

		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.help, (ViewGroup) findViewById(R.id.helpRoot));
		TextView helpTextView = (TextView) layout.findViewById(R.id.helpText);
		Button doneButton = (Button) layout.findViewById(R.id.helpDoneButton);
		doneButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				try
				{
					hDialog.dismiss();
				}
				catch (Exception e)
				{
					// who cares I have more important crap in my life to deal
					// with
				}
			}

		});

		if (equipType == DefinitionGlobal.ITEM_TYPE_ARMOR)
		{
			helpTextView.setText(R.string.equiparmorHelpText);
		}
		if (equipType == DefinitionGlobal.ITEM_TYPE_WEAPON)
		{
			helpTextView.setText(R.string.equipweaponHelpText);
		}
		if (equipType == DefinitionGlobal.ITEM_TYPE_ITEM)
		{
			helpTextView.setText(R.string.equipitemHelpText);
		}

		aboutDialog.setView(layout);

		hDialog = aboutDialog.create();
	}
	
	private void updateButtons()
	{
		if(sortState == 1)
			sortButton.setText("Showing Usable");
		else
			sortButton.setText("Showing All");
	}

	private void setupViews()
	{
		Button helpButton = (Button) findViewById(R.id.equipHelpButton);
		helpButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				showHelp();
			}

		});

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

				// b.putSerializable("ownedItems", ownedItems);
				b.putSerializable("player", player);

				resultIntent.putExtras(b);

				setResult(Activity.RESULT_OK, resultIntent);
				finish();
			}

		});
		equipTopLayout = (LinearLayout) findViewById(R.id.topNewItemLayout);
		equipNewLayout = (LinearLayout) findViewById(R.id.equipNewLayout);
		equipNewLayout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (!itemSelected)
					return;

				itemSelected = false;
				moveNewLayout();
				player.equipItemBySlot(itemsList.get(itemsListIndex).id(), equipSlot);
				DBHandler.updateActiveAbilities(player.getActiveAbilities(), player.playerID());
			}
		});

		sortButton = (Button) findViewById(R.id.equipSortButton);
		sortButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				if(sortState == 0)
					sortState = 1;
				else
					sortState = 0;
				
				updateItemsList();
				updateButtons();
			}

		});

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
				if(checkCanEquip(position) == false)
					return;

				SoundManager.playSound(SoundManager.CLICK, true);

				clearNewItem();

				itemSelected = true;

				itemsListIndex = position;
				newItemName.setText(itemsList.get(position).name());

				updateNewItem(itemsList.get(position).id());
			}
		});

	}
	
	private boolean checkCanEquip(int position)
	{
		boolean classAllowed = false;
		if (itemsList.get(position).itemType() == DefinitionGlobal.ITEM_TYPE_WEAPON)
		{
			for (int a = 0; a < DefinitionWeapons.WEAPON_ONLY_FOR_CLASSES[itemsList.get(position).id()].length; a++)
			{
				if (DefinitionWeapons.WEAPON_ONLY_FOR_CLASSES[itemsList.get(position).id()][a] == -1
					|| DefinitionWeapons.WEAPON_ONLY_FOR_CLASSES[itemsList.get(position).id()][a] == player
						.playerClass())
				{
					classAllowed = true;
					break;
				}
			}

			if (!classAllowed)
			{
				showToast("This weapon can't be equipped for your class.");
				return false;
			}

			if (DefinitionWeapons.WEAPON_MIN_LEVEL_TO_USE[itemsList.get(position).id()] > player.rank())
			{
				showToast("This weapon is above your Rank.");
				return false;
			}
		}

		if (itemsList.get(position).itemType() == DefinitionGlobal.ITEM_TYPE_ARMOR)
		{
			for (int a = 0; a < DefinitionArmor.ARMOR_FOR_CLASS_ONLY[itemsList.get(position).id()].length; a++)
			{
				if (DefinitionArmor.ARMOR_FOR_CLASS_ONLY[itemsList.get(position).id()][a] == -1
					|| DefinitionArmor.ARMOR_FOR_CLASS_ONLY[itemsList.get(position).id()][a] == player
						.playerClass())
				{
					classAllowed = true;
					break;
				}
			}

			if (!classAllowed)
			{
				showToast("This armor can't be equipped for your class.");
				return false;
			}

			if (DefinitionArmor.ARMOR_MIN_LEVEL_TO_USE[itemsList.get(position).id()] > player.rank())
			{
				showToast("This armor is above your Rank.");
				return false;
			}
		}
		
		return true;
	}

	private void showToast(String text)
	{
		Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	private void clearCurItem()
	{
		curItemName.setText("(none)");
		curItemImage.setImageResource(0);
		curItemStat1.setText("");
		curItemStat2.setText("");
		curItemStat3.setText("");
		curItemStat4.setText("");

		curItemStat1.setVisibility(View.GONE);
		curItemStat2.setVisibility(View.GONE);
		curItemStat3.setVisibility(View.GONE);
		curItemStat4.setVisibility(View.GONE);

		curItemDescription.setText("");
	}

	private void clearNewItem()
	{
		newItemName.setText("Select Something");
		newItemImage.setImageResource(0);
		newItemStat1.setText("");
		newItemStat2.setText("");
		newItemStat3.setText("");
		newItemStat4.setText("");

		newItemStat1.setVisibility(View.GONE);
		newItemStat2.setVisibility(View.GONE);
		newItemStat3.setVisibility(View.GONE);
		newItemStat4.setVisibility(View.GONE);

		newItemDescription.setText("");
	}

	private void updateItemsList()
	{
		if (equipType == DefinitionGlobal.ITEM_TYPE_ITEM)
		{
			itemsList = OwnedItems.getItemsToSell();
		}
		else
			itemsList = OwnedItems.getAllItemsForTypeSlot(equipType, equipSlot, sortState, player.rank(), player.playerClass());

		String[] eqNames = new String[itemsList.size()];
		String[] eqDescriptions = new String[itemsList.size()];
		int[] eqImages = new int[itemsList.size()];

		equipAdapterData.clear();

		for (int i = 0; i < eqNames.length; i++)
		{
			if (equipType == DefinitionGlobal.ITEM_TYPE_ITEM)
			{
				eqNames[i] = itemsList.get(i).name();

				// this sometimes causes charges text to double display
				// + " (" + itemsList.get(i).chargesLeft() + "/" +
				// itemsList.get(i).chargesPurchased() + ")"
			}
			else
				eqNames[i] = itemsList.get(i).name();

			String classData = "  [Classes: " + Helper.getAllowedClassesString(equipType, itemsList.get(i).id());

			eqDescriptions[i] =
				itemsList.get(i).description() + classData + "]  [Rank: " + itemsList.get(i).minLevel() + "]";
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
			newItemStat1.setVisibility(View.VISIBLE);
			newItemStat2.setVisibility(View.VISIBLE);
			newItemStat3.setVisibility(View.VISIBLE);
			newItemStat4.setVisibility(View.VISIBLE);

			newItemStat1.setText("Dmg: " + DefinitionWeapons.WEAPON_MIN_DAMAGE[itemId] + "-"
				+ DefinitionWeapons.WEAPON_MAX_DAMAGE[itemId]);
			newItemStat2.setText("Hit %: " + DefinitionWeapons.WEAPON_HIT_CHANCE[itemId]);
			newItemStat3.setText("Crit %: " + DefinitionWeapons.WEAPON_CRIT_CHANCE[itemId]);
			newItemStat4.setText("Stun %: " + DefinitionWeapons.WEAPON_STUN_CHANCE[itemId]);
			newItemDescription.setText(DefinitionWeapons.WEAPON_DESCRIPTIONS[itemId]);

			newItemImage.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_WEAPON, itemId));
		}
		else if (equipSlot == DefinitionGlobal.EQUIP_SLOT_HELM[0] || equipSlot == DefinitionGlobal.EQUIP_SLOT_CHEST[0]
			|| equipSlot == DefinitionGlobal.EQUIP_SLOT_SHOES[0] || equipSlot == DefinitionGlobal.EQUIP_SLOT_TRINKET[0])
		{
			newItemImage.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_ARMOR, itemId));

			if (DefinitionArmor.ARMOR_BLOCK_DAMAGE[itemId][2] > 0)
			{
				newItemStat1.setVisibility(View.VISIBLE);
				newItemStat1.setText( DefinitionArmor.ARMOR_BLOCK_DAMAGE[itemId][0] + "% to Block " + DefinitionArmor.ARMOR_BLOCK_DAMAGE[itemId][1] + "-"
					+ DefinitionArmor.ARMOR_BLOCK_DAMAGE[itemId][2]);
			}

			ArrayList<String> mods = new ArrayList<String>();

			if (DefinitionArmor.ARMOR_MODIFIES[itemId][0] > 0)
				mods.add("Dodge: +" + DefinitionArmor.ARMOR_MODIFIES[itemId][0]);

			if (DefinitionArmor.ARMOR_MODIFIES[itemId][1] > 0)
				mods.add("Init: +" + DefinitionArmor.ARMOR_MODIFIES[itemId][1]);

			if (DefinitionArmor.ARMOR_MODIFIES[itemId][2] > 0)
				mods.add("Abl %: +" + DefinitionArmor.ARMOR_MODIFIES[itemId][2]);
			
			if (DefinitionArmor.ARMOR_MODIFIES[itemId][0] < 0)
				mods.add("Dodge: " + DefinitionArmor.ARMOR_MODIFIES[itemId][0]);

			if (DefinitionArmor.ARMOR_MODIFIES[itemId][1] < 0)
				mods.add("Init: " + DefinitionArmor.ARMOR_MODIFIES[itemId][1]);

			if (DefinitionArmor.ARMOR_MODIFIES[itemId][2] < 0)
				mods.add("Abl %: " + DefinitionArmor.ARMOR_MODIFIES[itemId][2]);

			if (DefinitionArmor.ARMOR_MODIFIES[itemId][3] > 0)
				mods.add("Execution: +" + DefinitionArmor.ARMOR_MODIFIES[itemId][3]);
			
			if (DefinitionArmor.ARMOR_MODIFIES[itemId][3] < 0)
				mods.add("Execution: " + DefinitionArmor.ARMOR_MODIFIES[itemId][3]);

			if (DefinitionArmor.ARMOR_MODIFIES[itemId][4] > 0)
				mods.add("Reaction: +" + DefinitionArmor.ARMOR_MODIFIES[itemId][4]);
			
			if (DefinitionArmor.ARMOR_MODIFIES[itemId][4] < 0)
				mods.add("Reaction: " + DefinitionArmor.ARMOR_MODIFIES[itemId][4]);

			if (DefinitionArmor.ARMOR_MODIFIES[itemId][5] > 0)
				mods.add("Knowledge: +" + DefinitionArmor.ARMOR_MODIFIES[itemId][5]);
			
			if (DefinitionArmor.ARMOR_MODIFIES[itemId][5] < 0)
				mods.add("Knowledge: " + DefinitionArmor.ARMOR_MODIFIES[itemId][5]);

			if (DefinitionArmor.ARMOR_MODIFIES[itemId][6] > 0)
				mods.add("Magelore: +" + DefinitionArmor.ARMOR_MODIFIES[itemId][6]);
			
			if (DefinitionArmor.ARMOR_MODIFIES[itemId][6] < 0)
				mods.add("Magelore: " + DefinitionArmor.ARMOR_MODIFIES[itemId][6]);

			if (DefinitionArmor.ARMOR_MODIFIES[itemId][7] > 0)
				mods.add("Luck: +" + DefinitionArmor.ARMOR_MODIFIES[itemId][7]);
			
			if (DefinitionArmor.ARMOR_MODIFIES[itemId][7] < 0)
				mods.add("Luck: " + DefinitionArmor.ARMOR_MODIFIES[itemId][7]);
			
			if (DefinitionArmor.ARMOR_ADDS_HP[itemId] > 0)
			{
				mods.add("+" + DefinitionArmor.ARMOR_ADDS_HP[itemId] + " HP");
			}
			if (DefinitionArmor.ARMOR_ADDS_AP[itemId] > 0)
			{
				mods.add("+" + DefinitionArmor.ARMOR_ADDS_AP[itemId] + " AP");
			}

			if (mods.size() > 0)
			{
				newItemStat2.setVisibility(View.VISIBLE);
				newItemStat2.setText(mods.get(0));
			}

			if (mods.size() > 1)
			{
				newItemStat3.setVisibility(View.VISIBLE);
				newItemStat3.setText(mods.get(1));
			}

			if (mods.size() > 2)
			{
				newItemStat4.setVisibility(View.VISIBLE);
				newItemStat4.setText(mods.get(2));
			}

			String runeText = "";
			String addHPText = "";
			String addAPText = "";

			if (DefinitionArmor.ARMOR_GRANTS_RUNE_ID[itemId] >= 0)
			{
				runeText =
					" AP Cost: "
						+ DefinitionRunes.runeData[DefinitionArmor.ARMOR_GRANTS_RUNE_ID[itemId]][DefinitionRunes.RUNE_AP_COST][0];
			}
			

			newItemDescription.setText(DefinitionArmor.ARMOR_DESCRIPTIONS[itemId] + addHPText + addAPText + runeText);
		}

		else if (equipSlot == DefinitionGlobal.EQUIP_SLOT_ITEM[0] || equipSlot == DefinitionGlobal.EQUIP_SLOT_ITEM[1])
		{
			newItemName.setText((CharSequence) DefinitionItems.itemdata[itemId][DefinitionItems.ITEM_NAME][0]);
			newItemDescription
				.setText((CharSequence) DefinitionItems.itemdata[itemId][DefinitionItems.ITEM_DESCRIPTION][0]);
			newItemImage.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_ITEM, itemId));
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

				curItemStat1.setVisibility(View.VISIBLE);
				curItemStat2.setVisibility(View.VISIBLE);
				curItemStat3.setVisibility(View.VISIBLE);
				curItemStat4.setVisibility(View.VISIBLE);

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

				if (DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot1()][2] > 0)
				{
					curItemStat1.setVisibility(View.VISIBLE);
					curItemStat1.setText(DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot1()][0] + "% to Block " + DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot1()][1]
						+ "-" + DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot1()][2]);
				}

				ArrayList<String> mods = new ArrayList<String>();

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][0] > 0)
					mods.add("Dodge: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][0]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][1] > 0)
					mods.add("Init: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][1]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][2] > 0)
					mods.add("Abl %: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][2]);
				
				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][0] < 0)
					mods.add("Dodge: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][0]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][1] < 0)
					mods.add("Init: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][1]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][2] < 0)
					mods.add("Abl %: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][2]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][3] > 0)
					mods.add("Execution: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][3]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][4] > 0)
					mods.add("Reaction: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][4]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][5] > 0)
					mods.add("Knowledge: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][5]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][6] < 0)
					mods.add("Magelore: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][6]);
				
				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][3] < 0)
					mods.add("Execution: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][3]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][4] < 0)
					mods.add("Reaction: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][4]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][5] < 0)
					mods.add("Knowledge: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][5]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][6] < 0)
					mods.add("Magelore: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][6]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][7] < 0)
					mods.add("Luck: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot1()][7]);

				if (DefinitionArmor.ARMOR_ADDS_HP[player.equippedArmorSlot1()] > 0)
				{
					mods.add("+" + DefinitionArmor.ARMOR_ADDS_HP[player.equippedArmorSlot1()] + " HP");
				}
				if (DefinitionArmor.ARMOR_ADDS_AP[player.equippedArmorSlot1()] > 0)
				{
					mods.add("+" + DefinitionArmor.ARMOR_ADDS_AP[player.equippedArmorSlot1()] + " AP");
				}
				
				if (mods.size() > 0)
				{
					curItemStat2.setVisibility(View.VISIBLE);
					curItemStat2.setText(mods.get(0));
				}

				if (mods.size() > 1)
				{
					curItemStat3.setVisibility(View.VISIBLE);
					curItemStat3.setText(mods.get(1));
				}

				if (mods.size() > 2)
				{
					curItemStat4.setVisibility(View.VISIBLE);
					curItemStat4.setText(mods.get(2));
				}

				String runeText = "";
				String addHPText = "";
				String addAPText = "";
				
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

				if (DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot2()][2] > 0)
				{
					curItemStat1.setVisibility(View.VISIBLE);
					curItemStat1.setText(DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot2()][0] + "% to Block: " + DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot2()][1]
						+ "-" + DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot2()][2]);
				}

				ArrayList<String> mods = new ArrayList<String>();

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][0] > 0)
					mods.add("Dodge: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][0]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][1] > 0)
					mods.add("Init: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][1]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][2] > 0)
					mods.add("Abl %: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][2]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][3] > 0)
					mods.add("Execution: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][3]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][4] > 0)
					mods.add("Reaction: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][4]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][5] > 0)
					mods.add("Knowledge: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][5]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][6] > 0)
					mods.add("Magelore: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][6]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][7] > 0)
					mods.add("Luck: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][7]);
				
				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][0] < 0)
					mods.add("Dodge: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][0]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][1] < 0)
					mods.add("Init: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][1]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][2] < 0)
					mods.add("Abl %: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][2]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][3] < 0)
					mods.add("Execution: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][3]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][4] < 0)
					mods.add("Reaction: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][4]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][5] < 0)
					mods.add("Knowledge: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][5]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][6] < 0)
					mods.add("Magelore: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][6]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][7] < 0)
					mods.add("Luck: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot2()][7]);

				if (DefinitionArmor.ARMOR_ADDS_HP[player.equippedArmorSlot2()] > 0)
				{
					mods.add("+" + DefinitionArmor.ARMOR_ADDS_HP[player.equippedArmorSlot2()] + " HP");
				}
				if (DefinitionArmor.ARMOR_ADDS_AP[player.equippedArmorSlot2()] > 0)
				{
					mods.add("+" + DefinitionArmor.ARMOR_ADDS_AP[player.equippedArmorSlot2()] + " AP");
				}
				
				if (mods.size() > 0)
				{
					curItemStat2.setVisibility(View.VISIBLE);
					curItemStat2.setText(mods.get(0));
				}

				if (mods.size() > 1)
				{
					curItemStat3.setVisibility(View.VISIBLE);
					curItemStat3.setText(mods.get(1));
				}

				if (mods.size() > 2)
				{
					curItemStat4.setVisibility(View.VISIBLE);
					curItemStat4.setText(mods.get(2));
				}

				String runeText = "";
				String addHPText = "";
				String addAPText = "";

				

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

				if (DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot3()][2] > 0)
				{
					curItemStat1.setVisibility(View.VISIBLE);
					curItemStat1.setText(DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot3()][0] + "% to Block: " + DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot3()][1]
						+ "-" + DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot3()][2]);
				}

				ArrayList<String> mods = new ArrayList<String>();

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][0] > 0)
					mods.add("Dodge: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][0]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][1] > 0)
					mods.add("Init: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][1]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][2] > 0)
					mods.add("Abl %: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][2]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][3] > 0)
					mods.add("Execution: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][3]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][4] > 0)
					mods.add("Reaction: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][4]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][5] > 0)
					mods.add("Knowledge: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][5]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][6] > 0)
					mods.add("Magelore: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][6]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][7] > 0)
					mods.add("Luck: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][7]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][0] < 0)
					mods.add("Dodge: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][0]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][1] < 0)
					mods.add("Init: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][1]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][2] < 0)
					mods.add("Abl %: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][2]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][3] < 0)
					mods.add("Execution: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][3]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][4] < 0)
					mods.add("Reaction: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][4]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][5] < 0)
					mods.add("Knowledge: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][5]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][6] < 0)
					mods.add("Magelore: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][6]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][7] < 0)
					mods.add("Luck: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot3()][7]);

				
				if (DefinitionArmor.ARMOR_ADDS_HP[player.equippedArmorSlot3()] > 0)
				{
					mods.add("+" + DefinitionArmor.ARMOR_ADDS_HP[player.equippedArmorSlot3()] + " HP");
				}
				if (DefinitionArmor.ARMOR_ADDS_AP[player.equippedArmorSlot3()] > 0)
				{
					mods.add("+" + DefinitionArmor.ARMOR_ADDS_AP[player.equippedArmorSlot3()] + " AP");
				}
				
				if (mods.size() > 0)
				{
					curItemStat2.setVisibility(View.VISIBLE);
					curItemStat2.setText(mods.get(0));
				}

				if (mods.size() > 1)
				{
					curItemStat3.setVisibility(View.VISIBLE);
					curItemStat3.setText(mods.get(1));
				}

				if (mods.size() > 2)
				{
					curItemStat4.setVisibility(View.VISIBLE);
					curItemStat4.setText(mods.get(2));
				}

				String runeText = "";
				String addHPText = "";
				String addAPText = "";

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

				if (DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot4()][2] > 0)
				{
					curItemStat1.setVisibility(View.VISIBLE);
					curItemStat1.setText(DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot4()][0] + "% to Block: " + DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot4()][1]
						+ "-" + DefinitionArmor.ARMOR_BLOCK_DAMAGE[player.equippedArmorSlot4()][2]);
				}

				ArrayList<String> mods = new ArrayList<String>();

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][0] > 0)
					mods.add("Dodge: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][0]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][1] > 0)
					mods.add("Init: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][1]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][2] > 0)
					mods.add("Abl %: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][2]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][3] > 0)
					mods.add("Execution: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][3]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][4] > 0)
					mods.add("Reaction: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][4]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][5] > 0)
					mods.add("Knowledge: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][5]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][6] > 0)
					mods.add("Magelore: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][6]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][7] > 0)
					mods.add("Luck: +" + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][7]);
				
				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][0] < 0)
					mods.add("Dodge: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][0]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][1] < 0)
					mods.add("Init: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][1]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][2] < 0)
					mods.add("Abl %: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][2]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][3] < 0)
					mods.add("Execution: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][3]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][4] < 0)
					mods.add("Reaction: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][4]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][5] < 0)
					mods.add("Knowledge: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][5]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][6] < 0)
					mods.add("Magelore: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][6]);

				if (DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][7] < 0)
					mods.add("Luck: " + DefinitionArmor.ARMOR_MODIFIES[player.equippedArmorSlot4()][7]);
				
				
				if (DefinitionArmor.ARMOR_ADDS_HP[player.equippedArmorSlot4()] > 0)
				{
					mods.add("+" + DefinitionArmor.ARMOR_ADDS_HP[player.equippedArmorSlot4()] + " HP");
				}
				if (DefinitionArmor.ARMOR_ADDS_AP[player.equippedArmorSlot4()] > 0)
				{
					mods.add("+" + DefinitionArmor.ARMOR_ADDS_AP[player.equippedArmorSlot4()] + " AP");
				}

				if (mods.size() > 0)
				{
					curItemStat2.setVisibility(View.VISIBLE);
					curItemStat2.setText(mods.get(0));
				}

				if (mods.size() > 1)
				{
					curItemStat3.setVisibility(View.VISIBLE);
					curItemStat3.setText(mods.get(1));
				}

				if (mods.size() > 2)
				{
					curItemStat4.setVisibility(View.VISIBLE);
					curItemStat4.setText(mods.get(2));
				}

				String runeText = "";
				String addHPText = "";
				String addAPText = "";

				if (DefinitionArmor.ARMOR_GRANTS_RUNE_ID[player.equippedArmorSlot4()] >= 0)
				{
					runeText =
						"Grants Rune: "
							+ DefinitionRunes.runeData[DefinitionArmor.ARMOR_GRANTS_RUNE_ID[player.equippedArmorSlot4()]][DefinitionRunes.RUNE_NAMES][0];
				}
				

				curItemDescription.setText(runeText + DefinitionArmor.ARMOR_DESCRIPTIONS[player.equippedArmorSlot4()]
					+ addHPText + addAPText);

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
				curItemName
					.setText((CharSequence) DefinitionItems.itemdata[player.equippedItemSlot1()][DefinitionItems.ITEM_NAME][0]);
				curItemDescription
					.setText((CharSequence) DefinitionItems.itemdata[player.equippedItemSlot1()][DefinitionItems.ITEM_DESCRIPTION][0]);
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
				curItemName
					.setText((CharSequence) DefinitionItems.itemdata[player.equippedItemSlot2()][DefinitionItems.ITEM_NAME][0]);
				curItemDescription
					.setText((CharSequence) DefinitionItems.itemdata[player.equippedItemSlot2()][DefinitionItems.ITEM_DESCRIPTION][0]);
				curItemImage.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_ITEM,
					player.equippedItemSlot2()));
			}
		}
	}

	LinearLayout equipNewLayout;
	LinearLayout equipTopLayout;
	Button sortButton;
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
	Button backButton;
	ListView itemsListView;
	ArrayList<StoreItem> itemsList;
	int itemsListIndex;

	SimpleAdapter equipListAdapter = null;
	List<Map<String, Object>> equipAdapterData = new ArrayList<Map<String, Object>>();
}
