package dev.slne.surf.surfeastersearch.config.players;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SerializableAs("PlayerData")
public class PlayerData implements ConfigurationSerializable {

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

  public void resetTodayCollected() {
    todayCollected = 0;
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
