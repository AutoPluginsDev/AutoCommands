package fr.autoplugins.Util;

import fr.autoplugins.Main;
import org.bukkit.ChatColor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class Utilities {

    private Main plugin;

    public Utilities(Main plg) {
        plugin = plg;
    }

    public String replacePlaceHolders(String s) {
        return s;
    }

    public String replacePlaceHoldersPluginVars(String s) {

        s = s.replace("%acmdFound", "0");
        s = s.replace("%acmdcurrentlyRunning", "0");
        s = s.replace("%acmdCurrentlyEnabled", "0");

        s = PapiReplace(null, s);
        return s;
    }


    public String PapiReplace(Player player, String s) {
        if (plugin.isPapiPresent())
            s = PlaceholderAPI.setPlaceholders(player, s);
        return s;
    }


}
