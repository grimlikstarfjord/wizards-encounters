package com.alderangaming.wizardsencounters.vfx;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;

import java.util.Random;

public class LeavesEffect implements VfxEffect {

    private static class Leaf {
        float x, y;
        float vx, vy;
        float size;
        float spinHz;
        float phase;
    }

    private final Random rng = new Random();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final LeavesParams params;
    private final float densityScale;
    private Leaf[] leaves;
    private boolean init;
    private long last;

    public LeavesEffect(LeavesParams params, float densityScale) {
        this.params = (params == null) ? LeavesParams.defaults() : params;
        this.densityScale = Math.max(0.75f, densityScale);
        this.last = SystemClock.uptimeMillis();
        paint.setColor(this.params.color);
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

        for (int i = 0; i < leaves.length; i++) {
            Leaf l = leaves[i];
            l.x += l.vx * dt;
            l.y += l.vy * dt;
            float angle = (float) Math.sin((nowMs * 0.001f + l.phase) * (Math.PI * 2) * l.spinHz) * 0.5f;
            float wobbled = l.size * (1.0f + angle * 0.15f);
            c.drawCircle(l.x, l.y, wobbled, paint);
            if (l.y - l.size > h + 10)
                respawn(l, w);
        }
    }

    private void init(Canvas c) {
        init = true;
        int count = Math.max(1, params.count);
        leaves = new Leaf[count];
        int w = c.getWidth();
        for (int i = 0; i < count; i++) {
            Leaf l = new Leaf();
            l.size = rnd(params.sizeDpMin, params.sizeDpMax) * densityScale;
            l.x = rnd(-20, w + 20);
            l.y = rnd(-w, 0);
            l.vx = rnd(params.driftPxPerSecMin, params.driftPxPerSecMax);
            l.vy = rnd(params.fallSpeedPxPerSecMin, params.fallSpeedPxPerSecMax);
            l.spinHz = rnd(params.spinHzMin, params.spinHzMax);
            l.phase = rnd(0f, 1f);
            leaves[i] = l;
        }
    }

    private void respawn(Leaf l, int w) {
        l.size = rnd(params.sizeDpMin, params.sizeDpMax) * densityScale;
        l.x = rnd(-20, w + 20);
        l.y = -10;
        l.vx = rnd(params.driftPxPerSecMin, params.driftPxPerSecMax);
        l.vy = rnd(params.fallSpeedPxPerSecMin, params.fallSpeedPxPerSecMax);
        l.spinHz = rnd(params.spinHzMin, params.spinHzMax);
        l.phase = rnd(0f, 1f);
    }

    private float rnd(float a, float b) {
        return a + rng.nextFloat() * (b - a);
    }
}
