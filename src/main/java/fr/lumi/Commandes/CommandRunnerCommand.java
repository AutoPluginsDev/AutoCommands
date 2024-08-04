package fr.lumi.Commandes;

import fr.lumi.CommandPatternObject.CreateACMDCommand;
import fr.lumi.CommandPatternObject.DeleteACMDCommand;
import fr.lumi.Main;
import fr.lumi.Util.ListAfficher;
import fr.lumi.Util.StringNumberVerif;
import fr.lumi.Util.autocommand;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandRunnerCommand implements CommandExecutor, TabCompleter {

    private Main plugin;
    private ListAfficher listAfficher;

    public CommandRunnerCommand(Main plg) {
        plugin = plg;
        listAfficher = new ListAfficher(plugin);
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> l = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("acmd")) {

            if (sender instanceof Player) {
                List<String> list = new ArrayList<>();
                if (args.length == 1) {
                    l.add("list");
                    l.add("new");
                    l.add("enable");
                    l.add("disable");
                    l.add("run");
                    l.add("stop");
                    l.add("edit");
                    l.add("delete");
                    l.add("info");
                    l.add("force");
                }

                if (args.length == 2) {
                    if (Objects.equals(args[0], "list")) {

                        for (int i = 1; i < listAfficher.getPageNumber() + 1; i++) l.add((i) + "");

                    }
                }


                if (args.length == 2) {
                    if (Objects.equals(args[0], "delete")
                            || (Objects.equals(args[0], "enable")
                            || Objects.equals(args[0], "disable")
                            || Objects.equals(args[0], "run")
                            || Objects.equals(args[0], "stop")
                            || Objects.equals(args[0], "info")
                            || Objects.equals(args[0], "force"))) {
                        for (autocommand acmd : plugin.getcommandList()) {
                            l.add(acmd.getID());
                        }
                    }
                }

                if (Objects.equals(args[0], "edit")) {
                    if (args.length == 2) {
                        for (autocommand acmd : plugin.getcommandList()) {
                            l.add(acmd.getID());
                        }
                    }
                    if (args.length == 3) l.add("setMessage");
                    if (args.length == 3) l.add("addCommand");
                    if (args.length == 3) l.add("removeCommand");
                    if (args.length == 3) l.add("setDailyExecutionTime");

                    if (args.length == 4) {
                        if (Objects.equals(args[2], "setMessage")) {
                            l.add("message");
                        }
                        if (Objects.equals(args[2], "addCommand"))
                            l.add("command");
                        if (Objects.equals(args[2], "removeCommand"))
                            if (plugin.acmdIdExist(args[1])) {
                                autocommand acmd = plugin.getacmdInList(args[1]);
                                int i = 0;
                                for (String command : acmd.getCommands()) {
                                    l.add(i + "");
                                    i++;
                                }
                            }
                        if (Objects.equals(args[2], "setDailyExecutionTime")) {
                            l.add("08H05");
                        }
                    }
                }
                if (Objects.equals(args[0], "new")) {
                    if (args.length == 2) l.add("[name]");
                    if (args.length == 3) l.add("[Period]");
                    if (args.length == 4) l.add("[DelayBeforeStart]");
                    if (args.length == 5) l.add("[Repetitions(-1=noLimit)]");
                    if (args.length == 6) l.add("[command]");
                }
            }
        }
        return l;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&7This commands require more arguments! Use /acmdhelp for more information."));
            return true;
        }

        CommandSender player = sender;

        if (args.length >= 1) {
            if (Objects.equals(args[0], "list")) {

                if (args.length == 1) {
                    listAfficher.printListToSender(0, player);
                    return true;
                }
                if (!StringNumberVerif.isDigit(args[1]))
                    return false;
                int index = Integer.parseInt(args[1]);

                if (index <= 0 || index > listAfficher.getPageNumber() + 1) { //need more args
                    player.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Try a page between 1 and " + (listAfficher.getPageNumber() + 1)));
                    return true;
                }
                listAfficher.printListToSender(index - 1, player);
            }
        }


        if (args.length == 2 && (Objects.equals(args[0], "disable") || Objects.equals(args[0], "run") || Objects.equals(args[0], "stop") || Objects.equals(args[0], "enable") || Objects.equals(args[0], "info") || Objects.equals(args[0], "force"))) {

            String id = args[1];
            autocommand acmd = getAcmdWithID(id, sender);
            if (acmd == null) {
                Bukkit.getConsoleSender().sendMessage("debug");
                return true;
            }
            if (Objects.equals(args[0], "run")) {
                if (!acmd.isActive()) {
                    player.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Try to enable the command " + acmd.getName() + " first with : /acmd enable " + acmd.getID()));
                    return true;
                }
                acmd.setRunning(true, plugin.getCommandsConfig());
                player.sendMessage(plugin.getUt().replacePlaceHoldersForPlayer(plugin.getLangConfig().getString("onRunAcmd"), acmd, (Player) player));
            }

            if (Objects.equals(args[0], "stop")) {
                acmd.setRunning(false, plugin.getCommandsConfig());
                player.sendMessage(plugin.getUt().replacePlaceHoldersForPlayer(plugin.getLangConfig().getString("onStopAcmd"), acmd, (Player) player));
            }
            if (Objects.equals(args[0], "enable")) {
                acmd.setActive(true);
                player.sendMessage(plugin.getUt().replacePlaceHoldersForPlayer(plugin.getLangConfig().getString("onEnablAcmd"), acmd, (Player) player));
            }

            if (Objects.equals(args[0], "disable")) {
                acmd.setRunning(false, plugin.getCommandsConfig());
                acmd.setActive(false);
                player.sendMessage(plugin.getUt().replacePlaceHoldersForPlayer(plugin.getLangConfig().getString("onDisableAcmd"), acmd, (Player) player));
                //player.sendMessage(plugin.getConfig().getString("Prefix")+" please reload the plugin with /acmdreload to make this change effective");
            }

            if (Objects.equals(args[0], "info")) {
                acmd.printToPlayer(sender);
            }

            if (Objects.equals(args[0], "force")) {
                acmd.runTest();
                sender.sendMessage(plugin.getUt().replacePlaceHoldersForPlayer("&6 Force running %acmdName ", acmd, (Player) player));
            }

        }


        if (Objects.equals(args[0], "edit") && args.length >= 2) {
            String id = args[1];
            autocommand acmd = getAcmdWithID(id, sender);
            if (acmd == null) return true;

            if (args.length == 2) {
                plugin.getModificationLock().lock(((Player)player).getUniqueId().toString());
                plugin.getAcmdGUIEditor().openACMDEditor((Player) sender, acmd, plugin.getcommandList().indexOf(acmd));
                return true;
            }

            if (Objects.equals(args[2], "setMessage")) {
                StringBuilder s = new StringBuilder();
                for (int i = 3; i <= args.length - 1; i++) {
                    s.append(args[i] + " ");
                }
                if (args.length == 3) s = new StringBuilder();
                acmd.setmessage(s.toString());
                acmd.saveInConfig(plugin.getCommandsConfig(), plugin);
            }

            if (Objects.equals(args[2], "addCommand")) {
                StringBuilder s = new StringBuilder();
                for (int i = 3; i <= args.length - 1; i++) {
                    s.append(args[i] + " ");
                }
                if (args.length == 3) return false;
                acmd.addCommand(s.toString());
                acmd.saveInConfig(plugin.getCommandsConfig(), plugin);
            }

            if (Objects.equals(args[2], "removeCommand")) {
                if (!StringNumberVerif.isDigit(args[3]))
                    return false;
                int index = Integer.parseInt(args[3]);
                if (index >= 0 && index < acmd.getCommandCount())
                    acmd.removeCommand(index);
                acmd.saveInConfig(plugin.getCommandsConfig(), plugin);
            }

            if (Objects.equals(args[2], "setDai" +
                    "lyExecutionTime")) {

                if (args.length == 3) {
                    acmd.setTime("");
                } else
                    acmd.setTime(args[3]);
                acmd.saveInConfig(plugin.getCommandsConfig(), plugin);
                sender.sendMessage(plugin.getUt().replacePlaceHoldersForPlayer(plugin.getLangConfig().getString("CommandEdited"), acmd, (Player) player));
            }
        }


        if (Objects.equals(args[0], "delete") && args.length >= 2) {

            String id = args[1];
            autocommand acmd = getAcmdWithID(id, sender);

            if (acmd == null) return true;
            DeleteACMDCommand cmd = new DeleteACMDCommand(plugin);
            cmd.setAcmdToDelete(acmd);
            cmd.setPlayer((Player)player);

            // sending to execution
            plugin.executeCommand(cmd);
        }


        if (Objects.equals(args[0], "new") && args.length >= 4) {

            CreateACMDCommand cmd = new CreateACMDCommand(plugin);

            cmd.setPlayer((Player) player);

            long cycle;
            long delay;
            int repetitions;

            cmd.setacmdName(args[1]);
            if (!StringNumberVerif.isDigit(args[2]))
                return false;

            cycle = (long) Float.parseFloat(args[2]);
            cmd.setacmdCycle(cycle);

            if (!StringNumberVerif.isDigit(args[3]))
                return false;
            delay = (long) Float.parseFloat(args[3]);
            cmd.setacmdDelay(delay);

            if (!StringNumberVerif.isDigit(args[4]))
                return false;
            repetitions = Integer.parseInt(args[4]);

            cmd.setacmdRepetitions(repetitions);

            StringBuilder s = new StringBuilder();
            for (int i = 5; i <= args.length - 1; i++) {
                s.append(args[i] + " ");
            }

            cmd.setacmdcommand(s.toString());
            cmd.setacmdID("acmd" + plugin.getcommandList().size());
            plugin.executeCommand(cmd);

        }


        return true;
    }

    private autocommand getAcmdWithID(String id, CommandSender player) {
        if (!plugin.acmdIdExist(id)) {
            player.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("try a valid command ID"));
            return null;
        }
        return plugin.getacmdInList(id);

    }
}