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
    public static final ForgeConfigSpec.BooleanValue SHOW_CORDS;
    public static final ForgeConfigSpec.BooleanValue MANA_BAR;
    public static final ForgeConfigSpec.BooleanValue RARE_GARDEN_OFFERS;

    static {
        BUILDER.push("Toggles");

        REMOVE_FOG = BUILDER.comment("Enable Remove Fog").define("removeFog", false);
        PEST_HUD = BUILDER.comment("Enable Pest HUD").define("pestHud", false);
        YAWPITCH_HUD = BUILDER.comment("Enable Yaw/Pitch HUD").define("yawPitchHud", false);
        SHOW_PET = BUILDER.comment("Enable Pet Display").define("displayPet", false);
        SHOW_CORDS = BUILDER.comment("Enable Coordinates Display").define("displayCords", false);
        MANA_BAR = BUILDER.comment("Enable Mana Bar Display").define("displayManaBar", false);
        RARE_GARDEN_OFFERS = BUILDER.comment("Enable Rare Garden Offers").define("rareGardenOffers", false);

        BUILDER.pop();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static final Map<String, ForgeConfigSpec.BooleanValue> TOGGLE_MAP = new HashMap<>();

    static {
        TOGGLE_MAP.put("Remove Fog", REMOVE_FOG);
        TOGGLE_MAP.put("Display Pest Hud", PEST_HUD);
        TOGGLE_MAP.put("Display Yaw/Pitch Hud", YAWPITCH_HUD);
        TOGGLE_MAP.put("Display Pet Hud", SHOW_PET);
        TOGGLE_MAP.put("Display Cords Hud", SHOW_CORDS);
        TOGGLE_MAP.put("Display Mana Bar", MANA_BAR);
        TOGGLE_MAP.put("Rare Garden Offers Highlight", RARE_GARDEN_OFFERS);
    }
}
