package fr.autoplugins;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.autoplugins.Commands.*;
import fr.autoplugins.Metrics.Metrics;
import fr.autoplugins.Util.Utilities;
import org.bukkit.ChatColor;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.logging.Level;

import com.google.gson.JsonParser;

import java.util.Objects;

public final class Main extends JavaPlugin {

    private String[] Logo = {
            "&e&9     &6__     __ &e ",
            "&e&9 /\\ &6/  |\\/||  \\&e|  &9Auto&6Commands &aVersion DEV&e" + this.getDescription().getVersion(),
            "&e&9/--\\&6\\__|  ||__/&e|  &8running on bukkit - paper",
            ""};

    private void printLogo() {
        for (String s : Logo)//print the logo
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', s));
    }

    boolean papiPresent = false;

    public boolean isPapiPresent() {
        return papiPresent;
    }


    public void addBstatsMetrics() {
        int pluginId = 21737;
        Metrics metrics = new Metrics(this, pluginId);
    }


    private Utilities m_ut;

    public Utilities getUt() {
        return m_ut;
    }

    public void init() {
        m_ut = new Utilities(this);
    }


    public String callGithubForTag() {
        StringBuilder response = new StringBuilder();
        try {
            // Make HTTP GET request
            URL url = new URL("https://api.github.com/repos/AutoPluginsDev/AutoCommands/tags");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Check response code
            if (connection.getResponseCode() != 200) {
                throw new IOException("Failed to get response from GitHub API");
            }

            // Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Failed to check for a new version on spigot.", e);
        }
        return response.toString();
    }

    public String VerifyPluginVersion() {
        String spigotResponse = "";
        String currentVersion = this.getDescription().getVersion();

        String response = callGithubForTag();

        // Parse JSON response
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(response);
        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            if (jsonArray.size() > 0) {
                JsonObject latestTag = jsonArray.get(0).getAsJsonObject();
                spigotResponse = latestTag.get("name").getAsString();
            }
        }

        if (spigotResponse.equals("")) {
            return "&cFailed to check for a new version on spigot.";
        }

        if (spigotResponse.equals(currentVersion)) {
            return "&aYou are running the latest version of AutoCommands " + currentVersion + " !";
        }

        return "&eAutoCommands &a&l" + spigotResponse + " &eis available! &chttps://www.spigotmc.org/resources/acmd-%E2%8F%B0-%E2%8F%B3-autocommands-1-13-1-20-4.100090";
    }

    @Override
    public void onEnable() {
        printLogo();
        // verify if the plugin is up to date and send a message to the admins

        String broadcastMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix") + VerifyPluginVersion());
        Bukkit.broadcast(broadcastMessage, "bukkit.broadcast.admin");

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            papiPresent = true;
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',   " &aPlaceholderAPI found"));
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',   " &cPlaceholderAPI not found"));
        }

        // add bstat metrics
        addBstatsMetrics();

        long start = System.currentTimeMillis();

        init();
        Load();

        long exeTime = System.currentTimeMillis() - start;
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &aOn (took " + exeTime + " ms)"));
    }

    @Override
    public void onDisable() {
        Unload();
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &cOff"));
    }

    public boolean Load() {
        boolean verified = false;

        Objects.requireNonNull(this.getCommand("acmdhelp")).setExecutor(new CommandRunnerHelp(this));
        Objects.requireNonNull(this.getCommand("acmd")).setExecutor(new CommandRunnerCommand(this));
        Objects.requireNonNull(this.getCommand("acmdreload")).setExecutor(new CommandRunnerReload(this));
        Objects.requireNonNull(this.getCommand("acmdTime")).setExecutor(new CommandRunnerTime(this));

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &aLoaded"));
        return verified;
    }

    public void Unload() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &cUnloaded"));
    }

}
