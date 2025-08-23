package com.alderangaming.wizardsencounters;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ControllerRoundEnd extends Activity
{

	Player player;
	// OwnedItems OwnedItems;
	private int chestsOpened = 0;
	private boolean opened1, opened2, opened3, opened4, opened5, opened6 = false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		//stop previous combat song
		SoundManager.stopAllMusic();
		
		//start treasure room song
		SoundManager.playMusic(SoundManager.SOUND_TYPE_MUSIC_TREASURE_ROOM, false, ControllerRoundEnd.this);

		if (!DBHandler.isOpen(getApplicationContext()))
			DBHandler.open(getApplicationContext());

		Bundle b = getIntent().getExtras();

		// OwnedItems = (OwnedItems) b.getSerializable("OwnedItems");
		player = (Player) b.getSerializable("player");
		player.updateCurrentRound();

		setContentView(R.layout.roundend);
		setupViews();
		updateViews();
	}

	private void setupViews()
	{
		roundEndTitleText = (TextView) findViewById(R.id.roundendTitleText);
		roundendCheckoutButton = (Button) findViewById(R.id.roundEndCheckoutButton);

		roundEndText = (TextView) findViewById(R.id.roundEndText);

		goldText = (TextView) findViewById(R.id.roundEndGoldText);
		chest1Text = (TextView) findViewById(R.id.chest1Text);
		chest2Text = (TextView) findViewById(R.id.chest2Text);
		chest3Text = (TextView) findViewById(R.id.chest3Text);
		chest4Text = (TextView) findViewById(R.id.chest4Text);
		chest5Text = (TextView) findViewById(R.id.chest5Text);
		chest6Text = (TextView) findViewById(R.id.chest6Text);

		roundEndBackgroundImage = (ImageView) findViewById(R.id.roundEndBackgroundImage);
		chest1Image = (ImageView) findViewById(R.id.chest1Image);
		chest1Image.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (chestsOpened < DefinitionGlobal.MAX_CHESTS_TO_OPEN && !opened1)
				{
					if (OwnedItems.gold() >= player.rank() * DefinitionGlobal.CHEST_OPEN_COST[0])
					{
						opened1 = true;
						chestsOpened++;
						chest1Image.setImageResource(R.drawable.chest1open);
						OwnedItems.updateGold(-(player.rank() * DefinitionGlobal.CHEST_OPEN_COST[0]));

						if (Helper.randomInt(100) < DefinitionGlobal.CHANCE_FREE_CHEST_EMPTY)
						{
							SoundManager.playSound(SoundManager.OPEN_CHEST_EMPTY, true);
							showToast("The chest was empty!");
						}
						else
						{
							getLootItem(0, 50);
						}
						updateViews();

					}
					else
					{
						showToast("You can't afford to open this chest.");
					}
				}
			}

		});
		chest2Image = (ImageView) findViewById(R.id.chest2Image);
		chest2Image.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (chestsOpened < DefinitionGlobal.MAX_CHESTS_TO_OPEN && !opened2)
				{
					if (OwnedItems.gold() >= player.rank() * DefinitionGlobal.CHEST_OPEN_COST[1])
					{
						opened2 = true;
						chestsOpened++;
						chest2Image.setImageResource(R.drawable.chest2open);
						int openCost = player.rank() * DefinitionGlobal.CHEST_OPEN_COST[1];
						OwnedItems.updateGold(-openCost);

						getLootItem(1, openCost);

						updateViews();
					}
					else
					{
						showToast("You can't afford to open this chest.");
					}
				}
			}

		});
		chest3Image = (ImageView) findViewById(R.id.chest3Image);
		chest3Image.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (chestsOpened < DefinitionGlobal.MAX_CHESTS_TO_OPEN && !opened3)
				{
					if (OwnedItems.gold() >= player.rank() * DefinitionGlobal.CHEST_OPEN_COST[2])
					{
						opened3 = true;
						chestsOpened++;
						chest3Image.setImageResource(R.drawable.chest2open);
						int openCost = player.rank() * DefinitionGlobal.CHEST_OPEN_COST[2];
						OwnedItems.updateGold(-openCost);

						getLootItem(2, openCost);

						updateViews();
					}
					else
					{
						showToast("You can't afford to open this chest.");
					}
				}
			}

		});
		chest4Image = (ImageView) findViewById(R.id.chest4Image);
		chest4Image.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (chestsOpened < DefinitionGlobal.MAX_CHESTS_TO_OPEN && !opened4)
				{
					if (OwnedItems.gold() >= player.rank() * DefinitionGlobal.CHEST_OPEN_COST[3])
					{
						opened4 = true;
						chestsOpened++;
						chest4Image.setImageResource(R.drawable.chest4open);
						int openCost = player.rank() * DefinitionGlobal.CHEST_OPEN_COST[3];
						OwnedItems.updateGold(-openCost);

						getLootItem(3, openCost);

						updateViews();
					}
					else
					{
						showToast("You can't afford to open this chest.");
					}
				}
			}

		});
		chest5Image = (ImageView) findViewById(R.id.chest5Image);
		chest5Image.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (chestsOpened < DefinitionGlobal.MAX_CHESTS_TO_OPEN && !opened5)
				{
					if (OwnedItems.gold() >= player.rank() * DefinitionGlobal.CHEST_OPEN_COST[4])
					{
						opened5 = true;
						chestsOpened++;
						chest5Image.setImageResource(R.drawable.chest5open);
						int openCost = player.rank() * DefinitionGlobal.CHEST_OPEN_COST[4];
						OwnedItems.updateGold(-openCost);

						getLootItem(4, openCost);

						updateViews();
					}
					else
					{
						showToast("You can't afford to open this chest.");
					}
				}
			}

		});
		chest6Image = (ImageView) findViewById(R.id.chest6Image);
		chest6Image.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (chestsOpened < DefinitionGlobal.MAX_CHESTS_TO_OPEN && !opened6)
				{
					if (OwnedItems.gold() >= player.rank() * DefinitionGlobal.CHEST_OPEN_COST[5])
					{
						opened6 = true;
						chestsOpened++;
						chest6Image.setImageResource(R.drawable.chest6open);
						int openCost = player.rank() * DefinitionGlobal.CHEST_OPEN_COST[5];
						OwnedItems.updateGold(-openCost);

						getLootItem(5, openCost);

						updateViews();
					}
					else
					{
						showToast("You can't afford to open this chest.");
					}
				}
			}

		});

		roundendCheckoutButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				// TODO add checkout verify dialog

				//player.setCurrentRound(1);

				if (DBHandler.updatePlayer(player))
				{
					DBHandler.close();
				}

				finish();

			}

		});
		roundendContinueButton = (Button) findViewById(R.id.roundEndContinueButton);

		roundendContinueButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// save player data
				if (DBHandler.updatePlayer(player))
				{
					DBHandler.close();
				}

				Intent i = null;
				Bundle b = null;

				if (player.currentRound() >= DefinitionRounds.LAST_ROUND)
				{
					i =
						new Intent(getApplicationContext(), com.alderangaming.wizardsencounters.ControllerEndGame.class);
					b = new Bundle();
				}
				else
				{
					i =
						new Intent(getApplicationContext(),
							com.alderangaming.wizardsencounters.ControllerRoundStart.class);
					b = new Bundle();
				}

				// b.putSerializable("OwnedItems", OwnedItems);
				b.putSerializable("player", player);

				i.putExtras(b);

				startActivity(i);

				finish();
			}

		});

		roundendInventoryButton = (Button) findViewById(R.id.roundendInventoryButton);
		roundendInventoryButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (DBHandler.isOpen(getApplicationContext()))
					DBHandler.close();

				Intent i =
					new Intent(getApplicationContext(), com.alderangaming.wizardsencounters.ControllerInventory.class);
				Bundle b = new Bundle();

				// b.putSerializable("OwnedItems", OwnedItems);
				b.putSerializable("player", player);

				i.putExtras(b);

				startActivityForResult(i, 8000);
			}

		});
	}

	private void showToast(String text)
	{
		Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	private void getLootItem(int chest, int chestCost)
	{

		if (Helper.randomInt(100) < DefinitionGlobal.CHANCE_TO_GET_GOLD_FROM_CHEST && chestCost >= 0)
		{
			// player found gold
			
				int amt = (Helper.randomInt(DefinitionGlobal.MAX_PERCENT_OF_CHEST_COST_OF_GOLD_IN_CHEST));
			double amt2 = (double)amt / (double)100;

			int goldAmt = (int) Math.round((double)chestCost * amt2);
			
			SoundManager.playSound(SoundManager.OPEN_CHEST_ITEM, true);
			showToast("There was "+goldAmt+" gold inside!");
			OwnedItems.updateGold(goldAmt);
			updateGoldText();
			return;
		}

		ArrayList<StoreItem> lootItems =
			Helper.getRandomDropsForRound(player.currentRound() - 1, chest, getApplicationContext());

		Log.d("loot", "getLoot returned " + lootItems.size() + " items");

		int tries = 0;
		while (lootItems.size() < 1)
		{
			lootItems = Helper.getRandomDropsForRound(player.currentRound(), chest, getApplicationContext());
			tries++;

			if (tries > 10)
			{
				Log.d("loot", "tried 10 times and couldn't get a drop: round " + player.currentRound() + ", value "
					+ chest);
				break;
			}
		}

		if (tries > 10)
		{
			SoundManager.playSound(SoundManager.OPEN_CHEST_EMPTY, true);
			showToast("The chest was empty!");
		}

		else
		{
			// get item for that chest

			SoundManager.playSound(SoundManager.OPEN_CHEST_ITEM, true);

			StoreItem s = lootItems.get(Helper.randomInt(lootItems.size()));

			final Dialog dialog = new Dialog(ControllerRoundEnd.this);
			dialog.setContentView(R.layout.iteminfo);
			TextView itemName = (TextView) dialog.findViewById(R.id.itemInfoName);
			TextView itemDescription = (TextView) dialog.findViewById(R.id.itemInfoDescription);
			ImageView itemImage = (ImageView) dialog.findViewById(R.id.itemInfoImage);
			TextView itemStats1 = (TextView) dialog.findViewById(R.id.itemInfoStats1);
			TextView itemStats2 = (TextView) dialog.findViewById(R.id.itemInfoStats2);

			// check if this is an item the player already owns - in this case
			// we give them a charge
			if (OwnedItems.getPlayerOwnsThis(s.itemType(), s.id()) && s.itemType() == DefinitionGlobal.ITEM_TYPE_ITEM)
			{
				if (OwnedItems.getPlayerOwnsThis(s).chargesPurchased() + 1 > s.maxCharges())
				{
					getLootItem(chest, -1);
					return;
				}
				else
				{
					int index = OwnedItems.getIndexOfItemTypeId(s.itemType(), s.id());
					OwnedItems.setChargesPucrchasedOfItemByOwnedIndex(index, OwnedItems.getPlayerOwnsThis(s)
						.chargesPurchased() + 1);
					OwnedItems.rechargeItems(s.id(), -1);

					dialog.setTitle("Loot: Item Upgrade");
					itemName.setText(OwnedItems.getItemEnhancedNameByTypeId(s.itemType(), s.id()));
				}
			}
			// player does not own this thing yet
			else
			{
				OwnedItems.addOwnedItems(new StoreItem[]
				{ s });

				// if this is an item, give it the first charge
				if (s.itemType() == DefinitionGlobal.ITEM_TYPE_ITEM)
				{
					int index = OwnedItems.getIndexOfItemTypeId(s.itemType(), s.id());
					OwnedItems.setChargesPucrchasedOfItemByOwnedIndex(index, 1);
					OwnedItems.giveItemStartingCharge(s.id());
				}

				dialog.setTitle("Loot: " + s.equipTypeName());
				itemName.setText(s.name());
			}

			itemDescription.setText(s.description());
			itemImage.setImageResource(s.imageResource());
			itemStats1.setText("Equip on: "+Helper.getAllowedClassesString(s.itemType(), s.id()));
			itemStats2.setText("Min Rank: "+Helper.getMinRank(s.itemType(), s.id()));
			Button doneButton = (Button) dialog.findViewById(R.id.itemInfoDoneButton);

			doneButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					dialog.dismiss();
				}
			});

			if (DBHandler.updateOwnedItems(OwnedItems.getOwnedItems()))
			{
				Log.d("loot", "database updated, looted item saved: " + s.name());
				Log.d("loot", "player now owns " + OwnedItems.getAmountOwnedOfTypeId(s.itemType(), s.id()) + " of them");
			}
			else
			{
				Log.d("loot", "error saving new looted item to db!!!");
			}

			DBHandler.updateGlobalStats(OwnedItems.gold());

			dialog.show();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode)
		{
		// return from inventory
			case 8000:
			{
				// OwnedItems = (OwnedItems)
				// data.getExtras().getSerializable("OwnedItems");
				player = (Player) data.getExtras().getSerializable("player");

				break;
			}
		}
	}
	
	private void updateGoldText()
	{
		goldText.setText("You have " + OwnedItems.gold() + "gp");
	}

	private void updateViews()
	{
		roundEndTitleText.setText("Round " + (player.currentRound() - 1) + " Complete");
		
		if (player.currentRound() - 2 >= 0)
		{
			if (DefinitionRounds.ROUND_TYPE[player.currentRound() - 2] == 1)
			{
				roundEndText.setText("Congratulations! You are now Rank " + player.rank() + "! You may open up to "
					+ DefinitionGlobal.MAX_CHESTS_TO_OPEN + " chests.");
				DBHandler.updatePlayer(player);
			}
		}
		else
		{
			roundEndText.setText("Congratulations! You may open up to " + DefinitionGlobal.MAX_CHESTS_TO_OPEN
				+ " chests.");
		}
		
		updateGoldText();

		if (player.rank() * DefinitionGlobal.CHEST_OPEN_COST[0] < 1)
			chest1Text.setText("free");
		else
			chest1Text.setText(player.rank() * DefinitionGlobal.CHEST_OPEN_COST[0] + "gp");

		chest2Text.setText(player.rank() * DefinitionGlobal.CHEST_OPEN_COST[1] + "gp");
		chest3Text.setText(player.rank() * DefinitionGlobal.CHEST_OPEN_COST[2] + "gp");
		chest4Text.setText(player.rank() * DefinitionGlobal.CHEST_OPEN_COST[3] + "gp");
		chest5Text.setText(player.rank() * DefinitionGlobal.CHEST_OPEN_COST[4] + "gp");
		chest6Text.setText(player.rank() * DefinitionGlobal.CHEST_OPEN_COST[5] + "gp");

		if (chestsOpened >= DefinitionGlobal.MAX_CHESTS_TO_OPEN)
		{
			chest1Image.setVisibility(View.INVISIBLE);
			chest2Image.setVisibility(View.INVISIBLE);
			chest3Image.setVisibility(View.INVISIBLE);
			chest4Image.setVisibility(View.INVISIBLE);
			chest5Image.setVisibility(View.INVISIBLE);
			chest6Image.setVisibility(View.INVISIBLE);
			chest1Text.setText("");
			chest2Text.setText("");
			chest3Text.setText("");
			chest4Text.setText("");
			chest5Text.setText("");
			chest6Text.setText("");

			roundEndText
				.setText("You have opened all the chests you can for now. If you Check Out, your current Round will be saved.");
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	TextView roundEndTitleText;
	TextView roundEndText;
	TextView chest1Text;
	TextView chest2Text;
	TextView chest3Text;
	TextView chest4Text;
	TextView chest5Text;
	TextView chest6Text;
	TextView goldText;
	ImageView roundEndBackgroundImage;
	ImageView chest1Image;
	ImageView chest2Image;
	ImageView chest3Image;
	ImageView chest4Image;
	ImageView chest5Image;
	ImageView chest6Image;
	Button roundendCheckoutButton;
	Button roundendContinueButton;
	Button roundendInventoryButton;

}
