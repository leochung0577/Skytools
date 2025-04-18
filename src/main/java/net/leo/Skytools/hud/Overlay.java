package net.leo.Skytools.hud;

import net.leo.Skytools.obj.Pest;
import net.leo.Skytools.util.GameState;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;

import java.util.List;

public class Overlay implements LayeredDraw.Layer {

    private static Minecraft mc = Minecraft.getInstance();
    @Override
    public void render(GuiGraphics graphics, DeltaTracker delta) {
        if (mc.player == null || mc.screen != null || !GameState.isInSkyblock) return;

        int x = 10;
        int y = 10;

        if(GameState.isInGarden()) {
            if(GameState.showYawPitch) {
                float yaw = mc.player.getYRot() % 360;
                if (yaw > 180) yaw -= 360;
                if (yaw < -180) yaw += 360;

                float pitch = mc.player.getXRot() % 360;
                if (pitch > 180) pitch -= 360;
                if (pitch < -180) pitch += 360;

                graphics.drawString(
                        mc.font,
                        Component.literal(String.format("Yaw: %.1f", yaw)),
                        x, y,
                        0xFFFFFF,
                        true
                );
                graphics.drawString(
                        mc.font,
                        Component.literal(String.format("Pitch: %.1f", pitch)),
                        x, y + 10,
                        0xFFFFFF,
                        true
                );
            }

            if(GameState.showPesthud)
                renderPestInfo(graphics, x + 20, y + 200, GameState.currentPest);
        }

        if (GameState.currentPet.isValidPet() && GameState.displayPet) {
            graphics.pose().pushPose();
            float bigScale = 1.5f;
            graphics.pose().scale(bigScale, bigScale, bigScale);

            int baseX = (int)((x + 125) / bigScale);
            int baseY = (int)((y + 10) / bigScale);

            // Pet display name
            graphics.drawString(
                    mc.font,
                    Component.literal(GameState.currentPet.displayPet()),
                    baseX,
                    baseY,
                    -1,
                    true
            );

            // Pet progress
            graphics.drawString(
                    mc.font,
                    Component.literal(GameState.currentPet.progress),
                    baseX,
                    baseY + 10, // Adjust this value for spacing between lines
                    -1,
                    true
            );

            graphics.pose().popPose();

            // Draw image next to text
            if (GameState.currentPet.petImage != null) {
                graphics.blit(RenderType::guiTextured,
                        GameState.currentPet.petImage,
                        x + 90,
                        y + 10,
                        0f,
                        0f,
                        32,
                        32,
                        32,
                        32);
            }
        }
    }

    public void renderPestInfo(GuiGraphics graphics, int x, int y, Pest pest) {
        List<String> info = List.of(
                "Alive: §4" + pest.alive,
                "Plots: " + pest.getPlots(),
                "Spray: " + pest.spray,
                "Repellent: " + pest.repellent,
                "Bonus: " + pest.bonus,
                "Cooldown: " + pest.cooldown
        );

        graphics.drawString(
                mc.font,
                "§4§lPest: ",
                x,
                y,
                0xFFFFFF,
                true
        );

        for (int i = 0; i < info.size(); i++) {
            graphics.drawString(
                    mc.font,
                    Component.literal(info.get(i)),
                    x + 10,
                    y + (i + 1)* 10,
                    0xFFFFFF,
                    true
            );
        }
    }
}