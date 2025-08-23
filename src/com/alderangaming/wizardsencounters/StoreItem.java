package com.alderangaming.wizardsencounters;

import java.io.Serializable;

import android.content.Context;
import android.util.Log;

public abstract class StoreItem implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -66995741778997470L;
	
	/* charges */
	private int _chargesLeft = 0;
	private int _chargesPurchased = 0;
	private int _maxCharges = 0;	
	
	private int _itemType = -1;
	private int _itemID = -1;
	private int[] _equippableSlots = {-1};
	private int _equippedSlot = -1;
	private int _minLevel = 0;
	
	private int _value = 0;
	private int _cost = 0;
	
	private int _amount = 0;
	private String _name = "";
	private String _modifiedName = "";
	public String _animationImageName = "";
	private String _imageName = "";
	private String _description = "";
	
	private boolean _isBound = false;
	
	private int _animationImageResource = -1;
	private int _imageResource = -1;
	private int[] _onlyForClasses = null;
	private int _availForANewPlayer = 0;
	private int _showsInStore = 0;
	
	public String equipTypeName()
	{	
		
		if(_itemType == DefinitionGlobal.ITEM_TYPE_PLAYER_CLASS)
			return DefinitionGlobal.EQUIP_TYPE_NAMES[7];
		
		else if(_itemType == DefinitionGlobal.ITEM_TYPE_WEAPON)
			return DefinitionGlobal.EQUIP_TYPE_NAMES[1];
		
		else if(_itemType == DefinitionGlobal.ITEM_TYPE_ITEM)
			return DefinitionGlobal.EQUIP_TYPE_NAMES[5];
		
		else if(_itemType == DefinitionGlobal.ITEM_TYPE_RUNE_ABILITY)
			return DefinitionGlobal.EQUIP_TYPE_NAMES[6];
		
		else if(_itemType == DefinitionGlobal.ITEM_TYPE_ARMOR && _equippableSlots[0] == DefinitionGlobal.EQUIP_SLOT_HELM[0])
			return DefinitionGlobal.EQUIP_TYPE_NAMES[0];
		
		else if(_itemType == DefinitionGlobal.ITEM_TYPE_ARMOR && _equippableSlots[0] == DefinitionGlobal.EQUIP_SLOT_CHEST[0])
			return DefinitionGlobal.EQUIP_TYPE_NAMES[2];
		
		else if(_itemType == DefinitionGlobal.ITEM_TYPE_ARMOR && _equippableSlots[0] == DefinitionGlobal.EQUIP_SLOT_SHOES[0])
			return DefinitionGlobal.EQUIP_TYPE_NAMES[3];
		
		else 
			return DefinitionGlobal.EQUIP_TYPE_NAMES[4];
		
	}
	
	public void setImageName(String n, String an, Context c)
	{
		_animationImageName = an;
		_imageName = n;
		setAnimationImageResource(c);
		setImageResource(c);
	}
	
	public int cost()
	{
		if(itemType() == DefinitionGlobal.ITEM_TYPE_WEAPON)
		{
			return Helper.getWeaponCost(id());
		}
		
		return _cost;
	}
	public void setCost(int c)
	{
		_cost = c;
	}

	public StoreItem(int t, int i)
	{
		_itemType = t;
		_itemID = i;
		
		setEquippableSlots(t, i);
	}
	
	public void setModifiedName(String s)
	{
		_modifiedName = s;
	}
	
	public String modifiedName()
	{
		return _modifiedName;
	}
	
	public void setBound(boolean b)
	{
		_isBound = b;
	}
	
	public boolean bound()
	{
		return _isBound;
	}
	private void setEquippableSlots(int type, int id)
	{
		if(type == DefinitionGlobal.ITEM_TYPE_ARMOR)
		{
			setEquippableSlots(DefinitionArmor.ARMOR_SLOT[id]);
		}
		if(type == DefinitionGlobal.ITEM_TYPE_WEAPON)
		{
			setEquippableSlots(DefinitionGlobal.EQUIP_SLOT_WEAPON);
		}
		if(type == DefinitionGlobal.ITEM_TYPE_ITEM)
		{
			setEquippableSlots(DefinitionGlobal.EQUIP_SLOT_ITEM);
		}		
	}
	
	public void setAvailForANewPlayer(int a)
	{
		_availForANewPlayer = a;
		if(_availForANewPlayer == 1)
			setBound(true);
	}
	
	public void setChargesLeft(int c)
	{
		Log.d("itemsetup",name()+" chargesLeft set to "+c);
		_chargesLeft = c;
	}
	
	public int availForANewPlayer()
	{
		return _availForANewPlayer;
	}
	
	public int animationImageResource()
	{
		return _animationImageResource;
	}
	
	public int imageResource()
	{
		return _imageResource;
	}
	
	public void setEquippedSlot(int s)
	{
		_equippedSlot = s;
	}
	public int equippedSlot()
	{
		return _equippedSlot;
	}

	
	public void setChargesPurchased(int c)
	{
		Log.d("itemsetup",name()+" chargesPurchased set to "+c);
		_chargesPurchased = c;
	}

	public void useCharge()
	{
		Log.d("itemsetup",name()+" used a charge");
		_chargesLeft--;
	}
	public void addCharge()
	{
		_chargesLeft++;
		if(_chargesLeft > _chargesPurchased)
			_chargesLeft = _chargesPurchased;
		
		if(_chargesLeft > _maxCharges)
			_chargesLeft = _maxCharges;
		
	}
	public void rechargeAll()
	{
		Log.d("itemsetup",name()+" was recharged to "+_chargesLeft);
		_chargesLeft = _chargesPurchased;
	}
	public int chargesLeft()
	{
		return _chargesLeft;
	}
	public void setMaxCharges(int m)
	{
		_maxCharges = m;
	}
	public int maxCharges()
	{
		return _maxCharges;
	}
	public int chargesPurchased()
	{
		return _chargesPurchased;
	}
	
	public void setMinLevel(int i)
	{
		_minLevel = i;
	}
	
	public int minLevel()
	{
		return _minLevel;
	}
	
	public void setValue(int v)
	{
		_value = v;
	}
	
	public int value()
	{
		if(itemType() == DefinitionGlobal.ITEM_TYPE_WEAPON)
		{
			return Math.round(Helper.getWeaponCost(id()) / DefinitionGlobal.ITEM_VALUE_DIVISOR);
		}
		
		return _value;
	}
	
	public void setAnimationImageResource(Context context)
	{
		if(_animationImageName.length() > 0)
		_animationImageResource = context.getResources().getIdentifier(_animationImageName, "drawable", context.getPackageName());
	}
	
	public void setImageResource(Context context)
	{
		_imageResource = context.getResources().getIdentifier(_imageName, "drawable", context.getPackageName());
	}

	public void setEquippableSlots(int[] s)
	{
		_equippableSlots = new int[s.length];
		
		for(int a = 0; a < s.length; a++)
		{
			_equippableSlots[a] = s[a];
		}
		
		Log.d("stuff","set equippable slots called for "+_name+" size "+_equippableSlots.length);
	}

	public int[] equippableSlots()
	{
		Log.d("stuff","return equippable slots called for "+_name);
		
		if (_equippableSlots == null)
			return null;
		
		
		int[] es = new int[_equippableSlots.length];
		
		for(int a = 0; a < _equippableSlots.length; a++)
			es[a] = _equippableSlots[a];
		
		Log.d("stuff","returning equippable slots for "+_name+" size "+es.length);
		return es;
	}

	public void setAmount(int a)
	{
		Log.d("playerstore",_name+" amount was set to "+a);
		_amount = a;
	}

	public int amount()
	{
		return _amount;
	}

	public int itemType()
	{
		return _itemType;
	}

	public int id()
	{
		return _itemID;
	}

	public void setName(String n)
	{
		_name = n;
	}

	public String nameAndAmount()
	{
		String s = _name;
		if (_amount > 1)
			s = _name + "(" + _amount + ")";
		return s;
	}

	public String name()
	{
		return _name;
	}

	public void setDescription(String d)
	{
		_description = d;
	}

	public String description()
	{
		return _description;
	}

	public int[] onlyForClasses()
	{
		return _onlyForClasses;
	}

	public void setOnlyForClasses(int[] _onlyForClasses)
	{
		if (this._onlyForClasses == null)
		{
			this._onlyForClasses = new int[_onlyForClasses.length];
		}

		for (int a = 0; a < this._onlyForClasses.length; a++)
		{
			this._onlyForClasses[a] = _onlyForClasses[a];
		}
	}

	public boolean canUseWithClassId(int cls)
	{
		for (int a = 0; a < this._onlyForClasses.length; a++)
		{
			if (cls == this._onlyForClasses[a])
				return true;
		}

		return false;
	}

	public int showsInStore()
	{
		return _showsInStore;
	}

	public void setShowsInStore(int _showsInStore)
	{
		this._showsInStore = _showsInStore;
	}
}
