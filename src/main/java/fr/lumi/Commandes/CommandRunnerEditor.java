package fr.lumi.Commandes;

import fr.lumi.Main;
import fr.lumi.Util.CommandEditor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandRunnerEditor implements CommandExecutor, TabCompleter {

    CommandEditor acmdEditor;
    Main plugin;

    public CommandRunnerEditor(Main plg, CommandEditor Editor) {
        acmdEditor = Editor;
        plugin = plg;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> l = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("acmdEditor")) {
            if (sender instanceof Player) {
                List<String> list = new ArrayList<>();

            }
        }
        return l;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean valid = true;
        if (sender instanceof Player) {
            Player player = (Player) sender;


            valid = valid && openMenu((Player) sender);
            if (!valid) return false;

            valid = valid && SaveMenu();
            if (!valid) return false;


            valid = valid && closeMenu();
            if (!valid) return false;

        }
        return valid;
    }

    private boolean openMenu(Player p) {
        acmdEditor.openchoosing(p);
        return true;
    }

    private boolean SaveMenu() {
        return true;
    }


    private boolean closeMenu() {
        return true;
    }

}