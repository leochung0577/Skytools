package net.leo.Skytools.hud;

import net.leo.Skytools.renderer.CordsRenderer;
import net.leo.Skytools.renderer.PestRenderer;
import net.leo.Skytools.renderer.PetRenderer;
import net.leo.Skytools.renderer.YawPitchRenderer;
import net.leo.Skytools.state.DisplayState;
import net.leo.Skytools.state.GameState;
import net.leo.Skytools.state.ToggleState;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;

public class Overlay implements LayeredDraw.Layer {

    private static Minecraft mc = Minecraft.getInstance();
    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker delta) {
        if (mc.player == null || mc.screen != null
//                || !GameState.isInSkyblock
        ) return;

        if(ToggleState.displayCords) {
            GameState.playerX = mc.player.getX();
            GameState.playerY = mc.player.getY();
            GameState.playerZ = mc.player.getZ();

            CordsRenderer.renderCords(guiGraphics, DisplayState.cordX, DisplayState.cordY, DisplayState.cordSize);
        }

        if(GameState.isInGarden()) {
            if(ToggleState.showYawPitch) {
                float yaw = mc.player.getYRot() % 360;
                if (yaw > 180) yaw -= 360;
                if (yaw < -180) yaw += 360;

                float pitch = mc.player.getXRot() % 360;
                if (pitch > 180) pitch -= 360;
                if (pitch < -180) pitch += 360;

                GameState.yaw = yaw;
                GameState.pitch = pitch;

                YawPitchRenderer.renderYawPitch(guiGraphics, DisplayState.yawPitchX, DisplayState.yawPitchY, DisplayState.yawPitchSize);
            }

            if(ToggleState.showPesthud)
                PestRenderer.renderPestDisplay(guiGraphics, DisplayState.pestX, DisplayState.pestY, DisplayState.pestSize, GameState.currentPest);
        }

        if (GameState.currentPet.isValidPet() && ToggleState.displayPet) {
                PetRenderer.renderPetDisplay(guiGraphics, DisplayState.petX, DisplayState.petY, DisplayState.petSize);
        }
    }
}