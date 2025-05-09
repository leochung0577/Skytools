package net.leo.Skytools.util;

import net.leo.Skytools.state.GameState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber
public class AutoCommand {
    private static final Minecraft mc = Minecraft.getInstance();
    private static String commandText = "warp garden"; // Default command, can be changed dynamically later

    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event) {
        if (mc.player == null || event.getAction() != GLFW.GLFW_PRESS) return;
        if (event.getKey() == GameState.commandKey.getKey().getValue() && mc.screen == null) {
            sendChatCommand(commandText);
        }
    }

    public static void sendClientChatMessage(String message) {
        if (mc.player != null) {
            mc.player.displayClientMessage(Component.literal(message),false);
        }
    }

    public static void sendChatCommand(String command) {
        LocalPlayer player = mc.player;
        if (player != null && !command.isEmpty()) {
            player.connection.sendCommand(command); // Sends as a chat command
        }
    }

    public static void setCommandText(String newCommand) {
        commandText = newCommand; // Update command dynamically
    }
}
