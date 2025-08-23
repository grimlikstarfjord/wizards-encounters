package com.alderangaming.wizardsencounters;

import java.util.ArrayList;

public class BackgroundManager
{

	public static final ArrayList<Integer> backgrounds = new ArrayList<Integer>();
	
	private static int lastBackground = -1;
	
	public static int getNextBackground()
	{
		int newback = lastBackground;
		while(newback == lastBackground)
			newback= backgrounds.get(Helper.randomInt(backgrounds.size()));
		
		lastBackground = newback;
		return newback;
	}
	
	public static void setupBackgrounds()
	{		
		backgrounds.add(R.drawable.areaelvenwoodbackground);
		backgrounds.add(R.drawable.areawarriorcampbackground);
		backgrounds.add(R.drawable.areawestspellbackground);
		backgrounds.add(R.drawable.blacktidepoolbackground);
		backgrounds.add(R.drawable.camp);
		backgrounds.add(R.drawable.canopycavebackground);
		backgrounds.add(R.drawable.elderhutbackground);
		backgrounds.add(R.drawable.grassyplainsbackground);
		backgrounds.add(R.drawable.rollingfoothillsbackground);
		backgrounds.add(R.drawable.septictombbackground);
		backgrounds.add(R.drawable.stagnantswampbackground);
		backgrounds.add(R.drawable.tallforestbackground);
		backgrounds.add(R.drawable.tavernbackground);
		backgrounds.add(R.drawable.webbedwoodsbackground);
		
	}
	
}
