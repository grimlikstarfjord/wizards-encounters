package com.alderangaming.wizardsencounters.vfx;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;

public class RuneCircleEffect implements VfxEffect {
    private final float cx, cy, radius;
    private final long startMs;
    private final long durationMs;
    private final Paint paint;
    private final Path path = new Path();

    public RuneCircleEffect(float cx, float cy, float radius, long durationMs, int color) {
        this.cx = cx;
        this.cy = cy;
        this.radius = radius;
        this.startMs = android.os.SystemClock.uptimeMillis();
        this.durationMs = durationMs;
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(5f);
        this.paint.setColor(color);
    }

    public boolean isAlive(long nowMs) {
        return nowMs - startMs <= durationMs;
    }

    public void draw(Canvas c, long nowMs) {
        float t = Math.min(1f, (nowMs - startMs) / (float) durationMs);
        int a = (int) (255 * (1f - t));
        paint.setAlpha(a);
        paint.setPathEffect(new DashPathEffect(new float[] { 14f, 8f, 6f, 8f }, t * 120f));
        path.reset();
        path.addCircle(cx, cy, radius, Path.Direction.CW);
        c.drawPath(path, paint);
    }
}
