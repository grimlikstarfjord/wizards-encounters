package com.alderangaming.wizardsencounters.vfx;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

public class SparkBurstEffect implements VfxEffect {
    private static class P {
        float x, y;
        float vx, vy;
        float lifeMs, maxLifeMs;
        int color;
    }

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final P[] ps;
    private final long startMs;
    private final float gravity;

    public SparkBurstEffect(float cx, float cy, int count, long durationMs, int color, float speedPxPerS) {
        this.startMs = android.os.SystemClock.uptimeMillis();
        this.gravity = 480f; // px/s^2
        Random rng = new Random();
        ps = new P[count];
        for (int i = 0; i < count; i++) {
            float ang = (float) (rng.nextFloat() * Math.PI * 2);
            float spd = speedPxPerS * (0.6f + rng.nextFloat() * 0.6f);
            P p = new P();
            p.x = cx;
            p.y = cy;
            p.vx = (float) Math.cos(ang) * spd;
            p.vy = (float) Math.sin(ang) * spd;
            p.maxLifeMs = durationMs * (0.7f + rng.nextFloat() * 0.6f);
            p.lifeMs = 0f;
            p.color = color;
            ps[i] = p;
        }
    }

    public boolean isAlive(long nowMs) {
        for (int i = 0; i < ps.length; i++)
            if (ps[i].lifeMs < ps[i].maxLifeMs)
                return true;
        return false;
    }

    public void draw(Canvas c, long nowMs) {
        float dt = 16f / 1000f; // assume ~60fps; overlay schedules redraws
        for (int i = 0; i < ps.length; i++) {
            P p = ps[i];
            if (p.lifeMs >= p.maxLifeMs)
                continue;
            p.lifeMs += 16f;
            // integrate simple motion
            p.x += p.vx * dt;
            p.y += p.vy * dt;
            p.vy += gravity * dt;

            float t = p.lifeMs / p.maxLifeMs;
            int a = (int) (255 * (1f - t));
            paint.setColor((a << 24) | (p.color & 0x00FFFFFF));
            paint.setStrokeWidth(3.0f);
            float lx = p.x - p.vx * 0.02f;
            float ly = p.y - p.vy * 0.02f;
            c.drawLine(lx, ly, p.x, p.y, paint);
        }
    }
}
