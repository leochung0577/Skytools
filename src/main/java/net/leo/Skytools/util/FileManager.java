package net.leo.Skytools.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class FileManager {

    private static final Map<String, Map<String, ItemStack>> data = new HashMap<>();

    private static String getCacheKey(String location, String fileName) {
        return location + "/" + fileName;
    }

    private static File getFile(String location, String fileName) {
        File dir = new File(FMLPaths.CONFIGDIR.get().toFile(), "Skytools/" + location);
        if (!dir.exists()) dir.mkdirs();
        return new File(dir, fileName);
    }

    public static void saveAllItems(String location, String fileName, Map<String, ItemStack> items) {
        String cacheKey = getCacheKey(location, fileName);
        data.put(cacheKey, new HashMap<>(items));
        saveToFile(location, fileName);
    }

    public static void saveFile(String location, String fileName, String key, ItemStack stack) {
        String cacheKey = getCacheKey(location, fileName);
        loadFromFile(location, fileName);
        data.computeIfAbsent(cacheKey, loc -> new HashMap<>()).put(key, stack.copy());
        saveToFile(location, fileName);
    }

    public static ItemStack getItem(String location, String fileName, String key) {
        String cacheKey = getCacheKey(location, fileName);
        loadFromFile(location, fileName);
        return data.getOrDefault(cacheKey, new HashMap<>()).getOrDefault(key, ItemStack.EMPTY);
    }

    public static void saveToFile(String location, String fileName) {
        String cacheKey = getCacheKey(location, fileName);
        File file = getFile(location, fileName);
        CompoundTag root = new CompoundTag();

        Map<String, ItemStack> items = data.getOrDefault(cacheKey, new HashMap<>());
        for (Map.Entry<String, ItemStack> entry : items.entrySet()) {
            String key = entry.getKey();
            ItemStack stack = entry.getValue();

            ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, stack)
                    .resultOrPartial(error -> {
                        System.err.println("Failed to encode item '" + key + "': " + error);
                        System.err.println("Stack: " + stack.toString());
                    })
                    .ifPresent(tag -> {
                        if (tag instanceof CompoundTag compound) {
                            root.put(key, compound);
                        } else {
                            System.err.println("Warning: Tag for '" + key + "' was not a CompoundTag. Skipping.");
                        }
                    });
        }

        try (OutputStream out = Files.newOutputStream(file.toPath())) {
            NbtIo.writeCompressed(root, out);
//            System.out.println("[Skytools:FileManager] Saved " + items.size() + " items to " + file.getPath());
        } catch (IOException e) {
            System.err.println("Failed to save file: " + file.getPath());
            e.printStackTrace();
        }
    }

    public static void loadFromFile(String location, String fileName) {
        String cacheKey = getCacheKey(location, fileName);
        if (data.containsKey(cacheKey)) return;

        File file = getFile(location, fileName);
        Map<String, ItemStack> map = new HashMap<>();

        if (file.exists()) {
            try {
                CompoundTag root = NbtIo.readCompressed(file.toPath(), NbtAccounter.unlimitedHeap());
                for (String key : root.getAllKeys()) {
                    CompoundTag tag = root.getCompound(key);
                    ItemStack stack = ItemStack.CODEC.parse(NbtOps.INSTANCE, tag)
                            .resultOrPartial(error -> System.err.println("Failed to parse item '" + key + "': " + error))
                            .orElse(ItemStack.EMPTY);
                    map.put(key, stack);
                }
//                System.out.println("[Skytools:FileManager] Loaded " + map.size() + " items from " + file.getPath());
            } catch (IOException e) {
                System.err.println("Failed to load file: " + file.getPath());
                e.printStackTrace();
            }
        }

        data.put(cacheKey, map);
    }

    public static void saveSlotCount(String location, String fileName, int count) {
        File dir = new File(FMLPaths.CONFIGDIR.get().toFile(), "Skytools/" + location);
        if (!dir.exists()) dir.mkdirs();
        File countFile = new File(dir, fileName);
        try (FileWriter writer = new FileWriter(countFile)) {
            writer.write(Integer.toString(count));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getSlotCount(String location, String fileName) {
        File countFile = new File(FMLPaths.CONFIGDIR.get().toFile(), "Skytools/" + location + "/" + fileName);
        if (!countFile.exists()) return 0;
        try {
            return Integer.parseInt(Files.readString(countFile.toPath()).trim());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean folderExists(String path) {
        File dir = new File(FMLPaths.CONFIGDIR.get().toFile(), "Skytools/" + path);
        return dir.exists() && dir.isDirectory();
    }

    public static boolean fileExists(String path) {
        File file = new File(FMLPaths.CONFIGDIR.get().toFile(), "Skytools/" + path);
        return file.exists() && file.isFile();
    }

    public static void debugPrintItems(String location, String fileName) {
        String cacheKey = getCacheKey(location, fileName);
        Map<String, ItemStack> map = data.getOrDefault(cacheKey, new HashMap<>());

        if (map.isEmpty()) {
            System.out.println("[Skytools:FileManager] No items found in cache for " + cacheKey);
            return;
        }

        System.out.println("[Skytools:FileManager] Items in " + cacheKey + ":");
        for (Map.Entry<String, ItemStack> entry : map.entrySet()) {
            System.out.println(" - " + entry.getKey() + ": " + entry.getValue().getDisplayName().getString());
        }
    }
}
