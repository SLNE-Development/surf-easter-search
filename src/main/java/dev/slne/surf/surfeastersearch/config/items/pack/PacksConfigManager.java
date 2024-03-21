package dev.slne.surf.surfeastersearch.config.items.pack;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PacksConfigManager {

  private final Int2ObjectMap<List<ItemStack>> packs;


  @Contract(pure = true)
  public PacksConfigManager() {
    this.packs = new Int2ObjectOpenHashMap<>();
  }

  public void readConfig(@NotNull ConfigurationSection config) {
    for (final String key : config.getKeys(false)) {
      final int id = Integer.parseInt(key);
      final List<byte[]> items = new ArrayList<>((List<byte[]>) config.getList(key, new ArrayList<>()));

      packs.put(id, items.stream()
          .map(ItemStack::deserializeBytes)
          .toList());
    }
  }

  public void writeConfig(@NotNull ConfigurationSection config) {
    for (final Int2ObjectMap.Entry<List<ItemStack>> entry : packs.int2ObjectEntrySet()) {
      config.set(String.valueOf(entry.getIntKey()), entry.getValue().stream()
          .map(ItemStack::serializeAsBytes)
          .toList());
    }
  }

  public Int2ObjectMap<List<ItemStack>> getPacks() {
    return new Int2ObjectOpenHashMap<>(packs);
  }

  public void addItemToPack(int packId, ItemStack item) {
    packs.computeIfAbsent(packId, __ -> new ArrayList<>()).add(item);
  }

  public List<ItemStack> getItemsFromPack(int packId) {
    return packs.get(packId);
  }
}
