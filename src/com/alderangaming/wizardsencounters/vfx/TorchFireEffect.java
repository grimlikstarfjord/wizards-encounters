package com.alderangaming.wizardsencounters.vfx;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Ambient torch fire effect.
 *
 * Tuning guide:
 * - intensity01 (constructor): scales how many particles spawn per frame. Lower
 * = calmer flame.
 * - spawnCount (in draw): base emission per anchor; raise/lower for global
 * strength.
 * - spawnParticle:
 * - vx/vy: horizontal jitter and upward speed. Make vy less negative for slower
 * rise.
 * - startSize/endSize: width/expansion. Increase for wider, glowier flames.
 * - startColor/endColor: ARGB colors from core→edge. Change for different hues.
 * - lifeMs: particle lifetime. Increase with slower vy for lazier flames.
 * Anchors are normalized (0..1) so placement scales across device sizes.
 */
public class TorchFireEffect implements VfxEffect {

    private static class Particle {
        float x;
        float y;
        float vx;
        float vy;
        float startSize;
        float endSize;
        int startColor;
        int endColor;
        long birthMs;
        long lifeMs;
    }

    private static final Random RNG = new Random();

    // Torch anchors in normalized coordinates (0..1 of view width/height)
    private final float[][] anchors;
    private final float intensity; // emission multiplier
    private final float densityScale; // dp scaling for sizes/velocities
    private final TorchFireParams params = TorchFireParams.defaults();
    private final Paint addPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final List<Particle> particles = new ArrayList<Particle>(256);
    private long lastMs;

    /**
     * @param anchors01    Array of {x01, y01} anchor coords (0..1)
     * @param intensity01  Spawn density multiplier (0..1 typical, can be >1)
     * @param densityScale Use DisplayMetrics.density for consistent sizing
     */
    public TorchFireEffect(float[][] anchors01, float intensity01, float densityScale) {
        this.anchors = anchors01;
        this.intensity = Math.max(0.05f, intensity01);
        this.densityScale = Math.max(0.75f, densityScale);
        this.lastMs = SystemClock.uptimeMillis();
        addPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
    }

    public TorchFireEffect(float[][] anchors01, float intensity01, float densityScale, TorchFireParams customParams) {
        this.anchors = anchors01;
        this.intensity = Math.max(0.05f, intensity01);
        this.densityScale = Math.max(0.75f, densityScale);
        if (customParams != null) {
            // copy into our final params instance
            this.params.intensityScale = customParams.intensityScale;
            this.params.sizeScale = customParams.sizeScale;
            this.params.speedScale = customParams.speedScale;
            this.params.jitterScale = customParams.jitterScale;
            this.params.lifeScale = customParams.lifeScale;
            this.params.startColor = customParams.startColor;
            this.params.endColor = customParams.endColor;
        }
        this.lastMs = SystemClock.uptimeMillis();
        addPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
    }

    @Override
    public boolean isAlive(long nowMs) {
        // continuous ambient
        return true;
    }

    @Override
    public void draw(Canvas c, long nowMs) {
        float dt = Math.min(50, Math.max(0, nowMs - lastMs));
        lastMs = nowMs;

        // Baseline emission per anchor. Increase for stronger flame; decrease for
        // subtler flame.
        int spawnCount = Math.max(1, (int) (4 * intensity * params.intensityScale));
        for (float[] a : anchors) {
            float px = a[0] * c.getWidth();
            float py = a[1] * c.getHeight();
            for (int i = 0; i < spawnCount; i++)
                spawnParticle(px, py);
        }

        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            float t = (nowMs - p.birthMs) / (float) p.lifeMs;
            if (t >= 1f) {
                particles.remove(i);
                continue;
            }

            p.x += p.vx * dt;
            p.y += p.vy * dt;

            float size = lerp(p.startSize, p.endSize, t);
            int color = lerpColor(p.startColor, p.endColor, t);

            int center = (color & 0x00FFFFFF) | ((int) (255 * (1f - t)) << 24);
            int edge = 0x00000000;
            addPaint.setShader(new RadialGradient(p.x, p.y, size,
                    new int[] { center, edge }, new float[] { 0f, 1f }, Shader.TileMode.CLAMP));
            c.drawCircle(p.x, p.y, size, addPaint);
            addPaint.setShader(null);
        }
    }

    private void spawnParticle(float x, float y) {
        // Base jitter around anchor; increase ranges for broader base flicker
        Particle p = new Particle();
        p.x = x + rndRange(-2f, 2f) * densityScale * params.jitterScale;
        p.y = y + rndRange(-1f, 1f) * densityScale * params.jitterScale;
        p.vx = rndRange(-0.02f, 0.02f) * densityScale * params.speedScale; // px/ms
        p.vy = rndRange(-0.28f, -0.12f) * densityScale * params.speedScale; // px/ms (negative = up)
        p.startSize = rndRange(5f, 13f) * densityScale * params.sizeScale;
        p.endSize = p.startSize * rndRange(1.7f, 2.8f);
        p.startColor = params.startColor;
        p.endColor = params.endColor;
        p.lifeMs = (long) (rndRange(450, 750) * params.lifeScale);
        p.birthMs = SystemClock.uptimeMillis();
        particles.add(p);
    }

    private static float rndRange(float a, float b) {
        return a + RNG.nextFloat() * (b - a);
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    private static int lerpColor(int c1, int c2, float t) {
        int a = (int) (((c1 >>> 24) & 0xFF) + (((c2 >>> 24) & 0xFF) - ((c1 >>> 24) & 0xFF)) * t);
        int r = (int) (((c1 >>> 16) & 0xFF) + (((c2 >>> 16) & 0xFF) - ((c1 >>> 16) & 0xFF)) * t);
        int g = (int) (((c1 >>> 8) & 0xFF) + (((c2 >>> 8) & 0xFF) - ((c1 >>> 8) & 0xFF)) * t);
        int b = (int) ((c1 & 0xFF) + ((c2 & 0xFF) - (c1 & 0xFF)) * t);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
