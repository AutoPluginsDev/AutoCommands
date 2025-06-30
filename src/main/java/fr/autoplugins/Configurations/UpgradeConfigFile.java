package fr.autoplugins.Configurations;

import fr.autoplugins.Main;

import java.io.File;

/**
 * Handles upgrades of the configuration file
 */
public class UpgradeConfigFile {

    /**
     * Upgrades the configuration file from version 3 to version 4.
     *
     * @param plugin The main plugin instance.
     */
    public static void upgradeToVersion4(Main plugin)
    {
        /*
          Old config file format:
          ConfigVersion : 3
          LangFileVersion : 1
          ConsolePrefix : '&eAUTOCOMMANDS |'
          Prefix : '&eacmd | '
          DisplayAcmdInConsole : false
          MaxDisplayedCommandInList : 3
         */

        final int LangFileVersion =  plugin.getConfig().getInt("LangFileVersion", 1);
        final String ConsolePrefix = plugin.getConfig().getString("ConsolePrefix", "&eAUTOCOMMANDS |");
        final String Prefix = plugin.getConfig().getString("Prefix", "&eacmd | ");
        final boolean DisplayAcmdInConsole = plugin.getConfig().getBoolean("DisplayAcmdInConsole", false);
        final int MaxDisplayedCommandInList = plugin.getConfig().getInt("MaxDisplayedCommandInList", 3);

        // Rename the file to config.yml.old.<timestamp>
        File oldConfigFile = new File(plugin.getDataFolder(), "config.yml");
        if (! oldConfigFile.renameTo(new File(plugin.getDataFolder(), "config.yml.old." + System.currentTimeMillis()))) {
            plugin.getLogger().severe("Failed to rename the old config file. Please check permissions.");
            return;
        }
        // Create a new config file with the default values
        plugin.saveDefaultConfig();

        plugin.getConfig().set("lang_file_version", LangFileVersion);
        plugin.getConfig().set("console_prefix", ConsolePrefix);
        plugin.getConfig().set("prefix", Prefix);
        plugin.getConfig().set("display_acmd_in_console", DisplayAcmdInConsole);
        plugin.getConfig().set("max_displayed_command_in_list", MaxDisplayedCommandInList);
        plugin.getConfig().set("config_version", 4);

        plugin.getLogger().info("Configuration file upgraded to version 4.");
    }
}
