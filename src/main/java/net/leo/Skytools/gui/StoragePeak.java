package net.leo.Skytools.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.leo.Skytools.util.GameState;
import net.leo.Skytools.util.StoreItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mod.EventBusSubscriber
public class StoragePeak {

    private static final Minecraft mc = Minecraft.getInstance();

    private static final ResourceLocation STORAGE_TOP = ResourceLocation.fromNamespaceAndPath("skytools",
            "textures/storage/storage_top.png");
    private static final ResourceLocation STORAGE_ROW = ResourceLocation.fromNamespaceAndPath("skytools",
            "textures/storage/storage_row.png");
    private static final ResourceLocation STORAGE_BOTTOM = ResourceLocation.fromNamespaceAndPath("skytools",
            "textures/storage/storage_bottom.png");

    private static boolean isValidStorage = false;

    @SubscribeEvent
    public static void onTooltipRender(RenderTooltipEvent.Pre event) {
        if (!(Minecraft.getInstance().screen instanceof AbstractContainerScreen<?> screen) || !GameState.isInSkyblock)
            return;

        if(!isValidStorage) return;
        event.setCanceled(true);
    }


    @SubscribeEvent
    public static void onGuiRender(ScreenEvent.Render.Post event) {
        if (!(event.getScreen() instanceof AbstractContainerScreen<?> screen) || !GameState.isInSkyblock) {
            isValidStorage = false;
            return;
        }
        String name = screen.getTitle().getString();
        if (!name.equals("Storage")) {
            isValidStorage = false;
            return;
        }


        GuiGraphics graphics = event.getGuiGraphics();
        double mouseX = event.getMouseX();
        double mouseY = event.getMouseY();

        int guiLeft = screen.getGuiLeft();
        int guiTop = screen.getGuiTop();
        int storageId = 1;

        RenderSystem.disableDepthTest();

        for (Slot slot : screen.getMenu().slots) {
            int index = slot.getSlotIndex();
            if (index < 9 || (index >= 18 && index <= 26)) continue;
            if (storageId > 29) break;

            int slotX = guiLeft + slot.x;
            int slotY = guiTop + slot.y;

            if (isMouseOver(mouseX, mouseY, slotX, slotY, 16, 16)) {
                List<ItemStack> stacks = GameState.getStorageItems(storageId);
                if (stacks == null || stacks.size() == 0) {
                    isValidStorage = false;
                    return;
                }
                isValidStorage = true;

                int columns = 9;
                int rows = (int) Math.ceil(stacks.size() / (double) columns);

                int boxX = (int) mouseX + 15;
                int boxY = (int) mouseY - rows * 36 / 2 / 2;

                int storageX = boxX - 16 / 2;
                int storageY = boxY - 16 / 2;

                graphics.pose().pushPose();
                graphics.pose().translate(0, 0, 400); // +Z pushes forward (above GUI)

                graphics.blit(RenderType::guiTextured,
                        STORAGE_TOP,
                        storageX,
                        storageY,
                        0f,
                        0f,
                        352 / 2,
                        14 / 2,
                        352 / 2,
                        14 / 2);
                int lastRow = -1;
                // Push pose
// draw items
                for (int i = 0; i < stacks.size(); i++) {
                    ItemStack stack = stacks.get(i);

                    int row = i / columns;
                    int col = i % columns;

                    if (row != lastRow) {
                        lastRow = row;
                        graphics.blit(RenderType::guiTextured,
                                STORAGE_ROW,
                                storageX,
                                storageY + (row * 36 / 2 + 14 / 2),
                                0f,
                                0f,
                                352 / 2,
                                36 / 2,
                                352 / 2,
                                36 / 2);
                    }
                    if (stack == null || stack.isEmpty()) continue;

                    int x = boxX + col * 18;
                    int y = boxY + row * 18;

                    graphics.renderItem(stack, x, y);
                    graphics.renderItemDecorations(Minecraft.getInstance().font, stack, x, y);
                }

                graphics.blit(RenderType::guiTextured,
                        STORAGE_BOTTOM,
                        storageX,
                        storageY + rows * 36 / 2 + 14 / 2,
                        0f,
                        0f,
                        352 / 2,
                        14 / 2,
                        352 / 2,
                        14 / 2);

                graphics.pose().popPose();
                break;
            }

            storageId++;
        }
    }

    @SubscribeEvent
    public static void onScreenClose(ScreenEvent.Closing event) {
        if (!(event.getScreen() instanceof AbstractContainerScreen<?> screen)) return;

        String name = screen.getTitle().getString();
        int storageId = isStorage(name);
        if (storageId == -1) return;

        // This is the final capture of all items before closing
        List<Slot> slots = screen.getMenu().slots;
        List<ItemStack> storageContent = new ArrayList<>();
        for (int i = 9, index = 0; i < slots.size() - 36; i++, index++) {
            ItemStack stack = slots.get(i).getItem();
            String key = "slot" + (index + 1);

            ItemStack item;
            if (!stack.isEmpty()) {
                item = stack.copy();
                item.remove(DataComponents.ENCHANTMENTS);
                StoreItem.saveFile("storage/" + storageId, "items.nbt", key, item);
//                storageContent[index] = .copy();
            }
            storageContent.add(stack.copy());
        }

        GameState.saveStorageItems(storageId, storageContent);
        StoreItem.saveSlotCount("storage/" + storageId, "slotCount.txt", storageContent.size());

    }

    public static int isStorage(String name) {
        if (name.contains("Ender Chest")) {
            // Match pattern like: "Ender Chest (1/3)"
            Matcher matcher = Pattern.compile("\\((\\d+)/\\d+\\)").matcher(name);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }
        }

        if (name.contains("Backpack")) {
            // Match: "Jumbo Backpack (Slot #18)"
            Matcher matcher = Pattern.compile("Slot #?(\\d+)").matcher(name);
            if (matcher.find()) {
                int slotNumber = Integer.parseInt(matcher.group(1));
                return 9 + slotNumber;
            }
        }

        return -1;
    }

    private static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < (x + width) && mouseY >= y && mouseY < (y + height);
    }
}
