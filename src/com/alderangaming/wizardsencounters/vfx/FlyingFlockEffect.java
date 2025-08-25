package com.alderangaming.wizardsencounters.vfx;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;

import java.util.Random;

public class FlyingFlockEffect implements VfxEffect {
    private static class Bird {
        float x, y;
        float swayPhase;
        float size;
    }

    private final FlyingFlockParams params;
    private final float densityScale;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Random rng = new Random();
    private Bird[] birds;
    private boolean init;
    private long last;
    private float dir = 1f;

    public FlyingFlockEffect(FlyingFlockParams params, float densityScale) {
        this.params = (params == null) ? FlyingFlockParams.defaults() : params;
        this.densityScale = Math.max(0.75f, densityScale);
        this.last = SystemClock.uptimeMillis();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(this.params.color);
        // Randomize left->right or right->left
        dir = rng.nextBoolean() ? 1f : -1f;
    }

    @Override
    public boolean isAlive(long nowMs) {
        return true;
    }

    @Override
    public void draw(Canvas c, long nowMs) {
        if (!init)
            init(c);
        float dt = Math.min(50, Math.max(0, nowMs - last)) / 1000f;
        last = nowMs;
        int w = c.getWidth();
        int h = c.getHeight();
        for (int i = 0; i < birds.length; i++) {
            Bird b = birds[i];
            float sway = (float) Math.sin((nowMs * 0.001f) + b.swayPhase) * params.swayPxPerSec * dt;
            b.x += (params.speedPxPerSec * dir * dt) + sway;
            // wrap
            if (dir > 0 && b.x > w + 20)
                b.x = -20;
            if (dir < 0 && b.x < -20)
                b.x = w + 20;
            // simple V wing shape as two short lines
            float y = b.y;
            float s = b.size;
            c.drawLine(b.x - s, y, b.x, y - s * 0.5f, paint);
            c.drawLine(b.x + s, y, b.x, y - s * 0.5f, paint);
        }
    }

    private void init(Canvas c) {
        init = true;
        int n = Math.max(1, params.count);
        birds = new Bird[n];
        int w = c.getWidth();
        int h = c.getHeight();
        float y = params.altitude01 * h;
        for (int i = 0; i < n; i++) {
            Bird b = new Bird();
            b.size = (2.2f + rng.nextFloat() * 2.2f) * densityScale;
            float spread = params.spreadPx * densityScale;
            b.x = (w * 0.5f) + (rng.nextFloat() - 0.5f) * spread;
            b.y = y + (rng.nextFloat() - 0.5f) * (spread * 0.25f);
            b.swayPhase = rng.nextFloat() * (float) Math.PI * 2f;
            birds[i] = b;
        }
    }
}
