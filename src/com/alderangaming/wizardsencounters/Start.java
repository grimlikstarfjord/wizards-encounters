package com.alderangaming.wizardsencounters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Start extends Activity {
	private Dialog hDialog;

	// private OwnedItems ownedItems = null;
	private Player player = null;
	private boolean wasAppNavigation = false;

	private ArrayList<Player> savedPlayers;
	private int selectedPlayerIndex = -1;

	private boolean waitingForLoad = false;
	private int buyGoldChancesLeft = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		waitingForLoad = true;

		// setupBillingHandler();

		// BillingHelper.setCompletedHandler(mTransactionHandler);

		Log.d("applife", "onCreate...");

		super.onCreate(savedInstanceState);

		new LoadViewTask().execute();

	}

	// private void setupBillingHandler() {
	// /* ANDROID BILLING */
	// mTransactionHandler = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// try {
	// Log.i(TAG, "Transaction complete");
	// Log.i(TAG, "Transaction status: " +
	// BillingHelper.latestPurchase.purchaseState);
	// Log.i(TAG, "Item attempted purchase is: " +
	// BillingHelper.latestPurchase.productId);

	// if (BillingHelper.latestPurchase.isPurchased()) {
	// Toast.makeText(Start.this, "PURCHASE COMPLETE!", Toast.LENGTH_LONG).show();
	// OwnedItems.updateGold(1000);
	// DBHandler.updateGlobalStats(OwnedItems.gold());

	// updateViews();
	// } else {
	// Toast.makeText(Start.this, "PURCHASE FAILED!", Toast.LENGTH_LONG).show();
	// }
	// } catch (Exception e) {
	// Toast.makeText(Start.this, "Could not complete transaction.",
	// Toast.LENGTH_LONG).show();
	// }

	// };
	// };
	// }

	private class LoadViewTask extends AsyncTask<Void, Integer, Void> {
		// before running code in separate thread
		@Override
		protected void onPreExecute() {

			setContentView(R.layout.loading);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				// Get the current thread's token
				synchronized (this) {
					long startTime = System.currentTimeMillis();

					if (!DBHandler.isOpen(Start.this)) {
						try {
							openDB();
						} catch (Exception e) {
							Log.d("loading", "failed to openDB: " + e.toString());
						}
					}

					SoundManager.initSoundManager(Start.this);

					BackgroundManager.setupBackgrounds();

					// try to show loading screen for 5 seconds
					Log.d("loading", "times:  " + startTime + ", " + System.currentTimeMillis());
					if (System.currentTimeMillis() > 5000 + startTime) {
						// already waited long enough
					} else {
						long waitTime = 5000 - (System.currentTimeMillis() - startTime);
						if (waitTime < 0)
							waitTime = 1;

						Thread.sleep(waitTime);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		// Update the progress
		@Override
		protected void onProgressUpdate(Integer... values) {
			// set the current progress of the progress dialog

		}

		// after executing the code in the thread
		@Override
		protected void onPostExecute(Void result) {
			// close the progress dialog

			// initialize the View
			waitingForLoad = false;

			// startService(new Intent(mContext, BillingService.class));

			setContentView(R.layout.mainmenu);
			setupViews();
			onStartActions();
			SoundManager.playMusic(SoundManager.SOUND_TYPE_MUSIC_MAIN_MENU, true, Start.this);
		}
	}

	@Override
	public void onStop() {
		super.onStop();

		Log.d("applife", "onStop...");

		if (!wasAppNavigation)
			SoundManager.stopAll();
	}

	public void onStartActions() {
		wasAppNavigation = false;

		if (!DBHandler.isOpen(this)) {
			if (openDB()) {
				DBHandler.getSoundPrefs(this);

				int impactPref = DBHandler.getImpactPref(getApplicationContext());
				boolean b = false;
				if (impactPref == 1)
					b = true;

				SoundManager.setShowImpact(b);

				loadOwnedItems();
				loadSavedPlayers();
			}
		} else {
			DBHandler.getSoundPrefs(this);
			int impactPref = DBHandler.getImpactPref(getApplicationContext());
			boolean b = false;
			if (impactPref == 1)
				b = true;

			SoundManager.setShowImpact(b);

			loadOwnedItems();
			loadSavedPlayers();
		}

		updateViews();

		// doDropTest();
	}

	private void doDropTest() {
		for (int a = 1; a < 21; a++) {
			for (int b = 0; b < 6; b++)
				Helper.getRandomDropsForRound(a, b, getApplicationContext());
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d("applife", "onResume...");

	}

	@Override
	public void onStart() {
		super.onStart();

		/* AMAZON BILLING */
		// PurchaseObserver purchaseObserver = new PurchaseObserver(this);
		// PurchasingManager.registerObserver(purchaseObserver);

		Log.d("applife", "onStart...");

		if (DefinitionGlobal.BETA_BUILD) {
			Toast
					.makeText(
							Start.this,
							"Thanks for trying out the beta version! Please report bugs/suggestions/problems to alderangaming@gmail.com",
							Toast.LENGTH_LONG)
					.show();
		}

		if (waitingForLoad)
			return;

		else
			onStartActions();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		Log.d("applife", "onDestory...");
		SoundManager.stopAll();
	}

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

		closeDB();

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

	private void showHelp() {
		hDialog.show();
	}

	private void setupHelpDialog() {
		AlertDialog.Builder aboutDialog = new AlertDialog.Builder(this);

		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.help, (ViewGroup) findViewById(R.id.helpRoot));
		TextView helpTextView = (TextView) layout.findViewById(R.id.helpText);
		Button doneButton = (Button) layout.findViewById(R.id.helpDoneButton);
		doneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					hDialog.dismiss();
				} catch (Exception e) {
					// who cares I have more important crap in my life to deal
					// with
				}
			}

		});
		helpTextView.setText(R.string.mainmenuHelpText);

		aboutDialog.setView(layout);

		hDialog = aboutDialog.create();
	}

	private void setupSettingsDialog() {
		LayoutInflater inflater = getLayoutInflater();
		final View settingsView = inflater.inflate(R.layout.settings, (ViewGroup) getCurrentFocus());
		final SeekBar seekMusic = (SeekBar) settingsView.findViewById(R.id.seekMusicVolume);
		final SeekBar seekSounds = (SeekBar) settingsView.findViewById(R.id.seekSoundVolume);
		toggleImpactButton = (ToggleButton) settingsView.findViewById(R.id.toggleImpactButton);
		final ToggleButton toggleMusicButton = (ToggleButton) settingsView.findViewById(R.id.toggleMusicButton);

		AlertDialog.Builder builder = new AlertDialog.Builder(Start.this);
		builder.setMessage("Settings").setCancelable(false)
				.setPositiveButton("Done", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});

		builder.setView(settingsView);

		settingsDialog = builder.create();

		wasAppNavigation = true;

		toggleMusicButton.setChecked(SoundManager.playingMusic());
		seekMusic.setProgress((int) (SoundManager.getMusicVolume() * 100));
		seekSounds.setProgress((int) (SoundManager.getSoundVolume() * 100));

		seekMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				SoundManager.setMusicVolume(progress / 100f);
				DBHandler.updateSoundPrefs(Start.this);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

		seekSounds.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				SoundManager.setSoundVolume(progress / 100f);
				DBHandler.updateSoundPrefs(Start.this);
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
				DBHandler.updateSoundPrefs(Start.this);
			}
		});

		toggleImpactButton.setChecked(SoundManager.showingImpact());
		toggleImpactButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (toggleImpactButton.isChecked()) {
					SoundManager.setShowImpact(true);
				}

				else {
					SoundManager.setShowImpact(false);
				}

				DBHandler.updateImpactPref(Start.this);
			}
		});
	}

	private void showSettings() {
		settingsDialog.show();
	}

	private boolean openDB() {
		if (DBHandler.open(this))
			return (DBHandler.checkTables(this));

		return false;
	}

	private void reopenDB() {
		if (!DBHandler.isOpen(this)) {
			DBHandler.open(this);
			loadOwnedItems();
			updateViews();
		} else {
			loadOwnedItems();
			updateViews();
		}
	}

	private void closeDB() {
		if (!DBHandler.isOpen(this))
			return;

		DBHandler.close();
	}

	private void loadOwnedItems() {
		// ownedItems = new OwnedItems();

		StoreItem[] owned = DBHandler.pullOwnedItems(this);
		OwnedItems.addOwnedItems(owned);

		OwnedItems.setGold(DBHandler.loadGlobalStats()[0]);

		OwnedItems.addOwnedItems(Helper.getStartingPlayerClasses(this));

		// runes added to store when class is selected for a new player
	}

	private void updateViews() {
		try {
			hubStoreButton.setEnabled(true);
			hubStartButton.setEnabled(false);
			hubStartButton.setBackgroundResource(R.drawable.buttonstartrounddisabled);
			hubInventoryButton.setEnabled(false);
			hubInventoryButton.setBackgroundResource(R.drawable.buttoninventorydisabled);
			hubCurrentGoldText.setText("Gold: " + OwnedItems.gold());

			if (toggleMusicButton != null)
				toggleMusicButton.setChecked(SoundManager.playingMusic());
			if (toggleSoundsButton != null)
				toggleSoundsButton.setChecked(SoundManager.playingSounds());
			if (toggleImpactButton != null)
				toggleImpactButton.setChecked(SoundManager.showingImpact());

		} catch (Exception e) {
			// views not made yet?? WTF
			setupViews();
			updateViews();
		}
	}

	private void setupViews() {
		setupHelpDialog();
		setupSettingsDialog();
		setupAboutDialog();

		Button settingsButton = (Button) findViewById(R.id.mainmenuSettingsButton);
		settingsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showSettings();
			}

		});

		Button helpButton = (Button) findViewById(R.id.mainmenuHelpButton);
		helpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showHelp();
			}

		});

		hubCurrentGoldText = (TextView) findViewById(R.id.hubCurrentGoldText);

		hubBuyGoldButton = (Button) findViewById(R.id.hubBuyGoldButton);
		hubBuyGoldButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (buyGoldChancesLeft <= 0) {
					Toast.makeText(Start.this, "No more buy gold chances left.", Toast.LENGTH_SHORT).show();
					hubBuyGoldButton.setEnabled(false);
					return;
				}

				int amount = new Random().nextInt(1000) + 1; // 1..1000
				OwnedItems.updateGold(amount);
				buyGoldChancesLeft--;
				updateViews();
				Toast.makeText(Start.this, "Gained " + amount + " gold. " + buyGoldChancesLeft + " left.",
						Toast.LENGTH_SHORT).show();

				if (buyGoldChancesLeft <= 0) {
					hubBuyGoldButton.setEnabled(false);
				}
			}
		});

		hubStoreButton = (Button) findViewById(R.id.hubStoreButton);
		hubStoreButton.setEnabled(false);
		hubStoreButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				wasAppNavigation = true;

				Intent i = new Intent(getApplicationContext(),
						com.alderangaming.wizardsencounters.ControllerStore.class);
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
		hubStartButton.setBackgroundResource(R.drawable.buttonstartrounddisabled);
		hubStartButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				wasAppNavigation = true;

				SoundManager.stopAllMusic();

				player = savedPlayers.get(selectedPlayerIndex);

				OwnedItems.addDefaultItems(DefinitionClasses.CLASS_PRELOADED_ITEMS[player.playerClass()],
						getApplicationContext());
				player.setDefaultEquippedItems(DefinitionClasses.CLASS_PRELOADED_ITEMS[player.playerClass()]);

				Intent i = new Intent(getApplicationContext(),
						com.alderangaming.wizardsencounters.ControllerRoundStart.class);
				Bundle b = new Bundle();

				// b.putSerializable("ownedItems", ownedItems);
				b.putSerializable("player", player);

				i.putExtras(b);

				closeDB();
				startActivityForResult(i, 3000);
			}

		});

		Button hubNewButton = (Button) findViewById(R.id.hubNewButton);
		hubNewButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				wasAppNavigation = true;

				Intent i = new Intent(getApplicationContext(),
						com.alderangaming.wizardsencounters.ControllerMakeNewChar.class);
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
		hubInventoryButton.setBackgroundResource(R.drawable.buttoninventorydisabled);
		hubInventoryButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				wasAppNavigation = true;

				player = savedPlayers.get(selectedPlayerIndex);

				player.setDefaultEquippedItems(DefinitionClasses.CLASS_PRELOADED_ITEMS[player.playerClass()]);
				OwnedItems.addDefaultItems(DefinitionClasses.CLASS_PRELOADED_ITEMS[player.playerClass()],
						getApplicationContext());

				Intent i = new Intent(getApplicationContext(),
						com.alderangaming.wizardsencounters.ControllerInventory.class);
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

	private void loadSavedPlayers() {
		savedPlayers = DBHandler.loadSavedPlayers();
		if (savedPlayers == null)
			return;

		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

		for (int a = 0; a < savedPlayers.size(); a++) {
			rows.add(Helper.createPlayerMap(savedPlayers.get(a).name(),
					DefinitionClasses.CLASS_NAMES[savedPlayers.get(a).playerClass()], "Round: "
							+ savedPlayers.get(a).currentRound(),
					"Rank: " + savedPlayers.get(a).rank()));
		}

		String[] fromKeys = new String[] { "Name", "Class", "Round", "Rank" };
		int[] toIds = new int[] { R.id.playerListName, R.id.playerListClass, R.id.playerListRound,
				R.id.playerListRank };

		savedPlayerList.setAdapter(new SimpleAdapter(this, rows, R.layout.playerlistitem, fromKeys, toIds));

		savedPlayerList.invalidate();

		savedPlayerList.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				// show delete? dialog

				AlertDialog.Builder fightEndDialog = new AlertDialog.Builder(Start.this);

				fightEndDialog.setTitle("Kill " + savedPlayers.get(position).name() + "?")
						.setMessage("You will still own any items obtained by characters killed in this way.")
						.setCancelable(false).setPositiveButton("Wipe Him Out", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								if (DBHandler.killPlayer(savedPlayers.get(position).playerID()))
									loadSavedPlayers();

								dialog.cancel();
								updateViews();

							}
						}).setNegativeButton("Spare Him", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								dialog.cancel();
							}
						});

				AlertDialog alert = fightEndDialog.create();
				alert.show();

				return true;

			}
		});

		savedPlayerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
				for (int a = 0; a < parent.getChildCount(); a++) {
					parent.getChildAt(a).setBackgroundColor(Color.TRANSPARENT);
				}

				view.setBackgroundColor(Color.BLUE);

				selectedPlayerIndex = position;
				hubStartButton.setEnabled(true);
				hubStartButton.setBackgroundResource(R.drawable.buttonstartround);
				hubInventoryButton.setEnabled(true);
				hubInventoryButton.setBackgroundResource(R.drawable.buttoninventory);
				hubStoreButton.setEnabled(true);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			// return from make new
			case 1000: {
				reopenDB();

				// loadSavedPlayers();
				// loadOwnedItems();
				break;
			}
			// return from inventory
			case 2000: {
				reopenDB();

				// loadSavedPlayers();
				// loadOwnedItems();

				break;
			}
			// return from combat
			case 3000: {
				reopenDB();
				// loadSavedPlayers();
				// loadOwnedItems();

				SoundManager.playMusic(SoundManager.SOUND_TYPE_MUSIC_MAIN_MENU, true, this);

				break;
			}
			// return from store
			case 4000: {
				reopenDB();
				// loadSavedPlayers();
				// loadOwnedItems();

				break;
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
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
	TextView hubCurrentGoldText;
	Button hubStoreButton;
	Button hubBuyGoldButton;
	AlertDialog settingsDialog;
	AlertDialog aDialog;
	ToggleButton toggleMusicButton, toggleSoundsButton, toggleImpactButton;

}