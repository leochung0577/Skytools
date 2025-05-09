package net.leo.Skytools.event;

import net.leo.Skytools.state.GameState;
import net.leo.Skytools.state.ToggleState;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class RenderFogEvent {

    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        if (ToggleState.RemoveFogToggle) {
            event.setNearPlaneDistance(0.0f);       // how close fog starts
            event.setFarPlaneDistance(700.0f);     // how far you can see (simulate no fog)
            event.setCanceled(true);                // prevent vanilla handling
        }
    }
}
