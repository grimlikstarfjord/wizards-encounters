package com.alderangaming.wizardsencounters;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.util.Log;

public class SoundManager
{
	private static boolean playSounds = true;
	private static boolean playMusic = true;

	private static ArrayList<Integer> musicResources = new ArrayList<Integer>();
	private static ArrayList<Integer> musicTypes = new ArrayList<Integer>();
	
	private static ArrayList<Integer> soundEffects = new ArrayList<Integer>();
	private static ArrayList<Integer> soundTypes = new ArrayList<Integer>();

	public static int playingMusicType = -1;
	public static int lastCombatSong = -1;
	private static MediaPlayer playingMusic = null;

	// NEW//
	private static SoundPool mSoundPool;
	private static AudioManager mAudioManager;

	public static final int SOUND_TYPE_MUSIC_MAIN_MENU = 0;
	public static final int SOUND_TYPE_MUSIC_COMBAT = 1;
	public static final int SOUND_TYPE_MUSIC_BOSS = 2;

	// weapon attacks
	public static final int SOUND_TYPE_SWING = 5;
	public static final int SOUND_TYPE_PARRY = 6;
	public static final int SOUND_TYPE_STAB = 25;
	public static final int SOUND_TYPE_BLOCK = 7;
	public static final int SOUND_TYPE_DEAL = 8;
	public static final int SOUND_TYPE_SLASH = 9;
	public static final int SOUND_TYPE_BASH = 10;
	public static final int SOUND_TYPE_SHOOT = 11;
	public static final int SOUND_TYPE_STUN = 12;

	// combat sounds
	public static final int SOUND_TYPE_EXPLOSION_LARGE = 3;
	public static final int SOUND_TYPE_EXPLOSION_SMALL = 4;
	public static final int SOUND_TYPE_MISS = 13;
	public static final int SOUND_TYPE_DODGE = 26;
	public static final int TAKE_HIT = 28;
	public static final int FIGHT_WON = 29;

	// monster generic sounds
	public static final int SOUND_TYPE_MONSTER_DEAD = 14;
	public static final int SOUND_TYPE_MONSTER_TAKE_DAMAGE = 15;

	// monster taunts
	public static final int SOUND_TYPE_MONSTER_TAUNT_BEAST = 16;
	public static final int SOUND_TYPE_MONSTER_TAUNT_HUMAN_MALE = 17;
	public static final int SOUND_TYPE_MONSTER_TAUNT_HUMAN_FEMALE = 18;
	public static final int SOUND_TYPE_MONSTER_TAUNT_GHOST = 19;
	public static final int SOUND_TYPE_MONSTER_TAUNT_UNDEAD_HAPPY = 20;
	public static final int SOUND_TYPE_MONSTER_TAUNT_UNDEAD_SAD = 21;
	public static final int SOUND_TYPE_MONSTER_TAUNT_HUMAN_MALE_GRUMPY = 22;
	public static final int SOUND_TYPE_MONSTER_TAUNT_HYPER = 23;
	public static final int SOUND_TYPE_MONSTER_TAUNT_SLIMY = 24;

	// abilities
	public static final int CHANTOFTHEWILDERNESS = 30;
	public static final int FISTSOFTRUTH = 31;
	public static final int UNDERWORLDASSAULT = 32;
	public static final int MINDBULLETS = 33;
	public static final int PRECISIONBLOW = 34;
	public static final int RAPIDFIRE = 35;

	// items
	public static final int MAGIC_MIRROR = 27;
	
	//last index = 35

	public static void pauseSong()
	{
		Log.d("sounds", "pause song");
		if (playingMusic != null)
			playingMusic.pause();
	}

	public static void resumeSong()
	{
		Log.d("sounds", "resume song");
		if (playingMusic != null)
			playingMusic.start();
	}

	public static void initSoundManager(Context context)
	{
		mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		loadSoundEffects(context);
		setupAudio(context);
	}
	
	private static void loadSoundEffects(Context context)
	{		
		soundTypes.clear();
		soundEffects.clear();
				
		soundEffects.add(mSoundPool.load(context, R.raw.explosion1, 1));
		soundTypes.add(SOUND_TYPE_EXPLOSION_LARGE);

		soundEffects.add(mSoundPool.load(context, R.raw.explosion2, 1));
		soundTypes.add(SOUND_TYPE_EXPLOSION_LARGE);

		soundEffects.add(mSoundPool.load(context, R.raw.explosion3, 1));
		soundTypes.add(SOUND_TYPE_EXPLOSION_LARGE);

		soundEffects.add(mSoundPool.load(context, R.raw.explosion4, 1));
		soundTypes.add(SOUND_TYPE_EXPLOSION_SMALL);

		soundEffects.add(mSoundPool.load(context, R.raw.explosion5, 1));
		soundTypes.add(SOUND_TYPE_EXPLOSION_SMALL);

		soundEffects.add(mSoundPool.load(context, R.raw.explosion6, 1));
		soundTypes.add(SOUND_TYPE_EXPLOSION_SMALL);

		soundEffects.add(mSoundPool.load(context, R.raw.dodge1, 1));
		soundTypes.add(SOUND_TYPE_DODGE);

		soundEffects.add(mSoundPool.load(context, R.raw.bash1, 1));
		soundTypes.add(SOUND_TYPE_BASH);

		soundEffects.add(mSoundPool.load(context, R.raw.bash2, 1));
		soundTypes.add(SOUND_TYPE_BASH);

		soundEffects.add(mSoundPool.load(context, R.raw.bash3, 1));
		soundTypes.add(SOUND_TYPE_BASH);

		soundEffects.add(mSoundPool.load(context, R.raw.bash4, 1));
		soundTypes.add(SOUND_TYPE_BASH);

		soundEffects.add(mSoundPool.load(context, R.raw.bash5, 1));
		soundTypes.add(SOUND_TYPE_BASH);

		soundEffects.add(mSoundPool.load(context, R.raw.block1, 1));
		soundTypes.add(SOUND_TYPE_BLOCK);

		soundEffects.add(mSoundPool.load(context, R.raw.cardflip, 1));
		soundTypes.add(SOUND_TYPE_DEAL);

		soundEffects.add(mSoundPool.load(context, R.raw.cardflip2, 1));
		soundTypes.add(SOUND_TYPE_DEAL);

		soundEffects.add(mSoundPool.load(context, R.raw.dicethrow, 1));
		soundTypes.add(SOUND_TYPE_DEAL);

		soundEffects.add(mSoundPool.load(context, R.raw.dicethrow2, 1));
		soundTypes.add(SOUND_TYPE_DEAL);

		soundEffects.add(mSoundPool.load(context, R.raw.dicethrow3, 1));
		soundTypes.add(SOUND_TYPE_DEAL);

		soundEffects.add(mSoundPool.load(context, R.raw.dicethrow4, 1));
		soundTypes.add(SOUND_TYPE_DEAL);

		soundEffects.add(mSoundPool.load(context, R.raw.pass, 1));
		soundTypes.add(SOUND_TYPE_DEAL);

		soundEffects.add(mSoundPool.load(context, R.raw.parry1, 1));
		soundTypes.add(SOUND_TYPE_PARRY);

		soundEffects.add(mSoundPool.load(context, R.raw.parry2, 1));
		soundTypes.add(SOUND_TYPE_PARRY);

		soundEffects.add(mSoundPool.load(context, R.raw.parry3, 1));
		soundTypes.add(SOUND_TYPE_PARRY);

		soundEffects.add(mSoundPool.load(context, R.raw.shoot1, 1));
		soundTypes.add(SOUND_TYPE_SHOOT);

		soundEffects.add(mSoundPool.load(context, R.raw.shoot2, 1));
		soundTypes.add(SOUND_TYPE_SHOOT);

		soundEffects.add(mSoundPool.load(context, R.raw.shoot3, 1));
		soundTypes.add(SOUND_TYPE_SHOOT);

		soundEffects.add(mSoundPool.load(context, R.raw.slash1, 1));
		soundTypes.add(SOUND_TYPE_SLASH);

		soundEffects.add(mSoundPool.load(context, R.raw.slash2, 1));
		soundTypes.add(SOUND_TYPE_SLASH);

		soundEffects.add(mSoundPool.load(context, R.raw.stab4, 1));
		soundTypes.add(SOUND_TYPE_SLASH);

		soundEffects.add(mSoundPool.load(context, R.raw.stab1, 1));
		soundTypes.add(SOUND_TYPE_STAB);

		soundEffects.add(mSoundPool.load(context, R.raw.stab4, 1));
		soundTypes.add(SOUND_TYPE_STAB);

		soundEffects.add(mSoundPool.load(context, R.raw.stab5, 1));
		soundTypes.add(SOUND_TYPE_STAB);

		soundEffects.add(mSoundPool.load(context, R.raw.stab6, 1));
		soundTypes.add(SOUND_TYPE_STAB);

		soundEffects.add(mSoundPool.load(context, R.raw.stab7, 1));
		soundTypes.add(SOUND_TYPE_STAB);

		soundEffects.add(mSoundPool.load(context, R.raw.stab8, 1));
		soundTypes.add(SOUND_TYPE_STAB);

		soundEffects.add(mSoundPool.load(context, R.raw.weaponimpact1, 1));
		soundTypes.add(SOUND_TYPE_STUN);

		soundEffects.add(mSoundPool.load(context, R.raw.weaponimpact1, 1));
		soundTypes.add(SOUND_TYPE_SWING);

		soundEffects.add(mSoundPool.load(context, R.raw.weaponimpact2, 1));
		soundTypes.add(SOUND_TYPE_SWING);

		soundEffects.add(mSoundPool.load(context, R.raw.weaponimpact3, 1));
		soundTypes.add(SOUND_TYPE_SWING);

		soundEffects.add(mSoundPool.load(context, R.raw.miss1, 1));
		soundTypes.add(SOUND_TYPE_MISS);

		soundEffects.add(mSoundPool.load(context, R.raw.miss2, 1));
		soundTypes.add(SOUND_TYPE_MISS);

		soundEffects.add(mSoundPool.load(context, R.raw.miss3, 1));
		soundTypes.add(SOUND_TYPE_MISS);

		soundEffects.add(mSoundPool.load(context, R.raw.dead1, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_DEAD);

		soundEffects.add(mSoundPool.load(context, R.raw.dead2, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_DEAD);

		soundEffects.add(mSoundPool.load(context, R.raw.dead3, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_DEAD);

		soundEffects.add(mSoundPool.load(context, R.raw.agh, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAKE_DAMAGE);

		soundEffects.add(mSoundPool.load(context, R.raw.takedamage1, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAKE_DAMAGE);

		soundEffects.add(mSoundPool.load(context, R.raw.takedamage2, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAKE_DAMAGE);

		soundEffects.add(mSoundPool.load(context, R.raw.takedamage3, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAKE_DAMAGE);

		soundEffects.add(mSoundPool.load(context, R.raw.takedamage4, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAKE_DAMAGE);

		soundEffects.add(mSoundPool.load(context, R.raw.tauntbeast0, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_BEAST);

		soundEffects.add(mSoundPool.load(context, R.raw.tauntbeast1, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_BEAST);

		soundEffects.add(mSoundPool.load(context, R.raw.tauntbeast2, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_BEAST);

		soundEffects.add(mSoundPool.load(context, R.raw.tauntbeast3, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_BEAST);

		soundEffects.add(mSoundPool.load(context, R.raw.tauntbeast4, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_BEAST);

		soundEffects.add(mSoundPool.load(context, R.raw.tauntingmale1, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HUMAN_MALE);

		soundEffects.add(mSoundPool.load(context, R.raw.tauntingmale2, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HUMAN_MALE);

		soundEffects.add(mSoundPool.load(context, R.raw.tauntingmale3, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HUMAN_MALE);

		soundEffects.add(mSoundPool.load(context, R.raw.tauntfemale1, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HUMAN_FEMALE);

		soundEffects.add(mSoundPool.load(context, R.raw.tauntfemale2, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HUMAN_FEMALE);

		soundEffects.add(mSoundPool.load(context, R.raw.tauntfemale3, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HUMAN_FEMALE);

		soundEffects.add(mSoundPool.load(context, R.raw.tauntfemale4, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HUMAN_FEMALE);

		soundEffects.add(mSoundPool.load(context, R.raw.taughtghost1, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_GHOST);

		soundEffects.add(mSoundPool.load(context, R.raw.taughtghost2, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_GHOST);

		soundEffects.add(mSoundPool.load(context, R.raw.taughtghost3, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_GHOST);

		soundEffects.add(mSoundPool.load(context, R.raw.tauntundeadhappy1, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_UNDEAD_HAPPY);

		soundEffects.add(mSoundPool.load(context, R.raw.tauntundeadsad1, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_UNDEAD_SAD);

		soundEffects.add(mSoundPool.load(context, R.raw.tauntgrumpy1, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HUMAN_MALE_GRUMPY);

		soundEffects.add(mSoundPool.load(context, R.raw.tauntgrumpy2, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HUMAN_MALE_GRUMPY);

		soundEffects.add(mSoundPool.load(context, R.raw.tauntgrumpy3, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HUMAN_MALE_GRUMPY);

		soundEffects.add(mSoundPool.load(context, R.raw.taunthyper1, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HYPER);

		soundEffects.add(mSoundPool.load(context, R.raw.taunthyper2, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HYPER);

		soundEffects.add(mSoundPool.load(context, R.raw.taunthyper3, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HYPER);

		soundEffects.add(mSoundPool.load(context, R.raw.tauntslimy1, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_SLIMY);

		soundEffects.add(mSoundPool.load(context, R.raw.tauntslimy2, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_SLIMY);

		soundEffects.add(mSoundPool.load(context, R.raw.tauntslimy3, 1));
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_SLIMY);
		
		soundEffects.add(mSoundPool.load(context, R.raw.magicmirror, 1));
		soundTypes.add(MAGIC_MIRROR);
		
		soundEffects.add(mSoundPool.load(context, R.raw.takehit, 1));
		soundTypes.add(TAKE_HIT);
		
		soundEffects.add(mSoundPool.load(context, R.raw.fightwon, 1));
		soundTypes.add(FIGHT_WON);
		
		soundEffects.add(mSoundPool.load(context, R.raw.chantofthewilderness, 1));
		soundTypes.add(CHANTOFTHEWILDERNESS);
		soundEffects.add(mSoundPool.load(context, R.raw.fistsoftruth, 1));
		soundTypes.add(FISTSOFTRUTH);
		soundEffects.add(mSoundPool.load(context, R.raw.underworldassault, 1));
		soundTypes.add(UNDERWORLDASSAULT);
		soundEffects.add(mSoundPool.load(context, R.raw.mindbullets, 1));
		soundTypes.add(MINDBULLETS);
		soundEffects.add(mSoundPool.load(context, R.raw.precisionblow, 1));
		soundTypes.add(PRECISIONBLOW);
		soundEffects.add(mSoundPool.load(context, R.raw.rapidfire, 1));
		soundTypes.add(RAPIDFIRE);
	}

	public static void playSound(int soundType)
	{
		if (!playSounds)
			return;

		Log.d("sounds", "play sound type " + soundType);

		ArrayList<Integer> possibleSounds = new ArrayList<Integer>();

		for (int a = 0; a < soundTypes.size(); a++)
		{
			if (soundTypes.get(a) == soundType)
			{
				possibleSounds.add(a);
				Log.d("sounds","possible sounds: "+a);
			}
		}

		int clipId = Helper.randomInt(possibleSounds.size());
				
		Log.d("sounds","picked sound is index "+clipId+" = "+possibleSounds.get(clipId));
		
		float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(soundEffects.get(possibleSounds.get(clipId)), streamVolume, streamVolume, 1, 0, 1.0f);
	}

	private static void setupAudio(Context context)
	{
		musicResources.add(R.raw.battlesong1);
		musicTypes.add(SOUND_TYPE_MUSIC_MAIN_MENU);

		musicResources.add(R.raw.battlesong2);
		musicTypes.add(SOUND_TYPE_MUSIC_COMBAT);

		musicResources.add(R.raw.battlesong2);
		musicTypes.add(SOUND_TYPE_MUSIC_BOSS);

		musicResources.add(R.raw.battlesong4low);
		musicTypes.add(SOUND_TYPE_MUSIC_COMBAT);

		musicResources.add(R.raw.battlesong3);
		musicTypes.add(SOUND_TYPE_MUSIC_COMBAT);

		/*
		soundResources.add(R.raw.explosion1);
		soundTypes.add(SOUND_TYPE_EXPLOSION_LARGE);

		soundResources.add(R.raw.explosion2);
		soundTypes.add(SOUND_TYPE_EXPLOSION_LARGE);

		soundResources.add(R.raw.explosion3);
		soundTypes.add(SOUND_TYPE_EXPLOSION_LARGE);

		soundResources.add(R.raw.explosion4);
		soundTypes.add(SOUND_TYPE_EXPLOSION_SMALL);

		soundResources.add(R.raw.explosion5);
		soundTypes.add(SOUND_TYPE_EXPLOSION_SMALL);

		soundResources.add(R.raw.explosion6);
		soundTypes.add(SOUND_TYPE_EXPLOSION_SMALL);

		soundResources.add(R.raw.dodge1);
		soundTypes.add(SOUND_TYPE_DODGE);

		soundResources.add(R.raw.bash1);
		soundTypes.add(SOUND_TYPE_BASH);

		soundResources.add(R.raw.bash2);
		soundTypes.add(SOUND_TYPE_BASH);

		soundResources.add(R.raw.bash3);
		soundTypes.add(SOUND_TYPE_BASH);

		soundResources.add(R.raw.bash4);
		soundTypes.add(SOUND_TYPE_BASH);

		soundResources.add(R.raw.bash5);
		soundTypes.add(SOUND_TYPE_BASH);

		soundResources.add(R.raw.block1);
		soundTypes.add(SOUND_TYPE_BLOCK);

		soundResources.add(R.raw.cardflip);
		soundTypes.add(SOUND_TYPE_DEAL);

		soundResources.add(R.raw.cardflip2);
		soundTypes.add(SOUND_TYPE_DEAL);

		soundResources.add(R.raw.dicethrow);
		soundTypes.add(SOUND_TYPE_DEAL);

		soundResources.add(R.raw.dicethrow2);
		soundTypes.add(SOUND_TYPE_DEAL);

		soundResources.add(R.raw.dicethrow3);
		soundTypes.add(SOUND_TYPE_DEAL);

		soundResources.add(R.raw.dicethrow4);
		soundTypes.add(SOUND_TYPE_DEAL);

		soundResources.add(R.raw.pass);
		soundTypes.add(SOUND_TYPE_DEAL);

		soundResources.add(R.raw.parry1);
		soundTypes.add(SOUND_TYPE_PARRY);

		soundResources.add(R.raw.parry2);
		soundTypes.add(SOUND_TYPE_PARRY);

		soundResources.add(R.raw.parry3);
		soundTypes.add(SOUND_TYPE_PARRY);

		soundResources.add(R.raw.shoot1);
		soundTypes.add(SOUND_TYPE_SHOOT);

		soundResources.add(R.raw.shoot2);
		soundTypes.add(SOUND_TYPE_SHOOT);

		soundResources.add(R.raw.shoot3);
		soundTypes.add(SOUND_TYPE_SHOOT);

		soundResources.add(R.raw.slash1);
		soundTypes.add(SOUND_TYPE_SLASH);

		soundResources.add(R.raw.slash2);
		soundTypes.add(SOUND_TYPE_SLASH);

		soundResources.add(R.raw.stab4);
		soundTypes.add(SOUND_TYPE_SLASH);

		soundResources.add(R.raw.stab1);
		soundTypes.add(SOUND_TYPE_STAB);

		soundResources.add(R.raw.stab4);
		soundTypes.add(SOUND_TYPE_STAB);

		soundResources.add(R.raw.stab5);
		soundTypes.add(SOUND_TYPE_STAB);

		soundResources.add(R.raw.stab6);
		soundTypes.add(SOUND_TYPE_STAB);

		soundResources.add(R.raw.stab7);
		soundTypes.add(SOUND_TYPE_STAB);

		soundResources.add(R.raw.stab8);
		soundTypes.add(SOUND_TYPE_STAB);

		soundResources.add(R.raw.weaponimpact1);
		soundTypes.add(SOUND_TYPE_STUN);

		soundResources.add(R.raw.weaponimpact1);
		soundTypes.add(SOUND_TYPE_SWING);

		soundResources.add(R.raw.weaponimpact2);
		soundTypes.add(SOUND_TYPE_SWING);

		soundResources.add(R.raw.weaponimpact3);
		soundTypes.add(SOUND_TYPE_SWING);

		soundResources.add(R.raw.miss1);
		soundTypes.add(SOUND_TYPE_MISS);

		soundResources.add(R.raw.miss2);
		soundTypes.add(SOUND_TYPE_MISS);

		soundResources.add(R.raw.miss3);
		soundTypes.add(SOUND_TYPE_MISS);

		soundResources.add(R.raw.dead1);
		soundTypes.add(SOUND_TYPE_MONSTER_DEAD);

		soundResources.add(R.raw.dead2);
		soundTypes.add(SOUND_TYPE_MONSTER_DEAD);

		soundResources.add(R.raw.dead3);
		soundTypes.add(SOUND_TYPE_MONSTER_DEAD);

		soundResources.add(R.raw.agh);
		soundTypes.add(SOUND_TYPE_MONSTER_TAKE_DAMAGE);

		soundResources.add(R.raw.takedamage1);
		soundTypes.add(SOUND_TYPE_MONSTER_TAKE_DAMAGE);

		soundResources.add(R.raw.takedamage2);
		soundTypes.add(SOUND_TYPE_MONSTER_TAKE_DAMAGE);

		soundResources.add(R.raw.takedamage3);
		soundTypes.add(SOUND_TYPE_MONSTER_TAKE_DAMAGE);

		soundResources.add(R.raw.takedamage4);
		soundTypes.add(SOUND_TYPE_MONSTER_TAKE_DAMAGE);

		soundResources.add(R.raw.tauntbeast0);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_BEAST);

		soundResources.add(R.raw.tauntbeast1);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_BEAST);

		soundResources.add(R.raw.tauntbeast2);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_BEAST);

		soundResources.add(R.raw.tauntbeast3);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_BEAST);

		soundResources.add(R.raw.tauntbeast4);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_BEAST);

		soundResources.add(R.raw.tauntingmale1);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HUMAN_MALE);

		soundResources.add(R.raw.tauntingmale2);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HUMAN_MALE);

		soundResources.add(R.raw.tauntingmale3);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HUMAN_MALE);

		soundResources.add(R.raw.tauntfemale1);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HUMAN_FEMALE);

		soundResources.add(R.raw.tauntfemale2);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HUMAN_FEMALE);

		soundResources.add(R.raw.tauntfemale3);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HUMAN_FEMALE);

		soundResources.add(R.raw.tauntfemale4);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HUMAN_FEMALE);

		soundResources.add(R.raw.taughtghost1);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_GHOST);

		soundResources.add(R.raw.taughtghost2);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_GHOST);

		soundResources.add(R.raw.taughtghost3);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_GHOST);

		soundResources.add(R.raw.tauntundeadhappy1);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_UNDEAD_HAPPY);

		soundResources.add(R.raw.tauntundeadsad1);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_UNDEAD_SAD);

		soundResources.add(R.raw.tauntgrumpy1);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HUMAN_MALE_GRUMPY);

		soundResources.add(R.raw.tauntgrumpy2);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HUMAN_MALE_GRUMPY);

		soundResources.add(R.raw.tauntgrumpy3);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HUMAN_MALE_GRUMPY);

		soundResources.add(R.raw.taunthyper1);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HYPER);

		soundResources.add(R.raw.taunthyper2);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HYPER);

		soundResources.add(R.raw.taunthyper3);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_HYPER);

		soundResources.add(R.raw.tauntslimy1);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_SLIMY);

		soundResources.add(R.raw.tauntslimy2);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_SLIMY);

		soundResources.add(R.raw.tauntslimy3);
		soundTypes.add(SOUND_TYPE_MONSTER_TAUNT_SLIMY);
		
		*/

	}

	public static void setPlaySounds(boolean b)
	{
		Log.d("sounds", "set play sounds " + b);
		playSounds = b;
	}

	public static void setPlayMusic(boolean b)
	{
		Log.d("sounds", "set play music " + b);
		playMusic = b;

		if (!playMusic)
			stopAllMusic();
	}
	
	public static void playNextCombatSong(Context context)
	{
		if (!playMusic)
			return;
		
		playingMusicType = SOUND_TYPE_MUSIC_COMBAT;		
		
		ArrayList<Integer> possibleSongs = new ArrayList<Integer>();
		for (int a = 0; a < musicTypes.size(); a++)
		{
			if (musicTypes.get(a) == playingMusicType)
				possibleSongs.add(a);
		}
		
		int newsong = lastCombatSong;
		while(newsong == lastCombatSong)
			newsong = possibleSongs.get(Helper.randomInt(possibleSongs.size()));
		
		lastCombatSong = newsong;		
		
		playThisSong(newsong, context, true);
	}
	
	private static void playThisSong(int clipId, Context context, boolean repeat)
	{
		if (playingMusic != null)
		{
			if (playingMusic.isPlaying())
			{
				playingMusic.stop();
			}
			playingMusic.reset();
		}
		
		playingMusic = MediaPlayer.create(context, musicResources.get(clipId));
		playingMusic.setLooping(repeat);
		if (!repeat)
		{
			playingMusic.setOnCompletionListener(new OnCompletionListener()
			{
				@Override
				public void onCompletion(MediaPlayer mp)
				{
					try
					{
						playingMusic.release();
					}
					catch (IllegalStateException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
		playingMusic.start();
	}

	public static void playMusic(int soundType, boolean repeat, Context context)
	{
		if (!playMusic)
			return;

		playingMusicType = soundType;

		ArrayList<Integer> possibleSongs = new ArrayList<Integer>();
		for (int a = 0; a < musicTypes.size(); a++)
		{
			if (musicTypes.get(a) == soundType)
				possibleSongs.add(a);
		}

		// pick a random song of this type
		int clipId = Helper.randomInt(possibleSongs.size());
		
		playThisSong(possibleSongs.get(clipId), context, repeat);
	}

	public static boolean playingMusic()
	{
		return playMusic;
	}

	public static boolean playingSounds()
	{
		return playSounds;
	}

	public static void stopAllMusic()
	{
		Log.d("sounds", "stop all music");
		if (playingMusic != null)
		{
			if (playingMusic.isPlaying())
			{
				playingMusic.stop();
				playingMusic.reset();
			}
		}
	}

	public static void stopAll()
	{
		Log.d("sounds", "stop all");

		stopAllMusic();
		mSoundPool.release();
		mAudioManager.unloadSoundEffects();
	}
}
