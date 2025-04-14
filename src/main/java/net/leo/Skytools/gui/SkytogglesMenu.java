package net.leo.Skytools.gui;

import net.leo.Skytools.config.SkyConfig;
import net.leo.Skytools.util.GameState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkytogglesMenu extends Screen {

    private final List<String> toggleNames = List.of(
            "Remove Fog",
            "Pest Hud",
            "Yaw/Pitch Hud",
            "Pet Display"
    );

    private final Map<String, Boolean> toggleStates = new HashMap<>();

    public SkytogglesMenu() {
        super(Component.literal("Skytoggle Menu"));
    }

    @Override
    protected void init() {
        super.init();

        int toggleWidth = 200;
        int toggleHeight = 30;
        int gap = 12;
        int totalToggle = toggleNames.size();
        int centerX = this.width / 2 - toggleWidth / 2;
        int startY = this.height / 2 - (totalToggle * (toggleHeight + gap)) / 2;

        for (int i = 0; i < totalToggle; i++) {
            String toggleName = toggleNames.get(i);
            toggleStates.put(toggleName, SkyConfig.TOGGLE_MAP.get(toggleName).get());

            int y = startY + i * (toggleHeight + gap);
            addRenderableWidget(new ToggleButton(centerX, y, toggleWidth, toggleHeight, toggleName));
        }

        addRenderableWidget(Button.builder(Component.literal("Done"),
                button -> {
                    Minecraft.getInstance().setScreen(new SkytoolsMenu());
                }
        ).bounds(this.width / 2 - 200 / 2, this.height - 20 - 5, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    class ToggleButton extends AbstractWidget {
        private boolean toggled;
        private final String label;

        public ToggleButton(int x, int y, int width, int height, String label) {
            super(x, y, width, height, Component.literal(label));
            this.label = label;
            this.toggled = toggleStates.getOrDefault(label, false);
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            toggled = !toggled;
            toggleStates.put(label, toggled);
            SkyConfig.TOGGLE_MAP.get(label).set(toggled);
            SkyConfig.SPEC.save();

            SkyConfig.TOGGLE_MAP.get(label).set(toggled);
            SkyConfig.SPEC.save();

            if (label.equals("Remove Fog")) {
                GameState.RemoveFogToggle = toggled;
            }

            if (label.equals("Pest Hud")) {
                GameState.showPesthud = toggled;
            }

            if (label.equals("Yaw/Pitch Hud")) {
                GameState.showYawPitch = toggled;
            }

            if (label.equals("Pet Display")) {
                GameState.displayPet = toggled;
            }

        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            int x = getX();
            int y = getY();
            int w = getWidth();
            int h = getHeight();

            int textColor = toggled ? 0x00FF00 : 0xFF5555;

            // Background
            graphics.fill(x, y, x + w, y + h, 0x90000000);

            // Border on hover
            if (isHoveredOrFocused()) {
                graphics.fill(x - 1, y - 1, x + w + 1, y, 0xFFFFFFFF); // Top
                graphics.fill(x - 1, y + h, x + w + 1, y + h + 1, 0xFFFFFFFF); // Bottom
                graphics.fill(x - 1, y, x, y + h, 0xFFFFFFFF); // Left
                graphics.fill(x + w, y, x + w + 1, y + h, 0xFFFFFFFF); // Right
            }

            // Label and toggle state text
            graphics.drawString(font, label, x + 10, y + (h - 8) / 2, 0xFFFFFF);
            graphics.drawString(font, toggled ? "ON" : "OFF", x + w - 30, y + (h - 8) / 2, textColor);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }
    }
}
