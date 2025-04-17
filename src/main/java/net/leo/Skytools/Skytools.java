package net.leo.Skytools;

import net.leo.Skytools.config.SkyConfig;
import net.leo.Skytools.gui.EquipmentInventory;
import net.leo.Skytools.gui.SkytoolsMenu;
import net.leo.Skytools.hud.SkyOverlay;
import net.leo.Skytools.util.GameState;
import net.leo.Skytools.util.StoreItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(Skytools.MODID)
public class Skytools {

    public static final String MODID = "skytools";
    public static final Logger LOGGER = LogManager.getLogger();

    public Skytools() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::registerKeybinds);

        ModLoadingContext.get().registerConfig(
                ModConfig.Type.CLIENT,
                SkyConfig.SPEC,
                "Skytools/SkytoolsConfig.toml"
        );

        MinecraftForge.EVENT_BUS.register(this);

        // static function
        MinecraftForge.EVENT_BUS.register(net.leo.Skytools.event.onLoginEvent.class);
        MinecraftForge.EVENT_BUS.register(net.leo.Skytools.event.RenderFogEvent.class);
        MinecraftForge.EVENT_BUS.register(EquipmentInventory.class);

        new net.leo.Skytools.reader.ChatReader();

        LOGGER.info("Skytools mod loaded!");
    }


    private void onClientSetup(final FMLClientSetupEvent event) {
        Minecraft.getInstance().execute(() -> {
            Gui gui = Minecraft.getInstance().gui;
            LayeredDraw layeredDraw = gui.layers;
            layeredDraw.add(new SkyOverlay());

            //toggles
            GameState.RemoveFogToggle = SkyConfig.TOGGLE_MAP.get("Remove Fog").get();
            GameState.showPesthud = SkyConfig.TOGGLE_MAP.get("Pest Hud").get();
            GameState.showYawPitch = SkyConfig.TOGGLE_MAP.get("Yaw/Pitch Hud").get();
            GameState.displayPet = SkyConfig.TOGGLE_MAP.get("Pet Display").get();

            //equipment data
            for (int i = 0; i < 4; i++) {
                GameState.equipment[i] = StoreItem.getItem("equipment", "equipment.nbt", "slot" + (i + 1));
            }

            // storage data
            for (int storageId = 1; storageId <= 29; storageId++) {
                String path = "storage/" + storageId;

                // Only proceed if this storage folder actually exists
                if (!StoreItem.fileExists(path)) continue;

                int slotCount = StoreItem.getSlotCount(path);
                if (slotCount == 0) continue;

                List<ItemStack> contents = new ArrayList<>();
                for (int i = 0; i < slotCount; i++) {
                    String key = "slot" + (i + 1);
                    contents.add(StoreItem.getItem(path, "items.nbt", key));
                }

                GameState.saveStorageItems(storageId, contents);
            }
        });
    }

    private void registerKeybinds(final RegisterKeyMappingsEvent event) {
        event.register(GameState.menuKey);
        event.register(GameState.commandKey);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        if (GameState.menuKey != null && GameState.menuKey.consumeClick()) {
            Minecraft mc = Minecraft.getInstance();
            mc.setScreen(new SkytoolsMenu());
        }
    }

    @SubscribeEvent
    public void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof PauseScreen pauseScreen) {
            int x = 10;
            int y = pauseScreen.height - 30;

            Button skyToolsButton = Button.builder(
                    net.minecraft.network.chat.Component.literal("Skytools"),
                    btn -> Minecraft.getInstance().setScreen(new SkytoolsMenu())
            ).bounds(x, y, 100, 20).build();

            event.addListener(skyToolsButton);
        }
    }

    public static boolean isKeyConflict(KeyMapping mapping) {
        for (KeyMapping other : Minecraft.getInstance().options.keyMappings) {
            if (other != mapping && mapping.same(other)) {
                return true;
            }
        }
        return false;
    }
}
