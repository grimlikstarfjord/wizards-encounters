package com.alderangaming.wizardsencounters;

public class DefinitionAttackTypes
{
	public static final String[] ATTACK_TYPE_NAMES =
	{ "Swing", "Parry", "Block", "Deal", "Pass", "Stab", "Slash", "Thrash", "Cut", "Fling", "Bash", "Shoot", "Stun",
		"Deflect", "Head Shot", "Volley", "Groin Shot" };

	public static final int[] ATTACK_TYPE_HIT_CHANCE =
	{ 40, 10, -1, 20, 40, 50, 30, 27, 40, 31, 50, 70, 45, -1, 30, 60, 30, };

	public static final int[] ATTACK_TYPE_MIN_DAMAGE =
	{ 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0 };

	public static final int[] ATTACK_TYPE_MAX_DAMAGE =
	{ 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0 };

	public static final int[] ATTACK_TYPE_STUN_CHANCE =
	{ 4, 30, 0, 4, 5, 6, 4, 3, 6, 19, 0, 4, 20, 0, 0, 5, 50, };

	public static final int[] ATTACK_TYPE_CRIT_CHANCE =
	{ 4, 20, 0, 4, 5, 6, 4, 11, 6, 0, 12, 4, 0, 0, 45, 5, 0, };

	public static final int[] ATTACK_TYPE_BLOCK_NEXT_DAMAGE_PERCENT =
	{ 0, 17, 60, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 0, 0, 0, };

	public static final int[] ATTACK_TYPE_ANIMATION =
	{ Animator.SWING, Animator.PARRY, Animator.BLOCK, Animator.DEAL, Animator.SWING, Animator.STAB, Animator.SWING,
		Animator.SWING, Animator.SWING, Animator.DEAL, Animator.SWING, Animator.SHOOT_PROJECTILE, Animator.SWING,
		Animator.BLOCK, Animator.SHOOT_PROJECTILE, Animator.SHOOT_PROJECTILE, Animator.SHOOT_PROJECTILE };

	public static final int[] ATTACK_SOUND_CLIPS =
	{ SoundManager.SOUND_TYPE_SWING, SoundManager.SOUND_TYPE_PARRY, SoundManager.SOUND_TYPE_BLOCK,
		SoundManager.SOUND_TYPE_DEAL, SoundManager.SOUND_TYPE_DEAL, SoundManager.SOUND_TYPE_STAB,
		SoundManager.SOUND_TYPE_SLASH, SoundManager.SOUND_TYPE_SLASH, SoundManager.SOUND_TYPE_STAB,
		SoundManager.SOUND_TYPE_DEAL, SoundManager.SOUND_TYPE_BASH, SoundManager.SOUND_TYPE_SHOOT,
		SoundManager.SOUND_TYPE_STUN, SoundManager.SOUND_TYPE_SHOOT, SoundManager.SOUND_TYPE_SHOOT,
		SoundManager.SOUND_TYPE_SHOOT, SoundManager.SOUND_TYPE_SHOOT };
}
