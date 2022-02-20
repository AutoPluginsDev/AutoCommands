package fr.lumi.Commandes;

import fr.lumi.Main;
import fr.lumi.Util.Utilities;
import fr.lumi.Util.autocommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandRunnerCommand implements CommandExecutor, TabCompleter {


    Main plugin;
    public CommandRunnerCommand(Main plg)

    {
        plugin = plg;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> l = new ArrayList<>();
        if(cmd.getName().equalsIgnoreCase("acmd")){

            if(sender instanceof Player){
                List<String> list = new ArrayList<>();
                if (args.length == 1){
                    l.add("list");
                    l.add("new");
                    l.add("enable");
                    l.add("disable");
                    l.add("edit");
                    l.add("delete");
                }

                if (args.length == 2 ){
                    if(Objects.equals(args[0], "delete") ||(Objects.equals(args[0], "enable")|| Objects.equals(args[0], "disable"))){
                        for(autocommand acmd : plugin.getcommandList()){
                            l.add(acmd.getID());
                        }
                    }
                }

                if(Objects.equals(args[0], "edit")){
                    if (args.length == 2 ){
                        for(autocommand acmd : plugin.getcommandList()){
                            l.add(acmd.getID());
                        }
                    }
                    if (args.length == 3 ) l.add("setMessage");
                    if (args.length == 3 ) l.add("addCommand");
                    if (args.length == 3 ) l.add("removeCommand");
                    if (args.length == 3 ) l.add("setDaylyExecutionTime");

                    if(args.length == 4){
                        if(Objects.equals(args[2], "setMessage"))
                            l.add("message to display at this acmd's execution");
                        if(Objects.equals(args[2], "addCommand"))
                            l.add("command");
                        if(Objects.equals(args[2], "removeCommand"))
                            if(plugin.acmdIdExist(args[1])){
                                autocommand acmd = plugin.getacmdInList(args[1]);
                                int i =0;
                                for(String command : acmd.getCommands()){
                                    l.add(i+"");
                                    i++;
                                }
                            }
                        if(Objects.equals(args[2], "setDaylyExecutionTime"))
                            l.add("hhHmm (h->hour,m->minutes");
                    }


                }


                if(Objects.equals(args[0], "new")){
                    if (args.length == 2 ) l.add("NAME");
                    if (args.length == 3 ) l.add("Loop Time (tick) 20tick->1second");
                    if (args.length == 4 ) l.add("Delay At Start (tick) 20tick->1second");
                    if (args.length == 5 ) l.add("Repetitions(-1 = no limit)");
                    if (args.length == 6 ) l.add("YOUR COMMAND");
                    if (args.length == 6 ) l.add("If you need to display a message, use /acmd setMessage ID [message]");
                }
            }
        }
        return l;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandSender player = sender;
        if(args.length == 0) return false;
        if(args.length == 1 ){

            if(Objects.equals(args[0], "list")){
                if(plugin.getLangConfig().getString("CommandListTop") !="")
                    player.sendMessage(plugin.getUt().replacePlaceHoldersForPlayer(plugin.getLangConfig().getString("CommandListTop"),new autocommand(plugin)));
                for(int i = 0;i< plugin.getcommandList().size();i++){
                    autocommand cmd = plugin.getcommandList().get(i);
                    cmd.printToPlayer(player,plugin);
                }
                if(plugin.getLangConfig().getString("CommandListBottom") !="")
                    player.sendMessage(plugin.getUt().replacePlaceHoldersForPlayer(plugin.getLangConfig().getString("CommandListBottom"),new autocommand(plugin)));
            }
        }
        if(args.length == 2 ){
            if(Objects.equals(args[0], "enable")) {
                String id = args[1];
                if(!plugin.acmdIdExist(id)) return false;
                //initializing the new command
                autocommand cmd;

                cmd = plugin.getacmdInList(id);

                try {
                    cmd.setActive(true,plugin.getCommandsConfig(),plugin);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                player.sendMessage(plugin.getUt().replacePlaceHoldersForPlayer(plugin.getLangConfig().getString("onEnablAcmd"),cmd));
            }

            if(Objects.equals(args[0], "disable")) {
                String id = args[1];
                if(!plugin.acmdIdExist(id)) return false;
                //initializing the new command
                autocommand cmd;
                cmd = plugin.getacmdInList(id);
                try {
                    cmd.setActive(false, plugin.getCommandsConfig(), plugin);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                player.sendMessage(plugin.getUt().replacePlaceHoldersForPlayer(plugin.getLangConfig().getString("onDisableAcmd"),cmd));
                //player.sendMessage(plugin.getConfig().getString("Prefix")+" please reload the plugin with /acmdreload to make this change effective");
            }




        }
       if(Objects.equals(args[0], "edit") && args.length >=2){

           String id = args[1];
           if(!plugin.acmdIdExist(id)) return false;
           //initializing the new command
           autocommand cmd;
           cmd = plugin.getacmdInList(id);

           if(Objects.equals(args[2], "setMessage")) {

                StringBuilder s = new StringBuilder();
                for (int i=3;i<=args.length-1;i++){
                    s.append(args[i]+" ");
                }
                if(args.length == 3) s = new StringBuilder();
                    cmd.setmessage(s.toString());
                try {
                    cmd.saveInConfig(plugin.getCommandsConfig(), plugin);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
           if(Objects.equals(args[2], "addCommand")) {


               StringBuilder s = new StringBuilder();
               for (int i=3;i<=args.length-1;i++){
                   s.append(args[i]+" ");
               }
               if(args.length == 3) return false;
               cmd.addCommand(s.toString());
               try {
                   cmd.saveInConfig(plugin.getCommandsConfig(), plugin);
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }

           if(Objects.equals(args[2], "removeCommand")) {

               try{
                   int index = Integer.parseInt(args[3]);
               }catch (NumberFormatException e) {
                   return false;
               }
               int index = Integer.parseInt(args[3]);
               if(index >=0 && index < cmd.getCommandCount() )
               cmd.removeCommand(index);
               try {
                   cmd.saveInConfig(plugin.getCommandsConfig(), plugin);
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }

           if(Objects.equals(args[2], "setDaylyExecutionTime")) {


               if(args.length == 3){
                   cmd.setTime("");
               }
               else
                   cmd.setTime(args[3]);

               try {
                   cmd.saveInConfig(plugin.getCommandsConfig(), plugin);
               } catch (IOException e) {
                   e.printStackTrace();
               }
               player.sendMessage(plugin.getUt().replacePlaceHoldersForPlayer(plugin.getLangConfig().getString("CommandEdited"),cmd));
           }








        }

        if(args.length >= 2 ){
            if(Objects.equals(args[0], "delete")){

            String id = args[1];
            if(!plugin.acmdIdExist(id)) return false;
            //initializing the new command
            autocommand cmd;
            cmd = plugin.getacmdInList(id);

            //desactivation of the command
            try {
                cmd.setActive(false,plugin.getCommandsConfig(),plugin);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                cmd.delete(plugin.getCommandsConfig(), plugin);
            } catch (IOException e) {
                e.printStackTrace();
            }

            }
        }



        if(args.length >= 4 ){
            if(Objects.equals(args[0], "new")){

                long cycle;
                long delay;
                int repetitions;

                autocommand cmd = new autocommand(plugin);
                cmd.setName(args[1]);
                try{
                    cycle = (long) Float.parseFloat(args[2]);
                }catch (NumberFormatException e) {
                    return false;
                }

                cmd.setCycle(cycle);
                if(cmd.getCycle() < 200) {
                    player.sendMessage(plugin.getUt().replacePlaceHoldersForPlayer(plugin.getLangConfig().getString("AlertShortCycle"),cmd));

                }

                try{
                    delay = (long) Float.parseFloat(args[3]);
                }catch (NumberFormatException e) {
                    return false;
                }

                cmd.setDelay(delay);

                try{
                    repetitions = Integer.parseInt(args[4]);
                }catch (NumberFormatException e) {
                    return false;
                }


                cmd.setRepetition(repetitions);

                StringBuilder s = new StringBuilder();
                for (int i=5;i<=args.length-1;i++){
                    s.append(args[i]+" ");
                }

                cmd.addCommand(s.toString());

                cmd.setID("acmd"+plugin.getcommandList().size());

                player.sendMessage(plugin.getUt().replacePlaceHoldersForPlayer(plugin.getLangConfig().getString("onAddingANewCommand"),cmd));


                try {
                    cmd.saveInConfig(plugin.getCommandsConfig(),plugin);//sauvegarde de la commande dans le fichier de commands
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    cmd.setActive(cmd.isActive(),plugin.getCommandsConfig(),plugin);
                    plugin.getcommandList().add(cmd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }
}