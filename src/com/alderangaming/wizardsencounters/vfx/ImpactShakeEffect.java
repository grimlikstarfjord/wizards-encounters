package com.alderangaming.wizardsencounters.vfx;

import android.graphics.Canvas;
import android.graphics.Matrix;

public class ImpactShakeEffect implements VfxEffect {
    private final long startMs;
    private final long durationMs;
    private final float amplitudePx;

    public ImpactShakeEffect(long durationMs, float amplitudePx) {
        this.startMs = android.os.SystemClock.uptimeMillis();
        this.durationMs = durationMs;
        this.amplitudePx = amplitudePx;
    }

    public boolean isAlive(long nowMs) {
        return nowMs - startMs <= durationMs;
    }

    public void draw(Canvas c, long nowMs) {
        float t = Math.min(1f, (nowMs - startMs) / (float) durationMs);
        float decay = (float) Math.pow(1f - t, 2);
        float x = (float) ((Math.random() * 2 - 1) * amplitudePx * decay);
        float y = (float) ((Math.random() * 2 - 1) * amplitudePx * decay);
        c.translate(x, y);
    }
}
