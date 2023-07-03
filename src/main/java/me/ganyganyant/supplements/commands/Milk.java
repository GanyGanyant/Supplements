package me.ganyganyant.supplements.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import static me.ganyganyant.supplements.Supplements.sendFromConfig;

public class Milk implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            for (PotionEffectType effect : PotionEffectType.values() ) {
                player.removePotionEffect(effect);
            }
            sendFromConfig(player, "clearedEffects");
        }
        return true;
    }
}
