package dev.slne.surf.surfeastersearch.config;

import dev.slne.surf.surfeastersearch.config.items.ItemsConfigManager;
import dev.slne.surf.surfeastersearch.config.players.PlayerConfigManager;
import dev.slne.surf.surfeastersearch.config.players.PlayerData;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EasterConfigManager implements ConfigManager{

  private static final ComponentLogger LOGGER = ComponentLogger.logger("EasterConfigManager");
  public static final EasterConfigManager INSTANCE = new EasterConfigManager();

  private final ItemsConfigManager itemsConfigManager;
  private final PlayerConfigManager playerConfigManager;
  private final SecureRandom random;
  private int eggIdCounter;

  private EasterConfigManager() {
    this.itemsConfigManager = new ItemsConfigManager();
    this.playerConfigManager = new PlayerConfigManager();
    this.eggIdCounter = 0;

    SecureRandom tempRandom;
    try {
      tempRandom = SecureRandom.getInstance("SHA1PRNG");
    } catch (NoSuchAlgorithmException e) {
      LOGGER.error("Failed to get SecureRandom instance", e);
      tempRandom = new SecureRandom();
    }
    this.random = tempRandom;
  }

  @Override
  public void readConfig(@NotNull ConfigurationSection config) {
    itemsConfigManager.readConfig(getOrCreateSection(config,"items"));
    playerConfigManager.readConfig(getOrCreateSection(config, "players"));

    this.eggIdCounter = config.getInt("egg-id-counter", 0);
  }

  @Override
  public void writeConfig(@NotNull ConfigurationSection config) {
    itemsConfigManager.writeConfig(config.createSection("items"));
    playerConfigManager.writeConfig(config.createSection("players"));

    config.set("egg-id-counter", eggIdCounter);
  }

  public int getRandomUnCollectedPackId(@NotNull Player player) {
    final PlayerData playerData = playerConfigManager.getPlayerData(player.getUniqueId());
    final IntSet uncollectedPackIds = itemsConfigManager.getPacksConfigManager().getPacks().keySet();

    uncollectedPackIds.removeIf(playerData::hasCollectedPackId);

    if (uncollectedPackIds.isEmpty()) {
      return -1;
    }

    return uncollectedPackIds.toIntArray()[random.nextInt(uncollectedPackIds.size())];
  }

  public int getAndIncrementEggIdCounter() {
    return eggIdCounter++;
  }

  public void addItemToPack(int packId, ItemStack item) {
    itemsConfigManager.getPacksConfigManager().addItemToPack(packId, item);
  }


  public List<ItemStack> getItemsFromPack(int packId) {
    return itemsConfigManager.getPacksConfigManager().getItemsFromPack(packId);
  }

  public ItemsConfigManager getItemsConfigManager() {
    return itemsConfigManager;
  }

  public PlayerConfigManager getPlayerConfigManager() {
    return playerConfigManager;
  }
}
