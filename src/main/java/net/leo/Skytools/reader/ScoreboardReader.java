package net.leo.Skytools.reader;

import net.minecraft.client.Minecraft;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;


public class ScoreboardReader {
    private static String title = "";
    private static final Minecraft mc = Minecraft.getInstance();

    public static void updateScoreboardTitle() {
        title = "";
        if (mc.level == null) return;

        Scoreboard scoreboard = mc.level.getScoreboard();
        Objective sidebarObjective = scoreboard.getDisplayObjective(DisplaySlot.SIDEBAR);

        if (sidebarObjective != null) {
            title = sidebarObjective.getDisplayName().getString().trim();
            System.out.println("Sidebar Title: " + title);
        } else {
            System.out.println("No sidebar scoreboard currently displayed.");
        }
    }

    public static String getFirstWord(String title) {
        updateScoreboardTitle();
        return title.split(" ")[0];
    }

    public static String getTitle() {
        updateScoreboardTitle();
        return title;
    }

    public static boolean isInSkyblock() {
        updateScoreboardTitle();

//        SkyCommand.sendClientChatMessage("Location: " + title);
        if (title.equals("SKYBLOCK CO-OP")) return true;
        if (title.equals("SKYBLOCK")) return true;
        return false;
    }
}
