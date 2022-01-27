package me.ganyganyant.supplements.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Discord implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if (sender instanceof Player) {
            Player p = (Player) sender;

            TextComponent message = new TextComponent( ChatColor.GOLD + " ZDE" );
            TextComponent msg = new TextComponent(ChatColor.DARK_AQUA + "Připoj se na náš discord server:");
            message.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, "https://discord.gg/3Qm9a8Te" ) );
            msg.addExtra(message);

            p.sendMessage("------------------------------------");
            p.spigot().sendMessage(msg);
            p.sendMessage("------------------------------------");
        }

        return true;
    }
}