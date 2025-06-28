package fr.autoplugins.CommandExecutors;

import fr.autoplugins.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class CommandExecutorReload implements CommandExecutor {

    Main plugin;

    public CommandExecutorReload(Main plg) {
        plugin = plg;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //plugin.getAcmdGUIEditor().closeLastTennantInventory();
        plugin.onEnable();
        //Bukkit.getConsoleSender().sendMessage(plugin.getUt().replacePlaceHoldersForConsolePlgVar(plugin.getLangConfig().getString("OnReload")));
        //sender.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar(plugin.getLangConfig().getString("OnReload")));
        return true;
    }
}