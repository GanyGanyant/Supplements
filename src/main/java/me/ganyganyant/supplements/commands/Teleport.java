package me.ganyganyant.supplements.commands;

import me.ganyganyant.supplements.Supplements;
import me.ganyganyant.supplements.files.ToggleTP;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static me.ganyganyant.supplements.Supplements.sendFromConfig;
import static me.ganyganyant.supplements.commands.Back.TP;

public class Teleport implements CommandExecutor {

    static HashMap<UUID, UUID> requestMap = new HashMap<>();

    Supplements plugin = Supplements.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("tpa")) {
                if (args.length == 0) {
                    sendFromConfig(player, "specifyPlayerTP");
                    return true;
                }
                if (!Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
                    sendFromConfig(player, "playerOfflineTP");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[0]);
                assert target != null;
                if (!ToggleTP.getPlayer(target).canTP(player)) {
                    sendFromConfig(player, "playerToggledTP");
                    return true;
                }
                if (target.getUniqueId().equals(player.getUniqueId())) {
                    sendFromConfig(player, "cannotSelfTP");
                    return true;
                }
                if (requestMap.containsKey(player.getUniqueId())) {
                    sendFromConfig(player, "alreadyPendingRequestTP");
                    return true;
                }
                requestTeleport(player, target);
                return true;
            }
            else if (command.getName().equalsIgnoreCase("tpcancel")) {
                if (requestMap.containsKey(player.getUniqueId())) {
                    requestMap.remove(player.getUniqueId());
                    sendFromConfig(player, "requestCanceledTP");
                    return true;
                }
                sendFromConfig(player, "nothingToCancelTP");
                return true;
            }
            else if (command.getName().equalsIgnoreCase("tpaccept")) {
                if (requestMap.containsValue(player.getUniqueId())) {
                    for (Map.Entry<UUID, UUID> entry:requestMap.entrySet()) {
                        if (entry.getValue() == player.getUniqueId()) {

                            requestMap.remove(entry.getKey(),entry.getValue());
                            sendFromConfig(player, "requestAcceptedTP");
                            Player requester = Bukkit.getPlayer(entry.getKey());
                            assert requester != null;

                            if (plugin.getConfig().getInt("tpaTime") != 0) {
                                // sends a message that teleport will be delayed
                                requester.sendMessage(
                                        ChatColor.translateAlternateColorCodes('&', // changes &color to ChatColor.color
                                                Objects.requireNonNull(    // check if not null
                                                        plugin.getConfig().getString("tpaWait")   // get message from config
                                                ).replace("{TIME}", String.valueOf(plugin.getConfig().getInt("tpaTime")))));  // changes {TIME} to delay in seconds
                                // delays the teleport
                                TP(requester, player.getLocation(), plugin.getConfig().getInt("tpaTime") * 20L, "teleportingTP");
                                return true;
                            }
                            sendFromConfig(requester, "teleportingTP");
                            TP(requester, player.getLocation());
                            return true;
                        }
                    }
                }
                sendFromConfig(player, "noRequestsTP");
                return true;
            }
            else if (command.getName().equalsIgnoreCase("tpdeny")) {
                if (requestMap.containsValue(player.getUniqueId())) {
                    for (Map.Entry<UUID, UUID> entry:requestMap.entrySet()) {
                        if (entry.getValue() == player.getUniqueId()) {
                            Player requester = Bukkit.getPlayer(entry.getKey());
                            assert requester != null;
                            requestMap.remove(entry.getKey(),entry.getValue());
                            sendFromConfig(player, "requestDeniedTP");
                            sendFromConfig(requester, "yourRequestDeniedTP");
                            return true;
                        }
                    }
                }
                sendFromConfig(player, "noRequestsTP");
                return true;
            }
            else if (command.getName().equalsIgnoreCase("tpall")) {
                for (Player user:Bukkit.getOnlinePlayers()) {
                    TP(user, player.getLocation());
                }
            }
            else if (command.getName().equalsIgnoreCase("tptoggle")){

                ToggleTP.player p = ToggleTP.getPlayer(player);

                if (args.length == 0){
                    if (p.canTP()){
                        p.toggle();
                        sendFromConfig(player, "toggleFalse");
                        return true;
                    }
                    p.toggle();
                    sendFromConfig(player, "toggleTrue");
                    return true;
                }
                if (!Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
                    sendFromConfig(player, "toggleNotOnline");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[0]);
                assert target != null;
                if (target.getUniqueId().equals(player.getUniqueId())) {
                    sendFromConfig(player, "cannotToggleSelf");
                    return true;
                }
                if (p.notBlocked(target)) {
                    p.toggle(target);
                    player.sendMessage(
                            ChatColor.translateAlternateColorCodes('&',
                                Objects.requireNonNull(
                                    plugin.getConfig().getString("playerToggleFalse"))
                                    .replace("{PLAYER}",target.getDisplayName())));
                    return true;
                }
                p.toggle(target);

                player.sendMessage(
                        ChatColor.translateAlternateColorCodes('&',
                            Objects.requireNonNull(
                                plugin.getConfig().getString("playerToggleTrue"))
                                .replace("{PLAYER}",target.getDisplayName())));
                return true;
            }
        }
        return true;
    }

    private void requestTeleport(Player player, Player target) {
        UUID playerID = player.getUniqueId();
        UUID targetID = target.getUniqueId();
        requestMap.put(playerID, targetID);

        // confirm that request has been sent

        player.sendMessage(
                ChatColor.translateAlternateColorCodes('&', // changes &color to ChatColor.color
                        Objects.requireNonNull(    // check if not null
                                plugin.getConfig().getString("requestSentTP")   // get message from config
                        ).replace("{PLAYER}", target.getDisplayName())));  // changes {PLAYER} to name of targeted player

        // create building blocks of a message

        String message = Objects.requireNonNull(plugin.getConfig().getString("targetMsgTP")).replace("{PLAYER}",player.getDisplayName());

        String[] tpa = Objects.requireNonNull(
                        message)
                .split("\\{ACCEPT}|\\{DENY}", 3);

        TextComponent ACCEPT = new TextComponent(
                ChatColor.translateAlternateColorCodes('&',
                        Objects.requireNonNull(
                                plugin.getConfig().getString("acceptBoxTP"))));

        ACCEPT.setClickEvent( new ClickEvent(
                ClickEvent.Action.RUN_COMMAND,
                "/tpaccept" ));

        TextComponent DENY = new TextComponent(
                ChatColor.translateAlternateColorCodes('&',
                        Objects.requireNonNull(
                                plugin.getConfig().getString("denyBoxTP"))));

        DENY.setClickEvent( new ClickEvent(
                ClickEvent.Action.RUN_COMMAND,
                "/tpdeny" ));

        // build the message

        TextComponent msg = new TextComponent(ChatColor.translateAlternateColorCodes('&', tpa[0]));

        if (message.indexOf("{ACCEPT}") < message.indexOf("{DENY}")) {
            msg.addExtra(ACCEPT);
            msg.addExtra(ChatColor.translateAlternateColorCodes('&', tpa[1]));
            msg.addExtra(DENY);
        }
        else {
            msg.addExtra(DENY);
            msg.addExtra(ChatColor.translateAlternateColorCodes('&', tpa[1]));
            msg.addExtra(ACCEPT);
        }
        msg.addExtra(ChatColor.translateAlternateColorCodes('&', tpa[2]));

        // send the message

        target.spigot().sendMessage(msg);

        // terminate request after some time

        new BukkitRunnable(){
            @Override
            public void run() {
                if (requestMap.containsKey(playerID) && requestMap.get(playerID) == targetID) {
                    requestMap.remove(playerID, targetID);
                    sendFromConfig(player, "requestTimedOutTP");
                }
            }
        }.runTaskLater(plugin, plugin.getConfig().getInt("requestSpanTP") * 20L);
    }
}
