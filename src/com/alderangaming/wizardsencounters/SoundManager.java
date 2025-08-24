package com.alderangaming.wizardsencounters;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.util.Log;
import android.widget.Toast;

public class SoundManager {
	private static boolean playSounds = true;
	private static boolean playMusic = true;
	private static boolean showingImpact = true;
	private static float musicVolume = 0.3f; // 0.0 - 1.0 default 50%
	private static float soundVolume = 1.0f; // 0.0 - 1.0 default 50%

	private static ArrayList<Integer> musicResources = new ArrayList<Integer>();
	private static ArrayList<Integer> musicTypes = new ArrayList<Integer>();
	private static ArrayList<String> combatSongNames = new ArrayList<String>();

	private static ArrayList<Integer> soundEffectsGeneric = new ArrayList<Integer>();
	private static ArrayList<Integer> soundTypesGeneric = new ArrayList<Integer>();

	private static ArrayList<Integer> soundEffectsBattle = new ArrayList<Integer>();
	private static ArrayList<Integer> soundTypesBattle = new ArrayList<Integer>();

	public static int playingMusicType = -1;
	public static int lastCombatSongIndex = -1;
	private static MediaPlayer playingMusic = null;

	private static SoundPool mSoundPool;
	private static AudioManager mAudioManager;

	public static final int SOUND_TYPE_MUSIC_MAIN_MENU = 0;
	public static final int SOUND_TYPE_MUSIC_COMBAT = 1;
	public static final int SOUND_TYPE_MUSIC_BOSS = 2;
	public static final int SOUND_TYPE_MUSIC_TREASURE_ROOM = 3;

	public static void pauseSong() {
		Log.d("sounds", "pause song");
		if (playingMusic != null)
			playingMusic.pause();
	}

	public static void resumeSong() {
		Log.d("sounds", "resume song");
		if (playingMusic != null) {
			playingMusic.setVolume(musicVolume, musicVolume);
			playingMusic.start();
		}
	}

	public static void initSoundManager(Context context) {

		mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		makeSoundMap();
		loadGenericSoundEffects(context);
		setupAudio(context);
	}

	public static void loadBattleSoundEffects(Context context, int[] soundTypes) {

		for (int a = 0; a < soundTypes.length; a++) {
			Log.d("sounds", "loading sounds for type " + soundTypes[a]);

			// need to get all sounds associated with this sound type from the
			// soundMap
			SoundType s = soundMap.get(soundTypes[a]);

			if (s != null) {
				for (int b = 0; b < s.numSounds(); b++) {
					soundEffectsBattle.add(mSoundPool.load(context, s.getSoundResourceByIndex(b), 1));
					soundTypesBattle.add(soundTypes[a]);
				}
			}
		}
	}

	public static void unloadBattleSoundEffects() {
		for (int a = 0; a < soundEffectsBattle.size(); a++) {
			mSoundPool.unload(soundEffectsBattle.get(a));
		}
		soundEffectsBattle.clear();
		soundTypesBattle.clear();
	}

	private static void loadGenericSoundEffects(Context context) {
		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.explosion1, 1));
		soundTypesGeneric.add(SOUND_TYPE_EXPLOSION_LARGE);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.explosion2, 1));
		soundTypesGeneric.add(SOUND_TYPE_EXPLOSION_LARGE);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.explosion3, 1));
		soundTypesGeneric.add(SOUND_TYPE_EXPLOSION_LARGE);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.explosion4, 1));
		soundTypesGeneric.add(SOUND_TYPE_EXPLOSION_SMALL);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.explosion5, 1));
		soundTypesGeneric.add(SOUND_TYPE_EXPLOSION_SMALL);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.explosion6, 1));
		soundTypesGeneric.add(SOUND_TYPE_EXPLOSION_SMALL);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.dodge1, 1));
		soundTypesGeneric.add(SOUND_TYPE_DODGE);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.miss1, 1));
		soundTypesGeneric.add(SOUND_TYPE_MISS);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.miss2, 1));
		soundTypesGeneric.add(SOUND_TYPE_MISS);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.miss3, 1));
		soundTypesGeneric.add(SOUND_TYPE_MISS);

		// soundEffectsGeneric.add(mSoundPool.load(context, R.raw.dead1, 1));
		// soundTypesGeneric.add(SOUND_TYPE_MONSTER_DEAD);

		// soundEffectsGeneric.add(mSoundPool.load(context, R.raw.dead2, 1));
		// soundTypesGeneric.add(SOUND_TYPE_MONSTER_DEAD);

		// soundEffectsGeneric.add(mSoundPool.load(context, R.raw.dead3, 1));
		// soundTypesGeneric.add(SOUND_TYPE_MONSTER_DEAD);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.monsterdeath1, 1));
		soundTypesGeneric.add(SOUND_TYPE_DEATH_1);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.monsterdeath2, 1));
		soundTypesGeneric.add(SOUND_TYPE_DEATH_2);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.monsterdeath3, 1));
		soundTypesGeneric.add(SOUND_TYPE_DEATH_3);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.monsterdeath4, 1));
		soundTypesGeneric.add(SOUND_TYPE_DEATH_4);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.monsterdeath5, 1));
		soundTypesGeneric.add(SOUND_TYPE_DEATH_5);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.monsterdeath6, 1));
		soundTypesGeneric.add(SOUND_TYPE_DEATH_6);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.monsterdeath7, 1));
		soundTypesGeneric.add(SOUND_TYPE_DEATH_7);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.monsterdeath8, 1));
		soundTypesGeneric.add(SOUND_TYPE_DEATH_8);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.monsterdeath9, 1));
		soundTypesGeneric.add(SOUND_TYPE_DEATH_9);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.monsterdeath10, 1));
		soundTypesGeneric.add(SOUND_TYPE_DEATH_10);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.agh, 1));
		soundTypesGeneric.add(SOUND_TYPE_MONSTER_TAKE_DAMAGE);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.takedamage1, 1));
		soundTypesGeneric.add(SOUND_TYPE_MONSTER_TAKE_DAMAGE);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.takedamage2, 1));
		soundTypesGeneric.add(SOUND_TYPE_MONSTER_TAKE_DAMAGE);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.takedamage3, 1));
		soundTypesGeneric.add(SOUND_TYPE_MONSTER_TAKE_DAMAGE);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.takedamage4, 1));
		soundTypesGeneric.add(SOUND_TYPE_MONSTER_TAKE_DAMAGE);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.takehit, 1));
		soundTypesGeneric.add(TAKE_HIT);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.fightwon, 1));
		soundTypesGeneric.add(FIGHT_WON);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.openchestitem, 1));
		soundTypesGeneric.add(OPEN_CHEST_ITEM);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.openchestempty, 1));
		soundTypesGeneric.add(OPEN_CHEST_EMPTY);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.equip, 1));
		soundTypesGeneric.add(EQUIP_ITEM);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.click1, 1));
		soundTypesGeneric.add(CLICK);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.buysound, 1));
		soundTypesGeneric.add(BUYSOUND);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.sellsound, 1));
		soundTypesGeneric.add(SELLSOUND);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.alertsound, 1));
		soundTypesGeneric.add(ALERTSOUND);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.itemsound, 1));
		soundTypesGeneric.add(ITEMSOUND);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.rechargesound, 1));
		soundTypesGeneric.add(RECHARGESOUND);

		soundEffectsGeneric.add(mSoundPool.load(context, R.raw.playerdead, 1));
		soundTypesGeneric.add(PLAYERDEAD);

	}

	public static void playSound(int soundType, boolean isGeneric) {
		if (!playSounds)
			return;

		Log.d("sounds", "play sound type " + soundType);

		ArrayList<Integer> possibleSounds = new ArrayList<Integer>();
		int clipId = 0;

		if (isGeneric) {
			for (int a = 0; a < soundTypesGeneric.size(); a++) {
				if (soundTypesGeneric.get(a) == soundType) {
					possibleSounds.add(a);
					Log.d("sounds", "generic possible sounds: " + a);
				}
			}
		} else {
			for (int a = 0; a < soundTypesBattle.size(); a++) {
				if (soundTypesBattle.get(a) == soundType) {
					possibleSounds.add(a);
					Log.d("sounds", "battle possible sounds: " + a);
				}
			}
		}

		if (possibleSounds.size() < 1)
			return;

		clipId = Helper.randomInt(possibleSounds.size());

		Log.d("sounds", "picked sound is index " + clipId + " = " + possibleSounds.get(clipId));

		float streamVolume = soundVolume;
		if (isGeneric)
			mSoundPool
					.play(soundEffectsGeneric.get(possibleSounds.get(clipId)), streamVolume, streamVolume, 1, 0, 1.0f);
		else
			mSoundPool.play(soundEffectsBattle.get(possibleSounds.get(clipId)), streamVolume, streamVolume, 1, 0, 1.0f);
	}

	private static void setupAudio(Context context) {
		musicResources.add(R.raw.musicmainmenu);
		musicTypes.add(SOUND_TYPE_MUSIC_MAIN_MENU);

		musicResources.add(R.raw.musicbattle1);
		musicTypes.add(SOUND_TYPE_MUSIC_COMBAT);
		combatSongNames.add("Beware Falling Boulders");

		musicResources.add(R.raw.musicbattle2);
		musicTypes.add(SOUND_TYPE_MUSIC_COMBAT);
		combatSongNames.add("Not All That Different");

		musicResources.add(R.raw.musicbattle3);
		musicTypes.add(SOUND_TYPE_MUSIC_COMBAT);
		combatSongNames.add("Battle In The Raw");

		musicResources.add(R.raw.musicbattle4);
		musicTypes.add(SOUND_TYPE_MUSIC_COMBAT);
		combatSongNames.add("I Am Growlistrix");

		musicResources.add(R.raw.musicbattle5);
		musicTypes.add(SOUND_TYPE_MUSIC_COMBAT);
		combatSongNames.add("Wizzy Jam");

		musicResources.add(R.raw.musicbattle6);
		musicTypes.add(SOUND_TYPE_MUSIC_COMBAT);
		combatSongNames.add("Heart Attack Incoming");

		musicResources.add(R.raw.musicbattle7);
		musicTypes.add(SOUND_TYPE_MUSIC_COMBAT);
		combatSongNames.add("Barster's Goin' Rogue");

		musicResources.add(R.raw.musictreasureroom);
		musicTypes.add(SOUND_TYPE_MUSIC_TREASURE_ROOM);
	}

	public static void setPlaySounds(boolean b) {
		Log.d("sounds", "set play sounds " + b);
		playSounds = b;
	}

	public static void setPlayMusic(boolean b) {
		Log.d("sounds", "set play music " + b);
		playMusic = b;

		if (!playMusic)
			stopAllMusic();

	}

	public static void setPlayMusic(boolean b, int songType, Context context) {
		Log.d("sounds", "set play music " + b);
		playMusic = b;

		if (!playMusic)
			stopAllMusic();

		else {
			playMusic(songType, false, context);
		}

	}

	public static void playNextCombatSong(Context context) {
		if (!playMusic)
			return;

		playingMusicType = SOUND_TYPE_MUSIC_COMBAT;

		ArrayList<Integer> possibleSongs = new ArrayList<Integer>();
		for (int a = 0; a < musicTypes.size(); a++) {
			if (musicTypes.get(a) == playingMusicType)
				possibleSongs.add(a);
		}

		// int newsong = lastCombatSong;
		// while (newsong == lastCombatSong)
		// newsong = possibleSongs.get(Helper.randomInt(possibleSongs.size()));

		// lastCombatSong = newsong;

		lastCombatSongIndex++;
		if (lastCombatSongIndex >= possibleSongs.size())
			lastCombatSongIndex = 0;

		int newsong = possibleSongs.get(lastCombatSongIndex);

		playThisSong(newsong, context, false, true);

		Toast.makeText(context, combatSongNames.get(lastCombatSongIndex), Toast.LENGTH_SHORT).show();
	}

	private static void playThisSong(int clipId, final Context context, boolean repeat,
			final boolean playNextWhenFinished) {
		if (playingMusic != null) {
			try {
				if (playingMusic.isPlaying()) {
					playingMusic.stop();
				}
				playingMusic.reset();
			} catch (Exception e) {

			}

		}

		try {
			playingMusic = MediaPlayer.create(context, musicResources.get(clipId));
			playingMusic.setLooping(repeat);
			if (!repeat) {
				playingMusic.setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						try {
							playingMusic.release();
							if (playNextWhenFinished) {
								playNextCombatSong(context);
							}
						} catch (IllegalStateException e) {
							e.printStackTrace();
						}
					}
				});
			}

			// apply current music volume
			playingMusic.setVolume(musicVolume, musicVolume);
			playingMusic.start();
		} catch (Exception e) {
			// damn song won't play
		}
	}

	public static void playMusic(int soundType, boolean repeat, Context context) {
		if (!playMusic)
			return;

		playingMusicType = soundType;
		boolean playNextWhenFinished = false;

		ArrayList<Integer> possibleSongs = new ArrayList<Integer>();
		for (int a = 0; a < musicTypes.size(); a++) {
			if (musicTypes.get(a) == soundType)
				possibleSongs.add(a);
		}

		// if no songs exist for the requested type, gracefully fall back
		if (possibleSongs.size() == 0) {
			// prefer combat music when boss music is unavailable
			if (soundType == SOUND_TYPE_MUSIC_BOSS) {
				for (int a = 0; a < musicTypes.size(); a++) {
					if (musicTypes.get(a) == SOUND_TYPE_MUSIC_COMBAT)
						possibleSongs.add(a);
				}
				if (possibleSongs.size() > 0) {
					soundType = SOUND_TYPE_MUSIC_COMBAT;
				}
			}
		}

		// still empty? fall back to main menu music
		if (possibleSongs.size() == 0) {
			for (int a = 0; a < musicTypes.size(); a++) {
				if (musicTypes.get(a) == SOUND_TYPE_MUSIC_MAIN_MENU)
					possibleSongs.add(a);
			}
			if (possibleSongs.size() > 0) {
				soundType = SOUND_TYPE_MUSIC_MAIN_MENU;
			}
		}

		// no valid music to play
		if (possibleSongs.size() == 0)
			return;

		// pick a random song of this type
		int clipId = possibleSongs.get(Helper.randomInt(possibleSongs.size()));

		if (soundType == SoundManager.SOUND_TYPE_MUSIC_COMBAT)
			playNextWhenFinished = true;

		// update the type actually being played after fallbacks
		playingMusicType = soundType;

		playThisSong(clipId, context, repeat, playNextWhenFinished);
	}

	public static boolean showingImpact() {
		return showingImpact;
	}

	public static void setShowImpact(boolean b) {
		showingImpact = b;
	}

	public static boolean playingMusic() {
		return playMusic;
	}

	public static boolean playingSounds() {
		return playSounds;
	}

	public static void stopAllMusic() {
		Log.d("sounds", "stop all music");
		if (playingMusic != null) {
			try {
				playingMusic.stop();
				playingMusic.reset();
			} catch (Exception e) {
				// who cares, it's stopped isn't it??
			}
		}
	}

	public static void setMusicVolume(float volume) {
		if (volume < 0f)
			volume = 0f;
		if (volume > 1f)
			volume = 1f;
		musicVolume = volume;
		try {
			if (playingMusic != null)
				playingMusic.setVolume(musicVolume, musicVolume);
		} catch (Exception e) {
			// ignore
		}
	}

	public static float getMusicVolume() {
		return musicVolume;
	}

	public static void setSoundVolume(float volume) {
		if (volume < 0f)
			volume = 0f;
		if (volume > 1f)
			volume = 1f;
		soundVolume = volume;
	}

	public static float getSoundVolume() {
		return soundVolume;
	}

	public static void stopAll() {
		Log.d("sounds", "stop all");

		try {
			stopAllMusic();

			if (mSoundPool != null)
				mSoundPool.release();

			if (mAudioManager != null)
				mAudioManager.unloadSoundEffects();
		} catch (Exception e) {
			// we tried
		}
	}

	public static void makeSoundMap() {
		soundMap.clear();

		// weapons
		SoundType s = new SoundType(SOUND_TYPE_SWING);
		s.addSoundResource(R.raw.weaponimpact1);
		s.addSoundResource(R.raw.weaponimpact2);
		s.addSoundResource(R.raw.weaponimpact3);
		soundMap.put(SOUND_TYPE_SWING, s);

		s = new SoundType(SOUND_TYPE_PARRY);
		s.addSoundResource(R.raw.parry1);
		s.addSoundResource(R.raw.parry2);
		s.addSoundResource(R.raw.parry3);
		soundMap.put(SOUND_TYPE_PARRY, s);

		s = new SoundType(SOUND_TYPE_STAB);
		s.addSoundResource(R.raw.stab1);
		s.addSoundResource(R.raw.stab4);
		s.addSoundResource(R.raw.stab5);
		s.addSoundResource(R.raw.stab6);
		s.addSoundResource(R.raw.stab7);
		s.addSoundResource(R.raw.stab8);
		soundMap.put(SOUND_TYPE_STAB, s);

		s = new SoundType(SOUND_TYPE_BLOCK);
		s.addSoundResource(R.raw.block1);
		soundMap.put(SOUND_TYPE_BLOCK, s);

		s = new SoundType(SOUND_TYPE_DEAL);
		s.addSoundResource(R.raw.cardflip);
		s.addSoundResource(R.raw.cardflip2);
		s.addSoundResource(R.raw.dicethrow);
		s.addSoundResource(R.raw.dicethrow2);
		s.addSoundResource(R.raw.dicethrow3);
		s.addSoundResource(R.raw.dicethrow4);
		s.addSoundResource(R.raw.pass);
		soundMap.put(SOUND_TYPE_DEAL, s);

		s = new SoundType(SOUND_TYPE_SLASH);
		s.addSoundResource(R.raw.slash1);
		s.addSoundResource(R.raw.slash2);
		s.addSoundResource(R.raw.stab4);
		soundMap.put(SOUND_TYPE_SLASH, s);

		s = new SoundType(SOUND_TYPE_BASH);
		s.addSoundResource(R.raw.bash1);
		s.addSoundResource(R.raw.bash2);
		s.addSoundResource(R.raw.bash3);
		s.addSoundResource(R.raw.bash4);
		s.addSoundResource(R.raw.bash5);
		soundMap.put(SOUND_TYPE_BASH, s);

		s = new SoundType(SOUND_TYPE_SHOOT);
		s.addSoundResource(R.raw.shoot1);
		s.addSoundResource(R.raw.shoot2);
		s.addSoundResource(R.raw.shoot3);
		soundMap.put(SOUND_TYPE_SHOOT, s);

		s = new SoundType(SOUND_TYPE_STUN);
		s.addSoundResource(R.raw.weaponimpact1);
		soundMap.put(SOUND_TYPE_STUN, s);

		s = new SoundType(SOUND_TYPE_MONSTER_TAUNT_BEAST);
		s.addSoundResource(R.raw.tauntbeast0);
		s.addSoundResource(R.raw.tauntbeast1);
		s.addSoundResource(R.raw.tauntbeast2);
		s.addSoundResource(R.raw.tauntbeast3);
		s.addSoundResource(R.raw.tauntbeast4);
		soundMap.put(SOUND_TYPE_MONSTER_TAUNT_BEAST, s);

		s = new SoundType(SOUND_TYPE_MONSTER_TAUNT_HUMAN_MALE);
		s.addSoundResource(R.raw.tauntingmale1);
		s.addSoundResource(R.raw.tauntingmale2);
		s.addSoundResource(R.raw.tauntingmale3);
		soundMap.put(SOUND_TYPE_MONSTER_TAUNT_HUMAN_MALE, s);

		s = new SoundType(SOUND_TYPE_MONSTER_TAUNT_HUMAN_FEMALE);
		s.addSoundResource(R.raw.tauntfemale1);
		s.addSoundResource(R.raw.tauntfemale2);
		s.addSoundResource(R.raw.tauntfemale3);
		s.addSoundResource(R.raw.tauntfemale4);
		soundMap.put(SOUND_TYPE_MONSTER_TAUNT_HUMAN_FEMALE, s);

		s = new SoundType(SOUND_TYPE_MONSTER_TAUNT_GHOST);
		s.addSoundResource(R.raw.taughtghost1);
		s.addSoundResource(R.raw.taughtghost2);
		s.addSoundResource(R.raw.taughtghost3);
		soundMap.put(SOUND_TYPE_MONSTER_TAUNT_GHOST, s);

		s = new SoundType(SOUND_TYPE_MONSTER_TAUNT_UNDEAD_HAPPY);
		s.addSoundResource(R.raw.tauntundeadhappy1);
		soundMap.put(SOUND_TYPE_MONSTER_TAUNT_UNDEAD_HAPPY, s);

		s = new SoundType(SOUND_TYPE_MONSTER_TAUNT_UNDEAD_SAD);
		s.addSoundResource(R.raw.tauntundeadsad1);
		soundMap.put(SOUND_TYPE_MONSTER_TAUNT_UNDEAD_SAD, s);

		s = new SoundType(SOUND_TYPE_MONSTER_TAUNT_HUMAN_MALE_GRUMPY);
		s.addSoundResource(R.raw.tauntgrumpy1);
		s.addSoundResource(R.raw.tauntgrumpy2);
		s.addSoundResource(R.raw.tauntgrumpy3);
		soundMap.put(SOUND_TYPE_MONSTER_TAUNT_HUMAN_MALE_GRUMPY, s);

		s = new SoundType(SOUND_TYPE_MONSTER_TAUNT_HYPER);
		s.addSoundResource(R.raw.taunthyper1);
		s.addSoundResource(R.raw.taunthyper2);
		s.addSoundResource(R.raw.taunthyper3);
		soundMap.put(SOUND_TYPE_MONSTER_TAUNT_HYPER, s);

		s = new SoundType(SOUND_TYPE_MONSTER_TAUNT_SLIMY);
		s.addSoundResource(R.raw.tauntslimy1);
		s.addSoundResource(R.raw.tauntslimy2);
		s.addSoundResource(R.raw.tauntslimy3);
		soundMap.put(SOUND_TYPE_MONSTER_TAUNT_SLIMY, s);

		// abilities
		s = new SoundType(CHANTOFTHEWILDERNESS);
		s.addSoundResource(R.raw.chantofthewilderness);
		soundMap.put(CHANTOFTHEWILDERNESS, s);
		s = new SoundType(FISTSOFTRUTH);
		s.addSoundResource(R.raw.fistsoftruth);
		soundMap.put(FISTSOFTRUTH, s);
		s = new SoundType(UNDERWORLDASSAULT);
		s.addSoundResource(R.raw.underworldassault);
		soundMap.put(UNDERWORLDASSAULT, s);
		s = new SoundType(MINDBULLETS);
		s.addSoundResource(R.raw.mindbullets);
		soundMap.put(MINDBULLETS, s);
		s = new SoundType(PRECISIONBLOW);
		s.addSoundResource(R.raw.precisionblow);
		soundMap.put(PRECISIONBLOW, s);
		s = new SoundType(RAPIDFIRE);
		s.addSoundResource(R.raw.rapidfire);
		soundMap.put(RAPIDFIRE, s);
		s = new SoundType(HIDDENBLADE);
		s.addSoundResource(R.raw.hiddenblade);
		soundMap.put(HIDDENBLADE, s);
		s = new SoundType(OVERPOWEREDGRIPE);
		s.addSoundResource(R.raw.overpoweredgripe);
		soundMap.put(OVERPOWEREDGRIPE, s);
		s = new SoundType(MANUREBLAST);
		s.addSoundResource(R.raw.manureblast);
		soundMap.put(MANUREBLAST, s);
		s = new SoundType(EMOTIONALOUTBURST);
		s.addSoundResource(R.raw.emotionaloutburst);
		soundMap.put(EMOTIONALOUTBURST, s);
		s = new SoundType(DEATHLYELIXIR);
		s.addSoundResource(R.raw.deathlyelixir);
		soundMap.put(DEATHLYELIXIR, s);
		s = new SoundType(PROTECTORSAURA);
		s.addSoundResource(R.raw.protectorsaura);
		soundMap.put(PROTECTORSAURA, s);
		s = new SoundType(FISHHOOK);
		s.addSoundResource(R.raw.fishhook);
		soundMap.put(FISHHOOK, s);
		s = new SoundType(GOPHERHURL);
		s.addSoundResource(R.raw.gopherhurl);
		soundMap.put(GOPHERHURL, s);
		s = new SoundType(KNEECAPBASH);
		s.addSoundResource(R.raw.kneecapbash);
		soundMap.put(KNEECAPBASH, s);
		s = new SoundType(SHIELDWAVE);
		s.addSoundResource(R.raw.shieldwave);
		soundMap.put(SHIELDWAVE, s);
		s = new SoundType(SCARECROWJAB);
		s.addSoundResource(R.raw.scarecrowjab);
		soundMap.put(SCARECROWJAB, s);
		s = new SoundType(PITCHFORKBLITZ);
		s.addSoundResource(R.raw.pitchforkblitz);
		soundMap.put(PITCHFORKBLITZ, s);
		s = new SoundType(MINDSAP);
		s.addSoundResource(R.raw.mindsap);
		soundMap.put(MINDSAP, s);
		s = new SoundType(EMOTIONALWELLNESS);
		s.addSoundResource(R.raw.emotionalwelness);
		soundMap.put(EMOTIONALWELLNESS, s);
		s = new SoundType(THUNDEROUSHOWL);
		s.addSoundResource(R.raw.thunderoushowl);
		soundMap.put(THUNDEROUSHOWL, s);
		s = new SoundType(HOLYBURST);
		s.addSoundResource(R.raw.holyburst);
		soundMap.put(HOLYBURST, s);
		s = new SoundType(CURSEOFTHEFOREFATHERS);
		s.addSoundResource(R.raw.curseoftheforefathers);
		soundMap.put(CURSEOFTHEFOREFATHERS, s);
		s = new SoundType(INSIGHT);
		s.addSoundResource(R.raw.insight);
		soundMap.put(INSIGHT, s);
		s = new SoundType(HOLYFAST);
		s.addSoundResource(R.raw.holyfast);
		soundMap.put(HOLYFAST, s);
		s = new SoundType(HIGHERMEDITATION);
		s.addSoundResource(R.raw.highermeditation);
		soundMap.put(HIGHERMEDITATION, s);
		s = new SoundType(TALEOFTERROR);
		s.addSoundResource(R.raw.taleofterror);
		soundMap.put(TALEOFTERROR, s);
		s = new SoundType(HISSOFSONICPAIN);
		s.addSoundResource(R.raw.hissofsonicpain);
		soundMap.put(HISSOFSONICPAIN, s);
		s = new SoundType(SUMMONRATS);
		s.addSoundResource(R.raw.summonrats);
		soundMap.put(SUMMONRATS, s);
		s = new SoundType(LOGICPARADOX);
		s.addSoundResource(R.raw.logicparadox);
		soundMap.put(LOGICPARADOX, s);
		s = new SoundType(INNOCULATINGTUNE);
		s.addSoundResource(R.raw.innoculatingtune);
		soundMap.put(INNOCULATINGTUNE, s);
		s = new SoundType(ARCANEDOMINANCE);
		s.addSoundResource(R.raw.arcanedominance);
		soundMap.put(ARCANEDOMINANCE, s);
		s = new SoundType(MYSTICALPUSH);
		s.addSoundResource(R.raw.mysticalpush);
		soundMap.put(MYSTICALPUSH, s);
		s = new SoundType(REVASSAULT);
		s.addSoundResource(R.raw.revassault);
		soundMap.put(REVASSAULT, s);
		s = new SoundType(SUNRAYBLAST);
		s.addSoundResource(R.raw.sunrayblast);
		soundMap.put(SUNRAYBLAST, s);
		s = new SoundType(SPARK);
		s.addSoundResource(R.raw.spark);
		soundMap.put(SPARK, s);
		s = new SoundType(SUMMONBEES);
		s.addSoundResource(R.raw.summonbees);
		soundMap.put(SUMMONBEES, s);
		s = new SoundType(GIFT);
		s.addSoundResource(R.raw.gift);
		soundMap.put(GIFT, s);
		s = new SoundType(ASTEROID);
		s.addSoundResource(R.raw.asteroid);
		soundMap.put(ASTEROID, s);
		s = new SoundType(RAISETHEICE);
		s.addSoundResource(R.raw.raisetheice);
		soundMap.put(RAISETHEICE, s);
		s = new SoundType(RECALLBEAST);
		s.addSoundResource(R.raw.recallbeast);
		soundMap.put(RECALLBEAST, s);
		s = new SoundType(REMOTECHOKE);
		s.addSoundResource(R.raw.remotechoke);
		soundMap.put(REMOTECHOKE, s);
		s = new SoundType(LOVELIFE);
		s.addSoundResource(R.raw.lovelife);
		soundMap.put(LOVELIFE, s);
		s = new SoundType(AWARENESS);
		s.addSoundResource(R.raw.awareness);
		soundMap.put(AWARENESS, s);
		s = new SoundType(WILY);
		s.addSoundResource(R.raw.wily);
		soundMap.put(WILY, s);
		s = new SoundType(FLEX);
		s.addSoundResource(R.raw.flex);
		soundMap.put(FLEX, s);
		s = new SoundType(REFLEXES);
		s.addSoundResource(R.raw.reflexes);
		soundMap.put(REFLEXES, s);
		s = new SoundType(CHANTOFTHESPECTRE);
		s.addSoundResource(R.raw.chantofthespectre);
		soundMap.put(CHANTOFTHESPECTRE, s);

		s = new SoundType(DRAGONBREATH);
		s.addSoundResource(R.raw.dragonbreath);
		soundMap.put(DRAGONBREATH, s);

		s = new SoundType(FOOLISHGAMBLE);
		s.addSoundResource(R.raw.foolishgamble);
		soundMap.put(FOOLISHGAMBLE, s);

		s = new SoundType(SMACK);
		s.addSoundResource(R.raw.smack);
		soundMap.put(SMACK, s);

		s = new SoundType(WRAPATTACK);
		s.addSoundResource(R.raw.wrapattack);
		soundMap.put(WRAPATTACK, s);

		s = new SoundType(POWERWORDBOOKS);
		s.addSoundResource(R.raw.powerwordbooks);
		soundMap.put(POWERWORDBOOKS, s);

		// items
		s = new SoundType(MAGIC_MIRROR);
		s.addSoundResource(R.raw.magicmirror);
		soundMap.put(MAGIC_MIRROR, s);

		s = new SoundType(CLOUD);
		s.addSoundResource(R.raw.cloud);
		soundMap.put(CLOUD, s);

		s = new SoundType(FLUTE);
		s.addSoundResource(R.raw.flute);
		soundMap.put(FLUTE, s);

		s = new SoundType(TIMESHIFTDEVICE);
		s.addSoundResource(R.raw.timeshiftdevice);
		soundMap.put(TIMESHIFTDEVICE, s);
	}

	// last id = 104

	private static HashMap<Integer, SoundType> soundMap = new HashMap<Integer, SoundType>();

	// generic sounds - preload when game launches
	public static final int SOUND_TYPE_EXPLOSION_LARGE = 3;
	public static final int SOUND_TYPE_EXPLOSION_SMALL = 4;
	public static final int SOUND_TYPE_MISS = 13;
	public static final int SOUND_TYPE_DODGE = 26;
	public static final int TAKE_HIT = 28;
	public static final int FIGHT_WON = 29;
	public static final int PLAYERDEAD = 36;
	public static final int ALERTSOUND = 102;
	public static final int ITEMSOUND = 103;
	public static final int RECHARGESOUND = 104;

	// monster death sounds
	public static final int SOUND_TYPE_DEATH_1 = 14;
	public static final int SOUND_TYPE_DEATH_2 = 93;
	public static final int SOUND_TYPE_DEATH_3 = 94;
	public static final int SOUND_TYPE_DEATH_4 = 95;
	public static final int SOUND_TYPE_DEATH_5 = 96;
	public static final int SOUND_TYPE_DEATH_6 = 97;
	public static final int SOUND_TYPE_DEATH_7 = 98;
	public static final int SOUND_TYPE_DEATH_8 = 99;
	public static final int SOUND_TYPE_DEATH_9 = 100;
	public static final int SOUND_TYPE_DEATH_10 = 101;

	public static final int SOUND_TYPE_MONSTER_TAKE_DAMAGE = 15;
	public static final int OPEN_CHEST_ITEM = 64;
	public static final int OPEN_CHEST_EMPTY = 83;
	public static final int EQUIP_ITEM = 84;
	public static final int CLICK = 85;
	public static final int BUYSOUND = 91;
	public static final int SELLSOUND = 92;

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
	public static final int HIDDENBLADE = 37;
	public static final int OVERPOWEREDGRIPE = 38;
	public static final int MANUREBLAST = 39;
	public static final int EMOTIONALOUTBURST = 40;
	public static final int DEATHLYELIXIR = 41;
	public static final int PROTECTORSAURA = 42;
	public static final int FISHHOOK = 43;
	public static final int GOPHERHURL = 44;
	public static final int KNEECAPBASH = 45;
	public static final int SHIELDWAVE = 46;
	public static final int SCARECROWJAB = 47;
	public static final int PITCHFORKBLITZ = 48;
	public static final int MINDSAP = 49;
	public static final int EMOTIONALWELLNESS = 50;
	public static final int THUNDEROUSHOWL = 51;
	public static final int HOLYBURST = 52;
	public static final int CURSEOFTHEFOREFATHERS = 53;
	public static final int INSIGHT = 54;
	public static final int HOLYFAST = 55;
	public static final int MYSTICALPUSH = 56;
	public static final int HIGHERMEDITATION = 57;
	public static final int TALEOFTERROR = 58;
	public static final int HISSOFSONICPAIN = 59;
	public static final int SUMMONRATS = 60;
	public static final int LOGICPARADOX = 61;
	public static final int INNOCULATINGTUNE = 62;
	public static final int ARCANEDOMINANCE = 63;
	public static final int REVASSAULT = 68;
	public static final int SUNRAYBLAST = 69;
	public static final int SPARK = 70;
	public static final int SUMMONBEES = 71;
	public static final int GIFT = 72;
	public static final int ASTEROID = 73;
	public static final int RAISETHEICE = 74;
	public static final int RECALLBEAST = 75;
	public static final int REMOTECHOKE = 76;
	public static final int LOVELIFE = 77;
	public static final int AWARENESS = 78;
	public static final int WILY = 79;
	public static final int FLEX = 80;
	public static final int REFLEXES = 81;
	public static final int CHANTOFTHESPECTRE = 82;
	public static final int DRAGONBREATH = 86;
	public static final int FOOLISHGAMBLE = 87;
	public static final int SMACK = 88;
	public static final int WRAPATTACK = 89;
	public static final int POWERWORDBOOKS = 90;

	// items
	public static final int MAGIC_MIRROR = 27;
	public static final int CLOUD = 65;
	public static final int FLUTE = 66;
	public static final int TIMESHIFTDEVICE = 67;

}
