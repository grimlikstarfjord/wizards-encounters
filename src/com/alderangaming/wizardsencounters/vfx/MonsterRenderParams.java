package com.alderangaming.wizardsencounters.vfx;

/**
 * Per-monster render tuning: scale and optional offsets.
 */
public class MonsterRenderParams {

    public float scaleX = 1.0f;
    public float scaleY = 1.0f;
    public float offsetXDp = 0f;
    public float offsetYDp = 0f;

    public static MonsterRenderParams defaults() {
        return new MonsterRenderParams();
    }
}
