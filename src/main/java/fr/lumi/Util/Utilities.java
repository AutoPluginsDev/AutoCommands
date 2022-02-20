package fr.lumi.Util;

import fr.lumi.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Utilities {

    private Main plugin;

    public Utilities(Main plg){
        plugin = plg;
    }

    public String replacePlaceHolders(String s,autocommand cmd ){

        s = s.replace("%acmdName",cmd.getName());

        if(cmd.getRepetition() == -1) s = s.replace("%acmdRepetition","∞");
        else s = s.replace("%acmdRepetition",cmd.getRepetition()+"");


        s = s.replace("%acmdRepCounter",cmd.getRepetitionCounter()+"");
        s = s.replace("%acmdMessage",cmd.getmessage()+"");
        s = s.replace("%acmdDelayTick",cmd.getDelay()+"");
        s = s.replace("%acmdDelaySec",cmd.getDelay()/20+"");

        if (cmd.isActive()){
            s = s.replace("%acmdIsActive","&aEnable");
        }
        else s = s.replace("%acmdIsActive","&cDisable");


        if (cmd.getCycleInSec()<10){
            s = s.replace("%acmdCycleTick","&c"+cmd.getCycle()+"(short cycle)");
            s = s.replace("%acmdCycleSec","&c"+cmd.getCycleInSec()+"(short cycle)");
        }
        else{
            s = s.replace("%acmdCycleTick","&a"+cmd.getCycle());
            s = s.replace("%acmdCycleSec","&a"+cmd.getCycleInSec());
        }

        s = s.replace("%acmdID",cmd.getID()+"");
        s = s.replace("%acmdCommand",cmd.getStringFormatCommands()+"");
        s = s.replace("%acmdFound",plugin.getcommandList().size()+"");
        s = s.replace("%acmdRunning",plugin.getEnbaledCommand()+"");
        if(cmd.getTime() != "") s = s.replace("%acmdDaylyTime",cmd.getTime()+"");
        else s = s.replace("%acmdDaylyTime","&cX");

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
