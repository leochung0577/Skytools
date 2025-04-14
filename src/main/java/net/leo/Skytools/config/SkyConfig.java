package net.leo.Skytools.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.Map;

public class SkyConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue REMOVE_FOG;
    public static final ForgeConfigSpec.BooleanValue PEST_HUD;
    public static final ForgeConfigSpec.BooleanValue YAWPITCH_HUD;
    public static final ForgeConfigSpec.BooleanValue SHOW_PET;

    static {
        BUILDER.push("Toggles");

        REMOVE_FOG = BUILDER.comment("Enable Remove Fog").define("removeFog", false);
        PEST_HUD = BUILDER.comment("Enable Pest hud").define("PestHud", false);
        YAWPITCH_HUD = BUILDER.comment("Enable Yaw/Pitch hud").define("YawPitchHud", false);
        SHOW_PET = BUILDER.comment("Enable Pet Display").define("displayPet", false);

        BUILDER.pop();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static final Map<String, ForgeConfigSpec.BooleanValue> TOGGLE_MAP = new HashMap<>();

    static {
        TOGGLE_MAP.put("Remove Fog", REMOVE_FOG);
        TOGGLE_MAP.put("Pest Hud", PEST_HUD);
        TOGGLE_MAP.put("Yaw/Pitch Hud", YAWPITCH_HUD);
        TOGGLE_MAP.put("Pet Display", SHOW_PET);

    }
}
