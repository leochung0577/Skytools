package net.leo.Skytools.util;

import net.minecraftforge.fml.loading.FMLPaths;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeybindsUtil {
    private static final Path keybindFile = FMLPaths.CONFIGDIR.get().resolve("Skytools/keybinds.txt");

    public static Map<String, Integer> loadKeybinds() {
        Map<String, Integer> map = new HashMap<>();
        if (!Files.exists(keybindFile)) return map;

        try {
            List<String> lines = Files.readAllLines(keybindFile);
            for (String line : lines) {
                if (!line.contains("=")) continue;
                String[] parts = line.split("=");
                if (parts.length != 2) continue;

                String action = parts[0].trim();
                String keyName = parts[1].trim();

                int glfwKey = getGLFWKeyFromName(keyName);
                if (glfwKey != -1) {
                    map.put(action, glfwKey);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private static int getGLFWKeyFromName(String name) {
        try {
            Field field = GLFW.class.getField(name);
            return field.getInt(null);
        } catch (Exception e) {
            System.err.println("Invalid key name in keybinds.txt: " + name);
            return -1;
        }
    }

    public static void saveDefaultKeybindsIfMissing() {
        if (Files.exists(keybindFile)) return;

        try {
            Files.createDirectories(keybindFile.getParent());
            Files.write(keybindFile, List.of(
                    "openMenu=GLFW_KEY_Z",
                    "command=GLFW_KEY_PAGE_DOWN"
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveKeybinds() {
        if (GameState.menuKey == null || GameState.commandKey == null) return;

        try {
            Files.createDirectories(keybindFile.getParent());

            List<String> lines = List.of(
                    "openMenu=" + getGLFWConstantName(GameState.menuKey.getKey().getValue()),
                    "command=" + getGLFWConstantName(GameState.commandKey.getKey().getValue())
            );

            Files.write(keybindFile, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getGLFWConstantName(int keyCode) {
        for (Field field : GLFW.class.getFields()) {
            if (field.getType() == int.class) {
                try {
                    if (field.getInt(null) == keyCode) {
                        return field.getName();
                    }
                } catch (IllegalAccessException ignored) {}
            }
        }
        return "GLFW_KEY_UNKNOWN";
    }

}
