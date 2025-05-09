package net.leo.Skytools;

import net.leo.Skytools.config.DisplayConfig;
import net.leo.Skytools.config.SkyConfig;
import net.leo.Skytools.gui.SkytoolsMenu;
import net.leo.Skytools.hud.ManaBar;
import net.leo.Skytools.hud.Overlay;
import net.leo.Skytools.state.GameState;
import net.leo.Skytools.state.ToggleState;
import net.leo.Skytools.util.FileManager;
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
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SkyConfig.SPEC, "Skytools/SkytoolsConfig.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, DisplayConfig.SPEC, "Skytools/display-config.toml");

        loadEvents();

        LOGGER.info("Skytools mod loaded!");
    }

    private void loadEvents() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::registerKeybinds);

        MinecraftForge.EVENT_BUS.register(this);

        // static events
        MinecraftForge.EVENT_BUS.register(net.leo.Skytools.event.onLoginEvent.class);
        MinecraftForge.EVENT_BUS.register(net.leo.Skytools.event.RenderFogEvent.class);
        MinecraftForge.EVENT_BUS.register(net.leo.Skytools.gui.EquipmentInventory.class);
        MinecraftForge.EVENT_BUS.register(net.leo.Skytools.hud.ManaBar.class);

        // non-static events
        new net.leo.Skytools.reader.ChatReader();
    }

    private void loadstate() {
        // Toggles
        ToggleState.RemoveFogToggle = SkyConfig.TOGGLE_MAP.get("Remove Fog").get();
        ToggleState.showPesthud = SkyConfig.TOGGLE_MAP.get("Display Pest Hud").get();
        ToggleState.showYawPitch = SkyConfig.TOGGLE_MAP.get("Display Yaw/Pitch Hud").get();
        ToggleState.displayPet = SkyConfig.TOGGLE_MAP.get("Display Pet Hud").get();
        ToggleState.displayCords = SkyConfig.TOGGLE_MAP.get("Display Cords Hud").get();
        ToggleState.displayManaBar = SkyConfig.TOGGLE_MAP.get("Display Mana Bar").get();

        //
    }



    private void onClientSetup(final FMLClientSetupEvent event) {
        Minecraft.getInstance().execute(() -> {
            Gui gui = Minecraft.getInstance().gui;
            LayeredDraw layeredDraw = gui.layers;
            layeredDraw.add(new Overlay());
            layeredDraw.add(new ManaBar());

            loadstate();

            //equipment data
            for (int i = 0; i < 4; i++) {
                GameState.equipment[i] = FileManager.getItem("equipment", "equipment.nbt", "slot" + (i + 1));
            }

            // storage data
            for (int storageId = 1; storageId <= 29; storageId++) {
                String itemPath = "storage/storage" + storageId + ".nbt";
                String countPath = "storage/slotCount" + storageId + ".txt";

                // Skip if either file doesn't exist
                if (!FileManager.fileExists(itemPath) || !FileManager.fileExists(countPath)) continue;

                int slotCount = FileManager.getSlotCount("storage", "slotCount" + storageId + ".txt");
                if (slotCount == 0) continue;

                List<ItemStack> contents = new ArrayList<>();
                for (int i = 1; i <= slotCount; i++) {
                    String key = "slot" + i;
                    contents.add(FileManager.getItem("storage", "storage" + storageId + ".nbt", key));
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
