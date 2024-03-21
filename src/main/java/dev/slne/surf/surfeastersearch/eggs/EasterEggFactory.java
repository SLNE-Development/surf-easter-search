package dev.slne.surf.surfeastersearch.eggs;

import static com.google.common.base.Preconditions.checkNotNull;

import dev.slne.surf.surfeastersearch.SurfEasterSearch;
import dev.slne.surf.surfeastersearch.config.EasterConfigManager;
import org.bukkit.NamespacedKey;
import org.bukkit.block.TileState;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public final class EasterEggFactory {
  public static final NamespacedKey EGG_KEY = new NamespacedKey(SurfEasterSearch.getInstance(), "easter_egg");

  public static boolean isEgg(@NotNull TileState tileState) {
    return isEgg(tileState.getPersistentDataContainer());
  }

  public static boolean isEgg(@NotNull PersistentDataContainer pdc) {
    return pdc.has(EGG_KEY);
  }

  public static int getEggId(@NotNull TileState tileState) {
    return getEggId(tileState.getPersistentDataContainer());
  }

  public static int getEggId(@NotNull PersistentDataContainer pdc) {
    return checkNotNull(pdc.get(EGG_KEY, PersistentDataType.INTEGER), "Egg ID is null");
  }

  public static void makeEgg(@NotNull TileState tileState) {
    makeEgg(tileState.getPersistentDataContainer());
  }

  public static void makeEgg(@NotNull PersistentDataContainer pdc) {
    pdc.set(EGG_KEY, PersistentDataType.INTEGER, EasterConfigManager.INSTANCE.getAndIncrementEggIdCounter());
  }
}
