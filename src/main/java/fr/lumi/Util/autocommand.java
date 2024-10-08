package fr.lumi.Util;

import fr.lumi.ConditionsFolder.Condition;
import fr.lumi.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class autocommand implements Runnable {

    private String m_name = "";
    private long m_cycle = 200;
    private List<String> m_commands = new ArrayList<String>();

    private List<Condition> m_conditions = new ArrayList<Condition>();

    private String ID = "";
    private boolean m_running = false;
    private boolean m_Active = false;
    private String m_message = "";
    private long m_delay = 0;
    private int shedulerId = 0;
    private int m_Repetition = -1;
    private int m_RepetitionCounter = 0;
    private String m_time = "";
    private String m_error = "X";

    private String m_trigger = "";

    Main plugin;

    public autocommand(Main plg) {
        plugin = plg;
    }

    public void setRunning(boolean state, FileConfiguration config) {
        if (m_running == state) return;
        m_running = state;
        config.set(ID + ".TaskParameters.running", m_running);

        saveInConfig(config);
        addToScheduler();
    }

    public boolean isActive() {
        return m_Active;
    }

    public void setActive(boolean state) {
        m_Active = state;
    }

    public boolean isRunning() {
        return m_running;
    }

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    public String getTime() {
        return m_time;
    }

    public void setTime(String time) {
        m_time = time;
    }

    public String getmessage() {
        return m_message;
    }

    public void setmessage(String mess) {
        m_message = mess;
    }

    public long getCycle() {
        return m_cycle;
    }

    public void setCycle(long cycle) {
        m_cycle = cycle;
    }

    public void setDelay(long delay) {
        m_delay = delay;
    }

    public long getDelay() {
        return m_delay;
    }

    public float getCycleInSec() {
        return (float) getCycle() / 20;
    }

    public void delete(FileConfiguration config) {
        config.set(ID + "", null);
        plugin.getcommandList().remove(this);
        saveInConfig(config);
    }

    public void saveInConfig(FileConfiguration cfg) {
        try {
            cfg.save(plugin.getCommandsFile());
        } catch (IOException ignored) {
        }
    }


    public void addCommand(String cmd) {
        m_commands.add(cmd);
    }

    public void removeCommand(int commandID) {
        m_commands.remove(m_commands.get(commandID));
    }

    public int getCommandCount() {
        return m_commands.size();
    }

    public List<String> getCommands() {
        return m_commands;
    }


    public void addCondition(Condition cond) {
        m_conditions.add(cond);
    }

    public void removeCondition(int condID) {
        m_conditions.remove(m_conditions.get(condID));
    }


    public List<Condition> getConditions() {
        return m_conditions;
    }

    public String getTrigger() {
        return m_trigger;
    }

    public void setTrigger(String trigger) {
        m_trigger = trigger;
    }


    public String getStringFormatCommands() {
        int i = 0;
        StringBuilder s = new StringBuilder();
        for (String cmd : m_commands) {
            String cmd_ = "\n" + plugin.getConfig().getString("Prefix") + "&6" + "-ID : " + i + "-" + cmd;
            s.append(cmd_);
            i++;
        }
        return s.toString();
    }


    public String getError() {
        return m_error;
    }

    public void setError(String error) {
        m_error = error;
    }

    public void setID(String id) {
        ID = id;
    }

    public String getID() {
        return ID;
    }

    public void setRepetition(int rep) {
        m_Repetition = rep;
    }

    public int getRepetition() {
        return m_Repetition;
    }

    public void setRepetitionCounter(int rep) {
        m_RepetitionCounter = rep;
    }

    public int getRepetitionCounter() {
        return m_RepetitionCounter;
    }

    public void setShedulerId(int id) {
        shedulerId = id;
    }

    public int getsetShedulerId() {
        return shedulerId;
    }

    public void addToScheduler() {

        if (m_running) {
            if (getRepetition() != -1) setRepetitionCounter(0);
            shedulerId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, getDelay(), getCycle());
        } else
            cancel();
    }

    public void cancel() {

        plugin.getServer().getScheduler().cancelTask(shedulerId);
    }

    @Override
    public void run() {

        if (ConditionVerifier.verify(this)) {


            for (String command : m_commands) {
                String cmd = plugin.getUt().replacePlaceHolders(command, this);
                Bukkit.getConsoleSender().sendMessage(cmd);
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }


            setRepetitionCounter(getRepetitionCounter() + 1);
            if (getRepetition() != -1) {
                if (getRepetitionCounter() == getRepetition()) {
                    plugin.getServer().getScheduler().cancelTask(shedulerId);

                    setRunning(false, plugin.getCommandsConfig());

                }
            }
            try {
                Bukkit.getConsoleSender().sendMessage(plugin.getUt().replacePlaceHoldersForConsole(plugin.getLangConfig().getString("OnRepetitionEnd"), this));
                if (!Objects.equals(m_message, ""))
                    Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', m_message));
                Bukkit.getConsoleSender().sendMessage(plugin.getUt().replacePlaceHoldersForConsole(plugin.getLangConfig().getString("ConsoleExecutingMessage"), this));
            } catch (Exception e) {
                System.out.println("Verify your lang config file");
            }
        }

    }


    public void runTest() {

        if (!Objects.equals(m_message, ""))
            Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', m_message));
        Bukkit.getConsoleSender().sendMessage(plugin.getUt().replacePlaceHoldersForConsole(plugin.getLangConfig().getString("ConsoleExecutingMessage"), this));

        for (String command : m_commands) {
            Bukkit.getConsoleSender().sendMessage(command);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        }

    }


    public void saveInConfig(FileConfiguration config, Main plg) {
        config.set(ID + "", "");
        config.set(ID + ".active", m_Active);
        config.set(ID + ".TaskParameters", "");
        config.set(ID + ".TaskParameters.name", m_name);
        config.set(ID + ".TaskParameters.cycle", m_cycle);
        config.set(ID + ".TaskParameters.delay", m_delay);
        config.set(ID + ".TaskParameters.repetition", m_Repetition);
        config.set(ID + ".TaskParameters.running", m_running);
        if (!m_commands.isEmpty()) config.set(ID + ".TaskParameters.commands", m_commands);
        else config.set(ID + ".TaskParameters.commands", "");

        config.set(ID + ".TaskParameters.message", m_message);

        config.set(ID + ".DailySchedulerParameters", "");
        config.set(ID + ".DailySchedulerParameters.time", m_time);
        config.set(ID + ".TriggerParameters", "");
        config.set(ID + ".TriggerParameters.trigger", m_trigger);

        saveInConfig(config);


    }

    public boolean getInConfig(FileConfiguration config, Main plg, String id) {
        ID = id;
        m_Active = config.getBoolean(ID + ".active");
        m_running = config.getBoolean(ID + ".TaskParameters.running");

        m_name = config.getString(ID + ".TaskParameters.name");
        m_cycle = config.getLong(ID + ".TaskParameters.cycle");
        m_delay = config.getLong(ID + ".TaskParameters.delay");
        m_Repetition = config.getInt(ID + ".TaskParameters.repetition");

        if (!config.isSet(ID + ".TaskParameters.message")) {
            m_message = "";
        } else {
            m_message = config.getString(ID + ".TaskParameters.message");
        }
        m_time = config.getString(ID + ".DailySchedulerParameters.time");
        m_commands = config.getStringList(ID + ".TaskParameters.commands");
        m_trigger = config.getString(ID + ".TriggerParameters.trigger");
        return true;
    }

    public void printToPlayer(CommandSender player) {

        for (String line : plugin.getLangConfig().getStringList("onDysplayingAcmd")) {
            player.sendMessage(plugin.getUt().replacePlaceHoldersForPlayer(line, this, (Player) player));
        }
    }

    public void printToConsole() {
        for (String line : plugin.getLangConfig().getStringList("onDysplayingAcmd")) {
            Bukkit.getConsoleSender().sendMessage(plugin.getUt().replacePlaceHoldersForConsole(line, this));

        }
    }

    public boolean isConforme() {
        if (m_Repetition == -1 && m_time != "") {
            setError("infinite repetition but daily routine given");
            return false;
        }
        setError(m_error = "");
        return true;
    }


}
