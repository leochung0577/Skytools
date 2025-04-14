package net.leo.Skytools.util;

import net.leo.Skytools.Skytools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber
public class SkyCommand {
    private static final Minecraft mc = Minecraft.getInstance();
    private static int commandKeyValue = Skytools.getCommandKey() != null ? Skytools.getCommandKey().getKey().getValue() : GLFW.GLFW_KEY_PAGE_DOWN;
    private static String commandText = "warp garden"; // Default command, can be changed dynamically later

    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event) {
        if (mc.player == null || event.getAction() != GLFW.GLFW_PRESS) return;
        if (event.getKey() == commandKeyValue && mc.screen == null) {
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

    public static void setCommandKey(int key) {
        commandKeyValue = key;
    }
}
