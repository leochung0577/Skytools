package net.leo.Skytools.renderer;

import net.leo.Skytools.state.GameState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class CordsRenderer {
    public static final Minecraft mc = Minecraft.getInstance();

    public static void renderCords(GuiGraphics guiGraphics, int x, int y, double size) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0); // Move origin to (x, y)
        guiGraphics.pose().scale((float)size, (float)size, 1.0f);
        guiGraphics.drawString(
                mc.font,
                Component.literal(String.format("x: %.3f y: %.3f z: %.3f", GameState.playerX, GameState.playerY, GameState.playerZ)),
                0, 0,
                0xFFFFFF,
                true
        );
        guiGraphics.pose().popPose();
    }

    public static void renderCords(GuiGraphics guiGraphics, int x, int y, double size, double playerX, double playerY, double playerZ) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale((float)size, (float)size, 1.0f);
        guiGraphics.drawString(
                mc.font,
                Component.literal(String.format("x: %.3f y: %.3f z: %.3f", playerX, playerY, playerZ)),
                0, 0,
                0xFFFFFF,
                true
        );
        guiGraphics.pose().popPose();
    }
}

