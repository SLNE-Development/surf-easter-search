package dev.slne.surf.surfeastersearch.command.pack;

import dev.jorel.commandapi.CommandAPICommand;
import dev.slne.surf.surfeastersearch.command.pack.item.PackAddItemCommand;
import dev.slne.surf.surfeastersearch.command.pack.item.PackListItemsCommand;
import dev.slne.surf.surfeastersearch.command.pack.item.PackRemoveItemCommand;

public class PackCommand extends CommandAPICommand {

  public PackCommand(String commandName) {
    super(commandName);

    withSubcommand(new PackAddItemCommand("additem"));
    withSubcommand(new PackRemoveItemCommand("removeitem"));
    withSubcommand(new PackListItemsCommand("packlistitems"));
  }
}
