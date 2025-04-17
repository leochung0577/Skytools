package net.leo.Skytools.gui;

import net.leo.Skytools.config.SkyConfig;
import net.leo.Skytools.util.GameState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TogglesMenu extends Screen {

    private final List<String> toggleNames = List.of(
            "Remove Fog",
            "Pest Hud",
            "Yaw/Pitch Hud",
            "Pet Display"
    );

    private final Map<String, Boolean> toggleStates = new HashMap<>();

    public TogglesMenu() {
        super(Component.literal("Skytoggle Menu"));
    }

    @Override
    protected void init() {
        super.init();

        int toggleWidth = 200;
        int toggleHeight = 20;
        int gap = 10;
        int totalToggle = toggleNames.size();
        int centerX = this.width / 2 - toggleWidth / 2;
        int startY = this.height / 2 - (totalToggle * (toggleHeight + gap)) / 2;

        for (int i = 0; i < totalToggle; i++) {
            String toggleName = toggleNames.get(i);
            boolean isToggled = SkyConfig.TOGGLE_MAP.get(toggleName).get();
            toggleStates.put(toggleName, isToggled);

            int y = startY + i * (toggleHeight + gap);

            Button toggleButton = Button.builder(
                    Component.literal(getButtonLabel(toggleName, isToggled)),
                    button -> {
                        boolean toggled = !toggleStates.get(toggleName);
                        toggleStates.put(toggleName, toggled);
                        SkyConfig.TOGGLE_MAP.get(toggleName).set(toggled);
                        SkyConfig.SPEC.save();

                        // Update GameState
                        switch (toggleName) {
                            case "Remove Fog" -> GameState.RemoveFogToggle = toggled;
                            case "Pest Hud" -> GameState.showPesthud = toggled;
                            case "Yaw/Pitch Hud" -> GameState.showYawPitch = toggled;
                            case "Pet Display" -> GameState.displayPet = toggled;
                        }

                        // Update button label
                        button.setMessage(Component.literal(getButtonLabel(toggleName, toggled)));
                    }
            ).bounds(centerX, y, toggleWidth, toggleHeight).build();

            addRenderableWidget(toggleButton);
        }

        addRenderableWidget(Button.builder(
                Component.literal("Done"),
                b -> Minecraft.getInstance().setScreen(new SkytoolsMenu())
        ).bounds(this.width / 2 - 100, this.height - 25, 200, 20).build());
    }

    private String getButtonLabel(String name, boolean state) {
        return name + ": " + (state ? "§aON" : "§cOFF");
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }
}
