package net.leo.Skytools.renderer;

import net.leo.Skytools.obj.Pest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public class PestRenderer {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void renderPestDisplay(GuiGraphics guiGraphics, int x, int y, double size) {
        Pest pest = new Pest();

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale((float) size, (float) size, 1.0f);

        // Now draw pest info at (0,0) since we've already translated
        guiGraphics.drawString(
                mc.font,
                Component.literal("§4§lPest: "),
                0,
                0,
                0xFFFFFF,
                true
        );

        List<String> info = List.of(
                "Alive: §4" + pest.alive,
                "Plots: " + pest.getPlots(),
                "Spray: " + pest.spray,
                "Repellent: " + pest.repellent,
                "Bonus: " + pest.bonus,
                "Cooldown: " + pest.cooldown
        );

        for (int i = 0; i < info.size(); i++) {
            guiGraphics.drawString(
                    Minecraft.getInstance().font,
                    Component.literal(info.get(i)),
                    10,
                    (i + 1) * 10, // vertical spacing
                    0xFFFFFF,
                    true
            );
        }

        guiGraphics.pose().popPose();
    }

    public static void renderPestDisplay(GuiGraphics guiGraphics, int x, int y, double size, Pest pest) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale((float) size, (float) size, 1.0f);

        // Now draw pest info at (0,0) since we've already translated
        guiGraphics.drawString(
                mc.font,
                Component.literal("§4§lPest: "),
                0,
                0,
                0xFFFFFF,
                true
        );

        List<String> info = List.of(
                "Alive: §4" + pest.alive,
                "Plots: " + pest.getPlots(),
                "Spray: " + pest.spray,
                "Repellent: " + pest.repellent,
                "Bonus: " + pest.bonus,
                "Cooldown: " + pest.cooldown
        );

        for (int i = 0; i < info.size(); i++) {
            guiGraphics.drawString(
                    Minecraft.getInstance().font,
                    Component.literal(info.get(i)),
                    10,
                    (i + 1) * 10, // vertical spacing
                    0xFFFFFF,
                    true
            );
        }

        guiGraphics.pose().popPose();
    }

}
