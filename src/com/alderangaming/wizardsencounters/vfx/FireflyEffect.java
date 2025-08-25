package com.alderangaming.wizardsencounters.vfx;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.SystemClock;

import java.util.Random;

/**
 * Soft glowing fireflies that wander around anchors and blink.
 */
public class FireflyEffect implements VfxEffect {

    private static class Fly {
        float x, y;
        float targetX, targetY;
        float radius;
        float blinkHz;
        float phase;
    }

    private final Random rng = new Random();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final float[][] anchors;
    private final FireflyParams params;
    private final float densityScale;
    private Fly[] flies;
    private boolean initialized;
    private long last;

    public FireflyEffect(float[][] anchors01, FireflyParams params, float densityScale) {
        this.anchors = anchors01;
        this.params = (params == null) ? FireflyParams.defaults() : params;
        this.densityScale = Math.max(0.75f, densityScale);
        this.last = SystemClock.uptimeMillis();
    }

    @Override
    public boolean isAlive(long nowMs) {
        return true;
    }

    @Override
    public void draw(Canvas c, long nowMs) {
        if (!initialized)
            init(c);
        float dt = Math.min(50, Math.max(0, nowMs - last)) / 1000f;
        last = nowMs;
        float area = params.areaRadiusDp * densityScale;

        for (int i = 0; i < flies.length; i++) {
            Fly f = flies[i];
            // drift toward target
            float dx = f.targetX - f.x;
            float dy = f.targetY - f.y;
            float dist = (float) Math.sqrt(dx * dx + dy * dy) + 1e-3f;
            float speed = params.wanderSpeedPxPerSec * densityScale;
            f.x += dx / dist * speed * dt;
            f.y += dy / dist * speed * dt;
            if (dist < 4f)
                pickTarget(i, c.getWidth(), c.getHeight(), area);

            float blink = 0.6f + 0.4f * (float) Math.sin((nowMs * 0.001f + f.phase) * (Math.PI * 2) * f.blinkHz);
            int alpha = (int) (200 * blink);
            int core = (params.color & 0x00FFFFFF) | (alpha << 24);
            paint.setShader(new RadialGradient(f.x, f.y, f.radius,
                    new int[] { core, 0x00000000 }, new float[] { 0f, 1f }, Shader.TileMode.CLAMP));
            c.drawCircle(f.x, f.y, f.radius, paint);
            paint.setShader(null);
        }
    }

    private void init(Canvas c) {
        initialized = true;
        int per = Math.max(1, params.countPerAnchor);
        flies = new Fly[per * Math.max(1, anchors == null ? 1 : anchors.length)];
        float area = params.areaRadiusDp * densityScale;
        for (int i = 0; i < flies.length; i++) {
            flies[i] = new Fly();
            flies[i].radius = rndRange(params.radiusDpMin, params.radiusDpMax) * densityScale;
            flies[i].blinkHz = rndRange(params.blinkHzMin, params.blinkHzMax);
            flies[i].phase = rndRange(0f, 1f);
            pickStart(i, c.getWidth(), c.getHeight(), area);
            pickTarget(i, c.getWidth(), c.getHeight(), area);
        }
    }

    private void pickStart(int idx, int w, int h, float area) {
        float[] a = anchors == null || anchors.length == 0 ? new float[] { 0.5f, 0.5f } : anchors[idx % anchors.length];
        float cx = a[0] * w, cy = a[1] * h;
        Fly f = flies[idx];
        f.x = cx + rndRange(-area, area);
        f.y = cy + rndRange(-area, area);
    }

    private void pickTarget(int idx, int w, int h, float area) {
        float[] a = anchors == null || anchors.length == 0 ? new float[] { 0.5f, 0.5f } : anchors[idx % anchors.length];
        float cx = a[0] * w, cy = a[1] * h;
        Fly f = flies[idx];
        f.targetX = cx + rndRange(-area, area);
        f.targetY = cy + rndRange(-area, area);
    }

    private float rndRange(float a, float b) {
        return a + rng.nextFloat() * (b - a);
    }
}
