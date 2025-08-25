package com.alderangaming.wizardsencounters.vfx;

public class FlyingFlockParams {
    public int count = 10;
    public float altitude01 = 0.25f; // 0..1 of height
    public float spreadPx = 80f; // lateral spread
    public float speedPxPerSec = 120f;
    public float swayPxPerSec = 20f;
    public int color = 0xAA000000; // silhouettes

    public static FlyingFlockParams defaults() {
        return new FlyingFlockParams();
    }
}
