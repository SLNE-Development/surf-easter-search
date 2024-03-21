package dev.slne.surf.surfeastersearch;

import dev.slne.surf.surfeastersearch.config.EasterConfigManager;
import dev.slne.surf.surfeastersearch.config.players.PlayerConfigManager.PlayerData;
import dev.slne.surf.surfeastersearch.listener.ListenerManager;
import io.papermc.paper.util.Tick;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class SurfEasterSearch extends JavaPlugin {

  public SurfEasterSearch() {
  }

  @Override
  public void onLoad() {
    ConfigurationSerialization.registerClass(PlayerData.class);
    saveDefaultConfig();

    EasterConfigManager.INSTANCE.readConfig(getConfig());
  }

  @Contract(pure = true)
  @Override
  public void onEnable() {
    ListenerManager.INSTANCE.registerListeners();

    // schedule task on 0:00 every day
    final ZonedDateTime now = ZonedDateTime.now();
    final ZonedDateTime nextRun = now.plusDays(1).withHour(0).withMinute(0).withSecond(0);
    final long delay = now.until(nextRun, Tick.tick());

    getLogger().severe("Delay: " + delay);
    getLogger().severe("Next run: " + nextRun);
    getLogger().severe("Now: " + now);

    getServer().getScheduler().scheduleSyncRepeatingTask(
        this,
        () -> EasterConfigManager.INSTANCE.getPlayerConfigManager().resetDailyLimits(),
        delay,
        Duration.ofDays(1).get(Tick.tick())
    );
  }

  @Contract(pure = true)
  @Override
  public void onDisable() {
    saveEasterConfig();

    ListenerManager.INSTANCE.unregisterListeners();
  }

  public void saveEasterConfig() {
    EasterConfigManager.INSTANCE.writeConfig(getConfig());
    saveConfig();
  }

  public static @NotNull SurfEasterSearch getInstance() {
    return getPlugin(SurfEasterSearch.class);
  }
}
