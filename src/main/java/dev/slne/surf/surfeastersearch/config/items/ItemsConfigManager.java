package dev.slne.surf.surfeastersearch.config.items;

import dev.slne.surf.surfeastersearch.config.items.pack.PacksConfigManager;
import org.bukkit.configuration.ConfigurationSection;

public class ItemsConfigManager {

  private final PacksConfigManager packsConfigManager;

  public ItemsConfigManager() {
    this.packsConfigManager = new PacksConfigManager();
  }

  public void readConfig(ConfigurationSection config) {
    packsConfigManager.readConfig(config.getConfigurationSection("packs"));
  }

  public void writeConfig(ConfigurationSection config) {
    packsConfigManager.writeConfig(config.createSection("packs"));
  }

  public PacksConfigManager getPacksConfigManager() {
    return packsConfigManager;
  }
}
