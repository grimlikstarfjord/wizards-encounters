package com.alderangaming.wizardsencounters.vfx;

import android.graphics.Canvas;

public interface VfxEffect {
    boolean isAlive(long nowMs);

    void draw(Canvas canvas, long nowMs);
}
