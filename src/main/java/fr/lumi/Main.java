package fr.lumi;

import fr.lumi.Commandes.CommandRunnerCommand;
import fr.lumi.Commandes.CommandRunnerConf;
import fr.lumi.Commandes.CommandRunnerHelp;

import fr.lumi.Commandes.CommandRunnerReload;
import fr.lumi.Util.Utilities;
import fr.lumi.Util.autocommand;
import fr.lumi.Util.daylyCommandExecuter;
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

    daylyCommandExecuter executer;


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

    public Utilities getUt(){
        return m_ut;
    }


    private List<autocommand> commandList = new ArrayList<autocommand>();
    public List<autocommand> getcommandList(){
        return commandList;
    }


    @Override
    public void onEnable() {
        m_ut = new Utilities(this);

        saveDefaultConfig();
        getRessourceFile(getLangFile(),"lang.yml",this);
        getRessourceFile(getCommandsFile(),"commands.yml",this);

        try {
            Load();
        } catch (IOException e) {
            e.printStackTrace();
        }



        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("ConsolePrefix")+" &aOn"));
    }

    @Override
    public void onDisable() {
        Unload();

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("ConsolePrefix")+" &cOff"));
    }

    public void Load() throws IOException {
        reloadConfig();
        config = getConfig();

        Objects.requireNonNull(this.getCommand("acmdhelp")).setExecutor(new CommandRunnerHelp(this));
        Objects.requireNonNull(this.getCommand("acmd")).setExecutor(new CommandRunnerCommand(this));
        Objects.requireNonNull(this.getCommand("acmdreload")).setExecutor(new CommandRunnerReload(this));

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("ConsolePrefix")+ "&e---------------Loading "+getCommandsConfig().getKeys(false).size()+" AutoComands---------------"));

        if(!loadCommandsTimeTable()) Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("ConsolePrefix")+"&6No AutoComands to execute"));

        //dayly executor enable
        executer = new daylyCommandExecuter(this,getcommandList());
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, executer, 0, executer.getrefreshRate());

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("ConsolePrefix")+" &e-------------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("ConsolePrefix")+" &aLoaded"));

    }

    public void Unload(){
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("ConsolePrefix")+" &cUnloaded"));
    }


    public autocommand getacmdInList(String idRequested){
        int index = 0;
        for(String idInConfig : getCommandsConfig().getKeys(false)){
            if(Objects.equals(idInConfig, idRequested)){
                return getcommandList().get(index);
            }
            index++;
        }
        return null;
    }

    public boolean acmdIdExist(String idRequested){

        for(String idInConfig : getCommandsConfig().getKeys(false)){
            autocommand cmd = new autocommand(this);
            if(Objects.equals(idInConfig, idRequested)){
                return true;
            }
        }
        return false;
    }


    public boolean loadCommandsTimeTable() throws IOException {
        commandList.clear();
        getServer().getScheduler().cancelTasks(this);
        BukkitScheduler scheduler = this.getServer().getScheduler();



        for(String i : getCommandsConfig().getKeys(false)){
            autocommand cmd = new autocommand(this);
            if(cmd.getInConfig(getCommandsConfig(),this,i)){
                cmd.printToConsole(this);
                cmd.addToScheduler(this);
                commandList.add(cmd);
                cmd.saveInConfig(commandsConfig,this);
            }
        }
        return true;
    }

    public int getEnbaledCommand(){
        int count=0;
        for(autocommand a:getcommandList()){
            if (a.isActive()) count++;
        }
        return count;
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
