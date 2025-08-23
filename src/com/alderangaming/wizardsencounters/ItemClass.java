package com.alderangaming.wizardsencounters;

import java.io.Serializable;

import android.content.Context;

public class ItemClass extends StoreItem implements Serializable
{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -5410881469125519718L;
	
	private int _classType = -1;

	public ItemClass(int t, int i, Context c)
	{
		super(t, i);
		setName(DefinitionClasses.CLASS_NAMES[i]);
		setDescription(DefinitionClasses.CLASS_DESCRIPTIONS[i]);
		
		makeSelf(i);
	}
	
	private void makeSelf(int id)
	{
		_classType = DefinitionClasses.CLASS_TYPE[id];
	}
	
	public int classType()
	{
		return _classType;
	}
	
}
