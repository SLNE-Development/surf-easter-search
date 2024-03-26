package dev.slne.surf.surfeastersearch.config.items.pack;

import dev.slne.surf.surfeastersearch.config.ConfigManager;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PacksConfigManager implements ConfigManager {

  private final Int2ObjectMap<List<ItemStack>> packs;


  @Contract(pure = true)
  public PacksConfigManager() {
    this.packs = new Int2ObjectOpenHashMap<>();
  }

  @Override
  public void readConfig(@NotNull ConfigurationSection config) {
    for (final String key : config.getKeys(false)) {
      final int id = Integer.parseInt(key);
      final List<byte[]> items = new ArrayList<>(config.getStringList(key))
          .stream()
          .map(s -> Base64.getDecoder().decode(s))
          .toList();

      packs.put(id, items.stream()
          .map(ItemStack::deserializeBytes)
          .collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
    }
  }

  @Override
  public void writeConfig(@NotNull ConfigurationSection config) {
    for (final Int2ObjectMap.Entry<List<ItemStack>> entry : packs.int2ObjectEntrySet()) {
      config.set(String.valueOf(entry.getIntKey()), entry.getValue().stream()
          .map(ItemStack::serializeAsBytes)
          .map(bytes -> Base64.getEncoder().encodeToString(bytes))
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
