package net.leo.Skytools.renderer;

import net.leo.Skytools.obj.Pet;
import net.leo.Skytools.state.GameState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;

public class PetRenderer {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void renderPetDisplay(GuiGraphics guiGraphics, int x, int y, double size) {

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale((float) size, (float) size, 1.0f);
        // Pet display name
        guiGraphics.drawString(
                mc.font,
                Component.literal(GameState.currentPet.displayPet()),
                18,
                0,
                0xFFFFFF,
                true
        );

        // Pet progress
        guiGraphics.drawString(
                Minecraft.getInstance().font,
                Component.literal(GameState.currentPet.progress),
                18,
                10,
                0xFFFFFF,
                true
        );

        // Draw image outside scaling, at real pixel position
        if (GameState.currentPet.petImage != null) {
            guiGraphics.blit(
                    RenderType::guiTextured,
                    GameState.currentPet.petImage,
                    0,
                    0,
                    0f,
                    0f,
                    16,
                    16,
                    16,
                    16
            );
        }

        guiGraphics.pose().popPose();
    }

    public static void renderPetDisplay(GuiGraphics guiGraphics, int x, int y, double size, boolean test) {
        Pet pet = new Pet("bingo", 1);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale((float) size, (float) size, 1.0f);
        // Pet display name
        guiGraphics.drawString(
                mc.font,
                Component.literal(pet.displayPet()),
                18,
                0,
                0xFFFFFF,
                true
        );

        // Pet progress
        guiGraphics.drawString(
                Minecraft.getInstance().font,
                Component.literal(pet.progress),
                18,
                10,
                0xFFFFFF,
                true
        );

        // Draw image outside scaling, at real pixel position
        if (pet.petImage != null) {
            guiGraphics.blit(
                    RenderType::guiTextured,
                    pet.petImage,
                    0,
                    0,
                    0f,
                    0f,
                    16,
                    16,
                    16,
                    16
            );
        }

        guiGraphics.pose().popPose();
    }
}
