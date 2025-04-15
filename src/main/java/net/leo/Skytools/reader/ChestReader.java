package net.leo.Skytools.reader;

import net.leo.Skytools.util.GameState;
import net.leo.Skytools.util.StoreItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class ChestReader {

    @SubscribeEvent
    public static void onScreenOpen(ScreenEvent.Opening event) {
        if (!(event.getNewScreen() instanceof AbstractContainerScreen<?> screen)) return;

        String name = screen.getTitle().getString();
        Minecraft mc = Minecraft.getInstance();

//        System.out.println(name);
        if (name.equals("Your Equipment and Stats")) {
            new Thread(() -> {
                try {
                    Thread.sleep(500);

                    // Access the menu and its slots
                    List<Slot> slots = screen.getMenu().slots;

                    int[] targetIndices = {10, 19, 28, 37}; // Column 2, rows 2–5

                    for (int index = 0; index < targetIndices.length; index++) {
                        int slotIndex = targetIndices[index];
                        String key = "slot" + (index + 1);

                        if (slotIndex >= 0 && slotIndex < slots.size()) {
                            ItemStack stack = slots.get(slotIndex).getItem();

                            if (!stack.isEmpty()) {
                                // Store the item using StoreItem
                                StoreItem.saveItem("equipment", key, stack);

                                GameState.equipment[index] = stack.copy();
                            }
                        }
                    }

                } catch (InterruptedException e) {
                    System.out.println("Error Sleeping");
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }
}
