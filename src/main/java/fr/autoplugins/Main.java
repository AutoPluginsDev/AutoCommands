package fr.autoplugins;
import fr.autoplugins.CommandExecutors.*;
import fr.autoplugins.Metrics.Metrics;
import fr.autoplugins.Util.Utilities;
import fr.autoplugins.Util.PluginVersionChecker;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

import java.util.Objects;

public final class Main extends JavaPlugin {

    private String[] Logo = {
            "&e&9     &6__     __ &e ",
            "&e&9 /\\ &6/  |\\/||  \\&e|  &9Auto&6Commands &aVersion DEV&e" + this.getDescription().getVersion(),
            "&e&9/--\\&6\\__|  ||__/&e|  &8running on bukkit - paper",
            ""};

    boolean papiPresent = false;

    private Utilities m_ut;


    private void printLogo() {
        for (String s : Logo)//print the logo
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', s));
    }

    public boolean isPapiPresent() {
        return papiPresent;
    }

    public void addBstatsMetrics() {
        int pluginId = 21737;
        Metrics metrics = new Metrics(this, pluginId);
    }

    public Utilities getUt() {
        return m_ut;
    }

    public void init() {
        m_ut = new Utilities(this);
    }

    @Override
    public void onEnable() {
        printLogo();
        // verify if the plugin is up to date and send a message to the admins

        String broadcastMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix") + PluginVersionChecker.VerifyPluginVersion(this));
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

        Objects.requireNonNull(this.getCommand("acmdhelp")).setExecutor(new CommandExecutorHelp(this));
        Objects.requireNonNull(this.getCommand("acmd")).setExecutor(new CommandExecutorAcmd(this));
        Objects.requireNonNull(this.getCommand("acmdreload")).setExecutor(new CommandExecutorReload(this));
        Objects.requireNonNull(this.getCommand("acmdTime")).setExecutor(new CommandExecutorTime(this));

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &aLoaded"));
        return verified;
    }

    public void Unload() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &cUnloaded"));
    }

}
