package com.alderangaming.wizardsencounters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
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

public class ControllerCombat extends Activity
{

	private Player player;
	// private OwnedItems OwnedItems;
	private Monster monster;

	/* Flags */
	private boolean showingAbilityList = false;
	private boolean showingAttackTypeList = false;
	private boolean showingEffectsList = false;
	private boolean inCombat = false;
	private boolean waitingForDodge = false;
	private boolean waitingForFind = false;
	private int randomFindID = -1;
	private int turnFlag = 0;
	private int backgroundImage = -1;
	private int showImpact = 1;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

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

		start();
	}

	private void setupViews()
	{
		combatLayout = (RelativeLayout) findViewById(R.id.combatImageLayout);
		combatLayout.setBackgroundResource(backgroundImage);
		playerBar = (LinearLayout) findViewById(R.id.combatPlayerBar);
		redHitImage = (ImageView) findViewById(R.id.combatHitImage);
		combatBottomLayout = (LinearLayout) findViewById(R.id.combatBottomLayout);
		combatBottomLayout.setVisibility(View.INVISIBLE);
		monsterBar = (LinearLayout) findViewById(R.id.monsterBar);
		expandedMonsterBar = (LinearLayout) findViewById(R.id.expandedMonsterBar);
		expandedMonsterName = (TextView) findViewById(R.id.expandedMonsterName);
		expandedMonsterHP = (TextView) findViewById(R.id.expandedMonsterHP);
		expandedMonsterAP = (TextView) findViewById(R.id.expandedMonsterAP);
		expandedMonsterAbilities = (TextView) findViewById(R.id.expandedMonsterAbilities);

		execText = (TextView) findViewById(R.id.combatExecText);
		reacText = (TextView) findViewById(R.id.combatReactionText);
		knowText = (TextView) findViewById(R.id.combatKnowledgeText);
		mageText = (TextView) findViewById(R.id.combatMageloreText);
		luckText = (TextView) findViewById(R.id.combatLuckText);

		LayoutInflater inflater = getLayoutInflater();
		final View playerInfoView = inflater.inflate(R.layout.actorinfo, (ViewGroup) findViewById(R.id.playerInfoRoot));
		final TextView playerInfoName = (TextView) playerInfoView.findViewById(R.id.playerInfoName);
		playerInfoName.setText(player.name());

		final TextView playerInfoWeaponName = (TextView) playerInfoView.findViewById(R.id.playerInfoWeaponName);
		final TextView playerInfoWeaponInfo = (TextView) playerInfoView.findViewById(R.id.playerInfoWeaponInfo);
		if (player.equippedWeapon() >= 0)
		{
			playerInfoWeaponName.setText(DefinitionWeapons.WEAPON_NAMES[player.equippedWeapon()]);
			playerInfoWeaponInfo.setText(DefinitionWeapons.WEAPON_DESCRIPTIONS[player.equippedWeapon()]);
		}
		else
		{
			playerInfoWeaponName.setText("No Weapon Equipped");
			playerInfoWeaponInfo.setText("");
		}

		final TextView playerInfoHelmName = (TextView) playerInfoView.findViewById(R.id.playerInfoHelmName);
		final TextView playerInfoHelmInfo = (TextView) playerInfoView.findViewById(R.id.playerInfoHelmInfo);
		if (player.equippedArmorSlot1() >= 0)
		{
			playerInfoHelmName.setText(DefinitionArmor.ARMOR_NAMES[player.equippedArmorSlot1()]);
			playerInfoHelmInfo.setText(DefinitionArmor.ARMOR_DESCRIPTIONS[player.equippedArmorSlot1()]);
		}
		else
		{
			playerInfoHelmName.setText("No Helm Armor Equipped");
			playerInfoHelmInfo.setText("");
		}

		final TextView playerInfoChestName = (TextView) playerInfoView.findViewById(R.id.playerInfoChestName);
		final TextView playerInfoChestInfo = (TextView) playerInfoView.findViewById(R.id.playerInfoChestInfo);
		if (player.equippedArmorSlot2() >= 0)
		{
			playerInfoChestName.setText(DefinitionArmor.ARMOR_NAMES[player.equippedArmorSlot2()]);
			playerInfoChestInfo.setText(DefinitionArmor.ARMOR_DESCRIPTIONS[player.equippedArmorSlot2()]);
		}
		else
		{
			playerInfoChestName.setText("No Chest Armor Equipped");
			playerInfoChestInfo.setText("");
		}

		final TextView playerInfoShoesName = (TextView) playerInfoView.findViewById(R.id.playerInfoShoesName);
		final TextView playerInfoShoesInfo = (TextView) playerInfoView.findViewById(R.id.playerInfoShoesInfo);
		if (player.equippedArmorSlot3() >= 0)
		{
			playerInfoShoesName.setText(DefinitionArmor.ARMOR_NAMES[player.equippedArmorSlot3()]);
			playerInfoShoesInfo.setText(DefinitionArmor.ARMOR_DESCRIPTIONS[player.equippedArmorSlot3()]);
		}
		else
		{
			playerInfoShoesName.setText("No Shoes Equipped");
			playerInfoShoesInfo.setText("");
		}

		final TextView playerInfoTrinketName = (TextView) playerInfoView.findViewById(R.id.playerInfoTrinketName);
		final TextView playerInfoTrinketInfo = (TextView) playerInfoView.findViewById(R.id.playerInfoTrinketInfo);
		if (player.equippedArmorSlot4() >= 0)
		{
			playerInfoTrinketName.setText(DefinitionArmor.ARMOR_NAMES[player.equippedArmorSlot4()]);
			playerInfoTrinketInfo.setText(DefinitionArmor.ARMOR_DESCRIPTIONS[player.equippedArmorSlot4()]);
		}
		else
		{
			playerInfoTrinketName.setText("No Trinket Equipped");
			playerInfoTrinketInfo.setText("");
		}

		final TextView playerInfoRank = (TextView) playerInfoView.findViewById(R.id.playerInfoRank);
		playerInfoRank.setText(player.rank() + "");

		AlertDialog.Builder builder = new AlertDialog.Builder(ControllerCombat.this);
		builder.setCancelable(false).setPositiveButton("Done", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.dismiss();
			}
		});

		builder.setView(playerInfoView);

		final AlertDialog dialog = builder.create();

		playerBar.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// dialog.show();
			}

		});

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

		item1Text = (TextView) findViewById(R.id.combatItem1Text);
		item1Button = (ImageButton) findViewById(R.id.combatItem1Button);
		item1Button.setEnabled(false);

		item2Text = (TextView) findViewById(R.id.combatItem2Text);
		item2Button = (ImageButton) findViewById(R.id.combatItem2Button);
		item2Button.setEnabled(false);

		dodgeButton = (Button) findViewById(R.id.dodgeButton);
		dodgeButton.setVisibility(View.INVISIBLE);

		findObjectButton = (Button) findViewById(R.id.findObjectButton);
		findObjectButton.setVisibility(View.INVISIBLE);

		monsterImage = (ImageView) findViewById(R.id.combatMonsterImage);
		monsterImage.setImageResource(monster.imageResource());

		attackImage = (ImageView) findViewById(R.id.combatAttackImage);
		attackImage.setImageResource(0);
		attackImage.setVisibility(View.INVISIBLE);

		setupTouchHandlers();
	}

	private void setupTouchHandlers()
	{
		playerEffectsTable.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
					{
						if (!showingEffectsList)
						{
							showingEffectsList = true;
							if (effectListAdapter == null)
							{
								showEffectsList(0);
							}
							else
							{
								updateEffectsList(0);
							}

							effectsList.setVisibility(View.VISIBLE);
						}
					}
						break;
					case MotionEvent.ACTION_UP:
					{
						showingEffectsList = false;
						effectsList.setVisibility(View.INVISIBLE);
					}
						break;
				}
				return true;
			}
		});

		monsterEffectsTable.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
					{
						if (!showingEffectsList)
						{
							showingEffectsList = true;
							if (effectListAdapter == null)
							{
								showEffectsList(1);
							}
							else
							{
								updateEffectsList(1);
							}

							effectsList.setVisibility(View.VISIBLE);
						}
					}
						break;
					case MotionEvent.ACTION_UP:
					{
						showingEffectsList = false;
						effectsList.setVisibility(View.INVISIBLE);
					}
						break;
				}
				return true;
			}
		});

		dodgeButton.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (!dodgeButton.isEnabled())
					return true;

				switch (event.getAction())
				{
					case MotionEvent.ACTION_UP:
						dodgeEvent.dodged = 1;
						checkDodge();
						break;
				}
				return true;
			}
		});

		findObjectButton.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (!findObjectButton.isEnabled())
					return true;

				switch (event.getAction())
				{
					case MotionEvent.ACTION_UP:
						checkFindObject(1, randomFindID);
						break;
				}
				return true;
			}
		});

		hitButton.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (!hitButton.isEnabled())
					return true;

				switch (event.getAction())
				{

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

		abilityButton.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (!abilityButton.isEnabled())
					return true;

				switch (event.getAction())
				{
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

		fleeButton.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (!fleeButton.isEnabled())
					return true;

				switch (event.getAction())
				{
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

		item1Button.setOnTouchListener(new View.OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (!item1Button.isEnabled())
					return true;

				switch (event.getAction())
				{
					case MotionEvent.ACTION_UP:
						item1Button.setBackgroundResource(R.drawable.itembuttonup);
						handleUseItemButton(1);
						break;

					case MotionEvent.ACTION_DOWN:
						item1Button.setBackgroundResource(R.drawable.itembuttondown);
						break;
				}
				return true;
			}
		});

		item2Button.setOnTouchListener(new View.OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (!item2Button.isEnabled())
					return true;

				switch (event.getAction())
				{
					case MotionEvent.ACTION_UP:
						item2Button.setBackgroundResource(R.drawable.itembuttonup);
						handleUseItemButton(2);
						break;

					case MotionEvent.ACTION_DOWN:
						item2Button.setBackgroundResource(R.drawable.itembuttondown);
						break;
				}
				return true;
			}
		});
	}

	private void handleUseItemButton(int whichItem)
	{
		if (whichItem == 1)
		{
			if (OwnedItems.getChargesOfItemId(player.equippedItemSlot1())[0] <= 0)
				return;

			OwnedItems.useItemCharge(player.equippedItemSlot1());

			if (DBHandler.updateOwnedItems(OwnedItems.getOwnedItems()))
				useItem(player.equippedItemSlot1());

		}
		else if (whichItem == 2)
		{
			if (OwnedItems.getChargesOfItemId(player.equippedItemSlot2())[0] <= 0)
				return;

			OwnedItems.useItemCharge(player.equippedItemSlot2());

			if (DBHandler.updateOwnedItems(OwnedItems.getOwnedItems()))
				useItem(player.equippedItemSlot2());
		}

		updateItemButtons();

		DBHandler.updateOwnedItems(OwnedItems.getOwnedItems());
	}

	private void handleFleeButton()
	{

		if (player.playerAttackWait() == false)
		{
			player.setPlayerAttackWait(true);
			updateButtons();
		}
		else
			return;

		int runroll = Helper.randomInt(101);
		int runchance = player.getRunChance();

		if (runchance < runroll)
		{
			appendLog("You failed to run away!");
			endPlayerAttack();
		}
		else
		{
			showRunAwayDialog();
		}
	}

	private void flee()
	{
		appendLog("You ran away!");
		monster.setCurrentHP(0);
		monster.setDead(true);
		player.updateHP((int) Math.round((double) player.maxHP() / 2.0));
		inCombat = false;

		clearAll();
	}

	private void handleAbilityButton()
	{
		if (player.playerAttackWait() == false || showingAbilityList)
		{
			if (!showingAbilityList)
			{
				player.setPlayerAttackWait(true);
				showingAbilityList = true;
				abilitiesList.setVisibility(View.VISIBLE);
			}
			else
			{
				player.setPlayerAttackWait(false);
				showingAbilityList = false;
				abilitiesList.setVisibility(View.INVISIBLE);
			}

			updateButtons();
		}
	}

	private void handleHitButton()
	{
		hitButton.setImageResource(R.drawable.buttonhitup);

		if (player.playerAttackWait() == false || showingAttackTypeList)
		{
			if (!showingAttackTypeList)
			{
				player.setPlayerAttackWait(true);
				showingAttackTypeList = true;
				attackList.setVisibility(View.VISIBLE);
			}
			else
			{
				player.setPlayerAttackWait(false);
				showingAttackTypeList = false;
				attackList.setVisibility(View.INVISIBLE);
			}

			updateButtons();
		}
	}

	private void updateStatText()
	{
		execText.setText("EXEC:" + player.strength());
		if (player.execDiff() < 0)
		{
			execText.setTextColor(Color.RED);
		}
		else if (player.execDiff() > 0)
		{
			execText.setTextColor(Color.GREEN);
		}
		else
		{
			execText.setTextColor(Color.WHITE);
		}

		reacText.setText("REAC:" + player.reaction());
		if (player.reacDiff() < 0)
		{
			reacText.setTextColor(Color.RED);
		}
		else if (player.reacDiff() > 0)
		{
			reacText.setTextColor(Color.GREEN);
		}
		else
		{
			reacText.setTextColor(Color.WHITE);
		}

		knowText.setText("KNOW:" + player.knowledge());
		if (player.knowDiff() < 0)
		{
			knowText.setTextColor(Color.RED);
		}
		else if (player.knowDiff() > 0)
		{
			knowText.setTextColor(Color.GREEN);
		}
		else
		{
			knowText.setTextColor(Color.WHITE);
		}

		mageText.setText("MAGE:" + player.magelore());
		if (player.mageDiff() < 0)
		{
			mageText.setTextColor(Color.RED);
		}
		else if (player.mageDiff() > 0)
		{
			mageText.setTextColor(Color.GREEN);
		}
		else
		{
			mageText.setTextColor(Color.WHITE);
		}

		luckText.setText("LUCK:" + player.luck());
		if (player.luckDiff() < 0)
		{
			luckText.setTextColor(Color.RED);
		}
		else if (player.luckDiff() > 0)
		{
			luckText.setTextColor(Color.GREEN);
		}
		else
		{
			luckText.setTextColor(Color.WHITE);
		}
	}

	private void updateViews()
	{
		playerNameView.setText(player.name());
		playerHPView.setText(player.currentHP() + "/" + player.maxHP());
		playerAPView.setText(player.currentAP() + "/" + player.maxAP());

		if (expandedMonsterBar.getVisibility() == View.VISIBLE)
		{
			expandedMonsterName.setText(monster.name());
			expandedMonsterHP.setText("HP:" + monster.currentHP() + "/" + monster.maxHP());
			expandedMonsterAP.setText("AP:" + monster.currentAP() + "/" + monster.maxAP());
			String monsterAbils = "";
			for (int a = 0; a < monster.getActiveAbilities().length; a++)
			{
				if (a != 0)
					monsterAbils += ", ";

				monsterAbils +=
					DefinitionRunes.runeData[monster.getActiveAbilityByIndex(a)][DefinitionRunes.RUNE_NAMES][0];
			}
			expandedMonsterAbilities.setText(monsterAbils);
		}
		else
		{
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

	private void disableCombatButtons()
	{
		hitButton.setEnabled(false);
		hitButton.setImageResource(R.drawable.buttonhitdisabled);

		abilityButton.setEnabled(false);
		abilityButton.setImageResource(R.drawable.buttonabilitydisabled);

		fleeButton.setEnabled(false);
		fleeButton.setImageResource(R.drawable.buttonfleedisabled);

		disableItemButtons();
	}

	private void disableItemButtons()
	{
		item1Button.setEnabled(false);
		item2Button.setEnabled(false);
	}

	private void updateItemButtons()
	{
		if (player.equippedItemSlot1() >= 0)
		{
			item1Button.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_ITEM,
				player.equippedItemSlot1()));

			item1Text.setText(OwnedItems.getChargesOfItemId(player.equippedItemSlot1())[0] + "/"
				+ OwnedItems.getChargesOfItemId(player.equippedItemSlot1())[1]);

			if (OwnedItems.getChargesOfItemId(player.equippedItemSlot1())[0] > 0)
			{
				item1Button.setBackgroundResource(R.drawable.itembuttonup);
				item1Button.setEnabled(true);

			}
			else
			{
				item1Button.setBackgroundResource(R.drawable.nochargesbutton);
				item1Button.setEnabled(false);
			}
		}
		else
		{
			item1Text.setText("0/0");
			item1Button.setBackgroundResource(R.drawable.noitembutton);
			item1Button.setImageResource(0);
			item1Button.setEnabled(false);
		}

		if (player.equippedItemSlot2() >= 0)
		{
			item2Button.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_ITEM,
				player.equippedItemSlot2()));

			item2Text.setText(OwnedItems.getChargesOfItemId(player.equippedItemSlot2())[0] + "/"
				+ OwnedItems.getChargesOfItemId(player.equippedItemSlot2())[1]);

			if (OwnedItems.getChargesOfItemId(player.equippedItemSlot2())[0] > 0)
			{
				item2Button.setBackgroundResource(R.drawable.itembuttonup);
				item2Button.setEnabled(true);

			}
			else
			{
				item2Button.setBackgroundResource(R.drawable.nochargesbutton);
				item2Button.setEnabled(false);
			}
		}
		else
		{
			item2Text.setText("0/0");
			item2Button.setBackgroundResource(R.drawable.noitembutton);
			item2Button.setImageResource(0);
			item2Button.setEnabled(false);
		}
	}

	private void updateButtons()
	{
		if (inCombat)
		{

			updateItemButtons();

			if (player.playerAttackWait())
			{
				hitButton.setEnabled(false);
				abilityButton.setEnabled(false);
				fleeButton.setEnabled(false);

				if (!showingAbilityList)
				{
					abilityButton.setEnabled(false);
					abilityButton.setImageResource(R.drawable.buttonabilitydisabled);
				}
				else
				{
					abilityButton.setEnabled(true);
					abilityButton.setImageResource(R.drawable.buttonabilitydown);
				}
				if (!showingAttackTypeList)
				{
					hitButton.setEnabled(false);
					hitButton.setImageResource(R.drawable.buttonhitdisabled);
				}
				else
				{
					hitButton.setEnabled(true);
					hitButton.setImageResource(R.drawable.buttonhitdown);
				}

				fleeButton.setImageResource(R.drawable.buttonfleedisabled);

			}
			else
			{
				hitButton.setEnabled(true);
				abilityButton.setEnabled(true);
				fleeButton.setEnabled(true);

				hitButton.setImageResource(R.drawable.buttonhitup);
				abilityButton.setImageResource(R.drawable.buttonabilityup);
				fleeButton.setImageResource(R.drawable.buttonfleeup);

			}
		}
		else if (player.dead())
		{
			player.setRank(1);
			player.setCurrentFight(0);
			player.setCurrentRound(1);
			DBHandler.updatePlayer(player);
			showPlayerDiedDialog();
		}
	}

	private void showPlayerDiedDialog()
	{
		AlertDialog.Builder playerDiedDialog = new AlertDialog.Builder(this);

		playerDiedDialog
			.setTitle("You have died!")
			.setMessage(
				"All progress and items earned by this player have been consumed by the " + monster.name() + ".")
			.setPositiveButton("End", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					dialog.cancel();
					killPlayer();
				}
			});

		AlertDialog alert = playerDiedDialog.create();
		alert.show();
	}

	private void killPlayer()
	{
		if (DBHandler.killPlayer(player.playerID()))
		{
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

	private void updateAbilityListAdapter()
	{
		String[] abNames = new String[player.getActiveAbilities().length];
		String[] abCost = new String[player.getActiveAbilities().length];
		String[] abDescriptions = new String[player.getActiveAbilities().length];

		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < abNames.length; i++)
		{
			abNames[i] =
				(String) DefinitionRunes.runeData[player.getActiveAbilityByIndex(i)][DefinitionRunes.RUNE_NAMES][0];

			if (player.hasFreeAbility())
			{
				abCost[i] = "0";
			}
			else
				abCost[i] =
					""
						+ (Integer) DefinitionRunes.runeData[player.getActiveAbilityByIndex(i)][DefinitionRunes.RUNE_AP_COST][0];

			abDescriptions[i] =
				(String) DefinitionRunes.runeData[player.getActiveAbilityByIndex(i)][DefinitionRunes.RUNE_DESCRIPTION][0];

			rows.add(Helper.createMap(abNames[i], abCost[i], abDescriptions[i]));
		}

		String[] fromKeys = new String[]
		{ "Name", "APCost", "Description" };
		int[] toIds = new int[]
		{ R.id.abilityListName, R.id.abilityListCost, R.id.abilityListDescription };

		abilitiesList.setAdapter(new SimpleAdapter(this, rows, R.layout.abilitylistitem, fromKeys, toIds));

		abilitiesList.invalidate();
	}

	private void setupAbilityListAdapter()
	{

		TextView t = new TextView(this);
		t.setText("Abilities");
		abilitiesList.addHeaderView(t);

		String[] abNames = new String[player.getActiveAbilities().length];
		String[] abCost = new String[player.getActiveAbilities().length];
		String[] abDescriptions = new String[player.getActiveAbilities().length];

		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < abNames.length; i++)
		{
			abNames[i] =
				(String) DefinitionRunes.runeData[player.getActiveAbilityByIndex(i)][DefinitionRunes.RUNE_NAMES][0];

			if (player.hasFreeAbility())
			{
				abCost[i] = "0";
			}
			else
				abCost[i] =
					""
						+ (Integer) DefinitionRunes.runeData[player.getActiveAbilityByIndex(i)][DefinitionRunes.RUNE_AP_COST][0];

			abDescriptions[i] =
				(String) DefinitionRunes.runeData[player.getActiveAbilityByIndex(i)][DefinitionRunes.RUNE_DESCRIPTION][0];

			rows.add(Helper.createMap(abNames[i], abCost[i], abDescriptions[i]));
		}

		String[] fromKeys = new String[]
		{ "Name", "APCost", "Description" };
		int[] toIds = new int[]
		{ R.id.abilityListName, R.id.abilityListCost, R.id.abilityListDescription };

		abilitiesList.setAdapter(new SimpleAdapter(this, rows, R.layout.abilitylistitem, fromKeys, toIds));

		abilitiesList.invalidate();

	}

	private void updateEffectsList(int who)
	{
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

		if (who == 0)
		{
			for (int i = 0; i < efNames.length; i++)
			{
				efNames[i] = player.getActiveEffectByIndex(i).name();
				efTurns[i] = "Turns: " + player.getActiveEffectByIndex(i).turnsRemaining();
				efDescriptions[i] = player.getActiveEffectByIndex(i).description();
				efImages[i] = player.getActiveEffectByIndex(i).imageResource();

				effectAdapterData.add(Helper.createEffectMap(efNames[i], efTurns[i], efDescriptions[i], efImages[i]));
			}
		}
		else
		{
			for (int i = 0; i < efNames.length; i++)
			{
				efNames[i] = monster.getActiveEffectByIndex(i).name();
				efTurns[i] = "Turns: " + monster.getActiveEffectByIndex(i).turnsRemaining();
				efDescriptions[i] = monster.getActiveEffectByIndex(i).description();
				efImages[i] = monster.getActiveEffectByIndex(i).imageResource();

				effectAdapterData.add(Helper.createEffectMap(efNames[i], efTurns[i], efDescriptions[i], efImages[i]));
			}
		}

		if (effectListAdapter != null)
		{
			effectListAdapter.notifyDataSetChanged();
			effectsList.invalidate();
		}
	}

	private void showEffectsList(int who)
	{

		String[] fromKeys = new String[]
		{ "effectName", "effectDescription", "effectTurns", "effectImage" };

		int[] toIds = new int[]
		{ R.id.effectListName, R.id.effectListDescription, R.id.effectListTurns, R.id.effectListImage };

		updateEffectsList(who);

		effectListAdapter = new SimpleAdapter(this, effectAdapterData, R.layout.effectlistitem, fromKeys, toIds);
		effectsList.setAdapter(effectListAdapter);

		updateEffectsList(who);
	}

	private void setupAttackTypeListAdapter()
	{
		TextView t = new TextView(this);
		t.setText(DefinitionWeapons.WEAPON_NAMES[player.equippedWeapon()]);
		t.setBackgroundColor(Color.BLACK);
		attackList.addHeaderView(t);

		String[] atNames = new String[DefinitionWeapons.WEAPON_ATTACK_TYPES[player.equippedWeapon()].length];
		String[] atStat1 = new String[DefinitionWeapons.WEAPON_ATTACK_TYPES[player.equippedWeapon()].length];
		String[] atStat2 = new String[DefinitionWeapons.WEAPON_ATTACK_TYPES[player.equippedWeapon()].length];

		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < atNames.length; i++)
		{
			atNames[i] =
				((ItemWeapon) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_WEAPON, player.equippedWeapon()))
					.getAttackTypeByIndex(i).name;
			atStat1[i] =
				"+"
					+ ((ItemWeapon) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_WEAPON,
						player.equippedWeapon())).getAttackTypeByIndex(i).hitChance + " % hit";
			atStat2[i] =
				"+"
					+ ((ItemWeapon) OwnedItems.getItemByTypeId(DefinitionGlobal.ITEM_TYPE_WEAPON,
						player.equippedWeapon())).getAttackTypeByIndex(i).critChance + " % crit";

			rows.add(Helper.createAttackTypeMap(atNames[i], atStat1[i], atStat2[i]));
		}

		String[] fromKeys = new String[]
		{ "Name", "Stat1", "Stat2" };
		int[] toIds = new int[]
		{ R.id.attackListName, R.id.attackListStat1, R.id.attackListStat2 };

		attackList.setAdapter(new SimpleAdapter(this, rows, R.layout.attacklistitem, fromKeys, toIds));

		attackList.invalidate();
	}

	private void implementAttackTypeAdapter()
	{
		attackList.setVisibility(View.INVISIBLE);
		attackList.invalidate();
		attackList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> a, View v, int index, long l)
			{
				if (index == 0)
					return;

				index = index - 1;

				Log.d("combat", "clicked on attacktype index " + (index));

				int attackTypeIndex = DefinitionWeapons.WEAPON_ATTACK_TYPES[player.equippedWeapon()][index];

				Log.d("combat", "clicked on attacktypeId is " + attackTypeIndex);

				attackList.setVisibility(View.INVISIBLE);

				showingAttackTypeList = false;

				playerHitMonster(index, attackTypeIndex);
			}
		});
	}

	private void implementAbilityAdapter()
	{

		abilitiesList.setVisibility(View.INVISIBLE);

		abilitiesList.invalidate();

		abilitiesList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> a, View v, final int index, long l)
			{
				if (index == 0)
					return;

				abilitiesList.setVisibility(View.INVISIBLE);

				int newIndex = index - 1;

				showingAbilityList = false;

				if (!player.hasFreeAbility()
					&& (Integer) DefinitionRunes.runeData[player.getActiveAbilityByIndex(newIndex)][DefinitionRunes.RUNE_AP_COST][0] > player
						.currentAP())
				{
					appendLog("Insufficient AP to use this.");
					player.setPlayerAttackWait(false);
					updateButtons();
					return;
				}

				if (player.cannotUseAbilities())
				{
					appendLog("You are too confused to do this!");
					player.setPlayerAttackWait(false);
					updateButtons();
					return;
				}

				// DETERMINE IF ABILITY IS SUCCESSFUL
				int randomnumber = Helper.randomInt(101);

				// APPLY PLAYER CURSED EFFECT
				if (randomnumber == 100 && player.isEffectActive(Helper.getEffectIdByName("Cursed")) == false)
				{
					player.addActiveEffect(Helper.getEffectIdByName("Cursed"));
				}

				randomnumber -= player.knowledge();

				if (randomnumber > (Integer) DefinitionRunes.runeData[player.getActiveAbilityByIndex(newIndex)][DefinitionRunes.RUNE_SUCCESS_CHANCE][0])
				{
					appendLog((String) DefinitionRunes.runeData[player.getActiveAbilityByIndex(newIndex)][DefinitionRunes.RUNE_NAMES][0]
						+ " failed!");

					player.updateAP(-(Integer) DefinitionRunes.runeData[player.getActiveAbilityByIndex(newIndex)][DefinitionRunes.RUNE_AP_COST][0]);

					appendLog("(rolled "
						+ (100 - randomnumber)
						+ ", needed > "
						+ (100 - (Integer) DefinitionRunes.runeData[player.getActiveAbilityByIndex(newIndex)][DefinitionRunes.RUNE_SUCCESS_CHANCE][0])
						+ ")");
					playerAttackText.setText("FAILED!");
					playerAttackText.setTextSize(40);
					playerAttackText.setTextColor(0xFFFF0000);

					clearPlayerAttackTextDelay(1.8);
					endPlayerAttack();
				}
				else
					useAbility(player.getActiveAbilityByIndex(newIndex), randomnumber);
			}
		});
	}

	private void start()
	{
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
		if (randomnumber2 < 1)
		{
			appendLog("You were totally caught off guard!");
			player.addActiveEffect(Helper.getEffectIdByName("Cursed"));
		}

		if (playerScore >= monsterScore)
		{
			// player goes first
			Log.d("combat", "player goes first");
			player.setPlayerAttackWait(false);

			appendLog("You found a " + monster.name() + ".");
			updateViews();
		}
		else
		{
			Log.d("combat", "monster goes first");
			player.setPlayerAttackWait(true);
			updateViews();

			// wait some time then have the monster attack
			startMonsterAttackDelay(1);
		}
	}

	private void checkDodge()
	{
		// new DodgeEvent created before this was called
		dodgeButton.setEnabled(false);
		dodgeButton.setVisibility(View.INVISIBLE);

		if (waitingForDodge == true && randomDodgeID == dodgeEvent.dodgeID)
		{
			SoundManager.playSound(SoundManager.SOUND_TYPE_DODGE);
			waitingForDodge = false;
			appendLog("You dodged the attack!");
		}
		else
			return;

		updateViews();
	}

	private void checkFindObject(int foundObject, int findID)
	{
		if (waitingForFind == true && randomFindID == findID)
		{
			waitingForFind = false;
		}
		else
		{
			return;
		}

		findObjectButton.setEnabled(false);
		findObjectButton.setVisibility(View.INVISIBLE);

		String[] gems =
			{ "Glassy Bead", "Shiny Sprocket", "Glazed Bowl", "Book: 'Origins of Lances'",
				"Book: 'Innovative Conjurations'", "Book: 'A Kraken's Mating Habits'", "Book: 'Novel Sorceries'",
				"Book: 'Bane of the Knight'", "Book: 'Prophecy Crown: Vol 1'", "Book: 'The Folly of Spellcasting'",
				"Book: 'The Maiden and the Mage'", "Book: 'Friendly Dragons'", "Book: 'The Legend of the Purple Gar'",
				"Book: 'The Apprentice Garblin'", "Stone Key", "Sapphire Amulet", "Ruby Disc", "Skull Goblet",
				"Marble Vase", "Book: 'The Downfall of the Tollans'", "Book: 'Creating Your Own Prometheus'",
				"Book: 'The Lore of The Data'", "vial of rare snake repellent", "humming light rod",
				"container of corn seeds", "map of the stars", "Book: 'Dragons Were Dinosaurs'",
				"Book: 'How To Replicate Anything'", "Book: 'You Are More Than Your Level!'",
				"Book: 'Beowulf Was My Friend'", "Book: 'Cosmos A-to-Z'", "Book: 'The Teachings of Surak'",
				"Book: 'How To Use Your DHD'", "Book: 'The Dialect of Walking Carpets'", "Book: 'The Book of Origin'",
				"Book: 'Make Your R2 Fly!'", "Book: 'Tape From Ducks'", "Book: 'Mind Control for Mages'",
				"Bag of Kanga Spice", "Jade Crown", "Griffin Farthing", "Ancient Shekel", "Sage Relic" };

		int randomgem = Helper.randomInt(gems.length);
		int randomgold = randomgem + Helper.randomInt(player.rank() * 10);

		if (randomgold < 1)
			randomgold = 1;

		randomgold *= 3;

		if (randomgem == 0)
		{
			randomgold = 10 * player.knowledge() * player.rank();
			appendLog("You found an ancient Artifact worth " + randomgold + " gold!");

		}
		else
		{
			if (gems[randomgem].equals("Ancient Shekel"))
			{
				appendLog("You found an " + gems[randomgem] + " worth " + randomgold + " gold!");
			}
			else
			{
				appendLog("You found a " + gems[randomgem] + " worth " + randomgold + " gold!");
			}
		}

		OwnedItems.updateGold(randomgold);

		updateViews();
	}

	private void startMonsterAttackDelay(double d)
	{
		try
		{
			startMonsterAttackHandler.removeCallbacks(startMonsterAttackPost);
			startMonsterAttackHandler.postDelayed(startMonsterAttackPost, (long) (d * 1000));
		}
		catch (Exception e)
		{
			appendLog("thread did not sleep: " + e.getMessage());
			startMonsterAttackPost.run();
		}
	}

	private Runnable startMonsterAttackPost = new Runnable()
	{
		public void run()
		{
			startMonsterAttack();
		}
	};

	private void useItem(int itemId)
	{
		Log.d("UseItem", "used item " + DefinitionItems.ITEM_NAME[itemId]);

		if (DefinitionItems.ITEM_SOUND_CLIP[itemId] != 0)
			SoundManager.playSound(DefinitionItems.ITEM_SOUND_CLIP[itemId]);

		appendLog("Used Item: " + DefinitionItems.ITEM_NAME[itemId] + ".");

		// regain health % of max
		if (DefinitionItems.ITEM_REGAIN_HEALTH_PERCENT_OF_MAX[itemId] >= 0)
		{
			int gainAmt =
				Helper.getPercentFromInt(DefinitionItems.ITEM_REGAIN_HEALTH_PERCENT_OF_MAX[itemId], player.maxHP());

			int actualAmt = player.updateHP(gainAmt);
			appendLog("You gained " + actualAmt + " HP.");
		}

		// skip fight
		if (DefinitionItems.ITEM_SKIPS_FIGHT_FLAG[itemId] == 1)
		{
			showFightEndDialog();
		}

		// modify crit for absolute turns, or for level turns if flag
		if (DefinitionItems.ITEM_MODIFY_CRIT_PERCENT[itemId] >= 0)
		{
			int turns = 0;
			if (DefinitionItems.ITEM_APPLIED_OVER_NUM_TURNS[itemId][1] == 1)
				turns = player.rank();
			else
				turns = DefinitionItems.ITEM_APPLIED_OVER_NUM_TURNS[itemId][0];

			player.itemModifyCrit(DefinitionItems.ITEM_MODIFY_CRIT_PERCENT[itemId], turns);

			appendLog("Chance to crit is " + player.critChance() + "%. Bonus will be applied for " + turns + " turns.");
		}

		// modify damage taken decrease%
		if (DefinitionItems.ITEM_MODIFY_DAMAGE_TAKEN_DECREASE_PERCENT[itemId] != 0)
		{
			int turns = 0;
			if (DefinitionItems.ITEM_APPLIED_OVER_NUM_TURNS[itemId][1] == 1)
				turns = player.rank();
			else
				turns = DefinitionItems.ITEM_APPLIED_OVER_NUM_TURNS[itemId][0];

			player.itemModifyDamageTakenDecrease(DefinitionItems.ITEM_MODIFY_DAMAGE_TAKEN_DECREASE_PERCENT[itemId],
				turns);

			appendLog("Damage taken will be decreased by "
				+ DefinitionItems.ITEM_MODIFY_DAMAGE_TAKEN_DECREASE_PERCENT[itemId] + "% for " + turns + " turns.");
		}

		// modify dodge chance
		if (DefinitionItems.ITEM_MODIFY_DODGE_PERCENT[itemId] == 1)
		{
			int turns = 0;
			if (DefinitionItems.ITEM_APPLIED_OVER_NUM_TURNS[itemId][1] == 1)
				turns = player.rank();
			else
				turns = DefinitionItems.ITEM_APPLIED_OVER_NUM_TURNS[itemId][0];

			player.itemModifyDodge(DefinitionItems.ITEM_MODIFY_DODGE_PERCENT[itemId], turns);

			appendLog("Dodge chance increased by " + DefinitionItems.ITEM_MODIFY_DODGE_PERCENT[itemId] + " for "
				+ turns + " turns.");
		}

		// apply stun
		if (DefinitionItems.ITEM_APPLY_STUN_FLAG[itemId] == 1)
		{
			int turns = 0;
			if (DefinitionItems.ITEM_APPLIED_OVER_NUM_TURNS[itemId][1] == 1)
				turns = player.rank();
			else
				turns = DefinitionItems.ITEM_APPLIED_OVER_NUM_TURNS[itemId][0];

			monster.addStunCount(turns + 1);

			appendLog("The monster is stunned!");
		}

		// remove player effects
		if (DefinitionItems.ITEM_REMOVE_PLAYER_EFFECTS_FLAG[itemId] == 1)
		{
			player.removeEffects();
			appendLog(DefinitionItems.ITEM_NAME[itemId] + " removed your active effects.");
		}

		// next ability free
		if (DefinitionItems.ITEM_ABILITIES_ARE_FREE_FLAG[itemId] == 1)
		{
			player.addFreeAbility();

			appendLog("One free ability charge added to player.");
		}

		// apply animation to monster
		if (DefinitionItems.ITEM_APPLY_MONSTER_ANIMATION_ID[itemId] >= 0)
		{
			animateMonster(DefinitionItems.ITEM_APPLY_MONSTER_ANIMATION_ID[itemId]);
		}

		// modify monster attack power
		if (DefinitionItems.ITEM_MODIFY_MONSTER_ATTACK_POWER[itemId] != 0)
		{
			int turns = 0;
			if (DefinitionItems.ITEM_APPLIED_OVER_NUM_TURNS[itemId][1] == 1)
				turns = player.rank();
			else
				turns = DefinitionItems.ITEM_APPLIED_OVER_NUM_TURNS[itemId][0];

			monster.modifyAttackPowerByPercentTurns(DefinitionItems.ITEM_MODIFY_MONSTER_ATTACK_POWER[itemId], turns);

			appendLog(monster.name() + "'s attack damage is modified "
				+ DefinitionItems.ITEM_MODIFY_MONSTER_ATTACK_POWER[itemId] + "% for " + turns + " turns.");
		}

		// change monster image
		if (DefinitionItems.ITEM_TEMP_CHANGE_MONSTER_IMAGE_FLAG[itemId] == 1)
		{
			int turns = 0;
			if (DefinitionItems.ITEM_APPLIED_OVER_NUM_TURNS[itemId][1] == 1)
				turns = player.rank();
			else
				turns = DefinitionItems.ITEM_APPLIED_OVER_NUM_TURNS[itemId][0];

			monster.setTempImage(
				getResources().getIdentifier(DefinitionItems.ITEM_TEMP_CHANGE_MONSTER_IMAGE[itemId], "drawable",
					getPackageName()), turns);

			Animation a = Animator.getAnimation(Animator.FADE_OUT_SHAKING);
			a.setAnimationListener(new AnimationListener()
			{
				@Override
				public void onAnimationEnd(Animation arg0)
				{
					monsterImage.setImageResource(monster.tempImageResource());
					monsterImage.invalidate();
					monsterImage.startAnimation(Animator.getAnimation(Animator.FADE_IN));
					appendLog("The creature was transformed!");
				}

				@Override
				public void onAnimationRepeat(Animation animation)
				{

				}

				@Override
				public void onAnimationStart(Animation animation)
				{

				}

			});
			monsterImage.startAnimation(a);

		}

		// kill monster %
		if (DefinitionItems.ITEM_KILL_MONSTER_PERCENT_CHANCE[itemId] > 0)
		{
			if (Helper.randomInt(101) < DefinitionItems.ITEM_KILL_MONSTER_PERCENT_CHANCE[itemId])
			{
				appendLog(DefinitionItems.ITEM_NAME[itemId] + " blew up the " + monster.name() + "!");
				killMonster();
			}
			else
			{
				appendLog("The " + DefinitionItems.ITEM_NAME[itemId] + " fizzled.");
			}
		}

		// monster cannot attack
		if (DefinitionItems.ITEM_MONSTER_CANNOT_ATTACK_FLAG[itemId] == 1)
		{
			int turns = 0;
			if (DefinitionItems.ITEM_APPLIED_OVER_NUM_TURNS[itemId][1] == 1)
				turns = player.rank();
			else
				turns = DefinitionItems.ITEM_APPLIED_OVER_NUM_TURNS[itemId][0];

			monster.addStunCount(turns + 1);

			appendLog("The monster is unable to attack for " + turns + " turns.");
		}

		// restart fight at full health
		if (DefinitionItems.ITEM_RESTART_FIGHT_FLAG[itemId] == 1)
		{
			restartFight();
		}

		// modify monster HP
		if (DefinitionItems.ITEM_MODIFY_MONSTER_HP_LESS_PERCENT_MAX[itemId] > 0)
		{
			int amt =
				Helper.getPercentFromInt(DefinitionItems.ITEM_MODIFY_MONSTER_HP_LESS_PERCENT_MAX[itemId],
					monster.maxHP());

			monster.updateHP(-amt);
			appendLog(DefinitionItems.ITEM_NAME[amt] + " dealt " + amt + " damage.");
		}

		// warp to round
		if (DefinitionItems.ITEM_WARP_TO_ROUND[itemId] >= 0)
		{
			player.setCurrentRound(DefinitionItems.ITEM_WARP_TO_ROUND[itemId]);
			player.setCurrentFight(1);
			DBHandler.updatePlayer(player);

			warpToRound();
		}

		updateViews();
	}

	private void useAbility(int abilityId, int randomnumber)
	{
		appendLog("Used Ability: " + (String) DefinitionRunes.runeData[abilityId][DefinitionRunes.RUNE_NAMES][0]);

		if (inCombat == false)
			return;

		if (player.hasFreeAbility())
			player.useFreeAbility();

		else
			player.updateAP(-(Integer) DefinitionRunes.runeData[abilityId][DefinitionRunes.RUNE_AP_COST][0]);

		playerAPView.setText("AP: " + player.currentAP() + "/" + player.maxAP());

		// APPLY PLAYER LUCKY EFFECT
		if (randomnumber > 99 && !player.isEffectActive(Helper.getEffectIdByName("Lucky")))
		{
			player.addActiveEffect(Helper.getEffectIdByName("Lucky"));
		}

		doPlayerAbility(abilityId);
		endPlayerAttack();
	}

	private void doMonsterAbility(int abilityId)
	{
		ItemRune ability = new ItemRune(DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY, abilityId, getApplicationContext());
		animateAbility(ability, 1);
		doGenericAbilityActions(ability, monster, player, 1);
	}

	private void doPlayerAbility(int abilityId)
	{
		ItemRune ability = new ItemRune(DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY, abilityId, getApplicationContext());
		animateAbility(ability, 0);

		doGenericAbilityActions(ability, player, monster, 0);
	}

	private void doGenericAbilityActions(ItemRune ability, Actor source, Actor target, int sourceFlag)
	{
		// first: determine if a bonus condition is met
		boolean doBonus = false;
		boolean isCasting = false;
		boolean isCounter = false;
		boolean castingWaitToApplyEffect = false;
		boolean castingWaitToApplyDamage = false;

		if ((Integer) DefinitionRunes.runeData[ability.id()][DefinitionRunes.RUNE_SOUND_CLIP][0] != 0)
			SoundManager
				.playSound((Integer) DefinitionRunes.runeData[ability.id()][DefinitionRunes.RUNE_SOUND_CLIP][0]);

		if (sourceFlag == 0)
		{
			source = (Player) source;
			target = (Monster) target;
		}
		else
		{
			source = (Monster) source;
			target = (Player) target;
		}

		if (ability.comboActiveEffectRequirementID() >= 0)
		{
			if (ability.comboActiveEffectActor() == 0)
			{
				if (player.isEffectActive(ability.comboActiveEffectRequirementID()))
					doBonus = true;
			}
			else
			{
				if (monster.isEffectActive(ability.comboActiveEffectRequirementID()))
					doBonus = true;
			}
		}

		// second: see if this is a casting ability
		if (ability.castingTurnsMin() > 0)
		{
			source.startCasting(ability, source);
			return;
		}

		// deal damage:
		if (ability.dealWeaponDamageMin() > 0)
		{
			int dmg = 0;
			if (doBonus)
			{
				if (sourceFlag == 0)
					dmg =
						((Player) source).getDamage()
							* (Helper.getRandomIntFromRange(1 + ability.dealWeaponDamageMaxBonus(),
								ability.dealWeaponDamageMinBonus()));

				else
					dmg =
						((Monster) source).getDamage()
							* (Helper.getRandomIntFromRange(1 + ability.dealWeaponDamageMaxBonus(),
								ability.dealWeaponDamageMinBonus()));
			}
			else
			{
				if (sourceFlag == 0)
					dmg =
						((Player) source).getDamage()
							* (Helper.getRandomIntFromRange(1 + ability.dealWeaponDamageMax(),
								ability.dealWeaponDamageMin()));

				else
					dmg =
						((Monster) source).getDamage()
							* (Helper.getRandomIntFromRange(1 + ability.dealWeaponDamageMax(),
								ability.dealWeaponDamageMin()));
			}

			appendLog(ability.name() + " dealt " + dmg + " damage to " + target.name());
			dealAbilityDamage(source, target, -dmg);

		}// END deal damage

		// deal special damage
		if (ability.dealSpecialDamageID() >= 0)
		{
			int dmg;

			if (sourceFlag == 0)
				dmg =
					Helper.getSpecialDamageAmount(ability.dealSpecialDamageID(), ((Player) source).getDamage(), source,
						target);

			else
				dmg =
					Helper.getSpecialDamageAmount(ability.dealSpecialDamageID(), ((Monster) source).getDamage(),
						source, target);

			appendLog(ability.name() + " dealt " + dmg + " damage to " + target.name());
			dealAbilityDamage(source, target, -dmg);

		}// END deal special damage

		// stun:
		if (ability.stunTurns() > 0)
		{
			boolean reqMet = true;
			// check source effect active requirement
			if (ability.stunOnlyIfSourceEffectActive() >= 0)
			{
				reqMet = source.isEffectActive(ability.stunOnlyIfSourceEffectActive());
			}
			else if (ability.stunOnlyIfTargetEffectActive() >= 0)
			{
				reqMet = target.isEffectActive(ability.stunOnlyIfTargetEffectActive());
			}

			if (reqMet)
			{
				if (ability.stunActor() == 0)
				{
					source.addStunCount(ability.stunTurns());
				}
				else
				{
					target.addStunCount(ability.stunTurns());
				}
			}
		}// END Stun

		// deal multiple stat based damage
		if (ability.dealMultipleStatBasedDamageMult() > 0)
		{
			int amt1 = 0;
			int amt2 = 0;
			if (ability.dealMultipleStatBasedDamageStat1() == 0)
				amt1 = source.strength();

			if (ability.dealMultipleStatBasedDamageStat1() == 1)
				amt1 = source.reaction();

			if (ability.dealMultipleStatBasedDamageStat1() == 2)
				amt1 = source.knowledge();

			if (ability.dealMultipleStatBasedDamageStat1() == 3)
				amt1 = source.magelore();

			if (ability.dealMultipleStatBasedDamageStat1() == 4)
				amt1 = source.luck();

			if (ability.dealMultipleStatBasedDamageStat1() == 0)
				amt2 = source.strength();

			if (ability.dealMultipleStatBasedDamageStat1() == 1)
				amt2 = source.reaction();

			if (ability.dealMultipleStatBasedDamageStat1() == 2)
				amt2 = source.knowledge();

			if (ability.dealMultipleStatBasedDamageStat1() == 3)
				amt2 = source.magelore();

			if (ability.dealMultipleStatBasedDamageStat1() == 4)
				amt2 = source.luck();

			int dmg = (int) Math.round(ability.dealMultipleStatBasedDamageMult() * amt1 * amt2);

			appendLog(ability.name() + " dealt " + dmg + " damage to " + target.name());
			dealAbilityDamage(source, target, -dmg);
		}

		// deal stat is hp% dmg
		if (ability.dealStatIsHpPercentDamageSourceOrTargetFlag() >= 0)
		{
			int statVal = 0;
			if (ability.dealStatIsHpPercentDamageSourceOrTargetFlag() == 0)
			{
				if (ability.dealStatIsHpPercentDamageStatId() == 0)
					statVal = source.strength();

				if (ability.dealStatIsHpPercentDamageStatId() == 1)
					statVal = source.reaction();

				if (ability.dealStatIsHpPercentDamageStatId() == 2)
					statVal = source.knowledge();

				if (ability.dealStatIsHpPercentDamageStatId() == 3)
					statVal = source.magelore();

				if (ability.dealStatIsHpPercentDamageStatId() == 4)
					statVal = source.luck();

			}
			else
			{
				if (ability.dealStatIsHpPercentDamageStatId() == 0)
					statVal = target.strength();

				if (ability.dealStatIsHpPercentDamageStatId() == 1)
					statVal = target.reaction();

				if (ability.dealStatIsHpPercentDamageStatId() == 2)
					statVal = target.knowledge();

				if (ability.dealStatIsHpPercentDamageStatId() == 3)
					statVal = target.magelore();

				if (ability.dealStatIsHpPercentDamageStatId() == 4)
					statVal = target.luck();
			}

			int dmg = (int) Math.round((0.01 * statVal) * target.maxHP());

			appendLog(ability.name() + " dealt " + dmg + " damage to " + target.name());
			dealAbilityDamage(source, target, -dmg);
		}// END deal stat based damage

		// apply effect
		if (!castingWaitToApplyEffect)
		{
			if (ability.appliesEffectSource().length > 0)
			{
				for (int a = 0; a < ability.appliesEffectSource().length; a++)
				{
					source.addActiveEffect(ability.appliesEffectSource()[a]);
					appendLog(source.name() + " is " + DefinitionEffects.EFFECT_NAMES[ability.appliesEffectSource()[a]]
						+ ".");
				}

			}
			if (ability.appliesEffectTarget().length > 0)
			{
				for (int a = 0; a < ability.appliesEffectTarget().length; a++)
				{
					target.addActiveEffect(ability.appliesEffectTarget()[a]);
					appendLog(target.name() + " is " + DefinitionEffects.EFFECT_NAMES[ability.appliesEffectTarget()[a]]
						+ ".");
				}

			}
		}

		// sap AP based on stat
		if (ability.sapAPBasedOnStatID() >= 0)
		{
			int statVal = 0;
			if (ability.sapAPActorStatUsedFlag() == 0)
			{
				if (ability.sapAPBasedOnStatID() == 0)
					statVal = source.strength();

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
			}
			else
			{
				if (ability.sapAPBasedOnStatID() == 0)
					statVal = target.strength();

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
			appendLog(ability.name() + " has sapped " + statVal + " AP from " + target.name() + ".");
			target.updateAP(-statVal);

			if (ability.sapAPAndTransferToSourceFlag() == 1)
			{
				appendLog(source.name() + " has gained " + statVal + " AP.");
				source.updateAP(statVal);
			}
		}// END sap AP based on stat

		// modify AP
		if (ability.modifyAPActor() >= 0)
		{
			if (ability.modifyAPActor() == 0)
			{
				int amt = (int) Math.round((0.01 * (float) ability.modifyAPPercentAmount()) * (float) source.maxAP());
				if (doBonus)
				{
					amt =
						(int) Math
							.round((0.01 * (float) ability.modifyAPPercentAmountBonus()) * (float) source.maxAP());
				}
				source.updateAP(-amt);
			}
			else
			{
				int amt = (int) Math.round((0.01 * (float) ability.modifyAPPercentAmount()) * (float) target.maxAP());
				if (doBonus)
				{
					amt =
						(int) Math
							.round((0.01 * (float) ability.modifyAPPercentAmountBonus()) * (float) target.maxAP());
				}
				target.updateAP(-amt);
			}
		}// END modify AP

		// modify HP
		if (ability.modifyHPActor() >= 0)
		{
			if (ability.modifyHPActor() == 0)
			{
				int amt = (int) Math.round((0.01 * (float) ability.modifyHPPercentAmount()) * (float) source.maxHP());
				if (doBonus)
				{
					amt =
						(int) Math
							.round((0.01 * (float) ability.modifyHPPercentAmountBonus()) * (float) source.maxHP());
				}
				source.updateHP(-amt);
			}
			else
			{
				int amt = (int) Math.round((0.01 * (float) ability.modifyHPPercentAmount()) * (float) target.maxHP());
				if (doBonus)
				{
					amt =
						(int) Math
							.round((0.01 * (float) ability.modifyHPPercentAmountBonus()) * (float) target.maxHP());
				}
				target.updateHP(-amt);
			}
		}// END modify HP

		// absorb damage for some future turns
		if (ability.counterAbsorbDamageTurns() > 0)
		{
			int absDmgMinPercent = ability.counterAbsorbDamageMin();
			if (doBonus)
				absDmgMinPercent = ability.counterAbsorbDamageMinBonus();

			int absDmgMaxPercent = ability.counterAbsorbDamageMax();
			if (doBonus)
				absDmgMaxPercent = ability.counterAbsorbDamageMaxBonus();

			if (ability.counterAbsorbDamageStatIsPercentStatId() >= 0)
			{
				// absorb damage based on a stat
				int statId = ability.counterAbsorbDamageStatIsPercentStatId();
				int statMult = ability.counterAbsorbDamageStatIsPercentStatMult();
				int statAmt = 0;
				if (statId == 0)
				{
					statAmt = source.strength();
				}
				else if (statId == 1)
				{
					statAmt = source.reaction();
				}
				else if (statId == 2)
				{
					statAmt = source.knowledge();
				}
				else if (statId == 3)
				{
					statAmt = source.magelore();
				}
				else if (statId == 4)
				{
					statAmt = source.luck();
				}
				absDmgMinPercent = statAmt * statMult;
				absDmgMaxPercent = statAmt * statMult;
			}

			source.setCounterAbsorbDamage(ability.counterAbsorbDamageTurns(), absDmgMinPercent, absDmgMaxPercent);

			String absRangeText = "" + absDmgMinPercent;
			if (absDmgMinPercent != absDmgMaxPercent)
				absRangeText = absDmgMinPercent + "-" + absDmgMaxPercent;

			appendLog(source.name() + " will absorb " + absRangeText + "% of dmg for "
				+ ability.counterAbsorbDamageTurns() + " turns.");
		}

		// modify stat
		if (ability.counterModifyStatTurns() > 0)
		{
			if (ability.counterModifyStatSourceOrTargetFlag() == 0)
			{
				source.setcounterModifyHPStatBased(ability.counterModifyStatTurns(), ability.counterModifyStatId(),
					ability.counterModifyStatMult());
				appendLog(source.name() + "'s stats have been modified.");
			}
			else
			{
				target.setcounterModifyHPStatBased(ability.counterModifyStatTurns(), ability.counterModifyStatId(),
					ability.counterModifyStatMult());
				appendLog(target.name() + "'s stats have been modified.");
			}
		}

		// dot modify hp max hp based
		if (ability.counterModifyHPMaxHPBasedTurns() > 0)
		{
			if (ability.counterModifyHPMaxHPBasedSourceOrTargetFlag() == 0)
			{
				source.setCounterDotModifyHPMaxHPBasedHP(ability.counterModifyHPMaxHPBasedTurns(),
					ability.counterModifyHPMaxHPBasedAmount());
			}
			else
			{
				target.setCounterDotModifyHPMaxHPBasedHP(ability.counterModifyHPMaxHPBasedTurns(),
					ability.counterModifyHPMaxHPBasedAmount());
			}
		}

		// dot modify hp stat based times weapon dmg
		if (ability.counterModifyHPStatBasedTimesWpnDmgTurns() > 0)
		{
			// stats used in this ability are always based off source
			int turns = ability.counterModifyHPStatBasedTimesWpnDmgTurns();
			int statId = ability.counterModifyHPStatBasedTimesWpnDmgStatId();
			int hitDmg = source.getDamage();
			double mult = ability.counterModifyHPStatBasedTimesWpnDmgMult();
			int statAmt = 0;
			int changeAmt = 0;

			if (statId == 0)
			{
				statAmt = source.strength();
			}
			else if (statId == 1)
			{
				statAmt = source.reaction();
			}
			else if (statId == 2)
			{
				statAmt = source.knowledge();
			}
			else if (statId == 3)
			{
				statAmt = source.magelore();
			}
			else if (statId == 4)
			{
				statAmt = source.luck();
			}
			changeAmt = (int) Math.round((double) statAmt * (double) hitDmg * mult);

			String actorName = "";
			if (ability.counterModifyStatSourceOrTargetFlag() == 0)
			{
				source.setCounterModifyHPStatTimesWpnDmgBased(turns, changeAmt);
				actorName = source.name();
			}
			else
			{
				target.setCounterModifyHPStatTimesWpnDmgBased(turns, changeAmt);
				actorName = target.name();
			}

			if (changeAmt < 0)
				appendLog(actorName + " will take " + changeAmt + " dmg for " + turns + " turns.");
			else
				appendLog(actorName + " will gain " + changeAmt + " HP for " + turns + " turns.");

		}

		// dot modify hp based on wpn dmg
		if (ability.counterDotModifyHPWeaponDamageBasedTurns() > 0)
		{
			int dmg = player.getDamage();
			if (sourceFlag == 1)
				dmg = monster.getDamage();

			target.setCounterDotModifyHPWeaponDamageBased(ability.counterDotModifyHPWeaponDamageBasedTurns(),
				ability.counterDotModifyHPWeaponDamageBasedMinAmount(),
				ability.counterDotModifyHPWeaponDamageBasedMaxAmount(),
				ability.counterDotModifyHPWeaponDamageBasedMinAmountBonus(),
				ability.counterDotModifyHPWeaponDamageBasedMaxAmount(), ability.comboActiveEffectRequirementID(),
				ability.name(), dmg);
		}

		// reveal target info
		if (ability.revealsTargetInfo() == 1)
		{
			monsterBar.setVisibility(View.INVISIBLE);
			expandedMonsterBar.setVisibility(View.VISIBLE);
		}

		// reflect damage
		if (ability.counterReflectDamageTurns() > 0)
		{
			int turns = ability.counterReflectDamageTurns();
			int percentAmount = ability.counterReflectDamagePercentAmt();
			if (doBonus)
			{
				percentAmount = ability.counterReflectDamagePercentAmtBonus();
			}

			source.setCounterReflectDamage(turns, percentAmount);

			appendLog(source.name() + " will reflect " + percentAmount + "% of taken damage for " + turns + " turns.");
		}

		// counter modify stat per turn based off (stat) turns
		if (ability.counterDotModifyStatPerTurnSourceOrTargetStatForTurns() >= 0)
		{
			int turns = 0;
			int statIdToMod = ability.counterDotModifyStatPerTurnStatId();
			int statIdForTurns = ability.counterDotModifyStatPerTurnStatIdForTurns();
			int amt = ability.counterDotModifyStatPerTurnAmount();
			if (ability.counterDotModifyStatPerTurnSourceOrTargetStatForTurns() == 0)
			{
				if (statIdForTurns == 0)
					turns = source.strength();
				if (statIdForTurns == 1)
					turns = source.reaction();
				if (statIdForTurns == 2)
					turns = source.knowledge();
				if (statIdForTurns == 3)
					turns = source.magelore();
				if (statIdForTurns == 4)
					turns = source.luck();
			}
			else
			{
				if (statIdForTurns == 0)
					turns = target.strength();
				if (statIdForTurns == 1)
					turns = target.reaction();
				if (statIdForTurns == 2)
					turns = target.knowledge();
				if (statIdForTurns == 3)
					turns = target.magelore();
				if (statIdForTurns == 4)
					turns = target.luck();
			}

			if (ability.counterModifyStatSourceOrTargetFlag() == 0)
			{
				source.setCounterModifyStatForStatTurns(turns, statIdToMod, amt);
			}
			else
			{
				target.setCounterModifyStatForStatTurns(turns, statIdToMod, amt);
			}
		}
		
		//counter dot modify hp, stat is % with mult
		if(ability.counterDotModifyHPStatIsPercentWithMultSourceOrTargetFlag() >= 0)
		{
			int turns = ability.counterDotModifyHPStatIsPercentWithMultTurns();
			int statId = ability.counterDotModifyHPStatIsPercentWithMultStatId();
			int mult = ability.counterDotModifyHPStatIsPercentWithMultMult();
			int statAmt = 0;
			int amt = 0;
			
			//amts based on source stats for this ability
			if(statId == 0)
				statAmt = source.strength();
			
			if(statId == 1)
				statAmt = source.reaction();
			
			if(statId == 2)
				statAmt = source.knowledge();
			
			if(statId == 3)
				statAmt = source.magelore();
			
			if(statId == 4)
				statAmt = source.luck();
			
			amt = statAmt * mult;
			
			if(ability.counterDotModifyHPStatIsPercentWithMultSourceOrTargetFlag() == 0)
			{				
				source.setCounterModifyHPStatIsPercentWithMult(turns, amt);
				appendLog(source.name()+"'s HP will be modified by "+amt+" per turn for "+turns+" turns.");
			}
			else
			{
				target.setCounterModifyHPStatIsPercentWithMult(turns, amt);
				appendLog(target.name()+"'s HP will be modified by "+amt+" per turn for "+turns+" turns.");
			}			
		}
	}

	private void endPlayerAttack()
	{
		turnCleanup();

		if (inCombat == false)
		{
			updateViews();
			return;
		}
		else
		{
			turnFlag = 1;
			advanceTurn();
		}
	}

	private void clearPlayerAttackTextDelay(double d)
	{
		try
		{
			clearPlayerAttackTextHandler.removeCallbacks(clearPlayerAttackTextPost);
			clearPlayerAttackTextHandler.postDelayed(clearPlayerAttackTextPost, (long) (d * 1000));
		}
		catch (Exception e)
		{
			appendLog("thread did not sleep: " + e.getMessage());
			clearPlayerAttackTextPost.run();
		}
	}

	private Runnable clearPlayerAttackTextPost = new Runnable()
	{
		public void run()
		{
			clearPlayerAttackText();
		}
	};

	private void clearPlayerAttackText()
	{
		playerAttackText.setText("");
	}

	private void doCastingTurn(int turnFlag)
	{
		// player's casting
		if (turnFlag == 0)
		{
			boolean isBonus = Helper.checkForBonus(player.currentCastingAbility(), player, monster);

			// casting is over, do anything at end?
			if (player.castingTurnsLeft() <= 0)
			{
				// deal damage on end

				if (player.currentCastingAbility().castingDamageOnEndFlag() == 1)
				{
					int dmg = Helper.getCastingDamage(player.currentCastingAbility(), isBonus);

					appendLog(player.currentCastingAbility().name() + " dealt " + monster.name() + " " + dmg
						+ " damage.");
					monster.updateHP(-dmg);
				}

				// apply effect on end
				if (player.currentCastingAbility().castingApplyEffectOnEndFlag() == 1)
				{
					if (player.currentCastingAbility().appliesEffectSource().length > 0)
						for (int a = 0; a < player.currentCastingAbility().appliesEffectSource().length; a++)
						{
							player.addActiveEffect(player.currentCastingAbility().appliesEffectSource()[a]);
						}

					else if (player.currentCastingAbility().appliesEffectTarget().length > 0)
					{
						for (int a = 0; a < player.currentCastingAbility().appliesEffectTarget().length; a++)
						{
							monster.addActiveEffect(player.currentCastingAbility().appliesEffectTarget()[a]);
						}

					}

				}
			}
			// currently casting, do anything on each turn?
			if (player.castingTurnsLeft() >= 0)
			{
				// deal damage on turn
				if (player.currentCastingAbility().castingDamageOnTurnFlag() == 1)
				{
					int dmg = 0;

					// deal basic weapon damage
					if (player.currentCastingAbility().dealWeaponDamageMin() >= 0)
					{
						dmg = Helper.getCastingDamage(player.currentCastingAbility(), isBonus);
					}

					// deal stat based damage
					else if (player.currentCastingAbility().dealStatIsHpPercentDamageSourceOrTargetFlag() >= 0)
					{
						dmg = Helper.getCastingStatDamage(player.currentCastingAbility(), isBonus, player, monster);
					}

					appendLog(player.currentCastingAbility().name() + " dealt " + monster.name() + " " + dmg
						+ " damage.");
					monster.updateHP(-dmg);
				}

				// gain HP per turn
				if (player.currentCastingAbility().castingModifyHPPerTurnFlag() == 1)
				{
					if (player.currentCastingAbility().modifyHPActor() == 0)
					{
						// modify player's HP
						int amt =
							Helper.getPercentFromInt(player.currentCastingAbility().modifyHPPercentAmount(),
								player.maxHP());

						if (isBonus)
							amt =
								Helper.getPercentFromInt(player.currentCastingAbility().modifyHPPercentAmountBonus(),
									player.maxHP());

						appendLog("You gained " + amt + " HP from " + player.currentCastingAbility().name() + ".");
						player.updateHP(amt);
					}
					else
					{
						// modify monster's HP
						int amt =
							Helper.getPercentFromInt(monster.currentCastingAbility().modifyHPPercentAmount(),
								monster.maxHP());

						if (isBonus)
							amt =
								Helper.getPercentFromInt(monster.currentCastingAbility().modifyHPPercentAmountBonus(),
									monster.maxHP());

						appendLog(monster.name() + " lost " + amt + " HP from "
							+ monster.currentCastingAbility().name() + ".");
						monster.updateHP(amt);
					}
				}

				// gain AP per turn
				if (player.currentCastingAbility().castingModifyAPPerTurnFlag() == 1)
				{
					if (player.currentCastingAbility().modifyAPActor() == 0)
					{
						// modify player's AP
						int amt =
							Helper.getPercentFromInt(player.currentCastingAbility().modifyAPPercentAmount(),
								player.maxAP());

						if (isBonus)
							amt =
								Helper.getPercentFromInt(player.currentCastingAbility().modifyAPPercentAmountBonus(),
									player.maxAP());

						appendLog("You gained " + amt + " AP from " + player.currentCastingAbility().name() + ".");
						player.updateAP(amt);
					}
					else
					{
						// modify monster's AP
						int amt =
							Helper.getPercentFromInt(monster.currentCastingAbility().modifyAPPercentAmount(),
								monster.maxAP());

						if (isBonus)
							amt =
								Helper.getPercentFromInt(monster.currentCastingAbility().modifyAPPercentAmountBonus(),
									monster.maxAP());

						appendLog(monster.name() + " lost " + amt + " AP from "
							+ monster.currentCastingAbility().name() + ".");
						monster.updateAP(amt);
					}
				}

				// do any specialID stuff
				if (player.currentCastingAbility().castingSpecialID() >= 0)
				{
					int[] ints =
						Helper.getSpecialCastingInt(player.currentCastingAbility().castingSpecialID(), player, monster);

					// add random effect
					if (ints[0] >= 0)
					{
						monster.addActiveEffect(ints[0]);
					}
				}
			}
		}
		else
		{
			boolean isBonus = Helper.checkForBonus(monster.currentCastingAbility(), monster, player);

			// casting is over, do anything at end?
			if (monster.castingTurnsLeft() <= 0)
			{
				// deal damage on end

				if (monster.currentCastingAbility().castingDamageOnEndFlag() == 1)
				{
					int dmg = Helper.getCastingDamage(monster.currentCastingAbility(), isBonus);

					appendLog(monster.currentCastingAbility().name() + " dealt " + player.name() + " " + dmg
						+ " damage.");
					player.updateHP(-dmg);
				}

				// apply effect on end
				if (monster.currentCastingAbility().castingApplyEffectOnEndFlag() == 1)
				{
					if (monster.currentCastingAbility().appliesEffectSource().length > 0)
					{
						for (int a = 0; a < monster.currentCastingAbility().appliesEffectSource().length; a++)
						{
							monster.addActiveEffect(monster.currentCastingAbility().appliesEffectSource()[a]);
						}
					}

					else if (monster.currentCastingAbility().appliesEffectTarget().length > 0)
					{
						for (int a = 0; a < monster.currentCastingAbility().appliesEffectTarget().length; a++)
						{
							player.addActiveEffect(monster.currentCastingAbility().appliesEffectTarget()[a]);
						}
					}

				}
			}
			// currently casting, do anything on each turn?
			if (monster.castingTurnsLeft() >= 0)
			{
				// deal damage on turn
				if (monster.currentCastingAbility().castingDamageOnTurnFlag() == 1)
				{
					int dmg = 0;

					// deal basic weapon damage
					if (monster.currentCastingAbility().dealWeaponDamageMin() >= 0)
					{
						dmg = Helper.getCastingDamage(monster.currentCastingAbility(), isBonus);
					}

					// deal stat based damage
					else if (monster.currentCastingAbility().dealStatIsHpPercentDamageSourceOrTargetFlag() >= 0)
					{
						dmg = Helper.getCastingStatDamage(monster.currentCastingAbility(), isBonus, monster, player);
					}

					appendLog(monster.currentCastingAbility().name() + " dealt " + player.name() + " " + dmg
						+ " damage.");
					player.updateHP(-dmg);
				}

				// gain HP per turn
				if (monster.currentCastingAbility().castingModifyHPPerTurnFlag() == 1)
				{
					if (monster.currentCastingAbility().modifyHPActor() == 0)
					{
						// modify monster's HP
						int amt =
							Helper.getPercentFromInt(monster.currentCastingAbility().modifyHPPercentAmount(),
								monster.maxHP());

						if (isBonus)
							amt =
								Helper.getPercentFromInt(monster.currentCastingAbility().modifyHPPercentAmountBonus(),
									monster.maxHP());

						appendLog("You gained " + amt + " HP from " + monster.currentCastingAbility().name() + ".");
						monster.updateHP(amt);
					}
					else
					{
						// modify player's HP
						int amt =
							Helper.getPercentFromInt(monster.currentCastingAbility().modifyHPPercentAmount(),
								player.maxHP());

						if (isBonus)
							amt =
								Helper.getPercentFromInt(monster.currentCastingAbility().modifyHPPercentAmountBonus(),
									player.maxHP());

						appendLog(player.name() + " lost " + amt + " HP from " + monster.currentCastingAbility().name()
							+ ".");
						player.updateHP(amt);
					}
				}

				// gain AP per turn
				if (monster.currentCastingAbility().castingModifyAPPerTurnFlag() == 1)
				{
					if (monster.currentCastingAbility().modifyAPActor() == 0)
					{
						// modify monster's AP
						int amt =
							Helper.getPercentFromInt(monster.currentCastingAbility().modifyAPPercentAmount(),
								monster.maxAP());

						if (isBonus)
							amt =
								Helper.getPercentFromInt(monster.currentCastingAbility().modifyAPPercentAmountBonus(),
									monster.maxAP());

						appendLog("You gained " + amt + " AP from " + monster.currentCastingAbility().name() + ".");
						monster.updateAP(amt);
					}
					else
					{
						// modify player's AP
						int amt =
							Helper.getPercentFromInt(monster.currentCastingAbility().modifyAPPercentAmount(),
								player.maxAP());

						if (isBonus)
							amt =
								Helper.getPercentFromInt(monster.currentCastingAbility().modifyAPPercentAmountBonus(),
									player.maxAP());

						appendLog(player.name() + " lost " + amt + " AP from " + monster.currentCastingAbility().name()
							+ ".");
						player.updateAP(amt);
					}
				}

				// do any specialID stuff
				if (monster.currentCastingAbility().castingSpecialID() >= 0)
				{
					int[] ints =
						Helper
							.getSpecialCastingInt(monster.currentCastingAbility().castingSpecialID(), monster, player);

					// add random effect
					if (ints[0] >= 0)
					{
						player.addActiveEffect(ints[0]);
					}
				}
			}
		}

	}

	private void startMonsterAttack()
	{
		Log.d("combat", "startMonsterAttack()");

		if (inCombat == false)
			return;

		if (monster.casting())
		{
			doCastingTurn(1);
			endMonsterAttack();
			return;
		}

		int didAbility = 0;

		// the monster is attacking the player

		// TODO code monster use ability
		// check if monster should use ability

		// if monster has no ap, skip this
		if (monster.currentAP() > 0)
		{
			// first see if you satisify prefer to hit percent
			if (Helper.randomInt(100) > monster.preferToHitPercent())
			{
				// decide what ability to try to use
				int useAbility = Helper.getWinningPercent(monster.abilityPreferences());
				int usedAbilityId = monster.getActiveAbilityByIndex(useAbility);
				if ((Integer) DefinitionRunes.runeData[usedAbilityId][DefinitionRunes.RUNE_AP_COST][0] <= monster
					.currentAP())
				{
					didAbility = 1;

					monster
						.updateAP(-(Integer) DefinitionRunes.runeData[usedAbilityId][DefinitionRunes.RUNE_AP_COST][0]);

					appendLog(monster.name() + " is using "
						+ (String) DefinitionRunes.runeData[usedAbilityId][DefinitionRunes.RUNE_NAMES][0] + ".");

					doMonsterAbility(usedAbilityId);
					endMonsterAttack();
					monster.resetNoAbilityInARow();
				}
			}
		}

		if (didAbility == 0)
		{
			monster.updateNoAbilityInARow();
			monsterAttackPlayer();
			endMonsterAttack();
		}

	}

	private void endMonsterAttack()
	{
		turnCleanup();

		if (inCombat == false)
		{
			updateViews();
			return;
		}
		else
		{
			turnFlag = 0;
			advanceTurn();
		}
	}

	private void advanceTurnDelay(double d)
	{
		try
		{
			advanceTurnHandler.removeCallbacks(advanceTurnPost);
			advanceTurnHandler.postDelayed(advanceTurnPost, (long) (d * 1000));
		}
		catch (Exception e)
		{
			appendLog("thread did not sleep: " + e.getMessage());
			advanceTurnPost.run();
		}
	}

	private Runnable advanceTurnPost = new Runnable()
	{
		public void run()
		{
			advanceTurn();
		}
	};

	private void advanceTurn()
	{
		player.setPlayerAttackWait(true);

		updateButtons();

		turnCleanup(); // check for deaths

		// decide who can go next

		if (inCombat == false)
		{
			updateViews();
			return;
		}

		// player's turn?
		if (turnFlag == 0)
		{
			// mod AP on turn amount
			if (DefinitionClassType.CLASS_TYPE_AP_REGEN[DefinitionClasses.CLASS_TYPE[player.playerClass()]][3] != 0)
			{
				player.updateAP(DefinitionClassType.CLASS_TYPE_AP_REGEN[DefinitionClasses.CLASS_TYPE[player
					.playerClass()]][2]);
			}

			ArrayList<Actor.ReturnData> returnData = player.advanceTurn();
			for (int a = 0; a < returnData.size(); a++)
			{
				appendLog(returnData.get(a).whatHappend);
			}

			// if player is not stunned and not casting, it can go
			if (player.stunned() == false && player.casting() == false)
			{
				player.setPlayerAttackWait(false);
			}
			else
			{
				turnFlag = 1;
				player.setPlayerAttackWait(true);

				if (player.casting() == true)
				{
					// TODO what happens with this line below?
					// checkPlayerCastingAbilities();

					doCastingTurn(0);

					turnCleanup();
				}
			}

			updateViews();
		}

		// monster's turn?
		if (turnFlag == 1)
		{
			// check if monster has temp image up
			if (monster.tempImageTurns() > 0)
			{
				Log.d("combat", "monster should have temp image for " + monster.tempImageTurns() + " turns");
				monsterImage.setImageResource(monster.tempImageResource());
				monsterImage.invalidate();
			}
			else
			{
				monsterImage.setImageResource(monster.imageResource());
				monsterImage.invalidate();
			}

			// maybe play a taunt
			if (Helper.randomInt(100) < 20)
			{
				SoundManager.playSound(DefinitionMonsters.MONSTER_TAUNT_SOUND_TYPE[monster.monsterID()]);
			}

			ArrayList<Actor.ReturnData> returnData = monster.advanceTurn();
			for (int a = 0; a < returnData.size(); a++)
			{
				appendLog(returnData.get(a).whatHappend);
			}

			// if the monster is not stunned and not casting, it can go
			if (monster.casting())
			{
				startMonsterAttackDelay(1.6);
				turnCleanup();
			}
			else if (monster.stunned() == false)
			{
				player.setPlayerAttackWait(true);

				// wait some time then have the monster attack
				startMonsterAttackDelay(1.3);
			}
			else
			{
				turnFlag = 0;
				updateViews();
				advanceTurnDelay(0.8);
			}

		}
	}

	private void displayMonsterEffects()
	{
		for (int a = 0; a < monsterEffectImages.length; a++)
		{
			monsterEffectImages[a].setImageResource(0);
		}

		for (int i = 0; i < monster.getActiveEffects().size(); i++)
		{
			if (monster.getActiveEffectByIndex(i).imageResource() == 0)
			{
				monster.getActiveEffectByIndex(i).setImageResource(
					getResources().getIdentifier(monster.getActiveEffectByIndex(i).image(), "drawable",
						getPackageName()));
			}

			monsterEffectImages[i].setImageResource(monster.getActiveEffectByIndex(i).imageResource());
		}
	}

	private void displayPlayerEffects()
	{
		for (int a = 0; a < playerEffectImages.length; a++)
		{
			playerEffectImages[a].setImageResource(0);
		}

		for (int i = 0; i < player.getActiveEffects().size(); i++)
		{
			if (player.getActiveEffectByIndex(i).imageResource() == 0)
			{
				player.getActiveEffectByIndex(i).setImageResource(
					getResources()
						.getIdentifier(player.getActiveEffectByIndex(i).image(), "drawable", getPackageName()));
			}

			playerEffectImages[i].setImageResource(player.getActiveEffectByIndex(i).imageResource());

		}
	}

	private void turnCleanup()
	{
		if (player.currentHP() <= 0 || monster.currentHP() <= 0)
		{
			inCombat = false;
			player.setPlayerAttackWait(true);

			player.stopCasting();
			monster.stopCasting();

			clearAll();

			// IF THE PLAYER IS DEAD
			if (player.currentHP() < 1)
			{
				player.setPlayerAttackWait(true);
				player.setDead(true);
				appendLog("You have died.");

			}
			// IF THE MONSTER IS DEAD
			else
			{
				monster.setCurrentHP(0);
				monster.setDead(true);
				appendLog("You have won!");

				killMonster();
			}
		}
	}

	private void restartFight()
	{
		Intent i = null;
		Bundle b = new Bundle();

		// show next fight or end of round
		i = new Intent(getApplicationContext(), com.alderangaming.wizardsencounters.ControllerFightStart.class);

		// b.putSerializable("OwnedItems", OwnedItems);
		b.putSerializable("player", player);
		b.putInt("restartfightflag", 2);
		b.putInt("restartfightmonster", monster.monsterID());

		i.putExtras(b);

		startActivity(i);
		finish();
	}

	private void showRunAwayDialog()
	{
		AlertDialog.Builder fightEndDialog = new AlertDialog.Builder(this);

		fightEndDialog
			.setTitle(
				"Fight " + player.currentFight() + " of "
					+ DefinitionRounds.ROUND_NUMBER_OF_FIGHTS[player.currentRound() - 1] + " Aborted")
			.setMessage(
				"You may try this fight again, starting with " + (int) Math.round(player.maxHP() / 2) + "/"
					+ player.maxHP() + " health. If you drop out, your progress will revert to the beginning of Round "
					+ player.currentRound() + ".").setPositiveButton("Try Again", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					flee();

					dialog.cancel();

					DBHandler.updatePlayer(player);

					if (DBHandler.isOpen(getApplicationContext()))
					{
						DBHandler.close();
					}

					Intent i = null;
					Bundle b = new Bundle();

					// show next fight or end of round
					i =
						new Intent(getApplicationContext(),
							com.alderangaming.wizardsencounters.ControllerFightStart.class);

					// b.putSerializable("OwnedItems", OwnedItems);
					b.putSerializable("player", player);
					b.putInt("restartfightflag", 1);
					b.putInt("restartfightmonster", monster.monsterID());

					i.putExtras(b);

					startActivity(i);
					finish();
				}
			}).setNegativeButton("Drop Out", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					flee();

					dialog.cancel();

					if (DBHandler.isOpen(getApplicationContext()))
					{
						DBHandler.close();
					}

					finish();
				}
			}).setNeutralButton("Cancel", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					player.setPlayerAttackWait(false);

					updateButtons();

					dialog.cancel();

				}
			});

		AlertDialog alert = fightEndDialog.create();
		alert.show();
	}

	private void warpToRound()
	{
		Intent i = new Intent(getApplicationContext(), com.alderangaming.wizardsencounters.ControllerRoundStart.class);
		Bundle b = new Bundle();

		// b.putSerializable("OwnedItems", OwnedItems);
		b.putSerializable("player", player);

		i.putExtras(b);

		startActivity(i);

		finish();
	}

	private void showFightEndDialog()
	{
		AlertDialog.Builder fightEndDialog = new AlertDialog.Builder(this);

		fightEndDialog
			.setTitle(
				"Fight " + player.currentFight() + " of "
					+ DefinitionRounds.ROUND_NUMBER_OF_FIGHTS[player.currentRound() - 1] + " Complete")
			.setPositiveButton("Continue", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					player.updateCurrentFight();
					dialog.cancel();

					if (DefinitionRounds.ROUND_TYPE[player.currentRound() - 1] == 1)
					{
						player.setRank(player.rank() + 1);
						DBHandler.updatePlayer(player);
					}

					if (DBHandler.isOpen(getApplicationContext()))
					{
						DBHandler.close();
					}

					Intent i = null;
					Bundle b = new Bundle();

					// show next fight or end of round
					if (player.currentFight() > DefinitionRounds.ROUND_NUMBER_OF_FIGHTS[player.currentRound() - 1])
					{

						i =
							new Intent(getApplicationContext(),
								com.alderangaming.wizardsencounters.ControllerRoundEnd.class);
					}

					else
					{
						i =
							new Intent(getApplicationContext(),
								com.alderangaming.wizardsencounters.ControllerFightStart.class);
					}

					// b.putSerializable("OwnedItems", OwnedItems);
					b.putSerializable("player", player);
					b.putInt("background", backgroundImage);

					i.putExtras(b);

					startActivity(i);
					finish();
				}
			}).setNegativeButton("Drop Out", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					dialog.cancel();

					if (DBHandler.isOpen(getApplicationContext()))
					{
						DBHandler.close();
					}

					finish();
				}
			});

		AlertDialog alert = fightEndDialog.create();
		alert.show();

	}

	private Runnable endExplosionRunnable = new Runnable()
	{
		public void run()
		{
			explosionWaitHandler.removeCallbacks(endExplosionRunnable);
			showFightEndDialog();
		}
	};

	private void killMonster()
	{

		int goldDrop = monster.getGoldDrop();
		OwnedItems.updateGold(goldDrop);
		appendLog("The monster dropped " + goldDrop + " gold.");

		animateMonster(Animator.MOVE_UP);
		monsterImage.setImageResource(R.anim.largeexplosion);
		AnimationDrawable explosionAnimation = (AnimationDrawable) monsterImage.getDrawable();
		explosionWaitHandler.postDelayed(endExplosionRunnable, 2500);

		explosionAnimation.stop();
		explosionAnimation.start();
		SoundManager.playSound(SoundManager.SOUND_TYPE_MONSTER_DEAD);
		SoundManager.playSound(SoundManager.SOUND_TYPE_EXPLOSION_LARGE);

	}

	private void animateMonster(int animationId)
	{
		monsterImage.startAnimation(Animator.getAnimation(animationId));
	}

	private void clearAll()
	{
		playerAttackText.setText("");
		monsterAttackText.setText("");
		playerAbilityText.setText("");

		player.resetFlags();
		monster.resetFlags();

		player.removeEffects();
		monster.removeEffects();

		// monsterImage.setImageResource(0);
	}

	private void playerHitMonster(int localIndex, int attackTypeId)
	{
		moveAttackImage(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_WEAPON, player.equippedWeapon()),
			attackTypeId);

		// HIT: dmg amt, crit flag, stun flag
		// MISS: -1, roll, hit chance
		int[] tryHitResult = player.tryHit(localIndex);

		/*
		 * RelativeLayout.LayoutParams lp = new
		 * RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
		 * LayoutParams.WRAP_CONTENT); lp.leftMargin = left; lp.topMargin = top;
		 * 
		 * 
		 * playerAttackText.setLayoutParams(lp);
		 */

		player.updateNoAbilityInARow();

		String hitOrCrit = "hit";

		playerAttackText.setVisibility(View.VISIBLE);

		if (tryHitResult[0] > 0)
		{

			// stun
			if (tryHitResult[2] > 0)
			{
				appendLog("You stunned the monster!");
				monster.addStunCount(1);
			}
			// CRIT
			if (tryHitResult[1] > 0)
			{
				playerAttackText.setTextColor(0xFF2D0DCA);
				hitOrCrit = "CRIT";
			}

			int[] assignDmgResult = dealMonsterHitDamage(tryHitResult[0]);

			appendLog("You " + hitOrCrit + " " + monster.name() + " for " + assignDmgResult[0] + ".");

			animateMonster(Animator.SMALL_SHAKE);

			playerAttackText.setText("" + assignDmgResult[0]);

			playerAttackText.startAnimation(Animator.getAnimation(Animator.PLAYER_ATTACK_TEXT));

			SoundManager.playSound(DefinitionAttackTypes.ATTACK_SOUND_CLIPS[attackTypeId]);

			// class type mod AP on hit amount
			if (DefinitionClassType.CLASS_TYPE_AP_REGEN[DefinitionClasses.CLASS_TYPE[player.playerClass()]][2] != 0)
			{
				player.updateAP(DefinitionClassType.CLASS_TYPE_AP_REGEN[DefinitionClasses.CLASS_TYPE[player
					.playerClass()]][2]);
			}

		}
		else if (tryHitResult[0] == 0)
		{
			// block
			appendLog("You are blocking...");

			SoundManager.playSound(SoundManager.SOUND_TYPE_BLOCK);

			player.setBlockingFlag(tryHitResult[1]);
			endPlayerAttack();
		}

		else
		{
			appendLog("You missed!");

			SoundManager.playSound(SoundManager.SOUND_TYPE_MISS);

			player.updateMissesInARow();

			playerAttackText.setText("MISS!");
			playerAttackText.setTextSize(38);
			playerAttackText.setTextColor(0xFFFF0000);

			animateMonster(Animator.DODGE);
		}

		clearPlayerAttackTextDelay(1.8);
		endPlayerAttack();
	}

	private void dealAbilityDamage(Actor source, Actor target, int amt)
	{
		amt = Helper.getStatMod(source.mageDiff(), amt);

		if (amt > 0)
			amt = checkForCounterShieldAbsorb(target, amt);

		if (amt > 0 && source == player)
		{
			amt = checkForShieldAbsorb(amt);
		}

		if (amt > 0)
		{
			target.updateHP(amt);
			if (target.getReflectedDamage(amt) > 0)
			{
				dealReflectedDamage(target, source, target.getReflectedDamage(amt));
			}
		}

	}

	private void monsterAttackPlayer()
	{
		Log.d("combat", "monsterAttackPlayer()");

		waitingForDodge = false;
		dodgeEvent = new DodgeEvent();

		int[] tryHitResult = monster.tryHit();
		Log.d("combat", "monster.getHitDamage() = " + tryHitResult[0] + "," + tryHitResult[1] + "," + tryHitResult[2]);

		// give player a chance to dodge if possible
		int dodgeCheck = player.dodgeChance();

		int doDodge = 0;
		double time = DefinitionGlobal.DODGE_TIME + (player.luck() / 6);

		if (100 - Helper.randomInt(101) < dodgeCheck)
		{
			Log.d("combat", "player rolled to dodge");
			doDodge = 1;

			if (time < 1.0)
			{
				time = 1.0;
			}

			else if (time > 2.5)
				time = 2.5;
		}

		if (doDodge == 1)
		{
			disableCombatButtons();

			dodgeButton.setLayoutParams(Helper.getRandomButtonLayout(combatLayout));
			dodgeButton.setEnabled(true);
			dodgeButton.setVisibility(View.VISIBLE);

			dodgeEvent.dodgeID = Helper.randomInt(5000);
			randomDodgeID = dodgeEvent.dodgeID;

			dodgeEvent.damage = tryHitResult[0];
			dodgeEvent.critSuccess = tryHitResult[1];
			dodgeEvent.stunSuccess = tryHitResult[2];
			dodgeEvent.dodged = 0;
			waitingForDodge = true;

			checkDodgeDelay(time);

			monsterAttackText.setText("" + tryHitResult[1]);

			monsterAttackText.setTextSize(38);
			monsterAttackText.setTextColor(0xFF666699);

			double delayTime = 0;

			/*
			 * if (time >= 1.25) delayTime = 1.5;
			 * 
			 * else delayTime = 1.5;
			 * 
			 * clearMonsterAttackTextDelay(delayTime);
			 */
		}

		if (tryHitResult[0] >= 0 && doDodge == 0)
		{
			// monster hits with no chance of player dodging it

			monsterAttackText.setTextSize(49);
			monsterAttackText.setTextColor(0xFFCC0033);
			monsterAttackText.setText("" + tryHitResult[0]);

			dealPlayerHitDamage(tryHitResult);
			player.clearBlockingFlag();
		}
		else if (tryHitResult[0] < 0)
		{
			// monster missed
			appendLog("The monster missed!");

			monsterAttackText.setText("MISS!");

			monsterAttackText.setTextSize(38);
			monsterAttackText.setTextColor(0xFFFFFFFF);

			player.clearBlockingFlag();
			monster.updateMissesInARow();
		}

		Animation mTextAnim = Animator.getAnimation(Animator.MONSTER_ATTACK_TEXT);
		mTextAnim.setAnimationListener(new AnimationListener()
		{
			@Override
			public void onAnimationEnd(Animation arg0)
			{
				monsterAttackText.setText("");
			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{

			}

			@Override
			public void onAnimationStart(Animation animation)
			{

			}

		});
		monsterAttackText.startAnimation(mTextAnim);

		// clearMonsterAttackTextDelay(1.8);
	}

	private void clearMonsterAttackTextDelay(double d)
	{
		try
		{
			clearMonsterAttackTextHandler.removeCallbacks(clearMonsterAttackTextPost);
			clearMonsterAttackTextHandler.postDelayed(clearMonsterAttackTextPost, (long) (d * 1000));
		}
		catch (Exception e)
		{
			appendLog("thread did not sleep: " + e.getMessage());
			clearMonsterAttackTextPost.run();
		}
	}

	private Runnable clearMonsterAttackTextPost = new Runnable()
	{
		public void run()
		{
			clearMonsterAttackText();
		}
	};

	private void clearMonsterAttackText()
	{
		monsterAttackText.setText("");
	}

	private void dealPlayerHitDamage(int[] hitResult)
	{

		int hitAmount = hitResult[0];
		int critSuccess = hitResult[1];
		int stunSuccess = hitResult[2];

		if (hitAmount < 1)
		{
			hitAmount = 1;
		}

		String hitType = "hit";
		if (critSuccess == 1)
			hitType = "CRIT";

		String hitText = monster.name() + " " + hitType + " you for " + hitAmount;

		if (stunSuccess == 0)
		{
			hitText += ".";
		}
		else
		{
			hitText += " and Stunned you!";
			player.addStunCount(1);
		}
		appendLog(hitText);

		if (hitAmount > 0)
		{
			if (player.blocking())
			{
				int blockAmount = Helper.getPercentFromInt(player.blockingAmount(), hitAmount);
				appendLog("You blocked " + blockAmount + " damage.");
				hitAmount -= blockAmount;
			}
		}

		if (hitAmount > 0)
			hitAmount = checkForShieldAbsorb(hitAmount);

		if (hitAmount > 0)
			hitAmount = checkForCounterShieldAbsorb(player, hitAmount);

		if (hitAmount > 0)
		{
			player.updateHP(-hitAmount);
			if (player.getReflectedDamage(hitAmount) > 0)
				dealReflectedDamage(player, monster, player.getReflectedDamage(hitAmount));
		}

		if (hitAmount > 0)
		{
			SoundManager.playSound(SoundManager.TAKE_HIT);
			if (showImpact == 1)
			{
				combatBottomLayout.startAnimation(Animator.getAnimation(Animator.ATTACK_SHAKE));

				Animation hitAn = Animator.getAnimation(Animator.REDSCREEN);
				hitAn.setAnimationListener(new AnimationListener()
				{

					@Override
					public void onAnimationEnd(Animation arg0)
					{
						redHitImage.setVisibility(View.INVISIBLE);
					}

					@Override
					public void onAnimationRepeat(Animation animation)
					{

					}

					@Override
					public void onAnimationStart(Animation animation)
					{
						redHitImage.setVisibility(View.VISIBLE);
					}

				});
				redHitImage.startAnimation(hitAn);
			}
		}
	}

	private int checkForCounterShieldAbsorb(Actor target, int hitAmount)
	{
		int absorbAmount = target.getCounterAbsorbDamageAmount(hitAmount);
		if (absorbAmount > 0)
		{
			hitAmount = hitAmount - absorbAmount;

			if (hitAmount < 0)
			{
				hitAmount = Math.abs(hitAmount);
				appendLog(target.name() + " absorbed the attack.");
				hitAmount = 0;
			}
			else
			{
				appendLog(target.name() + " absorbed " + absorbAmount + " of " + hitAmount + " dmg.");
			}
		}

		return hitAmount;
	}

	private int checkForShieldAbsorb(int hitAmount)
	{
		// check for class type that takes ap damage from hp damage
		if (hitAmount > 0 && player.currentAP() > 0)
		{
			if (DefinitionClassType.CLASS_TYPE_AP_REGEN[DefinitionClasses.CLASS_TYPE[player.playerClass()]][5] == 1)
			{
				String absText = "";
				int newApAmt = player.currentAP() - hitAmount;

				if (newApAmt < 0)
				{
					hitAmount = Math.abs(newApAmt);
					absText = "AP Shield absorbed " + (newApAmt + hitAmount) + " dmg.";
					player.setCurrentAP(0);
				}
				else
				{
					absText = "AP Shield absorbed " + hitAmount + " dmg.";
					player.updateAP(-hitAmount);
					hitAmount = 0;
				}
				appendLog(absText);
			}
		}
		return hitAmount;
	}

	private void dealReflectedDamage(Actor source, Actor target, int dmg)
	{
		appendLog(source.name() + " reflected back " + dmg + " damage to " + target.name() + "!");
		target.updateHP(-dmg);
	}

	private int[] dealMonsterHitDamage(int hitValue)
	{
		// dmg amt applied
		int[] returnData = new int[2];

		System.out.println("assignMonsterDamage (" + hitValue + ")");

		if (inCombat == false)
		{
			return null;
		}

		returnData[0] = hitValue;

		hitValue = checkForCounterShieldAbsorb(monster, hitValue);

		if (hitValue > 0)
		{
			monster.updateHP(-hitValue);

			if (monster.getReflectedDamage(hitValue) > 0)
				dealReflectedDamage(monster, player, monster.getReflectedDamage(hitValue));
		}

		return returnData;
	}

	private void appendLog(String newLog)
	{
		// TODO update this log

		String ls = System.getProperty("line.separator");

		Log.d("combat", "appendLog: " + newLog);

		logView.append(ls);
		logView.append(Helper.getSpanString(newLog));

		logView.setMovementMethod(new ScrollingMovementMethod());
	}

	private void checkDodgeDelay(double d)
	{
		try
		{
			checkDodgeHandler.removeCallbacks(checkDodgePost);
			checkDodgeHandler.postDelayed(checkDodgePost, (long) (d * 1000));
		}
		catch (Exception e)
		{
			appendLog("thread did not sleep: " + e.getMessage());
			checkDodgePost.run();
		}
	}

	private Runnable checkDodgePost = new Runnable()
	{
		public void run()
		{
			if (waitingForDodge == false)
				return;

			else
			{
				dodgeButton.setEnabled(false);
				dodgeButton.setVisibility(View.INVISIBLE);
				waitingForDodge = false;
				dodgeEvent.dodged = 0;

				dealPlayerHitDamage(new int[]
				{ dodgeEvent.damage, dodgeEvent.critSuccess, dodgeEvent.stunSuccess });
				player.clearBlockingFlag();
			}
		}
	};

	private void animateAbility(ItemRune ability, final int playerOrMonster)
	{
		attackImage.setImageResource(ability.animationImageResource());
		attackImage.setVisibility(View.VISIBLE);
		Animation a =
			Animator
				.getAnimation((Integer) DefinitionRunes.runeData[ability.id()][DefinitionRunes.RUNE_ANIMATION_IDS][playerOrMonster]);
		a.setAnimationListener(new AnimationListener()
		{

			@Override
			public void onAnimationEnd(Animation arg0)
			{
				if (playerOrMonster == 0)
				{
					animateMonster(Animator.SMALL_SHAKE);
				}

				attackImage.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{

			}

			@Override
			public void onAnimationStart(Animation animation)
			{

			}

		});
		attackImage.startAnimation(a);

	}

	private void moveAttackImage(int imageResource, int attackTypeId)
	{
		attackImage.setImageResource(imageResource);
		attackImage.setVisibility(View.VISIBLE);

		Log.d("animator", "requesting animation for attackType " + attackTypeId);
		Animation a = Animator.getAttackAnimation(DefinitionAttackTypes.ATTACK_TYPE_ANIMATION[attackTypeId]);
		a.setAnimationListener(new AnimationListener()
		{

			@Override
			public void onAnimationEnd(Animation arg0)
			{
				attackImage.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{
			}

			@Override
			public void onAnimationStart(Animation animation)
			{
			}

		});
		attackImage.startAnimation(a);

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

						// b.putSerializable("OwnedItems", OwnedItems);
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

	private DodgeEvent dodgeEvent;
	private int randomDodgeID = -1;

	private Handler startMonsterAttackHandler = new Handler();
	private Handler checkDodgeHandler = new Handler();
	private Handler clearMonsterAttackTextHandler = new Handler();
	private Handler clearPlayerAttackTextHandler = new Handler();
	private Handler advanceTurnHandler = new Handler();
	private Handler findObjectHandler = new Handler();
	private Handler clearPlayerTrapTextHandler = new Handler();
	public Handler explosionWaitHandler = new Handler();

	private class DodgeEvent
	{
		int dodged = 0;
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
	Button findObjectButton;

	ImageView monsterImage;
	ImageView attackImage;

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);

		menu.add(0, DefinitionGlobal.TUTORIAL, 0, "Tutorial");
		// item.setIcon(R.drawable.paint);

		menu.add(0, DefinitionGlobal.SETTINGS, 0, "Settings");

		menu.add(0, DefinitionGlobal.ABOUT, 0, "About");

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
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

	private void showTutorial()
	{

	}

	private void showAbout()
	{
		aDialog.show();
	}

	private void setupAboutDialog()
	{
		AlertDialog.Builder aboutDialog = new AlertDialog.Builder(this);

		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.about, (ViewGroup) findViewById(R.id.layout_root));

		aboutDialog.setView(layout);

		aDialog = aboutDialog.create();
	}

	private void setupSettingsDialog()
	{
		LayoutInflater inflater = getLayoutInflater();
		final View settingsView = inflater.inflate(R.layout.settings, (ViewGroup) getCurrentFocus());
		final ToggleButton toggleMusicButton = (ToggleButton) settingsView.findViewById(R.id.toggleMusicButton);
		final ToggleButton toggleSoundsButton = (ToggleButton) settingsView.findViewById(R.id.toggleSoundsButton);
		final ToggleButton toggleImpactButton = (ToggleButton) settingsView.findViewById(R.id.toggleImpactButton);

		AlertDialog.Builder builder = new AlertDialog.Builder(ControllerCombat.this);
		builder.setMessage("Settings").setCancelable(false)
			.setPositiveButton("Done", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					dialog.dismiss();
				}
			});

		builder.setView(settingsView);

		settingsDialog = builder.create();

		wasAppNavigation = true;

		toggleMusicButton.setChecked(SoundManager.playingMusic());
		toggleMusicButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (toggleMusicButton.isChecked())
				{
					SoundManager.setPlayMusic(true);
				}
				else
				{
					SoundManager.setPlayMusic(false);
				}
				DBHandler.updateSoundPrefs(ControllerCombat.this);
			}
		});

		toggleSoundsButton.setChecked(SoundManager.playingSounds());
		toggleSoundsButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (toggleSoundsButton.isChecked())
				{
					SoundManager.setPlaySounds(true);
				}
				else
				{
					SoundManager.setPlaySounds(false);
				}
				DBHandler.updateSoundPrefs(ControllerCombat.this);
			}
		});

		int impactPref = DBHandler.getImpactPref(getApplicationContext());
		boolean b = false;
		if (impactPref == 1)
			b = true;
		toggleImpactButton.setChecked(b);

		toggleImpactButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				DBHandler.updateImpactPref(ControllerCombat.this, toggleImpactButton.isChecked());
				if (toggleImpactButton.isChecked())
					showImpact = 1;

				else
					showImpact = 0;
			}
		});
	}

	private void showSettings()
	{
		settingsDialog.show();
	}

	private boolean wasAppNavigation = false;
	AlertDialog settingsDialog;
	AlertDialog aDialog;

}
