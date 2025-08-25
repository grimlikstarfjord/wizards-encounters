package com.alderangaming.wizardsencounters.vfx;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;

public class TorchBloomEffect implements VfxEffect {
    private final float[][] anchors;
    private final TorchBloomParams params;
    private final float densityScale;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public TorchBloomEffect(float[][] anchors01, TorchBloomParams params, float densityScale) {
        this.anchors = anchors01;
        this.params = (params == null) ? TorchBloomParams.defaults() : params;
        this.densityScale = Math.max(0.75f, densityScale);
    }

    @Override
    public boolean isAlive(long nowMs) {
        return true;
    }

    @Override
    public void draw(Canvas c, long nowMs) {
        if (anchors == null)
            return;
        float radius = params.radiusDp * densityScale;
        // Choose a deterministic-but-varied frequency per anchor using its index
        // in range [flickerHzMin..flickerHzMax]
        float freqRange = Math.max(0f, params.flickerHzMax - params.flickerHzMin);
        float time = nowMs * 0.001f;
        for (int i = 0; i < anchors.length; i++) {
            float[] a = anchors[i];
            float x = a[0] * c.getWidth();
            float y = a[1] * c.getHeight();
            // Pseudo-random base per index with a slow time drift so frequency changes over
            // time
            float base = (i * 0.37f) % 1f;
            float drift = 0.5f + 0.5f * (float) Math.sin(time * 0.15f + i * 1.3f);
            float mix = base * 0.6f + drift * 0.4f;
            if (mix < 0f)
                mix = 0f;
            if (mix > 1f)
                mix = 1f;
            float freq = params.flickerHzMin + freqRange * mix;
            float flick = 1f + (float) Math.sin(time * (float) Math.PI * 2f * freq) * params.flickerAmplitude;
            int aCore = (int) (255 * params.intensity * flick);
            int color = (params.color & 0x00FFFFFF) | (aCore << 24);
            paint.setShader(new RadialGradient(x, y, radius,
                    new int[] { color, 0x00000000 }, new float[] { 0f, 1f }, Shader.TileMode.CLAMP));
            c.drawCircle(x, y, radius, paint);
            paint.setShader(null);
        }
    }
}
