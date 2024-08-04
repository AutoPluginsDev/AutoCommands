package fr.lumi;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.lumi.CommandPatternObject.Command;
import fr.lumi.Commandes.*;
import fr.lumi.Metrics.Metrics;
import fr.lumi.FileVerifiers.ConfigFileVerification;
import fr.lumi.FileVerifiers.LangFileVerification;
import fr.lumi.Util.*;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;

import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Main extends JavaPlugin {

    private String[] Logo = {
            "&e&9     &6__     __ &e ",
            "&e&9 /\\ &6/  |\\/||  \\&e|  &9Auto&6Commands &aVersion &e" + this.getDescription().getVersion(),
            "&e&9/--\\&6\\__|  ||__/&e|  &8running on bukkit - paper",
            ""};

    private void printLogo() {
        for (String s : Logo)//print the logo
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', s));
    }

    /*
     * modificationLock: This prevents the administrator to modify the plugin in the mean time, it could cause some issues/conflicts.
     */
    ModificationLock modificationLock = new ModificationLock(this);

    public ModificationLock getModificationLock() {
        return modificationLock;
    }


    boolean papiPresent = false;

    public boolean isPapiPresent() {
        return papiPresent;
    }

    FileConfiguration config = getConfig();

    // TODO: implement condition system
    ConditionVerifier amcdVerifier = new ConditionVerifier(this);
    dailyCommandExecuter executer;
    CommandEditor acmdGUIEditor;

    public CommandEditor getAcmdGUIEditor() {
        return acmdGUIEditor;
    }

    //command file creation
    private File commandsFile = new File(getDataFolder(), "commands.yml");
    private FileConfiguration commandsConfig = YamlConfiguration.loadConfiguration(commandsFile);

    public FileConfiguration getCommandsConfig() {
        commandsConfig = YamlConfiguration.loadConfiguration(commandsFile);
        return commandsConfig;
    }

    public File getCommandsFile() {
        commandsFile = new File(getDataFolder(), "commands.yml");
        return commandsFile;
    }

    public boolean saveCommandsFile() {
        try {
            getCommandsConfig().save(getCommandsFile());
        } catch (IOException ignored) {
            return false;
        }
        return true;
    }


    //translatefile gestion
    private File Langfile = new File(getDataFolder(), "lang.yml");
    private FileConfiguration Langconfig = YamlConfiguration.loadConfiguration(Langfile);

    public FileConfiguration getLangConfig() {
        Langconfig = YamlConfiguration.loadConfiguration(getLangFile());
        return Langconfig;
    }


    public File getLangFile() {
        Langfile = new File(getDataFolder(), "lang.yml");
        return Langfile;
    }

    public File getConfigFile() {
        Langfile = new File(getDataFolder(), "config.yml");
        return Langfile;
    }


    public void addBstatsMetrics() {
        int pluginId = 21737;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new Metrics.SimplePie("enabled_commands", () -> String.valueOf(getEnbaledCommand())));
    }


    private Utilities m_ut;

    public Utilities getUt() {
        return m_ut;
    }

    // command list, core of the plugin holding current commands
    private List<autocommand> commandList = new ArrayList<autocommand>();

    public List<autocommand> getcommandList() {
        return commandList;
    }

    //FileVerifiers
    private LangFileVerification LangVerif = new LangFileVerification(this);
    private ConfigFileVerification ConfigVerif = new ConfigFileVerification(this);

    public void executeCommand(Command cmd) {
        cmd.execute();
    }

    public void init() {

        m_ut = new Utilities(this);
        config = getConfig();
        Langfile = new File(getDataFolder(), "lang.yml");
        Langconfig = YamlConfiguration.loadConfiguration(Langfile);
        commandsFile = new File(getDataFolder(), "commands.yml");
        commandsConfig = YamlConfiguration.loadConfiguration(commandsFile);
        commandList = new ArrayList<autocommand>();
        //FileVerifiers
        LangVerif = new LangFileVerification(this);
        ConfigVerif = new ConfigFileVerification(this);

        //getRessourceFile(getCommandsFile(), "commands.yml", this);

        saveCommandsFile();
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
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("ConsolePrefix") + " &aPlaceholderAPI found"));
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("ConsolePrefix") + " &cPlaceholderAPI not found"));
        }

        // add bstat metrics
        addBstatsMetrics();

        long start = System.currentTimeMillis();
        init();

        saveDefaultConfig();
        //getRessourceFile(getLangFile(), "lang.yml", this);
        boolean verified = Load();

        long exeTime = System.currentTimeMillis() - start;
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("ConsolePrefix") + " &aOn (took " + exeTime + " ms)"));
    }

    public boolean verifyFiles() {
        boolean verified = false;
        verified = ConfigVerif.Verif();
        verified = verified && LangVerif.Verif();
        ConfigUtil.mergeConfig(this, "lang.yml", getLangFile());
        ConfigUtil.mergeConfig(this, "config.yml", getConfigFile());

        return verified;
    }

    @Override
    public void onDisable() {
        Unload();
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("ConsolePrefix") + " &cOff"));
    }

    public boolean Load() {
        boolean verified = false;

        verified = verifyFiles();
        if (!verified) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("ConsolePrefix") + "&4Ignore these errors if this is the first time you are running the plugin."));
        }

        reloadConfig();
        config = getConfig();
        Objects.requireNonNull(this.getCommand("acmdhelp")).setExecutor(new CommandRunnerHelp(this));
        Objects.requireNonNull(this.getCommand("acmd")).setExecutor(new CommandRunnerCommand(this));
        Objects.requireNonNull(this.getCommand("acmdreload")).setExecutor(new CommandRunnerReload(this));
        Objects.requireNonNull(this.getCommand("acmdTime")).setExecutor(new CommandRunnerTime(this));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("ConsolePrefix") + "&e-Loading " + getCommandsConfig().getKeys(false).size() + " AutoComands-"));

        //loading the commands in the plugin
        if (!loadCommandsTimeTable())
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("ConsolePrefix") + "&6No AutoComands to execute"));

        //daily executor enable
        executer = new dailyCommandExecuter(this, getcommandList());
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, executer, 0, executer.getrefreshRate());

        //registering the menu
        acmdGUIEditor = new CommandEditor(this);
        getServer().getPluginManager().registerEvents(acmdGUIEditor, this);
        Objects.requireNonNull(this.getCommand("acmdEditor")).setExecutor(new CommandRunnerEditor(this, acmdGUIEditor));

        if (getConfig().getBoolean("DisplayAcmdInConsole"))
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("ConsolePrefix") + " &e-------------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("ConsolePrefix") + " &aLoaded"));
        return verified;
    }

    public void Unload() {
        getServer().getScheduler().cancelTasks(this);
        for (autocommand acmd : getcommandList()) {
            acmd.setRunning(false, getCommandsConfig());
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("ConsolePrefix") + " &cUnloaded"));
    }


    public autocommand getacmdInList(String idRequested) {
        int index = 0;
        for (String idInConfig : getCommandsConfig().getKeys(false)) {
            if (Objects.equals(idInConfig, idRequested)) {
                return getcommandList().get(index);
            }
            index++;
        }
        return null;
    }

    public boolean acmdIdExist(String idRequested) {

        for (String idInConfig : getCommandsConfig().getKeys(false)) {
            autocommand cmd = new autocommand(this);
            if (Objects.equals(idInConfig, idRequested)) {
                return true;
            }
        }
        return false;
    }

    public boolean loadCommandsTimeTable() {
        commandList.clear();
        getServer().getScheduler().cancelTasks(this);
        for (String i : getCommandsConfig().getKeys(false)) {
            autocommand acmd = new autocommand(this);
            if (acmd.getInConfig(getCommandsConfig(), this, i)) {
                if (getConfig().getBoolean("DisplayAcmdInConsole"))
                    acmd.printToConsole();
                commandList.add(acmd);
            }
        }
        return true;
    }

    public int getEnbaledCommand() {
        int count = 0;
        for (autocommand a : getcommandList()) {
            if (a.isActive()) count++;
        }
        return count;
    }

    public int getRunningCommand() {
        int count = 0;
        for (autocommand a : getcommandList()) {
            if (a.isRunning()) count++;
        }
        return count;
    }

    public long convertToTick(long seconds) {
        return (long) seconds * 20;
    }

    public static void getRessourceFile(File file, String resource, Main plugin) {
        try {
            if (!file.exists()) {

                file.createNewFile();
                InputStream inputStream = plugin.getResource(resource);
                OutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.flush();
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        YamlConfiguration.loadConfiguration(file);
    }
}
