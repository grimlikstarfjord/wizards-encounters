package com.alderangaming.wizardsencounters;

import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class Animator
{
	public static final int MOVE_UP = 0;
	public static final int SMALL_SHAKE = 1;
	public static final int DODGE = 2;
	public static final int FLASH = 3;
	public static final int SCALE_AWAY = 4;
	public static final int SCALE_TOWARDS = 5;
	public static final int LEFT_TO_RIGHT = 6;
	public static final int SHRINK = 7;
	public static final int RAISE_AND_SPIN = 8;
	public static final int MONSTER_ATTACK_TEXT = 9;
	public static final int PLAYER_ATTACK_TEXT = 10;
	public static final int FADE_OUT_SHAKING = 11;
	public static final int FADE_IN = 12;
	public static final int REDSCREEN = 13;
	public static final int ATTACK_SHAKE = 14;

	public static Animation getAttackAnimation(int type)
	{
		Log.d("animator", "getting attack type animation: " + type);

		Animation animation = null;
		animation = new AnimationSet(true);

		// swing
		if (type == 0)
		{

			// move up
			TranslateAnimation t1 = new TranslateAnimation(0, 0, 40, 0);
			t1.setDuration(250);
			t1.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(t1);

			// rotate
			RotateAnimation r =
				new RotateAnimation(-50, 60, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
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
		else if (type == 1)
		{
			// move up
			TranslateAnimation t1 = new TranslateAnimation(0, 0, 40, 0);
			t1.setDuration(250);
			t1.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(t1);

			// rotate
			RotateAnimation r;
			int start = -10;
			int end = 10;
			for (int a = 0; a < 4; a++)
			{
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
		// block
		else if (type == 2)
		{
			animation.setFillEnabled(true);

			// rotate
			RotateAnimation r =
				new RotateAnimation(0, -70, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.9f);
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

		return animation;
	}

	public static Animation getAnimation(int type)
	{

		Animation animation = null;

		// slowly move up
		if (type == 0)
		{
			animation = new TranslateAnimation(0, 0, 0, -100);
			animation.setDuration(2500);
		}

		// small random shake
		else if (type == 1)
		{
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
			for (int a = 0; a < 5; a++)
			{

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
		else if (type == 2)
		{
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
		else if (type == 3)
		{
			animation = new AlphaAnimation(0.3f, 1.0f);
			animation.setStartOffset(300);
			animation.setDuration(300);
			animation.setFillEnabled(true);
			animation.setFillAfter(false);
		}

		// scale away
		else if (type == 4)
		{
			animation = new AnimationSet(true);
			ScaleAnimation sa;
			AlphaAnimation aa;
			TranslateAnimation ta;

			// scale
			sa =
				new ScaleAnimation(10.0f, 0.5f, 10.0f, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			sa.setDuration(800);
			sa.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(sa);

			ta = new TranslateAnimation(0, 0, 0, -70);
			ta.setDuration(800);
			ta.setInterpolator(new AccelerateInterpolator());
			ta.setFillAfter(true);
			((AnimationSet) animation).addAnimation(ta);

			// disappear
			aa = new AlphaAnimation(1.0f, 0.0f);
			aa.setStartOffset(700);
			aa.setDuration(100);
			((AnimationSet) animation).addAnimation(aa);
		}

		// scale towards
		else if (type == 5)
		{
			animation = new AnimationSet(true);
			ScaleAnimation sa;
			AlphaAnimation aa;
			TranslateAnimation ta;

			// scale
			sa =
				new ScaleAnimation(0.1f, 4.0f, 0.1f, 4.0f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 1.0f);
			sa.setDuration(800);
			sa.setInterpolator(new AccelerateInterpolator());
			((AnimationSet) animation).addAnimation(sa);

			// disappear
			aa = new AlphaAnimation(1.0f, 0.0f);
			aa.setStartOffset(700);
			aa.setDuration(100);
			((AnimationSet) animation).addAnimation(aa);
		}

		// left to right
		else if (type == 6)
		{
			animation = new AnimationSet(true);

			TranslateAnimation t0 = new TranslateAnimation(0, 0, -50, -50);
			t0.setFillBefore(true);
			t0.setFillEnabled(true);
			((AnimationSet) animation).addAnimation(t0);

			TranslateAnimation t1 = new TranslateAnimation(-150, 50, 0, 0);
			t1.setDuration(1000);
			((AnimationSet) animation).addAnimation(t1);

			// scale
			ScaleAnimation sa =
				new ScaleAnimation(1.5f, 2.5f, 1.5f, 2.5f, Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			sa.setDuration(1000);
			((AnimationSet) animation).addAnimation(sa);

		}

		// shrink
		else if (type == 7)
		{
			animation =
				new ScaleAnimation(1.0f, 0.2f, 1.0f, 0.2f, Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 1.0f);

			animation.setDuration(1250);

			animation.setFillEnabled(true);
			animation.setFillAfter(true);
		}

		// raise and spin
		else if (type == 8)
		{
			animation = new AnimationSet(true);

			// raise
			TranslateAnimation t0 = new TranslateAnimation(0, 0, 0, -150);
			t0.setDuration(1000);
			t0.setInterpolator(new AccelerateInterpolator());
			t0.setFillEnabled(true);
			t0.setFillAfter(true);
			((AnimationSet) animation).addAnimation(t0);

			RotateAnimation rAnim =
				new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			rAnim.setRepeatCount(15);
			rAnim.setInterpolator(new AccelerateInterpolator());
			rAnim.setDuration(150);

			((AnimationSet) animation).addAnimation(rAnim);

		}

		// monster attack text
		else if (type == 9)
		{
			animation = new AnimationSet(true);
			ScaleAnimation sa;
			AlphaAnimation aa;
			TranslateAnimation ta;
			// start in random offset place
			ta =
				new TranslateAnimation(0, Helper.randomInt(20) * Helper.randomPosNeg(), 0, Helper.randomInt(20)
					* Helper.randomPosNeg());
			ta.setDuration(0);
			ta.setFillEnabled(true);
			ta.setFillBefore(true);
			((AnimationSet) animation).addAnimation(ta);

			// scale
			sa =
				new ScaleAnimation(0.1f, 2.0f, 0.1f, 2.0f, Animation.RELATIVE_TO_SELF, 0.5f,
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
		else if (type == 10)
		{
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
		
		//fade out shaking
		else if(type == 11)
		{
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
			for (int a = 0; a < 5; a++)
			{

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
		
		//fade in
		else if(type == 12)
		{
			animation = new AlphaAnimation(0.0f, 1.0f);
			animation.setDuration(1000);
			animation.setFillEnabled(true);
			animation.setFillBefore(true);
		}
		
		//red screen
		else if(type == 13)
		{
			animation = new AnimationSet(true);
			
			AlphaAnimation a1 = new AlphaAnimation(0.0f, 0.2f);
			a1.setDuration(250);
			((AnimationSet) animation).addAnimation(a1);
			
			//AlphaAnimation a2 = new AlphaAnimation(0.5f, 0.0f);
			//a2.setDuration(250);
			//a2.setStartOffset(250);
			//((AnimationSet) animation).addAnimation(a2);
		}
		
		//attack shake
		else if(type == 14)
		{
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
			for (int a = 0; a < 4; a++)
			{

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

		return animation;
	}

}
