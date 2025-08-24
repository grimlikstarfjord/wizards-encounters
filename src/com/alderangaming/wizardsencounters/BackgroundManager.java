package com.alderangaming.wizardsencounters;

import java.util.ArrayList;

public class BackgroundManager {

	public static final ArrayList<Integer> backgrounds = new ArrayList<Integer>();
	// Resource entry names for backgrounds, same order as 'backgrounds' list
	private static final String[] RES_NAMES = new String[] {
			"areaelvenwoodbackground",
			"areawarriorcampbackground",
			"areawestspellbackground",
			"blacktidepoolbackground",
			"camp",
			"canopycavebackground",
			"grassyplainsbackground",
			"rollingfoothillsbackground",
			"septictombbackground",
			"stagnantswampbackground",
			"tallforestbackground",
			"tavernbackground",
			"webbedwoodsbackground",
			"backgroundtreasure"
	};

	private static int lastBackground = -1;

	public static int getNextBackground() {
		int newback = lastBackground;
		while (newback == lastBackground)
			newback = backgrounds.get(Helper.randomInt(backgrounds.size()));

		lastBackground = newback;
		return newback;
	}

	public static void setupBackgrounds() {
		backgrounds.add(R.drawable.areaelvenwoodbackground);
		backgrounds.add(R.drawable.areawarriorcampbackground);
		backgrounds.add(R.drawable.areawestspellbackground);
		backgrounds.add(R.drawable.blacktidepoolbackground);
		backgrounds.add(R.drawable.camp);
		backgrounds.add(R.drawable.canopycavebackground);
		backgrounds.add(R.drawable.grassyplainsbackground);
		backgrounds.add(R.drawable.rollingfoothillsbackground);
		backgrounds.add(R.drawable.septictombbackground);
		backgrounds.add(R.drawable.stagnantswampbackground);
		backgrounds.add(R.drawable.tallforestbackground);
		backgrounds.add(R.drawable.tavernbackground);
		backgrounds.add(R.drawable.webbedwoodsbackground);

	}

	/**
	 * Developer override: if player name contains a token like "[bgidx=3]" or
	 * "[bg=tavernbackground]", return the specified background. Otherwise return
	 * -1.
	 * This does not persist; caller should fall back to getNextBackground() when
	 * -1.
	 */
	public static int getDevOverrideBackground(String playerName) {
		if (playerName == null)
			return -1;
		try {
			String lower = playerName.toLowerCase();

			// Pattern 1: BGIDX<number> (letters+digits only)
			int idxPos = lower.indexOf("bgidx");
			if (idxPos >= 0) {
				int start = idxPos + 5;
				int end = start;
				while (end < lower.length() && Character.isDigit(lower.charAt(end)))
					end++;
				if (end > start) {
					int idx = Integer.parseInt(lower.substring(start, end));
					if (idx >= 0 && idx < backgrounds.size())
						return backgrounds.get(idx);
				}
			}

			// Pattern 2: BG<resourcename> (letters+digits only), e.g., BGTAVERNBACKGROUND
			for (int i = 0; i < RES_NAMES.length; i++) {
				String token = "bg" + RES_NAMES[i];
				if (lower.contains(token)) {
					int resId = getResIdByName(RES_NAMES[i]);
					if (resId != -1)
						return resId;
				}
			}
		} catch (Exception ignored) {
		}
		return -1;
	}

	// Map a drawable entry name (e.g., "tavernbackground") to its resource id
	// without needing Context
	private static int getResIdByName(String resName) {
		if (resName == null)
			return -1;
		switch (resName) {
			case "areaelvenwoodbackground":
				return R.drawable.areaelvenwoodbackground;
			case "areawarriorcampbackground":
				return R.drawable.areawarriorcampbackground;
			case "areawestspellbackground":
				return R.drawable.areawestspellbackground;
			case "blacktidepoolbackground":
				return R.drawable.blacktidepoolbackground;
			case "camp":
				return R.drawable.camp;
			case "canopycavebackground":
				return R.drawable.canopycavebackground;
			case "grassyplainsbackground":
				return R.drawable.grassyplainsbackground;
			case "rollingfoothillsbackground":
				return R.drawable.rollingfoothillsbackground;
			case "septictombbackground":
				return R.drawable.septictombbackground;
			case "stagnantswampbackground":
				return R.drawable.stagnantswampbackground;
			case "tallforestbackground":
				return R.drawable.tallforestbackground;
			case "tavernbackground":
				return R.drawable.tavernbackground;
			case "webbedwoodsbackground":
				return R.drawable.webbedwoodsbackground;
			case "backgroundtreasure":
				return R.drawable.backgroundtreasure;
			default:
				return -1;
		}
	}

}
