package dev.slne.surf.surfeastersearch.listener.player;

import dev.slne.surf.surfeastersearch.config.EasterConfigManager;
import dev.slne.surf.surfeastersearch.config.items.pack.PacksConfigManager;
import dev.slne.surf.surfeastersearch.config.players.PlayerData;
import dev.slne.surf.surfeastersearch.eggs.EasterEggFactory;
import dev.slne.surf.surfeastersearch.messages.MessageBundle;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class PlayerInteractListener implements Listener {

  public static final PlayerInteractListener INSTANCE = new PlayerInteractListener();

  @Contract(pure = true)
  private PlayerInteractListener() {
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

    if (!EasterEggFactory.isEgg(tileState)) {
      return;
    }

    final Player player = event.getPlayer();
    final int eggId = EasterEggFactory.getEggId(tileState);
    final PlayerData playerData = EasterConfigManager.INSTANCE.getPlayerConfigManager()
        .getPlayerData(player.getUniqueId());

    if (playerData.hasCollectedEggId(eggId)) {
      player.sendMessage(MessageBundle.getEggAlreadyCollected());
      return;
    }

    if (!EasterConfigManager.INSTANCE.getPlayerConfigManager().canCollectToday(player)) {
      player.sendMessage(MessageBundle.getDailyLimitReached());
      return;
    }

    final int packId = EasterConfigManager.INSTANCE.getRandomUnCollectedPackId(player);

    if (packId == -1) {
      player.sendMessage(MessageBundle.getAllPacksCollected());
      return;
    }

    collectPack(player, packId, eggId);
  }

  private void collectPack(@NotNull Player player, int packId, int eggId) {
    final UUID playerUuid = player.getUniqueId();
    final PlayerData playerData = EasterConfigManager.INSTANCE.getPlayerConfigManager()
        .getPlayerData(playerUuid);
    final PacksConfigManager packsConfigManager = EasterConfigManager.INSTANCE.getItemsConfigManager()
        .getPacksConfigManager();

    final List<ItemStack> items = packsConfigManager.getItemsFromPack(packId);

    playerData.addCollectedPackId(packId);
    playerData.addCollectedEggId(eggId);

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
