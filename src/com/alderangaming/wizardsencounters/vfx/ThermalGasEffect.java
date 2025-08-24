package com.alderangaming.wizardsencounters.vfx;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Thermal gas effect: small puffs bubble up and expand, with a sulfurous tint.
 */
public class ThermalGasEffect implements VfxEffect {

    private static class Puff {
        float x;
        float y;
        float vx;
        float vy;
        float startSize;
        float endSize;
        float wobbleHz;
        long birthMs;
        long lifeMs;
    }

    private static final Random RNG = new Random();

    private final List<Puff> puffs = new ArrayList<Puff>(128);
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final float[][] anchors;
    private final ThermalGasParams params;
    private final float densityScale;
    private long lastMs;
    private float spawnAccumulator;

    public ThermalGasEffect(float[][] anchors01, ThermalGasParams params, float densityScale) {
        this.anchors = anchors01;
        this.params = (params == null) ? ThermalGasParams.defaults() : params;
        this.densityScale = Math.max(0.75f, densityScale);
        this.lastMs = SystemClock.uptimeMillis();
    }

    @Override
    public boolean isAlive(long nowMs) {
        return true;
    }

    @Override
    public void draw(Canvas c, long nowMs) {
        float dt = Math.min(50, Math.max(0, nowMs - lastMs));
        lastMs = nowMs;

        // Spawn based on time accumulation
        float spawnPerMs = (params.spawnPerSecond * params.intensityScale) / 1000f;
        spawnAccumulator += dt * spawnPerMs * Math.max(1, anchors != null ? anchors.length : 1);
        int spawnCount = (int) Math.floor(spawnAccumulator);
        spawnAccumulator -= spawnCount;
        for (int i = 0; i < spawnCount; i++) {
            spawnPuff(c);
        }

        // Update and draw
        for (int i = puffs.size() - 1; i >= 0; i--) {
            Puff p = puffs.get(i);
            float t = (nowMs - p.birthMs) / (float) p.lifeMs;
            if (t >= 1f) {
                puffs.remove(i);
                continue;
            }

            // Rise and wobble
            float wobble = (float) Math.sin((nowMs - p.birthMs) * 0.001f * (Math.PI * 2) * p.wobbleHz)
                    * (params.wobbleAmplitudeDp * densityScale);
            p.x += (p.vx) * dt;
            p.y += (p.vy) * dt;

            float size = lerp(p.startSize, p.endSize, t);
            int alphaInt = (int) (255 * lerp(params.minAlpha, params.maxAlpha, 1f - t) * params.intensityScale);
            int start = (params.startColor & 0x00FFFFFF) | (alphaInt << 24);
            int end = 0x00000000;
            paint.setShader(new RadialGradient(p.x + wobble, p.y, size,
                    new int[] { start, end }, new float[] { 0f, 1f }, Shader.TileMode.CLAMP));
            c.drawCircle(p.x + wobble, p.y, size, paint);
            paint.setShader(null);
        }
    }

    private void spawnPuff(Canvas c) {
        if (anchors == null || anchors.length == 0)
            return;
        float[] a = anchors[RNG.nextInt(anchors.length)];
        float px = a[0] * c.getWidth();
        float py = a[1] * c.getHeight();

        Puff p = new Puff();
        p.x = px + rndRange(-2f, 2f) * densityScale;
        p.y = py + rndRange(-3f, 3f) * densityScale;
        p.vx = rndRange(-params.horizontalJitterPxPerMs, params.horizontalJitterPxPerMs) * densityScale;
        p.vy = -params.riseSpeedPxPerMs * densityScale * rndRange(0.85f, 1.15f);
        p.startSize = rndRange(params.startSizeDpMin, params.startSizeDpMax) * densityScale;
        p.endSize = p.startSize * rndRange(params.endSizeMultiplierMin, params.endSizeMultiplierMax);
        p.wobbleHz = rndRange(params.wobbleHzMin, params.wobbleHzMax);
        p.lifeMs = (long) rndRange(params.lifeMsMin, params.lifeMsMax);
        p.birthMs = SystemClock.uptimeMillis();
        puffs.add(p);
    }

    private static float rndRange(float a, float b) {
        return a + RNG.nextFloat() * (b - a);
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
}
