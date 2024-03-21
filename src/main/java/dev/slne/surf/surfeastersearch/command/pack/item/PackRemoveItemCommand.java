package dev.slne.surf.surfeastersearch.command.pack.item;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.slne.surf.surfeastersearch.config.EasterConfigManager;

public class PackRemoveItemCommand extends CommandAPICommand {

  public PackRemoveItemCommand(String commandName) {
    super(commandName);

    withArguments(new IntegerArgument("packId")
        .replaceSuggestions(ArgumentSuggestions.stringCollection(
            info -> EasterConfigManager.INSTANCE.getItemsConfigManager().getPacksConfigManager()
                .getPacks().keySet().intStream()
                .mapToObj(String::valueOf)
                .toList())));

    executesPlayer((player, args) -> {
      final int packId = args.getUnchecked("packId");
    });
  }
}
