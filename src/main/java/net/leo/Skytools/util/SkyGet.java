package net.leo.Skytools.util;

import net.leo.Skytools.obj.Pest;
import net.leo.Skytools.obj.Pet;
import net.leo.Skytools.state.GameState;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SkyGet {

    public static void updatePet() {
        Map<String, String> map = GameState.playerDisplayMap;

        String petKey = getNextKey(map, GameState.petKey, 1);
        String progressKey = getNextKey(map, GameState.petKey, 2);

        if (petKey != null && progressKey != null) {
            String petLine = map.get(petKey);
            String progressLine = map.get(progressKey);

            GameState.currentPet = extractPet(petLine, progressLine);
        }
    }

    public static void updatePest() {
        int alive = 0;
        String plots = "";
        String spray = "None";
        String repellent = "None";
        String bonus = "INACTIVE";
        String cooldown = "READY";

        String key = GameState.pestKey;
        Map<String, String> map = GameState.playerDisplayMap;

        for (int i = 1; i <= 6; i++) {
            key = getNextKey(map, key, 1);
            if (key == null) break;

            String line = map.get(key).trim();

            if (line.startsWith("Alive:")) {
                String value = line.substring(6 + 3).trim(); // + 3 is the "§4"
                try {
                    alive = Integer.parseInt(value);
                } catch (NumberFormatException ignored) {
                }
            } else if (line.startsWith("Plots:")) {
                plots = line.substring(6).trim();
            } else if (line.startsWith("Spray:")) {
                spray = line.substring(6).trim();
            } else if (line.startsWith("Repellent:")) {
                repellent = line.substring(10).trim();
            } else if (line.startsWith("Bonus:")) {
                bonus = line.substring(6).trim();
            } else if (line.startsWith("Cooldown:")) {
                cooldown = line.substring(9).trim();
            }
        }

        GameState.currentPest = new Pest(alive, plots, spray, repellent, bonus, cooldown);
    }

    private static String extractLocation(String line) {
        int index = line.indexOf("§7");
        return (index != -1) ? line.substring(index + 2).trim() : line.substring(5).trim();
    }

    private static Pet extractPet(String line, String progress) {
        // Matches [Lvl X], optional color (rarity), and pet name up until the next § or end of line
        Pattern pattern = Pattern.compile("§7\\[Lvl (\\d+)] (§.)?(.*)");
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            int level = Integer.parseInt(matcher.group(1));
            String rarity = matcher.group(2) != null ? matcher.group(2) : "§f";
            String petName = matcher.group(3).trim();
            return new Pet(petName, rarity, level, progress);
        }

        return new Pet();
    }

    // Utility to get the next tablist key (e.g., !B-l -> !B-m)
    private static String getNextKey(Map<String, String> map, String currentKey, int offset) {
        String[] parts = currentKey.split("-");
        if (parts.length != 2) return null;

        String row = parts[0]; // e.g., "!B"
        char column = parts[1].charAt(0);

        // Shift the column character by offset (e.g., 'l' -> 'm')
        char nextColumn = (char) (column + offset);
        String nextKey = row + "-" + nextColumn;

        return map.containsKey(nextKey) ? nextKey : null;
    }
}
