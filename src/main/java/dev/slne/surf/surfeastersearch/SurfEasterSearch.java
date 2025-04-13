package dev.slne.surf.surfeastersearch;

import dev.slne.surf.surfeastersearch.command.CommandManager;
import dev.slne.surf.surfeastersearch.config.EasterConfigManager;
import dev.slne.surf.surfeastersearch.config.players.PlayerData;
import dev.slne.surf.surfeastersearch.config.players.PlayerDataManager;
import dev.slne.surf.surfeastersearch.listener.ListenerManager;
import io.papermc.paper.util.Tick;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class SurfEasterSearch extends JavaPlugin {

    public static final ZonedDateTime START_DATE;
    public static final ZonedDateTime END_DATE;

    static {
        // current year
        int year = ZonedDateTime.now().getYear();
        // calculate eastersunday
        ZonedDateTime easterSunday = getEasterSunday(year);

        ZoneId zone = ZonedDateTime.now().getZone();

        // Karfreitag = 2 days before eastersunday
//        START_DATE = easterSunday.minusDays(2).withHour(0).withMinute(0).withSecond(0).withNano(0).withZoneSameInstant(zone);
        START_DATE = ZonedDateTime.now();

        // Ostermontag = 1 day after eastersunday
        END_DATE = easterSunday.plusDays(1).withHour(23).withMinute(59).withSecond(59).withNano(0).withZoneSameInstant(zone);
    }

    public SurfEasterSearch() {
    }

    @Override
    public void onLoad() {
        ConfigurationSerialization.registerClass(PlayerData.class);
        saveDefaultConfig();
        saveConfig();
        EasterConfigManager.INSTANCE.readConfig(getConfig());
    }

    @Override
    public void onEnable() {
        ListenerManager.INSTANCE.registerListeners();

        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime nextRun = now.plusDays(1).withHour(0).withMinute(0).withSecond(0);
        final long delay = now.until(nextRun, Tick.tick());


        getServer().getAsyncScheduler().runAtFixedRate(
                this,
                task -> {
                    PlayerDataManager.resetDailyLimits();
                },
                delay,
                1,
                TimeUnit.DAYS
        );

        CommandManager.INSTANCE.registerCommands();
    }

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

    /**
     * Calculates Easter Sunday for a given year (according to the Meeus/Jones/Butcher algorithm).
     */
    private static ZonedDateTime getEasterSunday(int year) {
        int a = year % 19;
        int b = year / 100;
        int c = year % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int month = (h + l - 7 * m + 114) / 31;
        int day = ((h + l - 7 * m + 114) % 31) + 1;

        return ZonedDateTime.of(year, month, day, 0, 0, 0, 0, ZonedDateTime.now().getZone());
    }
}
