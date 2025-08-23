package com.alderangaming.wizardsencounters;

public class DefinitionAttackTypes
{
	public static final String[] ATTACK_TYPE_NAMES = {
		"Swing",
		"Parry",
		"Block",
		"Deal",
		"Pass",
		"Stab",
		"Slash",
		"Thrash",
		"Cut",
		"Fling",
		"Bash",
		"Shoot",
		"Stun"
	};
	
	public static final int[] ATTACK_TYPE_HIT_CHANCE = {
		40,
		10,
		-1,
		20,
		40,
		50,
		30,
		20,
		40,
		50,
		50,
		50,
		50
	};
	
	public static final int[] ATTACK_TYPE_MIN_DAMAGE = {
		0,
		0,
		-1,
		0,
		0,
		0,
		0,
		0,
		0,
		0,
		0,
		0,
		0
	};
	
	public static final int[] ATTACK_TYPE_MAX_DAMAGE = {
		0,
		0,
		-1,
		0,
		0,
		0,
		0,
		0,
		0,
		0,
		0,
		0,
		0
	};
	
	public static final int[] ATTACK_TYPE_STUN_CHANCE = {
		4,
		30,
		0,
		4,
		5,
		6,
		4,
		5,
		6,
		4,
		4,
		4,
		20
	};
	
	public static final int[] ATTACK_TYPE_CRIT_CHANCE = {
		4,
		20,
		0,
		4,
		5,
		6,
		4,
		5,
		6,
		4,
		4,
		4,
		0
	};
	
	public static final int[] ATTACK_TYPE_BLOCK_NEXT_DAMAGE_PERCENT = {
		0,
		30,
		40,
		0,
		0,
		0,
		0,
		0,
		0,
		0,
		0,
		0,
		0
	};
	
	public static final int[] ATTACK_TYPE_ANIMATION = {
		0,
		1,
		2,
		3,
		4,
		5,
		6,
		7,
		8,
		9,
		10,
		11,
		12
	};
	
	public static final int[] ATTACK_SOUND_CLIPS = {
		SoundManager.SOUND_TYPE_SWING,
		SoundManager.SOUND_TYPE_PARRY,
		SoundManager.SOUND_TYPE_BLOCK,
		SoundManager.SOUND_TYPE_DEAL,
		SoundManager.SOUND_TYPE_DEAL,
		SoundManager.SOUND_TYPE_STAB,
		SoundManager.SOUND_TYPE_SLASH,
		SoundManager.SOUND_TYPE_SLASH,
		SoundManager.SOUND_TYPE_STAB,
		SoundManager.SOUND_TYPE_DEAL,
		SoundManager.SOUND_TYPE_BASH,
		SoundManager.SOUND_TYPE_SHOOT,
		SoundManager.SOUND_TYPE_STUN
	};			
}
