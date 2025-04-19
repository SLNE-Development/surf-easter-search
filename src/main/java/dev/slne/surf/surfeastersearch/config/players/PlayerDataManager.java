package dev.slne.surf.surfeastersearch.config.players;

import dev.slne.surf.surfeastersearch.SurfEasterSearch;
import dev.slne.surf.surfeastersearch.config.EasterConfigManager;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class PlayerDataManager {

  private static final Logger logger = SurfEasterSearch.getInstance().getLogger();

  private static final NamespacedKey COLLECTED_PACKS_KEY = new NamespacedKey(
      SurfEasterSearch.getInstance(), "collected-packs");
  private static final NamespacedKey COLLECTED_EGGS_KEY = new NamespacedKey(
      SurfEasterSearch.getInstance(), "collected-eggs");
  private static final NamespacedKey TODAY_COLLECTED_EGGS = new NamespacedKey(
      SurfEasterSearch.getInstance(), "today-collected-eggs");
  static final NamespacedKey LAST_RESET_TIMESTAMP = new NamespacedKey(
      SurfEasterSearch.getInstance(), "limit-reset-timestamp");

  private static final int MAX_COLLECT_PER_DAY = 3;

  public static void addCollectedPackId(Player player, int packId) {
    PersistentDataContainer container = player.getPersistentDataContainer();

    // get list or empty array from pdc
    int[] collectedPacks = container.getOrDefault(COLLECTED_PACKS_KEY,
        PersistentDataType.INTEGER_ARRAY, new int[0]);

    // if id is already in the pdc, return
    for (int id : collectedPacks) {
      if (id == packId) {
        return;
      }
    }
    // new array with additional id ot the pack
    int[] updated = Arrays.copyOf(collectedPacks, collectedPacks.length + 1);
    updated[updated.length - 1] = packId;

    // set the new array to the pdc
    container.set(COLLECTED_PACKS_KEY, PersistentDataType.INTEGER_ARRAY, updated);
  }

  public static boolean hasCollectedPack(Player player, int packId) {
    int[] collectedPacks = player.getPersistentDataContainer()
        .getOrDefault(COLLECTED_PACKS_KEY, PersistentDataType.INTEGER_ARRAY, new int[0]);
    return Arrays.stream(collectedPacks).anyMatch(id -> id == packId);
  }

  public static void addCollectedEggId(Player player, int eggId) {
    PersistentDataContainer container = player.getPersistentDataContainer();

    // get list or empty array
    int[] collectedEggs = container.getOrDefault(COLLECTED_EGGS_KEY,
        PersistentDataType.INTEGER_ARRAY, new int[0]);

    // if id is already in the pdc, return
    for (int id : collectedEggs) {
      if (id == eggId) {
        return;
      }
    }
    // new array with additional id to the pack
    int[] updated = Arrays.copyOf(collectedEggs, collectedEggs.length + 1);
    updated[updated.length - 1] = eggId;

    container.set(COLLECTED_EGGS_KEY, PersistentDataType.INTEGER_ARRAY, updated);
  }

  public static boolean hasCollectedEgg(Player player, int eggId) {
    int[] collectedEggs = player.getPersistentDataContainer()
        .getOrDefault(COLLECTED_EGGS_KEY, PersistentDataType.INTEGER_ARRAY, new int[0]);
    return Arrays.stream(collectedEggs).anyMatch(id -> id == eggId);
  }

  public static int increaseTodayCollected(Player player) {
    PersistentDataContainer container = player.getPersistentDataContainer();

    int todayCollected = container.getOrDefault(TODAY_COLLECTED_EGGS, PersistentDataType.INTEGER,
        0);
    todayCollected++;

    container.set(TODAY_COLLECTED_EGGS, PersistentDataType.INTEGER, todayCollected);

    return todayCollected;
  }

  public static void tryResetTodayCollected(Player player) {
    PersistentDataContainer container = player.getPersistentDataContainer();

    long lastReset = container.getOrDefault(LAST_RESET_TIMESTAMP, PersistentDataType.LONG, 0L);
    long currentTime = System.currentTimeMillis();
    long millisInDay = 24 * 60 * 60 * 1000;

    // Prüfen, ob mindestens ein voller Tag vergangen ist
    if (currentTime - lastReset < millisInDay) {
      logger.log(Level.INFO, "Daily egg limits not reset for player: " + player.getName());
      return;
    }

    logger.log(Level.INFO, "Resetting daily egg limits for player: " + player.getName());

    container.set(TODAY_COLLECTED_EGGS, PersistentDataType.INTEGER, 0);
    container.set(LAST_RESET_TIMESTAMP, PersistentDataType.LONG, System.currentTimeMillis());
  }

  public static void resetDailyLimits() {
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      tryResetTodayCollected(onlinePlayer);
    }
  }

  public static boolean canCollectToday(@NotNull Player player) {
    PersistentDataContainer container = player.getPersistentDataContainer();

    int todayCollected = container.getOrDefault(TODAY_COLLECTED_EGGS, PersistentDataType.INTEGER,
        0);

    return todayCollected < MAX_COLLECT_PER_DAY;
  }

  public static int getRandomUnCollectedPackId(Player player) {
    final IntSet uncollectedPackIds = EasterConfigManager.INSTANCE.getItemsConfigManager()
        .getPacksConfigManager().getPacks().keySet();

    uncollectedPackIds.removeIf(id -> hasCollectedPack(player, id));

    if (uncollectedPackIds.isEmpty()) {
      return -1;
    }

    return uncollectedPackIds.toIntArray()[EasterConfigManager.INSTANCE.getRandom()
        .nextInt(uncollectedPackIds.size())];
  }
}
