package com.alderangaming.wizardsencounters;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBHandler {
	private static SQLiteDatabase db = null;

	public static boolean open(Context context) {
		if (openDB(context))
			return true;

		return false;
	}

	public static boolean isOpen(Context context) {
		if (db == null)
			return false;

		return db.isOpen();
	}

	public static String[] getPossibleClasses() {

		String[] classes = null;

		return classes;
	}

	public static boolean killPlayer(int playerID) {
		String command = "UPDATE Player SET IsActive = 0 WHERE PlayerID = '" + playerID + "'";

		try {
			db.execSQL(command, new String[] {});
			Log.d("dbHandler", "kill Player: " + command);
		} catch (Exception e) {
			Log.d("dbHandler", "kill Player error: " + e.getMessage());
			return false;
		}

		return true;
	}

	public static boolean updateAllPlayers(ArrayList<Player> savedPlayers) {
		for (int a = 0; a < savedPlayers.size(); a++) {
			if (updatePlayer(savedPlayers.get(a)))
				continue;
		}
		return true;
	}

	public static boolean updatePlayer(Player player) {
		int cl = player.playerClass();
		int ra = player.rank();
		int ew = -1;
		if (player.equippedWeapon() >= 0)
			ew = player.equippedWeapon();

		int a1 = -1;
		if (player.equippedArmorSlot1() >= 0)
			a1 = player.equippedArmorSlot1();

		int a2 = -1;
		if (player.equippedArmorSlot2() >= 0)
			a2 = player.equippedArmorSlot2();

		int a3 = -1;
		if (player.equippedArmorSlot3() >= 0)
			a3 = player.equippedArmorSlot3();

		int a4 = -1;
		if (player.equippedArmorSlot4() >= 0)
			a4 = player.equippedArmorSlot4();

		int i1 = -1;
		if (player.equippedItemSlot1() >= 0)
			i1 = player.equippedItemSlot1();

		int i2 = -1;
		if (player.equippedItemSlot2() >= 0)
			i2 = player.equippedItemSlot2();

		int ch = player.currentHP();
		int ca = player.currentAP();
		int cr = player.currentRound();
		int cf = player.currentFight();

		String command = "UPDATE Player SET Class = '" + cl + "', " + "Rank = '" + ra + "', " + "EquippedWeapon = '"
				+ ew + "', "
				+ "EquippedArmorSlot1 = '" + a1 + "', " + "EquippedArmorSlot2 = '" + a2 + "', "
				+ "EquippedArmorSlot3 = '" + a3 + "', " + "EquippedArmorSlot4 = '" + a4 + "', "
				+ "EquippedItemSlot1 = '" + i1 + "', " + "EquippedItemSlot2 = '" + i2 + "', " + "CurrentHP = '" + ch
				+ "', " + "CurrentAP = '" + ca + "', " + "CurrentRound = '" + cr + "', " + "CurrentFight = '" + cf
				+ "', " + "CurrentAP = '" + ca + "', " + "CurrentRound = '" + cr + "', " + "CurrentFight = '" + cf
				+ "' WHERE PlayerID = '" + player.playerID() + "'";

		try {
			db.execSQL(command, new String[] {});
			Log.d("dbHandler", "updated Player: " + command);
		} catch (Exception e) {
			Log.d("dbHandler", "update Player error: " + e.getMessage());
			return false;
		}

		return true;
	}

	public static boolean updateGlobalStats(int gold) {
		String command = "UPDATE GlobalStats SET Gold = '" + gold + "'";

		try {
			db.execSQL(command, new String[] {});
			Log.d("dbHandler", "updated GlobalStats: " + command);
		} catch (Exception e) {
			Log.d("dbHandler", "update GlobalStats error: " + e.getMessage());
			return false;
		}
		return true;
	}

	public static boolean updateActiveAbilities(int[] items, int playerID) {
		String command = "DELETE FROM ActiveAbilities WHERE PlayerID = '" + playerID + "'";
		try {
			db.execSQL(command, new String[] {});
			Log.d("dbHandler", "deleted ActiveAbilities: " + command);
		} catch (Exception e) {
			Log.d("dbHandler", "delete ActiveAbilities error: " + e.getMessage());
			return false;
		}

		for (int a = 0; a < items.length; a++) {
			command = "INSERT INTO ActiveAbilities (AbilityID, PlayerID) VALUES ('" + items[a] + "', '" + playerID
					+ "')";

			try {
				db.execSQL(command, new String[] {});
				Log.d("dbHandler", "insert into ActiveAbilities: " + command);
			} catch (Exception e) {
				Log.d("dbHandler", "insert into ActiveAbilities error: " + e.getMessage());
				return false;
			}
		}
		return true;
	}

	public static boolean updateImpactPref(Context context) {
		if (!db.isOpen())
			open(context);

		boolean b = SoundManager.showingImpact();

		int i = 0;
		if (b == true)
			i = 1;

		String command = "UPDATE GlobalStats SET Impact = '" + i + "'";

		try {
			db.execSQL(command, new String[] {});
			Log.d("dbHandler", "updated GlobalStats impact prefs: " + command);
		} catch (Exception e) {
			Log.d("dbHandler", "update GlobalStats impact prefs error: " + e.getMessage());
			return false;
		}
		return true;
	}

	public static boolean updateSoundPrefs(Context context) {
		if (!db.isOpen())
			open(context);

		int m = 0;
		if (SoundManager.playingMusic())
			m = 1;

		int s = 0;
		if (SoundManager.playingSounds())
			s = 1;

		float mv = SoundManager.getMusicVolume();
		float sv = SoundManager.getSoundVolume();

		// Ensure columns exist (best-effort, ignore errors if present)
		try {
			String alter1 = "ALTER TABLE GlobalStats ADD COLUMN MusicVolume REAL NOT NULL DEFAULT 1.0";
			db.execSQL(alter1, new String[] {});
		} catch (Exception e) {
		}
		try {
			String alter2 = "ALTER TABLE GlobalStats ADD COLUMN SoundVolume REAL NOT NULL DEFAULT 1.0";
			db.execSQL(alter2, new String[] {});
		} catch (Exception e) {
		}

		String command = "UPDATE GlobalStats SET PlayMusic = '" + m + "', PlaySounds = '" + s + "', MusicVolume = '"
				+ mv + "', SoundVolume = '" + sv + "'";

		try {
			db.execSQL(command, new String[] {});
			Log.d("dbHandler", "updated GlobalStats sound prefs: " + command);
		} catch (Exception e) {
			Log.d("dbHandler", "update GlobalStats sound prefs error: " + e.getMessage());
			return false;
		}
		return true;
	}

	public static int insertNewPlayer(String playerName, int playerClass) {
		int curRank = 1;
		int curHP = DefinitionClasses.CLASS_HP_TREE[playerClass][curRank];
		int curAP = DefinitionClassType.CLASS_TYPE_AP_TREE_BY_LEVEL[DefinitionClasses.CLASS_TYPE[playerClass]][curRank];

		String command = "INSERT INTO Player (Name, Class, CurrentHP, CurrentAP) VALUES ('" + playerName + "','"
				+ playerClass
				+ "','" + curHP + "','" + curAP + "')";
		try {
			db.execSQL(command, new String[] {});
			Log.d("dbHandler", "inserted new player: " + command);
		} catch (Exception e) {
			Log.d("dbHandler", "insert player error: " + e.getMessage());
			return -1;
		}

		// get last ID for local purposes
		command = "SELECT * From Player;";

		Cursor cursor = null;
		try {
			cursor = db.rawQuery(command, null);
			cursor.moveToFirst();
		} catch (Exception e) {
			Log.d("dbHandler", "getLastID error: " + e.getMessage());
			cursor.close();
			return -1;
		}

		int playerID = cursor.getCount();
		cursor.close();

		return playerID;
	}

	public static boolean updateOwnedItems(StoreItem[] items) {
		// delete current owned items

		String command = "DELETE FROM OwnedItems";
		try {
			db.execSQL(command, new String[] {});
			Log.d("dbHandler", "deleted old ownedItems table: " + command);
		} catch (Exception e) {
			Log.d("dbHandler", "could not delete ownedItems table:" + e.toString());
		}

		// insert new owned items

		command = "INSERT INTO OwnedItems";
		for (int a = 0; a < items.length; a++) {
			if (a == 0) {
				command += " SELECT ";
				command += "'" + items[a].itemType() + "' AS 'ItemType'," + "'" + items[a].id() + "' AS 'ItemID'," + "'"
						+ items[a].chargesLeft() + "' AS 'ChargesLeft'," + "'" + items[a].chargesPurchased()
						+ "' AS 'ChargesPurchased'," + "'" + items[a].amount() + "' AS 'Amount'";
			} else {
				command += " UNION SELECT ";
				command += "'" + items[a].itemType() + "'," + "'" + items[a].id() + "'," + "'" + items[a].chargesLeft()
						+ "',"
						+ "'" + items[a].chargesPurchased() + "'," + "'" + items[a].amount() + "'";
			}
		}

		try {
			db.execSQL(command, new String[] {});
			Log.d("dbHandler", "inserted into OwnedItems: " + command);
		} catch (Exception e) {
			Log.d("dbHandler", "could not insert into ownedItems:" + e.toString());
			return false;
		}

		return true;
	}

	public static int getImpactPref(Context context) {
		if (!db.isOpen())
			open(context);

		String command = "SELECT Impact FROM GlobalStats";
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(command, null);
			cursor.moveToFirst();
			int i = cursor.getInt(0);
			cursor.close();
			return i;
		} catch (Exception e) {
			Log.d("dbHandler", "failed to pull impact prefs from Global Stats: " + e.toString());
			return -1;
		}
	}

	public static void getSoundPrefs(Context context) {
		if (!db.isOpen())
			open(context);

		String command = "SELECT PlayMusic, PlaySounds, MusicVolume, SoundVolume FROM GlobalStats";
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(command, null);
			cursor.moveToFirst();
		} catch (Exception e) {
			Log.d("dbHandler", "failed to pull sound prefs from Global Stats: " + e.toString());
			return;
		}

		if (cursor.getCount() > 0) {
			int pmusic = cursor.getInt(0);
			boolean pmusicB = false;
			if (pmusic == 1)
				pmusicB = true;
			SoundManager.setPlayMusic(pmusicB);

			int psound = cursor.getInt(1);
			boolean psoundB = false;
			if (psound == 1) {
				psoundB = true;
			}
			SoundManager.setPlaySounds(psoundB);

			try {
				float mv = cursor.getFloat(2);
				float sv = cursor.getFloat(3);
				SoundManager.setMusicVolume(mv);
				SoundManager.setSoundVolume(sv);
			} catch (Exception ex) {
				// older DB without columns; ignore
			}
		}
		cursor.close();
	}

	public static StoreItem[] pullOwnedItems(Context mContext) {
		String command = "SELECT * FROM OwnedItems";
		Cursor cursor = null;
		StoreItem[] ownedItems = null;
		try {
			cursor = db.rawQuery(command, null);
			cursor.moveToFirst();

			Log.d("dbHandler", "pulling from OwnedItems: " + cursor.getCount() + " items found");
			ownedItems = new StoreItem[cursor.getCount()];
		} catch (Exception e) {
			Log.d("dbHandler", "failed to pull from ownedItems: " + e.toString());
			return null;
		}

		if (cursor.getCount() > 0) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				Log.d("dbHandler",
						"row" + i + ": ItemType=" + cursor.getInt(0) + ", ItemId=" + cursor.getInt(1) + ", ChargesLeft="
								+ cursor.getInt(2) + ", chargesPurch=" + cursor.getInt(3) + ", amount="
								+ cursor.getInt(4));

				if (cursor.getInt(0) == DefinitionGlobal.ITEM_TYPE_PLAYER_CLASS) {
					// setting the class id that was pulled to the new object
					ownedItems[i] = new ItemClass(cursor.getInt(0), cursor.getInt(1), mContext);
					ownedItems[i].setAmount(cursor.getInt(4));
				} else if (cursor.getInt(0) == DefinitionGlobal.ITEM_TYPE_ARMOR) {
					ownedItems[i] = new ItemArmor(cursor.getInt(0), cursor.getInt(1), mContext);
					ownedItems[i].setAmount(cursor.getInt(4));
				} else if (cursor.getInt(0) == DefinitionGlobal.ITEM_TYPE_ITEM) {
					ownedItems[i] = new ItemItem(cursor.getInt(0), cursor.getInt(1), mContext);
					ownedItems[i].setChargesLeft(cursor.getInt(2));
					ownedItems[i].setChargesPurchased(cursor.getInt(3));
				} else if (cursor.getInt(0) == DefinitionGlobal.ITEM_TYPE_WEAPON) {
					ownedItems[i] = new ItemWeapon(cursor.getInt(0), cursor.getInt(1), mContext);
					ownedItems[i].setAmount(cursor.getInt(4));
				} else if (cursor.getInt(0) == DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY) {
					ownedItems[i] = new ItemRune(cursor.getInt(0), cursor.getInt(1), mContext);
					ownedItems[i].setAmount(cursor.getInt(4));
				}
			}
		}

		cursor.close();

		return ownedItems;
	}

	public static int[] loadGlobalStats() {
		String command = "SELECT * FROM GlobalStats";
		Cursor playerResult = null;

		int[] returnData = { 0 };

		try {
			playerResult = db.rawQuery(command, null);
			playerResult.moveToFirst();
			returnData[0] = playerResult.getInt(playerResult.getColumnIndex("Gold"));
			Log.d("dbHandler", "pulling from GlobalStats: " + returnData[0]);
		} catch (Exception e) {
			Log.d("dbHandler", "Error loading GlobalStats: " + e.toString());
			playerResult.close();
			return returnData;
		}

		playerResult.close();

		return returnData;
	}

	public static int[] loadActiveAbilities(int playerID) {
		String command = "SELECT * FROM ActiveAbilities WHERE PlayerID = '" + playerID + "'";
		Cursor playerResult = null;
		try {
			playerResult = db.rawQuery(command, null);
			playerResult.moveToFirst();
		} catch (Exception e) {
			Log.d("dbHandler", "Error loading ActiveAbilities: " + e.toString());
			return null;
		}

		if (playerResult.getCount() < 0) {
			Log.d("dbHandler", "0 ActiveAbilities loaded");
			playerResult.close();

			return null;
		}

		int[] abils = new int[playerResult.getCount()];
		for (int a = 0; a < playerResult.getCount(); a++) {
			playerResult.moveToPosition(a);

			int id = playerResult.getInt(playerResult.getColumnIndex("AbilityID"));
			abils[a] = id;
		}

		playerResult.close();

		return abils;
	}

	public static ArrayList<Player> loadSavedPlayers() {
		ArrayList<Player> players = new ArrayList<Player>();

		String command = "SELECT * FROM Player WHERE IsActive='1'";
		Cursor playerResult = null;
		try {
			playerResult = db.rawQuery(command, null);
			playerResult.moveToFirst();
		} catch (Exception e) {
			Log.d("dbHandler", "Error loading characters: " + e.toString());
		}

		if (playerResult.getCount() < 0) {
			Log.d("dbHandler", "0 players loaded");
			playerResult.close();

		} else {
			Log.d("dbHandler", "loading " + playerResult.getCount() + " saved players");
		}

		for (int a = 0; a < playerResult.getCount(); a++) {
			playerResult.moveToPosition(a);

			int playerID = playerResult.getInt(playerResult.getColumnIndex("PlayerID"));
			String playerName = playerResult.getString(playerResult.getColumnIndex("Name"));
			int playerClass = playerResult.getInt(playerResult.getColumnIndex("Class"));

			Player player = new Player(playerID, playerName, playerClass, false);
			player.setRank(playerResult.getInt(playerResult.getColumnIndex("Rank")));

			player.setEquippedWeapon(playerResult.getInt(playerResult.getColumnIndex("EquippedWeapon")));

			player.setEquippedArmorSlot1(playerResult.getInt(playerResult.getColumnIndex("EquippedArmorSlot1")));

			player.setEquippedArmorSlot2(playerResult.getInt(playerResult.getColumnIndex("EquippedArmorSlot2")));

			player.setEquippedArmorSlot3(playerResult.getInt(playerResult.getColumnIndex("EquippedArmorSlot3")));

			player.setEquippedArmorSlot4(playerResult.getInt(playerResult.getColumnIndex("EquippedArmorSlot4")));

			player.setEquippedItemSlot1(playerResult.getInt(playerResult.getColumnIndex("EquippedItemSlot1")));

			player.setEquippedItemSlot2(playerResult.getInt(playerResult.getColumnIndex("EquippedItemSlot2")));

			player.setCurrentHP(playerResult.getInt(playerResult.getColumnIndex("CurrentHP")));
			player.setCurrentAP(playerResult.getInt(playerResult.getColumnIndex("CurrentAP")));
			player.setCurrentRound(playerResult.getInt(playerResult.getColumnIndex("CurrentRound")));
			player.setCurrentFight(playerResult.getInt(playerResult.getColumnIndex("CurrentFight")));

			player.setLoadedAbilities(loadActiveAbilities(playerID));
			player.updateStats();

			players.add(player);
		}

		playerResult.close();
		return players;
	}

	public static boolean checkTables(Context mContext) {
		String dbVersion = mContext.getResources().getString(R.string.dbversion);
		String command = "SELECT * FROM DbVersion WHERE Version = '" + dbVersion + "';";

		boolean tablesMade = false;
		boolean savePlayerData = true;

		if (DefinitionGlobal.DEBUG_REBUILD_DB == 1) {
			tablesMade = false;
			savePlayerData = false;
			Log.d("dbHandler", "... updateTables()");
			dropTables();
			if (createTables(tablesMade, savePlayerData, dbVersion))
				return true;
		}

		Cursor cursor = null;
		try {
			cursor = db.rawQuery(command, null);
		} catch (Exception e) {
			Log.d("dbHandler", "checkForUpdate: " + e.getMessage());

			if (e.getMessage().contains("no such table")) {
				tablesMade = false;
				savePlayerData = false;
				Log.d("dbHandler", "... updateTables()");

				if (createTables(tablesMade, savePlayerData, dbVersion))
					return true;
			}
		}

		Log.d("dbHandler", "dbVersion returned " + cursor.getCount());
		if (cursor.getCount() > 0) {
			tablesMade = true;
			savePlayerData = true;
			Log.d("dbHandler", "... showMainMenu()");

			cursor.close();

			return true;
			// startGameRef.showMainMenu();
		} else {
			tablesMade = false;
			savePlayerData = true;
			Log.d("dbHandler", "... updateTables()");

			cursor.close();

			if (createTables(tablesMade, savePlayerData, dbVersion))
				return true;
		}

		return true;
	}

	private static void dropTables() {
		String command = "DROP TABLE Player";
		try {
			db.execSQL(command, new String[] {});
			Log.d("dbHandler", "dropped table Player: " + command);
		} catch (Exception e) {
			Log.d("dbHandler", "could not drop table Player:" + e.toString());
		}

		command = "DROP TABLE OwnedItems";
		try {
			db.execSQL(command, new String[] {});
			Log.d("dbHandler", "dropped table OwnedItems: " + command);
		} catch (Exception e) {
			Log.d("dbHandler", "could not drop table ownedItems:" + e.toString());
		}
		command = "DROP TABLE ActiveAbilities";
		try {
			db.execSQL(command, new String[] {});
			Log.d("dbHandler", "dropped table ActiveAbilities: " + command);
		} catch (Exception e) {
			Log.d("dbHandler", "could not drop table ActiveAbilities:" + e.toString());
		}
		command = "DROP TABLE GlobalStats";
		try {
			db.execSQL(command, new String[] {});
			Log.d("dbHandler", "dropped table GlobalStats: " + command);
		} catch (Exception e) {
			Log.d("dbHandler", "could not drop table GlobalStats:" + e.toString());
		}
		command = "DROP TABLE DbVersion";
		try {
			db.execSQL(command, new String[] {});
			Log.d("dbHandler", "dropped table DbVersion: " + command);
		} catch (Exception e) {
			Log.d("dbHandler", "could not drop table DbVersion:" + e.toString());
		}
	}

	private static boolean createTables(boolean tablesMade, boolean savePlayerData, String dbVersion) {
		if (tablesMade == false && savePlayerData == false) {
			if (createTableDbVersion())
				if (createTablePlayer())
					if (createTableOwnedItems())
						if (createTableActiveAbilities())
							if (createTableGlobalStats())
								;
		}

		if (updateTableVersion(dbVersion))
			if (updateTableGlobalStats())
				return true;

		return true;
	}

	private static boolean updateTableVersion(String dbVersion) {
		String command = "INSERT INTO DbVersion (Version) VALUES ('" + dbVersion + "');";

		try {
			db.execSQL(command, new String[] {});
			Log.d("dbHandler", "updated table DbVersion: " + command);
		} catch (Exception e) {
			Log.d("dbHandler", "could not create table:" + e.toString());
			return false;
		}

		return true;
	}

	private static boolean updateTableGlobalStats() {
		String command = "INSERT INTO GlobalStats (Gold, PlayMusic, PlaySounds, Impact, MusicVolume, SoundVolume) VALUES ('"
				+ DefinitionGlobal.DEFAULT_STARTING_GOLD + "', 1, 1, 1, 0.5, 0.5);";

		try {
			db.execSQL(command, new String[] {});
			Log.d("dbHandler", "updated table GlobalStats: " + command);
		} catch (Exception e) {
			Log.d("dbHandler", "could not create table:" + e.toString());
			return false;
		}

		return true;
	}

	private static boolean createTableActiveAbilities() {
		String command = "CREATE TABLE ActiveAbilities (ID INTEGER PRIMARY KEY autoincrement, "
				+ "AbilityID INTEGER NOT NULL DEFAULT '-1'," + "PlayerID INTEGER NOT NULL DEFAULT '-1')";

		try {
			db.execSQL(command, new String[] {});
			Log.d("dbHandler", "created table ActiveAbilities: " + command);
		} catch (Exception e) {
			Log.d("dbHandler", "could not create table ActiveAbilities:" + e.toString());
			return false;
		}
		return true;
	}

	private static boolean createTableOwnedItems() {
		String command = "CREATE TABLE OwnedItems (ItemType INTEGER NOT NULL DEFAULT '-1', "
				+ "ItemID INTEGER NOT NULL DEFAULT '-1', " + "ChargesLeft INTEGER NOT NULL DEFAULT '-1', "
				+ "ChargesPurchased INTEGER NOT NULL DEFAULT '-1', " + "Amount INTEGER NOT NULL DEFAULT '-1')";

		try {
			db.execSQL(command, new String[] {});
			Log.d("dbHandler", "created table ownedItems: " + command);
		} catch (Exception e) {
			Log.d("dbHandler", "could not create table ownedItems:" + e.toString());
			return false;
		}
		return true;
	}

	private static boolean createTablePlayer() {
		String command = "CREATE TABLE Player (PlayerID INTEGER PRIMARY KEY autoincrement, "
				+ "Name TEXT NOT NULL DEFAULT 'nothing', " + "Class INTEGER NOT NULL DEFAULT '-1', "
				+ "Rank INTEGER NOT NULL DEFAULT '1', " + "EquippedWeapon INTEGER NOT NULL DEFAULT '-1',"
				+ "EquippedArmorSlot1 INTEGER NOT NULL DEFAULT '-1', "
				+ "EquippedArmorSlot2 INTEGER NOT NULL DEFAULT '-1', "
				+ "EquippedArmorSlot3 INTEGER NOT NULL DEFAULT '-1', "
				+ "EquippedArmorSlot4 INTEGER NOT NULL DEFAULT '-1', "
				+ "EquippedItemSlot1 INTEGER NOT NULL DEFAULT '-1', "
				+ "EquippedItemSlot2 INTEGER NOT NULL DEFAULT '-1', " + "CurrentHP INTEGER NOT NULL DEFAULT '-1', "
				+ "CurrentAP INTEGER NOT NULL DEFAULT '-1', " + "CurrentRound INTEGER NOT NULL DEFAULT '1', "
				+ "CurrentFight INTEGER NOT NULL DEFAULT '-1', " + "IsActive INTEGER NOT NULL DEFAULT '1');";

		try {
			db.execSQL(command, new String[] {});
			Log.d("dbHandler", "created table Player: " + command);
		} catch (Exception e) {
			Log.d("dbHandler", "could not create table Player:" + e.toString());
			return false;
		}
		return true;
	}

	private static boolean createTableGlobalStats() {
		String command = "CREATE TABLE IF NOT EXISTS GlobalStats (Gold INTEGER NOT NULL DEFAULT '100', PlayMusic INTEGER NOT NULL DEFAULT '1', PlaySounds INTEGER NOT NULL DEFAULT '1', Impact INTEGER NOT NULL DEFAULT '1', MusicVolume REAL NOT NULL DEFAULT 0.5, SoundVolume REAL NOT NULL DEFAULT 0.5);";

		try {
			db.execSQL(command, new String[] {});
			Log.d("dbHandler", "created table GlobalStats");
		} catch (Exception e) {
			Log.d("dbHandler", "could not create table:" + e.toString());
			return false;
		}
		return true;

	}

	private static boolean createTableDbVersion() {
		String command = "CREATE TABLE IF NOT EXISTS DbVersion (Version INTEGER NOT NULL DEFAULT '0');";

		try {
			db.execSQL(command, new String[] {});
			Log.d("dbHandler", "created table DbVersion");
		} catch (Exception e) {
			Log.d("dbHandler", "could not create table:" + e.toString());
			return false;
		}
		return true;
	}

	private static boolean openDB(Context mContext) {

		try {
			db = mContext.openOrCreateDatabase(mContext.getResources().getString(R.string.dbnamestring),
					Context.MODE_PRIVATE, null);

			Log.d("dbHandler", "db Opened");
		} catch (Exception e) {
			Log.d("dbHandler", "Could not open/create db:" + e.toString());
			return false;
		}
		return true;

	}

	public static boolean close() {
		try {
			db.close();
			db = null;
			Log.d("dbHandler", "Database closed");
		} catch (Exception e) {
			Log.d("dbHandler", "Could not close db:" + e.toString());
			return false;
		}
		return true;
	}
}
