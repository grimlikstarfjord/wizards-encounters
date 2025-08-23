package com.alderangaming.wizardsencounters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.ToggleButton;

public class Start extends Activity
{
	// private OwnedItems ownedItems = null;
	private Player player = null;
	private boolean wasAppNavigation = false;

	private ArrayList<Player> savedPlayers;
	private int selectedPlayerIndex = -1;

	private ProgressDialog progressDialog;
	private boolean waitingForLoad = false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		waitingForLoad = true;
		
		Log.d("applife","onCreate...");

		super.onCreate(savedInstanceState);

		new LoadViewTask().execute();

	}

	private class LoadViewTask extends AsyncTask<Void, Integer, Void>
	{
		// before running code in separate thread
		@Override
		protected void onPreExecute()
		{
			// Create a new progress dialog
			progressDialog = ProgressDialog.show(Start.this, "Loading Wizards Encounters...", "", false, false);
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			try
			{
				// Get the current thread's token
				synchronized (this)
				{
					if (!DBHandler.isOpen(Start.this))
					{
						try
						{
							openDB();
						}
						catch (Exception e)
						{

						}
					}

					SoundManager.initSoundManager(Start.this);

					BackgroundManager.setupBackgrounds();

					publishProgress(0);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}

		// Update the progress
		@Override
		protected void onProgressUpdate(Integer... values)
		{
			// set the current progress of the progress dialog
			progressDialog.setProgress(values[0]);
		}

		// after executing the code in the thread
		@Override
		protected void onPostExecute(Void result)
		{
			// close the progress dialog
			progressDialog.dismiss();
			// initialize the View
			waitingForLoad = false;
			setContentView(R.layout.mainmenu);
			setupViews();
			onStartActions();
			SoundManager.playMusic(SoundManager.SOUND_TYPE_MUSIC_MAIN_MENU, true, Start.this);
		}
	}

	@Override
	public void onStop()
	{
		super.onStop();
		
		Log.d("applife","onStop...");

		if (!wasAppNavigation)
			SoundManager.stopAll();
	}

	public void onStartActions()
	{
		wasAppNavigation = false;

		updateViews();

		if (!DBHandler.isOpen(this))
		{
			if (openDB())
			{
				DBHandler.getSoundPrefs(this);
				loadOwnedItems();
				loadSavedPlayers();
			}
		}
		else
		{
			DBHandler.getSoundPrefs(this);
			loadOwnedItems();
			loadSavedPlayers();
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();
		Log.d("applife","onResume...");

	}

	@Override
	public void onStart()
	{
		super.onStart();
		
		Log.d("applife","onStart...");
		
		if (waitingForLoad)
			return;
		
		else
			onStartActions();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		Log.d("applife","onDestory...");
		SoundManager.stopAll();
	}

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

		AlertDialog.Builder builder = new AlertDialog.Builder(Start.this);
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
				DBHandler.updateSoundPrefs(Start.this);
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
				DBHandler.updateSoundPrefs(Start.this);
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
				DBHandler.updateImpactPref(Start.this, toggleImpactButton.isChecked());
			}
		});
	}

	private void showSettings()
	{
		settingsDialog.show();
	}

	private boolean openDB()
	{
		if (DBHandler.open(this))
			return (DBHandler.checkTables(this));

		return false;
	}

	private void reopenDB()
	{
		if (!DBHandler.isOpen(this))
		{
			DBHandler.open(this);
		}
	}

	private void closeDB()
	{
		if (!DBHandler.isOpen(this))
			return;

		DBHandler.close();
	}

	private void loadOwnedItems()
	{
		// ownedItems = new OwnedItems();

		StoreItem[] owned = DBHandler.pullOwnedItems(this);
		OwnedItems.addOwnedItems(owned);

		OwnedItems.setGold(DBHandler.loadGlobalStats()[0]);

		OwnedItems.addOwnedItems(Helper.getStartingPlayerClasses(this));

		// runes added to store when class is selected for a new player
		// TODO weapons and armor should be handled the same way
	}

	private void updateViews()
	{
		hubStoreButton.setEnabled(false);
		hubStartButton.setEnabled(false);
		hubInventoryButton.setEnabled(false);
	}

	private void setupViews()
	{
		setupSettingsDialog();
		setupAboutDialog();

		hubStoreButton = (Button) findViewById(R.id.hubStoreButton);
		hubStoreButton.setEnabled(false);
		hubStoreButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{

				wasAppNavigation = true;

				Intent i =
					new Intent(getApplicationContext(), com.alderangaming.wizardsencounters.ControllerStore.class);
				Bundle b = new Bundle();

				// b.putSerializable("ownedItems", ownedItems);
				b.putSerializable("savedPlayers", savedPlayers);
				b.putInt("selectedPlayerIndex", selectedPlayerIndex);

				i.putExtras(b);

				closeDB();
				startActivityForResult(i, 4000);
			}
		});

		hubStartButton = (Button) findViewById(R.id.hubStartButton);
		hubStartButton.setEnabled(false);
		hubStartButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				wasAppNavigation = true;

				SoundManager.stopAllMusic();

				player = savedPlayers.get(selectedPlayerIndex);

				OwnedItems.addDefaultItems(DefinitionClasses.CLASS_PRELOADED_ITEMS[player.playerClass()],
					getApplicationContext());
				player.setDefaultEquippedItems(DefinitionClasses.CLASS_PRELOADED_ITEMS[player.playerClass()]);				

				Intent i =
					new Intent(getApplicationContext(), com.alderangaming.wizardsencounters.ControllerRoundStart.class);
				Bundle b = new Bundle();

				// b.putSerializable("ownedItems", ownedItems);
				b.putSerializable("player", player);

				i.putExtras(b);

				closeDB();
				startActivityForResult(i, 3000);
			}

		});

		Button hubNewButton = (Button) findViewById(R.id.hubNewButton);
		hubNewButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				wasAppNavigation = true;

				Intent i =
					new Intent(getApplicationContext(), com.alderangaming.wizardsencounters.ControllerMakeNewChar.class);
				Bundle b = new Bundle();

				// b.putSerializable("ownedItems", ownedItems);

				i.putExtras(b);

				Log.d(
					"playerstore",
					"Start before intent: getAllClassesInStore: "
						+ OwnedItems.getAllItemsOfType(DefinitionGlobal.ITEM_TYPE_PLAYER_CLASS).size());

				closeDB();
				startActivityForResult(i, 1000);
			}

		});

		hubInventoryButton = (Button) findViewById(R.id.hubInventoryButton);
		hubInventoryButton.setEnabled(false);
		hubInventoryButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				wasAppNavigation = true;

				player = savedPlayers.get(selectedPlayerIndex);

				player.setDefaultEquippedItems(DefinitionClasses.CLASS_PRELOADED_ITEMS[player.playerClass()]);
				OwnedItems.addDefaultItems(DefinitionClasses.CLASS_PRELOADED_ITEMS[player.playerClass()],
					getApplicationContext());

				Intent i =
					new Intent(getApplicationContext(), com.alderangaming.wizardsencounters.ControllerInventory.class);
				Bundle b = new Bundle();

				// b.putSerializable("ownedItems", ownedItems);
				b.putSerializable("player", player);

				i.putExtras(b);

				closeDB();
				startActivityForResult(i, 2000);

			}

		});

		savedPlayerList = (ListView) findViewById(R.id.hubPlayerList);

	}

	private void loadSavedPlayers()
	{
		savedPlayers = DBHandler.loadSavedPlayers();
		if (savedPlayers == null)
			return;

		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

		for (int a = 0; a < savedPlayers.size(); a++)
		{
			rows.add(Helper.createPlayerMap(savedPlayers.get(a).name(),
				DefinitionClasses.CLASS_NAMES[savedPlayers.get(a).playerClass()], "Round: "
					+ savedPlayers.get(a).currentRound(), "Rank: " + savedPlayers.get(a).rank()));
		}

		String[] fromKeys = new String[]
		{ "Name", "Class", "Round", "Rank" };
		int[] toIds = new int[]
		{ R.id.playerListName, R.id.playerListClass, R.id.playerListRound, R.id.playerListRank };

		savedPlayerList.setAdapter(new SimpleAdapter(this, rows, R.layout.playerlistitem, fromKeys, toIds));

		savedPlayerList.invalidate();

		savedPlayerList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3)
			{
				for (int a = 0; a < parent.getChildCount(); a++)
				{
					parent.getChildAt(a).setBackgroundColor(Color.TRANSPARENT);
				}

				view.setBackgroundColor(Color.BLUE);

				selectedPlayerIndex = position;
				hubStartButton.setEnabled(true);
				hubInventoryButton.setEnabled(true);
				hubStoreButton.setEnabled(true);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode)
		{
		// return from make new
			case 1000:
			{
				reopenDB();

				// loadSavedPlayers();
				// loadOwnedItems();
				break;
			}
			// return from inventory
			case 2000:
			{
				reopenDB();

				// loadSavedPlayers();
				// loadOwnedItems();

				break;
			}
			// return from combat
			case 3000:
			{
				reopenDB();
				// loadSavedPlayers();
				// loadOwnedItems();

				SoundManager.playMusic(SoundManager.SOUND_TYPE_MUSIC_MAIN_MENU, true, this);

				break;
			}
			// return from store
			case 4000:
			{
				reopenDB();
				// loadSavedPlayers();
				// loadOwnedItems();

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

			SoundManager.stopAll();

			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	ListView savedPlayerList;
	Button hubStartButton;
	Button hubInventoryButton;
	Button hubStoreButton;
	AlertDialog settingsDialog;
	AlertDialog aDialog;
}