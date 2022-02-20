package fr.lumi.Commandes;

import fr.lumi.Main;
import fr.lumi.Util.Utilities;
import fr.lumi.Util.autocommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CommandRunnerReload implements CommandExecutor {

    Main plugin;

    public CommandRunnerReload(Main plg) {

        plugin = plg;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        try {
            plugin.Load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bukkit.getConsoleSender().sendMessage(plugin.getUt().replacePlaceHoldersForConsole(plugin.getLangConfig().getString("OnReload"),new autocommand(plugin)));
        sender.sendMessage(plugin.getUt().replacePlaceHoldersForPlayer(plugin.getLangConfig().getString("OnReload"),new autocommand(plugin)));

        return true;
    }
}