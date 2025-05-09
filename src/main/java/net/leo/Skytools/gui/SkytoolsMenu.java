package net.leo.Skytools.gui;

import net.leo.Skytools.state.GameState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public class SkytoolsMenu extends Screen {

    private final List<String> buttonNames = List.of(
            "Tp to Pest"
    );

    public SkytoolsMenu() {
        super(Component.literal("Skytools"));
    }

    @Override
    protected void init() {
        super.init();

        int buttonWidth = 125;
        int buttonHeight = 25;
        int gap = 10; // Space between buttons
        int buttonsPerRow = 3; // Number of buttons per row
        int totalButtons = buttonNames.size();
        int centerX = this.width / 2 - (buttonsPerRow * (buttonWidth + gap) - gap) / 2;

        for (int i = 0; i < totalButtons; i++) {
            int row = i / buttonsPerRow;
            int col = i % buttonsPerRow;

            int x = centerX + col * (buttonWidth + gap);
            int y = this.height / 2 - (totalButtons / buttonsPerRow) * (buttonHeight + gap) / 2 + row * (buttonHeight + gap);

            int index = i;
            addRenderableWidget(Button.builder(
                    Component.literal(buttonNames.get(i)),
                    button -> {
                        String buttonName = buttonNames.get(index);
                        this.onClose();

                        switch (buttonName) {
                            case "Tp to Pest" -> {
                                GameState.currentPest.tp2Pest();
                            }
                        }
                    }
            ).bounds(x, y, buttonWidth, buttonHeight).build());
        }

        addRenderableWidget(Button.builder(Component.literal("Done"),
                button -> {
                    this.onClose();
                }
        ).bounds(this.width / 2 - 200 / 2, this.height - 20 - 5, 200, 20).build());


        addRenderableWidget(Button.builder(Component.literal("Toggles"),
                button -> {
                    Minecraft mc = Minecraft.getInstance();
                    mc.setScreen(new ToggleMenu());
                }
        ).bounds(10, this.height - 20 - 10, 75, 20).build());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (GameState.menuKey != null && GameState.menuKey.matches(keyCode, scanCode)) {
            // Toggle off (close screen)
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }
}
