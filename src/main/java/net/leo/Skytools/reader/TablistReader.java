package net.leo.Skytools.reader;

import net.leo.Skytools.state.GameState;
import net.leo.Skytools.util.AutoCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class TablistReader {
    private static String header = "";
    private static List<List<Component>> tabSiblingList = new ArrayList<>();
    private static List<String> tabList = new ArrayList<>();
    private static String footer = "";

    public static final Logger LOGGER = LogManager.getLogger();

    public static void UpdateTabPlayerList() {
        ClientPacketListener connection = Minecraft.getInstance().player.connection;
        tabSiblingList.clear();

        for (PlayerInfo playerInfo : connection.getOnlinePlayers()) {
            Component component = playerInfo.getTabListDisplayName();
            if (component != null && !component.getSiblings().isEmpty()) {
                List<Component> componentsSiblings = new ArrayList<>(component.getSiblings());
                if(!componentsSiblings.toString().isBlank()) {
                    tabSiblingList.add(componentsSiblings);
                }
            }
        }
    }

    public static Map<String, String> getPlayerTabMap() {
        Map<String, String> result = new HashMap<>();

        ClientPacketListener connection = Minecraft.getInstance().player.connection;
        for (PlayerInfo playerInfo : connection.getOnlinePlayers()) {
            Component displayComponent = playerInfo.getTabListDisplayName();
            if (displayComponent != null && !displayComponent.getSiblings().isEmpty()) {
                String cleaned = cleanString(displayComponent.getSiblings().toString());
                String username = playerInfo.getProfile().getName().trim();
                result.put(username, cleaned);

                updateValues(cleaned, username);
            }
        }

        return result;
    }

    public static void updateValues(String line, String key) {
        if(line.startsWith("Area:")) {
            GameState.currentLocation = extractLocation(line);
        }
        if(line.startsWith("Pet:")) {
            GameState.petKey = key;
        }
        if(line.startsWith("Pests:")) {
            GameState.pestKey = key;
        }
    }

    private static String extractLocation(String line) {
        int index = line.indexOf("§7");
        return (index != -1) ? line.substring(index + 2).trim() : line.substring(5).trim();
    }

    // testing to see the playername and its tablistname
    public static List<String> getTabPlayerListWithCleanedText() {
        List<String> combinedList = new ArrayList<>();
        ClientPacketListener connection = Minecraft.getInstance().player.connection;

        for (PlayerInfo playerInfo : connection.getOnlinePlayers()) {
            Component component = playerInfo.getTabListDisplayName();
            String name = playerInfo.getProfile().getName();
            if (component != null && !component.getSiblings().isEmpty()) {
                List<Component> componentsSiblings = new ArrayList<>(component.getSiblings());
                String cleaned = cleanString(componentsSiblings.toString());

                combinedList.add(name + " = " + cleaned);
            }
        }

        return combinedList;
    }

    public static void UpdateTabList() {
        UpdateTabPlayerList();
        tabList.clear();

        for (List<Component> lists : tabSiblingList) {
            String titleName = cleanString(lists.toString());
            if (titleName.isBlank()) continue;
            tabList.add(titleName);
        }
    }

    public static List<List<Component>> getRawTablist() {
        UpdateTabPlayerList();
        return tabSiblingList;
    }

    public static List<String> getRawList() {
        UpdateTabList();
        return tabList;
    }

    public static void printList() {
        UpdateTabPlayerList();
        List<String> StringListNames = new ArrayList<>();
        for (List<Component> lists : tabSiblingList) {
            String titleName = cleanString(lists.toString());
            if (titleName.isBlank()) continue;
            StringListNames.add(titleName);
        }

        for(String line: StringListNames) {
            AutoCommand.sendClientChatMessage(line);
        }
    }

    public static void logComponentNList() {
        List<String> tabs = getTabPlayerListWithCleanedText();
        for (String lines : tabs) {
            System.out.println(lines);
        }
    }

    public static void logRawList() {
        UpdateTabPlayerList();
        List<String> StringListNames = new ArrayList<>();
        for (List<Component> lists : tabSiblingList) {
            String titleName = lists.toString();
            if (titleName.isBlank()) continue;
            StringListNames.add(titleName);
        }
        for(String line: StringListNames) {
            System.out.println(line);
        }
    }

    public static void logList() {
        UpdateTabPlayerList();
        List<String> StringListNames = new ArrayList<>();
        for (List<Component> lists : tabSiblingList) {
            String titleName = cleanString(lists.toString());
            if (titleName.isBlank()) continue;
            StringListNames.add(titleName);
        }

        for(String line: StringListNames) {
            System.out.println(line);
        }
    }

    public static List<String> getList() {
        UpdateTabPlayerList();
        List<String> StringListNames = new ArrayList<>();
        for (List<Component> lists : tabSiblingList) {
            String titleName = cleanString(lists.toString());
            if (titleName.isBlank()) continue;
            StringListNames.add(titleName);
        }

        return StringListNames;
    }

    public static String cleanString(String input) {
        if (input == null || input.isEmpty()) return "";

        StringBuilder result = new StringBuilder();

        // Pattern to match: literal{value}[style={color=colorName}]
        Pattern pattern = Pattern.compile("literal\\{(.*?)}(?:\\[style=\\{color=(.*?)}])?");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String text = matcher.group(1);
            String color = matcher.group(2);

            if (color != null) {
                result.append(getColorCode(color)); // append § color code
            }
            result.append(text); // append the actual literal text
        }
        return result.toString().trim();
    }

    private static String getColorCode(String colorName) {
        return switch (colorName.toLowerCase()) {
            case "black" -> "§0";
            case "dark_blue" -> "§1";
            case "dark_green" -> "§2";
            case "dark_aqua" -> "§3";
            case "dark_red" -> "§4";
            case "dark_purple" -> "§5";
            case "gold" -> "§6";
            case "gray" -> "§7";
            case "dark_gray" -> "§8";
            case "blue" -> "§9";
            case "green" -> "§a";
            case "aqua" -> "§b";
            case "red" -> "§c";
            case "light_purple" -> "§d";
            case "yellow" -> "§e";
            case "white" -> "§f";
            default -> ""; // fallback if unrecognized
        };
    }


}
