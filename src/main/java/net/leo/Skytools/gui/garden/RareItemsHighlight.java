package net.leo.Skytools.gui.garden;

import net.leo.Skytools.state.GameState;
import net.leo.Skytools.state.ToggleState;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Set;

public class RareItemsHighlight {
    private static AbstractContainerScreen<?> pendingScreen = null;
    private static boolean rareItem = false;
    private static final Set<String> rareItems = Set.of("Overgrown Grass", "Space Helmet");

    @SubscribeEvent
    public static void onGuiRender(ScreenEvent.Render.Post event) {
        if(!ToggleState.displayRareGardenOffers) return;
        if (!(event.getScreen() instanceof AbstractContainerScreen<?> containerScreen) && GameState.isInGarden()) return;
        if(!rareItem) return;
        GuiGraphics guiGraphics = event.getGuiGraphics();
        int x = event.getScreen().width / 2 - 45;
        int y = event.getScreen().height / 2 - 40;

        int size = 18;
        int borderThickness = 1;
        int color = 0xFF00FF00;

        guiGraphics.fill(x, y, x + size, y + borderThickness, color);
        guiGraphics.fill(x, y + size - borderThickness, x + size, y + size, color);
        guiGraphics.fill(x, y, x + borderThickness, y + size, color);
        guiGraphics.fill(x + size - borderThickness, y, x + size, y + size, color);

    }

    @SubscribeEvent
    public static void onScreenOpen(ScreenEvent.Opening event) {
        if(!ToggleState.displayRareGardenOffers) return;
        if (event.getNewScreen() instanceof AbstractContainerScreen<?> && GameState.isInGarden()) {
            rareItem = false;
            pendingScreen = (AbstractContainerScreen<?>) event.getNewScreen();
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && pendingScreen != null) {
            // This ensures we're on the client-side and the screen is initialized
            if (pendingScreen.getMenu() instanceof AbstractContainerMenu containerMenu) {
                // Wait one tick for sync

                ItemStack stack = containerMenu.slots.get(29).getItem();
                if (!stack.isEmpty() && stack.getHoverName().getString().trim().equals("Accept Offer")) {
                    // Access the components of the hover name (this will be a list of components)

                    for (TypedDataComponent<?> textComponent : stack.getComponents()) {
                        // Check if the component type matches "minecraft:lore"
                        if (textComponent.type().toString().equals("minecraft:lore")) {
                            Object value = textComponent.value();

                            // Ensure it's an ItemLore object
                            if (value instanceof ItemLore itemLore) {
                                List<Component> lines = itemLore.lines();

                                // Print each line as a string
                                for(String itemName : rareItems) {
                                    for (Component line : lines) {
                                        if (line.getString().trim().equals(itemName)) {
                                            rareItem = true;
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }

            // Reset pendingScreen after we've processed the items
            pendingScreen = null;
        }
    }
}
