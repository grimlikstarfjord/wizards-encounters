package com.alderangaming.wizardsencounters.vfx;

/**
 * Parameters for ThermalGasEffect (sulphur swamp bubbling gas).
 */
public class ThermalGasParams {

    // Emission
    public float intensityScale = 1.0f; // global multiplier for spawn/opacity/size
    public float spawnPerSecond = 1.6f; // per anchor baseline

    // Particle life and size
    public float lifeMsMin = 1000f;
    public float lifeMsMax = 3000f;
    public float startSizeDpMin = 10f;
    public float startSizeDpMax = 22f;
    public float endSizeMultiplierMin = 1.8f;
    public float endSizeMultiplierMax = 4f;

    // Motion
    public float riseSpeedPxPerMs = 0.12f; // upward speed baseline
    public float horizontalJitterPxPerMs = 0.02f; // random sway speed
    public float wobbleAmplitudeDp = 6f; // sinusoidal side-to-side amplitude
    public float wobbleHzMin = 0.2f;
    public float wobbleHzMax = 0.5f;

    // Visuals
    public float minAlpha = 0.18f;
    public float maxAlpha = 0.55f;
    public int startColor = 0xCCB7FF61; // green-yellow core
    public int endColor = 0x5599CC44; // translucent outer

    public static ThermalGasParams defaults() {
        return new ThermalGasParams();
    }
}
