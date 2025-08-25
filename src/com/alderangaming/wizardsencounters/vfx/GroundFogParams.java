package com.alderangaming.wizardsencounters.vfx;

public class GroundFogParams {
    public int layers = 3;
    public float[] height01 = new float[] { 0.80f, 0.88f, 0.94f }; // center heights
    public float[] thickness01 = new float[] { 0.06f, 0.05f, 0.04f }; // band thickness
    public float[] speedPxPerSec = new float[] { 12f, 8f, 5f }; // horizontal drift
    public float opacityMin = 0.05f;
    public float opacityMax = 0.18f;
    public int color = 0xFFFFFFFF;

    public static GroundFogParams defaults() {
        return new GroundFogParams();
    }
}
