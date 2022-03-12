package fr.lumi.FileVerifiers;

import fr.lumi.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public abstract class cfgFileVerification {

    public abstract void savemodif();


    Main plugin;
    protected HashMap<String,Object> keyList;
    private final String errorMess;
    private final String filename;
    private final boolean m_versionCorrect=true;
    private final FileConfiguration fileconf;

    public boolean isCorrect(){
        return m_versionCorrect;
    }


    cfgFileVerification(Main plg, FileConfiguration fg, String errorMessage,String fname){
        filename = fname;
        fileconf = fg ;
        plugin =plg;
        errorMess = errorMessage;
    }



    public void setKeys(HashMap<String,Object> keys){
        keyList = keys;
    }

    public void Verif(){
        boolean m_versionCorrect=true;
        for(String s : keyList.keySet()){
            if(!fileconf.isSet(s)){
                fileconf.set(s,keyList.get(s));
                Bukkit.getConsoleSender().sendMessage(plugin.getUt().replacePlaceHoldersForConsolePlgVar("§4"+errorMess+"-> "+s +" key is not set"));
                m_versionCorrect = false;
            }
        }
        if( !m_versionCorrect) {
            Bukkit.getConsoleSender().sendMessage(plugin.getUt().replacePlaceHoldersForConsolePlgVar("&4Your file " + filename + " is not up to date, go on the github page (/acmdhelp) to get the latest version of the .yml files."));
            Bukkit.getConsoleSender().sendMessage(plugin.getUt().replacePlaceHoldersForConsolePlgVar("&4The plugin may not work,please go repair that file."));
        }

        savemodif();
    }





}
