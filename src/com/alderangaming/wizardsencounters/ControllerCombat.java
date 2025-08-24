package com.alderangaming.wizardsencounters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.SeekBar;
import com.alderangaming.wizardsencounters.vfx.VfxOverlayView;
import com.alderangaming.wizardsencounters.vfx.ScreenFlashEffect;
import com.alderangaming.wizardsencounters.vfx.ShockwaveRingEffect;
import com.alderangaming.wizardsencounters.vfx.AmbientMotesEffect;
import com.alderangaming.wizardsencounters.vfx.SparkBurstEffect;
import com.alderangaming.wizardsencounters.vfx.RuneCircleEffect;
import com.alderangaming.wizardsencounters.vfx.ImpactShakeEffect;
import com.alderangaming.wizardsencounters.vfx.RainEffect;
import com.alderangaming.wizardsencounters.vfx.FloatingTextEffect;
import com.alderangaming.wizardsencounters.vfx.DotPulseEffect;
import com.alderangaming.wizardsencounters.vfx.DotAuraEffect;
import com.alderangaming.wizardsencounters.vfx.BackgroundVfxRegistry;
import com.alderangaming.wizardsencounters.vfx.TorchFireEffect;

public class ControllerCombat extends Activity {

	private Player player;
	// private OwnedItems OwnedItems;
	private Monster monster;

	/* Flags */
	private boolean showingAbilityList = false;
	private boolean showingAttackTypeList = false;
	private boolean showingEffectsList = false;
	private boolean inCombat = false;
	private boolean waitingForDodge = false;
	private boolean castingWaitToApplyEffectPlayer = false;
	private boolean castingWaitToApplyEffectMonster = false;
	private boolean castingWaitToApplyDamagePlayer = false;
	private boolean castingWaitToApplyDamageMonster = false;
	private int turnFlag = 0;
	private int backgroundImage = -1;
	private int showImpact = 1;

	private Set<Integer> knownMonsterAbilities;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (!DBHandler.isOpen(getApplicationContext()))
			DBHandler.open(getApplicationContext());

		Bundle b = getIntent().getExtras();

		showImpact = DBHandler.getImpactPref(getApplicationContext());

		// OwnedItems = (OwnedItems) b.getSerializable("OwnedItems");
		player = (Player) b.getSerializable("player");
		monster = (Monster) b.getSerializable("monster");
		backgroundImage = b.getInt("background");

		setContentView(R.layout.combat);

		setupViews();

		setupSettingsDialog();
		setupAboutDialog();
		setupAbilityListAdapter();
		implementAbilityAdapter();
		setupAttackTypeListAdapter();
		implementAttackTypeAdapter();

		updateViews();

		if (player.name().length() > 5 && player.name().substring(0, 5).equals(DefinitionGlobal.TESTCHAR)) {
			player.setCurrentAP(999);
			player.setCurrentHP(999);
		}

		start();

	}

	private void setupViews() {
		combatLayout = (RelativeLayout) findViewById(R.id.combatImageLayout);
		combatLayout.setBackgroundResource(backgroundImage);
		playerBar = (LinearLayout) findViewById(R.id.combatPlayerBar);
		redHitImage = (ImageView) findViewById(R.id.combatHitImage);
		combatBottomLayout = (LinearLayout) findViewById(R.id.combatBottomLayout);

		impactImage = (ImageView) findViewById(R.id.combatImpactImage);

		// settings button on combat screen
		try {
			ImageButton settingsBtn = (ImageButton) findViewById(R.id.combatSettingsButton);
			if (settingsBtn != null) {
				settingsBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						showSettings();
					}
				});
			}
		} catch (Exception ignored) {
		}

		// Ensure VFX overlay is present (add programmatically once)
		try {
			// randomly enable rain on some maps (25%)
			boolean enableRain = Helper.randomInt(100) < 25;

			if (ambientOverlay == null) {
				ambientOverlay = new VfxOverlayView(this);
				RelativeLayout.LayoutParams lpAmbient = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.MATCH_PARENT);
				// Insert at index 0 so it's behind monster/UI but above layout background
				combatLayout.addView(ambientOverlay, 0, lpAmbient);
				// Only show motes when rain is NOT active
				if (!enableRain) {
					ambientOverlay.setAmbient(new AmbientMotesEffect());
				}
			}

			if (vfxOverlay == null) {
				vfxOverlay = new VfxOverlayView(this);
				RelativeLayout.LayoutParams lpTop = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.MATCH_PARENT);
				combatLayout.addView(vfxOverlay, lpTop);
				vfxOverlay.bringToFront();

				if (enableRain) {
					vfxOverlay.addEffect(new RainEffect(0x55AACCFF, 0));
				}

				// Attach torch fire if this background defines anchors
				try {
					float density = getResources().getDisplayMetrics().density;
					java.util.List<com.alderangaming.wizardsencounters.vfx.TorchFireGroup> groups = BackgroundVfxRegistry
							.getTorchGroups(this, backgroundImage);
					if (groups != null && !groups.isEmpty()) {
						for (com.alderangaming.wizardsencounters.vfx.TorchFireGroup g : groups) {
							vfxOverlay.addEffect(new TorchFireEffect(g.anchors01, 1.0f, density, g.params));
						}
					} else {
						float[][] anchors = BackgroundVfxRegistry.getTorchAnchors(this, backgroundImage);
						if (anchors != null && anchors.length > 0) {
							vfxOverlay.addEffect(new TorchFireEffect(
									anchors,
									1.0f,
									density,
									BackgroundVfxRegistry.getTorchParams(this, backgroundImage)));
						}
					}
				} catch (Exception ignored) {
				}
			}
		} catch (Exception ignored) {
		}

		monsterBar = (LinearLayout) findViewById(R.id.monsterBar);
		expandedMonsterBar = (LinearLayout) findViewById(R.id.expandedMonsterBar);
		expandedMonsterName = (TextView) findViewById(R.id.expandedMonsterName);
		expandedMonsterHP = (TextView) findViewById(R.id.expandedMonsterHP);
		expandedMonsterAP = (TextView) findViewById(R.id.expandedMonsterAP);
		expandedMonsterAbilities = (TextView) findViewById(R.id.expandedMonsterAbilities);

		if (!player.name().contains(DefinitionGlobal.TESTCHAR) && !player.name().contains(DefinitionGlobal.TESTCHAR2))
			expandedMonsterBar.setVisibility(View.INVISIBLE);
		else {
			expandedMonsterBar.setVisibility(View.VISIBLE);
			monsterBar.setVisibility(View.INVISIBLE);
		}

		execText = (TextView) findViewById(R.id.combatExecText);
		reacText = (TextView) findViewById(R.id.combatReactionText);
		knowText = (TextView) findViewById(R.id.combatKnowledgeText);
		mageText = (TextView) findViewById(R.id.combatMageloreText);
		luckText = (TextView) findViewById(R.id.combatLuckText);

		playerEffect1Image = (ImageView) findViewById(R.id.playerEffect1Image);
		playerEffect2Image = (ImageView) findViewById(R.id.playerEffect2Image);
		playerEffect3Image = (ImageView) findViewById(R.id.playerEffect3Image);
		playerEffect4Image = (ImageView) findViewById(R.id.playerEffect4Image);
		playerEffect5Image = (ImageView) findViewById(R.id.playerEffect5Image);
		playerEffect6Image = (ImageView) findViewById(R.id.playerEffect6Image);
		playerEffect7Image = (ImageView) findViewById(R.id.playerEffect7Image);
		playerEffect8Image = (ImageView) findViewById(R.id.playerEffect8Image);
		playerEffect9Image = (ImageView) findViewById(R.id.playerEffect9Image);
		playerEffect10Image = (ImageView) findViewById(R.id.playerEffect10Image);
		playerEffect11Image = (ImageView) findViewById(R.id.playerEffect11Image);
		playerEffect12Image = (ImageView) findViewById(R.id.playerEffect12Image);
		playerEffect13Image = (ImageView) findViewById(R.id.playerEffect13Image);
		playerEffect14Image = (ImageView) findViewById(R.id.playerEffect14Image);

		monsterEffect1Image = (ImageView) findViewById(R.id.monsterEffect1Image);
		monsterEffect2Image = (ImageView) findViewById(R.id.monsterEffect2Image);
		monsterEffect3Image = (ImageView) findViewById(R.id.monsterEffect3Image);
		monsterEffect4Image = (ImageView) findViewById(R.id.monsterEffect4Image);
		monsterEffect5Image = (ImageView) findViewById(R.id.monsterEffect5Image);
		monsterEffect6Image = (ImageView) findViewById(R.id.monsterEffect6Image);
		monsterEffect7Image = (ImageView) findViewById(R.id.monsterEffect7Image);
		monsterEffect8Image = (ImageView) findViewById(R.id.monsterEffect8Image);
		monsterEffect9Image = (ImageView) findViewById(R.id.monsterEffect9Image);
		monsterEffect10Image = (ImageView) findViewById(R.id.monsterEffect10Image);
		monsterEffect11Image = (ImageView) findViewById(R.id.monsterEffect11Image);
		monsterEffect12Image = (ImageView) findViewById(R.id.monsterEffect12Image);
		monsterEffect13Image = (ImageView) findViewById(R.id.monsterEffect13Image);
		monsterEffect14Image = (ImageView) findViewById(R.id.monsterEffect14Image);

		monsterEffectImages[0] = monsterEffect1Image;
		monsterEffectImages[1] = monsterEffect2Image;
		monsterEffectImages[2] = monsterEffect3Image;
		monsterEffectImages[3] = monsterEffect4Image;
		monsterEffectImages[4] = monsterEffect5Image;
		monsterEffectImages[5] = monsterEffect6Image;
		monsterEffectImages[6] = monsterEffect7Image;
		monsterEffectImages[7] = monsterEffect8Image;
		monsterEffectImages[8] = monsterEffect9Image;
		monsterEffectImages[9] = monsterEffect10Image;
		monsterEffectImages[10] = monsterEffect11Image;
		monsterEffectImages[11] = monsterEffect12Image;
		monsterEffectImages[12] = monsterEffect13Image;
		monsterEffectImages[13] = monsterEffect14Image;

		playerEffectImages[0] = playerEffect1Image;
		playerEffectImages[1] = playerEffect2Image;
		playerEffectImages[2] = playerEffect3Image;
		playerEffectImages[3] = playerEffect4Image;
		playerEffectImages[4] = playerEffect5Image;
		playerEffectImages[5] = playerEffect6Image;
		playerEffectImages[6] = playerEffect7Image;
		playerEffectImages[7] = playerEffect8Image;
		playerEffectImages[8] = playerEffect9Image;
		playerEffectImages[9] = playerEffect10Image;
		playerEffectImages[10] = playerEffect11Image;
		playerEffectImages[11] = playerEffect12Image;
		playerEffectImages[12] = playerEffect13Image;
		playerEffectImages[13] = playerEffect14Image;

		playerNameView = (TextView) findViewById(R.id.combatPlayerNameLabel);
		playerHPView = (TextView) findViewById(R.id.combatPlayerHpLabel);
		playerAPView = (TextView) findViewById(R.id.combatPlayerApLabel);
		monsterNameView = (TextView) findViewById(R.id.combatMonsterName);
		monsterHealthView = (TextView) findViewById(R.id.combatMonsterHealth);

		// logScrollView = (ScrollView) findViewById(R.id.logScrollView);
		logView = (TextView) findViewById(R.id.logTextView);
		logView.setText("");

		playerAttackText = (TextView) findViewById(R.id.combatPlayerAttackText);
		monsterAttackText = (TextView) findViewById(R.id.combatMonsterAttackText);
		playerAbilityText = (TextView) findViewById(R.id.combatPlayerAbilityText);

		playerEffectsTable = (TableLayout) findViewById(R.id.playerEffectsTable);
		// playerEffectsTable.setVisibility(View.INVISIBLE);

		monsterEffectsTable = (TableLayout) findViewById(R.id.monsterEffectsTable);
		// monsterEffectsTable.setVisibility(View.INVISIBLE);

		abilitiesList = (ListView) findViewById(R.id.combatAbilitiesList);
		abilitiesList.setVisibility(View.GONE);

		attackList = (ListView) findViewById(R.id.combatAttackList);
		attackList.setVisibility(View.GONE);

		effectsList = (ListView) findViewById(R.id.combatEffectsList);
		effectsList.setVisibility(View.GONE);

		hitButton = (ImageButton) findViewById(R.id.combatHitButton);
		abilityButton = (ImageButton) findViewById(R.id.combatAbilityButton);
		fleeButton = (ImageButton) findViewById(R.id.combatRunButton);
		final ImageButton infoButton = (ImageButton) findViewById(R.id.combatInfoButton);

		item1Text = (TextView) findViewById(R.id.combatItem1Text);
		item1Button = (ImageButton) findViewById(R.id.combatItem1Button);
		item1Button.setEnabled(false);

		item2Text = (TextView) findViewById(R.id.combatItem2Text);
		item2Button = (ImageButton) findViewById(R.id.combatItem2Button);
		item2Button.setEnabled(false);

		dodgeButton = (Button) findViewById(R.id.dodgeButton);
		dodgeButton.setVisibility(View.INVISIBLE);

		monsterImage = (ImageView) findViewById(R.id.combatMonsterImage);
		monsterImage.setImageResource(monster.imageResource());

		attackImage = (ImageView) findViewById(R.id.combatAttackImage);
		attackImage.setImageResource(0);
		attackImage.setVisibility(View.INVISIBLE);

		setupTouchHandlers();
	}

	private void setupTouchHandlers() {
		playerEffectsTable.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN: {
						if (!showingEffectsList) {
							showingEffectsList = true;
							if (effectListAdapter == null) {
								showEffectsList(0);
							} else {
								updateEffectsList(0);
							}

							effectsList.setVisibility(View.VISIBLE);
						}
					}
						break;
					case MotionEvent.ACTION_UP: {
						showingEffectsList = false;
						effectsList.setVisibility(View.INVISIBLE);
					}
						break;
				}
				return true;
			}
		});

		monsterEffectsTable.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN: {
						if (!showingEffectsList) {
							showingEffectsList = true;
							if (effectListAdapter == null) {
								showEffectsList(1);
							} else {
								updateEffectsList(1);
							}

							effectsList.setVisibility(View.VISIBLE);
						}
					}
						break;
					case MotionEvent.ACTION_UP: {
						showingEffectsList = false;
						effectsList.setVisibility(View.INVISIBLE);
					}
						break;
				}
				return true;
			}
		});

		dodgeButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (!dodgeButton.isEnabled())
					return true;

				switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						checkDodge();
						break;
				}
				return true;
			}
		});

		hitButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (!hitButton.isEnabled())
					return true;

				switch (event.getAction()) {

					case MotionEvent.ACTION_UP:
						hitButton.setImageResource(R.drawable.buttonhitup);
						handleHitButton();
						break;

					case MotionEvent.ACTION_DOWN:
						hitButton.setImageResource(R.drawable.buttonhitdown);
						break;
				}
				return true;
			}
		});

		abilityButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (!abilityButton.isEnabled())
					return true;

				switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						abilityButton.setImageResource(R.drawable.buttonabilityup);
						handleAbilityButton();
						break;

					case MotionEvent.ACTION_DOWN:
						abilityButton.setImageResource(R.drawable.buttonabilitydown);
						break;
				}
				return true;
			}
		});

		fleeButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (!fleeButton.isEnabled())
					return true;

				switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						fleeButton.setImageResource(R.drawable.buttonfleeup);
						handleFleeButton();
						break;

					case MotionEvent.ACTION_DOWN:
						fleeButton.setImageResource(R.drawable.buttonfleedown);
						break;
				}
				return true;
			}
		});

		View infoBtn = findViewById(R.id.combatInfoButton);
		if (infoBtn != null) {
			infoBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showAbilitiesInfoDialog();
				}
			});
		}

		item1Button.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (!item1Button.isEnabled())
					return true;

				switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						item1Button.setAlpha(1.0f);
						handleUseItem1Button();
						break;

					case MotionEvent.ACTION_DOWN:
						item1Button.setAlpha(0.7f);
						break;
				}
				return true;
			}
		});

		item2Button.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (!item2Button.isEnabled())
					return true;

				switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						item2Button.setAlpha(1.0f);
						handleUseItem2Button();
						break;

					case MotionEvent.ACTION_DOWN:
						item2Button.setAlpha(0.7f);
						break;
				}
				return true;
			}
		});
	}

	private void handleFleeButton() {

		if (player.playerAttackWait() == false) {
			player.setPlayerAttackWait(true);
			updateButtons();
		} else
			return;

		int runroll = Helper.randomInt(101);
		int runchance = player.getRunChance() + 50;

		if (runchance < runroll) {
			appendLog("You failed to run away!", DefinitionGlobal.LOG_TYPE_ACTION_FAILED);
			endPlayerAttack();
		} else {
			showRunAwayDialog();
		}
	}

	private void handleAbilityButton() {
		if (player.playerAttackWait() == false || showingAbilityList) {
			if (!showingAbilityList) {
				player.setPlayerAttackWait(true);
				showingAbilityList = true;
				abilitiesList.setVisibility(View.VISIBLE);
			} else {
				player.setPlayerAttackWait(false);
				showingAbilityList = false;
				abilitiesList.setVisibility(View.INVISIBLE);
			}

			updateButtons();
		}
	}

	private void handleHitButton() {
		hitButton.setImageResource(R.drawable.buttonhitup);

		if (player.playerAttackWait() == false || showingAttackTypeList) {
			if (!showingAttackTypeList) {
				player.setPlayerAttackWait(true);
				showingAttackTypeList = true;
				attackList.setVisibility(View.VISIBLE);
			} else {
				player.setPlayerAttackWait(false);
				showingAttackTypeList = false;
				attackList.setVisibility(View.INVISIBLE);
			}

			updateButtons();
		}
	}

	private void updateStatText() {
		execText.setText("EXEC:" + player.exec());
		if (player.execDiff() < 0) {
			execText.setTextColor(Color.RED);
		} else if (player.execDiff() > 0) {
			execText.setTextColor(Color.GREEN);
		} else {
			execText.setTextColor(Color.WHITE);
		}

		reacText.setText("REAC:" + player.reaction());
		if (player.reacDiff() < 0) {
			reacText.setTextColor(Color.RED);
		} else if (player.reacDiff() > 0) {
			reacText.setTextColor(Color.GREEN);
		} else {
			reacText.setTextColor(Color.WHITE);
		}

		knowText.setText("KNOW:" + player.knowledge());
		if (player.knowDiff() < 0) {
			knowText.setTextColor(Color.RED);
		} else if (player.knowDiff() > 0) {
			knowText.setTextColor(Color.GREEN);
		} else {
			knowText.setTextColor(Color.WHITE);
		}

		mageText.setText("MAGE:" + player.magelore());
		if (player.mageDiff() < 0) {
			mageText.setTextColor(Color.RED);
		} else if (player.mageDiff() > 0) {
			mageText.setTextColor(Color.GREEN);
		} else {
			mageText.setTextColor(Color.WHITE);
		}

		luckText.setText("LUCK:" + player.luck());
		if (player.luckDiff() < 0) {
			luckText.setTextColor(Color.RED);
		} else if (player.luckDiff() > 0) {
			luckText.setTextColor(Color.GREEN);
		} else {
			luckText.setTextColor(Color.WHITE);
		}
	}

	private void updateViews() {
		playerNameView.setText(player.name());
		playerHPView.setText("HP: " + player.currentHP() + "/" + player.maxHP());
		playerAPView.setText("AP: " + player.currentAP() + "/" + player.maxAP());

		// flash HP/AP on change
		try {
			if (lastPlayerHP != player.currentHP()) {
				flashWhite(playerHPView);
				lastPlayerHP = player.currentHP();
			}

			if (lastPlayerAP != player.currentAP()) {
				flashWhite(playerAPView);
				lastPlayerAP = player.currentAP();
			}

			if (player.currentHP() < 15 && !lowHpPulseActive) {
				lowHpPulseActive = true;
				playerHPView.post(new Runnable() {
					@Override
					public void run() {
						if (player.currentHP() >= 15) {
							lowHpPulseActive = false;
							return;
						}
						flashWhite(playerHPView);
						playerHPView.postDelayed(this, 700);
					}
				});
			}
		} catch (Exception ignored) {
		}

		if (expandedMonsterBar.getVisibility() == View.VISIBLE) {
			expandedMonsterName.setText(monster.name());
			expandedMonsterHP.setText("HP:" + monster.currentHP() + "/" + monster.maxHP());
			expandedMonsterAP.setText("AP:" + monster.currentAP() + "/" + monster.maxAP());
			String monsterAbils = "";
			for (int a = 0; a < monster.getActiveAbilities().length; a++) {
				if (a != 0)
					monsterAbils += ", ";

				monsterAbils += DefinitionRunes.runeData[monster
						.getActiveAbilityByIndex(a)][DefinitionRunes.RUNE_NAMES][0];
			}
			expandedMonsterAbilities.setText(monsterAbils);
		} else {
			monsterNameView.setText(monster.name());
			monsterHealthView.setText(monster.health());
		}

		// monsterImage.setImageResource(monster.imageResource());

		updateButtons();

		updateStatText();

		displayPlayerEffects();
		displayMonsterEffects();

		updateAbilityListAdapter();
	}

	private void flashWhite(final View v) {
		if (v == null)
			return;
		if (v instanceof android.widget.TextView) {
			final android.widget.TextView tv = (android.widget.TextView) v;
			final int orig = tv.getCurrentTextColor();
			final int white = 0xFFFFFFFF;
			try {
				tv.setTextColor(white);
			} catch (Exception ignored) {
			}
			tv.postDelayed(new Runnable() {
				@Override
				public void run() {
					try {
						tv.setTextColor(orig);
					} catch (Exception ignored) {
					}
				}
			}, 120);
			tv.postDelayed(new Runnable() {
				@Override
				public void run() {
					try {
						tv.setTextColor(white);
					} catch (Exception ignored) {
					}
				}
			}, 220);
			tv.postDelayed(new Runnable() {
				@Override
				public void run() {
					try {
						tv.setTextColor(orig);
					} catch (Exception ignored) {
					}
				}
			}, 360);
		} else {
			final android.graphics.drawable.Drawable original = v.getBackground();
			v.setBackgroundColor(0xFFFFFFFF);
			v.postDelayed(new Runnable() {
				@Override
				public void run() {
					try {
						v.setBackgroundDrawable(original);
					} catch (Exception ignored) {
					}
				}
			}, 150);
		}
	}

	private void disableCombatButtons() {
		hitButton.setEnabled(false);
		hitButton.setImageResource(R.drawable.buttonhitdisabled);

		abilityButton.setEnabled(false);
		abilityButton.setImageResource(R.drawable.buttonabilitydisabled);

		fleeButton.setEnabled(false);
		fleeButton.setImageResource(R.drawable.buttonfleedisabled);

		disableItemButtons();
	}

	private void disableItemButtons() {
		item1Button.setEnabled(false);
		item2Button.setEnabled(false);
	}

	private void updateItemButtons() {
		if (player.equippedItemSlot1() >= 0) {
			item1Button.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_ITEM,
					player.equippedItemSlot1()));

			item1Text.setText(OwnedItems.getChargesOfItemId(player.equippedItemSlot1())[0] + "/"
					+ OwnedItems.getChargesOfItemId(player.equippedItemSlot1())[1]);

			if (OwnedItems.getChargesOfItemId(player.equippedItemSlot1())[0] > 0) {
				item1Button.setEnabled(true);
			} else {
				// show no-charges plate
				item1Button.setImageResource(R.drawable.nochargesbutton);
				item1Button.setEnabled(false);
			}
		} else {
			item1Text.setText("0/0");
			// show no-item plate
			item1Button.setImageResource(R.drawable.noitembutton);
			item1Button.setEnabled(false);
		}

		if (player.equippedItemSlot2() >= 0) {
			item2Button.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_ITEM,
					player.equippedItemSlot2()));

			item2Text.setText(OwnedItems.getChargesOfItemId(player.equippedItemSlot2())[0] + "/"
					+ OwnedItems.getChargesOfItemId(player.equippedItemSlot2())[1]);

			if (OwnedItems.getChargesOfItemId(player.equippedItemSlot2())[0] > 0) {
				item2Button.setEnabled(true);
			} else {
				// show no-charges plate
				item2Button.setImageResource(R.drawable.nochargesbutton);
				item2Button.setEnabled(false);
			}
		} else {
			item2Text.setText("0/0");
			// show no-item plate
			item2Button.setImageResource(R.drawable.noitembutton);
			item2Button.setEnabled(false);
		}
	}

	private void updateButtons() {
		if (inCombat) {

			updateItemButtons();

			if (player.playerAttackWait()) {
				hitButton.setEnabled(false);
				abilityButton.setEnabled(false);
				fleeButton.setEnabled(false);

				if (!showingAbilityList) {
					abilityButton.setEnabled(false);
					abilityButton.setImageResource(R.drawable.buttonabilitydisabled);
				} else {
					abilityButton.setEnabled(true);
					abilityButton.setImageResource(R.drawable.buttonabilitydown);
				}
				if (!showingAttackTypeList) {
					hitButton.setEnabled(false);
					hitButton.setImageResource(R.drawable.buttonhitdisabled);
				} else {
					hitButton.setEnabled(true);
					hitButton.setImageResource(R.drawable.buttonhitdown);
				}

				fleeButton.setImageResource(R.drawable.buttonfleedisabled);

			} else {
				hitButton.setEnabled(true);
				abilityButton.setEnabled(true);
				fleeButton.setEnabled(true);

				hitButton.setImageResource(R.drawable.buttonhitup);
				abilityButton.setImageResource(R.drawable.buttonabilityup);
				fleeButton.setImageResource(R.drawable.buttonfleeup);

			}
		} else if (player.dead()) {
			player.setRank(1);
			player.setCurrentFight(0);
			player.setCurrentRound(1);
			DBHandler.updatePlayer(player);
			showPlayerDiedDialog();
		}
	}

	private void showPlayerDiedDialog() {
		AlertDialog.Builder playerDiedDialog = new AlertDialog.Builder(this);

		SoundManager.playSound(SoundManager.PLAYERDEAD, true);

		playerDiedDialog
				.setTitle("Failure")
				.setCancelable(false)
				.setMessage(
						"You are dead! All progress and items earned by this player have been consumed by the "
								+ monster.name() + ".")
				.setPositiveButton("Die", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						killPlayer();
					}
				});

		AlertDialog alert = playerDiedDialog.create();
		alert.show();
	}

	private void killPlayer() {
		if (DBHandler.killPlayer(player.playerID())) {
			DBHandler.close();
		}

		Intent resultIntent = new Intent();
		Bundle b = new Bundle();

		// b.putSerializable("OwnedItems", OwnedItems);
		b.putSerializable("player", player);

		resultIntent.putExtras(b);

		setResult(Activity.RESULT_OK, resultIntent);

		finish();
	}

	private void updateAbilityListAdapter() {
		String[] abNames = new String[player.getActiveAbilities().length];
		String[] abCost = new String[player.getActiveAbilities().length];
		String[] abDescriptions = new String[player.getActiveAbilities().length];

		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < abNames.length; i++) {
			abNames[i] = (String) DefinitionRunes.runeData[player
					.getActiveAbilityByIndex(i)][DefinitionRunes.RUNE_NAMES][0];

			if (player.hasFreeAbility()) {
				abCost[i] = "0";
			} else
				abCost[i] = ""
						+ (Integer) DefinitionRunes.runeData[player
								.getActiveAbilityByIndex(i)][DefinitionRunes.RUNE_AP_COST][0];

			abDescriptions[i] = (String) DefinitionRunes.runeData[player
					.getActiveAbilityByIndex(i)][DefinitionRunes.RUNE_DESCRIPTION][0];

			rows.add(Helper.createMap(abNames[i], abCost[i], abDescriptions[i]));
		}

		String[] fromKeys = new String[] { "Name", "APCost", "Description" };
		int[] toIds = new int[] { R.id.abilityListName, R.id.abilityListCost, R.id.abilityListDescription };

		abilitiesList.setAdapter(new SimpleAdapter(this, rows, R.layout.abilitylistitem, fromKeys, toIds));

		abilitiesList.invalidate();
	}

	private void setupAbilityListAdapter() {

		TextView t = new TextView(this);
		t.setText("Abilities");
		abilitiesList.addHeaderView(t);

		String[] abNames = new String[player.getActiveAbilities().length];
		String[] abCost = new String[player.getActiveAbilities().length];
		String[] abDescriptions = new String[player.getActiveAbilities().length];

		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < abNames.length; i++) {
			abNames[i] = (String) DefinitionRunes.runeData[player
					.getActiveAbilityByIndex(i)][DefinitionRunes.RUNE_NAMES][0];

			if (player.hasFreeAbility()) {
				abCost[i] = "0";
			} else
				abCost[i] = ""
						+ (Integer) DefinitionRunes.runeData[player
								.getActiveAbilityByIndex(i)][DefinitionRunes.RUNE_AP_COST][0];

			abDescriptions[i] = (String) DefinitionRunes.runeData[player
					.getActiveAbilityByIndex(i)][DefinitionRunes.RUNE_DESCRIPTION][0];

			rows.add(Helper.createMap(abNames[i], abCost[i], abDescriptions[i]));
		}

		String[] fromKeys = new String[] { "Name", "APCost", "Description" };
		int[] toIds = new int[] { R.id.abilityListName, R.id.abilityListCost, R.id.abilityListDescription };

		abilitiesList.setAdapter(new SimpleAdapter(this, rows, R.layout.abilitylistitem, fromKeys, toIds));

		abilitiesList.invalidate();

	}

	private void updateEffectsList(int who) {
		int numEffects = 0;

		// effectsList.removeHeaderView(effectsTitle);

		if (who == 0)
			numEffects = player.getActiveEffects().size();
		else
			numEffects = monster.getActiveEffects().size();

		if (numEffects < 1)
			return;

		String[] efNames = new String[numEffects];
		String[] efTurns = new String[numEffects];
		String[] efDescriptions = new String[numEffects];
		int[] efImages = new int[numEffects];

		effectAdapterData.clear();

		if (who == 0) {
			for (int i = 0; i < efNames.length; i++) {
				efNames[i] = player.getActiveEffectByIndex(i).name();
				efTurns[i] = "Turns: " + player.getActiveEffectByIndex(i).turnsRemaining();
				efDescriptions[i] = player.getActiveEffectByIndex(i).description();
				efImages[i] = player.getActiveEffectByIndex(i).imageResource();

				effectAdapterData.add(Helper.createEffectMap(efNames[i], efTurns[i], efDescriptions[i], efImages[i]));
			}
		} else {
			for (int i = 0; i < efNames.length; i++) {
				efNames[i] = monster.getActiveEffectByIndex(i).name();
				efTurns[i] = "Turns: " + monster.getActiveEffectByIndex(i).turnsRemaining();
				efDescriptions[i] = monster.getActiveEffectByIndex(i).description();
				efImages[i] = monster.getActiveEffectByIndex(i).imageResource();

				effectAdapterData.add(Helper.createEffectMap(efNames[i], efTurns[i], efDescriptions[i], efImages[i]));
			}
		}

		if (effectListAdapter != null) {
			effectListAdapter.notifyDataSetChanged();
			effectsList.invalidate();
		}
	}

	private void showEffectsList(int who) {

		String[] fromKeys = new String[] { "effectName", "effectDescription", "effectTurns", "effectImage" };

		int[] toIds = new int[] { R.id.effectListName, R.id.effectListDescription, R.id.effectListTurns,
				R.id.effectListImage };

		updateEffectsList(who);

		effectListAdapter = new SimpleAdapter(this, effectAdapterData, R.layout.effectlistitem, fromKeys, toIds);
		effectsList.setAdapter(effectListAdapter);

		updateEffectsList(who);
	}

	private void setupAttackTypeListAdapter() {
		TextView t = new TextView(this);
		t.setText(DefinitionWeapons.WEAPON_NAMES[player.equippedWeapon()] + ": " + player.getDamageRange()[0] + "-"
				+ player.getDamageRange()[1] + " dmg");
		t.setBackgroundColor(Color.BLACK);
		attackList.addHeaderView(t);

		String[] atNames = new String[DefinitionWeapons.WEAPON_ATTACK_TYPES[player.equippedWeapon()].length];
		String[] atStat1 = new String[DefinitionWeapons.WEAPON_ATTACK_TYPES[player.equippedWeapon()].length];
		String[] atStat2 = new String[DefinitionWeapons.WEAPON_ATTACK_TYPES[player.equippedWeapon()].length];
		String[] atStat3 = new String[DefinitionWeapons.WEAPON_ATTACK_TYPES[player.equippedWeapon()].length];
		String[] atStat4 = new String[DefinitionWeapons.WEAPON_ATTACK_TYPES[player.equippedWeapon()].length];

		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < atNames.length; i++) {
			atNames[i] = ((ItemWeapon) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_WEAPON,
					player.equippedWeapon()))
					.getAttackTypeByIndex(i).name;

			int hitStat = ((ItemWeapon) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_WEAPON,
					player.equippedWeapon()))
					.getAttackTypeByIndex(i).hitChance;

			int critStat = ((ItemWeapon) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_WEAPON,
					player.equippedWeapon()))
					.getAttackTypeByIndex(i).critChance;

			int stunStat = ((ItemWeapon) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_WEAPON,
					player.equippedWeapon()))
					.getAttackTypeByIndex(i).stunChance;

			int blockStat = ((ItemWeapon) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_WEAPON,
					player.equippedWeapon()))
					.getAttackTypeByIndex(i).blockChance;

			if (hitStat > 0)
				atStat1[i] = "+" + hitStat + "% hit";

			if (critStat > 0)
				atStat2[i] = "+" + critStat + "% crit";

			if (stunStat > 0)
				atStat3[i] = "+" + stunStat + "% stun";

			if (blockStat > 0)
				atStat4[i] = "+" + blockStat + "% block";

			data.add(Helper.createAttackTypeMap(atNames[i], atStat1[i], atStat2[i], atStat3[i], atStat4[i]));
		}
		AttackTypeAdapter adapter = new AttackTypeAdapter(data);
		attackList.setAdapter(adapter);

		attackList.invalidate();
	}

	private class AttackTypeAdapter extends BaseAdapter {
		List<HashMap<String, String>> data;
		LayoutInflater inflater = null;

		public AttackTypeAdapter(List<HashMap<String, String>> d) {
			data = d;
			inflater = (LayoutInflater) ControllerCombat.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return data.size();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View vi = convertView;
			if (convertView == null)
				vi = inflater.inflate(R.layout.attacklistitem, null);

			TextView name = (TextView) vi.findViewById(R.id.attackListName);
			TextView stat1 = (TextView) vi.findViewById(R.id.attackListStat1);
			stat1.setVisibility(View.VISIBLE);
			TextView stat2 = (TextView) vi.findViewById(R.id.attackListStat2);
			stat2.setVisibility(View.VISIBLE);
			TextView stat3 = (TextView) vi.findViewById(R.id.attackListStat3);
			stat3.setVisibility(View.VISIBLE);
			TextView stat4 = (TextView) vi.findViewById(R.id.attackListStat4);
			stat4.setVisibility(View.VISIBLE);

			HashMap<String, String> item = new HashMap<String, String>();
			item = data.get(position);

			// Setting all values in listview

			name.setText(item.get("Name"));

			if (item.get("Stat1") != null)
				stat1.setText(item.get("Stat1"));
			else
				stat1.setVisibility(View.GONE);

			if (item.get("Stat2") != null)
				stat2.setText(item.get("Stat2"));
			else
				stat2.setVisibility(View.GONE);

			if (item.get("Stat3") != null)
				stat3.setText(item.get("Stat3"));
			else
				stat3.setVisibility(View.GONE);

			if (item.get("Stat4") != null)
				stat4.setText(item.get("Stat4"));
			else
				stat4.setVisibility(View.GONE);

			return vi;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	private void implementAttackTypeAdapter() {
		attackList.setVisibility(View.INVISIBLE);
		attackList.invalidate();
		attackList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int index, long l) {
				if (index == 0)
					return;

				index = index - 1;

				Log.d("combat", "clicked on attacktype index " + (index));

				int attackTypeIndex = DefinitionWeapons.WEAPON_ATTACK_TYPES[player.equippedWeapon()][index];

				Log.d("combat", "clicked on attacktypeId is " + attackTypeIndex);

				attackList.setVisibility(View.INVISIBLE);

				showingAttackTypeList = false;

				playerHitMonster(attackTypeIndex);
			}
		});
	}

	private void implementAbilityAdapter() {

		abilitiesList.setVisibility(View.INVISIBLE);

		abilitiesList.invalidate();

		abilitiesList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, final int index, long l) {
				if (index == 0)
					return;

				abilitiesList.setVisibility(View.INVISIBLE);

				int newIndex = index - 1;

				showingAbilityList = false;

				if (!player.hasFreeAbility()
						&& (Integer) DefinitionRunes.runeData[player
								.getActiveAbilityByIndex(newIndex)][DefinitionRunes.RUNE_AP_COST][0] > player
										.currentAP()) {
					appendLog("Insufficient AP to use this.", DefinitionGlobal.LOG_TYPE_ACTION_FAILED);
					player.setPlayerAttackWait(false);
					updateButtons();
					return;
				}

				if (player.cannotUseAbilities()) {
					appendLog("You are too confused to do this!", DefinitionGlobal.LOG_TYPE_ACTION_FAILED);
					player.setPlayerAttackWait(false);
					updateButtons();
					return;
				}

				// DETERMINE IF ABILITY IS SUCCESSFUL
				int randomnumber = Helper.randomInt(101);

				randomnumber -= player.knowledge();

				if (randomnumber > (Integer) DefinitionRunes.runeData[player
						.getActiveAbilityByIndex(newIndex)][DefinitionRunes.RUNE_SUCCESS_CHANCE][0]) {
					appendLog(
							(String) DefinitionRunes.runeData[player
									.getActiveAbilityByIndex(newIndex)][DefinitionRunes.RUNE_NAMES][0]
									+ " failed!",
							DefinitionGlobal.LOG_TYPE_ACTION_FAILED);

					player.updateAP(-(Integer) DefinitionRunes.runeData[player
							.getActiveAbilityByIndex(newIndex)][DefinitionRunes.RUNE_AP_COST][0]);

					appendLog(
							"(rolled "
									+ (100 - randomnumber)
									+ ", needed > "
									+ (100 - (Integer) DefinitionRunes.runeData[player
											.getActiveAbilityByIndex(newIndex)][DefinitionRunes.RUNE_SUCCESS_CHANCE][0])
									+ ")",
							DefinitionGlobal.LOG_TYPE_ACTION_FAILED);
					clearPlayerAttackTextDelay(1.8);
					endPlayerAttack();
				} else
					useAbility(player.getActiveAbilityByIndex(newIndex), randomnumber);
			}
		});
	}

	private void start() {
		inCombat = true;

		// player's initiative roll
		int randomnumber1 = Helper.randomInt(50);
		int randomnumber2 = Helper.randomInt(50);

		int monsterScore = monster.initiative() + randomnumber1;
		int playerScore = player.initiative() + randomnumber2;

		Log.d("combat",
				"starting combat against rank " + monster.rank() + " " + monster.name() + ": " + monster.currentHP()
						+ "hp, " + monster.currentAP() + "ap");

		Log.d("combat", "start: mScore=" + monsterScore + " pScore=" + playerScore);

		// apply cursed effect on 0 initiative roll
		if (randomnumber2 < 1) {
			appendLog("You were totally caught off guard! " + monster.name() + " makes a sneak attack!",
					DefinitionGlobal.LOG_TYPE_SOMETHING_BAD_FOR_PLAYER);

		}

		if (playerScore >= monsterScore) {
			// player goes first
			Log.d("combat", "player goes first");
			player.setPlayerAttackWait(false);

			appendLog("Before you stands the dreaded " + monster.name() + "! It looks distracted.",
					DefinitionGlobal.LOG_TYPE_DEFAULT);
			updateViews();
		} else {
			Log.d("combat", "monster goes first");
			appendLog(monster.name() + " gets the first attack!", DefinitionGlobal.LOG_TYPE_DEFAULT);
			player.setPlayerAttackWait(true);
			updateViews();

			// wait some time then have the monster attack
			startMonsterAttackDelay(1);
		}
	}

	private void checkDodge() {
		// new DodgeEvent created before this was called
		dodgeButton.setEnabled(false);
		dodgeButton.setVisibility(View.INVISIBLE);
		try {
			if (dodgeWanderRunnable != null)
				dodgeWanderHandler.removeCallbacks(dodgeWanderRunnable);
		} catch (Exception ignored) {
		}

		if (waitingForDodge == true && randomDodgeID == dodgeEvent.dodgeID) {
			SoundManager.playSound(SoundManager.SOUND_TYPE_DODGE, true);
			waitingForDodge = false;
			appendLog("You dodged the attack!", DefinitionGlobal.LOG_TYPE_PLAYER_DODGES);
			endMonsterAttack();
		} else
			return;

		updateViews();
	}

	private void checkForArtifactDrop() {
		if (Helper.randomInt(100) > player.luck())
			return;

		String[] gems = { "Plague Cure Vial", "Voynich Manuscript Translation", "Coffee Seed",
				"Book: 'Build A Roman Road'",
				"Book: 'Innovative Conjurations'", "Book: 'A Kraken's Mating Habits'",
				"Book: 'Novel Sorceries For Grandpa'", "Book: 'Bane of the Knight'",
				"Book: 'The Mongol and The Misses'", "Book: 'The Folly of Spellcasting'",
				"Book: 'The Maiden and the Mage'", "Book: 'Friendly Dragons'", "Book: 'The Legend of the Purple Gar'",
				"Book: 'The Apprentice Garblin'", "Isolinear Optical Chip", "Sapphire Amulet", "Ruby Disc",
				"Skull Goblet", "Marble Vase", "Book: 'The Downfall of the Tollans'",
				"Book: 'Creating Your Own Prometheus'", "Book: 'The Lore of The Data'", "tube of sunscreen",
				"Tizard Mission Chest", "container of corn seeds", "map of the stars",
				"Book: 'Dragons Were Dinosaurs'", "Book: 'How To Replicate Anything'",
				"Book: 'You Are More Than Your Level!'", "Book: 'Beowulf Was My Friend'", "Book: 'Cosmos A-to-Zeus'",
				"Book: 'The Teachings of Surak'", "Book: 'How To Use Your DHD'",
				"Book: 'The Dialect of Walking Carpets'", "Book: 'The Book of Origin'", "Book: 'Make Your R2 Fly!'",
				"Book: 'Tape From Ducks'", "Book: 'Mind Control for Mages'", "Bag of Kanga Spice", "Dilithium Crystal",
				"Griffin Farthing", "Ancient Shekel", "Sage Relic" };

		int randomgem = Helper.randomInt(gems.length);
		int randomgold = randomgem + Helper.randomInt(player.rank() * 10);

		if (randomgold < 1)
			randomgold = 1;

		randomgold *= 3;

		if (gems[randomgem].equals("Ancient Shekel") || gems[randomgem].equals("Isolinear Optical Chip")) {
			appendLog("You were lucky and found an " + gems[randomgem] + " worth " + randomgold + " gold!",
					DefinitionGlobal.LOG_TYPE_LOOT);
		} else {
			appendLog("You were lucky and found a " + gems[randomgem] + " worth " + randomgold + " gold!",
					DefinitionGlobal.LOG_TYPE_LOOT);
		}

		OwnedItems.updateGold(randomgold);
	}

	private void startMonsterAttackDelay(double d) {

		try {
			startMonsterAttackHandler.removeCallbacks(startMonsterAttackPost);
			startMonsterAttackHandler.postDelayed(startMonsterAttackPost, (long) (d * 1000));
		} catch (Exception e) {
			startMonsterAttackPost.run();
		}
	}

	private Runnable startMonsterAttackPost = new Runnable() {
		public void run() {
			startMonsterAttack();
		}
	};

	private void showUseItemDialog(final int itemId) {
		AlertDialog.Builder useItemDialog = new AlertDialog.Builder(this);

		useItemDialog.setTitle(DefinitionItems.itemdata[itemId][DefinitionItems.ITEM_NAME][0].toString())
				.setMessage(DefinitionItems.itemdata[itemId][DefinitionItems.ITEM_DESCRIPTION][0].toString())
				.setPositiveButton("Use It", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						OwnedItems.useItemCharge(itemId);

						updateItemButtons();

						if (DBHandler.updateOwnedItems(OwnedItems.getOwnedItems()))
							useItem(itemId);

						dialog.cancel();

					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alert = useItemDialog.create();
		alert.show();
	}

	private void handleUseItem1Button() {
		if (OwnedItems.getChargesOfItemId(player.equippedItemSlot1())[0] <= 0)
			return;

		showUseItemDialog(player.equippedItemSlot1());
	}

	private void handleUseItem2Button() {
		if (OwnedItems.getChargesOfItemId(player.equippedItemSlot2())[0] <= 0)
			return;

		showUseItemDialog(player.equippedItemSlot2());

	}

	private void useItem(int itemId) {
		ItemItem item = new ItemItem(DefinitionGlobal.ITEM_TYPE_ITEM, itemId, getApplicationContext());
		int turns = 0;

		if (item.appliedOverNumTurnsAbsolute() > 0)
			turns = item.appliedOverNumTurnsAbsolute();

		if (item.appliedOverNumTurnsLevelFlag() == 1)
			turns = player.rank();

		Log.d("UseItem", "used item " + item.name());

		if ((Integer) DefinitionItems.itemdata[itemId][DefinitionItems.ITEM_SOUND_CLIP][0] != 0)
			SoundManager.playSound((Integer) DefinitionItems.itemdata[itemId][DefinitionItems.ITEM_SOUND_CLIP][0],
					false);
		else
			SoundManager.playSound(SoundManager.ITEMSOUND, true);

		appendLog("Used Item: " + item.name() + ".", DefinitionGlobal.LOG_TYPE_PLAYER_USE_ITEM);

		// modify hp % of max self
		if (item.modifyHPPercentOfMaxSelf() != 0) {
			int amt = Helper.getPercentFromInt(item.modifyHPPercentOfMaxSelf(), player.maxHP());

			int actualAmt = player.updateHP(amt);
			if (amt < 0)
				appendLog("You lost " + -amt + " HP.", DefinitionGlobal.LOG_TYPE_PLAYER_LOSES_HP);

			else
				appendLog("You gained " + actualAmt + " HP.", DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_HP);
		}
		// modify hp % of monster
		if (item.modifyHPPercentOfMaxMonster() != 0) {
			int amt = Helper.getPercentFromInt(item.modifyHPPercentOfMaxMonster(), monster.maxHP());

			int actualAmt = monster.updateHP(amt);
			if (amt < 0)
				appendLog(monster.name() + " lost " + amt + " HP.", DefinitionGlobal.LOG_TYPE_MONSTER_LOSES_HP);

			else
				appendLog(monster.name() + " gained " + actualAmt + " HP.", DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_HP);
		}
		// modify ap % of self
		if (item.modifyAPPercentOfMaxSelf() != 0) {
			int amt = Helper.getPercentFromInt(item.modifyAPPercentOfMaxSelf(), player.maxAP());

			int actualAmt = player.updateAP(amt);

			if (amt < 0)
				appendLog("You lost " + -amt + " AP.", DefinitionGlobal.LOG_TYPE_PLAYER_LOSES_AP);

			else
				appendLog("You gained " + actualAmt + " AP.", DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_AP);
		}
		// modify ap % of monster
		if (item.modifyAPPercentOfMaxMonster() != 0) {
			int amt = Helper.getPercentFromInt(item.modifyAPPercentOfMaxMonster(), monster.maxAP());

			int actualAmt = monster.updateAP(amt);

			if (amt < 0)
				appendLog(monster.name() + " lost " + amt + " AP.", DefinitionGlobal.LOG_TYPE_MONSTER_LOSES_AP);

			else
				appendLog(monster.name() + " gained " + actualAmt + " AP.", DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_AP);
		}

		// skip fight
		if (item.skipsCurrentFightFlag() == 1) {
			showFightEndDialog();
		}

		// modify crit of self for turns
		if (item.modifyCritPercentSelf() > 0) {
			player.itemModifyCrit(item.modifyCritPercentSelf(), turns);

			appendLog("Chance to crit is " + player.critChance() + "%. Bonus will be applied for " + turns + " turns.",
					DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_GOOD);
		}
		// modify crit of monster for turns
		if (item.modifyCritPercentMonster() > 0) {
			// TODO
		}

		// modify damage taken self for turns
		if (item.modifyDamageTakenPercentSelf() != 0) {
			player.itemModifyDamageTaken(item.modifyDamageTakenPercentSelf(), turns);

			appendLog("Damage taken will be decreased by " + item.modifyDamageTakenPercentSelf() + "% for " + turns
					+ " turns.", DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_GOOD);
		}
		// modify damage taken monster for turns
		if (item.modifyDamageTakenPercentMonster() != 0) {
			// TODO
		}

		// modify dodge chance self
		if (item.modifyDodgePercentSelf() != 0) {
			player.itemModifyDodge(item.modifyDodgePercentSelf(), turns);

			appendLog("Dodge chance modified by " + item.modifyDodgePercentSelf() + " for " + turns + " turns.",
					DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_GOOD);
		}

		// modify dodge chance monster
		if (item.modifyDodgePercentMonster() != 0) {
			// TODO
		}

		// apply stun monster
		if (item.stunForTurnsMonster() > 0) {
			monster.addStunCount(item.stunForTurnsMonster());

			appendLog(monster.name() + " is stunned for " + item.stunForTurnsMonster() + " turns.",
					DefinitionGlobal.LOG_TYPE_MONSTER_STUNNED);
		}

		// remove player effects
		if (item.removesPlayerEffectsFlag() == 1) {
			player.removeEffects();
			appendLog(item.name() + " removed your active effects.",
					DefinitionGlobal.LOG_TYPE_SOMETHING_GOOD_FOR_PLAYER);
		}

		// add free abilities
		if (item.abilitiesAreFreeFlag() > 0) {
			for (int a = 0; a < turns; a++)
				player.addFreeAbility();

			if (turns == 1)
				appendLog("One free ability charge added to you.", DefinitionGlobal.LOG_TYPE_SOMETHING_GOOD_FOR_PLAYER);
			else
				appendLog(turns + " free ability charges added to you.",
						DefinitionGlobal.LOG_TYPE_SOMETHING_GOOD_FOR_PLAYER);
		}

		// apply animation to monster
		if (item.applyMonsterAnimationID() >= 0) {
			animateMonster(1, item.applyMonsterAnimationID());
		}

		// change monster image
		if (item.changeMonsterImageFlag() == 1) {
			monster.setTempImage(getResources().getIdentifier(item.changeMonsterImage(), "drawable", getPackageName()),
					turns);

			Animation a = Animator.getAnimation(Animator.FADE_OUT_SHAKING);
			a.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationEnd(Animation arg0) {
					monsterImage.setImageResource(monster.tempImageResource());
					monsterImage.invalidate();
					monsterImage.startAnimation(Animator.getAnimation(Animator.FADE_IN));
					appendLog("The creature was transformed!", DefinitionGlobal.LOG_TYPE_DEFAULT);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationStart(Animation animation) {

				}

			});
			monsterImage.startAnimation(a);

		}

		// modify attack attack power player (DEFUNCT)

		// modify attack power monster
		if (item.modifyAttackPowerPercentMonster() != 0) {
			monster.modifyAttackPowerByPercentTurns(item.modifyAttackPowerPercentMonster(), turns);

			appendLog(monster.name() + "'s attack damage is modified by " + item.modifyAttackPowerPercentMonster()
					+ "% for " + turns + " turns.", DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_EFFECT);
		}

		// kill monster %
		if (item.killMonsterPercentChance() > 0) {
			if (Helper.randomInt(101) < item.killMonsterPercentChance()) {
				appendLog(item.name() + " blew up the " + monster.name() + "!", DefinitionGlobal.LOG_TYPE_DEFAULT);
				killMonster();
			} else {
				appendLog("The " + item.name() + " fizzled.", DefinitionGlobal.LOG_TYPE_ACTION_FAILED);
			}
		}

		// restart fight at full health
		if (item.restartFightFlag() == 1) {
			restartFight();
		}

		// warp to round
		if (item.warpToRound() > 0) {
			player.setCurrentRound(item.warpToRound());
			player.setCurrentFight(1);
			DBHandler.updatePlayer(player);

			warpToRound();
		}

		// apply hp gain after num turns
		if (item.applyHPPercentGainAfterTurnsAmount() > 0) {
			player.itemApplyHPPercentGainAfterTurns(item.applyHPPercentGainAfterTurnsAmount(),
					item.applyHPPercentGainAfterTurnsTurns());

			appendLog(
					"You will gain " + item.applyHPPercentGainAfterTurnsAmount() + "% HP after "
							+ item.applyHPPercentGainAfterTurnsTurns() + " turns.",
					DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_GOOD);
		}

		// apply stun self
		if (item.stunForTurnsSelf() > 0) {
			player.addStunCount(item.stunForTurnsSelf());

			appendLog("You are stunned for " + item.stunForTurnsSelf() + " turns.",
					DefinitionGlobal.LOG_TYPE_PLAYER_STUNNED);

			endPlayerAttack();
		}

		updateViews();
	}

	private void useAbility(int abilityId, int randomnumber) {
		appendLog("Used Ability: " + (String) DefinitionRunes.runeData[abilityId][DefinitionRunes.RUNE_NAMES][0],
				DefinitionGlobal.LOG_TYPE_PLAYER_USE_ABILITY);

		if (inCombat == false)
			return;

		if (player.hasFreeAbility())
			player.useFreeAbility();

		else
			player.updateAP(-(Integer) DefinitionRunes.runeData[abilityId][DefinitionRunes.RUNE_AP_COST][0]);

		playerAPView.setText("AP: " + player.currentAP() + "/" + player.maxAP());

		doPlayerAbility(abilityId);

	}

	private void doMonsterAbility(int abilityId) {
		ItemRune ability = new ItemRune(DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY, abilityId, getApplicationContext());
		animateAbility(ability, 1);
		doGenericAbilityActions(ability, monster, player, 1);
		// track known monster abilities used in this combat
		try {
			if (knownMonsterAbilities == null)
				knownMonsterAbilities = new java.util.HashSet<Integer>();
			knownMonsterAbilities.add(abilityId);
		} catch (Exception ignored) {
		}
	}

	private void doPlayerAbility(int abilityId) {
		ItemRune ability = new ItemRune(DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY, abilityId, getApplicationContext());
		animateAbility(ability, 0);

		doGenericAbilityActions(ability, player, monster, 0);
	}

	private void doGenericAbilityActions(ItemRune ability, Actor source, Actor target, int sourceFlag) {
		boolean doBonus = false;

		if ((Integer) DefinitionRunes.runeData[ability.id()][DefinitionRunes.RUNE_SOUND_CLIP][0] != 0)
			SoundManager.playSound(
					(Integer) DefinitionRunes.runeData[ability.id()][DefinitionRunes.RUNE_SOUND_CLIP][0], false);

		String targetName = target.name();
		String sourceName = source.name();
		String sourceNameAction = source.name() + " is ";
		String targetNameAction = target.name() + " is ";
		String targetNameLower = target.name();
		String sourceNameLower = source.name();
		String sourceNamePossessive = source.name() + "'s";
		String targetNamePossessive = target.name() + "'s";

		if (sourceFlag == 0) {
			source = (Player) source;
			target = (Monster) target;
			sourceName = "You";
			sourceNameAction = "You are";
			sourceNameLower = "you";
			sourceNamePossessive = "Your";

		} else {
			source = (Monster) source;
			target = (Player) target;
			targetName = "You";
			targetNameAction = "You are";
			targetNameLower = "you";
			targetNamePossessive = "your";
		}

		if (ability.comboActiveEffectRequirementID() >= 0) {
			if (ability.comboActiveEffectActor() == 0) {
				if (source.isEffectActive(ability.comboActiveEffectRequirementID()))
					doBonus = true;
			} else {
				if (target.isEffectActive(ability.comboActiveEffectRequirementID()))
					doBonus = true;
			}
		}

		// see if this is a casting ability
		if (ability.castingTurnsMin() > 0) {
			// check for special action ID
			if (ability.castingSpecialID() == 0) {
				// stun target for source(magelore) turns
				target.addStunCount(source.magelore() + 1);
			}

			// check to see if we apply an effect right now
			if (ability.castingApplyEffectOnEndFlag() == 0) {
				if (ability.appliesEffectSource().length > 0) {
					for (int a = 0; a < ability.appliesEffectSource().length; a++) {
						if (source.immuneToBadEffects()
								&& DefinitionEffects.EFFECT_IS_GOOD_FLAG_FOR_PLAYER[ability
										.appliesEffectSource()[a]] != 1) {
							appendLog(source.name() + " is too Focused to become "
									+ DefinitionEffects.EFFECT_NAMES[ability.appliesEffectSource()[a]] + "!",
									DefinitionGlobal.LOG_TYPE_DEFAULT);
						} else {
							source.addActiveEffect(ability.appliesEffectSource()[a]);
							if (source == player) {
								if (DefinitionEffects.EFFECT_IS_GOOD_FLAG_FOR_PLAYER[ability
										.appliesEffectSource()[a]] == 1)
									appendLog(
											sourceNameAction + " "
													+ DefinitionEffects.EFFECT_NAMES[ability.appliesEffectSource()[a]]
													+ ".",
											DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_GOOD);

								else
									appendLog(
											sourceNameAction + " "
													+ DefinitionEffects.EFFECT_NAMES[ability.appliesEffectSource()[a]]
													+ ".",
											DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_BAD);
							} else {
								appendLog(
										sourceNameAction + " "
												+ DefinitionEffects.EFFECT_NAMES[ability.appliesEffectSource()[a]]
												+ ".",
										DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_EFFECT);
							}
						}
					}

				}
				if (ability.appliesEffectTarget().length > 0) {
					for (int a = 0; a < ability.appliesEffectTarget().length; a++) {
						if (target.immuneToBadEffects()
								&& DefinitionEffects.EFFECT_IS_GOOD_FLAG_FOR_PLAYER[ability
										.appliesEffectTarget()[a]] != 1) {
							appendLog(target.name() + " is too Focused to become "
									+ DefinitionEffects.EFFECT_NAMES[ability.appliesEffectTarget()[a]] + "!",
									DefinitionGlobal.LOG_TYPE_DEFAULT);
						} else {
							target.addActiveEffect(ability.appliesEffectTarget()[a]);

							if (target == player) {
								if (DefinitionEffects.EFFECT_IS_GOOD_FLAG_FOR_PLAYER[ability
										.appliesEffectTarget()[a]] == 1)
									appendLog(
											targetNameAction + " "
													+ DefinitionEffects.EFFECT_NAMES[ability.appliesEffectTarget()[a]]
													+ ".",
											DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_GOOD);
								else
									appendLog(
											targetNameAction + " "
													+ DefinitionEffects.EFFECT_NAMES[ability.appliesEffectTarget()[a]]
													+ ".",
											DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_BAD);
							} else {
								appendLog(
										targetNameAction + " "
												+ DefinitionEffects.EFFECT_NAMES[ability.appliesEffectTarget()[a]]
												+ ".",
										DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_EFFECT);
							}
						}

					}
				}
			} else {
				// these effects are applied to target only (opposite of source)

				if (sourceFlag == 0)
					castingWaitToApplyEffectMonster = true;

				else
					castingWaitToApplyEffectPlayer = true;
			}

			// check to see if this deals damage on end
			if (ability.castingDamageOnEndFlag() == 1) {
				if (sourceFlag == 0)
					castingWaitToApplyDamageMonster = true;
				else
					castingWaitToApplyDamagePlayer = true;
			}

			source.startCasting(ability, source);

		}

		// deal direct damage:
		if (ability.dealWeaponDamageMin() >= 0) {
			if (sourceFlag == 0 && castingWaitToApplyDamageMonster
					|| sourceFlag == 1 && castingWaitToApplyDamagePlayer) {
				// handle this in doCastingTurn()
			} else {
				int dmg = 0;
				if (doBonus) {
					if (sourceFlag == 0)
						dmg = ((Player) source).getDamage()
								* (Helper.getRandomIntFromRange(1 + ability.dealWeaponDamageMaxBonus(),
										ability.dealWeaponDamageMinBonus()));

					else
						dmg = ((Monster) source).getDamage()
								* (Helper.getRandomIntFromRange(1 + ability.dealWeaponDamageMaxBonus(),
										ability.dealWeaponDamageMinBonus()));

					if (target == player) {
						appendLog(ability.name() + " dealt " + dmg + " bonus damage to " + targetNameLower + "!",
								DefinitionGlobal.LOG_TYPE_PLAYER_TAKE_ABILITY_DMG);
					} else {
						appendLog(ability.name() + " dealt " + dmg + " bonus damage to " + targetNameLower + "!",
								DefinitionGlobal.LOG_TYPE_MONSTER_TAKE_ABILITY_DMG);
					}

					dealAbilityDamage(ability.name(), source, target, dmg, sourceFlag);
				} else if (ability.dealWeaponDamageMin() > 0) {
					if (sourceFlag == 0)
						dmg = ((Player) source).getDamage()
								* (Helper.getRandomIntFromRange(1 + ability.dealWeaponDamageMax(),
										ability.dealWeaponDamageMin()));

					else
						dmg = ((Monster) source).getDamage()
								* (Helper.getRandomIntFromRange(1 + ability.dealWeaponDamageMax(),
										ability.dealWeaponDamageMin()));

					int dltAmt = dealAbilityDamage(ability.name(), source, target, dmg, sourceFlag);

					if (target == player) {
						appendLog(ability.name() + " dealt " + -dltAmt + " damage to " + targetNameLower + ".",
								DefinitionGlobal.LOG_TYPE_PLAYER_TAKE_ABILITY_DMG);
					} else {
						appendLog(ability.name() + " dealt " + -dltAmt + " damage to " + targetNameLower + ".",
								DefinitionGlobal.LOG_TYPE_MONSTER_TAKE_ABILITY_DMG);
					}

				}

			}

		} // END deal damage

		// stun:
		if (ability.stunTurns() > 0) {
			boolean reqMet = true;
			// check source effect active requirement
			if (ability.stunOnlyIfSourceEffectActive() >= 0) {
				reqMet = source.isEffectActive(ability.stunOnlyIfSourceEffectActive());
			} else if (ability.stunOnlyIfTargetEffectActive() >= 0) {
				reqMet = target.isEffectActive(ability.stunOnlyIfTargetEffectActive());
			}

			if (reqMet) {
				if (ability.stunActor() == 0) {
					source.addStunCount(ability.stunTurns());
					if (source == player)
						appendLog(ability.name() + " stunned " + sourceNameLower + " for " + ability.stunTurns()
								+ " turns.", DefinitionGlobal.LOG_TYPE_PLAYER_STUNNED);
					else
						appendLog(ability.name() + " stunned " + sourceNameLower + " for " + ability.stunTurns()
								+ " turns.", DefinitionGlobal.LOG_TYPE_MONSTER_STUNNED);
				} else {
					target.addStunCount(ability.stunTurns());
					if (target == player)
						appendLog(ability.name() + " stunned " + targetNameLower + " for " + ability.stunTurns()
								+ " turns.", DefinitionGlobal.LOG_TYPE_PLAYER_STUNNED);
					else
						appendLog(ability.name() + " stunned " + targetNameLower + " for " + ability.stunTurns()
								+ " turns.", DefinitionGlobal.LOG_TYPE_MONSTER_STUNNED);
				}
			}
		} // END Stun

		// deal multiple stat based damage
		if (ability.dealMultipleStatBasedDamageMult() > 0) {
			if (sourceFlag == 0 && castingWaitToApplyDamageMonster
					|| sourceFlag == 1 && castingWaitToApplyDamagePlayer) {
				// handle this in doCastingTurn()
			} else {

				int dmg = Helper.getMultipleStatDamage(source, ability);

				int dltAmt = dealAbilityDamage(ability.name(), source, target, dmg, sourceFlag);

				if (target == player)
					appendLog(ability.name() + " dealt " + -dltAmt + " damage to " + targetNameLower + ".",
							DefinitionGlobal.LOG_TYPE_PLAYER_TAKE_ABILITY_DMG);
				else
					appendLog(ability.name() + " dealt " + -dltAmt + " damage to " + targetNameLower + ".",
							DefinitionGlobal.LOG_TYPE_MONSTER_TAKE_ABILITY_DMG);

			}
		}

		// deal stat is hp% dmg
		if (ability.dealStatIsHpPercentDamageSourceOrTargetFlag() >= 0) {
			int statVal = 0;
			if (ability.dealStatIsHpPercentDamageSourceOrTargetFlag() == 0) {
				if (ability.dealStatIsHpPercentDamageStatId() == 0)
					statVal = source.exec();

				if (ability.dealStatIsHpPercentDamageStatId() == 1)
					statVal = source.reaction();

				if (ability.dealStatIsHpPercentDamageStatId() == 2)
					statVal = source.knowledge();

				if (ability.dealStatIsHpPercentDamageStatId() == 3)
					statVal = source.magelore();

				if (ability.dealStatIsHpPercentDamageStatId() == 4)
					statVal = source.luck();

			} else {
				if (ability.dealStatIsHpPercentDamageStatId() == 0)
					statVal = target.exec();

				if (ability.dealStatIsHpPercentDamageStatId() == 1)
					statVal = target.reaction();

				if (ability.dealStatIsHpPercentDamageStatId() == 2)
					statVal = target.knowledge();

				if (ability.dealStatIsHpPercentDamageStatId() == 3)
					statVal = target.magelore();

				if (ability.dealStatIsHpPercentDamageStatId() == 4)
					statVal = target.luck();
			}

			int dmg = Helper.getPercentFromInt(statVal, target.maxHP());

			int dltAmt = dealAbilityDamage(ability.name(), source, target, dmg, sourceFlag);

			if (target == player)
				appendLog(ability.name() + " dealt " + -dltAmt + " damage to " + targetNameLower + ".",
						DefinitionGlobal.LOG_TYPE_PLAYER_TAKE_ABILITY_DMG);
			else
				appendLog(ability.name() + " dealt " + -dltAmt + " damage to " + targetNameLower + ".",
						DefinitionGlobal.LOG_TYPE_MONSTER_TAKE_ABILITY_DMG);

		} // END deal stat based damage

		// apply effect
		if (!castingWaitToApplyEffectMonster && sourceFlag == 0 || !castingWaitToApplyEffectPlayer && sourceFlag == 1) {
			if (ability.appliesEffectSource().length > 0) {
				for (int a = 0; a < ability.appliesEffectSource().length; a++) {
					if (source.immuneToBadEffects()
							&& DefinitionEffects.EFFECT_IS_GOOD_FLAG_FOR_PLAYER[ability
									.appliesEffectSource()[a]] != 1) {
						appendLog(
								source.name() + " is too Focused to become "
										+ DefinitionEffects.EFFECT_NAMES[ability.appliesEffectSource()[a]] + "!",
								DefinitionGlobal.LOG_TYPE_DEFAULT);
					} else {
						source.addActiveEffect(ability.appliesEffectSource()[a]);

						if (source == player) {
							if (DefinitionEffects.EFFECT_IS_GOOD_FLAG_FOR_PLAYER[ability.appliesEffectSource()[a]] == 1)
								appendLog(
										sourceNameAction + " "
												+ DefinitionEffects.EFFECT_NAMES[ability.appliesEffectSource()[a]]
												+ ".",
										DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_GOOD);
							else
								appendLog(
										sourceNameAction + " "
												+ DefinitionEffects.EFFECT_NAMES[ability.appliesEffectSource()[a]]
												+ ".",
										DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_BAD);
						} else {
							appendLog(
									sourceNameAction + " "
											+ DefinitionEffects.EFFECT_NAMES[ability.appliesEffectSource()[a]] + ".",
									DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_EFFECT);
						}
					}
				}

			}
			if (ability.appliesEffectTarget().length > 0) {
				for (int a = 0; a < ability.appliesEffectTarget().length; a++) {
					if (target.immuneToBadEffects()
							&& DefinitionEffects.EFFECT_IS_GOOD_FLAG_FOR_PLAYER[ability
									.appliesEffectTarget()[a]] != 1) {
						appendLog(
								target.name() + " is too Focused to become "
										+ DefinitionEffects.EFFECT_NAMES[ability.appliesEffectTarget()[a]] + "!",
								DefinitionGlobal.LOG_TYPE_DEFAULT);
					} else {
						target.addActiveEffect(ability.appliesEffectTarget()[a]);
						if (target == player) {
							if (DefinitionEffects.EFFECT_IS_GOOD_FLAG_FOR_PLAYER[ability.appliesEffectTarget()[a]] == 1)
								appendLog(
										targetNameAction + " "
												+ DefinitionEffects.EFFECT_NAMES[ability.appliesEffectTarget()[a]]
												+ ".",
										DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_GOOD);
							else
								appendLog(
										targetNameAction + " "
												+ DefinitionEffects.EFFECT_NAMES[ability.appliesEffectTarget()[a]]
												+ ".",
										DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_BAD);
						} else {
							appendLog(
									targetNameAction + " "
											+ DefinitionEffects.EFFECT_NAMES[ability.appliesEffectTarget()[a]] + ".",
									DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_EFFECT);
						}
					}

				}
			}
		}

		// sap AP based on stat
		if (ability.sapAPBasedOnStatID() >= 0) {
			int statVal = 0;
			if (ability.sapAPActorStatUsedFlag() == 0) {
				if (ability.sapAPBasedOnStatID() == 0)
					statVal = source.exec();

				if (ability.sapAPBasedOnStatID() == 1)
					statVal = source.reaction();

				if (ability.sapAPBasedOnStatID() == 2)
					statVal = source.knowledge();

				if (ability.sapAPBasedOnStatID() == 3)
					statVal = source.magelore();

				if (ability.sapAPBasedOnStatID() == 4)
					statVal = source.luck();

				if (ability.sapAPBasedOnStatID() == 5)
					statVal = source.currentHP();

				if (ability.sapAPBasedOnStatID() == 6)
					statVal = source.currentAP();
			} else {
				if (ability.sapAPBasedOnStatID() == 0)
					statVal = target.exec();

				if (ability.sapAPBasedOnStatID() == 1)
					statVal = target.reaction();

				if (ability.sapAPBasedOnStatID() == 2)
					statVal = target.knowledge();

				if (ability.sapAPBasedOnStatID() == 3)
					statVal = target.magelore();

				if (ability.sapAPBasedOnStatID() == 4)
					statVal = target.luck();

				if (ability.sapAPBasedOnStatID() == 5)
					statVal = target.currentHP();

				if (ability.sapAPBasedOnStatID() == 6)
					statVal = target.currentAP();
			}

			if (statVal > target.currentAP()) {
				statVal = target.currentAP();
				if (target == player)
					appendLog(ability.name() + " sapped all remaining AP from " + targetNameLower + ".",
							DefinitionGlobal.LOG_TYPE_PLAYER_LOSES_AP);
				else
					appendLog(ability.name() + " sapped all remaining AP from " + targetNameLower + ".",
							DefinitionGlobal.LOG_TYPE_MONSTER_LOSES_AP);
			} else {
				if (target == player)
					appendLog(ability.name() + " sapped " + statVal + " AP from " + targetNameLower + ".",
							DefinitionGlobal.LOG_TYPE_PLAYER_LOSES_AP);
				else
					appendLog(ability.name() + " sapped " + statVal + " AP from " + targetNameLower + ".",
							DefinitionGlobal.LOG_TYPE_MONSTER_LOSES_AP);
			}

			target.updateAP(-statVal);

			if (ability.sapAPAndTransferToSourceFlag() == 1) {
				if (source == player)
					appendLog(sourceName + " gained " + statVal + " AP from " + targetNameLower + ".",
							DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_AP);
				else
					appendLog(sourceName + " gained " + statVal + " AP from " + targetNameLower + ".",
							DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_AP);

				source.updateAP(statVal);
			}
		} // END sap AP based on stat

		// modify AP
		if (ability.modifyAPActor() >= 0) {
			if (ability.modifyAPActor() == 0) {
				int amt = Helper.getPercentFromInt(ability.modifyAPPercentAmount(), source.maxAP());
				if (doBonus) {
					amt = Helper.getPercentFromInt(ability.modifyAPPercentAmountBonus(), source.maxAP());
				}
				source.updateAP(amt);

				if (amt > 0) {
					if (source == player)
						appendLog(sourceName + " gained " + amt + " AP from " + ability.name() + ".",
								DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_AP);
					else
						appendLog(sourceName + " gained " + amt + " AP from " + ability.name() + ".",
								DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_AP);
				} else {
					if (source == player)
						appendLog(sourceName + " lost " + amt + " AP from " + ability.name() + ".",
								DefinitionGlobal.LOG_TYPE_PLAYER_LOSES_AP);
					else
						appendLog(sourceName + " lost " + amt + " AP from " + ability.name() + ".",
								DefinitionGlobal.LOG_TYPE_MONSTER_LOSES_AP);
				}
			} else {
				int amt = Helper.getPercentFromInt(ability.modifyAPPercentAmount(), target.maxAP());
				if (doBonus) {
					amt = Helper.getPercentFromInt(ability.modifyAPPercentAmountBonus(), target.maxAP());
				}
				target.updateAP(amt);
				if (amt > 0) {
					if (target == player)
						appendLog(targetName + " gained " + amt + " AP from " + ability.name() + ".",
								DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_AP);
					else
						appendLog(targetName + " gained " + amt + " AP from " + ability.name() + ".",
								DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_AP);
				} else {
					if (target == player)
						appendLog(targetName + " lost " + amt + " AP from " + ability.name() + ".",
								DefinitionGlobal.LOG_TYPE_PLAYER_LOSES_AP);
					else
						appendLog(targetName + " lost " + amt + " AP from " + ability.name() + ".",
								DefinitionGlobal.LOG_TYPE_MONSTER_LOSES_AP);
				}

			}
		} // END modify AP

		// modify HP
		if (ability.modifyHPActor() >= 0) {
			// source
			if (ability.modifyHPActor() == 0) {
				int amt = Helper.getPercentFromInt(ability.modifyHPPercentAmount(), source.maxHP());
				if (doBonus) {
					amt = Helper.getPercentFromInt(ability.modifyHPPercentAmountBonus(), source.maxHP());
				}

				if (amt < 0) {
					if (source == player) {
						appendLog(sourceName + " lost " + amt + " HP from " + ability.name() + ".",
								DefinitionGlobal.LOG_TYPE_PLAYER_LOSES_HP);
					} else
						appendLog(sourceName + " lost " + amt + " HP from " + ability.name() + ".",
								DefinitionGlobal.LOG_TYPE_MONSTER_LOSES_HP);

				} else if (amt > 0) {
					if (source == player) {
						appendLog(sourceName + " gained " + amt + " HP from " + ability.name() + ".",
								DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_HP);
					} else
						appendLog(sourceName + " gained " + amt + " HP from " + ability.name() + ".",
								DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_HP);
				}

				source.updateHP(amt);
			}
			// target
			else if (ability.modifyHPActor() == 1) {
				int amt = Helper.getPercentFromInt(ability.modifyHPPercentAmount(), target.maxHP());
				if (doBonus) {
					amt = Helper.getPercentFromInt(ability.modifyHPPercentAmountBonus(), target.maxHP());
				}

				if (amt < 0) {
					if (target == player)
						appendLog(targetName + " lost " + amt + " HP from " + ability.name() + ".",
								DefinitionGlobal.LOG_TYPE_PLAYER_LOSES_HP);
					else
						appendLog(targetName + " lost " + amt + " HP from " + ability.name() + ".",
								DefinitionGlobal.LOG_TYPE_MONSTER_LOSES_HP);
				} else if (amt > 0) {
					if (target == player)
						appendLog(targetName + " gained " + amt + " HP from " + ability.name() + ".",
								DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_HP);
					else
						appendLog(targetName + " gained " + amt + " HP from " + ability.name() + ".",
								DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_HP);
				}

				target.updateHP(amt);
			} else if (ability.modifyHPActor() == 2) {
				int amt1 = Helper.getPercentFromInt(ability.modifyHPPercentAmount(), source.maxHP());
				int amt2 = Helper.getPercentFromInt(ability.modifyHPPercentAmount(), target.maxHP());

				if (amt1 < 0) {
					if (source == player)
						appendLog(sourceName + " lost " + amt1 + " HP from " + ability.name() + ".",
								DefinitionGlobal.LOG_TYPE_PLAYER_LOSES_HP);
					else
						appendLog(sourceName + " lost " + amt1 + " HP from " + ability.name() + ".",
								DefinitionGlobal.LOG_TYPE_MONSTER_LOSES_HP);
				} else if (amt1 > 0) {
					if (source == player)
						appendLog(sourceName + " gained " + amt1 + " HP from " + ability.name() + ".",
								DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_HP);
					else
						appendLog(sourceName + " gained " + amt1 + " HP from " + ability.name() + ".",
								DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_HP);
				}

				if (amt2 < 0) {
					if (target == player)
						appendLog(targetName + " lost " + amt2 + " HP from " + ability.name() + ".",
								DefinitionGlobal.LOG_TYPE_PLAYER_LOSES_HP);
					else
						appendLog(targetName + " lost " + amt2 + " HP from " + ability.name() + ".",
								DefinitionGlobal.LOG_TYPE_MONSTER_LOSES_HP);
				} else if (amt2 > 0) {
					if (target == player)
						if (source == player)
							appendLog(targetName + " gained " + amt2 + " HP from " + ability.name() + ".",
									DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_HP);
						else
							appendLog(targetName + " gained " + amt2 + " HP from " + ability.name() + ".",
									DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_HP);
				}
			}
		} // END modify HP

		// absorb damage for some future turns
		if (ability.counterAbsorbDamageTurns() > 0) {
			int absDmgMinPercent = ability.counterAbsorbDamageMin();
			if (doBonus)
				absDmgMinPercent = ability.counterAbsorbDamageMinBonus();

			int absDmgMaxPercent = ability.counterAbsorbDamageMax();
			if (doBonus)
				absDmgMaxPercent = ability.counterAbsorbDamageMaxBonus();

			if (ability.counterAbsorbDamageStatIsPercentStatId() >= 0) {
				// absorb damage based on a stat
				int statId = ability.counterAbsorbDamageStatIsPercentStatId();
				int statMult = ability.counterAbsorbDamageStatIsPercentStatMult();
				int statAmt = 0;
				if (statId == 0) {
					statAmt = source.exec();
				} else if (statId == 1) {
					statAmt = source.reaction();
				} else if (statId == 2) {
					statAmt = source.knowledge();
				} else if (statId == 3) {
					statAmt = source.magelore();
				} else if (statId == 4) {
					statAmt = source.luck();
				}
				absDmgMinPercent = statAmt * statMult;
				absDmgMaxPercent = statAmt * statMult;
			}

			source.setCounterAbsorbDamage(ability.counterAbsorbDamageTurns(), absDmgMinPercent, absDmgMaxPercent);

			String absRangeText = "" + absDmgMinPercent;
			if (absDmgMinPercent != absDmgMaxPercent)
				absRangeText = absDmgMinPercent + "-" + absDmgMaxPercent;

			if (source == player)
				appendLog(
						sourceName + " will absorb " + absRangeText + "% of dmg " + ability.counterAbsorbDamageTurns()
								+ " times.",
						DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_GOOD);
			else
				appendLog(
						sourceName + " will absorb " + absRangeText + "% of dmg " + ability.counterAbsorbDamageTurns()
								+ " times.",
						DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_EFFECT);
		}

		// modify stat
		if (ability.counterModifyStatTurns() > 0) {
			if (ability.counterModifyStatSourceOrTargetFlag() == 0) {
				source.setCounterModifyStat(ability.counterModifyStatTurns(), ability.counterModifyStatId(),
						ability.counterModifyStatMult());

				appendLog(sourceNamePossessive + " stats have been modified.", DefinitionGlobal.LOG_TYPE_DEFAULT);
			} else {
				target.setCounterModifyStat(ability.counterModifyStatTurns(), ability.counterModifyStatId(),
						ability.counterModifyStatMult());
				appendLog(targetNamePossessive + " stats have been modified.", DefinitionGlobal.LOG_TYPE_DEFAULT);
			}
		}

		// dot modify hp max hp based
		if (ability.counterModifyHPMaxHPBasedTurns() > 0) {
			if (ability.counterModifyHPMaxHPBasedSourceOrTargetFlag() == 0) {
				int amt = Helper.getPercentFromInt(ability.counterModifyHPMaxHPBasedAmount(), source.maxHP());

				if (amt == 0) {
					if (ability.counterModifyHPMaxHPBasedAmount() < 0)
						amt = -1;
					else
						amt = 1;
				}

				source.setCounterDotModifyHPMaxHPBasedHP(ability.counterModifyHPMaxHPBasedTurns(), amt, ability.name());

				if (amt < 0) {
					amt = -amt;
					if (source == player)
						appendLog(
								sourceName + " will take " + amt + " dmg for "
										+ ability.counterModifyHPMaxHPBasedTurns()
										+ " turns.",
								DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_BAD);
					else
						appendLog(
								sourceName + " will take " + amt + " dmg for "
										+ ability.counterModifyHPMaxHPBasedTurns()
										+ " turns.",
								DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_EFFECT);
				} else {
					if (source == player)
						appendLog(
								sourceName + " will gain " + amt + " HP for " + ability.counterModifyHPMaxHPBasedTurns()
										+ " turns.",
								DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_GOOD);
					else
						appendLog(
								sourceName + " will gain " + amt + " HP for " + ability.counterModifyHPMaxHPBasedTurns()
										+ " turns.",
								DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_EFFECT);

				}
			} else {
				int amt = Helper.getPercentFromInt(ability.counterModifyHPMaxHPBasedAmount(), target.maxHP());

				if (amt == 0) {
					if (ability.counterModifyHPMaxHPBasedAmount() < 0)
						amt = -1;
					else
						amt = 1;
				}

				target.setCounterDotModifyHPMaxHPBasedHP(ability.counterModifyHPMaxHPBasedTurns(), amt, ability.name());

				if (amt < 0) {
					amt = -amt;

					if (target == player)
						appendLog(
								targetName + " will take " + amt + " dmg for "
										+ ability.counterModifyHPMaxHPBasedTurns()
										+ " turns.",
								DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_BAD);
					else
						appendLog(
								targetName + " will take " + amt + " dmg for "
										+ ability.counterModifyHPMaxHPBasedTurns()
										+ " turns.",
								DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_EFFECT);

				} else {
					if (target == player)
						appendLog(
								targetName + " will gain " + amt + " HP for " + ability.counterModifyHPMaxHPBasedTurns()
										+ " turns.",
								DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_GOOD);
					else
						appendLog(
								targetName + " will gain " + amt + " HP for " + ability.counterModifyHPMaxHPBasedTurns()
										+ " turns.",
								DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_EFFECT);
				}
			}
		}

		// dot modify hp stat based times weapon dmg
		if (ability.counterModifyHPStatBasedTimesWpnDmgTurns() > 0) {
			// stats used in this ability are always based off source, and dmg
			// dealt to target
			int turns = ability.counterModifyHPStatBasedTimesWpnDmgTurns();
			int statId = ability.counterModifyHPStatBasedTimesWpnDmgStatId();
			int hitDmg = source.getMaxDamage();
			double mult = ability.counterModifyHPStatBasedTimesWpnDmgMult();
			int statAmt = 0;
			int changeAmt = 0;

			if (statId == 0) {
				statAmt = source.exec();
			} else if (statId == 1) {
				statAmt = source.reaction();
			} else if (statId == 2) {
				statAmt = source.knowledge();
			} else if (statId == 3) {
				statAmt = source.magelore();
			} else if (statId == 4) {
				statAmt = source.luck();
			}
			changeAmt = (int) Math.round((double) statAmt * (double) hitDmg * mult);

			target.setCounterModifyHPStatTimesWpnDmgBased(turns, changeAmt, ability.name());

			if (changeAmt < 0) {
				if (target == player)
					appendLog(targetName + " will take " + -changeAmt + " dmg for " + turns + " turns.",
							DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_BAD);
				else
					appendLog(targetName + " will take " + -changeAmt + " dmg for " + turns + " turns.",
							DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_EFFECT);
			} else {
				if (target == player)
					appendLog(targetName + " will gain " + -changeAmt + " HP for " + turns + " turns.",
							DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_GOOD);
				else
					appendLog(targetName + " will gain " + -changeAmt + " HP for " + turns + " turns.",
							DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_EFFECT);
			}

		}

		// counter modify weapon damage for turns
		if (ability.counterModifyWeaponDamageTurns() > 0) {
			int turns = ability.counterModifyWeaponDamageTurns();
			int amt = ability.counterModifyWeaponDamageAmount();
			int amtBonus = ability.counterModifyWeapondamageAmountBonus();

			if (ability.counterModifyWeaponDamageSourceOrTargetFlag() == 0)
				source.setCounterModifyWeaponDamage(turns, amt, amtBonus, ability.comboActiveEffectRequirementID());

			else
				target.setCounterModifyWeaponDamage(turns, amt, amtBonus, ability.comboActiveEffectRequirementID());
		}

		// dot modify hp based on wpn dmg
		if (ability.counterDotModifyHPWeaponDamageBasedTurns() > 0) {
			int dmg = player.getMaxDamage();
			if (sourceFlag == 1)
				dmg = monster.getMaxDamage();

			if (dmg < 1)
				dmg = 1;

			target.setCounterDotModifyHPWeaponDamageBased(ability.counterDotModifyHPWeaponDamageBasedTurns(),
					ability.counterDotModifyHPWeaponDamageBasedMinAmount(),
					ability.counterDotModifyHPWeaponDamageBasedMaxAmount(),
					ability.counterDotModifyHPWeaponDamageBasedMinAmountBonus(),
					ability.counterDotModifyHPWeaponDamageBasedMaxAmount(), ability.comboActiveEffectRequirementID(),
					ability.name(), dmg);
		}

		// reveal target info
		if (ability.revealsTargetInfo() == 1) {
			monsterBar.setVisibility(View.INVISIBLE);
			expandedMonsterBar.setVisibility(View.VISIBLE);
		}

		// reflect damage
		if (ability.counterReflectDamageTurns() > 0) {
			int turns = ability.counterReflectDamageTurns();
			int percentAmount = ability.counterReflectDamagePercentAmt();
			if (doBonus) {
				percentAmount = ability.counterReflectDamagePercentAmtBonus();
			}

			source.setCounterReflectDamage(turns, percentAmount);

			if (source == player)
				appendLog(sourceName + " will reflect " + percentAmount + "% of directed damage " + turns + " times.",
						DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_GOOD);
			else
				appendLog(sourceName + " will reflect " + percentAmount + "% of directed damage " + turns + " times.",
						DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_EFFECT);
		}

		// counter modify stat per turn based off (stat) turns
		if (ability.counterDotModifyStatPerTurnSourceOrTargetStatForTurns() >= 0) {
			int turns = 0;
			int statIdToMod = ability.counterDotModifyStatPerTurnStatId();
			int statIdForTurns = ability.counterDotModifyStatPerTurnStatIdForTurns();
			int amt = ability.counterDotModifyStatPerTurnAmount();
			if (ability.counterDotModifyStatPerTurnSourceOrTargetStatForTurns() == 0) {
				if (statIdForTurns == 0)
					turns = source.exec();
				if (statIdForTurns == 1)
					turns = source.reaction();
				if (statIdForTurns == 2)
					turns = source.knowledge();
				if (statIdForTurns == 3)
					turns = source.magelore();
				if (statIdForTurns == 4)
					turns = source.luck();
			} else {
				if (statIdForTurns == 0)
					turns = target.exec();
				if (statIdForTurns == 1)
					turns = target.reaction();
				if (statIdForTurns == 2)
					turns = target.knowledge();
				if (statIdForTurns == 3)
					turns = target.magelore();
				if (statIdForTurns == 4)
					turns = target.luck();
			}

			if (ability.counterDotModifyStatPerTurnSourceOrTargetStat() == 0) {
				source.setCounterModifyStatForStatTurns(turns, statIdToMod, amt);
			} else {
				target.setCounterModifyStatForStatTurns(turns, statIdToMod, amt);
			}
		}

		// counter dot modify hp, stat is % with mult
		if (ability.counterDotModifyHPStatIsPercentWithMultSourceOrTargetFlag() >= 0) {
			int turns = ability.counterDotModifyHPStatIsPercentWithMultTurns();
			int statId = ability.counterDotModifyHPStatIsPercentWithMultStatId();
			int mult = ability.counterDotModifyHPStatIsPercentWithMultMult();
			int statAmt = 0;
			int amt = 0;

			// amts based on source stats for this ability
			if (statId == 0)
				statAmt = source.exec();

			if (statId == 1)
				statAmt = source.reaction();

			if (statId == 2)
				statAmt = source.knowledge();

			if (statId == 3)
				statAmt = source.magelore();

			if (statId == 4)
				statAmt = source.luck();

			amt = Helper.getPercentFromInt((statAmt * mult), target.maxHP());

			if (ability.counterDotModifyHPStatIsPercentWithMultSourceOrTargetFlag() == 0) {
				source.setCounterModifyHPStatIsPercentWithMult(turns, amt, ability.name());

				if (source == player) {
					if (amt > 0)
						appendLog(sourceNamePossessive + " HP will be modified by " + amt + " per turn for " + turns
								+ " turns.", DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_GOOD);
					else
						appendLog(sourceNamePossessive + " HP will be modified by " + amt + " per turn for " + turns
								+ " turns.", DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_BAD);
				} else
					appendLog(sourceNamePossessive + " HP will be modified by " + amt + " per turn for " + turns
							+ " turns.", DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_EFFECT);

			} else {
				target.setCounterModifyHPStatIsPercentWithMult(turns, amt, ability.name());

				if (target == player) {
					if (amt > 0)
						appendLog(targetNamePossessive + " HP will be modified by " + amt + " per turn for " + turns
								+ " turns.", DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_GOOD);
					else
						appendLog(targetNamePossessive + " HP will be modified by " + amt + " per turn for " + turns
								+ " turns.", DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_BAD);
				} else
					appendLog(targetNamePossessive + " HP will be modified by " + amt + " per turn for " + turns
							+ " turns.", DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_EFFECT);

			}
		}
	}

	private void endPlayerAttack() {
		turnCleanup();

		if (inCombat == false) {
			updateViews();
			return;
		} else {
			turnFlag = 1;
			Log.d("advanceturn", "endPlayerAttack...");
			advanceTurn();
		}
	}

	private void clearPlayerAttackTextDelay(double d) {
		try {
			clearPlayerAttackTextHandler.removeCallbacks(clearPlayerAttackTextPost);
			clearPlayerAttackTextHandler.postDelayed(clearPlayerAttackTextPost, (long) (d * 1000));
		} catch (Exception e) {
			clearPlayerAttackTextPost.run();
		}
	}

	private Runnable clearPlayerAttackTextPost = new Runnable() {
		public void run() {
			clearPlayerAttackText();
		}
	};

	private void clearPlayerAttackText() {
		playerAttackText.setText("");
	}

	private void doCastingTurn(int turnFlag) {
		// player's casting
		if (turnFlag == 0) {
			boolean isBonus = Helper.checkForBonus(player.currentCastingAbility(), player, monster);

			// casting is over, do anything at end?
			if (player.castingTurnsLeft() <= 0) {
				// deal damage on end

				if (player.currentCastingAbility().castingDamageOnEndFlag() == 1) {
					castingWaitToApplyDamageMonster = false;

					int dmg = 0;

					// wpn dmg
					if (player.currentCastingAbility().dealWeaponDamageMax() > 0) {
						dmg = Helper.getRandomIntFromRange(player.currentCastingAbility().dealWeaponDamageMax(), player
								.currentCastingAbility().dealWeaponDamageMin());
						if (isBonus)
							dmg = Helper.getRandomIntFromRange(
									player.currentCastingAbility().dealWeaponDamageMaxBonus(),
									player.currentCastingAbility().dealWeaponDamageMinBonus());
					}
					// multiple stat dmg
					if (player.currentCastingAbility().dealMultipleStatBasedDamageStat1() >= 0) {
						dmg = Helper.getMultipleStatDamage(player, player.currentCastingAbility());
					}

					int dltAmt = dealAbilityDamage(player.currentCastingAbility().name(), player, monster, dmg, 0);

					appendLog(player.currentCastingAbility().name() + " dealt " + monster.name() + " " + -dltAmt
							+ " damage.", DefinitionGlobal.LOG_TYPE_MONSTER_TAKE_ABILITY_DMG);

					animateImpact();

					int refAmt = monster.getReflectedDamage(-dltAmt);
					if (refAmt > 0) {
						dealReflectedDamage(monster, player, refAmt);
						/*
						 * appendLog("It reflected " + monster.getReflectedDamage(dmg) +
						 * " damage back!",
						 * DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_BAD);
						 * player.updateHP(-monster.getReflectedDamage(dmg));
						 */
					}

				}

				// apply effect on end (only applies to target)
				if (player.currentCastingAbility().castingApplyEffectOnEndFlag() == 1) {
					castingWaitToApplyEffectMonster = false;

					if (player.currentCastingAbility().appliesEffectTarget().length > 0) {
						for (int a = 0; a < player.currentCastingAbility().appliesEffectTarget().length; a++) {
							if (monster.immuneToBadEffects()
									&& DefinitionEffects.EFFECT_IS_GOOD_FLAG_FOR_PLAYER[player.currentCastingAbility()
											.appliesEffectSource()[a]] != 1) {
								appendLog(monster.name()
										+ " is too Focused to become "
										+ DefinitionEffects.EFFECT_NAMES[player.currentCastingAbility()
												.appliesEffectSource()[a]]
										+ "!", DefinitionGlobal.LOG_TYPE_DEFAULT);
							} else {
								monster.addActiveEffect(player.currentCastingAbility().appliesEffectTarget()[a]);
								appendLog(monster.name()
										+ " is "
										+ DefinitionEffects.EFFECT_NAMES[player.currentCastingAbility()
												.appliesEffectTarget()[a]]
										+ ".",
										DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_EFFECT);
							}
						}
					}
				}
			}
			// currently casting, do anything on each turn?
			if (player.castingTurnsLeft() >= 0) {
				// deal damage on turn
				if (player.currentCastingAbility().castingDamageOnTurnFlag() == 1) {
					int dmg = 0;

					// deal basic weapon damage
					if (player.currentCastingAbility().dealWeaponDamageMin() >= 0) {
						dmg = Helper.getRandomIntFromRange(player.currentCastingAbility().dealWeaponDamageMax(), player
								.currentCastingAbility().dealWeaponDamageMin());
						if (isBonus)
							dmg = Helper.getRandomIntFromRange(
									player.currentCastingAbility().dealWeaponDamageMaxBonus(),
									player.currentCastingAbility().dealWeaponDamageMinBonus());
					}

					// deal stat based damage
					else if (player.currentCastingAbility().dealStatIsHpPercentDamageSourceOrTargetFlag() >= 0) {
						dmg = Helper.getCastingStatDamage(player.currentCastingAbility(), isBonus, player, monster);
					}

					int dltAmt = dealAbilityDamage(player.currentCastingAbility().name(), player, monster, dmg, 0);

					appendLog(player.currentCastingAbility().name() + " dealt " + monster.name() + " " + -dltAmt
							+ " damage.", DefinitionGlobal.LOG_TYPE_MONSTER_TAKE_ABILITY_DMG);
					animateImpact();

					int refAmt = monster.getReflectedDamage(-dltAmt);
					if (refAmt > 0) {
						dealReflectedDamage(monster, player, refAmt);

						/*
						 * appendLog("It reflected " + monster.getReflectedDamage-(dltAmt) +
						 * " damage back!",
						 * DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_BAD);
						 * 
						 * player.updateHP(-monster.getReflectedDamage(dmg));
						 */
					}

				}

				// modify HP per turn
				if (player.currentCastingAbility().castingModifyHPPerTurnFlag() == 1) {
					if (player.currentCastingAbility().modifyHPActor() == 0) {
						// modify player's HP
						int amt = Helper.getPercentFromInt(player.currentCastingAbility().modifyHPPercentAmount(),
								player.maxHP());

						if (isBonus)
							amt = Helper.getPercentFromInt(player.currentCastingAbility().modifyHPPercentAmountBonus(),
									player.maxHP());

						if (amt > 0) {
							appendLog("You gained " + amt + " HP from " + player.currentCastingAbility().name() + ".",
									DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_HP);
							player.updateHP(amt);
						}

						else {
							appendLog("You lost " + -amt + " HP from " + player.currentCastingAbility().name() + ".",
									DefinitionGlobal.LOG_TYPE_PLAYER_LOSES_HP);
							player.updateHP(amt);
							try {
								if (vfxOverlay != null && playerBar != null) {
									float px = playerBar.getLeft() + playerBar.getWidth() * 0.5f;
									float py = playerBar.getTop() + playerBar.getHeight() * 1.8f;
									vfxOverlay.addEffect(new DotPulseEffect(px, py, 120f, 500));
									vfxOverlay.addEffect(new DotAuraEffect(px, py, 90f, 900, 0x88FF3344));
								}
							} catch (Exception ignored) {
							}
						}
					} else {
						// modify monster's HP
						int amt = Helper.getPercentFromInt(player.currentCastingAbility().modifyHPPercentAmount(),
								monster.maxHP());

						if (isBonus)
							amt = Helper.getPercentFromInt(player.currentCastingAbility().modifyHPPercentAmountBonus(),
									monster.maxHP());

						if (amt < 0) {

							animateImpact();
							int dltAmt = dealAbilityDamage(player.currentCastingAbility().name(), player, monster, -amt,
									0);

							appendLog(monster.name() + " lost " + -dltAmt + " HP from "
									+ player.currentCastingAbility().name() + ".",
									DefinitionGlobal.LOG_TYPE_MONSTER_LOSES_HP);
						} else {
							appendLog(monster.name() + " gained " + amt + " HP from "
									+ player.currentCastingAbility().name() + ".",
									DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_HP);
							monster.updateHP(amt);
						}
					}
				}

				// modify AP per turn
				if (player.currentCastingAbility().castingModifyAPPerTurnFlag() == 1) {
					if (player.currentCastingAbility().modifyAPActor() == 0) {
						// modify player's AP
						int amt = Helper.getPercentFromInt(player.currentCastingAbility().modifyAPPercentAmount(),
								player.maxAP());

						if (isBonus)
							amt = Helper.getPercentFromInt(player.currentCastingAbility().modifyAPPercentAmountBonus(),
									player.maxAP());

						appendLog("You gained " + amt + " AP from " + player.currentCastingAbility().name() + ".",
								DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_AP);
						player.updateAP(amt);
					} else {
						// modify monster's AP
						int amt = Helper.getPercentFromInt(player.currentCastingAbility().modifyAPPercentAmount(),
								monster.maxAP());

						if (isBonus)
							amt = Helper.getPercentFromInt(player.currentCastingAbility().modifyAPPercentAmountBonus(),
									monster.maxAP());

						appendLog(monster.name() + " lost " + amt + " AP from " + player.currentCastingAbility().name()
								+ ".", DefinitionGlobal.LOG_TYPE_MONSTER_LOSES_AP);
						monster.updateAP(amt);
					}
				}
			}
		}
		// monster is casting
		else {
			boolean isBonus = Helper.checkForBonus(monster.currentCastingAbility(), monster, player);

			// casting is over, do anything at end?
			if (monster.castingTurnsLeft() <= 0) {
				// deal damage on end

				castingWaitToApplyDamagePlayer = false;

				if (monster.currentCastingAbility().castingDamageOnEndFlag() == 1) {
					int dmg = 0;

					// deal direct wpn dmg based
					if (monster.currentCastingAbility().dealWeaponDamageMin() > 0) {
						dmg = Helper.getRandomIntFromRange(monster.currentCastingAbility().dealWeaponDamageMax(),
								monster
										.currentCastingAbility().dealWeaponDamageMin());
						if (isBonus)
							dmg = Helper.getRandomIntFromRange(
									monster.currentCastingAbility().dealWeaponDamageMaxBonus(), monster
											.currentCastingAbility().dealWeaponDamageMinBonus());
					}
					// multiple stat dmg
					if (monster.currentCastingAbility().dealMultipleStatBasedDamageStat1() >= 0) {
						dmg = Helper.getMultipleStatDamage(monster, monster.currentCastingAbility());
					}

					appendLog(monster.currentCastingAbility().name() + " dealt " + player.name() + " " + dmg
							+ " damage.", DefinitionGlobal.LOG_TYPE_PLAYER_TAKE_ABILITY_DMG);

					if (vfxOverlay != null && playerBar != null) {
						float px = playerBar.getLeft() + playerBar.getWidth() * 0.5f;
						float py = playerBar.getTop() + playerBar.getHeight() * 1.8f;
						vfxOverlay.addEffect(new DotPulseEffect(px, py, 120f, 500));
					}

					if (player.getReflectedDamage(dmg) > 0) {
						appendLog("You reflected " + player.getReflectedDamage(dmg) + " damage back!",
								DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_GOOD);
						monster.updateHP(-player.getReflectedDamage(dmg));
					}
					player.updateHP(-dmg);
				}

				// apply effect on end (only to target)
				if (monster.currentCastingAbility().castingApplyEffectOnEndFlag() == 1) {
					castingWaitToApplyEffectPlayer = false;

					if (monster.currentCastingAbility().appliesEffectTarget().length > 0) {
						for (int a = 0; a < monster.currentCastingAbility().appliesEffectTarget().length; a++) {
							if (player.immuneToBadEffects()
									&& DefinitionEffects.EFFECT_IS_GOOD_FLAG_FOR_PLAYER[monster.currentCastingAbility()
											.appliesEffectSource()[a]] != 1) {
								appendLog(player.name()
										+ " is too Focused to become "
										+ DefinitionEffects.EFFECT_NAMES[monster.currentCastingAbility()
												.appliesEffectSource()[a]]
										+ "!", DefinitionGlobal.LOG_TYPE_DEFAULT);
							} else {
								player.addActiveEffect(monster.currentCastingAbility().appliesEffectTarget()[a]);

								if (DefinitionEffects.EFFECT_IS_GOOD_FLAG_FOR_PLAYER[monster.currentCastingAbility()
										.appliesEffectTarget()[a]] == 1) {
									appendLog("You are "
											+ DefinitionEffects.EFFECT_NAMES[monster.currentCastingAbility()
													.appliesEffectTarget()[a]]
											+ ".",
											DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_GOOD);
								} else {
									appendLog("You are "
											+ DefinitionEffects.EFFECT_NAMES[monster.currentCastingAbility()
													.appliesEffectTarget()[a]]
											+ ".",
											DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_BAD);
								}
							}
						}
					}

				}
			}
			// currently casting, do anything on each turn?
			if (monster.castingTurnsLeft() >= 0) {
				// deal damage on turn
				if (monster.currentCastingAbility().castingDamageOnTurnFlag() == 1) {
					int dmg = 0;

					// deal basic weapon damage
					if (monster.currentCastingAbility().dealWeaponDamageMin() >= 0) {
						dmg = Helper.getRandomIntFromRange(monster.currentCastingAbility().dealWeaponDamageMax(),
								monster
										.currentCastingAbility().dealWeaponDamageMin());
						if (isBonus)
							dmg = Helper.getRandomIntFromRange(
									monster.currentCastingAbility().dealWeaponDamageMaxBonus(), monster
											.currentCastingAbility().dealWeaponDamageMinBonus());
					}

					// deal stat based damage
					else if (monster.currentCastingAbility().dealStatIsHpPercentDamageSourceOrTargetFlag() >= 0) {
						dmg = Helper.getCastingStatDamage(monster.currentCastingAbility(), isBonus, monster, player);
					}

					appendLog(monster.currentCastingAbility().name() + " dealt " + player.name() + " " + dmg
							+ " damage.", DefinitionGlobal.LOG_TYPE_PLAYER_TAKE_ABILITY_DMG);

					if (vfxOverlay != null && playerBar != null) {
						float px = playerBar.getLeft() + playerBar.getWidth() * 0.5f;
						float py = playerBar.getTop() + playerBar.getHeight() * 1.8f;
						vfxOverlay.addEffect(new DotPulseEffect(px, py, 120f, 500));
					}

					if (player.getReflectedDamage(dmg) > 0) {
						appendLog("You reflected " + player.getReflectedDamage(dmg) + " damage back!",
								DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_EFFECT_GOOD);
						monster.updateHP(-player.getReflectedDamage(dmg));
					}
					player.updateHP(-dmg);
				}

				// mod HP per turn
				if (monster.currentCastingAbility().castingModifyHPPerTurnFlag() == 1) {
					if (monster.currentCastingAbility().modifyHPActor() == 0) {
						// modify monster's HP
						int amt = Helper.getPercentFromInt(monster.currentCastingAbility().modifyHPPercentAmount(),
								monster.maxHP());

						if (isBonus)
							amt = Helper.getPercentFromInt(monster.currentCastingAbility().modifyHPPercentAmountBonus(),
									monster.maxHP());

						appendLog(monster.name() + " gained " + amt + " HP from "
								+ monster.currentCastingAbility().name() + ".",
								DefinitionGlobal.LOG_TYPE_MONSTER_GAINS_HP);

						monster.updateHP(amt);
					} else {
						// modify player's HP
						int amt = Helper.getPercentFromInt(monster.currentCastingAbility().modifyHPPercentAmount(),
								player.maxHP());

						if (isBonus)
							amt = Helper.getPercentFromInt(monster.currentCastingAbility().modifyHPPercentAmountBonus(),
									player.maxHP());

						appendLog(monster.name() + "'s " + monster.currentCastingAbility().name() + " drained " + amt
								+ " HP from you.", DefinitionGlobal.LOG_TYPE_PLAYER_LOSES_HP);

						player.updateHP(amt);
					}
				}

				// gain AP per turn
				if (monster.currentCastingAbility().castingModifyAPPerTurnFlag() == 1) {
					if (monster.currentCastingAbility().modifyAPActor() == 0) {
						// modify monster's AP
						int amt = Helper.getPercentFromInt(monster.currentCastingAbility().modifyAPPercentAmount(),
								monster.maxAP());

						if (isBonus)
							amt = Helper.getPercentFromInt(monster.currentCastingAbility().modifyAPPercentAmountBonus(),
									monster.maxAP());

						appendLog("You gained " + amt + " AP from " + monster.currentCastingAbility().name() + ".",
								DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_AP);
						monster.updateAP(amt);
					} else {
						// modify player's AP
						int amt = Helper.getPercentFromInt(monster.currentCastingAbility().modifyAPPercentAmount(),
								player.maxAP());

						if (isBonus)
							amt = Helper.getPercentFromInt(monster.currentCastingAbility().modifyAPPercentAmountBonus(),
									player.maxAP());

						appendLog(player.name() + " lost " + amt + " AP from " + monster.currentCastingAbility().name()
								+ ".", DefinitionGlobal.LOG_TYPE_PLAYER_LOSES_AP);
						player.updateAP(amt);
					}
				}
			}
		}

	}

	private void startMonsterAttack() {
		Log.d("combat", "startMonsterAttack()");

		if (inCombat == false)
			return;

		if (monster.casting()) {
			doCastingTurn(1);
			endMonsterAttack();
			return;
		}

		int didAbility = 0;

		// the monster is attacking the player

		// if monster has no ap, skip this
		if (monster.currentAP() > 0) {
			// this will return -1 if an ability should not be used, forcing a
			// weapon attack
			int useAbilityId = monster.getUseAbilityId(
					(int) Math.floor((double) player.currentHP() / (double) player.maxHP()),
					player.currentAP());

			if (useAbilityId >= 0) {
				didAbility = 1;

				appendLog(monster.name() + " is using "
						+ (String) DefinitionRunes.runeData[useAbilityId][DefinitionRunes.RUNE_NAMES][0] + "!",
						DefinitionGlobal.LOG_TYPE_MONSTER_USE_ABILITY);

				doMonsterAbility(useAbilityId);
			}
		}

		if (didAbility == 0) {
			monster.updateNoAbilityInARow();
			monsterAttackPlayer();
			// endMonsterAttack();
		}

	}

	private void endMonsterAttack() {
		turnCleanup();

		if (inCombat == false) {
			updateViews();
			return;
		} else {
			turnFlag = 0;
			Log.d("advanceturn", "endMonsterAttack...");
			advanceTurn();
		}
	}

	private void advanceTurnDelay(double d) {
		Log.d("advanceturn", "advanceTurnDelay...");
		try {
			advanceTurnHandler.removeCallbacks(advanceTurnPost);
			advanceTurnHandler.postDelayed(advanceTurnPost, (long) (d * 1000));
		} catch (Exception e) {
			advanceTurnPost.run();
		}
	}

	private Runnable advanceTurnPost = new Runnable() {
		public void run() {
			Log.d("advanceturn", "advanceTurnPost...");
			advanceTurn();
		}
	};

	private void startMonsterAnimateDelay(double d) {
		try {
			animateMonsterHandler.removeCallbacks(animateMonsterPost);
			animateMonsterHandler.postDelayed(animateMonsterPost, (long) (d * 1000));
		} catch (Exception e) {
			animateMonsterPost.run();
		}

	}

	private Runnable animateMonsterPost = new Runnable() {
		public void run() {
			animateMonster(0, monster.getNextAnimation());
		}
	};

	private void advanceTurn() {
		Log.d("advanceturn", "advanceTurn()");
		player.setPlayerAttackWait(true);

		updateButtons();

		turnCleanup(); // check for deaths

		// decide who can go next

		if (inCombat == false) {
			updateViews();
			return;
		}

		// player's turn?
		if (turnFlag == 0) {

			// start animate monster delay
			startMonsterAnimateDelay(1.0);

			// mod AP on turn amount
			if (DefinitionClassType.CLASS_TYPE_AP_REGEN[DefinitionClasses.CLASS_TYPE[player.playerClass()]][3] != 0) {
				player.updateAP(DefinitionClassType.CLASS_TYPE_AP_REGEN[DefinitionClasses.CLASS_TYPE[player
						.playerClass()]][2]);
			}

			if (player.name().length() > 5 && player.name().substring(0, 5).equals(DefinitionGlobal.TESTCHAR)) {
				player.setCurrentAP(999);
				player.setCurrentHP(999);
			}

			Log.d("advanceturn", "player.advanceTurn()");
			ArrayList<Actor.ReturnData> returnData = player.advanceTurn();
			if (returnData != null) {
				for (int a = 0; a < returnData.size(); a++) {
					appendLog(returnData.get(a).whatHappend, returnData.get(a).logType);
				}
			}

			// if player is not stunned and not casting, it can go
			if (player.stunned() == false && player.casting() == false) {
				player.setPlayerAttackWait(false);
				// animateMonster(0, monster.getNextAnimation());
			} else {
				turnFlag = 1;
				player.setPlayerAttackWait(true);

				if (player.casting() == true) {
					// TODO what happens with this line below?
					// checkPlayerCastingAbilities();

					doCastingTurn(0);

					turnCleanup();
				}
			}

			updateViews();
		}

		// monster's turn?
		if (turnFlag == 1) {

			// check if monster has temp image up
			if (monster.tempImageTurns() > 0) {
				Log.d("combat", "monster should have temp image for " + monster.tempImageTurns() + " turns");
				monsterImage.setImageResource(monster.tempImageResource());
				monsterImage.invalidate();
			} else {
				monsterImage.setImageResource(monster.imageResource());
				monsterImage.invalidate();
			}

			// maybe play a taunt
			if (Helper.randomInt(100) < 20) {
				SoundManager.playSound(DefinitionMonsters.MONSTER_TAUNT_SOUND_TYPE[monster.monsterID()], false);

			}

			Log.d("advanceturn", "monster.advanceTurn()");
			ArrayList<Actor.ReturnData> returnData = monster.advanceTurn();
			for (int a = 0; a < returnData.size(); a++) {
				appendLog(returnData.get(a).whatHappend, returnData.get(a).logType);
			}

			// if the monster is not stunned and not casting, it can go
			if (monster.casting()) {
				startMonsterAttackDelay(1.8);
				turnCleanup();
			} else if (monster.stunned() == false) {
				player.setPlayerAttackWait(true);

				// wait some time then have the monster attack
				startMonsterAttackDelay(1.4);
			} else {
				turnFlag = 0;
				updateViews();
				advanceTurnDelay(1.3);
			}

		}
	}

	private void displayMonsterEffects() {
		for (int a = 0; a < monsterEffectImages.length; a++) {
			monsterEffectImages[a].setImageResource(0);
		}

		for (int i = 0; i < monster.getActiveEffects().size(); i++) {
			if (monster.getActiveEffectByIndex(i).imageResource() == 0) {
				monster.getActiveEffectByIndex(i).setImageResource(
						getResources().getIdentifier(monster.getActiveEffectByIndex(i).image(), "drawable",
								getPackageName()));
			}

			monsterEffectImages[i].setImageResource(monster.getActiveEffectByIndex(i).imageResource());
		}
	}

	private void displayPlayerEffects() {
		for (int a = 0; a < playerEffectImages.length; a++) {
			playerEffectImages[a].setImageResource(0);
		}

		for (int i = 0; i < player.getActiveEffects().size(); i++) {
			if (player.getActiveEffectByIndex(i).imageResource() == 0) {
				player.getActiveEffectByIndex(i).setImageResource(
						getResources()
								.getIdentifier(player.getActiveEffectByIndex(i).image(), "drawable", getPackageName()));
			}

			playerEffectImages[i].setImageResource(player.getActiveEffectByIndex(i).imageResource());

		}
	}

	private void turnCleanup() {
		if (player.currentHP() <= 0 || monster.currentHP() <= 0) {
			inCombat = false;
			player.setPlayerAttackWait(true);

			player.stopCasting();
			monster.stopCasting();

			clearAll();

			// IF THE PLAYER IS DEAD
			if (player.currentHP() < 1) {
				player.setPlayerAttackWait(true);
				player.setDead(true);
				appendLog("You have died.", DefinitionGlobal.LOG_TYPE_SOMETHING_BAD_FOR_PLAYER);

			}
			// IF THE MONSTER IS DEAD
			else {
				monster.setCurrentHP(0);
				monster.setDead(true);
				appendLog("You have won!", DefinitionGlobal.LOG_TYPE_SOMETHING_GOOD_FOR_PLAYER);

				killMonster();
			}
		}
	}

	private void restartFight() {
		Intent i = null;
		Bundle b = new Bundle();

		// show next fight or end of round
		i = new Intent(getApplicationContext(), com.alderangaming.wizardsencounters.ControllerFightStart.class);

		// b.putSerializable("OwnedItems", OwnedItems);
		b.putSerializable("player", player);
		b.putInt("restartfightflag", 2);
		b.putInt("restartfightmonster", monster.monsterID());
		b.putInt("background", backgroundImage);

		i.putExtras(b);

		startActivity(i);
		finish();
	}

	private void showRunAwayDialog() {

		SoundManager.playSound(SoundManager.ALERTSOUND, true);

		final Dialog dialog = new Dialog(ControllerCombat.this);
		dialog.setContentView(R.layout.fleedialog);
		dialog.setTitle("Flee");
		TextView fleeTextView = (TextView) dialog.findViewById(R.id.fleeDialogText);
		fleeTextView
				.setText("You ran away! You may try this same fight again, starting with "
						+ (int) Math.round(player.maxHP() / 2) + "/"
						+ player.maxHP()
						+ " health. Or you can continue fighting and pretend this never happened. If you exit the Arena, you will lose all gold earned this round, and your character will revert to Rank 1, Round 1!");
		Button exitArenaButton = (Button) dialog.findViewById(R.id.fleeDialogExitRoundButton);
		Button donotexitArenaButton = (Button) dialog.findViewById(R.id.fleeDialogCancelButton);
		Button fleeDialogButton = (Button) dialog.findViewById(R.id.fleeDialogFleeButton);

		exitArenaButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				exitArena();
			}
		});
		donotexitArenaButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				player.setPlayerAttackWait(false);

				updateButtons();

				dialog.cancel();
			}
		});
		fleeDialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				appendLog("You ran away!", DefinitionGlobal.LOG_TYPE_DEFAULT);
				monster.setCurrentHP(0);
				monster.setDead(true);
				player.updateHP((int) Math.round((double) player.maxHP() / 2.0));
				inCombat = false;

				clearAll();

				dialog.cancel();

				DBHandler.updatePlayer(player);

				if (DBHandler.isOpen(getApplicationContext())) {
					DBHandler.close();
				}

				Intent i = null;
				Bundle b = new Bundle();

				// show next fight or end of round
				i = new Intent(getApplicationContext(),
						com.alderangaming.wizardsencounters.ControllerFightStart.class);

				// b.putSerializable("OwnedItems", OwnedItems);
				b.putSerializable("player", player);
				b.putInt("restartfightflag", 1);
				b.putInt("restartfightmonster", monster.monsterID());
				b.putInt("background", backgroundImage);

				i.putExtras(b);

				startActivity(i);
				finish();
			}
		});
		dialog.show();

		/*
		 * AlertDialog.Builder fightEndDialog = new AlertDialog.Builder(this);
		 * 
		 * fightEndDialog
		 * .setTitle(
		 * "Fight " + player.currentFight() + " of "
		 * + DefinitionRounds.ROUND_NUMBER_OF_FIGHTS[player.currentRound() - 1] +
		 * " Aborted")
		 * .setMessage(
		 * "You ran away! You may try this fight again, starting with " + (int)
		 * Math.round(player.maxHP() / 2) + "/"
		 * + player.maxHP() +
		 * " health. Or you can continue fighting. Remember - Round progression and gold is only saved at the end of each Round!"
		 * )
		 * .setPositiveButton("Try Again", new DialogInterface.OnClickListener()
		 * {
		 * public void onClick(DialogInterface dialog, int id)
		 * {
		 * flee();
		 * 
		 * dialog.cancel();
		 * 
		 * DBHandler.updatePlayer(player);
		 * 
		 * if (DBHandler.isOpen(getApplicationContext()))
		 * {
		 * DBHandler.close();
		 * }
		 * 
		 * Intent i = null;
		 * Bundle b = new Bundle();
		 * 
		 * // show next fight or end of round
		 * i =
		 * new Intent(getApplicationContext(),
		 * com.alderangaming.wizardsencounters.ControllerFightStart.class);
		 * 
		 * // b.putSerializable("OwnedItems", OwnedItems);
		 * b.putSerializable("player", player);
		 * b.putInt("restartfightflag", 1);
		 * b.putInt("restartfightmonster", monster.monsterID());
		 * b.putInt("background", backgroundImage);
		 * 
		 * i.putExtras(b);
		 * 
		 * startActivity(i);
		 * finish();
		 * }
		 * })
		 * .setNeutralButton("Continue", new DialogInterface.OnClickListener()
		 * {
		 * public void onClick(DialogInterface dialog, int id)
		 * {
		 * player.setPlayerAttackWait(false);
		 * 
		 * updateButtons();
		 * 
		 * dialog.cancel();
		 * 
		 * }
		 * });
		 * 
		 * AlertDialog alert = fightEndDialog.create();
		 * alert.show();
		 */
	}

	private void warpToRound() {
		Intent i = new Intent(getApplicationContext(), com.alderangaming.wizardsencounters.ControllerRoundStart.class);
		Bundle b = new Bundle();

		// b.putSerializable("OwnedItems", OwnedItems);
		b.putSerializable("player", player);

		i.putExtras(b);

		startActivity(i);

		finish();
	}

	private void showFightEndDialog() {
		AlertDialog.Builder fightEndDialog = new AlertDialog.Builder(this);

		fightEndDialog.setTitle(
				"Fight " + player.currentFight() + " of "
						+ DefinitionRounds.ROUND_NUMBER_OF_FIGHTS[player.currentRound() - 1] + " Complete")
				.setPositiveButton(
						"Continue", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								player.updateCurrentFight();
								dialog.cancel();

								if (DefinitionRounds.ROUND_TYPE[player.currentRound() - 1] == 1) {
									player.setRank(player.rank() + 1);
									DBHandler.updatePlayer(player);
								}

								if (DBHandler.isOpen(getApplicationContext())) {
									DBHandler.close();
								}

								Intent i = null;
								Bundle b = new Bundle();

								// show next fight or end of round
								if (player
										.currentFight() > DefinitionRounds.ROUND_NUMBER_OF_FIGHTS[player.currentRound()
												- 1]) {

									i = new Intent(getApplicationContext(),
											com.alderangaming.wizardsencounters.ControllerRoundEnd.class);
								}

						else {
									i = new Intent(getApplicationContext(),
											com.alderangaming.wizardsencounters.ControllerFightStart.class);
								}

								// b.putSerializable("OwnedItems", OwnedItems);
								b.putSerializable("player", player);
								b.putInt("background", backgroundImage);

								i.putExtras(b);

								startActivity(i);
								finish();
							}
						});

		/*
		 * // why have this option if it's just going to f them over anyways if
		 * they really want to exit combat, they'll use the back button
		 * .setNegativeButton("Exit Arena", new
		 * DialogInterface.OnClickListener() { public void
		 * onClick(DialogInterface dialog, int id) { dialog.cancel();
		 * 
		 * if (DBHandler.isOpen(getApplicationContext())) { DBHandler.close(); }
		 * 
		 * finish(); } });
		 */

		AlertDialog alert = fightEndDialog.create();
		alert.setCancelable(false);
		alert.show();

		SoundManager.unloadBattleSoundEffects();

		SoundManager.playSound(SoundManager.FIGHT_WON, true);

		DBHandler.updateGlobalStats(OwnedItems.gold());

	}

	private Runnable endExplosionRunnable = new Runnable() {
		public void run() {
			explosionWaitHandler.removeCallbacks(endExplosionRunnable);
			showFightEndDialog();
		}
	};

	private void killMonster() {

		int goldDrop = monster.getGoldDrop();
		OwnedItems.updateGold(goldDrop);
		appendLog("The monster dropped " + goldDrop + " gold.", DefinitionGlobal.LOG_TYPE_LOOT);

		checkForArtifactDrop();

		animateMonster(1, Animator.MOVE_UP);
		monsterImage.setImageResource(R.anim.largeexplosion);
		AnimationDrawable explosionAnimation = (AnimationDrawable) monsterImage.getDrawable();
		explosionWaitHandler.postDelayed(endExplosionRunnable, 2500);

		explosionAnimation.stop();
		explosionAnimation.start();
		SoundManager.playSound(DefinitionMonsters.MONSTER_DEATH_SOUND_TYPE[monster.monsterID()], true);
		SoundManager.playSound(SoundManager.SOUND_TYPE_EXPLOSION_LARGE, true);

	}

	private void animateImpact() {
		SoundManager.playSound(SoundManager.SOUND_TYPE_EXPLOSION_SMALL, true);
		impactImage.setImageResource(R.anim.smallexplosion);
		AnimationDrawable impactAnimation = (AnimationDrawable) impactImage.getDrawable();
		impactAnimation.stop();
		impactAnimation.start();

		// Procedural VFX overlay
		try {
			if (vfxOverlay != null && monsterImage != null) {
				float cx = monsterImage.getLeft() + monsterImage.getWidth() * 0.5f;
				float cy = monsterImage.getTop() + monsterImage.getHeight() * 0.5f;
				vfxOverlay.addEffect(new ScreenFlashEffect(0x80FFFFFF, 140));
				vfxOverlay.addEffect(
						new ShockwaveRingEffect(cx, cy, Math.max(monsterImage.getWidth(), monsterImage.getHeight()),
								android.os.SystemClock.uptimeMillis(), 320, 0xFFFFFFFF));
				vfxOverlay.addEffect(new SparkBurstEffect(cx, cy, 22, 520, 0xFFFFEE99, 520f));
				vfxOverlay.addEffect(new RuneCircleEffect(cx, cy,
						Math.min(monsterImage.getWidth(), monsterImage.getHeight()) * 0.38f, 900, 0xFFAA88FF));
				vfxOverlay.addEffect(new ImpactShakeEffect(240, 14f));
				// stronger ambient push on impact
				if (ambientOverlay != null)
					ambientOverlay.pushAmbient(cx, cy, 1200f,
							Math.max(monsterImage.getWidth(), monsterImage.getHeight()) * 0.9f);
			}
		} catch (Exception ignored) {
		}
	}

	private void animateMonster(final int type, int animationId) {
		// type = 0 is moving on turn start, 1 is event driven

		Log.d("animatemonster", "animate monster called: type " + type + ", id " + animationId);

		final Animation a = Animator.getAnimation(animationId);

		if (monster.isAnimating() && type == 0)
			return;

		if (a != null)
			a.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationEnd(Animation arg0) {
					monster.setAnimating(arg0, false);

					Log.d("animatemonster", "onAnimationEnd called: type " + type);
					if (monster.animationRepeats()) {
						animateMonster(0, monster.getNextAnimation());
					}
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationStart(Animation animation) {
					monster.setAnimating(animation, true);
				}

			});

		if (a != null)
			monsterImage.startAnimation(a);

	}

	private void clearAll() {
		playerAttackText.setText("");
		monsterAttackText.setText("");
		playerAbilityText.setText("");

		// reset flags also calls clearCounters()
		player.resetFlags();
		monster.resetFlags();

		player.removeEffects();
		monster.removeEffects();

	}

	private void playerHitMonster(int attackTypeId) {

		moveAttackImage(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_WEAPON, player.equippedWeapon()),
				attackTypeId);

		if (player.name().contains(DefinitionGlobal.TESTCHAR4)) {
			killMonster();
			return;
		}

		// HIT: dmg amt, crit flag, stun flag
		// MISS: -1, roll, hit chance
		int[] tryHitResult = player.tryHit(attackTypeId);

		/*
		 * RelativeLayout.LayoutParams lp = new
		 * RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
		 * LayoutParams.WRAP_CONTENT); lp.leftMargin = left; lp.topMargin = top;
		 * 
		 * 
		 * playerAttackText.setLayoutParams(lp);
		 */

		player.updateNoAbilityInARow();

		playerAttackText.setVisibility(View.VISIBLE);

		if (tryHitResult[0] > 0) {
			int[] assignDmgResult = dealMonsterHitDamage(tryHitResult[0]);

			// Procedural VFX for weapon hit
			try {
				if (vfxOverlay != null && monsterImage != null) {
					float cx = monsterImage.getLeft() + monsterImage.getWidth() * 0.5f;
					float cy = monsterImage.getTop() + monsterImage.getHeight() * 0.5f;
					int sparkCount = (tryHitResult[1] > 0) ? 60 : 28;
					int sparkDur = (tryHitResult[1] > 0) ? 900 : 600;
					float sparkSpeed = (tryHitResult[1] > 0) ? 980f : 720f;
					vfxOverlay.addEffect(new SparkBurstEffect(cx, cy, sparkCount, sparkDur, 0xFFFFD080, sparkSpeed));
					if (tryHitResult[1] > 0) {
						// extra silver and red sparks on crit
						vfxOverlay.addEffect(new SparkBurstEffect(cx, cy, 40, 900, 0xFFCCCCCC, 960f));
						vfxOverlay.addEffect(new SparkBurstEffect(cx, cy, 24, 900, 0xFFFF4455, 900f));
					}
					float tx = cx + (Helper.randomInt(21) - 10);
					float ty = monsterImage.getTop() + monsterImage.getHeight() * 0.33f;
					vfxOverlay.addEffect(new FloatingTextEffect("" + assignDmgResult[0], tx,
							ty, 1400,
							(tryHitResult[1] > 0) ? FloatingTextEffect.Kind.CRIT : FloatingTextEffect.Kind.NORMAL,
							assignDmgResult[0]));
					if (ambientOverlay != null)
						ambientOverlay.pushAmbient(cx, cy, 700f,
								Math.max(monsterImage.getWidth(), monsterImage.getHeight()) * 0.7f);
				}
			} catch (Exception ignored) {
			}

			// CRIT
			if (tryHitResult[1] > 0) {
				playerAttackText.setTextColor(0xFF2D0DCA);
				appendLog("You CRIT " + monster.name() + " for " + assignDmgResult[0] + ".",
						DefinitionGlobal.LOG_TYPE_MONSTER_TAKE_CRIT_DMG);
			} else {
				appendLog("You hit " + monster.name() + " for " + assignDmgResult[0] + ".",
						DefinitionGlobal.LOG_TYPE_MONSTER_TAKE_HIT_DMG);
			}

			// stun
			if (tryHitResult[2] > 0) {
				appendLog("You stunned the monster!", DefinitionGlobal.LOG_TYPE_MONSTER_STUNNED);
				monster.addStunCount(1);
			}

			animateMonster(1, Animator.SMALL_SHAKE);

			// legacy inline damage text removed; floating text handles this now

			SoundManager.playSound(DefinitionAttackTypes.ATTACK_SOUND_CLIPS[attackTypeId], false);

			// class type mod AP on hit amount
			if (DefinitionClassType.CLASS_TYPE_AP_REGEN[DefinitionClasses.CLASS_TYPE[player.playerClass()]][2] != 0) {
				player.updateAP(DefinitionClassType.CLASS_TYPE_AP_REGEN[DefinitionClasses.CLASS_TYPE[player
						.playerClass()]][2]);
			}

		} else if (tryHitResult[0] == -999) {
			// block
			appendLog("You are blocking...", DefinitionGlobal.LOG_TYPE_DEFAULT);

			SoundManager.playSound(SoundManager.SOUND_TYPE_BLOCK, false);

			player.setBlockingFlag(tryHitResult[1]);
		}

		else {
			appendLog("You missed!", DefinitionGlobal.LOG_TYPE_PLAYER_MISSES);

			SoundManager.playSound(SoundManager.SOUND_TYPE_MISS, true);

			// Floating MISS text
			try {
				if (vfxOverlay != null && monsterImage != null) {
					float cx = monsterImage.getLeft() + monsterImage.getWidth() * 0.5f;
					float cy = monsterImage.getTop() + monsterImage.getHeight() * 0.4f;
					vfxOverlay.addEffect(new FloatingTextEffect("MISS", cx, cy, 1000, FloatingTextEffect.Kind.MISS));
				}
			} catch (Exception ignored) {
			}

			animateMonster(1, Animator.DODGE);
		}

		clearPlayerAttackTextDelay(1.8);
		endPlayerAttack();
	}

	private int dealAbilityDamage(String abilName, Actor source, Actor target, int amt, int sourceFlag) {
		Log.d("combat", "dealAbilityDamage: " + abilName + "," + source.name() + "," + target.name() + ", amt: " + amt
				+ ", source: " + sourceFlag);

		amt = -Helper.getStatMod(source.mageDiff(), amt);

		if (amt < 0)
			amt = checkForCounterShieldAbsorb(target, amt);

		if (amt < 0 && sourceFlag == 0) {
			amt = checkForShieldAbsorb(amt);
		}

		if (amt < 0) {
			target.updateHP(amt);

			if (sourceFlag == 0) {
				// Floating ability damage over monster
				try {
					if (vfxOverlay != null && monsterImage != null) {
						float cx = monsterImage.getLeft() + monsterImage.getWidth() * 0.5f;
						float cy = monsterImage.getTop() + monsterImage.getHeight() * 0.4f;
						vfxOverlay.addEffect(new FloatingTextEffect("" + (-amt), cx, cy, 1200,
								FloatingTextEffect.Kind.NORMAL, -amt));
					}
				} catch (Exception ignored) {
				}
			}
		}

		return amt;
	}

	private void monsterAttackPlayer() {
		animateMonster(1, Animator.MONSTER_LUNGE);

		Log.d("combat", "monsterAttackPlayer()");

		waitingForDodge = false;
		dodgeEvent = new DodgeEvent();

		int[] tryHitResult = monster.tryHit();
		Log.d("combat", "monster.getHitDamage() = " + tryHitResult[0] + "," + tryHitResult[1] + "," + tryHitResult[2]);

		if (100 - Helper.randomInt(101) < player.dodgeChance()) {
			// player gets opportunity to dodge this attack

			Log.d("combat", "player rolled to dodge");

			double time = DefinitionGlobal.DODGE_TIME + (player.luck() / 6);

			if (time < 1.0) {
				time = 1.0;
			}

			else if (time > 2.5)
				time = 2.5;

			disableCombatButtons();

			// Position near the monster's center (with small jitter)
			try {
				int mx = monsterImage.getLeft() + (monsterImage.getWidth() / 2);
				int my = monsterImage.getTop() + (monsterImage.getHeight() / 2);
				int jitterX = Helper.randomInt(81) - 40; // -40..40
				int jitterY = Helper.randomInt(81) - 40;
				int btnW = dodgeButton.getWidth();
				int btnH = dodgeButton.getHeight();
				if (btnW <= 0)
					btnW = 80;
				if (btnH <= 0)
					btnH = 80;
				android.widget.RelativeLayout.LayoutParams startLpPos = new android.widget.RelativeLayout.LayoutParams(
						android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
						android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
				startLpPos.leftMargin = Math.max(0,
						Math.min(combatLayout.getWidth() - btnW, mx + jitterX - (btnW / 2)));
				startLpPos.topMargin = Math.max(0,
						Math.min(combatLayout.getHeight() - btnH, my + jitterY - (btnH / 2)));
				dodgeButton.setLayoutParams(startLpPos);
			} catch (Exception ignored) {
				// fallback to random placement if any metrics missing
				dodgeButton.setLayoutParams(Helper.getRandomButtonLayout(combatLayout));
			}
			dodgeButton.setEnabled(true);
			dodgeButton.setVisibility(View.VISIBLE);
			try {
				if (dodgeWanderRunnable != null)
					dodgeWanderHandler.removeCallbacks(dodgeWanderRunnable);
				android.widget.RelativeLayout.LayoutParams startLp = (android.widget.RelativeLayout.LayoutParams) dodgeButton
						.getLayoutParams();
				final int originX = startLp.leftMargin + (dodgeButton.getWidth() / 2);
				final int originY = startLp.topMargin + (dodgeButton.getHeight() / 2);
				final long startMs = android.os.SystemClock.uptimeMillis();
				dodgeWanderRunnable = new Runnable() {
					@Override
					public void run() {
						if (dodgeButton.getVisibility() != View.VISIBLE)
							return;
						long now = android.os.SystemClock.uptimeMillis();
						float t = (now - startMs) / 1000f; // seconds since appear
						if (t >= 3.0f) {
							dodgeButton.setEnabled(false);
							dodgeButton.setVisibility(View.INVISIBLE);
							try {
								if (dodgeWanderRunnable != null)
									dodgeWanderHandler.removeCallbacks(dodgeWanderRunnable);
							} catch (Exception ignored) {
							}
							return;
						}
						float angle = t * 4.2f; // radians/sec
						float radius = 250f + t * 220f; // expand outward from origin
						int targetX = (int) (originX + Math.cos(angle) * radius) - dodgeButton.getWidth() / 2;
						int targetY = (int) (originY + Math.sin(angle) * radius) - dodgeButton.getHeight() / 2;
						targetX = Math.max(0, Math.min(combatLayout.getWidth() - dodgeButton.getWidth(), targetX));
						targetY = Math.max(0, Math.min(combatLayout.getHeight() - dodgeButton.getHeight(), targetY));
						android.widget.RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) dodgeButton
								.getLayoutParams();
						android.view.animation.TranslateAnimation ta = new android.view.animation.TranslateAnimation(0,
								targetX - lp.leftMargin, 0, targetY - lp.topMargin);
						ta.setDuration(10);
						ta.setFillAfter(false);
						dodgeButton.startAnimation(ta);
						lp.leftMargin = targetX;
						lp.topMargin = targetY;
						dodgeButton.setLayoutParams(lp);
						dodgeWanderHandler.postDelayed(this, 10);
					}
				};
				dodgeWanderHandler.postDelayed(dodgeWanderRunnable, 10);
			} catch (Exception ignored) {
			}

			dodgeEvent.dodgeID = Helper.randomInt(5000);
			randomDodgeID = dodgeEvent.dodgeID;

			// save results of tryHit in case player fails to dodge, these
			// values will be applied
			dodgeEvent.damage = tryHitResult[0]; // dmg
			dodgeEvent.critSuccess = tryHitResult[1]; // crit flag
			dodgeEvent.stunSuccess = tryHitResult[2]; // stun flag
			waitingForDodge = true;

			checkDodgeDelay(time);
		} else if (tryHitResult[0] >= 0) {
			// monster hits with no chance of player dodging it

			try {
				if (vfxOverlay != null && monsterImage != null && playerBar != null && combatBottomLayout != null) {
					float sx = monsterImage.getLeft() + monsterImage.getWidth() * 0.5f + (Helper.randomInt(35) - 17);
					float sy = monsterImage.getTop() + monsterImage.getHeight() * 0.22f;
					float tx = playerBar.getLeft() + playerBar.getWidth() * 0.5f + (Helper.randomInt(45) - 22);
					float ty = combatBottomLayout.getTop() + combatBottomLayout.getHeight() - 20;
					vfxOverlay.addEffect(new FloatingTextEffect("" + tryHitResult[0], sx, sy, tx, ty, 1600,
							FloatingTextEffect.Kind.PLAYER_DAMAGE, tryHitResult[0]));
					// stronger feedback on taking damage
					vfxOverlay.addEffect(new ImpactShakeEffect(360, 24f));
					vfxOverlay.addEffect(new ScreenFlashEffect(0x40FF2222, 220));
				}
			} catch (Exception ignored) {
			}

			dealPlayerHitDamage(tryHitResult);
			player.clearBlockingFlag();

		} else {
			// monster missed
			appendLog("The monster missed!", DefinitionGlobal.LOG_TYPE_MONSTER_MISSES);

			try {
				if (vfxOverlay != null && playerBar != null) {
					float px = playerBar.getLeft() + playerBar.getWidth() * 0.5f;
					float py = playerBar.getTop() + playerBar.getHeight() * 1.2f;
					vfxOverlay.addEffect(new FloatingTextEffect("MISS", px, py, 800, FloatingTextEffect.Kind.MISS));
				}
			} catch (Exception ignored) {
			}

			player.clearBlockingFlag();

			endMonsterAttack();
		}

		Animation mTextAnim = Animator.getAnimation(Animator.MONSTER_ATTACK_TEXT);
		mTextAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				monsterAttackText.setText("");
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationStart(Animation animation) {

			}

		});

		if (!waitingForDodge)
			monsterAttackText.startAnimation(mTextAnim);

		// clearMonsterAttackTextDelay(1.8);
	}

	private void dealPlayerHitDamage(int[] hitResult) {

		int hitAmount = hitResult[0];
		int critSuccess = hitResult[1];
		int stunSuccess = hitResult[2];

		if (hitAmount < 1) {
			hitAmount = 1;
		}

		String hitType = "hit";

		int logType = DefinitionGlobal.LOG_TYPE_PLAYER_TAKE_HIT_DMG;

		if (critSuccess == 1) {
			hitType = "CRIT";
			logType = DefinitionGlobal.LOG_TYPE_PLAYER_TAKE_CRIT_DMG;
		}

		String hitText = monster.name() + " " + hitType + " you for " + hitAmount;

		if (stunSuccess == 0) {
			hitText += ".";
		} else {
			hitText += " and Stunned you!";
			player.addStunCount(1);
			logType = DefinitionGlobal.LOG_TYPE_PLAYER_STUNNED;
		}
		appendLog(hitText, logType);

		if (hitAmount > 0) {
			int refDmg = player.getReflectedDamage(hitAmount);
			if (refDmg > 0)
				dealReflectedDamage(player, monster, refDmg);

			if (player.blocking()) {
				int blockAmount = Helper.getPercentFromInt(player.blockingAmount(), hitAmount);

				if (Helper.randomInt(100) > player.blockingChance() + player.reaction()) {
					appendLog("(You got nervous and failed to block)", DefinitionGlobal.LOG_TYPE_PLAYER_MISSES);
				} else {
					if (blockAmount <= 0 && hitAmount > 0)
						blockAmount = 1;

					else if (hitAmount <= 0)
						return;

					appendLog("You blocked " + blockAmount + " damage!", DefinitionGlobal.LOG_TYPE_PLAYER_DODGES);
					hitAmount -= blockAmount;
				}

				player.setBlockingFlag(-1);
			}
		}

		if (hitAmount > 0) {
			// roll for armor block
			if (Helper.randomInt(100) < DefinitionGlobal.ARMOR_BLOCKS_CHANCE) {
				int armorBlockAmt = player.blockDmg()[0];
				if (armorBlockAmt > hitAmount) {
					hitAmount = 0;
					appendLog("Your armor blocked all the damage.", DefinitionGlobal.LOG_TYPE_PLAYER_DODGES);
				} else {
					hitAmount = hitAmount - armorBlockAmt;
					appendLog("Your armor blocked " + armorBlockAmt + " damage.",
							DefinitionGlobal.LOG_TYPE_PLAYER_DODGES);
				}
			}
		}

		if (hitAmount > 0)
			hitAmount = checkForCounterShieldAbsorb(player, hitAmount);

		if (hitAmount > 0)
			hitAmount = checkForShieldAbsorb(hitAmount);

		if (hitAmount > 0) {
			player.updateHP(-hitAmount);

		}

		if (hitAmount > 0) {
			SoundManager.playSound(SoundManager.TAKE_HIT, true);
			if (showImpact == 1) {
				combatBottomLayout.startAnimation(Animator.getAnimation(Animator.ATTACK_SHAKE));

				Animation hitAn = Animator.getAnimation(Animator.REDSCREEN);
				hitAn.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationEnd(Animation arg0) {
						redHitImage.setVisibility(View.INVISIBLE);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationStart(Animation animation) {
						redHitImage.setVisibility(View.VISIBLE);
					}

				});
				redHitImage.startAnimation(hitAn);
			}
		}

		endMonsterAttack();
	}

	private int checkForCounterShieldAbsorb(Actor actor, int hitAmount) {
		int absorbAmount = actor.getCounterAbsorbDamageAmount(hitAmount);
		if (absorbAmount > 0) {
			hitAmount = hitAmount - absorbAmount;

			if (hitAmount <= 0) {
				hitAmount = Math.abs(hitAmount);

				if (actor.actorType() == 0) {
					appendLog("You absorbed the attack.", DefinitionGlobal.LOG_TYPE_PLAYER_DODGES);
				} else {
					appendLog(actor.name() + " absorbed the attack.", DefinitionGlobal.LOG_TYPE_PLAYER_MISSES);
				}

				hitAmount = 0;
			} else {
				if (actor.actorType() == 0) {
					appendLog(actor.name() + " absorbed " + absorbAmount + " dmg.",
							DefinitionGlobal.LOG_TYPE_PLAYER_DODGES);
				} else {
					appendLog(actor.name() + " absorbed " + absorbAmount + " dmg.",
							DefinitionGlobal.LOG_TYPE_PLAYER_MISSES);
				}

			}
		}

		return hitAmount;
	}

	private int checkForShieldAbsorb(int hitAmount) {
		// check for class type that takes ap damage from hp damage
		if (hitAmount > 0 && player.currentAP() > 0) {
			if (DefinitionClassType.CLASS_TYPE_AP_REGEN[DefinitionClasses.CLASS_TYPE[player.playerClass()]][5] == 1) {
				String absText = "";
				int newApAmt = player.currentAP() - hitAmount;

				if (newApAmt < 0) {
					hitAmount = Math.abs(newApAmt);
					absText = "AP Shield absorbed " + (newApAmt + hitAmount) + " dmg.";
					player.setCurrentAP(0);
				} else {
					absText = "AP Shield absorbed " + hitAmount + " dmg.";
					player.updateAP(-hitAmount);
					hitAmount = 0;
				}
				appendLog(absText, DefinitionGlobal.LOG_TYPE_PLAYER_MISSES);
			}
		}
		return hitAmount;
	}

	private void dealReflectedDamage(Actor sourceOfReflectedDmg, Actor targetOfReflectedDmg, int dmg) {
		if (targetOfReflectedDmg.actorType() == 1) {
			appendLog("You reflected back " + dmg + " damage to " + targetOfReflectedDmg.name() + "!",
					DefinitionGlobal.LOG_TYPE_MONSTER_LOSES_HP);
			targetOfReflectedDmg.updateHP(-dmg);

		} else {
			appendLog(sourceOfReflectedDmg.name() + " reflected back " + dmg + " damage to you!",
					DefinitionGlobal.LOG_TYPE_PLAYER_GAINS_HP);
			targetOfReflectedDmg.updateHP(-dmg);
		}

	}

	private int[] dealMonsterHitDamage(int hitValue) {
		// dmg amt applied
		int[] returnData = new int[2];

		System.out.println("assignMonsterDamage (" + hitValue + ")");

		if (inCombat == false) {
			return null;
		}

		returnData[0] = hitValue;

		hitValue = checkForCounterShieldAbsorb(monster, hitValue);

		if (hitValue > 0) {
			monster.updateHP(-hitValue);

			int refDmg = monster.getReflectedDamage(hitValue);
			if (refDmg > 0)
				dealReflectedDamage(monster, player, refDmg);
		}

		return returnData;
	}

	private void appendLog(final String newLog, final int logType) {
		// TODO update this log

		String ls = System.getProperty("line.separator");

		Log.d("combat", "appendLog: " + newLog);

		logView.append(ls);
		logView.append(Helper.getSpanString(newLog, logType));

		logView.setMovementMethod(new ScrollingMovementMethod());
	}

	private void checkDodgeDelay(double d) {
		try {
			checkDodgeHandler.removeCallbacks(checkDodgePost);
			checkDodgeHandler.postDelayed(checkDodgePost, (long) (d * 1000));
		} catch (Exception e) {
			checkDodgePost.run();
		}
	}

	private Runnable checkDodgePost = new Runnable() {
		public void run() {
			if (waitingForDodge == false)
				return;

			else {
				dodgeButton.setEnabled(false);
				dodgeButton.setVisibility(View.INVISIBLE);
				try {
					if (dodgeWanderRunnable != null)
						dodgeWanderHandler.removeCallbacks(dodgeWanderRunnable);
				} catch (Exception ignored) {
				}
				waitingForDodge = false;

				dealPlayerHitDamage(new int[] { dodgeEvent.damage, dodgeEvent.critSuccess, dodgeEvent.stunSuccess });
				player.clearBlockingFlag();
			}
		}
	};

	private void animateAbility(final ItemRune ability, final int playerOrMonster) {
		attackImage.setImageResource(ability.animationImageResource());
		attackImage.setVisibility(View.VISIBLE);
		Animation a = Animator
				.getAnimation((Integer) DefinitionRunes.runeData[ability
						.id()][DefinitionRunes.RUNE_ANIMATION_IDS][playerOrMonster]);
		a.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation arg0) {
				if (playerOrMonster == 0) {
					animateMonster(1, Animator.SMALL_SHAKE);
					if (ability.dealWeaponDamageMin() > 0 || ability.dealMultipleStatBasedDamageMult() > 0
							|| ability.dealStatIsHpPercentDamageStatId() >= 0) {
						animateImpact();
					}

					endPlayerAttack();
				} else {

					endMonsterAttack();
				}

				attackImage.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationStart(Animation animation) {

			}

		});
		attackImage.startAnimation(a);

	}

	private void moveAttackImage(int imageResource, int attackTypeId) {
		attackImage.setImageResource(imageResource);
		attackImage.setVisibility(View.VISIBLE);

		Log.d("animator", "requesting animation for attackType " + attackTypeId);
		Animation a = Animator.getAnimation(DefinitionAttackTypes.ATTACK_TYPE_ANIMATION[attackTypeId]);
		a.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation arg0) {
				attackImage.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

		});
		attackImage.startAnimation(a);

	}

	private void exitArena() {
		player.setRank(1);
		player.setCurrentRound(1);
		player.setCurrentFight(1);

		try {
			if (DBHandler.isOpen(getApplicationContext())) {
				if (DBHandler.updatePlayer(player))
					DBHandler.close();
			} else {
				if (DBHandler.open(getApplicationContext())) {
					if (DBHandler.updatePlayer(player))
						DBHandler.close();
				}
			}
		} catch (Exception e) {
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			SoundManager.playSound(SoundManager.ALERTSOUND, true);

			final Dialog dialog = new Dialog(ControllerCombat.this);
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
			exitArenaButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					exitArena();
				}

			});

			donotexitArenaButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.cancel();
				}

			});
			dialog.show();

			/*
			 * 
			 * alertDialog = new AlertDialog.Builder(this);
			 * 
			 * alertDialog
			 * .setMessage(
			 * "Exiting the Arena will cause this character ("
			 * + player.name()
			 * + ", Rank "
			 * + player.rank()
			 * +
			 * ") to lose all progress and gold gained from this Round. They will revert to Round 1, Rank 1! What say you?"
			 * )
			 * .setCancelable(true).setPositiveButton("Exit Arena", new
			 * DialogInterface.OnClickListener()
			 * {
			 * public void onClick(DialogInterface dialog, int id)
			 * {
			 * player.setRank(1);
			 * player.setCurrentRound(1);
			 * player.setCurrentFight(1);
			 * 
			 * try
			 * {
			 * if (DBHandler.isOpen(getApplicationContext()))
			 * {
			 * if (DBHandler.updatePlayer(player))
			 * DBHandler.close();
			 * }
			 * else
			 * {
			 * if (DBHandler.open(getApplicationContext()))
			 * {
			 * if (DBHandler.updatePlayer(player))
			 * DBHandler.close();
			 * }
			 * }
			 * }
			 * catch (Exception e)
			 * {
			 * Log.d("combat",
			 * "failed to save updated player info to DB when exiting via back:" +
			 * e.toString());
			 * }
			 * 
			 * Intent resultIntent = new Intent();
			 * Bundle b = new Bundle();
			 * 
			 * // b.putSerializable("OwnedItems", OwnedItems);
			 * b.putSerializable("player", player);
			 * 
			 * resultIntent.putExtras(b);
			 * 
			 * setResult(Activity.RESULT_OK, resultIntent);
			 * 
			 * finish();
			 * 
			 * }
			 * }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
			 * {
			 * public void onClick(DialogInterface dialog, int id)
			 * {
			 * dialog.cancel();
			 * }
			 * });
			 * alertDialog.show();
			 */

		}
		return super.onKeyDown(keyCode, event);
	}

	AlertDialog.Builder alertDialog;

	private DodgeEvent dodgeEvent;
	private int randomDodgeID = -1;

	private Handler startMonsterAttackHandler = new Handler();
	private Handler checkDodgeHandler = new Handler();
	private Handler clearPlayerAttackTextHandler = new Handler();
	private Handler advanceTurnHandler = new Handler();
	private Handler explosionWaitHandler = new Handler();
	private Handler animateMonsterHandler = new Handler();
	private Handler dodgeWanderHandler = new Handler();
	private Runnable dodgeWanderRunnable;
	private boolean lowHpPulseActive = false;

	private class DodgeEvent {
		int dodgeID = 0;
		int damage = 0;
		int critSuccess = 0;
		int stunSuccess = 0;
	}

	TextView playerNameView;
	TextView playerHPView;
	TextView playerAPView;
	TextView monsterNameView;
	TextView monsterHealthView;
	TextView logView;
	TextView playerAttackText;
	TextView monsterAttackText;
	TextView playerAbilityText;

	TextView execText;
	TextView reacText;
	TextView knowText;
	TextView mageText;
	TextView luckText;

	TextView item1Text;
	TextView item2Text;

	ScrollView logScrollView;

	TableLayout playerEffectsTable;
	TableLayout monsterEffectsTable;

	ListView abilitiesList;
	ListView effectsList;
	ListView attackList;

	ImageButton hitButton;
	ImageButton abilityButton;
	ImageButton fleeButton;
	ImageButton item1Button;
	ImageButton item2Button;
	Button dodgeButton;

	ImageView monsterImage;
	ImageView attackImage;
	ImageView impactImage;
	VfxOverlayView vfxOverlay;
	VfxOverlayView ambientOverlay;

	ImageView playerEffect1Image;
	ImageView playerEffect2Image;
	ImageView playerEffect3Image;
	ImageView playerEffect4Image;
	ImageView playerEffect5Image;
	ImageView playerEffect6Image;
	ImageView playerEffect7Image;
	ImageView playerEffect8Image;
	ImageView playerEffect9Image;
	ImageView playerEffect10Image;
	ImageView playerEffect11Image;
	ImageView playerEffect12Image;
	ImageView playerEffect13Image;
	ImageView playerEffect14Image;
	ImageView monsterEffect1Image;
	ImageView monsterEffect2Image;
	ImageView monsterEffect3Image;
	ImageView monsterEffect4Image;
	ImageView monsterEffect5Image;
	ImageView monsterEffect6Image;
	ImageView monsterEffect7Image;
	ImageView monsterEffect8Image;
	ImageView monsterEffect9Image;
	ImageView monsterEffect10Image;
	ImageView monsterEffect11Image;
	ImageView monsterEffect12Image;
	ImageView monsterEffect13Image;
	ImageView monsterEffect14Image;
	ImageView[] monsterEffectImages = new ImageView[14];
	ImageView[] playerEffectImages = new ImageView[14];
	TextView effectsTitle;

	ImageView redHitImage;
	RelativeLayout combatLayout = null;
	LinearLayout playerBar = null;
	LinearLayout combatBottomLayout;
	LinearLayout monsterBar;
	LinearLayout expandedMonsterBar;
	TextView expandedMonsterName;
	TextView expandedMonsterHP;
	TextView expandedMonsterAP;
	TextView expandedMonsterAbilities;

	SimpleAdapter effectListAdapter = null;
	List<Map<String, Object>> effectAdapterData = new ArrayList<Map<String, Object>>();

	int lastPlayerHP = Integer.MIN_VALUE;
	int lastPlayerAP = Integer.MIN_VALUE;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, DefinitionGlobal.TUTORIAL, 0, "Tutorial");
		// item.setIcon(R.drawable.paint);

		menu.add(0, DefinitionGlobal.SETTINGS, 0, "Settings");

		menu.add(0, DefinitionGlobal.ABOUT, 0, "About");

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case DefinitionGlobal.TUTORIAL:
				showTutorial();
				return true;

			case DefinitionGlobal.SETTINGS:
				showSettings();
				return true;

			case DefinitionGlobal.ABOUT:
				showAbout();
				return true;
		}
		return false;
	}

	private void showTutorial() {
		Intent i = new Intent(getApplicationContext(), com.alderangaming.wizardsencounters.ControllerTutorial.class);

		startActivity(i);
	}

	private void showAbout() {
		aDialog.show();
	}

	private void setupAboutDialog() {
		AlertDialog.Builder aboutDialog = new AlertDialog.Builder(this);

		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.about, (ViewGroup) findViewById(R.id.layout_root));

		aboutDialog.setView(layout);

		aDialog = aboutDialog.create();
	}

	private void setupSettingsDialog() {
		LayoutInflater inflater = getLayoutInflater();
		final View settingsView = inflater.inflate(R.layout.settings, (ViewGroup) getCurrentFocus());
		final SeekBar seekMusic = (SeekBar) settingsView.findViewById(R.id.seekMusicVolume);
		final SeekBar seekSounds = (SeekBar) settingsView.findViewById(R.id.seekSoundVolume);
		toggleImpactButton = (ToggleButton) settingsView.findViewById(R.id.toggleImpactButton);
		nextSongButton = (Button) settingsView.findViewById(R.id.nextSongButton);
		final ToggleButton toggleMusicButton = (ToggleButton) settingsView.findViewById(R.id.toggleMusicButton);

		AlertDialog.Builder builder = new AlertDialog.Builder(ControllerCombat.this);
		builder.setMessage("Settings").setCancelable(false)
				.setPositiveButton("Done", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});

		builder.setView(settingsView);

		settingsDialog = builder.create();

		toggleMusicButton.setChecked(SoundManager.playingMusic());
		if (SoundManager.playingMusic()) {
			nextSongButton.setEnabled(true);
		}

		nextSongButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SoundManager.playNextCombatSong(ControllerCombat.this);
			}
		});

		seekMusic.setProgress((int) (SoundManager.getMusicVolume() * 100));
		seekSounds.setProgress((int) (SoundManager.getSoundVolume() * 100));

		seekMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				SoundManager.setMusicVolume(progress / 100f);
				DBHandler.updateSoundPrefs(ControllerCombat.this);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

		toggleMusicButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean enable = toggleMusicButton.isChecked();
				SoundManager.setPlayMusic(enable);
				nextSongButton.setEnabled(enable);
				DBHandler.updateSoundPrefs(ControllerCombat.this);
			}
		});

		seekSounds.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				SoundManager.setSoundVolume(progress / 100f);
				DBHandler.updateSoundPrefs(ControllerCombat.this);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

		toggleImpactButton.setChecked(SoundManager.showingImpact());
		toggleImpactButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (toggleImpactButton.isChecked()) {
					showImpact = 1;
					SoundManager.setShowImpact(true);
				}

				else {
					showImpact = 0;
					SoundManager.setShowImpact(false);
				}
				DBHandler.updateImpactPref(ControllerCombat.this);
			}
		});
	}

	private void showSettings() {
		settingsDialog.show();
	}

	private void showAbilitiesInfoDialog() {
		try {
			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(R.layout.abilities_dialog, null);

			LinearLayout monsterContainer = (LinearLayout) view.findViewById(R.id.monsterAbilitiesContainer);
			LinearLayout playerContainer = (LinearLayout) view.findViewById(R.id.playerAbilitiesContainer);

			int padding = (int) (getResources().getDisplayMetrics().density * 6f);
			// Monster: only show abilities that are known (used this combat)
			if (knownMonsterAbilities != null && knownMonsterAbilities.size() > 0) {
				for (Integer runeId : knownMonsterAbilities) {
					monsterContainer.addView(createAbilityRow(runeId, false, padding));
				}
			} else {
				TextView tv = new TextView(this);
				tv.setText("Unknown (no abilities observed yet)");
				tv.setTextColor(0xFFAAAAAA);
				tv.setPadding(0, padding, 0, padding);
				monsterContainer.addView(tv);
			}

			// Player: show all equipped abilities
			for (int i = 0; i < player.getActiveAbilities().length; i++) {
				int runeId = player.getActiveAbilityByIndex(i);
				playerContainer.addView(createAbilityRow(runeId, true, padding));
			}

			AlertDialog.Builder b = new AlertDialog.Builder(ControllerCombat.this);
			b.setView(view);
			b.setPositiveButton("Close", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			b.create().show();
		} catch (Exception ignored) {
		}
	}

	private View createAbilityRow(int runeId, boolean isPlayer, int paddingPx) {
		LinearLayout row = new LinearLayout(this);
		row.setOrientation(LinearLayout.HORIZONTAL);
		row.setPadding(0, paddingPx, 0, paddingPx);

		ImageView icon = new ImageView(this);
		try {
			String imgName = (String) DefinitionRunes.runeData[runeId][DefinitionRunes.RUNE_ANIMATION_IMAGE][0];
			int resId = getResources().getIdentifier(imgName, "drawable", getPackageName());
			if (resId != 0) {
				icon.setImageResource(resId);
			}
		} catch (Exception e) {
		}
		LinearLayout.LayoutParams iconLp = new LinearLayout.LayoutParams(
				(int) (getResources().getDisplayMetrics().density * 26f),
				(int) (getResources().getDisplayMetrics().density * 26f));
		iconLp.rightMargin = paddingPx;
		row.addView(icon, iconLp);

		LinearLayout textCol = new LinearLayout(this);
		textCol.setOrientation(LinearLayout.VERTICAL);

		TextView nameTv = new TextView(this);
		nameTv.setText((String) DefinitionRunes.runeData[runeId][DefinitionRunes.RUNE_NAMES][0]);
		nameTv.setTextColor(isPlayer ? 0xFF99CCFF : 0xFFFFDD88);
		nameTv.setTextSize(16f);
		nameTv.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);

		TextView descTv = new TextView(this);
		descTv.setText((String) DefinitionRunes.runeData[runeId][DefinitionRunes.RUNE_DESCRIPTION][0]);
		descTv.setTextColor(0xFFFFFFFF);
		descTv.setTextSize(14f);

		textCol.addView(nameTv);
		textCol.addView(descTv);

		row.addView(textCol, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));

		return row;
	}

	AlertDialog settingsDialog;
	AlertDialog aDialog;
	ToggleButton toggleImpactButton;
	Button nextSongButton;

}
