package fr.lumi.Util;

import fr.lumi.Main;
import org.bukkit.command.CommandSender;

public class ListAfficher {

    private final int m_itemsMax;
    private final Main plugin;

    public ListAfficher(Main plg) {
        plugin = plg;
        m_itemsMax = plugin.getConfig().getInt("MaxDisplayedCommandInList");
    }

    public void printListToSender(int page, CommandSender sender) {

        if (plugin.getLangConfig().getString("CommandListTop") != "")
            sender.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar(plugin.getLangConfig().getString("CommandListTop")));

        for (int i = page * m_itemsMax; i < page * m_itemsMax + m_itemsMax && i < plugin.getcommandList().size(); i++) {
            autocommand cmd = plugin.getcommandList().get(i);
            cmd.printToPlayer(sender);
        }

        String s = "";
        if (plugin.getLangConfig().getString("CommandListBottom") != "") {
            s = plugin.getLangConfig().getString("CommandListBottom");
            s = s.replace("%acmdNumberOfListPage", (getPageNumber() + 1) + "");
            s = s.replace("%acmdCurrentPageOfList", (page + 1) + "");
        }
        sender.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar(s));
    }

    public int getPageNumber() {
        return (int) ((plugin.getcommandList().size() - 1) / (m_itemsMax));
    }

    public int getMaxItem() {
        return m_itemsMax;
    }

}
