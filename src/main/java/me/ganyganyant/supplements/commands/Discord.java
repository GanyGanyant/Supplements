package me.ganyganyant.supplements.commands;

import me.ganyganyant.supplements.Supplements;
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

        Supplements plugin = Supplements.getPlugin();

        if (sender instanceof Player) {
            Player p = (Player) sender;

            String Text1 = plugin.getConfig().getString("discordText1");
            String Text2 = plugin.getConfig().getString("discordLinkText");
            String Text3 = plugin.getConfig().getString("discordText2");

            String Link = plugin.getConfig().getString("discordLinkAddress");

            assert Text1 != null;
            TextComponent msg1 = new TextComponent(ChatColor.translateAlternateColorCodes('&',Text1));
            assert Text2 != null;
            TextComponent msg2 = new TextComponent(ChatColor.translateAlternateColorCodes('&',Text2));
            assert Text3 != null;
            TextComponent msg3 = new TextComponent(ChatColor.translateAlternateColorCodes('&',Text3));
            msg2.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, Link ) );
            msg1.addExtra(msg2);
            msg1.addExtra(msg3);

            String Line1 = plugin.getConfig().getString("discordLine1");
            String Line2 = plugin.getConfig().getString("discordLine2");

            if (Line1 != null) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',Line1));
            }
            p.spigot().sendMessage(msg1);
            if (Line2 != null) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',Line2));
            }
        }

        return true;
    }
}