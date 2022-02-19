package fr.lumi;

import fr.lumi.Commandes.CommandRunnerCommand;
import fr.lumi.Commandes.CommandRunnerConf;
import fr.lumi.Commandes.CommandRunnerHelp;

import fr.lumi.Commandes.CommandRunnerReload;
import fr.lumi.Util.Utilities;
import fr.lumi.Util.autocommand;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.*;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public final class Main extends JavaPlugin {

    FileConfiguration config = getConfig();

    //command file creation
    private File commandsFile = new File(getDataFolder(),"commands.yml");
    private FileConfiguration commandsConfig = YamlConfiguration.loadConfiguration(commandsFile);
    public FileConfiguration getCommandsConfig() {
        commandsConfig = YamlConfiguration.loadConfiguration(commandsFile);
        return commandsConfig;
    }
    public File getCommandsFile() {
        commandsFile= new File(getDataFolder(),"commands.yml");
        return commandsFile;
    }

    //translatefile gestion
    private File Langfile = new File(getDataFolder(),"lang.yml");
    private FileConfiguration Langconfig= YamlConfiguration.loadConfiguration(Langfile);
    public FileConfiguration getLangConfig() {
        Langconfig = YamlConfiguration.loadConfiguration(Langfile);
        return Langconfig;
    }
    public File getLangFile() {
        Langfile= new File(getDataFolder(),"lang.yml");
        return Langfile;
    }


    private Utilities m_ut;

    private List<autocommand> commandList = new ArrayList<autocommand>();
    public List<autocommand> getcommandList(){
        return commandList;
    }


    @Override
    public void onEnable() {
        m_ut = new Utilities(this);

        saveDefaultConfig();
        if(!getCommandsFile().exists()){
            saveResource("commands.yml",false);
            try {
                getCommandsConfig().save(getCommandsFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        getRessourceFile(getLangFile(),"lang.yml",this);


        try {
            Load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("ConsolePrefix")+"On"));
    }

    @Override
    public void onDisable() {
        Unload();

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("ConsolePrefix")+"Off"));
    }

    public void Load() throws IOException {
        reloadConfig();
        config = getConfig();
        //getCommandsConfig().save(getCommandsFile());

        Objects.requireNonNull(this.getCommand("acmdconf")).setExecutor(new CommandRunnerConf(this));
        Objects.requireNonNull(this.getCommand("acmdhelp")).setExecutor(new CommandRunnerHelp(this,m_ut));
        Objects.requireNonNull(this.getCommand("acmd")).setExecutor(new CommandRunnerCommand(this,m_ut));
        Objects.requireNonNull(this.getCommand("acmdreload")).setExecutor(new CommandRunnerReload(this,m_ut));

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("ConsolePrefix"))+ "---------------Loading "+getCommandsConfig().getKeys(false).size()+" AutoComands---------------");

        if(!loadCommandsTimeTable()) Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("ConsolePrefix"))+"No AutoComands to execute");

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("ConsolePrefix"))+" -------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("ConsolePrefix"))+" Loaded");

    }

    public void Unload(){
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("ConsolePrefix"))+" Unloaded");
    }

    public boolean loadCommandsTimeTable() throws IOException {

        commandList.clear();
        getServer().getScheduler().cancelTasks(this);

        BukkitScheduler scheduler = this.getServer().getScheduler();

        for(int i = 0;i< getCommandsConfig().getKeys(false).size();i++){
            autocommand cmd = new autocommand();
            if(cmd.getInConfig(getCommandsConfig(),this,i)){

                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("ConsolePrefix"))+"  >>AutoCommand §a"+cmd.getName()+"§e succesfully loaded");
                cmd.printToConsole(this);

                cmd.addToScheduler(this);
                commandList.add(cmd);

                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("ConsolePrefix"))+" | -------------------------------------------------");
            }
        }
        return true;
    }
    public long convertToTick(long seconds){
        return (long) seconds*20;
    }
    public static YamlConfiguration getRessourceFile(File file, String resource, Main plugin) {
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

        return YamlConfiguration.loadConfiguration(file);
    }


}
