package fr.lumi.Util;

import fr.lumi.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ListAfficher {

    private final int m_commandMax;
    private final Main plugin;

    public ListAfficher(Main plg){
        plugin = plg;
        m_commandMax = plugin.getConfig().getInt("MaxDisplayedCommandInList");
    }

    public void printListToSender(int page, CommandSender sender){

        if (plugin.getLangConfig().getString("CommandListTop") != "")
            sender.sendMessage(plugin.getUt().replacePlaceHoldersPluginVars(plugin.getLangConfig().getString("CommandListTop")));

        for (int i = page * m_commandMax; i < page* m_commandMax + m_commandMax && i < plugin.getcommandList().size(); i++) {
            autocommand cmd = plugin.getcommandList().get(i);
            cmd.printToPlayer(sender);//
        }

        String s = "";
        if (plugin.getLangConfig().getString("CommandListBottom") != "")
            s = plugin.getLangConfig().getString("CommandListBottom");
            s = s.replace("%acmdNumberOfListPage",(getPageNumber()+1)+"");
            s = s.replace("%acmdCurrentPageOfList",(page+1)+"");
            sender.sendMessage(plugin.getUt().replacePlaceHoldersPluginVars(s));
    }

    public int getPageNumber(){
        return (plugin.getcommandList().size()/m_commandMax) ;
    }

    public int getMaxCommand(){
        return m_commandMax;
    }

}
