package dev.slne.surf.surfeastersearch.command.pack.item;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.slne.surf.surfeastersearch.config.EasterConfigManager;
import dev.slne.surf.surfeastersearch.messages.MessageBundle;
import org.bukkit.inventory.ItemStack;

public class PackAddItemCommand extends CommandAPICommand {

  public PackAddItemCommand(String commandName) {
    super(commandName);

    withArguments(new IntegerArgument("packId")
        .replaceSuggestions(ArgumentSuggestions.stringCollection(
            info -> EasterConfigManager.INSTANCE.getItemsConfigManager().getPacksConfigManager()
                .getPacks().keySet().intStream()
                .mapToObj(String::valueOf)
                .toList())));

    executesPlayer((player, args) -> {
      final int packId = args.getUnchecked("packId");
      final ItemStack item = player.getInventory().getItemInMainHand().clone();

      if (item.isEmpty()) {
        player.sendMessage(MessageBundle.getNoItemInHand());
        return;
      }

      EasterConfigManager.INSTANCE.getItemsConfigManager().getPacksConfigManager()
          .addItemToPack(packId, item);

      player.sendMessage(MessageBundle.getPackItemAdded(packId, item));
    });
  }
}
