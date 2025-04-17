package dev.slne.surf.surfeastersearch.command.pack.item;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.slne.surf.surfeastersearch.config.EasterConfigManager;
import dev.slne.surf.surfeastersearch.messages.Colors;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PackListItemsCommand extends CommandAPICommand {

    public PackListItemsCommand(String commandName) {
        super(commandName);

        withArguments(new IntegerArgument("packId")
                .replaceSuggestions(ArgumentSuggestions.stringCollection(info ->
                        EasterConfigManager.INSTANCE.getItemsConfigManager()
                                .getPacksConfigManager()
                                .getPacks()
                                .keySet()
                                .intStream()
                                .mapToObj(String::valueOf)
                                .toList()
                )));

        executesPlayer((player, args) -> {
            final int packId = args.getUnchecked("packId");

            Int2ObjectMap<List<ItemStack>> packs = EasterConfigManager.INSTANCE
                    .getItemsConfigManager()
                    .getPacksConfigManager()
                    .getPacks();

            List<ItemStack> items = packs.get(packId);

            if (items == null) {
                player.sendMessage(Colors.PREFIX.append(Component.text(
                        "Pack mit der ID " + packId + " wurde nicht gefunden.", Colors.ERROR)));
                return;
            }

            player.sendMessage(Colors.PREFIX.append(Component.text(
                    "Pack " + packId + " enthält folgende Items:", Colors.INFO)));

            if (items.isEmpty()) {
                player.sendMessage(Component.text("  (Leer)", Colors.SPACER));
            } else {
                for (ItemStack item : items) {
                    if (item != null && item.getAmount() > 0) {
                        TextComponent itemLine = Component.text("  - ", Colors.SPACER)
                                .append(Component.text(item.getType().name(), Colors.VARIABLE_VALUE))
                                .append(Component.text(" x" + item.getAmount(), Colors.SECONDARY));
                        player.sendMessage(itemLine);
                    }
                }
            }
        });
    }
}
