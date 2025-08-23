package com.alderangaming.wizardsencounters.vfx;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class DotPulseEffect implements VfxEffect {
    private final long startMs;
    private final long durationMs;
    private final float cx, cy, radius;
    private final Paint ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint tickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint tintPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public DotPulseEffect(float cx, float cy, float radius, long durationMs) {
        this.cx = cx;
        this.cy = cy;
        this.radius = radius;
        this.durationMs = durationMs;
        this.startMs = android.os.SystemClock.uptimeMillis();
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeWidth(8f);
        ringPaint.setColor(0xFFFF3344);
        tickPaint.setStyle(Paint.Style.STROKE);
        tickPaint.setStrokeWidth(5f);
        tickPaint.setColor(0xFFFF6677);
        tintPaint.setColor(0x22FF0000);
        tintPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
    }

    public boolean isAlive(long nowMs) {
        return nowMs - startMs <= durationMs;
    }

    public void draw(Canvas c, long nowMs) {
        float t = Math.min(1f, (nowMs - startMs) / (float) durationMs);
        // ring expands slightly and fades
        float r = radius * (1.0f + 0.2f * t);
        int a = (int) (255 * (1f - t));
        ringPaint.setAlpha(a);
        tickPaint.setAlpha(a);
        // ring
        c.drawCircle(cx, cy, r, ringPaint);
        // 6 vertical ticks around ring
        for (int i = 0; i < 6; i++) {
            double ang = Math.toRadians(i * 60);
            float sx = (float) (cx + Math.cos(ang) * (r - 10));
            float sy = (float) (cy + Math.sin(ang) * (r - 10));
            float ex = (float) (cx + Math.cos(ang) * (r + 24));
            float ey = (float) (cy + Math.sin(ang) * (r + 24));
            c.drawLine(sx, sy, ex, ey, tickPaint);
        }
        // subtle red screen add
        int save = c.saveLayer(null, null);
        c.drawRect(0, 0, c.getWidth(), c.getHeight(), tintPaint);
        c.restoreToCount(save);
    }
}
