package dev.slne.surf.surfeastersearch.config;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public interface ConfigManager {
  void readConfig(@NotNull ConfigurationSection config);

  void writeConfig(@NotNull ConfigurationSection config);

  @NotNull
  default ConfigurationSection getOrCreateSection(@NotNull ConfigurationSection config, String key) {
    final ConfigurationSection section = config.getConfigurationSection(key);
    return section != null ? section : config.createSection(key);
  }
}
