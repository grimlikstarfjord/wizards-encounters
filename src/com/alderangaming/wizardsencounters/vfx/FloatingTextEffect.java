package com.alderangaming.wizardsencounters.vfx;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class FloatingTextEffect implements VfxEffect {
    public enum Kind {
        NORMAL, CRIT, MISS, PLAYER_DAMAGE, HEAL
    }

    private final String text;
    private final float startX;
    private final float startY;
    private final float targetX;
    private final float targetY;
    private final boolean hasTarget;
    private final long startMs;
    private final long durationMs;
    private final Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint stroke = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final float baseSizePx;
    private final Kind kind;
    private final float magnitude; // used to scale size

    // curve/wobble for player damage path
    private final float ctrlX;
    private final float ctrlY;
    private final float normalX;
    private final float normalY;
    private final float noiseAmp;
    private final float noiseFreq;
    private final float noisePhase;
    private final float upNoiseAmp;
    private final float upNoiseFreq;
    private final float upNoisePhase;
    private final float rotStartDeg;

    public FloatingTextEffect(String text, float x, float y, long durationMs, Kind kind) {
        this(text, x, y, durationMs, kind, 0f);
    }

    public FloatingTextEffect(String text, float x, float y, long durationMs, Kind kind, float magnitude) {
        this.text = text;
        this.startX = x;
        this.startY = y;
        this.targetX = 0f;
        this.targetY = 0f;
        this.hasTarget = false;
        this.startMs = android.os.SystemClock.uptimeMillis();
        this.durationMs = durationMs;
        this.kind = kind;
        this.magnitude = Math.max(0f, magnitude);

        int color;
        float size;
        switch (kind) {
            case CRIT:
                color = 0xFFFFD040; // gold
                size = 88f; // doubled
                break;
            case PLAYER_DAMAGE:
                color = 0xFFFF3344; // red
                size = 80f; // doubled
                break;
            case MISS:
                color = 0xFFEEEEEE; // light
                size = 72f; // doubled
                break;
            case HEAL:
                color = 0xFF66DD66;
                size = 76f; // doubled
                break;
            default:
                color = 0xFFFFFFFF;
                size = 76f; // doubled
        }
        baseSizePx = size;
        fill.setColor(color);
        fill.setFakeBoldText(true);

        stroke.setStyle(Paint.Style.STROKE);
        stroke.setStrokeWidth(6f); // doubled outline
        stroke.setColor(0xAA000000);
        stroke.setFakeBoldText(true);

        // defaults for non-target paths
        this.ctrlX = 0f;
        this.ctrlY = 0f;
        this.normalX = 0f;
        this.normalY = 0f;
        this.noiseAmp = 0f;
        this.noiseFreq = 0f;
        this.noisePhase = 0f;
        // upward path subtle variance
        this.upNoiseAmp = 14f + (float) (Math.random() * 10f);
        this.upNoiseFreq = (float) (Math.PI * (1.2 + Math.random() * 1.4));
        this.upNoisePhase = (float) (Math.random() * Math.PI * 2);
        this.rotStartDeg = (float) ((Math.random() - 0.5) * 12.0); // -6..+6 deg
    }

    public FloatingTextEffect(String text, float sx, float sy, float tx, float ty, long durationMs, Kind kind,
            float magnitude) {
        this.text = text;
        this.startX = sx;
        this.startY = sy;
        this.targetX = tx;
        this.targetY = ty;
        this.hasTarget = true;
        this.startMs = android.os.SystemClock.uptimeMillis();
        this.durationMs = durationMs;
        this.kind = kind;
        this.magnitude = Math.max(0f, magnitude);

        int color;
        float size;
        switch (kind) {
            case CRIT:
                color = 0xFFFFD040; // gold
                size = 88f; // doubled
                break;
            case PLAYER_DAMAGE:
                color = 0xFFFF3344; // red
                size = 80f; // doubled
                break;
            case MISS:
                color = 0xFFEEEEEE; // light
                size = 72f; // doubled
                break;
            case HEAL:
                color = 0xFF66DD66;
                size = 76f; // doubled
                break;
            default:
                color = 0xFFFFFFFF;
                size = 76f; // doubled
        }
        baseSizePx = size;
        fill.setColor(color);
        fill.setFakeBoldText(true);
        stroke.setStyle(Paint.Style.STROKE);
        stroke.setStrokeWidth(6f);
        stroke.setColor(0xAA000000);
        stroke.setFakeBoldText(true);

        // compute subtle curved path and noise along perpendicular for player damage
        float dx = tx - sx;
        float dy = ty - sy;
        float len = (float) Math.hypot(dx, dy);
        float nx = 0f, ny = 0f;
        if (len > 1e-3f) {
            nx = -dy / len;
            ny = dx / len;
        }
        float amp = (float) ((Math.random() - 0.5) * 52f); // subtle, random
        this.ctrlX = sx + dx * 0.5f + nx * amp;
        this.ctrlY = sy + dy * 0.5f + ny * amp;
        this.normalX = nx;
        this.normalY = ny;
        this.noiseAmp = 8f;
        this.noiseFreq = (float) (Math.PI * (1.6 + Math.random() * 1.2));
        this.noisePhase = (float) (Math.random() * Math.PI * 2);
        // initialize upward noise/rotation finals even if not used on this path
        this.upNoiseAmp = 14f + (float) (Math.random() * 10f);
        this.upNoiseFreq = (float) (Math.PI * (1.2 + Math.random() * 1.4));
        this.upNoisePhase = (float) (Math.random() * Math.PI * 2);
        this.rotStartDeg = (float) ((Math.random() - 0.5) * 12.0);
    }

    public boolean isAlive(long nowMs) {
        return nowMs - startMs <= durationMs;
    }

    public void draw(Canvas c, long nowMs) {
        float t = Math.min(1f, (nowMs - startMs) / (float) durationMs);
        float ease = (float) (1 - Math.pow(1 - t, 3));

        float x;
        float y;
        if (kind == Kind.PLAYER_DAMAGE && hasTarget) {
            // subtle quadratic bezier from start to target with perpendicular drift
            float u = (float) Math.pow(t, 1.8); // slower start, then accelerate
            float inv = 1f - u;
            x = inv * inv * startX + 2f * inv * u * ctrlX + u * u * targetX;
            y = inv * inv * startY + 2f * inv * u * ctrlY + u * u * targetY;
            float wobble = (float) Math.sin(u * noiseFreq + noisePhase) * noiseAmp * (1f - u);
            x += normalX * wobble;
            y += normalY * wobble;
        } else {
            x = startX;
            // stronger upward travel for player->monster numbers so they rise beyond the
            // monster box
            float travel = (kind == Kind.CRIT ? 600f : (kind == Kind.MISS ? 360f : 520f));
            y = startY - travel * ease;
            // subtle horizontal wobble and slight rotational pop
            float wobble = (float) Math.sin(t * upNoiseFreq + upNoisePhase) * upNoiseAmp * (1f - t);
            x += wobble;
        }

        // size scales with magnitude and eases over time
        float magBoost = (magnitude > 0f) ? Math.min(120f, (float) Math.sqrt(magnitude) * 5.0f) : 0f;
        float startScale = (kind == Kind.PLAYER_DAMAGE ? 0.9f : (kind == Kind.CRIT ? 3.6f : 3.0f));
        float endScale = (kind == Kind.PLAYER_DAMAGE ? 3.2f : 0.6f); // player damage zooms in; others zoom out
        float scale = startScale + (endScale - startScale) * ease;
        float size = (baseSizePx + magBoost + (kind == Kind.CRIT ? 10f : 0f));
        fill.setTextSize(size);
        stroke.setTextSize(size);

        // brief crit shake horizontally at spawn
        float shakeX = 0f;
        if (kind == Kind.CRIT) {
            float tMs = (nowMs - startMs);
            if (tMs < 180f) {
                float k = 1f - (tMs / 180f);
                shakeX = (float) (Math.sin(tMs * 0.08f) * 14f * k); // doubled
            }
        }

        int alpha = (int) (255 * (1f - t));
        fill.setAlpha(alpha);
        stroke.setAlpha(Math.min(220, (int) (alpha * 0.9f)));

        c.save();
        c.translate(x + shakeX, y);
        if (!(kind == Kind.PLAYER_DAMAGE && hasTarget)) {
            // rotation decays as it rises for player->monster numbers
            float rot = rotStartDeg * (1f - ease);
            c.rotate(rot);
        }
        c.scale(scale, scale);

        Rect bounds = new Rect();
        fill.getTextBounds(text, 0, text.length(), bounds);
        float tx = -bounds.width() * 0.5f;
        float ty = -bounds.height();

        c.drawText(text, tx, ty, stroke);
        c.drawText(text, tx, ty, fill);

        c.restore();
    }
}
