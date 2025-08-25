package com.alderangaming.wizardsencounters.vfx;

public class TorchBloomParams {
    public float intensity = .1f; // overall alpha
    public float radiusDp = 150f; // bloom radius around anchor
    // Randomize flicker per bloom between [min..max] for natural variation
    public float flickerHzMin = 0.4f;
    public float flickerHzMax = 1.6f;
    public float flickerAmplitude = 1.0f; // 0..1 fraction of intensity
    public int color = 0xFFFFD780;

    public static TorchBloomParams defaults() {
        return new TorchBloomParams();
    }
}
