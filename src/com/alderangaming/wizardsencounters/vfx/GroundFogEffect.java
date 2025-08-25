package com.alderangaming.wizardsencounters.vfx;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.SystemClock;

import java.util.Random;

public class GroundFogEffect implements VfxEffect {

    private static class Band {
        float y;
        float thickness;
        float x;
        float speed;
    }

    private final GroundFogParams params;
    private final float densityScale;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Random rng = new Random();
    private Band[] bands;
    private boolean init;
    private long last;

    public GroundFogEffect(GroundFogParams params, float densityScale) {
        this.params = (params == null) ? GroundFogParams.defaults() : params;
        this.densityScale = Math.max(0.75f, densityScale);
        this.last = SystemClock.uptimeMillis();
    }

    @Override
    public boolean isAlive(long nowMs) {
        return true;
    }

    @Override
    public void draw(Canvas c, long nowMs) {
        if (!init)
            init(c);
        float dt = Math.min(50, Math.max(0, nowMs - last)) / 1000f;
        last = nowMs;
        int w = c.getWidth();
        int h = c.getHeight();

        for (int i = 0; i < bands.length; i++) {
            Band b = bands[i];
            b.x += b.speed * dt;
            // wrap
            if (b.x > w + 50)
                b.x = -50;
            if (b.x < -50)
                b.x = w + 50;

            float y = b.y * h;
            float thicknessPx = b.thickness * h;
            float radius = Math.max(1f, thicknessPx * 2.2f);
            float minA = params.opacityMin;
            float maxA = params.opacityMax;
            int aCenter = (int) (255 * maxA);
            int aEdge = (int) (255 * minA);
            int center = (params.color & 0x00FFFFFF) | (aCenter << 24);
            int edge = (params.color & 0x00FFFFFF) | (aEdge << 24);
            paint.setShader(new RadialGradient(b.x, y, radius,
                    new int[] { center, edge, 0x00000000 }, new float[] { 0f, 0.65f, 1f }, Shader.TileMode.CLAMP));
            c.drawCircle(b.x, y, radius, paint);
            paint.setShader(null);
        }
    }

    private void init(Canvas c) {
        init = true;
        int n = Math.max(1, params.layers);
        bands = new Band[n];
        for (int i = 0; i < n; i++) {
            Band b = new Band();
            float y01 = i < params.height01.length ? params.height01[i] : params.height01[params.height01.length - 1];
            float thick01 = i < params.thickness01.length ? params.thickness01[i]
                    : params.thickness01[params.thickness01.length - 1];
            float spd = i < params.speedPxPerSec.length ? params.speedPxPerSec[i]
                    : params.speedPxPerSec[params.speedPxPerSec.length - 1];
            b.y = y01;
            b.thickness = thick01;
            b.x = rng.nextFloat() * c.getWidth();
            b.speed = spd;
            bands[i] = b;
        }
    }
}
