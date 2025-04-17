package dev.slne.surf.surfeastersearch.command.egg;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import dev.slne.surf.surfeastersearch.eggs.EasterEggFactory;
import dev.slne.surf.surfeastersearch.messages.MessageBundle;
import org.bukkit.Location;
import org.bukkit.block.TileState;

public class CreateEggCommand extends CommandAPICommand {

  public CreateEggCommand(String commandName) {
    super(commandName);

    withArguments(new LocationArgument("eggLocation", LocationType.BLOCK_POSITION));

    executes((sender, args) -> {
      final Location eggLocation = args.getUnchecked("eggLocation");

      assert eggLocation != null;

      if (!(eggLocation.getBlock().getState() instanceof TileState tileState)) {
        sender.sendMessage(MessageBundle.getNoTileStateMessage());
        return;
      }

      if (EasterEggFactory.isEgg(tileState)) {
        sender.sendMessage(MessageBundle.getTileStateAlreadyEgg());
        return;
      }

      EasterEggFactory.makeEgg(tileState);
      sender.sendMessage(MessageBundle.getEggMade(eggLocation));
    });
  }
}
