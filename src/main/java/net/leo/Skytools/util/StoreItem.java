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

public class StoreItem {

    // Cached per location
    private static final Map<String, Map<String, ItemStack>> data = new HashMap<>();

    private static File getFile(String location) {
        File dir = new File(FMLPaths.CONFIGDIR.get().toFile(), "Skytools/" + location);
        if (!dir.exists()) dir.mkdirs();
        return new File(dir, "items.nbt");
    }

    public static void saveItem(String location, String key, ItemStack stack) {
        loadFromFile(location); // Ensure loaded before saving
        data.computeIfAbsent(location, loc -> new HashMap<>()).put(key, stack.copy());
        saveToFile(location);
    }

    public static ItemStack getItem(String location, String key) {
        loadFromFile(location); // Lazy load
        return data.getOrDefault(location, new HashMap<>()).getOrDefault(key, ItemStack.EMPTY);
    }

    public static void saveToFile(String location) {
        File file = getFile(location);
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


    public static void loadFromFile(String location) {
        if (data.containsKey(location)) return;

        File file = getFile(location);
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

    public static Map<String, ItemStack> getAllItems(String location) {
        loadFromFile(location);
        return data.getOrDefault(location, new HashMap<>());
    }
}
