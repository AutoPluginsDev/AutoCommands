package fr.lumi.Util;

import fr.lumi.Main;
import org.bukkit.ChatColor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class Utilities {

    private Main plugin;

    public Utilities(Main plg){
        plugin = plg;
    }

    public String replacePlaceHolders(String s,autocommand cmd){
        try {


            s = s.replace("%acmdName",cmd.getName());

            if(cmd.getRepetition() == -1) s = s.replace("%acmdRepetition","∞");
            else s = s.replace("%acmdRepetition",cmd.getRepetition()+"");


            s = s.replace("%acmdRepCounter",cmd.getRepetitionCounter()+"");
            s = s.replace("%acmdMessage", cmd.getmessage());
            s = s.replace("%acmdDelayTick",cmd.getDelay()+"");
            s = s.replace("%acmdDelaySec",(long)(cmd.getDelay()/20)+"");

            if (cmd.isRunning()){
                s = s.replace("%acmdIsRunning","&aRunning");
            }
            else s = s.replace("%acmdIsRunning","&cStopped");

            if (cmd.isActive()){
                s = s.replace("%acmdIsActive","&aEnable");
            }
            else s = s.replace("%acmdIsActive","&cDisable");


            if (cmd.getCycleInSec() < 10 && cmd.getCycleInSec() > 0 ){
                s = s.replace("%acmdCycleTick","&c"+cmd.getCycle()+"(short cycle)");
                s = s.replace("%acmdCycleSec","&c"+cmd.getCycleInSec()+"(short cycle)");
            }
            else if(cmd.getCycleInSec() == 0){
                s = s.replace("%acmdCycleTick","&c"+cmd.getCycle()+"(do you want to crash your serv ?)");
                s = s.replace("%acmdCycleSec","&c"+cmd.getCycleInSec()+"(do you want to crash your serv ?)");
            }
            else{
                s = s.replace("%acmdCycleTick","&a"+cmd.getCycle());
                s = s.replace("%acmdCycleSec","&a"+cmd.getCycleInSec());
            }

            s = s.replace("%acmdID",cmd.getID());
            s = s.replace("%acmdCommand",cmd.getStringFormatCommands());

            if(!cmd.getTime().isEmpty())
                s = s.replace("%acmdDaylyTime",cmd.getTime());
            else
                s = s.replace("%acmdDaylyTime","&cX");

            s = s.replace("%acmdFound",plugin.getcommandList().size()+"");
            s = s.replace("%acmdcurrentlyRunning",plugin.getRunningCommand()+"");
            s = s.replace("%acmdCurrentlyEnabled",plugin.getEnbaledCommand()+"");
            // get the last tennant and tansform it to a player name from plugin.getModificationLock().getLastTennant()
            String tenanntPlayerName = plugin.getServer().getOfflinePlayer(plugin.getModificationLock().getLastTennant()).getName();
            s = s.replace("%acmdLockingUser",tenanntPlayerName);
            s = PapiReplace(null,s);
        }
        catch (Exception e){
            plugin.getLogger().info("Error while replacing placeholders. Have a look to you language file");
            plugin.getLogger().info(e.getMessage());
            throw e;
        }
        return s;
    }

    public String replacePlaceHoldersPluginVars(String s) {

        s = s.replace("%acmdFound",plugin.getcommandList().size()+"");
        s = s.replace("%acmdcurrentlyRunning",plugin.getRunningCommand()+"");
        s = s.replace("%acmdCurrentlyEnabled",plugin.getEnbaledCommand()+"");

        s = PapiReplace(null,s);
        return s;
    }



    public String replacePlaceHoldersForPlayer(String s, autocommand cmd, Player player){
        try {
            s = replacePlaceHolders(s,cmd);
            s = PapiReplace(player,s);
        }
        catch (Exception e){
            plugin.getLogger().info("Error in replacePlaceHoldersForPlayer");
            plugin.getLogger().info(e.getMessage());
            player.sendMessage("Error with the language file:");

        }
        s = replacePlaceHolders(s,cmd);

        s = PapiReplace(player,s);
        return ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Prefix")+s);
    }

    public String replacePlaceHoldersForPlayerPlgVar(String s){
        s = replacePlaceHoldersPluginVars(s);
        return ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Prefix")+s);
    }


    public String replacePlaceHoldersForConsole(String s,autocommand cmd){
        s = replacePlaceHolders(s,cmd);
        return ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("ConsolePrefix")+s);
    }

    public String replacePlaceHoldersForConsolePlgVar(String s){
        s = replacePlaceHoldersPluginVars(s);
        return ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("ConsolePrefix")+s);
    }

    public String PapiReplace( Player player, String s){
        if (plugin.isPapiPresent())
            s = PlaceholderAPI.setPlaceholders(player,s);
        return s;
    }


}
