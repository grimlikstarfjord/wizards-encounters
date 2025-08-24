package com.alderangaming.wizardsencounters.vfx;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Bubbling hot springs: small bubbles rise and pop, spawning concentric
 * ripples.
 */
public class HotSpringBubbleEffect implements VfxEffect {

    private static class Bubble {
        float x;
        float y;
        float vx;
        float vy;
        float radius;
        long birthMs;
        long lifeMs;
        boolean popped;
    }

    private static class Ripple {
        float x;
        float y;
        float startR;
        float endR;
        float width;
        long birthMs;
        long lifeMs;
        float jitterSeed;
    }

    private static final Random RNG = new Random();

    private final List<Bubble> bubbles = new ArrayList<Bubble>(128);
    private final List<Ripple> ripples = new ArrayList<Ripple>(64);
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final float[][] anchors;
    private final HotSpringBubbleParams params;
    private final float densityScale;
    private long lastMs;
    private float spawnAccumulator;

    public HotSpringBubbleEffect(float[][] anchors01, HotSpringBubbleParams params, float densityScale) {
        this.anchors = anchors01;
        this.params = (params == null) ? HotSpringBubbleParams.defaults() : params;
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

        // Spawn bubbles
        float spawnPerMs = (params.spawnPerSecond * params.intensityScale) / 1000f;
        spawnAccumulator += dt * spawnPerMs * Math.max(1, anchors != null ? anchors.length : 1);
        int spawnCount = (int) Math.floor(spawnAccumulator);
        spawnAccumulator -= spawnCount;
        for (int i = 0; i < spawnCount; i++)
            spawnBubble(c);

        // Update bubbles
        for (int i = bubbles.size() - 1; i >= 0; i--) {
            Bubble b = bubbles.get(i);
            float t = (nowMs - b.birthMs) / (float) b.lifeMs;
            if (t >= 1f) {
                // spawn ripples on pop
                if (!b.popped)
                    spawnRipples(b.x, b.y, nowMs);
                bubbles.remove(i);
                continue;
            }
            b.x += b.vx * dt;
            b.y += b.vy * dt;

            int alpha = (int) (255 * Math.min(1f, 0.5f * params.intensityScale));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(Math.max(1f, b.radius * 0.15f));
            paint.setColor((params.bubbleColor & 0x00FFFFFF) | (alpha << 24));
            c.drawCircle(b.x, b.y, b.radius, paint);
        }

        // Update ripples
        for (Iterator<Ripple> it = ripples.iterator(); it.hasNext();) {
            Ripple r = it.next();
            float t = (nowMs - r.birthMs) / (float) r.lifeMs;
            if (t >= 1f) {
                it.remove();
                continue;
            }
            // Eased expansion with slight surface jitter
            float te = easeOutCubic(t);
            float rNow = lerp(r.startR, r.endR, te);
            float wobble = (float) Math.sin((nowMs - r.birthMs) * 0.012f + r.jitterSeed) * rNow * 0.02f;
            rNow = Math.max(1f, rNow + wobble);

            float band = Math.max(1f, r.width);
            float radius = rNow + band;
            float inner = clamp((rNow - band) / radius, 0f, 1f);
            float mid = clamp(rNow / radius, 0f, 1f);
            float outer = 1f;

            float alphaFactor = (float) Math.pow(1f - t, 1.8f);
            int ringAlpha = (int) (255 * alphaFactor * Math.min(1f, params.intensityScale));
            int ringColor = (params.rippleColor & 0x00FFFFFF) | (ringAlpha << 24);

            paint.setShader(new RadialGradient(r.x, r.y, radius,
                    new int[] { 0x00000000, ringColor, 0x00000000 },
                    new float[] { inner, mid, outer }, Shader.TileMode.CLAMP));
            c.drawCircle(r.x, r.y, radius, paint);
            paint.setShader(null);
        }
    }

    private void spawnBubble(Canvas c) {
        if (anchors == null || anchors.length == 0)
            return;
        float[] a = anchors[RNG.nextInt(anchors.length)];
        float px = a[0] * c.getWidth();
        float py = a[1] * c.getHeight();
        Bubble b = new Bubble();
        b.x = px + rndRange(-2f, 2f) * densityScale;
        b.y = py + rndRange(-2f, 2f) * densityScale;
        b.vx = rndRange(-params.horizontalJitterPxPerMs, params.horizontalJitterPxPerMs) * densityScale;
        b.vy = -rndRange(params.riseSpeedPxPerMsMin, params.riseSpeedPxPerMsMax) * densityScale;
        b.radius = rndRange(params.startRadiusDpMin, params.startRadiusDpMax) * densityScale;
        b.lifeMs = (long) rndRange(params.lifeMsMin, params.lifeMsMax);
        b.birthMs = SystemClock.uptimeMillis();
        bubbles.add(b);
    }

    private void spawnRipples(float x, float y, long nowMs) {
        for (int i = 0; i < Math.max(1, params.rippleCount); i++) {
            Ripple r = new Ripple();
            r.x = x;
            r.y = y;
            r.startR = params.rippleStartRadiusDp * densityScale * (1f + i * 0.25f);
            r.endR = params.rippleEndRadiusDp * densityScale * (1f + i * 0.25f);
            r.width = Math.max(1f, params.rippleWidthDp * densityScale);
            r.lifeMs = (long) (params.rippleLifeMs * (1f + i * 0.25f));
            r.birthMs = nowMs;
            r.jitterSeed = rndRange(0f, (float) Math.PI * 2f);
            ripples.add(r);
        }
    }

    private static float rndRange(float a, float b) {
        return a + RNG.nextFloat() * (b - a);
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    private static float clamp(float v, float a, float b) {
        return Math.max(a, Math.min(b, v));
    }

    private static float easeOutCubic(float t) {
        float inv = 1f - t;
        return 1f - inv * inv * inv;
    }
}
