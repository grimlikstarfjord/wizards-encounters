package com.alderangaming.wizardsencounters.vfx;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Soft drifting fog composed of semi-transparent radial gradients.
 * Designed to be light and run behind gameplay layers.
 */
public class FogEffect implements VfxEffect {

    private static class Puff {
        float x;
        float y;
        float radius;
        float speedPxPerMs;
        float alpha;
        int color;
    }

    private static final Random RNG = new Random();

    private final List<Puff> puffs = new ArrayList<Puff>();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final FogParams params;
    private final float densityScale;
    private long lastMs;

    public FogEffect(FogParams params, float densityScale) {
        this.params = (params == null) ? FogParams.defaults() : params;
        this.densityScale = Math.max(0.75f, densityScale);
        this.lastMs = SystemClock.uptimeMillis();
    }

    @Override
    public boolean isAlive(long nowMs) {
        return true; // continuous ambient
    }

    @Override
    public void draw(Canvas c, long nowMs) {
        if (puffs.isEmpty()) {
            spawnInitial(c);
        }

        float dt = Math.min(50, Math.max(0, nowMs - lastMs));
        lastMs = nowMs;

        float width = c.getWidth();
        float height = c.getHeight();

        // Wind oscillation factor ([-amp..+amp])
        float windFactor = 0f;
        if (params.windOscillationPeriodMs > 0f && params.windOscillationAmplitude > 0f) {
            float phase = (nowMs % params.windOscillationPeriodMs) / params.windOscillationPeriodMs;
            windFactor = (float) Math.sin(phase * Math.PI * 2) * params.windOscillationAmplitude;
        }

        // Move and draw puffs
        for (int i = 0; i < puffs.size(); i++) {
            Puff p = puffs.get(i);

            // Horizontal drift
            float speed = p.speedPxPerMs * (1f + windFactor);
            p.x += speed * dt * params.directionX;

            // Gentle vertical sine-ish drift via random walk
            p.y += rndRange(-params.verticalJitter01, params.verticalJitter01) * height * 0.02f;

            // Wrap around horizontally
            if (params.directionX > 0 && p.x - p.radius > width + 10) {
                respawnPuff(p, -p.radius - 10, height);
            } else if (params.directionX < 0 && p.x + p.radius < -10) {
                respawnPuff(p, width + p.radius + 10, height);
            }

            // Subtle alpha pulse per puff
            float alphaPulse = 0.0f;
            if (params.intensityScale != 1.0f) {
                // stronger intensity → slightly stronger alpha
                alphaPulse += (params.intensityScale - 1.0f) * 0.08f;
            }
            float minA = params.minAlpha * params.intensityScale;
            float maxA = params.maxAlpha * Math.max(1.0f, params.intensityScale);
            int alphaInt = (int) (255 * clamp(p.alpha + alphaPulse, minA, maxA));
            int color = (params.color & 0x00FFFFFF) | (alphaInt << 24);
            paint.setShader(new RadialGradient(p.x, p.y, p.radius,
                    new int[] { color, 0x00000000 }, new float[] { 0f, 1f }, Shader.TileMode.CLAMP));
            c.drawCircle(p.x, p.y, p.radius, paint);
            paint.setShader(null);
        }
    }

    private void spawnInitial(Canvas c) {
        float width = c.getWidth();
        float height = c.getHeight();
        int count = Math.max(1, Math.round(params.puffCount * params.intensityScale));
        for (int i = 0; i < count; i++) {
            Puff p = new Puff();
            p.radius = rndRange(60f, 140f) * densityScale * params.sizeScale;
            p.x = rndRange(-p.radius, width + p.radius);
            p.y = lerp(params.heightStart01, params.heightEnd01, RNG.nextFloat()) * height;
            p.speedPxPerMs = (params.baseSpeed * densityScale) * rndRange(0.85f, 1.25f);
            p.alpha = lerp(params.minAlpha, params.maxAlpha, RNG.nextFloat()) * params.intensityScale;
            p.color = params.color;
            puffs.add(p);
        }
    }

    private void respawnPuff(Puff p, float newX, float height) {
        p.x = newX;
        p.y = lerp(params.heightStart01, params.heightEnd01, RNG.nextFloat()) * height;
        p.radius = p.radius * rndRange(0.85f, 1.15f);
        p.speedPxPerMs = (params.baseSpeed * densityScale) * rndRange(0.7f, 1.3f);
        p.alpha = lerp(params.minAlpha, params.maxAlpha, RNG.nextFloat());
    }

    private static float rndRange(float a, float b) {
        return a + RNG.nextFloat() * (b - a);
    }

    private static float clamp(float v, float a, float b) {
        return Math.max(a, Math.min(b, v));
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
}
