package fr.lumi.ConditionsFolder;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class C_playerOnline extends Condition {

    int onlinePlayerCondNumber = 0;


    @Override
    public boolean verify() {
        return Bukkit.getServer().getOnlinePlayers().size() == onlinePlayerCondNumber;
    }

    @Override
    public String getName() {
        return "OnlinePlayer";
    }

    @Override
    public String getParam(int id) {
        return null;
    }

    @Override
    boolean paramVerifier() {
        return m_parameters.size() == 2;
    }


    @Override
    public String getCondition() {
        return null;
    }
}
