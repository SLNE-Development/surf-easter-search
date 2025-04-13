package dev.slne.surf.surfeastersearch.config.players;

import dev.slne.surf.surfeastersearch.SurfEasterSearch;
import dev.slne.surf.surfeastersearch.config.ConfigManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerConfigManager implements ConfigManager {
  private static final int MAX_COLLECT_PER_DAY = 3;
  private final Object2ObjectMap<UUID, PlayerData> playerDataMap;

  public PlayerConfigManager() {
    this.playerDataMap = new Object2ObjectOpenHashMap<>();
  }

  @Override
  public void readConfig(@NotNull ConfigurationSection config) {
    for (String stringUuid : config.getKeys(false)) {
      final UUID uuid = UUID.fromString(stringUuid);
      playerDataMap.put(uuid, config.getSerializable(stringUuid, PlayerData.class));
    }
  }

  @Override
  public void writeConfig(@NotNull ConfigurationSection config) {
    for (Map.Entry<UUID, PlayerData> entry : playerDataMap.object2ObjectEntrySet()) {
      config.set(entry.getKey().toString(), entry.getValue());
    }
  }
//
//  public boolean canCollectToday(@NotNull Player player) {
//    final PlayerData playerData = playerDataMap.get(player.getUniqueId());
//    final int todayCollected = playerData.getAndIncrementTodayCollected();
//
//    return todayCollected < MAX_COLLECT_PER_DAY;
//  }
  public PlayerData getPlayerData(@NotNull UUID uuid) {
    return playerDataMap.computeIfAbsent(uuid, __ -> new PlayerData());
  }

//  public void resetDailyLimits() {
//    for (PlayerData playerData : playerDataMap.values()) {
//      playerData.resetTodayCollected();
//    }
//
//    SurfEasterSearch.getInstance().saveEasterConfig();
//  }
}
