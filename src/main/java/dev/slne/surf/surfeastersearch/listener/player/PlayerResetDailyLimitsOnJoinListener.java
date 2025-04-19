package dev.slne.surf.surfeastersearch.listener.player;

import dev.slne.surf.surfeastersearch.SurfEasterSearch;
import dev.slne.surf.surfeastersearch.config.players.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerResetDailyLimitsOnJoinListener implements Listener {

    public static final PlayerResetDailyLimitsOnJoinListener INSTANCE = new PlayerResetDailyLimitsOnJoinListener();

    private PlayerResetDailyLimitsOnJoinListener() {

    }

    @EventHandler()
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.getScheduler().runDelayed(SurfEasterSearch.getInstance(), (task) -> {

            PlayerDataManager.tryResetTodayCollected(player);

        }, null, 10 * 20L);
    }
}
