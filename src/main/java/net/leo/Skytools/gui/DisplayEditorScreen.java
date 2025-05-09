package net.leo.Skytools.gui;

import net.leo.Skytools.config.DisplayConfig;
import net.leo.Skytools.renderer.*;
import net.leo.Skytools.state.DisplayState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class DisplayEditorScreen extends Screen {
    private String label;
    private boolean dragging = false;
    private int dragOffsetX, dragOffsetY;

    private static final int RESIZER_SIZE = 3;
    private static final int RESIZER_LENGTH = 5;
    private boolean resizing = false;

    private int x , y, width, height;
    private Double size;

    public DisplayEditorScreen(String label) {
        super(Component.literal("Edit Position: " + label));
        this.label = label;

        switch (label) {
            case "Display Cords Hud" -> {
                this.x = DisplayState.cordX;
                this.y = DisplayState.cordY;
                this.size = DisplayState.cordSize;
                this.width = (int)(DisplayState.cordWidth * this.size);
                this.height = (int)(DisplayState.cordHeight * this.size);
            }
            case "Display Yaw/Pitch Hud" -> {
                this.x = DisplayState.yawPitchX;
                this.y = DisplayState.yawPitchY;
                this.size = DisplayState.yawPitchSize;
                this.width = (int)(DisplayState.yawPitchWidth * this.size);
                this.height = (int)(DisplayState.yawPitchHeight * this.size);
            }
            case "Display Pest Hud" -> {
                this.x = DisplayState.pestX;
                this.y = DisplayState.pestY;
                this.size = DisplayState.pestSize;
                this.width = (int)(DisplayState.pestWidth * this.size);
                this.height = (int)(DisplayState.pestHeight * this.size);
            }
            case "Display Pet Hud" -> {
                this.x = DisplayState.petX;
                this.y = DisplayState.petY;
                this.size = DisplayState.petSize;
                this.width = (int)(DisplayState.petWidth * this.size);
                this.height = (int)(DisplayState.petHeight * this.size);
            }
            case "Display Mana Bar" -> {
                this.x = DisplayState.manaBarX;
                this.y = DisplayState.manaBarY;
                this.size = DisplayState.manaBarSize;
                this.width = (int)(DisplayState.manaBarWidth * this.size);
                this.height = (int)(DisplayState.manaBarHeight * this.size);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (isOverResizer((int)mouseX, (int)mouseY)) {
                resizing = true;
                return true;
            } else if (isOverManaBar((int)mouseX, (int)mouseY)) {
                this.dragging = true;
                this.dragOffsetX = (int) mouseX - x;
                this.dragOffsetY = (int) mouseY - y;
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.dragging = false;
        this.resizing = false;

        switch (label) {
            case "Display Cords Hud" -> {
                DisplayState.cordX = this.x;
                DisplayState.cordY = this.y;
                DisplayState.cordSize = this.size;

                DisplayConfig.CORD_X.set(this.x);
                DisplayConfig.CORD_Y.set(this.y);
                DisplayConfig.CORD_SIZE.set(this.size);
            }
            case "Display Yaw/Pitch Hud" -> {
                DisplayState.yawPitchX = this.x;
                DisplayState.yawPitchY = this.y;
                DisplayState.yawPitchSize = this.size;

                DisplayConfig.YAW_PITCH_X.set(this.x);
                DisplayConfig.YAW_PITCH_Y.set(this.y);
                DisplayConfig.YAW_PITCH_SIZE.set(this.size);
            }
            case "Display Pest Hud" -> {
                DisplayState.pestX = this.x;
                DisplayState.pestY = this.y;
                DisplayState.pestSize = this.size;

                DisplayConfig.PEST_X.set(this.x);
                DisplayConfig.PEST_Y.set(this.y);
                DisplayConfig.PEST_SIZE.set(this.size);
            }
            case "Display Pet Hud" -> {
                DisplayState.petX = this.x;
                DisplayState.petY = this.y;
                DisplayState.petSize = this.size;

                DisplayConfig.PET_X.set(this.x);
                DisplayConfig.PET_Y.set(this.y);
                DisplayConfig.PET_SIZE.set(this.size);
            }
            case "Display Mana Bar" -> {
                DisplayState.manaBarX = this.x;
                DisplayState.manaBarY = this.y;
                DisplayState.manaBarSize = this.size;

                DisplayConfig.MANA_X.set(this.x);
                DisplayConfig.MANA_Y.set(this.y);
                DisplayConfig.MANA_SIZE.set(this.size);
            }
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            Minecraft.getInstance().setScreen(new ToggleMenu());
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int editorX, int editorY, float partialTicks) {
        if (dragging) {
            this.x = editorX - dragOffsetX;
            this.y = editorY - dragOffsetY;
        }

        if (resizing) {
            int newWidth = editorX - x;
            switch (label) {
                case "Display Cords Hud" -> {
                    size = Math.max(0.5, newWidth / (double) DisplayState.cordWidth);
                    width = (int)(DisplayState.cordWidth * size);
                    height = (int)(DisplayState.cordHeight * size);
                }
                case "Display Yaw/Pitch Hud" -> {
                    size = Math.max(0.5, newWidth / (double) DisplayState.yawPitchWidth);
                    width = (int)(DisplayState.yawPitchWidth * size);
                    height = (int)(DisplayState.yawPitchHeight * size);
                }
                case "Display Pest Hud" -> {
                    size = Math.max(0.5, newWidth / (double) DisplayState.pestWidth);
                    width = (int)(DisplayState.pestWidth * size);
                    height = (int)(DisplayState.pestHeight * size);
                }
                case "Display Pet Hud" -> {
                    size = Math.max(0.5, newWidth / (double) DisplayState.petWidth);
                    width = (int)(DisplayState.petWidth * size);
                    height = (int)(DisplayState.petHeight * size);
                }
                case "Display Mana Bar" -> {
                    size = Math.max(0.5, newWidth / (double) DisplayState.manaBarWidth);
                    width = (int)(DisplayState.manaBarWidth * size);
                    height = (int)(DisplayState.manaBarHeight * size);
                }
            }
        }

        switch (label) {
            case "Display Cords Hud" -> CordsRenderer.renderCords(guiGraphics, this.x, this.y, this.size, 0, 0, 0);
            case "Display Yaw/Pitch Hud" -> YawPitchRenderer.renderYawPitch(guiGraphics, this.x, this.y, this.size, 0, 0);
            case "Display Pest Hud" -> PestRenderer.renderPestDisplay(guiGraphics, this.x, this.y, this.size);
            case "Display Pet Hud" -> PetRenderer.renderPetDisplay(guiGraphics, this.x, this.y, this.size, true);
            case "Display Mana Bar" -> ManaBarRenderer.renderManaBar(guiGraphics, this.x, this.y, this.size);
        }

        // Draw resizer
        int outerX = x + width;
        int outerY = y + height;

        if(size != -1) {
            guiGraphics.fill(
                    outerX - RESIZER_LENGTH, outerY,
                    outerX + RESIZER_SIZE, outerY + RESIZER_SIZE,
                    0xFFAAAAAA
            );

            guiGraphics.fill(
                    outerX, outerY - RESIZER_LENGTH,
                    outerX + RESIZER_SIZE, outerY + RESIZER_SIZE,
                    0xFFAAAAAA
            );
        }
    }

    private boolean isOverManaBar(int mouseX, int mouseY) {
        return mouseX >= this.x && mouseX <= this.x + this.width &&
                mouseY >= this.y && mouseY <= this.y + this.height;
    }

    private boolean isOverResizer(int mouseX, int mouseY) {
        if(size == -1) return false;

        int outerX = x + width;
        int outerY = y + height;

        boolean overHorizontal = mouseX >= outerX - RESIZER_LENGTH && mouseX <= outerX + RESIZER_SIZE &&
                mouseY >= outerY && mouseY <= outerY + RESIZER_SIZE;

        boolean overVertical = mouseX >= outerX && mouseX <= outerX + RESIZER_SIZE &&
                mouseY >= outerY - RESIZER_LENGTH && mouseY <= outerY + RESIZER_SIZE;

        return overHorizontal || overVertical;
    }
}
