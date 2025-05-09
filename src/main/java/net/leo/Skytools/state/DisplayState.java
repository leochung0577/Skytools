package net.leo.Skytools.state;

import net.leo.Skytools.config.DisplayConfig;

public class DisplayState {

    // cords
    public static int cordX = DisplayConfig.CORD_X.get();
    public static int cordY = DisplayConfig.CORD_Y.get();
    public static double cordSize = DisplayConfig.CORD_SIZE.get();
    public static final int cordWidth = 122;
    public static final int cordHeight = 8;

    // yaw pitch
    public static int yawPitchX = DisplayConfig.YAW_PITCH_X.get();
    public static int yawPitchY = DisplayConfig.YAW_PITCH_Y.get();
    public static double yawPitchSize = DisplayConfig.YAW_PITCH_SIZE.get();
    public static final int yawPitchWidth = 44;
    public static final int yawPitchHeight = 18;

    // pest
    public static int pestX = DisplayConfig.PEST_X.get();
    public static int pestY = DisplayConfig.PEST_Y.get();
    public static double pestSize = DisplayConfig.PEST_SIZE.get();
    public static final int pestWidth = 98;
    public static final int pestHeight = 68;

    // pet
    public static int petX = DisplayConfig.PET_X.get();
    public static int petY = DisplayConfig.PET_Y.get();
    public static double petSize = DisplayConfig.PET_SIZE.get();
    public static final int petWidth = 88;
    public static final int petHeight = 18;

    // mana
    public static int manaBarX = DisplayConfig.MANA_X.get();
    public static int manaBarY = DisplayConfig.MANA_Y.get();
    public static double manaBarSize = DisplayConfig.MANA_SIZE.get();
    public static final int manaBarWidth = 71;
    public static final int manaBarHeight = 5;
}
