package net.leo.Skytools.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class DisplayConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // Cords
    public static ForgeConfigSpec.IntValue CORD_X;
    public static ForgeConfigSpec.IntValue CORD_Y;
    public static ForgeConfigSpec.DoubleValue CORD_SIZE;

    // Yaw & Pitch
    public static ForgeConfigSpec.IntValue YAW_PITCH_X;
    public static ForgeConfigSpec.IntValue YAW_PITCH_Y;
    public static ForgeConfigSpec.DoubleValue YAW_PITCH_SIZE;

    // Pest
    public static ForgeConfigSpec.IntValue PEST_X;
    public static ForgeConfigSpec.IntValue PEST_Y;
    public static ForgeConfigSpec.DoubleValue PEST_SIZE;

    // Pet
    public static ForgeConfigSpec.IntValue PET_X;
    public static ForgeConfigSpec.IntValue PET_Y;
    public static ForgeConfigSpec.DoubleValue PET_SIZE;

    // Mana
    public static ForgeConfigSpec.IntValue MANA_X;
    public static ForgeConfigSpec.IntValue MANA_Y;
    public static ForgeConfigSpec.DoubleValue MANA_SIZE;

    static {
        BUILDER.push("Display");

        // Cords
        BUILDER.push("Cords");
        CORD_X = BUILDER.defineInRange("x", 10, 0, 10000);
        CORD_Y = BUILDER.defineInRange("y", 10, 0, 10000);
        CORD_SIZE = BUILDER.defineInRange("size", 1.0, 0.1, 10.0);
        BUILDER.pop();

        // Yaw & Pitch
        BUILDER.push("YawPitch");
        YAW_PITCH_X = BUILDER.defineInRange("x", 10, 0, 10000);
        YAW_PITCH_Y = BUILDER.defineInRange("y", 20, 0, 10000);
        YAW_PITCH_SIZE = BUILDER.defineInRange("size", 1.0, 0.1, 10.0);
        BUILDER.pop();

        // Pest
        BUILDER.push("Pest");
        PEST_X = BUILDER.defineInRange("x", 30, 0, 10000);
        PEST_Y = BUILDER.defineInRange("y", 210, 0, 10000);
        PEST_SIZE = BUILDER.defineInRange("size", 1.0, 0.1, 10.0);
        BUILDER.pop();

        // Pet
        BUILDER.push("Pet");
        PET_X = BUILDER.defineInRange("x", 135, 0, 10000);
        PET_Y = BUILDER.defineInRange("y", 20, 0, 10000);
        PET_SIZE = BUILDER.defineInRange("size", 1.5, 0.1, 10.0);
        BUILDER.pop();

        // Mana
        BUILDER.push("ManaBar");
        MANA_X = BUILDER.defineInRange("x", 480, 0, 10000);
        MANA_Y = BUILDER.defineInRange("y", 458, 0, 10000);
        MANA_SIZE = BUILDER.defineInRange("size", 1.28, 0.1, 10.0);
        BUILDER.pop();

        BUILDER.pop();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
