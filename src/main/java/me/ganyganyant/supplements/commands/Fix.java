package me.ganyganyant.supplements.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import static me.ganyganyant.supplements.Supplements.sendFromConfig;

public class Fix implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            boolean repaired = false;
            if (command.getName().equalsIgnoreCase("fix")) {
                ItemStack item = player.getInventory().getItemInMainHand();
                repaired = fixItem(item);
            } else if (command.getName().equalsIgnoreCase("fixall")) {
                for (ItemStack item : player.getInventory().getContents()) {
                    repaired |= fixItem(item);
                }
            }
            if (repaired) {
                sendFromConfig(player, "repaired");
            } else {
                sendFromConfig(player, "cannotRepair");
            }
        }
        return true;
    }

    public boolean fixItem(ItemStack item) {
        if (item == null) {return false;}
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof Damageable && ((Damageable) meta).hasDamage()) {
            ((Damageable) meta).setDamage(0);
            item.setItemMeta(meta);
            return true;
        } else {
            return false;
        }
    }
}
