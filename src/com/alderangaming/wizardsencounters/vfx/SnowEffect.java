package com.alderangaming.wizardsencounters.vfx;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;

import java.util.Random;

public class SnowEffect implements VfxEffect {
    private static class Flake {
        float x, y;
        float vy;
        float size;
        float swayPhase;
    }

    private final Random rng = new Random();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final SnowParams params;
    private final float densityScale;
    private Flake[] flakes;
    private boolean init;
    private long last;

    public SnowEffect(SnowParams params, float densityScale) {
        this.params = (params == null) ? SnowParams.defaults() : params;
        this.densityScale = Math.max(0.75f, densityScale);
        this.last = SystemClock.uptimeMillis();
        paint.setColor(this.params.color);
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
        for (int i = 0; i < flakes.length; i++) {
            Flake f = flakes[i];
            f.y += f.vy * dt;
            float sway = (float) Math.sin((nowMs * 0.001f) + f.swayPhase) * params.swayPxPerSec * dt;
            f.x += sway;
            c.drawCircle(f.x, f.y, f.size, paint);
            if (f.y - f.size > h + 8)
                respawn(f, w);
        }
    }

    private void init(Canvas c) {
        init = true;
        int count = Math.max(1, params.flakeCount);
        flakes = new Flake[count];
        int w = c.getWidth();
        for (int i = 0; i < count; i++) {
            Flake f = new Flake();
            f.size = rnd(params.sizeDpMin, params.sizeDpMax) * densityScale;
            f.x = rnd(-20, w + 20);
            f.y = rnd(-w, 0);
            f.vy = rnd(params.fallSpeedPxPerSecMin, params.fallSpeedPxPerSecMax);
            f.swayPhase = rnd(0f, (float) Math.PI * 2f);
            flakes[i] = f;
        }
    }

    private void respawn(Flake f, int w) {
        f.size = rnd(params.sizeDpMin, params.sizeDpMax) * densityScale;
        f.x = rnd(-20, w + 20);
        f.y = -10;
        f.vy = rnd(params.fallSpeedPxPerSecMin, params.fallSpeedPxPerSecMax);
        f.swayPhase = rnd(0f, (float) Math.PI * 2f);
    }

    private float rnd(float a, float b) {
        return a + rng.nextFloat() * (b - a);
    }
}
