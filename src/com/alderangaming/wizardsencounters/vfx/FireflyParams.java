package com.alderangaming.wizardsencounters.vfx;

/**
 * Parameters for FireflyEffect: glowing motes clustering around anchors.
 */
public class FireflyParams {
    public int countPerAnchor = 10;
    public float radiusDpMin = 1.8f;
    public float radiusDpMax = 3.2f;
    public float areaRadiusDp = 60f; // wander radius from anchor
    public float wanderSpeedPxPerSec = 22f;
    public float blinkHzMin = 0.4f;
    public float blinkHzMax = 1.2f;
    public int color = 0xFFFFF2A8; // warm glow

    public static FireflyParams defaults() {
        return new FireflyParams();
    }
}
