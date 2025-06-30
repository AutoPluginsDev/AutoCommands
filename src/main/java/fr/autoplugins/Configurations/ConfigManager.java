package fr.autoplugins.Configurations;

import fr.autoplugins.Main;

public class ConfigManager {

    private Main plugin;
    public ConfigManager(Main plugin) {
        this.plugin = plugin;
    }

    /**
     * Loads the configuration file and upgrades it if necessary.
     **/
    public void loadConfig() {
        plugin.saveDefaultConfig();
        String loadedConfigVersion = plugin.getConfig().getString("ConfigVersion");
        if (loadedConfigVersion == null)
        {
            plugin.getLogger().severe("Config version is not set in the configuration file. (corrupted file?)");
            return;
        }
        short version = Short.parseShort(loadedConfigVersion);
        if (version == 3) UpgradeConfigFile.upgradeToVersion4(plugin);

    }

    public void saveConfig() {
        // Save configuration logic
    }

    public void reloadConfig() {
        // Reload configuration logic
    }
}
