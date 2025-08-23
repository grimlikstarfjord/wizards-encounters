package com.alderangaming.wizardsencounters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ControllerRoundStart extends Activity
{
	Player player;
	private String roundInfo = "";

	// OwnedItems ownedItems;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		if (!DBHandler.isOpen(getApplicationContext()))
			DBHandler.open(getApplicationContext());
		
		//stop prev treasure room song if playing
		SoundManager.stopAllMusic();

		Bundle b = getIntent().getExtras();

		// ownedItems = (OwnedItems) b.getSerializable("ownedItems");
		player = (Player) b.getSerializable("player");

		setContentView(R.layout.roundstart);
		setupViews();
		setupRound();
		updateViews();
	}

	private void setupRound()
	{
		// regen hp, ap on boss rounds
		if (DefinitionRounds.ROUND_TYPE[player.currentRound() - 1] == 1)
		{
			roundInfo += System.getProperty("line.separator") + "* HP Recharged *";
			roundInfo += System.getProperty("line.separator") + "* AP Recharged *";
			player.setCurrentAP(player.maxAP());
			player.setCurrentHP(player.maxHP());
		}
		// class type ap regen
		// start round with full ap flag
		else if (DefinitionClassType.CLASS_TYPE_AP_REGEN[DefinitionClasses.CLASS_TYPE[player.playerClass()]][4] == 1)
		{
			player.setCurrentAP(player.maxAP());
			roundInfo += System.getProperty("line.separator") + "* AP Recharged *";
			roundInfo +=
				System.getProperty("line.separator") + "* Your class type ["
					+ DefinitionClassType.CLASS_TYPE_NAME[DefinitionClasses.CLASS_TYPE[player.playerClass()]]
					+ "] starts each round will FULL AP *";
		}
	}
	
	private void updateGoldText()
	{
		goldText.setText("Gold: " + OwnedItems.gold());
	}

	private void updateViews()
	{
		roundStartInfoText.setText(roundInfo);
		playerHPView.setText("HP: " + player.currentHP() + "/" + player.maxHP());
		playerAPView.setText("AP: " + player.currentAP() + "/" + player.maxAP());
		roundView.setText("Round " + player.currentRound());
		playerNameView.setText(player.name());
		roundInfoView.setText(DefinitionRounds.ROUND_DESCRIPTION[player.currentRound() - 1]);
		updateGoldText();
		itemsEquippedText.setText("Items Equipped: " + player.numEquippedItems());
		updateEquippedItemsText();
	}

	private void updateEquippedItemsText()
	{
		item1InfoText.setText("No Item in Slot 1");
		item1CostText.setText("");
		item1RechargeButton.setBackgroundResource(R.drawable.buttonrechargedisabled);
		item1RechargeButton.setEnabled(false);

		item2InfoText.setText("No Item in Slot 2");
		item2CostText.setText("");
		item2RechargeButton.setBackgroundResource(R.drawable.buttonrechargedisabled);
		item2RechargeButton.setEnabled(false);

		if (player.equippedItemSlot1() >= 0)
		{
			String itemText = "";
			itemText += DefinitionItems.itemdata[player.equippedItemSlot1()][DefinitionItems.ITEM_NAME][0].toString();
			itemText += " ";
			itemText += OwnedItems.getChargesOfItemId(player.equippedItemSlot1())[0];
			itemText += "/";
			itemText += OwnedItems.getChargesOfItemId(player.equippedItemSlot1())[1];
			item1InfoText.setText(itemText);

			String costText = "Cost for charge: ";
			costText += OwnedItems.costToRechargeItem(player.equippedItemSlot1());
			item1CostText.setText(costText);

			if (OwnedItems.getChargesOfItemId(player.equippedItemSlot1())[0] < OwnedItems.getChargesOfItemId(player
				.equippedItemSlot1())[1])
			{
				item1RechargeButton.setBackgroundResource(R.drawable.buttonrecharge);
			item1RechargeButton.setEnabled(true);
			}
		}

		if (player.equippedItemSlot2() >= 0)
		{
			String itemText = "";
			itemText += DefinitionItems.itemdata[player.equippedItemSlot2()][DefinitionItems.ITEM_NAME][0].toString();
			itemText += " ";
			itemText += OwnedItems.getChargesOfItemId(player.equippedItemSlot2())[0];
			itemText += "/";
			itemText += OwnedItems.getChargesOfItemId(player.equippedItemSlot2())[1];
			item2InfoText.setText(itemText);

			String costText = "Cost for charge: ";
			costText += OwnedItems.costToRechargeItem(player.equippedItemSlot2());
			item2CostText.setText(costText);

			if (OwnedItems.getChargesOfItemId(player.equippedItemSlot2())[0] < OwnedItems.getChargesOfItemId(player
				.equippedItemSlot2())[1])
			{
				item2RechargeButton.setBackgroundResource(R.drawable.buttonrecharge);
				item2RechargeButton.setEnabled(true);
			}
		}

	}

	private void chargeItem(int itemId)
	{
		if (OwnedItems.gold() >= OwnedItems.costToRechargeItem(itemId))
		{
			SoundManager.playSound(SoundManager.RECHARGESOUND, true);
			OwnedItems.payForRechargeItem(itemId);
			OwnedItems.updateGold(-OwnedItems.costToRechargeItem(itemId));
			DBHandler.updateGlobalStats(OwnedItems.gold());
			DBHandler.updateOwnedItems(OwnedItems.getOwnedItems());
			showToast("Item gained 1 charge.");
			updateEquippedItemsText();
			updateGoldText();
		}
		else
		{
			showToast("You can't afford to recharge this item.");
		}
	}

	private void showToast(String text)
	{
		Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	private void setupViews()
	{
		roundStartInfoText = (TextView) findViewById(R.id.startRoundInfoText);
		playerNameView = (TextView) findViewById(R.id.startRoundPlayerNameLabel);
		playerHPView = (TextView) findViewById(R.id.startRoundPlayerHpLabel);
		playerAPView = (TextView) findViewById(R.id.startRoundPlayerApLabel);
		roundView = (TextView) findViewById(R.id.startRoundRoundLabel);
		goldText = (TextView) findViewById(R.id.roundstartcurrentgold);

		itemsEquippedText = (TextView) findViewById(R.id.startrounditemsequipped);
		item1InfoText = (TextView) findViewById(R.id.roundstartitem1info);
		item2InfoText = (TextView) findViewById(R.id.roundstartitem2info);
		item1CostText = (TextView) findViewById(R.id.roundstartitem1cost);
		item2CostText = (TextView) findViewById(R.id.roundstartitem2cost);

		roundInfoView = (TextView) findViewById(R.id.startRoundInfoLabel);

		item1RechargeButton = (Button) findViewById(R.id.roundstartrechargeitem1);
		item1RechargeButton.setBackgroundResource(R.drawable.buttonrechargedisabled);
		item1RechargeButton.setEnabled(false);
		item1RechargeButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				chargeItem(player.equippedItemSlot1());
				item1RechargeButton.setBackgroundResource(R.drawable.buttonrechargedisabled);
				item1RechargeButton.setEnabled(false);
			}
		});

		item2RechargeButton = (Button) findViewById(R.id.roundstartrechargeitem2);
		item2RechargeButton.setEnabled(false);
		item2RechargeButton.setBackgroundResource(R.drawable.buttonrechargedisabled);
		item2RechargeButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				chargeItem(player.equippedItemSlot2());
				item2RechargeButton.setBackgroundResource(R.drawable.buttonrechargedisabled);
				item2RechargeButton.setEnabled(false);
			}
		});

		

		goButton = (Button) findViewById(R.id.startRoundGoButton);
		goButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				goButton.setEnabled(false);				
				goButton.setVisibility(View.INVISIBLE);

				if (DefinitionRounds.ROUND_TYPE[player.currentRound() - 1] == 1)
					SoundManager.playMusic(SoundManager.SOUND_TYPE_MUSIC_BOSS, true, getApplicationContext());

				else
					SoundManager.playNextCombatSong(getApplicationContext());

				player.setInRound(true);

				if (DBHandler.updatePlayer(player))
				{
					DBHandler.close();
				}

				Intent i =
					new Intent(getApplicationContext(), com.alderangaming.wizardsencounters.ControllerFightStart.class);
				Bundle b = new Bundle();

				// replenish item charges
				player.rechargeItemsAtStartOfRound();

				// b.putSerializable("ownedItems", ownedItems);
				b.putSerializable("player", player);
				b.putInt("background", BackgroundManager.getNextBackground());

				i.putExtras(b);

				startActivity(i);
				finish();
			}

		});
	}
	
	private void exitArena()
	{
		player.setRank(1);
		player.setCurrentRound(1);
		player.setCurrentFight(1);

		try
		{
			if (DBHandler.isOpen(getApplicationContext()))
			{
				if (DBHandler.updatePlayer(player))
					DBHandler.close();
			}
			else
			{
				if (DBHandler.open(getApplicationContext()))
				{
					if (DBHandler.updatePlayer(player))
						DBHandler.close();
				}
			}
		}
		catch (Exception e)
		{
			Log.d("combat",
				"failed to save updated player info to DB when exiting via back:" + e.toString());
		}

		Intent resultIntent = new Intent();
		Bundle b = new Bundle();

		// b.putSerializable("OwnedItems", OwnedItems);
		b.putSerializable("player", player);

		resultIntent.putExtras(b);

		setResult(Activity.RESULT_OK, resultIntent);

		finish();	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			
			SoundManager.playSound(SoundManager.ALERTSOUND, true);

			final Dialog dialog = new Dialog(ControllerRoundStart.this);
			dialog.setContentView(R.layout.exitarenadialog);
			dialog.setTitle("Exit Arena?");
			TextView exitArenaTextView = (TextView) dialog.findViewById(R.id.exitArenaDialogTextView);
			exitArenaTextView
				.setText("Exiting the Arena will cause this character ("
					+ player.name()
					+ ", Rank "
					+ player.rank()
					+ ") to lose all progress and gold gained from this Round. They will revert to Round 1, Rank 1! What say you?");
			Button exitArenaButton = (Button) dialog.findViewById(R.id.exitArenaDialogButton);
			Button donotexitArenaButton = (Button) dialog.findViewById(R.id.exitArenaDialogCancelButton);
			exitArenaButton.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					exitArena();	
				}

			});

			donotexitArenaButton.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					dialog.cancel();					
				}

			});
			dialog.show();
		}
		return super.onKeyDown(keyCode, event);
	}

	

	AlertDialog.Builder alertDialog;

	TextView playerNameView;
	TextView playerHPView;
	TextView playerAPView;
	TextView roundView;
	TextView roundInfoView;
	TextView roundStartInfoText;
	TextView itemsEquippedText, item1InfoText, item2InfoText, item1CostText, item2CostText;
	TextView goldText;
	Button item1RechargeButton, item2RechargeButton;
	
	Button goButton;

}
