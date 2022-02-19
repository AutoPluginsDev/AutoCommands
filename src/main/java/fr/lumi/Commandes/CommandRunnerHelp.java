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
    public CommandRunnerHelp(Main plg ) {

        plugin = plg;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> l = new ArrayList<>();
        if(cmd.getName().equalsIgnoreCase("acmdhelp")){
            if(sender instanceof Player){
                List<String> list = new ArrayList<>();

            }
        }
        return l;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if(args.length >= 0 ){
                player.sendMessage("§e-----------§aAutoCommands-Help§e--------------");
                player.sendMessage("§6ACMD Version : 1.0");
                player.sendMessage("§6/acmd -> §aget the list or write new AutoCommands");
                player.sendMessage("§6/acmdconfig -> §amodification of the plugin's parameters");
                player.sendMessage("§6/acmdreload -> §areload the plugin and the autocomands");
                player.sendMessage("§e------------------------------------------");

            }
        }
        return true;
    }
}