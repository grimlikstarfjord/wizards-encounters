package com.alderangaming.wizardsencounters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ControllerAbilities extends Activity
{

	private Player player;
	// private OwnedItems ownedItems;
	private int numEquipped = 0;
	private int trinketRuneId = -1;
	private boolean trinketRuneEquipped = false;
	private Dialog hDialog;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Bundle b = getIntent().getExtras();

		if (!DBHandler.isOpen(getApplicationContext()))
			DBHandler.open(getApplicationContext());

		player = (Player) b.getSerializable("player");
		// ownedItems = (OwnedItems) b.getSerializable("ownedItems");

		setContentView(R.layout.abilities);
		setupViews();
		updateViews();
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
		helpTextView.setText(R.string.abilitiesHelpText);

		aboutDialog.setView(layout);

		hDialog = aboutDialog.create();
	}

	private void setupViews()
	{
		setupHelpDialog();
		Button helpButton = (Button) findViewById(R.id.abilitiesHelpButton);
		helpButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				showHelp();
			}

		});
		backButton = (Button) findViewById(R.id.abilitiesBackButton);
		backButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (DBHandler.updateActiveAbilities(player.getActiveAbilities(), player.playerID()))
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

		abilitiesLabel = (TextView) findViewById(R.id.abilitiesScreenLabel);
		abilitiesLabel.setText(player.name() + " - Abilities");

		equippedLabel = (TextView) findViewById(R.id.abilitiesScreenEquippedText);

		abilitiesList = (ListView) findViewById(R.id.abilitiesScreenListView);
		abilitiesList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3)
			{
				// move item from active abilities back to rune
				player.removeActiveAbility(activeAbilities[position]);
				Log.d("equipthing", "removing ability id " + activeAbilities[position]);

				if (activeAbilities[position] == trinketRuneId)
					trinketRuneEquipped = false;

				updateViews();
			}

		});

		runesList = (ListView) findViewById(R.id.abilityRuneList);
		runesList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3)
			{

				if (numEquipped >= DefinitionGlobal.DEFAULT_MAX_ABILITIES_ALLOWED)
				{
					return;
				}

				player.addActiveAbility(runes.get(position).id());

				if (runes.get(position).id() == trinketRuneId)
					trinketRuneEquipped = true;

				updateViews();
			}

		});
	}

	private void updateViews()
	{
		updateEquippedList();
		updateRuneList();

		numEquipped = player.getActiveAbilities().length;
		equippedLabel.setText("Equipped Abilities (" + numEquipped + "/"
			+ DefinitionGlobal.DEFAULT_MAX_ABILITIES_ALLOWED + ")");
	}

	public void updateRuneList()
	{
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

		runes = OwnedItems.getRunesForClass(player);

		for (int i = 0; i < runes.size(); i++)
		{
			String name = runes.get(i).name();

			String apcost = "" + runes.get(i).apCost();

			String description = runes.get(i).description();

			rows.add(Helper.createMap(name, apcost, description));
		}

		if (!trinketRuneEquipped && player.equippedArmorSlot4() >= 0
			&& DefinitionArmor.ARMOR_GRANTS_RUNE_ID[player.equippedArmorSlot4()] >= 0)
		{
			trinketRuneId = DefinitionArmor.ARMOR_GRANTS_RUNE_ID[player.equippedArmorSlot4()];

			String name = (String) DefinitionRunes.runeData[trinketRuneId][DefinitionRunes.RUNE_NAMES][0];

			String apcost = "" + DefinitionRunes.runeData[trinketRuneId][DefinitionRunes.RUNE_AP_COST][0];

			String description = (String) DefinitionRunes.runeData[trinketRuneId][DefinitionRunes.RUNE_DESCRIPTION][0];

			runes.add(new ItemRune(DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY, trinketRuneId, getApplicationContext()));
			rows.add(Helper.createMap(name, apcost, description));
		}

		String[] fromKeys = new String[]
		{ "Name", "APCost", "Description" };
		int[] toIds = new int[]
		{ R.id.abilityListName, R.id.abilityListCost, R.id.abilityListDescription };

		runesList.setAdapter(new SimpleAdapter(this, rows, R.layout.abilitylistitem, fromKeys, toIds));

		runesList.invalidate();
	}

	private void updateEquippedList()
	{
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

		activeAbilities = player.getActiveAbilities();

		Log.d("equipthing", "player activeAbilities size is " + activeAbilities.length);

		for (int i = 0; i < activeAbilities.length; i++)
		{
			String name = (String) DefinitionRunes.runeData[activeAbilities[i]][DefinitionRunes.RUNE_NAMES][0];

			String apcost = "" + DefinitionRunes.runeData[activeAbilities[i]][DefinitionRunes.RUNE_AP_COST][0];

			String description =
				(String) DefinitionRunes.runeData[activeAbilities[i]][DefinitionRunes.RUNE_DESCRIPTION][0];

			rows.add(Helper.createMap(name, apcost, description));
		}

		String[] fromKeys = new String[]
		{ "Name", "APCost", "Description" };
		int[] toIds = new int[]
		{ R.id.abilityListName, R.id.abilityListCost, R.id.abilityListDescription };

		abilitiesList.setAdapter(new SimpleAdapter(this, rows, R.layout.abilitylistitem, fromKeys, toIds));

		abilitiesList.invalidate();
	}

	Button backButton;
	TextView abilitiesLabel;
	TextView equippedLabel;
	ListView abilitiesList;
	ListView runesList;
	ArrayList<ItemRune> runes = new ArrayList<ItemRune>();
	int[] activeAbilities;
}
