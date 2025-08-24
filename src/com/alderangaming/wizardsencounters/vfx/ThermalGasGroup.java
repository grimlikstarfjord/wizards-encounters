package com.alderangaming.wizardsencounters.vfx;

/**
 * Anchor set + params for thermal gas emitters.
 * Anchors are normalized positions (0..1) where gas bubbles emerge.
 */
public final class ThermalGasGroup {
    public final float[][] anchors01;
    public final ThermalGasParams params;

    public ThermalGasGroup(float[][] anchors01, ThermalGasParams params) {
        this.anchors01 = anchors01;
        this.params = (params == null) ? ThermalGasParams.defaults() : params;
    }
}
