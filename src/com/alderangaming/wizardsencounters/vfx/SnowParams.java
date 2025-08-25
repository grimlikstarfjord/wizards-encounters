package com.alderangaming.wizardsencounters.vfx;

public class SnowParams {
    public int flakeCount = 120;
    public float fallSpeedPxPerSecMin = 40f;
    public float fallSpeedPxPerSecMax = 120f;
    public float swayPxPerSec = 25f;
    public float sizeDpMin = 1.2f;
    public float sizeDpMax = 3.5f;
    public int color = 0xFFFFFFFF;

    public static SnowParams defaults() {
        return new SnowParams();
    }
}
