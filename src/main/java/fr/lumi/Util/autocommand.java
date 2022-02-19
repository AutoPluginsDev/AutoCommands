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
    private boolean m_active=false;
    private String m_message="";
    private long m_delay = 0;
    private int shedulerId=0;
    private int m_Repetition=-1;
    private int m_RepetitionCounter=0;
    Main plugin;
    public autocommand(Main plg){plugin = plg;}


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

    public void setRepetition(int rep){m_Repetition = rep;}
    public int getRepetition(){return m_Repetition;}

    public void setRepetitionCounter(int rep){m_RepetitionCounter = rep;}
    public int getRepetitionCounter(){return m_RepetitionCounter;}

    public void setShedulerId(int id){shedulerId = id;}
    public int getsetShedulerId(){return shedulerId;}

public void addToScheduler(Main plg) throws IOException {

    if(m_active){
        if(getRepetition() != -1) setRepetitionCounter(0);
        shedulerId = plg.getServer().getScheduler().scheduleSyncRepeatingTask(plg, this, getDelay(), getCycle());
    }
    else
        plg.getServer().getScheduler().cancelTask(shedulerId);



}



    @Override
    public void run() {
        if(!Objects.equals(m_message, "")) Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&',m_message));
        Bukkit.getConsoleSender().sendMessage(plugin.getUt().replacePlaceHoldersForConsole(plugin.getLangConfig().getString("ConsoleExecutingMessage"),this));
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),getCommande());
        setRepetitionCounter(getRepetitionCounter()+1);
        if(getRepetition() != -1){
            if (getRepetitionCounter() == getRepetition()) {
                Bukkit.getConsoleSender().sendMessage(plugin.getUt().replacePlaceHoldersForConsole(plugin.getLangConfig().getString("OnRepetitionEnd"),this));
                plugin.getServer().getScheduler().cancelTask(shedulerId);
                try {
                    setActive(false,plugin.getCommandsConfig(),plugin);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public boolean saveInConfig(FileConfiguration config, Main plg) throws IOException {

        config.set(ID+"","");
        config.set(ID+".name",m_name);
        config.set(ID+".cycle",m_cycle);
        config.set(ID+".delay",m_delay);
        config.set(ID+".repetition",m_Repetition);
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
                    m_Repetition = config.getInt(ID+".repetition");
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
        for(int i=0;plugin.getLangConfig().isSet("onDysplayingAcmd."+i);i++){

            player.sendMessage(plugin.getUt().replacePlaceHoldersForPlayer(plugin.getLangConfig().getString("onDysplayingAcmd."+i),this));
        }

    }

    public void printToConsole(Main plugin){
        for(int i=0;plugin.getLangConfig().isSet("onDysplayingAcmd."+i);i++){

            Bukkit.getConsoleSender().sendMessage(plugin.getUt().replacePlaceHoldersForConsole(plugin.getLangConfig().getString("onDysplayingAcmd."+i),this));
        }
    }

}
