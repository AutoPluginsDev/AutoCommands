package fr.lumi.Commandes;

import fr.lumi.Main;
import fr.lumi.Util.Utilities;
import fr.lumi.Util.autocommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandRunnerHelp implements CommandExecutor, TabCompleter {


    Main plugin;

    public CommandRunnerHelp(Main plg) {

        plugin = plg;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> l = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("acmdhelp")) {
            if (sender instanceof Player) {
                List<String> list = new ArrayList<>();

            }
        }
        return l;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 0) {
            sender.sendMessage("§e-----------§aAutoCommands-Help§e--------------");
            sender.sendMessage("§6ACMD Version : 1.0");
            sender.sendMessage("§6/acmd [new|enable|disable|delete|list|edit] -> §aMain commands");
            sender.sendMessage("§6/acmdconfig -> §amodification of the plugin's parameters");
            sender.sendMessage("§6/acmdreload -> §areload the plugin and the autocomands");
            sender.sendMessage("§e------------------------------------------");

        }

        return true;
    }
}