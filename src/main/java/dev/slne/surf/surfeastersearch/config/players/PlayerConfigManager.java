package dev.slne.surf.surfeastersearch.config.players;

import dev.slne.surf.surfeastersearch.SurfEasterSearch;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PlayerConfigManager {
  private static final int MAX_COLLECT_PER_DAY = 3;
  private final Object2ObjectMap<UUID, PlayerData> playerDataMap;

  public PlayerConfigManager() {
    this.playerDataMap = new Object2ObjectOpenHashMap<>();
    playerDataMap.defaultReturnValue(new PlayerData());
  }

  public void readConfig(@NotNull ConfigurationSection config) {
    for (String stringUuid : config.getKeys(false)) {
      final UUID uuid = UUID.fromString(stringUuid);
      playerDataMap.put(uuid, config.getSerializable(stringUuid, PlayerData.class));
    }
  }

  public void writeConfig(ConfigurationSection config) {
    for (Map.Entry<UUID, PlayerData> entry : playerDataMap.object2ObjectEntrySet()) {
      config.set(entry.getKey().toString(), entry.getValue());
    }
  }

  public boolean canCollectToday(@NotNull Player player) {
    final PlayerData playerData = playerDataMap.get(player.getUniqueId());
    final int todayCollected = playerData.getAndIncrementTodayCollected();

    return todayCollected < MAX_COLLECT_PER_DAY;
  }

  public PlayerData getPlayerData(@NotNull UUID uuid) {
    return playerDataMap.get(uuid);
  }

  public void resetDailyLimits() {
    for (PlayerData playerData : playerDataMap.values()) {
      playerData.todayCollected = 0;
    }

    SurfEasterSearch.getInstance().saveEasterConfig();
  }


  @SerializableAs("PlayerData")
  public static class PlayerData implements ConfigurationSerializable {

    private int todayCollected;
    private final IntList collectedPackIds;
    private final IntList collectedEggIds;

    @Contract(pure = true)
    public PlayerData() {
      this.todayCollected = 0;
      this.collectedPackIds = new IntArrayList();
      this.collectedEggIds = new IntArrayList();
    }

    public PlayerData(@NotNull Map<String, Object> map) {
      this.todayCollected = (int) map.get("today-collected");
      this.collectedPackIds = new IntArrayList((List<Integer>) map.get("collected-pack-ids"));
      this.collectedEggIds = new IntArrayList((List<Integer>) map.get("collected-egg-ids"));
    }


    public int getAndIncrementTodayCollected() {
      return todayCollected++;
    }

    public void addCollectedPackId(int packId) {
      collectedPackIds.add(packId);
    }

    public boolean hasCollectedPackId(int packId) {
      return collectedPackIds.contains(packId);
    }

    public void addCollectedEggId(int eggId) {
      collectedEggIds.add(eggId);
    }

    public boolean hasCollectedEggId(int eggId) {
      return collectedEggIds.contains(eggId);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
      return Map.of(
          "today-collected", todayCollected,
          "collected-pack-ids", collectedPackIds,
          "collected-egg-ids", collectedEggIds
      );
    }
  }
}
