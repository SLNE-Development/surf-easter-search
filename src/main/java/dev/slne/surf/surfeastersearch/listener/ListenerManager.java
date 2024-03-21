package dev.slne.surf.surfeastersearch.listener;

import dev.slne.surf.surfeastersearch.SurfEasterSearch;
import dev.slne.surf.surfeastersearch.listener.save.WorldSaveListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

public final class ListenerManager {
  public static final ListenerManager INSTANCE = new ListenerManager();

  private ListenerManager() {
  }

  public void registerListeners() {
    final SurfEasterSearch plugin = SurfEasterSearch.getInstance();
    final PluginManager pm = Bukkit.getPluginManager();

    pm.registerEvents(WorldSaveListener.INSTANCE, plugin);
  }

  public void unregisterListeners() {
    HandlerList.unregisterAll(SurfEasterSearch.getInstance());
  }
}
