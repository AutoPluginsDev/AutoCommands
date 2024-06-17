package fr.lumi.Commandes;

import fr.lumi.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class CommandRunnerReload implements CommandExecutor {

    Main plugin;

    public CommandRunnerReload(Main plg) {

        plugin = plg;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.onEnable();
        Bukkit.getConsoleSender().sendMessage(plugin.getUt().replacePlaceHoldersForConsolePlgVar(plugin.getLangConfig().getString("OnReload")));
        sender.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar(plugin.getLangConfig().getString("OnReload")));

        return true;
    }
}