package com.alderangaming.wizardsencounters.vfx;

/**
 * Parameters to tune FogEffect appearance per background.
 */
public class FogParams {

    // Approximate number of fog puffs on screen
    public int puffCount = 14;

    // Overall intensity multiplier. Scales puffCount and opacity range.
    public float intensityScale = 1.0f;

    // Overall opacity (0..1); each puff will vary within [minAlpha..maxAlpha]
    public float minAlpha = 0.10f;
    public float maxAlpha = 0.35f;

    // Speed and direction (pixels/ms scaled by densityScale in effect)
    public float baseSpeed = 0.012f; // horizontal drift speed baseline
    public float directionX = 1.0f; // 1 = left->right, -1 = right->left

    // Slow wind oscillation that modulates horizontal speed
    public float windOscillationPeriodMs = 8000f; // ms per cycle
    public float windOscillationAmplitude = 0.25f; // 0..1 fraction of base speed

    // Size scaling for puffs (multiplies the base pixel radius)
    public float sizeScale = 1.0f;

    // Vertical band where fog spawns/moves (0..1 of height)
    public float heightStart01 = 0.60f;
    public float heightEnd01 = 1.00f;

    // Gentle vertical jitter amplitude as a fraction of height
    public float verticalJitter01 = 0.02f;

    // Fog tint (ARGB). Alpha is ignored and derived from min/max.
    public int color = 0xFFFFFFFF;

    public static FogParams defaults() {
        return new FogParams();
    }
}
