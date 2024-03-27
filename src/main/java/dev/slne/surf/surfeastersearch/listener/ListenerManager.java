package dev.slne.surf.surfeastersearch.listener;

import dev.slne.surf.surfeastersearch.SurfEasterSearch;
import dev.slne.surf.surfeastersearch.listener.player.PlayerInteractListener;
import dev.slne.surf.surfeastersearch.listener.save.WorldSaveListener;
import io.papermc.paper.util.Tick;
import java.time.ZonedDateTime;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.Contract;

public final class ListenerManager {

  public static final ListenerManager INSTANCE = new ListenerManager();

  @Contract(pure = true)
  private ListenerManager() {
  }

  public void registerListeners() {
    final SurfEasterSearch plugin = SurfEasterSearch.getInstance();
    final PluginManager pm = Bukkit.getPluginManager();

    pm.registerEvents(WorldSaveListener.INSTANCE, plugin);

    if (ZonedDateTime.now().isBefore(SurfEasterSearch.START_DATE)) {
      Bukkit.getScheduler()
          .runTaskLater(plugin, () -> pm.registerEvents(PlayerInteractListener.INSTANCE, plugin),
              ZonedDateTime.now().until(SurfEasterSearch.START_DATE, Tick.tick()));
    } else {
      pm.registerEvents(PlayerInteractListener.INSTANCE, plugin);
    }

  }

  public void unregisterListeners() {
    HandlerList.unregisterAll(SurfEasterSearch.getInstance());
  }
}
