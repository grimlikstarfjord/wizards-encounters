package com.alderangaming.wizardsencounters.vfx;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;

import java.util.Random;

public class AmbientMotesEffect implements VfxEffect {
    private static class Mote {
        float x;
        float y;
        float homeX;
        float homeY;
        float vx;
        float vy;
        float radius;
        int baseColor;
        float alpha;
    }

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Random rng = new Random(12345L);
    private Mote[] motes;
    private long lastDrawMs = 0L;
    private boolean initialized = false;
    private int canvasWidth = 0;
    private int canvasHeight = 0;

    private void init(int width, int height) {
        if (width <= 0 || height <= 0)
            return;
        if (initialized)
            return;
        initialized = true;

        int count = Math.max(22, (int) (width * height / 60000f));
        motes = new Mote[count];
        for (int i = 0; i < count; i++) {
            Mote m = new Mote();
            m.x = rng.nextFloat() * width;
            m.y = rng.nextFloat() * height;
            m.homeX = m.x;
            m.homeY = m.y;
            float speed = 3f + rng.nextFloat() * 5f; // px/s idle drift (slower)
            float angle = (float) (rng.nextFloat() * Math.PI * 2);
            m.vx = (float) Math.cos(angle) * speed * 0.15f;
            m.vy = (float) Math.sin(angle) * speed * 0.15f;
            m.radius = 2.5f + rng.nextFloat() * 3.5f;
            m.baseColor = 0xFFFFFFCC; // soft warm
            m.alpha = 0.22f + rng.nextFloat() * 0.28f;
            motes[i] = m;
        }
    }

    public void push(float cx, float cy, float strength, float radius) {
        if (motes == null)
            return;
        float r2 = radius * radius;
        for (int i = 0; i < motes.length; i++) {
            Mote m = motes[i];
            float dx = m.x - cx;
            float dy = m.y - cy;
            float dist2 = dx * dx + dy * dy;
            if (dist2 > r2)
                continue;
            float dist = (float) Math.sqrt(Math.max(1e-3f, dist2));
            float falloff = 1f - (dist / radius);
            float impulse = strength * falloff;
            m.vx += (dx / Math.max(1e-3f, dist)) * impulse * 0.04f;
            m.vy += (dy / Math.max(1e-3f, dist)) * impulse * 0.04f;
        }
    }

    public boolean isAlive(long nowMs) {
        return true;
    }

    public void draw(Canvas c, long nowMs) {
        if (!initialized)
            init(c.getWidth(), c.getHeight());
        if (motes == null)
            return;

        if (lastDrawMs == 0L)
            lastDrawMs = nowMs;
        float dtMs = Math.min(50f, nowMs - lastDrawMs);
        float dt = dtMs / 1000f; // seconds
        lastDrawMs = nowMs;

        canvasWidth = c.getWidth();
        canvasHeight = c.getHeight();
        int w = canvasWidth;
        int h = canvasHeight;

        for (int i = 0; i < motes.length; i++) {
            Mote m = motes[i];
            // spring back to home subtly
            float toHomeX = (m.homeX - m.x) * 0.12f;
            float toHomeY = (m.homeY - m.y) * 0.12f;
            m.vx += toHomeX * dt;
            m.vy += toHomeY * dt;

            // gentle random noise (subtle)
            m.vx += (rng.nextFloat() - 0.5f) * 0.4f * dt;
            m.vy += (rng.nextFloat() - 0.5f) * 0.4f * dt;

            // mild drag
            m.vx *= 0.996f;
            m.vy *= 0.996f;

            // integrate with seconds
            m.x += m.vx * dt;
            m.y += m.vy * dt;

            // wrap around bounds
            if (m.x < -10)
                m.x = w + 10;
            if (m.x > w + 10)
                m.x = -10;
            if (m.y < -10)
                m.y = h + 10;
            if (m.y > h + 10)
                m.y = -10;

            int a = (int) (255 * m.alpha);
            paint.setColor((a << 24) | (m.baseColor & 0x00FFFFFF));
            c.drawCircle(m.x, m.y, m.radius, paint);
        }
    }
}
