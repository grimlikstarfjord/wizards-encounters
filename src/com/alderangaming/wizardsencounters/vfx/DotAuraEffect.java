package com.alderangaming.wizardsencounters.vfx;

import android.graphics.Canvas;
import android.graphics.Paint;

public class DotAuraEffect implements VfxEffect {
    private final long startMs;
    private final long durationMs;
    private final float cx, cy, baseR;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public DotAuraEffect(float cx, float cy, float radius, long durationMs, int color) {
        this.cx = cx;
        this.cy = cy;
        this.baseR = radius;
        this.durationMs = durationMs;
        this.startMs = android.os.SystemClock.uptimeMillis();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);
        paint.setColor(color);
    }

    public boolean isAlive(long nowMs) {
        return nowMs - startMs <= durationMs;
    }

    public void draw(Canvas c, long nowMs) {
        float t = Math.min(1f, (nowMs - startMs) / (float) durationMs);
        float pulse = 1.0f + 0.08f * (float) Math.sin(t * Math.PI * 4);
        int a = (int) (180 * (1f - t));
        paint.setAlpha(a);
        c.drawCircle(cx, cy, baseR * pulse, paint);
    }
}
