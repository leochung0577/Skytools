package net.leo.Skytools.reader;

import net.leo.Skytools.Skytools;
import net.leo.Skytools.util.GameState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.SystemMessageReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Skytools.MODID, value = Dist.CLIENT)
public class ChatReader {

    @SubscribeEvent
    public static void onChatReceived(SystemMessageReceivedEvent event) {
        if (event.isOverlay()) return;
        String message = event.getMessage().getString();

        if (message.contains("[Skytools]") || !GameState.isInSkyblock) return;

//        System.out.println(message);
    }
}