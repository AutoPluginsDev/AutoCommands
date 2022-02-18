package fr.lumi.Util;

import fr.lumi.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Utilities {

    private Main plugin;

    public Utilities(Main plg){
        plugin = plg;
    }

    public String replacePlaceHolders(String s,autocommand cmd){

        s = s.replace("%acmdName",cmd.getName());
        s = s.replace("%acmdCycleTick",cmd.getCycle()+"");
        s = s.replace("%acmdCycleSec",cmd.getCycleInSec()+"");
        s = s.replace("%acmdMessage",cmd.getmessage()+"");
        s = s.replace("%acmdCommand",cmd.getCommande()+"");
        s = s.replace("%acmdRunningCount",plugin.getcommandList().size()+"");

        return s;
    }


    public String replacePlaceHoldersForPlayer(String s,autocommand cmd){
        s = replacePlaceHolders(s,cmd);
        return ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Prefix")+s);
    }

    public String replacePlaceHoldersForConsole(String s,autocommand cmd){
        s = replacePlaceHolders(s,cmd);
        return ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("ConsolePrefix")+s);
    }
}
