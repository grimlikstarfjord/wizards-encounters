package com.alderangaming.wizardsencounters.vfx;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

public class RainEffect implements VfxEffect {
    private static class Drop {
        float x, y;
        float vx, vy;
        float len;
    }

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Random rng = new Random();
    private Drop[] drops;
    private boolean init = false;
    private int w, h;

    public RainEffect(int color, int count) {
        paint.setColor(color);
        paint.setStrokeWidth(3.2f);
    }

    private void ensure(int width, int height) {
        if (init)
            return;
        this.w = width;
        this.h = height;
        int count = Math.max(180, (int) (width * height / 16000f));
        drops = new Drop[count];
        for (int i = 0; i < count; i++) {
            drops[i] = spawnDrop(rng.nextFloat() * width, rng.nextFloat() * height);
        }
        init = true;
    }

    private Drop spawnDrop(float x, float y) {
        Drop d = new Drop();
        d.x = x;
        d.y = y;
        d.vx = -120f + rng.nextFloat() * 40f; // stronger wind
        d.vy = 900f + rng.nextFloat() * 320f;
        d.len = 16f + rng.nextFloat() * 18f;
        return d;
    }

    public boolean isAlive(long nowMs) {
        return true;
    }

    public void draw(Canvas c, long nowMs) {
        ensure(c.getWidth(), c.getHeight());
        for (int i = 0; i < drops.length; i++) {
            Drop d = drops[i];
            // integrate assuming ~60fps steps; motion looks fine without precise dt
            d.x += d.vx / 50f;
            d.y += d.vy / 50f;
            float x2 = d.x + d.vx * 0.012f;
            float y2 = d.y + d.len;
            c.drawLine(d.x, d.y, x2, y2, paint);
            if (d.y > h + 20) {
                drops[i] = spawnDrop(rng.nextFloat() * w, -rng.nextFloat() * 100f);
            }
        }
    }
}
