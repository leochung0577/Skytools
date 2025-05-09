package net.leo.Skytools.renderer;

import net.leo.Skytools.state.GameState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class YawPitchRenderer {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void renderYawPitch(GuiGraphics guiGraphics, int x, int y, double size) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0); // Move origin to (x, y)
        guiGraphics.pose().scale((float)size, (float)size, 1.0f);
        guiGraphics.drawString(
                mc.font,
                Component.literal(String.format("Yaw: %.1f", GameState.yaw)),
                0, 0,
                0xFFFFFF,
                true
        );
        guiGraphics.drawString(
                mc.font,
                Component.literal(String.format("Pitch: %.1f", GameState.pitch)),
                0, 10,
                0xFFFFFF,
                true
        );
        guiGraphics.pose().popPose();
    }

    public static void renderYawPitch(GuiGraphics guiGraphics, int x, int y, double size, double yaw, double pitch) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale((float)size, (float)size, 1.0f);
        guiGraphics.drawString(
                mc.font,
                Component.literal(String.format("Yaw: %.1f", yaw)),
                0, 0,
                0xFFFFFF,
                true
        );
        guiGraphics.drawString(
                mc.font,
                Component.literal(String.format("Pitch: %.1f", pitch)),
                0, 10,
                0xFFFFFF,
                true
        );
        guiGraphics.pose().popPose();
    }
}
