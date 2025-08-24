package com.alderangaming.wizardsencounters.vfx;

import android.content.Context;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.List;

import com.alderangaming.wizardsencounters.R;

/**
 * Maps background drawable resources to normalized anchor arrays used by
 * TorchFireEffect or other background-bound effects.
 */
public final class BackgroundVfxRegistry {

    private static final SparseArray<float[][]> TORCH_ANCHORS = new SparseArray<float[][]>();
    private static final SparseArray<TorchFireParams> TORCH_PARAMS = new SparseArray<TorchFireParams>();
    private static final SparseArray<List<TorchFireGroup>> TORCH_GROUPS = new SparseArray<List<TorchFireGroup>>();
    private static boolean initialized;

    private BackgroundVfxRegistry() {
    }

    public static float[][] getTorchAnchors(Context ctx, int backgroundResId) {
        ensureInit();
        float[][] anchors = TORCH_ANCHORS.get(backgroundResId);
        return anchors;
    }

    public static TorchFireParams getTorchParams(Context ctx, int backgroundResId) {
        ensureInit();
        TorchFireParams p = TORCH_PARAMS.get(backgroundResId);
        return p != null ? p : TorchFireParams.defaults();
    }

    /**
     * Optional: per-background list of groups. When non-empty, use these groups
     * instead of the single anchors/params pair.
     */
    public static List<TorchFireGroup> getTorchGroups(Context ctx, int backgroundResId) {
        ensureInit();
        List<TorchFireGroup> g = TORCH_GROUPS.get(backgroundResId);
        return g;
    }

    private static void ensureInit() {
        if (initialized)
            return;
        initialized = true;

        // Example mappings. Adjust per background art. Coordinates are (x01,y01)
        // for torch flame centers relative to the full view.
        TORCH_ANCHORS.put(R.drawable.septictombbackground, new float[][] {
                { 0.08f, 0.18f }, { 0.90f, 0.18f }
        });
        TorchFireParams tomb = TorchFireParams.defaults();
        tomb.intensityScale = 0.2f;
        tomb.sizeScale = 0.5f;
        tomb.speedScale = 0.4f;
        tomb.startColor = 0xFFFFB060;
        tomb.endColor = 0xFF993311;
        TORCH_PARAMS.put(R.drawable.septictombbackground, tomb);

        // Example: two different flames in the same background (left cool blue, right
        // warm)
        List<TorchFireGroup> treasureGroups = new ArrayList<TorchFireGroup>();
        TorchFireParams treasureLeft = TorchFireParams.defaults();
        treasureLeft.intensityScale = 1.0f;
        treasureLeft.sizeScale = 1.1f;
        treasureLeft.startColor = 0xFF80D8FF; // cool blue
        treasureLeft.endColor = 0xFF2060A0;
        treasureGroups.add(new TorchFireGroup(new float[][] { { 0.75f, 0.40f } }, treasureLeft));

        TorchFireParams treasureRight = TorchFireParams.defaults();
        treasureRight.intensityScale = 1.3f;
        treasureRight.sizeScale = 1.4f;
        treasureRight.speedScale = 0.9f;
        treasureRight.startColor = 0xFFFFE070; // warm gold
        treasureRight.endColor = 0xFFAA5511;
        treasureGroups.add(new TorchFireGroup(new float[][] { { 0.85f, 0.40f } }, treasureRight));

        TORCH_GROUPS.put(R.drawable.backgroundtreasure, treasureGroups);

        TORCH_ANCHORS.put(R.drawable.camp, new float[][] {
                { 0.30f, 0.70f }
        });
        TorchFireParams camp = TorchFireParams.defaults();
        camp.intensityScale = 1.4f;
        camp.sizeScale = 1.6f;
        camp.speedScale = 1.0f;
        camp.jitterScale = 1.3f;
        camp.startColor = 0xFFFFE890;
        camp.endColor = 0xFFCC4411;
        TORCH_PARAMS.put(R.drawable.camp, camp);

        // The new inn or tomb images can be added similarly when used.
    }
}
