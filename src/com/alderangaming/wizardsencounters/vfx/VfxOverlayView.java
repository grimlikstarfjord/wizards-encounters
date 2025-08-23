package com.alderangaming.wizardsencounters.vfx;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VfxOverlayView extends View {
    private final List<VfxEffect> effects = new ArrayList<VfxEffect>(16);
    private AmbientMotesEffect ambientRef;

    public VfxOverlayView(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public VfxOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public synchronized void addEffect(VfxEffect effect) {
        effects.add(effect);
        invalidateSelf();
    }

    public synchronized void setAmbient(AmbientMotesEffect ambient) {
        this.ambientRef = ambient;
        effects.add(ambient);
        invalidateSelf();
    }

    public void pushAmbient(float x, float y, float strength, float radius) {
        AmbientMotesEffect a;
        synchronized (this) {
            a = ambientRef;
        }
        if (a != null)
            a.push(x, y, strength, radius);
        invalidateSelf();
    }

    private void invalidateSelf() {
        if (Build.VERSION.SDK_INT >= 16) {
            postInvalidateOnAnimation();
        } else {
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        long now = SystemClock.uptimeMillis();
        boolean anyAlive = false;

        synchronized (this) {
            Iterator<VfxEffect> it = effects.iterator();
            while (it.hasNext()) {
                VfxEffect e = it.next();
                if (e.isAlive(now)) {
                    anyAlive = true;
                    int save = canvas.save();
                    e.draw(canvas, now);
                    canvas.restoreToCount(save);
                } else {
                    it.remove();
                }
            }
        }

        if (anyAlive) {
            invalidateSelf();
        }
    }
}
