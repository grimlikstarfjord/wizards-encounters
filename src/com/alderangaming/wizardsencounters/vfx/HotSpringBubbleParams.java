package com.alderangaming.wizardsencounters.vfx;

/**
 * Parameters for bubbling hot springs: underwater bubbles that rise and pop,
 * spawning surface ripples.
 */
public class HotSpringBubbleParams {

    // Emission
    public float spawnPerSecond = 2.0f; // per anchor baseline
    public float intensityScale = 1.0f; // multiplies spawn and opacities

    // Bubble sizes and motion
    public float startRadiusDpMin = 1f;
    public float startRadiusDpMax = 4f;
    public float riseSpeedPxPerMsMin = 0.01f;
    public float riseSpeedPxPerMsMax = 0.05f;
    public float horizontalJitterPxPerMs = 0.015f;
    public float lifeMsMin = 700f;
    public float lifeMsMax = 1400f;

    // Visuals
    public int bubbleColor = 0x55FFFFFF; // alpha is used directly for bubbles

    // Ripple rings spawned at pop
    public int rippleCount = 2;
    public float rippleStartRadiusDp = 6f;
    public float rippleEndRadiusDp = 30f;
    public float rippleWidthDp = 2f;
    public float rippleLifeMs = 900f;
    public int rippleColor = 0x10FFFFFF;

    public static HotSpringBubbleParams defaults() {
        return new HotSpringBubbleParams();
    }
}
