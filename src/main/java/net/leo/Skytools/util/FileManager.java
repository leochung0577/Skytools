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

    // Cached per location
    private static final Map<String, Map<String, ItemStack>> data = new HashMap<>();

    private static File getFile(String location, String fileName) {
        File dir = new File(FMLPaths.CONFIGDIR.get().toFile(), "Skytools/" + location);
        if (!dir.exists()) dir.mkdirs();
        return new File(dir, fileName);
    }

    public static void saveFile(String location, String fileName, String key, ItemStack stack) {
        loadFromFile(location, fileName); // Ensure loaded before saving
        data.computeIfAbsent(location, loc -> new HashMap<>()).put(key, stack.copy());
        saveToFile(location, fileName);
    }

    public static ItemStack getItem(String location, String fileName, String key) {
        loadFromFile(location, fileName); // Lazy load
        return data.getOrDefault(location, new HashMap<>()).getOrDefault(key, ItemStack.EMPTY);
    }

    public static void saveToFile(String location, String fileName) {
        File file = getFile(location, fileName);
        CompoundTag root = new CompoundTag();

        Map<String, ItemStack> items = data.getOrDefault(location, new HashMap<>());
        for (Map.Entry<String, ItemStack> entry : items.entrySet()) {
            String key = entry.getKey();
            ItemStack stack = entry.getValue();

            ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, stack)
                    .resultOrPartial(error -> System.err.println("Failed to encode item '" + key + "': " + error))
                    .ifPresent(tag -> root.put(key, (CompoundTag) tag));
        }

        try (OutputStream out = Files.newOutputStream(file.toPath())) {
            NbtIo.writeCompressed(root, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void loadFromFile(String location, String fileName) {
        if (data.containsKey(location)) return;

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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        data.put(location, map);
    }

    public static boolean fileExists(String path) {
        File dir = new File(FMLPaths.CONFIGDIR.get().toFile(), "Skytools/" + path);
        return dir.exists() && dir.isDirectory();
    }

    public static void saveSlotCount(String location, String fileName,int count) {
        File dir = new File(FMLPaths.CONFIGDIR.get().toFile(), "Skytools/" + location);
        if (!dir.exists()) dir.mkdirs();
        File countFile = new File(dir, fileName);
        try (FileWriter writer = new FileWriter(countFile)) {
            writer.write(Integer.toString(count));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getSlotCount(String location) {
        File countFile = new File(FMLPaths.CONFIGDIR.get().toFile(), "Skytools/" + location + "/slotCount.txt");
        if (!countFile.exists()) return 0;
        try {
            return Integer.parseInt(Files.readString(countFile.toPath()).trim());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
