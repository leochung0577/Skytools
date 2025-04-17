package net.leo.Skytools.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.leo.Skytools.obj.Pest;
import net.leo.Skytools.obj.Pet;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameState {
    // Keybinds
    public static final KeyMapping menuKey = new KeyMapping(
            "Skytools Menu",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "Skytools"
    );
    public static final KeyMapping commandKey = new KeyMapping(
            "Auto Command",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "Skytools"
    );

    //data
    public static String currentLocation = "";
    public static Pet currentPet = new Pet();
    public static Pest currentPest = new Pest();

    // storage
    public static final Map<Integer, List<ItemStack>> storageItems = new HashMap<>();

    // equipment
    public static ItemStack[] equipment = new ItemStack[4];

    //location chcker
    public static boolean isInSkyblock = false;

    //toggles
    public static boolean RemoveFogToggle = false;
    public static boolean showPesthud = false;
    public static boolean showYawPitch = false;
    public static boolean displayPet = false;

    // keys
    public static String petKey = "";
    public static String pestKey = "";

    public static Map<String, String> playerDisplayMap = new HashMap<>();

    public static void resetAll() {
        isInSkyblock = false;
        currentLocation = "";
        currentPet = new Pet();
        currentPest = new Pest();

        petKey = "";
        pestKey = "";
    }

    public static void resetKeys() {
        petKey = "";
        pestKey = "";
    }

    public static void updatePlayerTabMap(Map<String, String> newMap) {
        playerDisplayMap.putAll(newMap);
        updateGameState();
    }

    public static void updateGameState() {
        if(displayPet) {
            SkyGet.updatePet();
        }

        if(isInGarden() || showPesthud) {
            SkyGet.updatePest();
        }
    }

    public static void printPlayerDisplayMap() {
        if (playerDisplayMap.isEmpty()) {
            System.out.println("[Skytools] Player Display Map is empty.");
            return;
        }

        System.out.println("[Skytools] Player Display Map:");
        for (Map.Entry<String, String> entry : playerDisplayMap.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }

    public static boolean isInGarden() {
        return currentLocation.equals("Garden");
    }

    public static void saveStorageItems(int storageId, List<ItemStack> items) {
        storageItems.put(storageId, items);
    }

    public static List<ItemStack> getStorageItems(int storageId) {
        return storageItems.get(storageId);
    }
}
