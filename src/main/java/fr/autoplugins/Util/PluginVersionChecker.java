package fr.autoplugins.Util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.autoplugins.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

public class PluginVersionChecker {
    private static String callGithubForTag(Main plugin) {
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
            plugin.getLogger().log(Level.WARNING, "Failed to check for a new version on spigot.", e);
        }
        return response.toString();
    }

    public static String VerifyPluginVersion(Main plugin) {
        String spigotResponse = "";
        String currentVersion = plugin.getDescription().getVersion();

        String response = callGithubForTag(plugin);

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


}
