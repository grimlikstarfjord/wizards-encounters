package com.alderangaming.wizardsencounters.vfx;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Utility to build a layered monster view from a MonsterRig and provide simple
 * per-part animations.
 * Does not alter existing single-image flows; callers can choose to use this
 * when a rig exists.
 */
public final class MonsterRigRenderer {

    public static final class BuiltRigView {
        public final FrameLayout container;
        // Image nodes for direct bitmap access (legacy)
        public final Map<MonsterRig.PartType, ImageView> partByType;
        public final Map<String, ImageView> partByName;
        // Hierarchical containers to apply transforms that propagate to children
        public final Map<MonsterRig.PartType, FrameLayout> nodeByType;
        public final Map<String, FrameLayout> nodeByName;

        BuiltRigView(FrameLayout container,
                Map<MonsterRig.PartType, ImageView> partByType,
                Map<String, ImageView> partByName,
                Map<MonsterRig.PartType, FrameLayout> nodeByType,
                Map<String, FrameLayout> nodeByName) {
            this.container = container;
            this.partByType = partByType;
            this.partByName = partByName;
            this.nodeByType = nodeByType;
            this.nodeByName = nodeByName;
        }
    }

    private MonsterRigRenderer() {
    }

    /**
     * Builds a FrameLayout containing ImageViews for each rig part. Parts are
     * z-ordered ascending.
     * The returned container can be positioned/scaled by the existing
     * MonsterRenderParams logic.
     */
    public static BuiltRigView build(Context ctx, MonsterRig rig) {
        if (rig == null)
            return null;
        FrameLayout container = new FrameLayout(ctx);
        // Allow parts to extend beyond the container without being clipped
        container.setClipChildren(false);
        container.setClipToPadding(false);

        // Prepare maps for quick access
        Map<MonsterRig.PartType, ImageView> byType = new HashMap<MonsterRig.PartType, ImageView>();
        Map<String, ImageView> byName = new HashMap<String, ImageView>();
        Map<MonsterRig.PartType, FrameLayout> nodeByType = new HashMap<MonsterRig.PartType, FrameLayout>();
        Map<String, FrameLayout> nodeByName = new HashMap<String, FrameLayout>();
        // Track pivots for animation helpers
        ensurePivotStore();

        List<MonsterRig.Part> parts = new ArrayList<MonsterRig.Part>(rig.getParts());
        Collections.sort(parts, new Comparator<MonsterRig.Part>() {
            @Override
            public int compare(MonsterRig.Part a, MonsterRig.Part b) {
                if (a.zOrder == b.zOrder)
                    return 0;
                return a.zOrder < b.zOrder ? -1 : 1;
            }
        });

        // First pass: create views for all parts
        Map<String, ImageView> tempByName = new HashMap<String, ImageView>();
        Map<String, FrameLayout> tempNodeByName = new HashMap<String, FrameLayout>();
        for (MonsterRig.Part part : parts) {
            int resId = resolveDrawable(ctx, part.drawableName);
            if (resId == 0)
                continue; // silently skip missing assets; keeps app stable until art is added

            // Create a node container so children inherit transforms
            FrameLayout node = new FrameLayout(ctx);
            node.setClipChildren(false);
            node.setClipToPadding(false);
            // Apply per-part scale to the node so children (e.g., hands) inherit
            node.setScaleX(part.scaleX);
            node.setScaleY(part.scaleY);

            // Create the actual image view for this part
            ImageView iv = new ImageView(ctx);
            iv.setImageResource(resId);
            iv.setScaleType(ImageView.ScaleType.CENTER);
            FrameLayout.LayoutParams ivlp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            iv.setLayoutParams(ivlp);
            node.addView(iv);

            // Create params for the node; position applied later (after anchoring resolved)
            FrameLayout.LayoutParams nlp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            node.setLayoutParams(nlp);

            if (part.type != null)
                byType.put(part.type, iv);
            if (part.name != null)
                byName.put(part.name, iv);
            if (part.type != null)
                nodeByType.put(part.type, node);
            if (part.name != null) {
                nodeByName.put(part.name, node);
                tempByName.put(part.name, iv);
                tempNodeByName.put(part.name, node);
            }
            // Store pivot in pivot store
            PIVOT_BY_VIEW.put(node, new float[] { part.pivotXPct, part.pivotYPct });
        }

        // Second pass: apply anchored positioning (offsets are in dp relative to
        // anchor's top-left)
        for (MonsterRig.Part part : parts) {
            FrameLayout node = part.name != null ? tempNodeByName.get(part.name) : null;
            if (node == null)
                continue;
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) node.getLayoutParams();
            int left = dpToPx(ctx.getResources(), part.offsetXDp);
            int top = dpToPx(ctx.getResources(), part.offsetYDp);
            if (part.anchorToName != null && part.anchorToName.length() > 0) {
                FrameLayout anchorNode = tempNodeByName.get(part.anchorToName);
                if (anchorNode != null) {
                    // Place as child of the anchor node so transforms propagate
                    ((ViewGroup) anchorNode).addView(node);
                } else {
                    container.addView(node);
                }
            } else {
                container.addView(node);
            }
            lp.leftMargin = left;
            lp.topMargin = top;
            node.setLayoutParams(lp);
        }

        return new BuiltRigView(container, byType, byName, nodeByType, nodeByName);
    }

    /** Rotate a specific node/view using its configured pivot. */
    public static Animation rotateNode(View targetView, float fromDeg, float toDeg, long durationMs) {
        if (targetView == null)
            return null;
        float px = getPivot(targetView, true);
        float py = getPivot(targetView, false);
        RotateAnimation r = new RotateAnimation(fromDeg, toDeg,
                Animation.RELATIVE_TO_SELF, px,
                Animation.RELATIVE_TO_SELF, py);
        r.setDuration(durationMs);
        r.setFillAfter(true);
        targetView.startAnimation(r);
        return r;
    }

    /** Backward-compat helper: rotate an ImageView by delegating to rotateNode. */
    public static Animation rotatePart(ImageView partView, float fromDeg, float toDeg, long durationMs) {
        return rotateNode(partView, fromDeg, toDeg, durationMs);
    }

    /**
     * Translate a specific part by dx, dy (in pixels) relative to its current
     * position.
     */
    public static Animation translatePart(ImageView partView, float dxPx, float dyPx, long durationMs) {
        if (partView == null)
            return null;
        TranslateAnimation t = new TranslateAnimation(0, dxPx, 0, dyPx);
        t.setDuration(durationMs);
        t.setFillAfter(true);
        partView.startAnimation(t);
        return t;
    }

    private static int resolveDrawable(Context ctx, String drawableName) {
        if (drawableName == null || drawableName.length() == 0)
            return 0;
        try {
            return ctx.getResources().getIdentifier(drawableName, "drawable", ctx.getPackageName());
        } catch (Throwable t) {
            return 0;
        }
    }

    private static int dpToPx(Resources r, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    private static WeakHashMap<View, float[]> PIVOT_BY_VIEW;

    private static void ensurePivotStore() {
        if (PIVOT_BY_VIEW == null) {
            PIVOT_BY_VIEW = new WeakHashMap<View, float[]>();
        }
    }

    private static float getPivot(View v, boolean xAxis) {
        ensurePivotStore();
        float[] piv = PIVOT_BY_VIEW.get(v);
        if (piv == null)
            return 0.5f;
        return xAxis ? piv[0] : piv[1];
    }

    /** Returns a defensive copy of the stored pivot percentages [x,y] in [0..1]. */
    public static float[] getPivot01(ImageView v) {
        ensurePivotStore();
        float[] piv = PIVOT_BY_VIEW.get(v);
        if (piv == null)
            return new float[] { 0.5f, 0.5f };
        return new float[] { piv[0], piv[1] };
    }

    /**
     * Plays a quick forward charge on the rig container with subtle limb motion.
     */
    public static void playChargeAnimation(BuiltRigView rig, long durationMs) {
        if (rig == null || rig.container == null)
            return;
        // Container: strong scale-up in place with auto return (no forward translate)
        AnimationSet set = new AnimationSet(true);
        float scaleTo = 1.65f;
        ScaleAnimation s1 = new ScaleAnimation(1.0f, scaleTo, 1.0f, scaleTo,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.85f);
        s1.setDuration(durationMs);
        set.addAnimation(s1);

        set.setRepeatMode(Animation.REVERSE);
        set.setRepeatCount(1);
        rig.container.startAnimation(set);

        // Arms: pronounced swing
        FrameLayout armL = rig.nodeByType.get(MonsterRig.PartType.ARM_LEFT);
        FrameLayout armR = rig.nodeByType.get(MonsterRig.PartType.ARM_RIGHT);
        if (armL != null) {
            Animation rl = rotateNode(armL, -14f, 26f, durationMs);
            if (rl != null) {
                rl.setRepeatMode(Animation.REVERSE);
                rl.setRepeatCount(1);
            }
        }
        if (armR != null) {
            Animation rr = rotateNode(armR, 12f, -24f, durationMs);
            if (rr != null) {
                rr.setRepeatMode(Animation.REVERSE);
                rr.setRepeatCount(1);
            }
        }

        // Head: sharper nod
        FrameLayout head = rig.nodeByType.get(MonsterRig.PartType.HEAD);
        if (head != null) {
            Animation rh = rotateNode(head, -6f, 10f, durationMs);
            if (rh != null) {
                rh.setRepeatMode(Animation.REVERSE);
                rh.setRepeatCount(1);
            }
        }
    }
}
