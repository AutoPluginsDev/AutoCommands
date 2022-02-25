package fr.lumi.FileVerifiers;

import fr.lumi.Main;

import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.HashMap;

public class LangFileVerification extends cfgFileVerification {

    private HashMap<String,Object> keys = new HashMap<>();


    public LangFileVerification(Main plg) {
        super(plg,plg.getLangConfig(),"| ERROR IN LANG.YML |","lang.yml");
        keys.put("onEnablAcmd","");
        keys.put("onDisableAcmd","");
        keys.put("onRunAcmd","");
        keys.put("onStopAcmd","");
        keys.put("AlertShortCycle","");
        keys.put("onAddingANewCommand","");
        keys.put("onDeleteAcmd","");
        keys.put("CommandListTop","");
        keys.put("CommandListBottom","");
        keys.put("OnReload","");
        keys.put("OnRepetitionEnd","");
        keys.put("ConsoleExecutingMessage","");
        keys.put("CommandEdited","");
        keys.put("onDysplayingAcmd","");
        super.setKeys(keys);
    }

    @Override
    public void savemodif() {
        plugin.saveLangFile();
    }


}
