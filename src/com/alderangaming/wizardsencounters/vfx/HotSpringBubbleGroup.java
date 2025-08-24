package com.alderangaming.wizardsencounters.vfx;

/**
 * Anchor set + params for bubbling hot springs emitters.
 */
public final class HotSpringBubbleGroup {
    public final float[][] anchors01;
    public final HotSpringBubbleParams params;

    public HotSpringBubbleGroup(float[][] anchors01, HotSpringBubbleParams params) {
        this.anchors01 = anchors01;
        this.params = (params == null) ? HotSpringBubbleParams.defaults() : params;
    }
}
