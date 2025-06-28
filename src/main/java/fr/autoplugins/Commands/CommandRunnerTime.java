package fr.autoplugins.Commands;

import fr.autoplugins.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.time.ZoneId;
import java.util.Date;
import java.time.LocalDateTime;

public class CommandRunnerTime implements CommandExecutor {

    Main plugin;

    public CommandRunnerTime(Main plg) {

        plugin = plg;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Date dateInput = new Date();
        LocalDateTime currentDateTime = LocalDateTime.now();
        sender.sendMessage(plugin.getUt().replacePlaceHolders("full date: " + currentDateTime));
        int Hours = dateInput.toInstant().atZone(ZoneId.systemDefault()).getHour();
        int minutes = dateInput.toInstant().atZone(ZoneId.systemDefault()).getMinute();
        String hour;
        String minute;
        if (Hours < 10) hour = "0" + Hours;
        else hour = "" + Hours;
        if (minutes < 10) minute = "0" + minutes;
        else minute = "" + minutes;
        String HourString = hour + "H" + minute;
        Bukkit.getConsoleSender().sendMessage(plugin.getUt().replacePlaceHolders("It is " + HourString));
        sender.sendMessage(plugin.getUt().replacePlaceHolders("It is " + HourString));
        return true;
    }
}