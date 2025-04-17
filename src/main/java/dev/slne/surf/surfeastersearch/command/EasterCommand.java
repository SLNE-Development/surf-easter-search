package dev.slne.surf.surfeastersearch.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.slne.surf.surfeastersearch.command.debug.EasterPeriodDebug;
import dev.slne.surf.surfeastersearch.command.egg.CreateEggCommand;
import dev.slne.surf.surfeastersearch.command.pack.PackCommand;

public class EasterCommand extends CommandAPICommand {

  public EasterCommand() {
    super("easter");

    withPermission("surfeastersearch.command.easter");
    withAliases("ostern");

    withSubcommands(
        new CreateEggCommand("createegg"),
        new PackCommand("pack"),
        new EasterPeriodDebug("easterperiod")
    );

  }
}
