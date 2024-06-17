package fr.lumi.Util;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

public class ConfigUtil {

    public static void mergeConfig(JavaPlugin plugin, String resourceFileName, File configFile) {
        try {
            // Load the default configuration from the resource file
            InputStream defaultConfigStream = plugin.getResource(resourceFileName);
            if (defaultConfigStream != null) {
                FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultConfigStream));

                // Load the existing configuration file
                FileConfiguration existingConfig = YamlConfiguration.loadConfiguration(configFile);

                // Merge the configurations
                mergeConfigurations(defaultConfig, existingConfig);

                // Save the merged configuration back to the file
                existingConfig.save(configFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void mergeConfigurations(FileConfiguration defaultConfig, FileConfiguration existingConfig) {
        Set<String> keys = defaultConfig.getKeys(true);
        for (String key : keys) {
            if (!existingConfig.contains(key)) {
                existingConfig.set(key, defaultConfig.get(key));
            }
        }
    }
}