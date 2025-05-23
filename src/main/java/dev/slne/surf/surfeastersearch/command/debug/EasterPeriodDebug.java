package dev.slne.surf.surfeastersearch.command.debug;

import dev.jorel.commandapi.CommandAPICommand;
import dev.slne.surf.surfeastersearch.SurfEasterSearch;
import dev.slne.surf.surfeastersearch.messages.MessageBundle;
import net.kyori.adventure.text.Component;

public class EasterPeriodDebug extends CommandAPICommand {

    public EasterPeriodDebug(String commandName) {
        super(commandName);
        withPermission("surfeastersearch.command.easter.debug");

        executes((player, args) -> {
                    player.sendMessage(MessageBundle.getEasterPeriod());
                }
        );
    }
}
