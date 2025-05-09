package net.leo.Skytools.gui;

import net.leo.Skytools.config.SkyConfig;
import net.leo.Skytools.state.ToggleState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToggleMenu extends Screen {
    private final List<String> toggleNames = List.of(
            "Remove Fog",
            "Display Pest Hud",
            "Display Yaw/Pitch Hud",
            "Display Pet Hud",
            "Display Cords Hud",
            "Display Mana Bar",
            "Rare Garden Offers Highlight"
    );

    private final Map<String, Boolean> toggleStates = new HashMap<>();

    public ToggleMenu() {
        super(Component.literal("Skytoggle Menu"));
    }

    @Override
    protected void init() {
        super.init();

        int toggleWidth = 200;
        int toggleHeight = 20;
        int gap = 10;
        int totalToggles = toggleNames.size();
        int columns = 2;

        int rows = (int) Math.ceil((double) totalToggles / columns);

        int totalWidth = columns * toggleWidth + (columns - 1) * gap;
        int totalHeight = rows * toggleHeight + (rows - 1) * gap;

        int startX = (this.width - totalWidth) / 2;
        int startY = (this.height - totalHeight) / 2;

        for (int i = 0; i < totalToggles; i++) {
            String toggleName = toggleNames.get(i);
            boolean isToggled = SkyConfig.TOGGLE_MAP.get(toggleName).get();
            toggleStates.put(toggleName, isToggled);

            int col = i % columns;
            int row = i / columns;

            int x = startX + col * (toggleWidth + gap);
            int y = startY + row * (toggleHeight + gap);
            addRenderableWidget(new ToggleButton(x, y, toggleWidth, toggleHeight, toggleName));
        }

        addRenderableWidget(Button.builder(
                Component.literal("Done"),
                b -> this.onClose()
        ).bounds(this.width / 2 - 100, this.height - 25, 200, 20).build());
    }

    private String getButtonLabel(String name, boolean state) {
        return name + ": " + (state ? "§aON" : "§cOFF");
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);

        graphics.drawCenteredString(this.font, "Note: ", 125, 10, 0xFFFFFF);
        graphics.drawCenteredString(this.font, "Left Click to toggle (ON / OFF)", 125, 25, 0xFFFFFF);
        graphics.drawCenteredString(this.font, "Right Click to edit size and location", 125, 37, 0xFFFFFF);
        graphics.drawCenteredString(this.font, "You can only edit the buttons that say \"Display\"", 125, 49, 0xFFFFFF);
        graphics.drawCenteredString(this.font, "Press \"esc\" on Editor screen to save edit", 125, 61, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    class ToggleButton extends AbstractWidget {
        private boolean toggled = false;
        private final String label;

        private static final ResourceLocation BUTTON = ResourceLocation.fromNamespaceAndPath("minecraft",
                "textures/gui/sprites/widget/button.png");
        private static final ResourceLocation BUTTON_HIGHLIGHTED = ResourceLocation.fromNamespaceAndPath("minecraft",
                "textures/gui/sprites/widget/button_highlighted.png");

        public ToggleButton(int x, int y, int width, int height, String label) {
            super(x, y, width, height, Component.literal(label));
            this.label = label;
            this.toggled = toggleStates.getOrDefault(label, false);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (this.isHovered && button == 1) { // Right-click
                if (label.startsWith("Display")) {
                    System.out.println("Right Clicked: " + label);
                    Minecraft.getInstance().setScreen(new DisplayEditorScreen(label));
                    return true;
                }
            } if(this.isHovered && button == 0) {
                toggled = !toggled;
                toggleStates.put(label, toggled);
                SkyConfig.TOGGLE_MAP.get(label).set(toggled);
                SkyConfig.SPEC.save();

                switch (label) {
                    case "Remove Fog" -> ToggleState.RemoveFogToggle = toggled;
                    case "Display Pest Hud" -> ToggleState.showPesthud = toggled;
                    case "Display Yaw/Pitch Hud" -> ToggleState.showYawPitch = toggled;
                    case "Display Pet Hud" -> ToggleState.displayPet = toggled;
                    case "Display Cords Hud" -> ToggleState.displayCords = toggled;
                    case "Display Mana Bar" -> ToggleState.displayManaBar = toggled;
                    case "Rare Garden Offers Highlight" -> ToggleState.displayRareGardenOffers = toggled;
                }
                return true;
            }
            return false;
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            int x = getX();
            int y = getY();
            int w = getWidth();
            int h = getHeight();

            // button render
            graphics.blit(RenderType::guiTextured, isHovered() ? BUTTON_HIGHLIGHTED : BUTTON, x, y, 0f, 0f, w, h, w, h);
            graphics.drawString(font, label, x + 10, y + (h - 8) / 2, 0xFFFFFF);
            graphics.drawString(font, toggled ? "§aON" : "§cOFF", x + w - 30, y + (h - 8) / 2, 0xFFFFFF);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }
    }
}
