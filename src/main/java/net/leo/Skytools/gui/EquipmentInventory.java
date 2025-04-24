package net.leo.Skytools.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.leo.Skytools.util.AutoCommand;
import net.leo.Skytools.util.GameState;
import net.leo.Skytools.util.FileManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber
public class EquipmentInventory {

    private static final Minecraft mc = Minecraft.getInstance();

    private static final ResourceLocation EQUIPMENT_TEXTURE = ResourceLocation.fromNamespaceAndPath("skytools",
            "textures/inventory/equipment_slot.png");
    private static final ResourceLocation INVENTORY_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft",
            "textures/gui/container/inventory.png");

    // Tweak constants for positioning
    private static final int SLOT_RENDER_OFFSET_X = 8;
    private static final int SLOT_RENDER_OFFSET_Y = 8;
    private static final int SLOT_SPACING = 18;

    private static Button equipmentButton;

    @SubscribeEvent
    public static void onGuiInit(ScreenEvent.Init.Post event) {
        if (!(event.getScreen() instanceof InventoryScreen invScreen) || !GameState.isInSkyblock) return;

        int guiLeft = (invScreen.width - 176) / 2;
        int guiTop = (invScreen.height - 166) / 2;

        int equipmentButtonX = guiLeft - 12;
        int equipmentButtonY = guiTop + 7;
        int size = 18;

        equipmentButton = Button.builder(Component.literal(""), btn -> {
            AutoCommand.sendChatCommand("equipment");
        }).bounds(equipmentButtonX, equipmentButtonY, size, size + 54).build();

        event.addListener(equipmentButton);
    }

    @SubscribeEvent
    public static void onGuiRender(ScreenEvent.Render.Post event) {
        if (!(event.getScreen() instanceof InventoryScreen invScreen) || !GameState.isInSkyblock) return;

        PoseStack pose = event.getGuiGraphics().pose();
        GuiGraphics gfx = event.getGuiGraphics();

        int guiLeft = (invScreen.width - 176) / 2;
        int guiTop = (invScreen.height - 166) / 2;

        int x = guiLeft - 19;
        int y = guiTop;

        RenderSystem.setShaderTexture(0, INVENTORY_TEXTURE);

        // Draw the equipment slot background
        gfx.blit(RenderType::guiTextured,
                EQUIPMENT_TEXTURE,
                x,
                y,
                0f,
                0f,
                50 / 2,
                172 / 2,
                50 / 2,
                172 / 2);

        int startX = x + SLOT_RENDER_OFFSET_X;
        int startY = y + SLOT_RENDER_OFFSET_Y;

        for (int i = 0; i < GameState.equipment.length; i++) {
            ItemStack stack = GameState.equipment[i];
            int itemX = startX;
            int itemY = startY + i * SLOT_SPACING;

            if (!stack.isEmpty()) {
                gfx.renderItem(stack, itemX, itemY);
                gfx.renderItemDecorations(mc.font, stack, itemX, itemY);

                int mouseX = event.getMouseX();
                int mouseY = event.getMouseY();

                // Handle tooltip if hovered
                if (isMouseOver(mouseX, mouseY, itemX, itemY, 16, 16)) {
                    Item.TooltipContext ctx = Item.TooltipContext.EMPTY;
                    TooltipFlag flag = mc.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL;

                    List<Component> tooltip = stack.getTooltipLines(ctx, mc.player, flag);
                    gfx.renderTooltip(
                            mc.font,
                            tooltip,
                            stack.getTooltipImage(),
                            stack,
                            mouseX,
                            mouseY
                    );
                }
            }
        }
    }

    @SubscribeEvent
    public static void onScreenClose(ScreenEvent.Closing event) {
        if (!(event.getScreen() instanceof AbstractContainerScreen<?> screen)) return;

        String name = screen.getTitle().getString();

        if (name.equals("Your Equipment and Stats")) {
            List<Slot> slots = screen.getMenu().slots;
            int[] targetIndices = {10, 19, 28, 37}; // Column 2, rows 2–5

            Map<String, ItemStack> equipmentMap = new HashMap<>();

            for (int i = 0; i < targetIndices.length; i++) {
                int slotIndex = targetIndices[i];
                String key = "slot" + (i + 1);

                if (slotIndex >= 0 && slotIndex < slots.size()) {
                    ItemStack stack = slots.get(slotIndex).getItem();

                    if (!stack.isEmpty()) {
                        equipmentMap.put(key, stack.copy());
                        GameState.equipment[i] = stack.copy();
                    }
                }
            }

            FileManager.saveAllItems("equipment", "equipment.nbt", equipmentMap);
        }
    }


    private static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < (x + width) && mouseY >= y && mouseY < (y + height);
    }
}
