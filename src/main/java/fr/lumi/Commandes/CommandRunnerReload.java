package fr.lumi.Commandes;

import fr.lumi.Main;
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
        if (sender instanceof Player) {
            try {
                plugin.Load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("ConsolePrefix"))+" ReLoaded");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Prefix"))+"succesfully reloaded");
        }
        return true;
    }
}