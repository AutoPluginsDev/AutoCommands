package fr.lumi;

import fr.lumi.Commandes.CommandRunnerCommand;
import fr.lumi.Commandes.CommandRunnerHelp;

import fr.lumi.Commandes.CommandRunnerReload;
import fr.lumi.FileVerifiers.ConfigFileVerification;
import fr.lumi.FileVerifiers.LangFileVerification;
import fr.lumi.Util.Utilities;
import fr.lumi.Util.autocommand;
import fr.lumi.Util.dailyCommandExecuter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Main extends JavaPlugin {

    private String[] Logo ={
    "&e&9     &6__     __ &e ",
    "&e&9 /\\ &6/  |\\/||  \\&e|  &9Auto&6Commands &aVersion &e1.2.1",
    "&e&9/--\\&6\\__|  ||__/&e|  &8running on bukkit - paper",
    ""};




    FileConfiguration config = getConfig();

    dailyCommandExecuter executer;


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

    public boolean saveCommandsFile() {
        try {
            getCommandsConfig().save(getCommandsFile());
        } catch (IOException ignored){
            return false;
        }
        return true;
    }



    //translatefile gestion
    private File Langfile = new File(getDataFolder(),"lang.yml");
    private FileConfiguration Langconfig= YamlConfiguration.loadConfiguration(Langfile);

    public FileConfiguration getLangConfig() {
        Langconfig = YamlConfiguration.loadConfiguration(Langfile);
        return Langconfig;
    }

    public FileConfiguration getLangConfigWithoutreload() {

        return Langconfig;
    }


    public File getLangFile() {
        Langfile= new File(getDataFolder(),"lang.yml");
        return Langfile;
    }

    public boolean saveLangFile() {

        try {
            Bukkit.getConsoleSender().sendMessage("saving the lang");
            getLangConfigWithoutreload().save(getLangFile());
        } catch (IOException ignored){
            return false;
        }
        return true;
    }


    private Utilities m_ut;

    public Utilities getUt(){
        return m_ut;
    }


    private List<autocommand> commandList = new ArrayList<autocommand>();
    public List<autocommand> getcommandList(){
        return commandList;
    }

    //FileVerifiers
    private LangFileVerification LangVerif = new LangFileVerification(this);
    private ConfigFileVerification ConfigVerif = new ConfigFileVerification(this);


    @Override
    public void onEnable() {
        long  start = System.currentTimeMillis();

        m_ut = new Utilities(this);

        for(String s :Logo)//print the logo
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',s));

        saveDefaultConfig();


        ConfigVerif.Verif();
        LangVerif.Verif();

        getRessourceFile(getLangFile(),"lang.yml",this);
        getRessourceFile(getCommandsFile(),"commands.yml",this);

        Load();
        long exeTime = System.currentTimeMillis() - start;

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("ConsolePrefix")+" &aOn (took "+exeTime+" ms)"));



    }


    @Override
    public void onDisable() {
        Unload();

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("ConsolePrefix")+" &cOff"));
    }

    public void Load() {
        reloadConfig();
        config = getConfig();

        //verification des fichiers configs



        Objects.requireNonNull(this.getCommand("acmdhelp")).setExecutor(new CommandRunnerHelp(this));
        Objects.requireNonNull(this.getCommand("acmd")).setExecutor(new CommandRunnerCommand(this));
        Objects.requireNonNull(this.getCommand("acmdreload")).setExecutor(new CommandRunnerReload(this));

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("ConsolePrefix")+ "&e-Loading "+getCommandsConfig().getKeys(false).size()+" AutoComands-"));

        if(!loadCommandsTimeTable()) Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("ConsolePrefix")+"&6No AutoComands to execute"));

        //daily executor enable
        executer = new dailyCommandExecuter(this,getcommandList());
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, executer, 0, executer.getrefreshRate());
        if(getConfig().getBoolean("DisplayAcmdInConsole"))
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


    public boolean loadCommandsTimeTable() {
        commandList.clear();
        getServer().getScheduler().cancelTasks(this);
        for(String i : getCommandsConfig().getKeys(false)){
            autocommand acmd = new autocommand(this);
            if(acmd.getInConfig(getCommandsConfig(),this,i)){
/*
                int index=0;
                while(acmdIdExist(acmd.getID())){
                    Bukkit.getConsoleSender().sendMessage(acmd.getID());
                    acmd.setID(acmd.getID() +"_"+index);
                    index++;
                }*/

                if(getConfig().getBoolean("DisplayAcmdInConsole"))
                    acmd.printToConsole();

                acmd.addToScheduler();
                commandList.add(acmd);
                acmd.saveInConfig(commandsConfig,this);
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

    public int getRunningCommand(){
        int count=0;
        for(autocommand a:getcommandList()){
            if (a.isRunning()) count++;
        }
        return count;
    }



    public long convertToTick(long seconds){
        return (long) seconds*20;
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
