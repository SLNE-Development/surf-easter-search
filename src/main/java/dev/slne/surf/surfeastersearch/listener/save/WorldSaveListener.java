package dev.slne.surf.surfeastersearch.listener.save;

import dev.slne.surf.surfeastersearch.SurfEasterSearch;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class WorldSaveListener implements Listener {

  public static final WorldSaveListener INSTANCE = new WorldSaveListener();

  @Contract(pure = true)
  private WorldSaveListener() {
  }

  @Contract(pure = true)
  @EventHandler
  public void onWorldSave(@NotNull WorldSaveEvent event) {
    if (!event.getWorld().equals(Bukkit.getWorlds().get(0))) {
      return; // already saved
    }

    SurfEasterSearch.getInstance().saveEasterConfig();
  }
}
