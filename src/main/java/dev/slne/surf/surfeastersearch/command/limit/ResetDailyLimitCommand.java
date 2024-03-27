package dev.slne.surf.surfeastersearch.command.limit;

import dev.jorel.commandapi.CommandAPICommand;
import dev.slne.surf.surfeastersearch.config.EasterConfigManager;

public class ResetDailyLimitCommand extends CommandAPICommand {

  public ResetDailyLimitCommand(String commandName) {
    super(commandName);

    executes((sender, args) -> {
      EasterConfigManager.INSTANCE.getPlayerConfigManager().resetDailyLimits();
      sender.sendMessage("Daily limits have been reset.");
    });
  }
}
