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
    private static final SparseArray<FogParams> FOG_PARAMS = new SparseArray<FogParams>();
    private static final SparseArray<List<ThermalGasGroup>> GAS_GROUPS = new SparseArray<List<ThermalGasGroup>>();
    private static final SparseArray<List<HotSpringBubbleGroup>> BUBBLE_GROUPS = new SparseArray<List<HotSpringBubbleGroup>>();
    private static final SparseArray<Float> RAIN_PROBABILITY = new SparseArray<Float>();
    private static final SparseArray<Boolean> MOTES_ENABLED = new SparseArray<Boolean>();
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

    public static FogParams getFogParams(Context ctx, int backgroundResId) {
        ensureInit();
        FogParams p = FOG_PARAMS.get(backgroundResId);
        return p; // null when no fog for this background
    }

    public static List<ThermalGasGroup> getThermalGasGroups(Context ctx, int backgroundResId) {
        ensureInit();
        return GAS_GROUPS.get(backgroundResId);
    }

    public static List<HotSpringBubbleGroup> getHotSpringBubbleGroups(Context ctx, int backgroundResId) {
        ensureInit();
        return BUBBLE_GROUPS.get(backgroundResId);
    }

    /**
     * Returns probability [0..1] that rain should be shown for this background.
     * 0 or null -> no rain. 1 -> always rain. Values in between -> random.
     */
    public static float getRainProbability(Context ctx, int backgroundResId) {
        ensureInit();
        Float p = RAIN_PROBABILITY.get(backgroundResId);
        return p == null ? 0f : Math.max(0f, Math.min(1f, p));
    }

    /**
     * Returns whether ambient motes are allowed on this background (default true).
     */
    public static boolean isMotesEnabled(Context ctx, int backgroundResId) {
        ensureInit();
        Boolean b = MOTES_ENABLED.get(backgroundResId);
        return b == null ? true : b.booleanValue();
    }

    private static void ensureInit() {
        if (initialized)
            return;
        initialized = true;

        // Example mappings. Adjust per background art. Coordinates are (x01,y01)
        // for torch flame centers relative to the full view.
        // Septic tomb: two identical torch flames → combine anchors into one group
        List<TorchFireGroup> tombFlames = new ArrayList<TorchFireGroup>();
        TorchFireParams tombTorches = TorchFireParams.defaults();
        tombTorches.intensityScale = 0.2f;
        tombTorches.sizeScale = 0.8f;
        tombTorches.speedScale = 0.4f;
        tombTorches.startColor = 0xFFFFB060;
        tombTorches.endColor = 0xFF993311;
        tombFlames.add(new TorchFireGroup(new float[][] { { 0.08f, 0.18f }, { 0.90f, 0.18f } }, tombTorches));

        TorchFireParams tombHutFlames = TorchFireParams.defaults();
        tombHutFlames.intensityScale = 0.01f;
        tombHutFlames.sizeScale = 0.3f;
        tombHutFlames.speedScale = 0.1f;
        tombHutFlames.startColor = 0xAA6E2E11;
        tombHutFlames.endColor = 0xAA993311;
        tombFlames.add(new TorchFireGroup(
                new float[][] { { 0.478f, 0.642f }, { 0.52f, 0.49f }, { 0.63f, 0.48f }, { 0.635f, 0.605f } },
                tombHutFlames));

        TORCH_GROUPS.put(R.drawable.septictombbackground, tombFlames);

        List<TorchFireGroup> blacktidepoolFlames = new ArrayList<TorchFireGroup>();
        TorchFireParams blacktidepoolTorch = TorchFireParams.defaults();
        blacktidepoolTorch.intensityScale = .4f;
        blacktidepoolTorch.sizeScale = .2f;
        blacktidepoolTorch.startColor = 0xAA6E2E11;
        blacktidepoolTorch.endColor = 0xAA993311;
        blacktidepoolTorch.speedScale = 0.2f;
        blacktidepoolFlames.add(new TorchFireGroup(new float[][] { { 0.39f, 0.3f }, { 0.5f, 0.32f }, { 0.61f, 0.29f } },
                blacktidepoolTorch));

        TORCH_GROUPS.put(R.drawable.blacktidepoolbackground, blacktidepoolFlames);

        TORCH_ANCHORS.put(R.drawable.camp, new float[][] {
                { 0.30f, 0.70f }
        });
        TorchFireParams campTorch = TorchFireParams.defaults();
        campTorch.intensityScale = 1.4f;
        campTorch.sizeScale = 1.6f;
        campTorch.speedScale = 1.0f;
        campTorch.jitterScale = 1.3f;
        campTorch.startColor = 0xFFFFE890;
        campTorch.endColor = 0xFFCC4411;
        TORCH_PARAMS.put(R.drawable.camp, campTorch);

        // Fog examples: enable soft fog on select backgrounds
        FogParams campFog = FogParams.defaults();
        campFog.puffCount = 14;
        campFog.intensityScale = 1.0f;
        campFog.minAlpha = 0.12f;
        campFog.maxAlpha = 0.25f;
        campFog.baseSpeed = 0.015f;
        campFog.sizeScale = 1.3f;
        campFog.heightStart01 = 0.35f;
        campFog.heightEnd01 = 1f;
        campFog.windOscillationPeriodMs = 9000f;
        campFog.windOscillationAmplitude = 0.20f;
        campFog.color = 0xFFBBFFCC; // neutral white
        FOG_PARAMS.put(R.drawable.areawarriorcampbackground, campFog);

        FogParams tombFog = FogParams.defaults();
        tombFog.puffCount = 20;
        tombFog.intensityScale = 1.6f; // stronger, more obvious fog
        tombFog.minAlpha = 0.10f;
        tombFog.maxAlpha = 0.35f;
        tombFog.baseSpeed = 0.0135f; // slightly faster drift
        tombFog.sizeScale = 1.35f;
        tombFog.heightStart01 = 0.45f;
        tombFog.heightEnd01 = 1.00f;
        tombFog.windOscillationPeriodMs = 8000f;
        tombFog.windOscillationAmplitude = 0.30f;
        tombFog.color = 0xFFEFEFEF; // slightly warm fog
        FOG_PARAMS.put(R.drawable.septictombbackground, tombFog);

        FogParams blacktidepoolFog = FogParams.defaults();
        blacktidepoolFog.puffCount = 10;
        blacktidepoolFog.intensityScale = 1.0f; // stronger, more obvious fog
        blacktidepoolFog.minAlpha = 0.3f;
        blacktidepoolFog.maxAlpha = 0.6f;
        blacktidepoolFog.baseSpeed = 0.0135f; // slightly faster drift
        blacktidepoolFog.sizeScale = 1.0f;
        blacktidepoolFog.heightStart01 = 0.4f;
        blacktidepoolFog.heightEnd01 = .8f;
        blacktidepoolFog.windOscillationPeriodMs = 8000f;
        blacktidepoolFog.windOscillationAmplitude = 0.30f;
        blacktidepoolFog.color = 0xFFEFEFEF; // slightly warm fog
        FOG_PARAMS.put(R.drawable.blacktidepoolbackground, blacktidepoolFog);

        // Thermal gas examples: swamp vents
        List<ThermalGasGroup> swampGas = new ArrayList<ThermalGasGroup>();
        ThermalGasParams swampParams = ThermalGasParams.defaults();
        swampParams.intensityScale = 0.3f;
        swampParams.spawnPerSecond = 2f;
        swampParams.riseSpeedPxPerMs = 0.05f;
        swampParams.minAlpha = 0.22f;
        swampParams.maxAlpha = 0.60f;
        swampParams.startColor = 0xCCB1FF5A; // sickly yellow-green
        swampParams.endColor = 0x4490CC40;
        swampGas.add(new ThermalGasGroup(new float[][] { { 0.24f, 0.23f }, { 0.3f, 0.42f }, { 0.75f, 0.52f } },
                swampParams));
        GAS_GROUPS.put(R.drawable.areawarriorcampbackground, swampGas);

        // Bubbling hotspots
        List<HotSpringBubbleGroup> bubbles = new ArrayList<HotSpringBubbleGroup>();
        HotSpringBubbleParams bubbleParams = HotSpringBubbleParams.defaults();
        bubbleParams.spawnPerSecond = 25f;
        bubbleParams.intensityScale = .2f;
        bubbleParams.rippleCount = 10;
        bubbleParams.rippleEndRadiusDp = 0.5f;
        bubbles.add(new HotSpringBubbleGroup(new float[][] { { 0.3f, 0.43f }, { 0.75f, 0.52f } }, bubbleParams));
        BUBBLE_GROUPS.put(R.drawable.areawarriorcampbackground, bubbles);

        // Ambient toggles: control rain/motes per background
        // Seed defaults for all known backgrounds; tweak later as desired
        RAIN_PROBABILITY.put(R.drawable.areaelvenwoodbackground, 0.15f);
        RAIN_PROBABILITY.put(R.drawable.areawarriorcampbackground, 0.35f);
        RAIN_PROBABILITY.put(R.drawable.areawestspellbackground, 0.10f);
        RAIN_PROBABILITY.put(R.drawable.blacktidepoolbackground, 0.25f);
        RAIN_PROBABILITY.put(R.drawable.camp, 0.00f);
        RAIN_PROBABILITY.put(R.drawable.canopycavebackground, 0.05f);
        RAIN_PROBABILITY.put(R.drawable.grassyplainsbackground, 0.20f);
        RAIN_PROBABILITY.put(R.drawable.rollingfoothillsbackground, 0.12f);
        RAIN_PROBABILITY.put(R.drawable.septictombbackground, 0.10f);
        RAIN_PROBABILITY.put(R.drawable.stagnantswampbackground, 0.30f);
        RAIN_PROBABILITY.put(R.drawable.tallforestbackground, 0.18f);
        RAIN_PROBABILITY.put(R.drawable.tavernbackground, 0.00f);
        RAIN_PROBABILITY.put(R.drawable.webbedwoodsbackground, 0.08f);
        RAIN_PROBABILITY.put(R.drawable.backgroundtreasure, 0.00f);

        // Enable/disable motes (default true when absent)
        MOTES_ENABLED.put(R.drawable.areaelvenwoodbackground, true);// 0
        MOTES_ENABLED.put(R.drawable.areawarriorcampbackground, false);// 1
        MOTES_ENABLED.put(R.drawable.areawestspellbackground, false);// 2
        MOTES_ENABLED.put(R.drawable.blacktidepoolbackground, false); // 3
        MOTES_ENABLED.put(R.drawable.camp, true); // 4
        MOTES_ENABLED.put(R.drawable.canopycavebackground, false);
        MOTES_ENABLED.put(R.drawable.grassyplainsbackground, true);
        MOTES_ENABLED.put(R.drawable.rollingfoothillsbackground, true);
        MOTES_ENABLED.put(R.drawable.septictombbackground, true);
        MOTES_ENABLED.put(R.drawable.stagnantswampbackground, false);
        MOTES_ENABLED.put(R.drawable.tallforestbackground, true);
        MOTES_ENABLED.put(R.drawable.tavernbackground, true); // 11
        MOTES_ENABLED.put(R.drawable.webbedwoodsbackground, false);// 12
        MOTES_ENABLED.put(R.drawable.backgroundtreasure, true);

        // The new inn or tomb images can be added similarly when used.
    }
}
