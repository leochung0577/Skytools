package net.leo.Skytools.hud;

import net.leo.Skytools.renderer.ManaBarRenderer;
import net.leo.Skytools.state.DisplayState;
import net.leo.Skytools.state.GameState;
import net.leo.Skytools.state.ToggleState;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraftforge.client.event.SystemMessageReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManaBar implements LayeredDraw.Layer {
    private static final Minecraft mc = Minecraft.getInstance();

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (mc.player == null || mc.screen != null || !ToggleState.displayManaBar
                || !GameState.isInSkyblock
        ) return;

        ManaBarRenderer.renderManaBar(guiGraphics, DisplayState.manaBarX, DisplayState.manaBarY, DisplayState.manaBarSize);
    }

    @SubscribeEvent
    public static void onChatReceived(SystemMessageReceivedEvent event) {
        String message = event.getMessage().getString();
        if(!event.isOverlay() || !ToggleState.displayManaBar || !GameState.isInSkyblock) return;

        String statsMessage = event.getMessage().getString();
        String manaRegex = "§b(\\d{1,3}(?:,\\d{3})*)/(\\d{1,3}(?:,\\d{3})*)";
        Pattern pattern = Pattern.compile(manaRegex);
        Matcher matcher = pattern.matcher(statsMessage);

        if (matcher.find()) {
            GameState.currentMana = Integer.parseInt(matcher.group(1).replace(",", ""));
            GameState.maxMana = Integer.parseInt(matcher.group(2).replace(",", ""));
        } else {
            GameState.currentMana = 0;
        }
    }
}
