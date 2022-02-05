package me.ganyganyant.supplements.commands;

import me.ganyganyant.supplements.Supplements;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class Discord implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        Supplements plugin = Supplements.getPlugin();

        String[] dis = Objects.requireNonNull(
                plugin.getConfig().getString("discord"))
                .split("\\{LINK}", 2);

        TextComponent LINK = new TextComponent(
                ChatColor.translateAlternateColorCodes('&',
                        Objects.requireNonNull(
                                plugin.getConfig().getString("discordLinkText"))));

        LINK.setClickEvent( new ClickEvent(
                ClickEvent.Action.OPEN_URL,
                plugin.getConfig().getString("discordURL") ));

        TextComponent msg = new TextComponent(ChatColor.translateAlternateColorCodes('&',dis[0]));
        msg.addExtra(LINK);
        msg.addExtra(ChatColor.translateAlternateColorCodes('&',dis[1]));

        sender.spigot().sendMessage(msg);

        return true;
    }
}