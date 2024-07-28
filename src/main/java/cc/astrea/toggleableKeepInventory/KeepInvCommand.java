package cc.astrea.toggleableKeepInventory;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameRule;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KeepInvCommand implements CommandExecutor, TabExecutor {
    NamespacedKey key;

    KeepInvCommand(NamespacedKey key) {
        this.key = key;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("togglekeepinv.command")) {
            sender.sendMessage(NamedTextColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (args.length >= 2) {
            return false;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can run this command.");
            return true;
        }

        boolean value;

        if (args.length == 0) { // no arguments passed, just toggle
            Boolean previousValue = player.getPersistentDataContainer().get(key, BooleanType.BOOLEAN);
            if (previousValue == null) {
                previousValue = player.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY);
            }
            value = Boolean.FALSE.equals(previousValue);
        } else {
            String arg = args[0];
            // this check could be better, but eh
            if (!arg.equalsIgnoreCase("on") && !arg.equalsIgnoreCase("off") && !arg.equalsIgnoreCase("true") && !arg.equalsIgnoreCase("false")) {
                return false;
            }

            value = args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("true");
        }

        player.getPersistentDataContainer().set(key, BooleanType.BOOLEAN, value);
        sender.sendMessage("Keep inventory turned " + (value ? "on" : "off") + "."); // TODO make this a string you can set
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            return List.of("on", "off");
        }
        return null;
    }
}
