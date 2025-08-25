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
    private static final SparseArray<float[][]> FIREFLY_ANCHORS = new SparseArray<float[][]>();
    private static final SparseArray<FireflyParams> FIREFLY_PARAMS = new SparseArray<FireflyParams>();
    private static final SparseArray<LeavesParams> LEAVES_PARAMS = new SparseArray<LeavesParams>();
    private static final SparseArray<SnowParams> SNOW_PARAMS = new SparseArray<SnowParams>();
    private static final SparseArray<TorchBloomParams> TORCH_BLOOM_PARAMS = new SparseArray<TorchBloomParams>();
    private static final SparseArray<GroundFogParams> GROUND_FOG_PARAMS = new SparseArray<GroundFogParams>();
    private static final SparseArray<FlyingFlockParams> FLOCK_PARAMS = new SparseArray<FlyingFlockParams>();
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

    public static float[][] getFireflyAnchors(Context ctx, int backgroundResId) {
        ensureInit();
        return FIREFLY_ANCHORS.get(backgroundResId);
    }

    public static FireflyParams getFireflyParams(Context ctx, int backgroundResId) {
        ensureInit();
        FireflyParams p = FIREFLY_PARAMS.get(backgroundResId);
        return p == null ? FireflyParams.defaults() : p;
    }

    public static LeavesParams getLeavesParams(Context ctx, int backgroundResId) {
        ensureInit();
        return LEAVES_PARAMS.get(backgroundResId);
    }

    public static SnowParams getSnowParams(Context ctx, int backgroundResId) {
        ensureInit();
        return SNOW_PARAMS.get(backgroundResId);
    }

    public static TorchBloomParams getTorchBloomParams(Context ctx, int backgroundResId) {
        ensureInit();
        return TORCH_BLOOM_PARAMS.get(backgroundResId);
    }

    public static GroundFogParams getGroundFogParams(Context ctx, int backgroundResId) {
        ensureInit();
        return GROUND_FOG_PARAMS.get(backgroundResId);
    }

    public static FlyingFlockParams getFlockParams(Context ctx, int backgroundResId) {
        ensureInit();
        return FLOCK_PARAMS.get(backgroundResId);
    }

    private static void ensureInit() {
        if (initialized)
            return;
        initialized = true;

        // BG 0: areaelvenwoodbackground
        // BG 1: areawarriorcampbackground

        // BG 2: areawestspellbackground
        // BG 3: blacktidepoolbackground
        // BG 4: camp
        // BG 5: canopycavebackground
        // BG 6: grassyplainsbackground
        // BG 7: rollingfoothillsbackground
        // BG 8: septictombbackground
        // BG 9: stagnantswampbackground
        // BG 10: tallforestbackground
        // BG 11: tavernbackground
        // BG 12: webbedwoodsbackground
        // BG 13: backgroundtreasure

        /* BG 0: areaelvenwoodbackground */
        // Fireflies
        FIREFLY_ANCHORS.put(R.drawable.areaelvenwoodbackground, new float[][] { { 0.40f, 0.62f }, { 0.78f, 0.54f } });
        FIREFLY_PARAMS.put(R.drawable.areaelvenwoodbackground, FireflyParams.defaults());
        // Ambient toggles for elven wood
        RAIN_PROBABILITY.put(R.drawable.areaelvenwoodbackground, 0.15f);
        MOTES_ENABLED.put(R.drawable.areaelvenwoodbackground, true);

        /* BG 1: areawarriorcampbackground */
        // Ambient toggles for warrior camp
        RAIN_PROBABILITY.put(R.drawable.areawarriorcampbackground, 0.35f);
        MOTES_ENABLED.put(R.drawable.areawarriorcampbackground, false);
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

        /* BG 2: areawestspellbackground */
        // Ambient toggles for west spell
        RAIN_PROBABILITY.put(R.drawable.areawestspellbackground, 0.10f);
        MOTES_ENABLED.put(R.drawable.areawestspellbackground, false);

        /* BG 3: blacktidepoolbackground */
        // Ambient toggles for black tide pool
        RAIN_PROBABILITY.put(R.drawable.blacktidepoolbackground, 0.25f);
        MOTES_ENABLED.put(R.drawable.blacktidepoolbackground, false);
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
        // Ground fog layers
        {
            GroundFogParams groundSwamp = GroundFogParams.defaults();
            GROUND_FOG_PARAMS.put(R.drawable.blacktidepoolbackground, groundSwamp);
        }

        /* BG 4: camp */
        // Ambient toggles for camp
        RAIN_PROBABILITY.put(R.drawable.camp, 0.00f);
        MOTES_ENABLED.put(R.drawable.camp, true);
        TORCH_ANCHORS.put(R.drawable.camp, new float[][] {
                { 0.30f, 0.67f }
        });
        TorchFireParams campTorch = TorchFireParams.defaults();
        campTorch.intensityScale = 0.4f;
        campTorch.sizeScale = 0.7f;
        campTorch.speedScale = 0.3f;
        campTorch.jitterScale = 1.6f;
        campTorch.startColor = 0xFFFFE890;
        campTorch.endColor = 0xFFCC4411;
        TORCH_PARAMS.put(R.drawable.camp, campTorch);
        // Torch bloom
        TORCH_BLOOM_PARAMS.put(R.drawable.camp, TorchBloomParams.defaults());
        // Fireflies near camp
        FIREFLY_ANCHORS.put(R.drawable.camp, new float[][] { { 0.32f, 0.25f } });

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

        /* BG 5: canopycavebackground */
        // Ambient toggles for canopy cave
        RAIN_PROBABILITY.put(R.drawable.canopycavebackground, 0.05f);
        MOTES_ENABLED.put(R.drawable.canopycavebackground, false);

        /* BG 6: grassyplainsbackground */
        // Snow
        {
            SnowParams snowPlains = SnowParams.defaults();
            snowPlains.flakeCount = 90;
            SNOW_PARAMS.put(R.drawable.grassyplainsbackground, snowPlains);
        }
        // Ambient toggles for grassy plains
        RAIN_PROBABILITY.put(R.drawable.grassyplainsbackground, 0.20f);
        MOTES_ENABLED.put(R.drawable.grassyplainsbackground, true);

        /* BG 7: rollingfoothillsbackground */
        // Leaves
        {
            LeavesParams leavesHills = LeavesParams.defaults();
            leavesHills.count = 25;
            LEAVES_PARAMS.put(R.drawable.rollingfoothillsbackground, leavesHills);
        }
        // Snow
        {
            SnowParams snowFoothills = SnowParams.defaults();
            snowFoothills.flakeCount = 140;
            SNOW_PARAMS.put(R.drawable.rollingfoothillsbackground, snowFoothills);
        }
        // Birds
        {
            FlyingFlockParams birdsHills = FlyingFlockParams.defaults();
            birdsHills.altitude01 = 0.22f;
            birdsHills.count = 8;
            FLOCK_PARAMS.put(R.drawable.rollingfoothillsbackground, birdsHills);
        }
        // Ambient toggles for rolling foothills
        RAIN_PROBABILITY.put(R.drawable.rollingfoothillsbackground, 0.12f);
        MOTES_ENABLED.put(R.drawable.rollingfoothillsbackground, true);

        /* BG 8: septictombbackground */
        // Ambient toggles for septic tomb
        RAIN_PROBABILITY.put(R.drawable.septictombbackground, 0.10f);
        MOTES_ENABLED.put(R.drawable.septictombbackground, true);
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
        // Torch bloom
        TORCH_BLOOM_PARAMS.put(R.drawable.septictombbackground, TorchBloomParams.defaults());

        /* BG 9: stagnantswampbackground */
        // Ground fog layers
        {
            GroundFogParams groundSwamp = GroundFogParams.defaults();
            GROUND_FOG_PARAMS.put(R.drawable.stagnantswampbackground, groundSwamp);
        }
        // Ambient toggles for stagnant swamp
        RAIN_PROBABILITY.put(R.drawable.stagnantswampbackground, 0.30f);
        MOTES_ENABLED.put(R.drawable.stagnantswampbackground, false);

        /* BG 10: tallforestbackground */
        // Fireflies
        FIREFLY_ANCHORS.put(R.drawable.tallforestbackground, new float[][] { { 0.25f, 0.65f }, { 0.72f, 0.58f } });
        FIREFLY_PARAMS.put(R.drawable.tallforestbackground, FireflyParams.defaults());
        // Leaves
        LEAVES_PARAMS.put(R.drawable.tallforestbackground, LeavesParams.defaults());
        // Ambient toggles for tall forest
        RAIN_PROBABILITY.put(R.drawable.tallforestbackground, 0.18f);
        MOTES_ENABLED.put(R.drawable.tallforestbackground, true);

        /* BG 11: tavernbackground */
        // Ambient toggles for tavern
        RAIN_PROBABILITY.put(R.drawable.tavernbackground, 0.00f);
        MOTES_ENABLED.put(R.drawable.tavernbackground, true);

        /* BG 12: webbedwoodsbackground */
        // Bats
        {
            FlyingFlockParams batsWoods = FlyingFlockParams.defaults();
            batsWoods.altitude01 = 0.35f;
            batsWoods.count = 12;
            batsWoods.color = 0xCC000000;
            FLOCK_PARAMS.put(R.drawable.webbedwoodsbackground, batsWoods);
        }
        // Ambient toggles for webbed woods
        RAIN_PROBABILITY.put(R.drawable.webbedwoodsbackground, 0.08f);
        MOTES_ENABLED.put(R.drawable.webbedwoodsbackground, false);

        /* BG 13: backgroundtreasure */
        // Torch bloom
        TORCH_BLOOM_PARAMS.put(R.drawable.backgroundtreasure, TorchBloomParams.defaults());
        // Ambient toggles for treasure
        RAIN_PROBABILITY.put(R.drawable.backgroundtreasure, 0.00f);
        MOTES_ENABLED.put(R.drawable.backgroundtreasure, true);

        // (ambient toggles and per-background effect configs are defined in their
        // sections above)

        // The new inn or tomb images can be added similarly when used.
    }
}
