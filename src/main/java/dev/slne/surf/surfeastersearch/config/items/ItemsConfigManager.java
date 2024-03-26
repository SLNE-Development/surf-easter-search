package dev.slne.surf.surfeastersearch.config.items;

import dev.slne.surf.surfeastersearch.config.ConfigManager;
import dev.slne.surf.surfeastersearch.config.items.pack.PacksConfigManager;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ItemsConfigManager implements ConfigManager {

  private final PacksConfigManager packsConfigManager;

  @Contract(pure = true)
  public ItemsConfigManager() {
    this.packsConfigManager = new PacksConfigManager();
  }

  @Override
  public void readConfig(@NotNull ConfigurationSection config) {
    packsConfigManager.readConfig(getOrCreateSection(config, "packs"));
  }

  @Override
  public void writeConfig(@NotNull ConfigurationSection config) {
    packsConfigManager.writeConfig(config.createSection("packs"));
  }

  public PacksConfigManager getPacksConfigManager() {
    return packsConfigManager;
  }
}
