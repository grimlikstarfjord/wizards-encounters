package com.alderangaming.wizardsencounters.vfx;

/**
 * Parameters to tune TorchFireEffect appearance per background.
 * All scales default to 1.0f; colors default to warm yellow → deep red.
 */
public class TorchFireParams {

    public float intensityScale = 1.0f; // overall spawn multiplier
    public float sizeScale = 1.0f; // multiplies start/end sizes
    public float speedScale = 1.0f; // multiplies velocities
    public float jitterScale = 1.0f; // multiplies base jitter
    public float lifeScale = 1.0f; // multiplies particle lifetime
    public int startColor = 0xFFFFD070; // ARGB core color
    public int endColor = 0xFFAA3311; // ARGB edge color

    public static TorchFireParams defaults() {
        return new TorchFireParams();
    }
}
