package fr.lumi.Commandes;

import fr.lumi.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class CommandRunnerConf implements CommandExecutor, TabCompleter {

    Main plugin;

    public CommandRunnerConf(Main plg) {
        plugin = plg;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> l = new ArrayList<>();

        if (cmd.getName().equalsIgnoreCase("acmdconf")) {
            if (sender instanceof Player) {
                List<String> list = new ArrayList<>();
                l.add("prefix");
            }
        }
        return l;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {

            }
        }
        return true;
    }
}
