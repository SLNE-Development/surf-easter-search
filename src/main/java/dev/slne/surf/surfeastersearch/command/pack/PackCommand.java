package dev.slne.surf.surfeastersearch.command.pack;

import dev.jorel.commandapi.CommandAPICommand;
import dev.slne.surf.surfeastersearch.command.pack.item.PackAddItemCommand;

public class PackCommand extends CommandAPICommand {

  public PackCommand(String commandName) {
    super(commandName);

    withSubcommand(new PackAddItemCommand("additem"));
  }
}
