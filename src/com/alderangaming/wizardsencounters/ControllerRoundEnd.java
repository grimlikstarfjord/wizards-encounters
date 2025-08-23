package com.alderangaming.wizardsencounters;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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

		SoundManager.stopAllMusic();

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
							showToast("The chest was empty!");
						}
						else
						{

							getLootItem(DefinitionGlobal.CHEST_OPEN_COST[0]);
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
						OwnedItems.updateGold(-(player.rank() * DefinitionGlobal.CHEST_OPEN_COST[1]));

						getLootItem(DefinitionGlobal.CHEST_OPEN_COST[1]);

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
						OwnedItems.updateGold(-(player.rank() * DefinitionGlobal.CHEST_OPEN_COST[2]));

						getLootItem(DefinitionGlobal.CHEST_OPEN_COST[2]);

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
						OwnedItems.updateGold(-(player.rank() * DefinitionGlobal.CHEST_OPEN_COST[3]));

						getLootItem(DefinitionGlobal.CHEST_OPEN_COST[3]);

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
						OwnedItems.updateGold(-(player.rank() * DefinitionGlobal.CHEST_OPEN_COST[4]));

						getLootItem(DefinitionGlobal.CHEST_OPEN_COST[4]);

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
						OwnedItems.updateGold(-(player.rank() * DefinitionGlobal.CHEST_OPEN_COST[5]));

						getLootItem(DefinitionGlobal.CHEST_OPEN_COST[5]);

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

				// save player data
				player.setCurrentRound(1);

				if (DBHandler.isOpen(getApplicationContext()))
					DBHandler.updatePlayer(player);

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

				Intent i =
					new Intent(getApplicationContext(), com.alderangaming.wizardsencounters.ControllerRoundStart.class);
				Bundle b = new Bundle();

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

	private void getLootItem(int value)
	{
		ArrayList<StoreItem> lootItems =
			Helper.getRandomDropsForRound(player.currentRound() - 1, value, getApplicationContext());

		Log.d("loot", "getLoot returned " + lootItems.size() + " items");

		int tries = 0;
		while (lootItems.size() < 1)
		{
			lootItems = Helper.getRandomDropsForRound(player.currentRound(), value, getApplicationContext());
			tries++;

			if (tries > 10)
			{
				Log.d("loot", "tried 10 times and couldn't get a drop: round " + player.currentRound() + ", value "
					+ value);
				break;
			}
		}

		if (tries > 10)
			showToast("The chest was empty!");

		else
		{
			StoreItem s = lootItems.get(Helper.randomInt(lootItems.size()));

			final Dialog dialog = new Dialog(ControllerRoundEnd.this);
			dialog.setContentView(R.layout.iteminfo);
			TextView itemName = (TextView) dialog.findViewById(R.id.itemInfoName);
			TextView itemDescription = (TextView) dialog.findViewById(R.id.itemInfoDescription);
			ImageView itemImage = (ImageView) dialog.findViewById(R.id.itemInfoImage);

			// check if this is an item the player already owns - in this case
			// we give them a charge
			if (OwnedItems.getPlayerOwnsThis(s.itemType(), s.id()) && s.itemType() == DefinitionGlobal.ITEM_TYPE_ITEM)
			{
				if (OwnedItems.getPlayerOwnsThis(s).chargesPurchased() + 1 > s.maxCharges())
				{
					getLootItem(value);
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
					OwnedItems.rechargeItems(s.id(), -1);
				}

				dialog.setTitle("Loot: " + DefinitionGlobal.EQUIP_TYPE_NAMES[s.itemType()]);
				itemName.setText(s.name());
			}

			itemDescription.setText(s.description());
			itemImage.setImageResource(s.imageResource());
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

	private void updateViews()
	{
		roundEndTitleText.setText("Round " + (player.currentRound() - 1) + " Complete");
		roundendContinueButton.setText("Continue To Round " + player.currentRound());

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
		goldText.setText("You have " + OwnedItems.gold() + "gp");

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
				.setText("You have opened all the chests you can for now. If you checkout, you will keep your loot, but "
					+ player.name() + " will start over at Round 1.");
		}

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
