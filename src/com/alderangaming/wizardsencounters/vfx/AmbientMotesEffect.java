package com.alderangaming.wizardsencounters.vfx;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;

import java.util.Random;

public class AmbientMotesEffect implements VfxEffect {
    private static class Mote {
        float x;
        float y;
        float vx;
        float vy;
        float radius;
        int baseColor;
        float alpha;
        float noiseOffset;
        float flowPhase;
        float preferredSpeed;
    }

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Random rng = new Random();
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

        int count = Math.max(25, (int) (width * height / 60000f));
        motes = new Mote[count];
        for (int i = 0; i < count; i++) {
            Mote m = new Mote();
            m.x = rng.nextFloat() * width;
            m.y = rng.nextFloat() * height;
            float speed = 5f + rng.nextFloat() * 10f;
            float angle = (float) (rng.nextFloat() * Math.PI * 2);
            m.vx = (float) Math.cos(angle) * speed;
            m.vy = (float) Math.sin(angle) * speed;
            m.radius = 2.5f + rng.nextFloat() * 3.5f;
            m.baseColor = 0xFFFFFFCC; // soft warm
            m.alpha = 0.30f + rng.nextFloat() * 0.20f; // more visible
            m.noiseOffset = rng.nextFloat() * 1000f;
            m.flowPhase = rng.nextFloat() * (float) (Math.PI * 2);
            m.preferredSpeed = 8f + rng.nextFloat() * 8f; // px/sec
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
            m.vx += (dx / Math.max(1e-3f, dist)) * impulse * 0.15f;
            m.vy += (dy / Math.max(1e-3f, dist)) * impulse * 0.15f;
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

        float t = nowMs * 0.001f;
        for (int i = 0; i < motes.length; i++) {
            Mote m = motes[i];
            // smooth flow field: direction changes slowly over time and space
            float flowAngle = (float) (Math.sin((m.x + m.noiseOffset) * 0.004 + t * 0.25) * 0.9 +
                    Math.cos((m.y - m.noiseOffset) * 0.004 + t * 0.21) * 0.6 +
                    Math.sin(t * 0.17 + m.flowPhase) * 0.4);
            // project to unit direction
            float dirX = (float) Math.cos(flowAngle);
            float dirY = (float) Math.sin(flowAngle);

            // apply gentle propulsion toward preferred speed
            float speed = (float) Math.sqrt(m.vx * m.vx + m.vy * m.vy);
            float targetSpeed = m.preferredSpeed;
            float speedError = targetSpeed - speed;
            float accel = 6f; // px/sec^2
            m.vx += dirX * accel * dt + (rng.nextFloat() - 0.5f) * 1.2f * dt;
            m.vy += dirY * accel * dt + (rng.nextFloat() - 0.5f) * 1.2f * dt;

            // slight steering to maintain speed around preferred
            if (speed > 1e-3f) {
                float scale = Math.min(1f, Math.max(-1f, speedError * 0.08f));
                m.vx += (m.vx / speed) * scale;
                m.vy += (m.vy / speed) * scale;
            }

            // dt-aware drag to prevent runaway velocity but never fully stopping
            float dragPerSec = 0.10f; // 10% loss per second
            float drag = (float) Math.pow(1.0 - dragPerSec, dt);
            m.vx *= drag;
            m.vy *= drag;

            // clamp max speed to avoid outliers on a straight path
            float maxSpeed = 28f; // px/sec
            speed = (float) Math.sqrt(m.vx * m.vx + m.vy * m.vy);
            if (speed > maxSpeed) {
                float s = maxSpeed / Math.max(1e-3f, speed);
                m.vx *= s;
                m.vy *= s;
            }

            // integrate
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
