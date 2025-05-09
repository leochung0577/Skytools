package net.leo.Skytools.renderer;

import net.leo.Skytools.state.DisplayState;
import net.leo.Skytools.state.GameState;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ManaBarRenderer {
    private static final ResourceLocation EXPERIENCE_BAR_BACKGROUND_SPRITE =
            ResourceLocation.fromNamespaceAndPath("skytools", "textures/hud/mana_bar_background.png");
    private static final ResourceLocation EXPERIENCE_BAR_PROGRESS_SPRITE =
            ResourceLocation.fromNamespaceAndPath("skytools", "textures/hud/mana_bar_progress.png");
    private static final ResourceLocation EXPERIENCE_BAR_BACKGROUND_SPRITE_SMALL =
            ResourceLocation.fromNamespaceAndPath("skytools", "textures/hud/mana_bar_background_small.png");
    private static final ResourceLocation EXPERIENCE_BAR_PROGRESS_SPRITE_SMALL =
            ResourceLocation.fromNamespaceAndPath("skytools", "textures/hud/mana_bar_progress_small.png");

    public static void renderManaBar(GuiGraphics guiGraphics, int x, int y, double size) {
        int barWidth = (int)(DisplayState.manaBarWidth * size);
        int barHeight = (int)(DisplayState.manaBarHeight * size);

        int currentMana = GameState.currentMana;
        int maxMana = GameState.maxMana;

        int filled = (int) ((currentMana / (float) maxMana) * barWidth);

        // Background
        guiGraphics.blit(RenderType::guiTextured,
                EXPERIENCE_BAR_BACKGROUND_SPRITE_SMALL,
                x, y,
                0f, 0f,
                barWidth, barHeight,
                barWidth, barHeight);

        // Foreground (progress)
        if (filled > 0) {
            guiGraphics.blit(RenderType::guiTextured,
                    EXPERIENCE_BAR_PROGRESS_SPRITE_SMALL,
                    x, y,
                    0f, 0f,
                    filled, barHeight,
                    barWidth, barHeight);
        }
    }
}
