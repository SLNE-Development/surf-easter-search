package dev.slne.surf.surfeastersearch.messages;

import java.util.List;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MessageBundle implements Colors {

    public static @NotNull Component getEggAlreadyCollected() {
        return PREFIX
                .append(Component.text("Du hast dieses Ei bereits eingesammelt.", ERROR));
    }

    public static @NotNull Component getAllPacksCollected() {
        return PREFIX
                .append(Component.text("Du hast bereits alle Packs eingesammelt.", ERROR));
    }

    public static @NotNull Component getPackCollected(int packId, @NotNull List<ItemStack> items) {
        final JoinConfiguration itemJoinConfig = JoinConfiguration.builder()
                .prefix(Component.text("[", GREEN))
                .separator(Component.text(", ", SPACER))
                .suffix(Component.text("]", GREEN))
                .build();

        final List<Component> itemDisplayNames = items.stream()
                .map(ItemStack::displayName)
                .toList();

        return PREFIX
                .append(Component.text("Du hast Pack ", SUCCESS))
                .append(Component.text(packId, VARIABLE_VALUE))
                .append(Component.text(" eingesammelt: ", SUCCESS))
                .append(Component.join(itemJoinConfig, itemDisplayNames));
    }

    public static Component getDailyLimitReached() {
        return PREFIX
                .append(Component.text("Du hast das tägliche Limit erreicht.", ERROR));
    }

    public static Component getNoTileStateMessage() {
        return PREFIX
                .append(Component.text("Dieser Block hat keinen TileState.", ERROR));
    }

    public static Component getTileStateAlreadyEgg() {
        return PREFIX
                .append(Component.text("Dieser Block ist bereits ein Ei.", ERROR));
    }

    public static Component getEggMade(Location eggLocation) {
        String worldName = eggLocation.getWorld().getName();
        int x = eggLocation.getBlockX();
        int y = eggLocation.getBlockY();
        int z = eggLocation.getBlockZ();

        return PREFIX
                .append(Component.text("Du hast erfolgreich ein Ei bei ", SUCCESS)
                        .append(Component.text(x + ", " + y + ", " + z, INFO)
                                .append(Component.text(" in der Welt ", SUCCESS)
                                        .append(Component.text(worldName, INFO)
                                                .append(Component.text(" erstellt!", SUCCESS)
                                                )))));
    }

    public static Component getNoItemInHand() {
        return PREFIX
                .append(Component.text("Du hältst kein Item in der Hand.", ERROR));
    }

    public static Component getEventEnded() {
        return PREFIX
                .append(Component.text("Du hast das Event leider verpasst!", ERROR));
    }

    public static Component getPackItemAdded(int packId, ItemStack item) {
        return PREFIX
                .append(Component.text("Item hinzugefügt zu Pack ", SUCCESS))
                .append(Component.text(packId, VARIABLE_VALUE))
                .append(Component.text(": ", SUCCESS))
                .append(item.displayName());
    }
}
