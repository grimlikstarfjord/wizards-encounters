package com.alderangaming.wizardsencounters.vfx;

/**
 * A set of one or more torch anchors that share the same parameters.
 */
public final class TorchFireGroup {

    public final float[][] anchors01;
    public final TorchFireParams params;

    public TorchFireGroup(float[][] anchors01, TorchFireParams params) {
        this.anchors01 = anchors01;
        this.params = (params == null) ? TorchFireParams.defaults() : params;
    }
}
