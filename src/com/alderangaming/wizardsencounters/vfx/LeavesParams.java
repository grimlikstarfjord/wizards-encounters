package com.alderangaming.wizardsencounters.vfx;

public class LeavesParams {
    public int count = 40;
    public float fallSpeedPxPerSecMin = 40f;
    public float fallSpeedPxPerSecMax = 90f;
    public float driftPxPerSecMin = -20f;
    public float driftPxPerSecMax = 20f;
    public float spinHzMin = 0.2f;
    public float spinHzMax = 0.8f;
    public int color = 0xFFCC8A3A; // autumn leaf tint
    public float sizeDpMin = 3f;
    public float sizeDpMax = 6f;

    public static LeavesParams defaults() {
        return new LeavesParams();
    }
}
