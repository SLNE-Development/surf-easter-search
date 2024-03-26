package dev.slne.surf.surfeastersearch;

import dev.slne.surf.surfeastersearch.command.CommandManager;
import dev.slne.surf.surfeastersearch.config.EasterConfigManager;
import dev.slne.surf.surfeastersearch.config.players.PlayerData;
import dev.slne.surf.surfeastersearch.listener.ListenerManager;
import io.papermc.paper.util.Tick;
import java.time.Duration;
import java.time.ZonedDateTime;
import net.kyori.adventure.util.Ticks;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class SurfEasterSearch extends JavaPlugin {

  public static final ZonedDateTime START_DATE = ZonedDateTime.of(2024, 3, 31, 0, 0, 0, 0,
      ZonedDateTime.now().getZone());

  public SurfEasterSearch() {
  }

  @Override
  public void onLoad() {
    ConfigurationSerialization.registerClass(PlayerData.class);
    saveDefaultConfig();
    saveConfig();

    EasterConfigManager.INSTANCE.readConfig(getConfig());
  }

  @Contract(pure = true)
  @Override
  public void onEnable() {
    ListenerManager.INSTANCE.registerListeners();

    final ZonedDateTime now = ZonedDateTime.now();
    final ZonedDateTime nextRun = now.plusDays(1).withHour(0).withMinute(0).withSecond(0);
    final long delay = now.until(nextRun, Tick.tick());

    getServer().getScheduler().scheduleSyncRepeatingTask(
        this,
        () -> EasterConfigManager.INSTANCE.getPlayerConfigManager().resetDailyLimits(),
        delay,
        Duration.ofDays(1).toMillis() / Ticks.SINGLE_TICK_DURATION_MS
    );

    CommandManager.INSTANCE.registerCommands();
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
