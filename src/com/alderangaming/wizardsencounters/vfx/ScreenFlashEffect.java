package com.alderangaming.wizardsencounters.vfx;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.SystemClock;

public class ScreenFlashEffect implements VfxEffect {
    private final long startMs;
    private final long durationMs;
    private final Paint addPaint;
    private final int color;

    public ScreenFlashEffect(int color, long durationMs) {
        this.startMs = SystemClock.uptimeMillis();
        this.durationMs = durationMs;
        this.addPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.color = color;
        this.addPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
    }

    public boolean isAlive(long nowMs) {
        return nowMs - startMs <= durationMs;
    }

    public void draw(Canvas c, long nowMs) {
        float t = Math.min(1f, (nowMs - startMs) / (float) durationMs);
        int a = (int) (255 * (1f - t));
        int base = color & 0x00FFFFFF;
        int center = (a << 24) | base;
        int edge = 0x00000000;
        float cx = c.getWidth() * 0.5f;
        float cy = c.getHeight() * 0.5f;
        float r = (float) Math.hypot(cx, cy);
        addPaint.setShader(new RadialGradient(cx, cy, r,
                new int[] { center, edge }, new float[] { 0.0f, 1.0f }, Shader.TileMode.CLAMP));
        c.drawRect(0, 0, c.getWidth(), c.getHeight(), addPaint);
        addPaint.setShader(null);
    }
}
