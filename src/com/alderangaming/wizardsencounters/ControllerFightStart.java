package com.alderangaming.wizardsencounters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ControllerFightStart extends Activity {
	Player player;
	// OwnedItems ownedItems;
	Monster monster;

	private int monsterId = -1;
	private int restartingFight = -1;
	private int background = -1;
	private String fightInfo = "";
	private boolean pendingMonsterSelection = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (!DBHandler.isOpen(getApplicationContext()))
			DBHandler.open(getApplicationContext());

		Bundle b = getIntent().getExtras();

		// ownedItems = (OwnedItems) b.getSerializable("ownedItems");
		player = (Player) b.getSerializable("player");
		background = b.getInt("background");

		restartingFight = b.getInt("restartfightflag");

		if (restartingFight > 0) {
			monsterId = b.getInt("restartfightmonster");
		}

		setContentView(R.layout.fightstart);

		setupViews();
		setupFight();
		if (!pendingMonsterSelection) {
			loadBattleSounds();
			updateViews();
		}
	}

	private void updateViews() {
		monsterNameView.setText(DefinitionMonsters.MONSTER_NAMES[monsterId]);
		monster.setImageResource(getResources().getIdentifier(monster.imageName(), "drawable", getPackageName()));
		playerNameView.setText(player.name());
		playerHPView.setText("HP: " + player.currentHP() + "/" + player.maxHP());
		playerAPView.setText("AP: " + player.currentAP() + "/" + player.maxAP());
		monsterImageView.setImageResource(monster.imageResource());
		fightInfoText.setText(fightInfo);

		fightNameView.setText("Round " + player.currentRound() + " : " + player.currentFight());

		if (player.currentFight() == 1)
			progressFight1Text.setBackgroundColor(0xFF5555FF);

		if (player.currentFight() == 2)
			progressFight2Text.setBackgroundColor(0xFF5555FF);

		if (player.currentFight() == 3)
			progressFight3Text.setBackgroundColor(0xFF5555FF);

		if (player.currentFight() == 4)
			progressFight4Text.setBackgroundColor(0xFF5555FF);

		if (player.currentFight() == 5)
			progressFight5Text.setBackgroundColor(0xFF5555FF);

		int curRound = player.currentRound() - 1;
		boolean foundboss = false;
		for (int a = curRound; a >= 0; a--) {
			if (DefinitionRounds.ROUND_TYPE[a] == 1) {
				if (a == curRound)
					continue;

				// we know which group we're in, start with a+1
				progressRound1Text.setText("" + (a + 2));
				progressRound2Text.setText("" + (a + 3));
				progressRound3Text.setText("" + (a + 4));
				progressRound4Text.setText("" + (a + 5));
				foundboss = true;
				break;
			}
		}
		if (!foundboss) {
			progressRound1Text.setText("" + (1));
			progressRound2Text.setText("" + (2));
			progressRound3Text.setText("" + (3));
			progressRound4Text.setText("" + (4));
		}

		if (player.currentRound() % 5 == 1)
			progressRound1Text.setBackgroundColor(0xFF5555FF);

		if (player.currentRound() % 5 == 2)
			progressRound2Text.setBackgroundColor(0xFF5555FF);

		if (player.currentRound() % 5 == 3)
			progressRound3Text.setBackgroundColor(0xFF5555FF);

		if (player.currentRound() % 5 == 4)
			progressRound4Text.setBackgroundColor(0xFF5555FF);

		if (player.currentRound() % 5 == 0)
			progressRound5Text.setBackgroundColor(0xFF5555FF);
	}

	private void setupFight() {

		if (restartingFight == 1) {
			fightInfo += System.getProperty("line.separator") + "* HP Set to 50% *";
			player.setCurrentHP((int) Math.round(player.maxHP() / 2));
		} else if (restartingFight == 2) {
			fightInfo += System.getProperty("line.separator") + "* HP Set to 100% *";
			player.setCurrentHP(player.maxHP());
		} else if (DefinitionRounds.ROUND_TYPE[player.currentRound() - 1] == 1) {
			// this is a boss fight
			// monster ID is specified uniquely by round
			monsterId = Helper.getBossMonsterIDForRound(player.currentRound());

			if (player.currentHP() != player.maxHP()) {
				fightInfo += System.getProperty("line.separator") + "* +" + DefinitionGlobal.REGEN_HP_AT_BOSS_START
						+ "% HP Recharged *";
				player.updateHP(Helper.getPercentFromInt(DefinitionGlobal.REGEN_HP_AT_BOSS_START, player.maxHP()));
			}
		} else {
			final int[] possibleMonsterIds = Helper.getPossibleMonstersForFight(
					DefinitionRounds.ROUND_MONSTER_LEVELS[player.currentRound() - 1],
					player.foughtMonstersInRound());

			// Testing helper: let the user pick the monster for this fight
			pendingMonsterSelection = true;
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Pick Monster (Test)");
			String[] names = new String[possibleMonsterIds.length + 1];
			names[0] = "Random";
			for (int i = 0; i < possibleMonsterIds.length; i++) {
				names[i + 1] = DefinitionMonsters.MONSTER_NAMES[possibleMonsterIds[i]];
			}
			builder.setItems(names, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == 0) {
						monsterId = Helper.getRandomIntFromIntArray(possibleMonsterIds);
					} else {
						monsterId = possibleMonsterIds[which - 1];
					}

					// Apply non-boss fight regen if needed
					if (player.currentHP() != player.maxHP()) {
						fightInfo += System.getProperty("line.separator") + "* +"
								+ DefinitionGlobal.REGEN_HP_AT_FIGHT_START + "% HP Recharged *";
						player.updateHP(
								Helper.getPercentFromInt(DefinitionGlobal.REGEN_HP_AT_FIGHT_START, player.maxHP()));
					}

					finalizeMonsterSetupAndContinue();
				}
			});
			builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					// Fallback to random on cancel
					monsterId = Helper.getRandomIntFromIntArray(possibleMonsterIds);
					if (player.currentHP() != player.maxHP()) {
						fightInfo += System.getProperty("line.separator") + "* +"
								+ DefinitionGlobal.REGEN_HP_AT_FIGHT_START + "% HP Recharged *";
						player.updateHP(
								Helper.getPercentFromInt(DefinitionGlobal.REGEN_HP_AT_FIGHT_START, player.maxHP()));
					}
					finalizeMonsterSetupAndContinue();
				}
			});
			builder.show();
		}

		if (!pendingMonsterSelection) {
			monster = new Monster(monsterId, getApplicationContext());
			player.addMonsterInRound(monsterId);

			// set AP per class type
			// start combat with full ap flag
			if (DefinitionClassType.CLASS_TYPE_AP_REGEN[DefinitionClasses.CLASS_TYPE[player.playerClass()]][0] == 1) {
				player.setCurrentAP(player.maxAP());
				fightInfo += System.getProperty("line.separator") + "* Your class type ["
						+ DefinitionClassType.CLASS_TYPE_NAME[DefinitionClasses.CLASS_TYPE[player.playerClass()]]
						+ "] starts each fight will FULL AP *";
			}
			// start combat with no ap flag
			if (DefinitionClassType.CLASS_TYPE_AP_REGEN[DefinitionClasses.CLASS_TYPE[player.playerClass()]][1] == 1) {
				player.setCurrentAP(0);
				fightInfo += System.getProperty("line.separator") + "* Your class type ["
						+ DefinitionClassType.CLASS_TYPE_NAME[DefinitionClasses.CLASS_TYPE[player.playerClass()]]
						+ "] starts each fight will 0 AP *";
			}
		}
	}

	private void finalizeMonsterSetupAndContinue() {
		pendingMonsterSelection = false;
		monster = new Monster(monsterId, getApplicationContext());
		player.addMonsterInRound(monsterId);

		// set AP per class type
		if (DefinitionClassType.CLASS_TYPE_AP_REGEN[DefinitionClasses.CLASS_TYPE[player.playerClass()]][0] == 1) {
			player.setCurrentAP(player.maxAP());
			fightInfo += System.getProperty("line.separator") + "* Your class type ["
					+ DefinitionClassType.CLASS_TYPE_NAME[DefinitionClasses.CLASS_TYPE[player.playerClass()]]
					+ "] starts each fight will FULL AP *";
		}
		if (DefinitionClassType.CLASS_TYPE_AP_REGEN[DefinitionClasses.CLASS_TYPE[player.playerClass()]][1] == 1) {
			player.setCurrentAP(0);
			fightInfo += System.getProperty("line.separator") + "* Your class type ["
					+ DefinitionClassType.CLASS_TYPE_NAME[DefinitionClasses.CLASS_TYPE[player.playerClass()]]
					+ "] starts each fight will 0 AP *";
		}

		loadBattleSounds();
		updateViews();
	}

	private void loadBattleSounds() {
		// weapon attacks
		int[] attackSoundTypes = new int[DefinitionWeapons.WEAPON_ATTACK_TYPES[player.equippedWeapon()].length];

		for (int a = 0; a < attackSoundTypes.length; a++) {
			attackSoundTypes[a] = DefinitionAttackTypes.ATTACK_SOUND_CLIPS[DefinitionWeapons.WEAPON_ATTACK_TYPES[player
					.equippedWeapon()][a]];
		}

		SoundManager.loadBattleSoundEffects(getApplicationContext(), attackSoundTypes);

		// monster taunts
		SoundManager.loadBattleSoundEffects(getApplicationContext(),
				new int[] { DefinitionMonsters.MONSTER_TAUNT_SOUND_TYPE[monsterId] });

		// monster abilities
		int[] monsAbils = new int[DefinitionMonsters.MONSTER_ABILITIES[monsterId].length];
		for (int a = 0; a < monsAbils.length; a++) {
			monsAbils[a] = (Integer) DefinitionRunes.runeData[DefinitionMonsters.MONSTER_ABILITIES[monsterId][a]][DefinitionRunes.RUNE_SOUND_CLIP][0];
		}
		SoundManager.loadBattleSoundEffects(getApplicationContext(), monsAbils);

		// current abilities
		int[] abilitySounds = new int[player.getActiveAbilities().length];
		for (int a = 0; a < abilitySounds.length; a++) {
			abilitySounds[a] = (Integer) DefinitionRunes.runeData[player
					.getActiveAbilityByIndex(a)][DefinitionRunes.RUNE_SOUND_CLIP][0];
		}
		SoundManager.loadBattleSoundEffects(getApplicationContext(), abilitySounds);

		// current items
		if (player.equippedItemSlot1() > -1) {
			if (OwnedItems.getChargesOfItemId(player.equippedItemSlot1())[0] > 0) {
				SoundManager.loadBattleSoundEffects(getApplicationContext(),
						new int[] { (Integer) DefinitionItems.itemdata[player
								.equippedItemSlot1()][DefinitionItems.ITEM_SOUND_CLIP][0] });
			}
		}
		if (player.equippedItemSlot2() > -1) {
			if (OwnedItems.getChargesOfItemId(player.equippedItemSlot2())[0] > 0) {
				SoundManager.loadBattleSoundEffects(getApplicationContext(),
						new int[] { (Integer) DefinitionItems.itemdata[player
								.equippedItemSlot2()][DefinitionItems.ITEM_SOUND_CLIP][0] });
			}
		}

		startButton.setEnabled(true);
	}

	private void setupViews() {
		playerNameView = (TextView) findViewById(R.id.startFightPlayerNameLabel);
		playerHPView = (TextView) findViewById(R.id.startFightPlayerHpLabel);
		playerAPView = (TextView) findViewById(R.id.startFightPlayerApLabel);
		monsterNameView = (TextView) findViewById(R.id.startFightMonsterNameLabel);
		monsterImageView = (ImageView) findViewById(R.id.fightStartImage);
		fightNameView = (TextView) findViewById(R.id.startFightFightLabel);
		startButton = (Button) findViewById(R.id.startFightBeginButton);
		startButton.setEnabled(false);
		fightInfoText = (TextView) findViewById(R.id.fightStartInfoText);

		progressFight1Text = (TextView) findViewById(R.id.progressFight1Text);
		progressFight2Text = (TextView) findViewById(R.id.progressFight2Text);
		progressFight3Text = (TextView) findViewById(R.id.progressFight3Text);
		progressFight4Text = (TextView) findViewById(R.id.progressFight4Text);
		progressFight5Text = (TextView) findViewById(R.id.progressFight5Text);

		progressRound1Text = (TextView) findViewById(R.id.progressRound1Text);
		progressRound2Text = (TextView) findViewById(R.id.progressRound2Text);
		progressRound3Text = (TextView) findViewById(R.id.progressRound3Text);
		progressRound4Text = (TextView) findViewById(R.id.progressRound4Text);
		progressRound5Text = (TextView) findViewById(R.id.progressRound5Text);

		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startButton.setEnabled(false);
				startButton.setVisibility(View.INVISIBLE);
				ProgressBar startFightSpinner = (ProgressBar) findViewById(R.id.startFightSpinner);
				startFightSpinner.setVisibility(View.VISIBLE);

				if (DBHandler.isOpen(getApplicationContext()))
					DBHandler.close();

				Intent i = new Intent(getApplicationContext(),
						com.alderangaming.wizardsencounters.ControllerCombat.class);
				Bundle b = new Bundle();

				// b.putSerializable("ownedItems", ownedItems);
				b.putSerializable("player", player);
				b.putSerializable("monster", monster);
				b.putInt("background", background);

				i.putExtras(b);

				// startActivityForResult(i, 3002);
				startActivity(i);
				finish();
			}

		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			alertDialog = new AlertDialog.Builder(this);
			alertDialog
					.setMessage(
							"Exiting this round will cause your player to lose all progress and dropped items. Are you sure you want to quit?")
					.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							if (DBHandler.isOpen(getApplicationContext())) {
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
					}).setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
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
	TextView monsterNameView;
	TextView fightNameView;
	ImageView monsterImageView;
	Button startButton;
	TextView fightInfoText;

	TextView progressFight1Text;
	TextView progressFight2Text;
	TextView progressFight3Text;
	TextView progressFight4Text;
	TextView progressFight5Text;

	TextView progressRound1Text;
	TextView progressRound2Text;
	TextView progressRound3Text;
	TextView progressRound4Text;
	TextView progressRound5Text;
}
