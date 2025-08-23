package com.alderangaming.wizardsencounters;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class Animator {
	public static final int MOVE_UP = 0;
	public static final int SMALL_SHAKE = 1;
	public static final int DODGE = 2;
	public static final int FLASH = 3;
	public static final int SCALE_AWAY = 4;
	public static final int SCALE_TOWARDS = 5;
	public static final int LEFT_TO_RIGHT = 6;
	public static final int SHRINK = 7;
	public static final int RAISE_AND_ROTATE = 8;
	public static final int MONSTER_ATTACK_TEXT = 9;
	public static final int PLAYER_ATTACK_TEXT = 10;
	public static final int FADE_OUT_SHAKING = 11;
	public static final int FADE_IN = 12;
	public static final int REDSCREEN = 13;
	public static final int ATTACK_SHAKE = 14;
	public static final int SWING = 15;
	public static final int PARRY = 16;
	public static final int BLOCK = 17;
	public static final int DEAL = 18;
	public static final int STAB = 19;
	public static final int RIGHT_TO_LEFT = 20;
	public static final int POW = 21;
	public static final int STRETCH_UP = 22;
	public static final int SCALE_TOWARDS_ROTATE = 23;
	public static final int CRASH_DOWN = 24;
	public static final int PARRY_LONG = 25;
	public static final int SCALE_AWAY_AND_SPIN = 26;
	public static final int RAISE_AND_REVOLVE = 27;
	public static final int MOVE_EQUIP = 28;
	public static final int SCALE_AWAY_SLOW = 29;
	public static final int SHOOT_PROJECTILE = 30;
	public static final int MONSTER_FLOATS = 31;
	public static final int MONSTER_FLIP_AND_MOVE_LEFT = 32;
	public static final int MONSTER_MOVE_LEFT = 33;
	public static final int MONSTER_FLIP_AND_MOVE_RIGHT = 34;
	public static final int MONSTER_MOVE_RIGHT = 35;
	public static final int MONSTER_LUNGE = 36;
	public static final int MONSTER_JUMPS = 37;
	public static final int MONSTER_ROCKS = 38;
	public static final int SPIN = 39;

	public static Animation getAnimation(int type) {

		Animation animation = null;

		if (type == MONSTER_FLOATS) {
			animation = new AnimationSet(true);

			TranslateAnimation t1;
			TranslateAnimation t2;

			// move up
			t1 = new TranslateAnimation(0, 0, 0, -15);
			t1.setDuration(300);
			t1.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(t1);

			// move down
			t2 = new TranslateAnimation(0, 0, -15, 0);
			t2.setDuration(300);
			t2.setStartOffset(300);
			t2.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(t2);

		}

		else if (type == MONSTER_FLIP_AND_MOVE_LEFT) {
			animation = new AnimationSet(true);

			TranslateAnimation t1;
			TranslateAnimation t2;

			// flip
			t1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1.0f,
					Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
			t1.setDuration(30);
			t1.setInterpolator(new AccelerateInterpolator());
			t1.setFillAfter(true);
			((AnimationSet) animation).addAnimation(t1);

			// move to left
			t2 = new TranslateAnimation(0, -50, 0, 0);
			t2.setDuration(700);
			t2.setInterpolator(new AccelerateInterpolator());
			t2.setStartOffset(100);
			t2.setFillAfter(true);
			((AnimationSet) animation).addAnimation(t2);
		}

		else if (type == MONSTER_FLIP_AND_MOVE_RIGHT) {
			animation = new AnimationSet(true);

			TranslateAnimation t1;
			TranslateAnimation t2;

			// flip
			t1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1.0f,
					Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
			t1.setDuration(30);
			t1.setInterpolator(new AccelerateInterpolator());
			t1.setFillAfter(true);
			((AnimationSet) animation).addAnimation(t1);

			// move to right
			t2 = new TranslateAnimation(0, 50, 0, 0);
			t2.setDuration(700);
			t2.setInterpolator(new AccelerateInterpolator());
			t2.setStartOffset(100);
			t2.setFillAfter(true);
			((AnimationSet) animation).addAnimation(t2);
		}

		else if (type == MONSTER_MOVE_LEFT) {
			animation = new AnimationSet(true);

			TranslateAnimation t2;

			// move to left
			t2 = new TranslateAnimation(0, -50, 0, 0);
			t2.setDuration(700);
			t2.setInterpolator(new AccelerateInterpolator());
			t2.setFillAfter(true);
			((AnimationSet) animation).addAnimation(t2);
		}

		else if (type == MONSTER_MOVE_RIGHT) {
			animation = new AnimationSet(true);

			TranslateAnimation t2;

			// move to right
			t2 = new TranslateAnimation(0, 50, 0, 0);
			t2.setDuration(700);
			t2.setInterpolator(new AccelerateInterpolator());
			t2.setFillAfter(true);
			((AnimationSet) animation).addAnimation(t2);
		}

		else if (type == MONSTER_JUMPS) {
			animation = new AnimationSet(true);

			int randNumJumps = 1 + Helper.randomInt(10);

			TranslateAnimation t1;
			TranslateAnimation t2;

			int randHeight = 15 + Helper.randomInt(15);

			// up
			t1 = new TranslateAnimation(0, 0, 0, -randHeight);
			t1.setInterpolator(new AccelerateInterpolator());
			t1.setDuration(100);
			((AnimationSet) animation).addAnimation(t1);

			// down
			t2 = new TranslateAnimation(0, 0, -randHeight, 0);
			t2.setInterpolator(new AccelerateInterpolator());
			t2.setStartOffset(100);
			t2.setDuration(100);
			((AnimationSet) animation).addAnimation(t2);

			animation.setRepeatCount(randNumJumps);

		}

		else if (type == MONSTER_ROCKS) {
			animation = new AnimationSet(true);

			int randNumRocks = 8 + Helper.randomInt(10);

			RotateAnimation r1;
			RotateAnimation r2;

			r1 = new RotateAnimation(-30.0f, 15.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.8f);
			r1.setDuration(150);
			r1.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(r1);

			r2 = new RotateAnimation(15.0f, -30.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.8f);
			r2.setDuration(150);
			r2.setStartOffset(150);
			r2.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(r2);

			animation.setRepeatCount(randNumRocks);

		}

		else if (type == MONSTER_LUNGE) {
			animation = new AnimationSet(true);

			float randLungeSize = (float) Helper.randomInt(25) / 10.0f;

			if (randLungeSize < 1.0f)
				randLungeSize = 1.0f;

			ScaleAnimation s1 = new ScaleAnimation(1.0f, randLungeSize, 1.0f, randLungeSize);
			s1.setDuration(300);
			s1.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(s1);

			ScaleAnimation s2 = new ScaleAnimation(randLungeSize, 1.0f, randLungeSize, 1.0f);
			s2.setDuration(200);
			s2.setStartOffset(300);
			s2.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(s2);
		}

		// swing
		else if (type == SWING) {
			animation = new AnimationSet(true);

			// move up
			TranslateAnimation t1 = new TranslateAnimation(0, 0, 40, 0);
			t1.setDuration(250);
			t1.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(t1);

			// rotate
			RotateAnimation r = new RotateAnimation(-50, 60, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 1.0f);
			r.setDuration(400);
			r.setStartOffset(250);
			r.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(r);

			// move down
			TranslateAnimation t2 = new TranslateAnimation(0, 0, 0, 40);
			t2.setDuration(250);
			t2.setStartOffset(650);
			t2.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(t2);
		}
		// parry
		else if (type == PARRY) {
			animation = new AnimationSet(true);

			// move up
			TranslateAnimation t1 = new TranslateAnimation(0, 0, 40, 0);
			t1.setDuration(250);
			t1.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(t1);

			// rotate
			RotateAnimation r;
			int start = -10;
			int end = 10;
			for (int a = 0; a < 4; a++) {
				r = new RotateAnimation(start, end, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
				r.setDuration(150);
				r.setStartOffset(300 + (a * 150));
				r.setInterpolator(new AccelerateInterpolator());
				((AnimationSet) animation).addAnimation(r);

				int save = start;
				start = end;
				end = save;
			}

			// move down
			TranslateAnimation t2 = new TranslateAnimation(0, 0, 0, 40);
			t2.setDuration(250);
			t2.setStartOffset(650);
			t2.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(t2);
		}
		// parry long
		else if (type == PARRY_LONG) {
			animation = new AnimationSet(true);

			// move up
			TranslateAnimation t1 = new TranslateAnimation(0, 0, 40, 0);
			t1.setDuration(250);
			t1.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(t1);

			// rotate
			RotateAnimation r;
			int start = -10;
			int end = 10;
			for (int a = 0; a < 5; a++) {
				r = new RotateAnimation(start, end, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
				r.setDuration(300);
				r.setStartOffset(300 + (a * 300));
				r.setInterpolator(new AccelerateInterpolator());
				((AnimationSet) animation).addAnimation(r);

				int save = start;
				start = end;
				end = save;
			}

			// move down
			TranslateAnimation t2 = new TranslateAnimation(0, 0, 0, 40);
			t2.setDuration(250);
			t2.setStartOffset(1500);
			t2.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(t2);
		}
		// block
		else if (type == BLOCK) {
			animation = new AnimationSet(true);
			animation.setFillEnabled(true);

			// rotate
			RotateAnimation r = new RotateAnimation(0, -70, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.9f);
			r.setDuration(300);
			r.setFillAfter(true);
			((AnimationSet) animation).addAnimation(r);

			// move up
			TranslateAnimation t1 = new TranslateAnimation(0, 90, 40, -50);
			t1.setDuration(200);
			t1.setInterpolator(new AccelerateInterpolator());
			t1.setFillAfter(true);
			((AnimationSet) animation).addAnimation(t1);

			// vanish
			AlphaAnimation t2 = new AlphaAnimation(1.0f, 0.0f);
			t2.setDuration(250);
			t2.setStartOffset(650);
			((AnimationSet) animation).addAnimation(t2);
		}
		// deal
		else if (type == DEAL) {
			animation = new AnimationSet(true);
			animation.setFillEnabled(true);

			// rotate
			RotateAnimation r;
			int start = 0;
			int end = 360;
			for (int a = 0; a < 3; a++) {
				r = new RotateAnimation(start, end, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
				r.setDuration(333);
				r.setStartOffset(a * 333);
				r.setInterpolator(new AccelerateInterpolator());
				((AnimationSet) animation).addAnimation(r);

			}

			// scale towards
			ScaleAnimation sa = new ScaleAnimation(1.0f, 0.2f, 1.0f, 0.2f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			sa.setDuration(1000);
			sa.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(sa);
		}
		// stab
		else if (type == STAB) {
			animation = new AnimationSet(true);
			// move up
			TranslateAnimation t1 = new TranslateAnimation(0, 0, 40, -50);
			t1.setDuration(600);
			t1.setInterpolator(new AccelerateInterpolator());
			t1.setFillAfter(true);
			((AnimationSet) animation).addAnimation(t1);

			// vanish
			AlphaAnimation t2 = new AlphaAnimation(1.0f, 0.0f);
			t2.setDuration(250);
			t2.setStartOffset(600);
			((AnimationSet) animation).addAnimation(t2);
		}

		// slowly move up
		else if (type == MOVE_UP) {
			animation = new TranslateAnimation(0, 0, 0, -100);
			animation.setDuration(2500);
		}

		// small random shake
		else if (type == SMALL_SHAKE) {
			animation = new AnimationSet(true);
			TranslateAnimation t;
			AlphaAnimation aa;
			aa = new AlphaAnimation(0.3f, 1.0f);
			aa.setStartOffset(100);
			aa.setDuration(400);
			aa.setFillEnabled(true);
			aa.setFillAfter(false);

			((AnimationSet) animation).addAnimation(aa);

			int lastx = 0;
			int lasty = 0;
			for (int a = 0; a < 5; a++) {

				int randx = Helper.randomInt(10);
				int randy = Helper.randomInt(10);
				if (Helper.randomInt(100) < 50)
					randx *= -1;

				if (Helper.randomInt(100) < 50)
					randy *= -1;

				t = new TranslateAnimation(lasty, randy, lastx, randx);
				t.setDuration(50);
				t.setStartOffset(a * 50);
				((AnimationSet) animation).addAnimation(t);

				lastx = randx;
				lasty = randy;
			}
		}

		// random move out of the way
		else if (type == DODGE) {
			animation = new AnimationSet(true);
			TranslateAnimation t1;
			TranslateAnimation t2;
			int random1 = 50 + Helper.randomInt(100);
			int random2 = 50 + Helper.randomInt(100);
			int randomDur = 200 + Helper.randomInt(200);
			random1 = random1 * Helper.randomPosNeg();
			random2 *= -1;
			t1 = new TranslateAnimation(0, random1, 0, random2);
			t1.setDuration(randomDur);
			t1.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(t1);

			t2 = new TranslateAnimation(0, -random1, 0, -random2);
			t2.setDuration(300);
			t2.setInterpolator(new AccelerateInterpolator());
			t2.setStartOffset(randomDur);
			((AnimationSet) animation).addAnimation(t2);

		}

		// flash
		else if (type == FLASH) {
			animation = new AlphaAnimation(0.3f, 1.0f);
			animation.setStartOffset(300);
			animation.setDuration(300);
			animation.setFillEnabled(true);
			animation.setFillAfter(false);
		}

		// scale away
		else if (type == SCALE_AWAY) {
			animation = new AnimationSet(true);
			ScaleAnimation sa;
			AlphaAnimation aa;
			TranslateAnimation ta;

			// scale
			sa = new ScaleAnimation(4.0f, 0.5f, 4.0f, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			sa.setDuration(1500);
			sa.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(sa);

			ta = new TranslateAnimation(0, 0, 0, -70);
			ta.setDuration(1500);
			ta.setInterpolator(new AccelerateInterpolator());
			ta.setFillAfter(true);
			((AnimationSet) animation).addAnimation(ta);

			// disappear
			aa = new AlphaAnimation(1.0f, 0.0f);
			aa.setStartOffset(1400);
			aa.setDuration(100);
			((AnimationSet) animation).addAnimation(aa);
		}

		else if (type == SCALE_AWAY_AND_SPIN) {

			animation = new AnimationSet(true);
			ScaleAnimation sa;
			AlphaAnimation aa;
			TranslateAnimation ta;
			RotateAnimation rAnim;

			float lastDegree = 0.0f;
			float increment = 4.0f;
			long moveDuration = 10;
			for (int a = 0; a < 150; a++) {
				rAnim = new RotateAnimation(lastDegree, (increment * (float) a), Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				rAnim.setDuration(moveDuration);
				rAnim.setStartOffset(moveDuration * a);
				lastDegree = (increment * (float) a);
				((AnimationSet) animation).addAnimation(rAnim);
			}

			// scale
			sa = new ScaleAnimation(4.0f, 0.5f, 4.0f, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			sa.setDuration(1500);
			sa.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(sa);

			ta = new TranslateAnimation(0, 0, 0, -70);
			ta.setDuration(1500);
			ta.setInterpolator(new AccelerateInterpolator());
			ta.setFillAfter(true);
			((AnimationSet) animation).addAnimation(ta);

			// disappear
			aa = new AlphaAnimation(1.0f, 0.0f);
			aa.setStartOffset(1400);
			aa.setDuration(100);
			((AnimationSet) animation).addAnimation(aa);

		}

		// scale towards
		else if (type == SCALE_TOWARDS) {
			animation = new AnimationSet(true);
			ScaleAnimation sa;
			AlphaAnimation aa;

			// scale
			sa = new ScaleAnimation(0.1f, 4.0f, 0.1f, 4.0f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 1.0f);
			sa.setDuration(1000);
			sa.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(sa);

			// disappear
			aa = new AlphaAnimation(1.0f, 0.0f);
			aa.setStartOffset(900);
			aa.setDuration(100);
			((AnimationSet) animation).addAnimation(aa);
		}

		// left to right
		else if (type == LEFT_TO_RIGHT) {
			animation = new AnimationSet(true);

			TranslateAnimation t0 = new TranslateAnimation(0, 0, -50, -50);
			t0.setFillBefore(true);
			t0.setFillEnabled(true);
			((AnimationSet) animation).addAnimation(t0);

			TranslateAnimation t1 = new TranslateAnimation(-150, 50, 0, 0);
			t1.setDuration(1200);
			((AnimationSet) animation).addAnimation(t1);

			// scale
			ScaleAnimation sa = new ScaleAnimation(1.5f, 2.5f, 1.5f, 2.5f, Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			sa.setDuration(1200);
			((AnimationSet) animation).addAnimation(sa);

		}

		// right to left
		else if (type == RIGHT_TO_LEFT) {
			animation = new AnimationSet(true);

			TranslateAnimation t0 = new TranslateAnimation(150, 150, -50, -50);
			t0.setFillBefore(true);
			t0.setFillEnabled(true);
			((AnimationSet) animation).addAnimation(t0);

			TranslateAnimation t1 = new TranslateAnimation(50, -150, 0, 0);
			t1.setDuration(1200);
			t1.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(t1);

			// scale
			ScaleAnimation sa = new ScaleAnimation(1.5f, 2.5f, 1.5f, 2.5f, Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			sa.setDuration(1200);

			((AnimationSet) animation).addAnimation(sa);

		}

		// shrink
		else if (type == SHRINK) {
			animation = new ScaleAnimation(1.0f, 0.2f, 1.0f, 0.2f, Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 1.0f);

			animation.setDuration(1250);

			animation.setFillEnabled(true);
			animation.setFillAfter(true);
		}

		// spin
		else if (type == SPIN) {
			animation = new AnimationSet(true);

			RotateAnimation rAnim;

			// spin
			float lastDegree = 0.0f;
			float increment = 4.0f;
			long moveDuration = 10;
			for (int a = 0; a < 150; a++) {
				rAnim = new RotateAnimation(lastDegree, (increment * (float) a), Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				rAnim.setDuration(moveDuration);
				rAnim.setStartOffset(moveDuration * a);
				lastDegree = (increment * (float) a);
				((AnimationSet) animation).addAnimation(rAnim);
			}
		}

		// raise and rotate
		else if (type == RAISE_AND_ROTATE) {
			animation = new AnimationSet(true);

			RotateAnimation rAnim;

			// spin
			float lastDegree = 0.0f;
			float increment = 4.0f;
			long moveDuration = 10;
			for (int a = 0; a < 150; a++) {
				rAnim = new RotateAnimation(lastDegree, (increment * (float) a), Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				rAnim.setDuration(moveDuration);
				rAnim.setStartOffset(moveDuration * a);
				lastDegree = (increment * (float) a);
				((AnimationSet) animation).addAnimation(rAnim);
			}

			// raise
			TranslateAnimation t0 = new TranslateAnimation(0, 0, 0, -150);
			t0.setDuration(300);
			t0.setInterpolator(new AccelerateInterpolator());
			t0.setFillEnabled(true);
			t0.setFillAfter(true);
			((AnimationSet) animation).addAnimation(t0);

		}

		// raise and revolve
		else if (type == RAISE_AND_REVOLVE) {
			animation = new AnimationSet(true);

			RotateAnimation rAnim;

			// raise
			TranslateAnimation t0 = new TranslateAnimation(0, 0, 0, -150);
			t0.setDuration(300);
			t0.setInterpolator(new AccelerateInterpolator());
			t0.setFillEnabled(true);
			t0.setFillAfter(true);
			((AnimationSet) animation).addAnimation(t0);

			// spin
			float lastDegree = 0.0f;
			float increment = 4.0f;
			long moveDuration = 10;
			for (int a = 0; a < 150; a++) {
				rAnim = new RotateAnimation(lastDegree, (increment * (float) a), Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				rAnim.setDuration(moveDuration);
				rAnim.setStartOffset(moveDuration * a);
				lastDegree = (increment * (float) a);
				((AnimationSet) animation).addAnimation(rAnim);
			}
		}

		// monster attack text
		else if (type == MONSTER_ATTACK_TEXT) {
			animation = new AnimationSet(true);
			ScaleAnimation sa;
			AlphaAnimation aa;
			TranslateAnimation ta;
			// start in random offset place
			ta = new TranslateAnimation(0, Helper.randomInt(20) * Helper.randomPosNeg(), 0, Helper.randomInt(20)
					* Helper.randomPosNeg());
			ta.setDuration(0);
			ta.setFillEnabled(true);
			ta.setFillBefore(true);
			((AnimationSet) animation).addAnimation(ta);

			// scale
			sa = new ScaleAnimation(0.1f, 2.0f, 0.1f, 2.0f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			sa.setDuration(500);
			sa.setInterpolator(new AccelerateInterpolator());
			sa.setFillEnabled(true);
			sa.setFillAfter(true);
			((AnimationSet) animation).addAnimation(sa);

			// disappear
			aa = new AlphaAnimation(1.0f, 0.0f);
			aa.setStartOffset(1100);
			aa.setDuration(100);
			sa.setFillEnabled(true);
			sa.setFillAfter(true);
			((AnimationSet) animation).addAnimation(aa);

		}

		// player attack text
		else if (type == PLAYER_ATTACK_TEXT) {
			// slowly raise up and disappear

			animation = new AnimationSet(true);

			// move up
			TranslateAnimation t1 = new TranslateAnimation(0, 0, 40, 0);
			t1.setDuration(250);
			t1.setInterpolator(new AccelerateInterpolator());
			t1.setFillEnabled(true);
			t1.setFillAfter(true);
			((AnimationSet) animation).addAnimation(t1);

			// disappear
			AlphaAnimation aa = new AlphaAnimation(1.0f, 0.0f);
			aa.setStartOffset(1100);
			aa.setDuration(100);
			aa.setFillEnabled(true);
			aa.setFillAfter(true);
			((AnimationSet) animation).addAnimation(aa);
		}

		// fade out shaking
		else if (type == FADE_OUT_SHAKING) {
			animation = new AnimationSet(true);
			TranslateAnimation t;
			AlphaAnimation aa;
			aa = new AlphaAnimation(1.0f, 0.0f);
			aa.setDuration(1000);
			aa.setFillEnabled(true);
			aa.setFillAfter(true);

			((AnimationSet) animation).addAnimation(aa);

			int lastx = 0;
			int lasty = 0;
			for (int a = 0; a < 5; a++) {

				int randx = Helper.randomInt(10);
				int randy = Helper.randomInt(10);
				if (Helper.randomInt(100) < 50)
					randx *= -1;

				if (Helper.randomInt(100) < 50)
					randy *= -1;

				t = new TranslateAnimation(lasty, randy, lastx, randx);
				t.setDuration(50);
				t.setStartOffset(a * 50);
				((AnimationSet) animation).addAnimation(t);

				lastx = randx;
				lasty = randy;
			}
		}

		// fade in
		else if (type == FADE_IN) {
			animation = new AlphaAnimation(0.0f, 1.0f);
			animation.setDuration(1000);
			animation.setFillEnabled(true);
			animation.setFillBefore(true);
		}

		// red screen
		else if (type == REDSCREEN) {
			animation = new AnimationSet(true);

			AlphaAnimation a1 = new AlphaAnimation(0.0f, 0.5f);
			a1.setDuration(220);
			((AnimationSet) animation).addAnimation(a1);

			AlphaAnimation a2 = new AlphaAnimation(0.5f, 0.0f);
			a2.setDuration(280);
			a2.setStartOffset(220);
			((AnimationSet) animation).addAnimation(a2);

			TranslateAnimation t1 = new TranslateAnimation(0, 6, 0, -6);
			t1.setDuration(60);
			t1.setStartOffset(120);
			((AnimationSet) animation).addAnimation(t1);
		}

		// attack shake
		else if (type == ATTACK_SHAKE) {
			animation = new AnimationSet(true);
			TranslateAnimation t;
			AlphaAnimation aa;
			aa = new AlphaAnimation(0.3f, 1.0f);
			aa.setStartOffset(100);
			aa.setDuration(400);
			aa.setFillEnabled(true);
			aa.setFillAfter(false);

			((AnimationSet) animation).addAnimation(aa);

			int lastx = 0;
			int lasty = 0;
			for (int a = 0; a < 4; a++) {

				int randx = Helper.randomInt(5);
				int randy = Helper.randomInt(5);
				if (Helper.randomInt(100) < 50)
					randx *= -1;

				if (Helper.randomInt(100) < 50)
					randy *= -1;

				t = new TranslateAnimation(lasty, randy, lastx, randx);
				t.setDuration(50);
				t.setStartOffset(a * 50);
				((AnimationSet) animation).addAnimation(t);

				lastx = randx;
				lasty = randy;
			}
		}

		// pow
		else if (type == POW) {
			animation = new AnimationSet(true);
			// scale towards
			ScaleAnimation s1 = new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			s1.setDuration(600);
			s1.setInterpolator(new AccelerateInterpolator());
			s1.setFillEnabled(true);
			s1.setFillAfter(true);
			((AnimationSet) animation).addAnimation(s1);

			// scale away
			ScaleAnimation s2 = new ScaleAnimation(1.3f, 1.0f, 1.3f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			s2.setDuration(600);
			s2.setInterpolator(new AccelerateInterpolator());
			s2.setStartOffset(600);
			((AnimationSet) animation).addAnimation(s2);
		}

		// shoot projectile
		else if (type == SHOOT_PROJECTILE) {
			animation = new AnimationSet(true);

			// scale away
			ScaleAnimation s2 = new ScaleAnimation(1.3f, 1.0f, 1.3f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			s2.setDuration(300);
			s2.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(s2);
		}

		// scale away slow
		else if (type == SCALE_AWAY_SLOW) {
			animation = new AnimationSet(true);
			// scale away
			ScaleAnimation s2 = new ScaleAnimation(1.3f, 1.0f, 1.3f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			s2.setDuration(2500);
			s2.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(s2);
		}

		// stretch up
		else if (type == STRETCH_UP) {
			animation = new ScaleAnimation(1.0f, 1.0f, 0.1f, 3.0f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			animation.setDuration(1200);
			animation.setInterpolator(new AccelerateInterpolator());
		}

		// scale towards rotate
		else if (type == SCALE_TOWARDS_ROTATE) {
			animation = new AnimationSet(true);

			RotateAnimation r1 = new RotateAnimation(0, 180);
			r1.setFillEnabled(true);
			r1.setFillBefore(true);
			((AnimationSet) animation).addAnimation(r1);

			ScaleAnimation s1 = new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			s1.setDuration(1500);
			s1.setInterpolator(new AccelerateInterpolator());
			s1.setFillEnabled(true);
			s1.setFillAfter(true);
			((AnimationSet) animation).addAnimation(s1);
		}

		// crash down
		else if (type == CRASH_DOWN) {
			animation = new AnimationSet(true);
			ScaleAnimation s1 = new ScaleAnimation(0.1f, 1.5f, 0.1f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			s1.setDuration(1500);
			s1.setInterpolator(new AccelerateInterpolator());
			s1.setFillEnabled(true);
			s1.setFillAfter(true);
			((AnimationSet) animation).addAnimation(s1);

			// move down
			TranslateAnimation t1 = new TranslateAnimation(0, 0, -100, 40);
			t1.setDuration(1500);
			t1.setInterpolator(new AccelerateInterpolator());
			t1.setFillEnabled(true);
			t1.setFillAfter(true);
			((AnimationSet) animation).addAnimation(t1);

		}
		// move equip
		else if (type == MOVE_EQUIP) {
			animation = new AnimationSet(true);

			// move left
			TranslateAnimation t1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
					-1.0f,
					Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f);

			t1.setDuration(500);
			t1.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(t1);
		}

		return animation;
	}

}
