package net.leo.Skytools.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.leo.Skytools.Skytools;
import net.leo.Skytools.util.GameState;
import net.leo.Skytools.util.SkyCommand;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class SkytoolsMenu extends Screen {

    private final List<String> buttonNames = List.of(
            "Tp to Pest"
    );

    private final String menuName = "Skytools Menu";
    private Button menuKeyButton;
    private boolean menuKeyPressed = false;
    private KeyMapping menuKeyMapping;

    private final String commandName = "SkyCommand";
    private Button commandKeyButton;
    private boolean commandKeyPressed = false;
    private KeyMapping commandKeyMapping;

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


        this.menuKeyMapping = Skytools.getMenuKey();
        this.commandKeyMapping = Skytools.getCommandKey();
        int leftX = 100;
        int topY = 20;

        menuKeyButton = addRenderableWidget(Button.builder(
                Component.literal(menuKeyMapping.getTranslatedKeyMessage().getString()),
                button -> {
                    menuKeyPressed = true;
                    button.setMessage(Component.literal("> ... <"));
                }).bounds(leftX, topY + 5, 100, 20).build());

        commandKeyButton = addRenderableWidget(Button.builder(
                Component.literal(commandKeyMapping.getTranslatedKeyMessage().getString()),
                button -> {
                    commandKeyPressed = true;
                    button.setMessage(Component.literal("> ... <"));
                }).bounds(leftX, topY + 35, 100, 20).build());

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
                    mc.setScreen(new SkytogglesMenu());
                }
        ).bounds(10, this.height - 20 - 10, 75, 20).build());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (menuKeyMapping != null && menuKeyMapping.matches(keyCode, scanCode)) {
            // Toggle off (close screen)
            this.onClose();
            return true;
        }

        if (menuKeyPressed) {
            menuKeyPressed = false; // Stop waiting
            if (keyCode != GLFW.GLFW_KEY_ESCAPE) {
                menuKeyMapping.setKey(InputConstants.getKey(keyCode, scanCode));
                KeyMapping.resetMapping();
                menuKeyButton.setMessage(Component.literal(getDisplayText(menuKeyMapping)));
            }
            return true;
        } else if (commandKeyPressed) {
            if (keyCode != GLFW.GLFW_KEY_ESCAPE) {
                commandKeyMapping.setKey(InputConstants.getKey(keyCode, scanCode));
                KeyMapping.resetMapping();
                commandKeyButton.setMessage(Component.literal(getDisplayText(commandKeyMapping)));
                SkyCommand.setCommandKey(commandKeyMapping.getKey().getValue());
            }
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        final int keyBindX = 5;

        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);

        graphics.drawCenteredString(this.font, "Key Binds", 90, 10, 0xFFFFFF);
        graphics.drawCenteredString(this.font, menuName, keyBindX + getPixel(menuName), 30, 0xFFFFFF);
        graphics.drawCenteredString(this.font, commandName, keyBindX + getPixel(commandName), 60, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    public int getPixel(String text) {
        return text.length() * 3;
    }

    private String getDisplayText(KeyMapping key) {
        String keyName = key.getTranslatedKeyMessage().getString();
        boolean conflict = Skytools.isKeyConflict(key);
        if (conflict) {
            return "§c[ §f" + keyName + " §c]"; //* Show red warning like Minecraft
        }
        return keyName;
    }
}
