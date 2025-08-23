package com.alderangaming.wizardsencounters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

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
		//regen hp, ap on boss rounds
		if (DefinitionRounds.ROUND_TYPE[player.currentRound() - 1] == 1)
		{
			roundInfo += System.getProperty("line.separator")+"* HP Recharged *";
			roundInfo += System.getProperty("line.separator")+"* AP Recharged *";
			player.setCurrentAP(player.maxAP());
			player.setCurrentHP(player.maxHP());
		}
		// class type ap regen
		// start round with full ap flag
		else if (DefinitionClassType.CLASS_TYPE_AP_REGEN[DefinitionClasses.CLASS_TYPE[player.playerClass()]][4] == 1)
		{
			player.setCurrentAP(player.maxHP());
			roundInfo += System.getProperty("line.separator")+"* AP Recharged *";
			roundInfo +=
				System.getProperty("line.separator")+"* Your class type ["
					+ DefinitionClassType.CLASS_TYPE_NAME[DefinitionClasses.CLASS_TYPE[player.playerClass()]]
					+ "] starts each round will FULL AP *";
		}
	}
	
	private void updateViews()
	{
		roundStartInfoText.setText(roundInfo);
		playerHPView.setText(player.currentHP() + "/" + player.maxHP());
		playerAPView.setText(player.currentAP() + "/" + player.maxAP());
		roundView.setText("Round " + player.currentRound());
		playerNameView.setText(player.name());
		roundInfoView.setText(DefinitionRounds.ROUND_DESCRIPTION[player.currentRound() - 1]);
	}

	private void setupViews()
	{
		roundStartInfoText = (TextView) findViewById(R.id.startRoundInfoText);
		playerNameView = (TextView) findViewById(R.id.startRoundPlayerNameLabel);	
		playerHPView = (TextView) findViewById(R.id.startRoundPlayerHpLabel);
		playerAPView = (TextView) findViewById(R.id.startRoundPlayerApLabel);
		roundView = (TextView) findViewById(R.id.startRoundRoundLabel);

		roundInfoView = (TextView) findViewById(R.id.startRoundInfoLabel);	

		noButton = (Button) findViewById(R.id.startRoundNoButton);
		noButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				finish();
			}

		});

		goButton = (Button) findViewById(R.id.startRoundGoButton);
		goButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				goButton.setEnabled(false);

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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{

			alertDialog = new AlertDialog.Builder(this);
			alertDialog
				.setMessage(
					"Exiting this round will cause your player to lose all progress and dropped items. Are you sure you want to quit?")
				.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						if (DBHandler.isOpen(getApplicationContext()))
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
				}).setNegativeButton("No", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						dialog.cancel();
					}
				});

			alertDialog.show();

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
	Button noButton;
	Button goButton;

}
