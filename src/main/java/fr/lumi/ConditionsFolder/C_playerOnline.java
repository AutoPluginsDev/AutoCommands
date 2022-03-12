package fr.lumi.ConditionsFolder;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class C_playerOnline extends Condition {

    int onlinePlayerCondNumber = 0;
    int cnd =0;

    @Override
    public boolean verify() {
        return Bukkit.getServer().getOnlinePlayers().size() == onlinePlayerCondNumber;
    }

    @Override
    public String getName() {
        return "OnlinePlayer";
    }

    @Override
    protected void formatingParams() {
        if(m_parameters.get(0) == "<") cnd = -1;
        if(m_parameters.get(0) == "=") cnd = 0;
        if(m_parameters.get(0) == ">") cnd = 1;
        onlinePlayerCondNumber = Integer.parseInt(m_parameters.get(1));

    }

    @Override
    boolean paramVerifier() {
        return m_parameters.size() == 2;
    }


    @Override
    public String getCondition() {
        return "Online players "+m_parameters.get(0)+" "+m_parameters.get(1);
    }
}
