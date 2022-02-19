package fr.lumi.Util;

import fr.lumi.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Objects;


public class autocommand implements Runnable {

    private String m_name="";
    private long m_cycle=0;
    private String m_commande="";
    private int ID = 0;
    private boolean m_active=true;
    private String m_message="";
    private long m_delay = 0;
    private int shedulerId=0;
    public autocommand(){}


    public void setActive(boolean state, FileConfiguration config,Main plg) throws IOException {
        m_active=state;
        config.set(ID+".active",m_active);

        config.save(plg.getCommandsFile());
        addToScheduler(plg);
    }

    public boolean isActive() {return m_active;}

    public String getName(){return m_name;}
    public void setName(String name){m_name = name;}

    public String getmessage(){return m_message;}
    public void setmessage(String mess){m_message = mess;}



    public long getCycle() {return m_cycle;}
    public void setCycle(long cycle) {m_cycle = cycle;}

    public void setDelay(long delay){m_delay = delay;}
    public long getDelay() {return m_delay ;}


    public float getCycleInSec(){
        return (float)getCycle()/20;
    }

    public String getCommande() {return m_commande;}
    public void setCommande(String commande) {m_commande = commande;}

    public void setID(int id){ID = id;}
    public int getID(){return ID;}

    public void setShedulerId(int id){shedulerId = id;}
    public int getsetShedulerId(){return shedulerId;}

public void addToScheduler(Main plg){

    if(m_active)
        shedulerId = plg.getServer().getScheduler().scheduleSyncRepeatingTask(plg, this, getDelay(), getCycle());
    else
        plg.getServer().getScheduler().cancelTask(shedulerId);

}



    @Override
    public void run() {

        if(!Objects.equals(m_message, "")) Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&',m_message));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&eExecuting -> "+m_commande));
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),getCommande());

    }

    public boolean saveInConfig(FileConfiguration config, Main plg) throws IOException {

        config.set(ID+"","");
        config.set(ID+".name",m_name);
        config.set(ID+".cycle",m_cycle);
        config.set(ID+".delay",m_delay);
        config.set(ID+".command",m_commande);
        config.set(ID+".active",m_active);
        config.set(ID+".message",m_message);
        config.save(plg.getCommandsFile());
        return true;
    }

    public boolean getInConfig(FileConfiguration config,Main plg,int id){

            for(int i =0;i< plg.getCommandsConfig().getKeys(false).size();i++){
                if(id == i){
                    ID = id ;
                    m_name = config.getString(+ID+".name");
                    m_cycle = config.getLong(ID+".cycle");
                    m_delay= config.getLong(ID+".delay");
                    m_commande = config.getString(ID+".command");
                    m_active = config.getBoolean(ID+".active");
                    m_message = config.getString(ID+".message");
                }
            }
            return true;
        }


    public boolean eraseInConfig(FileConfiguration config,Main plg,int id){

        for(int i =0;i< plg.getCommandsConfig().getKeys(false).size();i++){
            if(id == i){
                config.set(ID+"","");
            }
        }
        return true;
    }
    public void printToPlayer(Player player,Main plugin){
        if(isActive()) player.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Prefix"))+" §aActive acmd -");
        else player.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Prefix"))+" §cInactive acmd -");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Prefix"))+" §eID : " +getID()+" §a"+getName()+"§e every §c"+getCycleInSec()+" sec");
        if(getmessage() != "") player.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Prefix"))+" §eDisplay message : "+ChatColor.translateAlternateColorCodes('&',getmessage()));
        else player.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Prefix"))+" §eNo message displayed when executed ");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Prefix"))+"      §ecmd: -> §6"+getCommande());
    }
    public void printToConsole(Main plugin){
        if(isActive()) Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("ConsolePrefix"))+" §aActive acmd -");
        else Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("ConsolePrefix"))+" §cInactive acmd -");
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("ConsolePrefix"))+" §eID : " +getID()+" §a"+getName()+"§e every §c"+getCycleInSec()+" sec");
        if(getmessage() != "") Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("ConsolePrefix"))+" §eDisplay message : "+ChatColor.translateAlternateColorCodes('&',getmessage()));
        else Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("ConsolePrefix"))+" §eNo message displayed when executed ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("ConsolePrefix"))+"      §ecmd: -> §6"+getCommande());
    }

}
