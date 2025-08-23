package com.alderangaming.wizardsencounters;

import java.util.ArrayList;

public class SoundType
{
	private int _soundTypeId = -1;
	
	public SoundType(int st)
	{
		_soundTypeId = st;
	}
	
	private ArrayList<Integer> _soundResources = new ArrayList<Integer>();

	public void addSoundResource(int r)
	{
		_soundResources.add(r);
	}
		
	public int getSoundResourceByIndex(int i)
	{
		return _soundResources.get(i);
	}
	
	public int numSounds()
	{
		return _soundResources.size();
	}
	
	public int soundTypeId()
	{
		return _soundTypeId;
	}
}
