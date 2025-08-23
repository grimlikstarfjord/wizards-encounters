package com.alderangaming.wizardsencounters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ControllerInventory extends Activity
{

	// private OwnedItems ownedItems;
	private Player player;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		if (!DBHandler.isOpen(getApplicationContext()))
			DBHandler.open(getApplicationContext());

		Bundle b = getIntent().getExtras();

		// ownedItems = (OwnedItems) b.getSerializable("ownedItems");
		player = (Player) b.getSerializable("player");

		setContentView(R.layout.inventory);

		setupViews();

		updateViews();
	}

	private void setupViews()
	{
		inventoryLabel = (TextView) findViewById(R.id.inventoryLabel);
		inventoryLabel.setText(player.name() + " - Inventory");

		dmgText = (TextView) findViewById(R.id.inventoryDmgText);
		dodgeText = (TextView) findViewById(R.id.inventoryDodgeText);
		hpText = (TextView) findViewById(R.id.inventoryHPText);
		critText = (TextView) findViewById(R.id.inventoryCritText);
		stunText = (TextView) findViewById(R.id.inventoryStunText);
		hitText = (TextView) findViewById(R.id.inventoryHitText);
		execText = (TextView) findViewById(R.id.inventoryExecText);
		reacText = (TextView) findViewById(R.id.inventoryReactionText);
		knowText = (TextView) findViewById(R.id.inventoryKnowledgeText);
		mageText = (TextView) findViewById(R.id.inventoryMageloreText);
		luckText = (TextView) findViewById(R.id.inventoryLuckText);

		backButton = (Button) findViewById(R.id.inventoryBackButton);
		backButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (DBHandler.isOpen(getApplicationContext()))
				{
					if (DBHandler.updatePlayer(player))
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
		forwardButton = (Button) findViewById(R.id.inventoryForwardButton);
		forwardButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				if (DBHandler.isOpen(getApplicationContext()))
					DBHandler.close();

				Intent i =
					new Intent(getApplicationContext(), com.alderangaming.wizardsencounters.ControllerAbilities.class);
				Bundle b = new Bundle();

				b.putSerializable("player", player);
				// b.putSerializable("ownedItems", ownedItems);

				i.putExtras(b);

				startActivityForResult(i, 2003);
			}

		});

		helmButton = (ImageButton) findViewById(R.id.inventoryHelmButton);
		helmButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (DBHandler.isOpen(getApplicationContext()))
					DBHandler.close();

				Intent i =
					new Intent(getApplicationContext(), com.alderangaming.wizardsencounters.ControllerEquip.class);
				Bundle b = new Bundle();

				// b.putSerializable("ownedItems", ownedItems);
				b.putSerializable("player", player);
				b.putIntArray("slot", DefinitionGlobal.EQUIP_SLOT_HELM);
				b.putInt("type", DefinitionGlobal.ITEM_TYPE_ARMOR);
				i.putExtras(b);

				startActivityForResult(i, 2001);
			}
		});
		weaponButton = (ImageButton) findViewById(R.id.inventoryWeaponButton);
		weaponButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (DBHandler.isOpen(getApplicationContext()))
					DBHandler.close();

				Intent i =
					new Intent(getApplicationContext(), com.alderangaming.wizardsencounters.ControllerEquip.class);
				Bundle b = new Bundle();

				// b.putSerializable("ownedItems", ownedItems);
				b.putSerializable("player", player);
				b.putInt("slot", DefinitionGlobal.EQUIP_SLOT_WEAPON[0]);
				b.putInt("type", DefinitionGlobal.ITEM_TYPE_WEAPON);
				i.putExtras(b);

				startActivityForResult(i, 2001);
			}
		});
		torsoButton = (ImageButton) findViewById(R.id.inventoryChestButton);
		torsoButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (DBHandler.isOpen(getApplicationContext()))
					DBHandler.close();

				Intent i =
					new Intent(getApplicationContext(), com.alderangaming.wizardsencounters.ControllerEquip.class);
				Bundle b = new Bundle();

				// b.putSerializable("ownedItems", ownedItems);
				b.putSerializable("player", player);
				b.putInt("slot", DefinitionGlobal.EQUIP_SLOT_CHEST[0]);
				b.putInt("type", DefinitionGlobal.ITEM_TYPE_ARMOR);
				i.putExtras(b);

				startActivityForResult(i, 2001);
			}
		});
		shoesButton = (ImageButton) findViewById(R.id.inventoryShoesButton);
		shoesButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (DBHandler.isOpen(getApplicationContext()))
					DBHandler.close();

				Intent i =
					new Intent(getApplicationContext(), com.alderangaming.wizardsencounters.ControllerEquip.class);
				Bundle b = new Bundle();

				// b.putSerializable("ownedItems", ownedItems);
				b.putSerializable("player", player);
				b.putInt("slot", DefinitionGlobal.EQUIP_SLOT_SHOES[0]);
				b.putInt("type", DefinitionGlobal.ITEM_TYPE_ARMOR);
				i.putExtras(b);

				startActivityForResult(i, 2001);
			}
		});
		trinketButton = (ImageButton) findViewById(R.id.inventoryTrinketButton);
		trinketButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (DBHandler.isOpen(getApplicationContext()))
					DBHandler.close();

				Intent i =
					new Intent(getApplicationContext(), com.alderangaming.wizardsencounters.ControllerEquip.class);
				Bundle b = new Bundle();

				// b.putSerializable("ownedItems", ownedItems);
				b.putSerializable("player", player);
				b.putInt("slot", DefinitionGlobal.EQUIP_SLOT_TRINKET[0]);
				b.putInt("type", DefinitionGlobal.ITEM_TYPE_ARMOR);
				i.putExtras(b);

				startActivityForResult(i, 2001);
			}
		});
		item1Button = (ImageButton) findViewById(R.id.inventoryItem1Button);
		item1Button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (DBHandler.isOpen(getApplicationContext()))
					DBHandler.close();

				Intent i =
					new Intent(getApplicationContext(), com.alderangaming.wizardsencounters.ControllerEquip.class);
				Bundle b = new Bundle();

				// b.putSerializable("ownedItems", ownedItems);
				b.putSerializable("player", player);
				b.putInt("slot", DefinitionGlobal.EQUIP_SLOT_ITEM[0]);
				b.putInt("type", DefinitionGlobal.ITEM_TYPE_ITEM);
				i.putExtras(b);

				startActivityForResult(i, 2001);
			}
		});

		inventoryItem1Text = (TextView) findViewById(R.id.inventoryItem1ChargesText);
		inventoryItem1Text.setText("No Item 1");

		inventoryItem2Text = (TextView) findViewById(R.id.inventoryItem2ChargesText);
		inventoryItem2Text.setText("No Item 2");

		item2Button = (ImageButton) findViewById(R.id.inventoryItem2Button);
		item2Button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (DBHandler.isOpen(getApplicationContext()))
					DBHandler.close();

				Intent i =
					new Intent(getApplicationContext(), com.alderangaming.wizardsencounters.ControllerEquip.class);
				Bundle b = new Bundle();

				// b.putSerializable("ownedItems", ownedItems);
				b.putSerializable("player", player);
				b.putInt("slot", DefinitionGlobal.EQUIP_SLOT_ITEM[1]);
				b.putInt("type", DefinitionGlobal.ITEM_TYPE_ITEM);
				i.putExtras(b);

				startActivityForResult(i, 2001);
			}
		});

	}

	private void updateViews()
	{
		helmButton.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_ARMOR,
			player.equippedArmorSlot1()));

		weaponButton.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_WEAPON,
			player.equippedWeapon()));

		torsoButton.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_ARMOR,
			player.equippedArmorSlot2()));

		shoesButton.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_ARMOR,
			player.equippedArmorSlot3()));

		trinketButton.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_ARMOR,
			player.equippedArmorSlot4()));

		item1Button.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_ITEM,
			player.equippedItemSlot1()));

		item2Button.setImageResource(OwnedItems.getItemImage(DefinitionGlobal.ITEM_TYPE_ITEM,
			player.equippedItemSlot2()));

		if (player.equippedItemSlot1() >= 0)
			inventoryItem1Text.setText(OwnedItems.getChargesOfItemId(player.equippedItemSlot1())[0] + "/"
				+ OwnedItems.getChargesOfItemId(player.equippedItemSlot1())[1]);
		else
			inventoryItem1Text.setText("No Item 1");

		if (player.equippedItemSlot2() >= 0)
			inventoryItem2Text.setText(OwnedItems.getChargesOfItemId(player.equippedItemSlot2())[0] + "/"
				+ OwnedItems.getChargesOfItemId(player.equippedItemSlot2())[1]);
		else
			inventoryItem2Text.setText("No Item 2");

		dmgText.setText("Hit Dmg: " + player.getDamageRange()[0] + "-" + player.getDamageRange()[1]);
		dodgeText.setText("Dodge: " + player.dodgeChance() + "%");
		hpText.setText("HP: " + player.maxHP() + "   AP: " + player.maxAP());
		critText.setText("Crit: " + player.critChance() + "%");
		stunText.setText("Stun: " + player.stunChance() + "%");
		hitText.setText("Hit: " + player.hitChance() + "%");
		updateStatText();
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode)
		{
			case 2001:
			{
				if (!DBHandler.isOpen(getApplicationContext()))
					DBHandler.open(getApplicationContext());

				player = (Player) data.getExtras().getSerializable("player");
				// ownedItems = (OwnedItems)
				// data.getExtras().getSerializable("ownedItems");

				updateViews();

				break;
			}

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (DBHandler.isOpen(getApplicationContext()))
				DBHandler.close();

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

	TextView inventoryLabel;
	TextView inventoryItem1Text;
	TextView inventoryItem2Text;

	TextView dmgText;
	TextView dodgeText;
	TextView hpText;
	TextView critText;
	TextView stunText;
	TextView hitText;
	TextView execText;
	TextView reacText;
	TextView knowText;
	TextView mageText;
	TextView luckText;

	Button backButton;
	Button forwardButton;
	ImageButton helmButton;
	ImageButton weaponButton;
	ImageButton torsoButton;
	ImageButton shoesButton;
	ImageButton trinketButton;
	ImageButton item1Button;
	ImageButton item2Button;

}
