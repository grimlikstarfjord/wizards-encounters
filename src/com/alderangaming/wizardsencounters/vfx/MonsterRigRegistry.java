package com.alderangaming.wizardsencounters.vfx;

import android.util.SparseArray;

/**
 * Registry mapping monster IDs to MonsterRig definitions.
 * Falls back to null (meaning: use single-image path) when a rig is not
 * defined.
 */
public final class MonsterRigRegistry {
    private static final SparseArray<MonsterRig> MAP = new SparseArray<MonsterRig>();
    private static boolean initialized;

    private MonsterRigRegistry() {
    }

    public static MonsterRig get(int monsterId) {
        ensure();
        return MAP.get(monsterId);
    }

    public static void register(int monsterId, MonsterRig rig) {
        if (rig == null)
            return;
        MAP.put(monsterId, rig);
    }

    private static void ensure() {
        if (initialized)
            return;
        initialized = true;
        seedDefaults();
    }

    private static void seedDefaults() {
        // Example rig for monster 0 (Hideous Aunt). Uses sliced parts:
        // head, torso, right hand, left hand, right arm, left arm, right shoe, left
        // shoe.
        try {
            MonsterRig rig0 = new MonsterRig();

            MonsterRig.Part torso = new MonsterRig.Part();
            torso.type = MonsterRig.PartType.TORSO;
            torso.drawableName = "hideousaunt_torso";
            torso.zOrder = 1;
            torso.scaleX = 0.3f;
            torso.scaleY = 0.3f;
            torso.pivotXPct = 0.5f;
            torso.pivotYPct = 0.9f;
            torso.name = "torso";
            rig0.addPart(torso);

            MonsterRig.Part head = new MonsterRig.Part();
            head.type = MonsterRig.PartType.HEAD;
            head.drawableName = "hideousaunt_head";
            head.anchorToName = "torso";
            head.offsetXDp = 0f;
            head.offsetYDp = -600f;
            head.scaleX = 1f;
            head.scaleY = 1f;
            head.pivotXPct = 0.5f;
            head.pivotYPct = 0.90f;
            head.zOrder = 10; // ensure hair/face draw in front of arms/torso
            head.name = "head";
            rig0.addPart(head);

            MonsterRig.Part armLeft = new MonsterRig.Part();
            armLeft.type = MonsterRig.PartType.ARM_LEFT;
            armLeft.drawableName = "hideousaunt_arm_left";
            armLeft.anchorToName = "torso";
            armLeft.offsetXDp = 470f;
            armLeft.offsetYDp = -190f;
            armLeft.scaleX = 1f;
            armLeft.scaleY = 1f;
            armLeft.pivotXPct = 0.15f; // shoulder joint a bit in from the edge
            armLeft.pivotYPct = 0.08f;
            armLeft.zOrder = 2;
            armLeft.name = "arm_left";
            rig0.addPart(armLeft);

            MonsterRig.Part armRight = new MonsterRig.Part();
            armRight.type = MonsterRig.PartType.ARM_RIGHT;
            armRight.drawableName = "hideousaunt_arm_right";
            armRight.anchorToName = "torso";
            armRight.offsetXDp = -280f;
            armRight.offsetYDp = -190f;
            armRight.scaleX = 1f;
            armRight.scaleY = 1f;
            armRight.pivotXPct = 0.85f; // shoulder joint a bit in from the edge
            armRight.pivotYPct = 0.08f;
            armRight.zOrder = 2;
            armRight.name = "arm_right";
            rig0.addPart(armRight);

            MonsterRig.Part handLeft = new MonsterRig.Part();
            handLeft.type = MonsterRig.PartType.HAND_LEFT;
            handLeft.drawableName = "hideousaunt_hand_left";
            handLeft.anchorToName = "arm_left";
            handLeft.offsetXDp = 60f;
            handLeft.offsetYDp = 400f;
            handLeft.scaleX = 1f;
            handLeft.scaleY = 1f;
            handLeft.pivotXPct = 0.15f;
            handLeft.pivotYPct = 0.15f;
            handLeft.zOrder = 3;
            handLeft.name = "hand_left";
            rig0.addPart(handLeft);

            MonsterRig.Part handRight = new MonsterRig.Part();
            handRight.type = MonsterRig.PartType.HAND_RIGHT;
            handRight.drawableName = "hideousaunt_hand_right";
            handRight.anchorToName = "arm_right";
            handRight.offsetXDp = 18f;
            handRight.offsetYDp = 300f;
            handRight.scaleX = 1f;
            handRight.scaleY = 1f;
            handRight.pivotXPct = 0.85f;
            handRight.pivotYPct = 0.20f;
            handRight.zOrder = 3;
            handRight.name = "hand_right";
            rig0.addPart(handRight);

            MonsterRig.Part shoeLeft = new MonsterRig.Part();
            shoeLeft.type = MonsterRig.PartType.SHOE_LEFT;
            shoeLeft.drawableName = "hideousaunt_shoe_left";
            shoeLeft.anchorToName = "torso";
            shoeLeft.offsetXDp = 300f;
            shoeLeft.offsetYDp = 700f; // place near bottom of torso
            shoeLeft.scaleX = 1f;
            shoeLeft.scaleY = 1f;
            shoeLeft.pivotXPct = 0.5f;
            shoeLeft.pivotYPct = 0.2f;
            shoeLeft.zOrder = 0;
            shoeLeft.name = "shoe_left";
            rig0.addPart(shoeLeft);

            MonsterRig.Part shoeRight = new MonsterRig.Part();
            shoeRight.type = MonsterRig.PartType.SHOE_RIGHT;
            shoeRight.drawableName = "hideousaunt_shoe_right";
            shoeRight.anchorToName = "torso";
            shoeRight.offsetXDp = -100f;
            shoeRight.offsetYDp = 700f; // place near bottom of torso
            shoeRight.scaleX = 1f;
            shoeRight.scaleY = 1f;
            shoeRight.pivotXPct = 0.5f;
            shoeRight.pivotYPct = 0.2f;
            shoeRight.zOrder = 0;
            shoeRight.name = "shoe_right";
            rig0.addPart(shoeRight);

            register(0, rig0);
        } catch (Throwable ignored) {
        }
    }
}
