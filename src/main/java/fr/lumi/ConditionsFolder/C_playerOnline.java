package fr.lumi.ConditionsFolder;

import org.bukkit.Bukkit;

public class C_playerOnline extends Condition {

    public C_playerOnline() {
    }


    int onlinePlayerCondNumber = 0;
    int cnd = 0;

    @Override
    public boolean verify() {
        if (cnd == -1) return Bukkit.getServer().getOnlinePlayers().size() < onlinePlayerCondNumber;
        else if (cnd == 0) return Bukkit.getServer().getOnlinePlayers().size() == onlinePlayerCondNumber;
        else return Bukkit.getServer().getOnlinePlayers().size() > onlinePlayerCondNumber;
    }

    @Override
    public String getName() {
        return "OnlinePlayer";
    }

    @Override
    protected void formatingParams() {
        if (paramVerifier()) {
            if (m_parameters.get(0) == "<") cnd = -1;
            else if (m_parameters.get(0) == "=") cnd = 0;
            else if (m_parameters.get(0) == ">") cnd = 1;
            onlinePlayerCondNumber = Integer.parseInt(m_parameters.get(1));
        }

    }

    @Override
    boolean paramVerifier() {
        return m_parameters.size() == 2;
    }


    @Override
    public String getCondition() {
        return "Online players " + m_parameters.get(0) + " " + m_parameters.get(1);
    }
}
