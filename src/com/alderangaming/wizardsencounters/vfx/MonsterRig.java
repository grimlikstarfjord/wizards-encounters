package com.alderangaming.wizardsencounters.vfx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Describes a layered cutout rig for a monster: body, head, left/right arms,
 * etc.
 * Parts are defined by drawable resource names (resolved at runtime) and
 * include
 * offsets, scales, z-order, and animation pivots (percentages of the part's
 * size).
 *
 * Using drawable names (Strings) allows shipping code before art is present.
 */
public final class MonsterRig {

    public enum PartType {
        BODY, // legacy anchor, prefer TORSO
        TORSO,
        HEAD,
        ARM_LEFT,
        ARM_RIGHT,
        HAND_LEFT,
        HAND_RIGHT,
        SHOE_LEFT,
        SHOE_RIGHT,
        WEAPON,
        CUSTOM
    }

    public static final class Part {
        public PartType type = PartType.CUSTOM;
        /** Drawable resource name, e.g., "hideousaunt_head". */
        public String drawableName;
        /**
         * Optional: anchor this part's offsets to another part's top-left (by name).
         */
        public String anchorToName;
        /** Layout offset from the body anchor, in dp. */
        public float offsetXDp;
        public float offsetYDp;
        /** Optional per-part scale. Defaults to 1.0. */
        public float scaleX = 1.0f;
        public float scaleY = 1.0f;
        /** Pivot for rotations/transforms, as RELATIVE_TO_SELF percentages [0..1]. */
        public float pivotXPct = 0.5f;
        public float pivotYPct = 0.5f;
        /** Higher zOrder draws above lower values. */
        public int zOrder;
        /** Optional human-readable name for debugging. */
        public String name;
    }

    private final List<Part> parts = new ArrayList<Part>();

    public void addPart(Part p) {
        if (p == null)
            return;
        parts.add(p);
    }

    public List<Part> getParts() {
        return Collections.unmodifiableList(parts);
    }
}
