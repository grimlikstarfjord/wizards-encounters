package com.alderangaming.wizardsencounters.vfx;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;

public class ShockwaveRingEffect implements VfxEffect {
    private final float centerX;
    private final float centerY;
    private final float maxRadiusPx;
    private final long startMs;
    private final long durationMs;
    private final Paint paint;

    public ShockwaveRingEffect(float cx, float cy, float maxRadiusPx, long startMs, long durationMs, int color) {
        this.centerX = cx;
        this.centerY = cy;
        this.maxRadiusPx = maxRadiusPx;
        this.startMs = startMs;
        this.durationMs = durationMs;
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(8f);
        this.paint.setColor(color);
    }

    public boolean isAlive(long nowMs) {
        return nowMs - startMs <= durationMs;
    }

    public void draw(Canvas c, long nowMs) {
        float t = Math.min(1f, (nowMs - startMs) / (float) durationMs);
        float r = 12f + (maxRadiusPx - 12f) * t;
        int alpha = (int) (255 * (1f - t));
        paint.setAlpha(alpha);
        paint.setPathEffect(new DashPathEffect(new float[] { 24f, 14f }, t * 60f));
        c.drawCircle(centerX, centerY, r, paint);
    }
}
