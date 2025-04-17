package dev.slne.surf.surfeastersearch.listener.player;

import dev.slne.surf.surfeastersearch.SurfEasterSearch;
import dev.slne.surf.surfeastersearch.config.EasterConfigManager;
import dev.slne.surf.surfeastersearch.config.items.pack.PacksConfigManager;
import dev.slne.surf.surfeastersearch.config.players.PlayerData;
import dev.slne.surf.surfeastersearch.config.players.PlayerDataManager;
import dev.slne.surf.surfeastersearch.eggs.EasterEggFactory;
import dev.slne.surf.surfeastersearch.messages.MessageBundle;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class PlayerInteractListener implements Listener {

    public static final PlayerInteractListener INSTANCE = new PlayerInteractListener();
    private final ObjectSet<UUID> processingPlayers;

    @Contract(pure = true)
    private PlayerInteractListener() {
        this.processingPlayers = ObjectSets.synchronize(new ObjectOpenHashSet<>());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Block clickedBlock = event.getClickedBlock();

        if (clickedBlock == null) {
            return;
        }

        if (!(clickedBlock.getState() instanceof TileState tileState)) {
            return;
        }

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        if (!EasterEggFactory.isEgg(tileState)) {
            return;
        }

        event.setCancelled(true);

        if (ZonedDateTime.now().isAfter(SurfEasterSearch.END_DATE)) {
            event.getPlayer().sendMessage(MessageBundle.getEventEnded());
            return;
        }

        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        if (!processingPlayers.add(uuid)) {
            return;
        }

        final int eggId = EasterEggFactory.getEggId(tileState);

        if (PlayerDataManager.hasCollectedEgg(player, eggId)) {
            player.sendMessage(MessageBundle.getEggAlreadyCollected());
            removePlayerFromProcessingList(uuid);
            return;
        }

        if (!(PlayerDataManager.canCollectToday(player))) {
            player.sendMessage(MessageBundle.getDailyLimitReached());
            removePlayerFromProcessingList(uuid);
            return;
        }


        final int packId = PlayerDataManager.getRandomUnCollectedPackId(player);

        if (packId == -1) {
            player.sendMessage(MessageBundle.getAllPacksCollected());
            removePlayerFromProcessingList(uuid);
            return;
        }

        collectPack(player, packId, eggId);
        removePlayerFromProcessingList(uuid);
    }

    private void removePlayerFromProcessingList(UUID uuid) {
        Bukkit.getScheduler()
                .runTaskLater(SurfEasterSearch.getInstance(), () -> processingPlayers.remove(uuid), 5L);
    }

    private void collectPack(@NotNull Player player, int packId, int eggId) {
        final UUID playerUuid = player.getUniqueId();
        final PacksConfigManager packsConfigManager = EasterConfigManager.INSTANCE.getItemsConfigManager()
                .getPacksConfigManager();

        final List<ItemStack> items = packsConfigManager.getItemsFromPack(packId);

        PlayerDataManager.addCollectedPackId(player, packId);
        PlayerDataManager.addCollectedEggId(player, eggId);
        PlayerDataManager.increaseTodayCollected(player);


        player.sendMessage(MessageBundle.getPackCollected(packId, items));

        final Map<Integer, ItemStack> leftOvers = player.getInventory()
                .addItem(items.toArray(ItemStack[]::new));

        if (leftOvers.isEmpty()) {
            return;
        }

        final World world = player.getWorld();
        final Location location = player.getLocation();

        for (final ItemStack itemStack : leftOvers.values()) {
            world.dropItem(location, itemStack, item -> {
                item.setPickupDelay(0);
                item.setUnlimitedLifetime(true);
                item.setOwner(playerUuid);
                item.setCanMobPickup(false);
            });
        }
    }
}
