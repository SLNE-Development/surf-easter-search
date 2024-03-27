package dev.slne.surf.surfeastersearch.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.slne.surf.surfeastersearch.command.egg.EggCommand;
import dev.slne.surf.surfeastersearch.command.limit.ResetDailyLimitCommand;
import dev.slne.surf.surfeastersearch.command.pack.PackCommand;

public class EasterCommand extends CommandAPICommand {

  public EasterCommand() {
    super("easter");

    withPermission("surfeastersearch.command.easter");
    withAliases("ostern");

    withSubcommands(
        new EggCommand("egg"),
        new PackCommand("pack"),
        new ResetDailyLimitCommand("resetdailylimit")
    );

  }
}
