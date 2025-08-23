package com.alderangaming.wizardsencounters.vfx;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;

public class SplineProjectileEffect implements VfxEffect {
    private final float sx, sy, cx, cy, ex, ey;
    private final long startMs;
    private final long travelMs;
    private final Paint paint;
    private final Path path = new Path();
    private final PathMeasure measure;
    private final float length;

    public SplineProjectileEffect(float sx, float sy, float cx, float cy, float ex, float ey, long travelMs,
            int color) {
        this.sx = sx;
        this.sy = sy;
        this.cx = cx;
        this.cy = cy;
        this.ex = ex;
        this.ey = ey;
        this.startMs = android.os.SystemClock.uptimeMillis();
        this.travelMs = travelMs;
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.paint.setColor(color);
        this.paint.setStrokeWidth(6f);
        this.paint.setStyle(Paint.Style.STROKE);
        path.moveTo(sx, sy);
        path.quadTo(cx, cy, ex, ey);
        measure = new PathMeasure(path, false);
        length = measure.getLength();
    }

    public boolean isAlive(long nowMs) {
        return nowMs - startMs <= travelMs;
    }

    public void draw(Canvas c, long nowMs) {
        float t = Math.min(1f, (nowMs - startMs) / (float) travelMs);
        float distance = t * length;
        float[] pos = new float[2];
        float[] tan = new float[2];
        measure.getPosTan(distance, pos, tan);

        // trail: draw diminishing circles behind current pos
        for (int i = 0; i < 8; i++) {
            float trailT = Math.max(0f, t - i * 0.045f);
            float d = trailT * length;
            float[] p2 = new float[2];
            measure.getPosTan(d, p2, null);
            int a = (int) (255 * (1f - i / 8f));
            paint.setAlpha(a);
            c.drawCircle(p2[0], p2[1], Math.max(2f, 6f - i * 0.6f), paint);
        }
    }
}
