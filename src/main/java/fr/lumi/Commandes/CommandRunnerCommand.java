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
    Utilities m_ut;
    public CommandRunnerCommand(Main plg, Utilities ut)

    {
        m_ut =ut;
        plugin = plg;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> l = new ArrayList<>();
        if(cmd.getName().equalsIgnoreCase("acmd") ){
            if(sender instanceof Player){
                List<String> list = new ArrayList<>();
                if (args.length == 1){
                    l.add("list");
                    l.add("new");
                    l.add("delete");
                    l.add("enable");
                    l.add("disable");
                    l.add("setMessage");
                }
                if (args.length == 2 && (Objects.equals(args[0], "list") || Objects.equals(args[0], "delete") )) l.add("ID");
                if (args.length == 2 &&  Objects.equals(args[0], "setMessage") ) l.add("ID");
                if (args.length == 2 && (Objects.equals(args[0], "enable") || Objects.equals(args[0], "disable") )) l.add("ID");

                if(Objects.equals(args[0], "setMessage")){
                    if (args.length == 3 ) l.add("message to display at this acmd's execution");
                }

                if(Objects.equals(args[0], "new")){
                    if (args.length == 2 ) l.add("NAME");
                    if (args.length == 3 ) l.add("Loop Time (tick) 20tick->1second");
                    if (args.length == 4 ) l.add("Delay At Start (tick) 20tick->1second");
                    if (args.length == 5 ) l.add("YOUR COMMAND");
                    if (args.length == 5 ) l.add("If you need to display a message, use /acmd setMessage ID [message]");
                }
            }
        }
        return l;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if(args.length == 1 ){

                if(Objects.equals(args[0], "list")){

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Prefix"))+" ---§6 acmd running : "+plugin.getcommandList().size()+"---");
                    for(int i = 0;i< plugin.getcommandList().size();i++){
                        autocommand cmd = plugin.getcommandList().get(i);
                        cmd.printToPlayer(player,plugin);
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Prefix"))+" ---------------------");
                }
            }
            if(args.length == 2 ){
                if(Objects.equals(args[0], "enable")) {
                    autocommand cmd;
                    int id = Integer.parseInt(args[1]);
                    if( id < 0 || id > plugin.getcommandList().size()-1) return false;

                        cmd = plugin.getcommandList().get(id);
                        try {
                            cmd.setActive(true,plugin.getCommandsConfig(),plugin);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    player.sendMessage(m_ut.replacePlaceHoldersForPlayer(plugin.getLangConfig().getString("onEnablAcmd"),cmd));
                }

                if(Objects.equals(args[0], "disable")) {
                    autocommand cmd;
                    int id = Integer.parseInt(args[1]);
                    if( id <0 || id > plugin.getcommandList().size()-1) return false;
                    cmd = plugin.getcommandList().get(Integer.parseInt(args[1]));

                    try {
                        cmd.setActive(false, plugin.getCommandsConfig(), plugin);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.sendMessage(m_ut.replacePlaceHoldersForPlayer(plugin.getLangConfig().getString("onDisableAcmd"),cmd));
                    //player.sendMessage(plugin.getConfig().getString("Prefix")+" please reload the plugin with /acmdreload to make this change effective");
                }




            }
            if(args.length >=2){
                if(Objects.equals(args[0], "setMessage")) {
                    autocommand cmd;
                    int id = Integer.parseInt(args[1]);
                    if( id <0 || id > plugin.getcommandList().size()-1) return false;
                    cmd = plugin.getcommandList().get(Integer.parseInt(args[1]));

                    StringBuilder s = new StringBuilder();
                    for (int i=2;i<=args.length-1;i++){
                        s.append(args[i]+" ");
                    }
                    if(args.length == 2) s = new StringBuilder();
                    cmd.setmessage(s.toString());
                    try {
                        cmd.saveInConfig(plugin.getCommandsConfig(), plugin);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }



            if(args.length >= 4 ){
                if(Objects.equals(args[0], "new")){
                    autocommand cmd = new autocommand();
                    cmd.setName(args[1]);

                    cmd.setCycle((long) Float.parseFloat(args[2]) );
                    if(cmd.getCycle() < 200) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Prefix"))+" §a"+cmd.getName()+"§e will be executed every §c"+(float)cmd.getCycle()/20+" seconds.");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Prefix"))+" §c§l"+(float)cmd.getCycle()/20+" seconds is verry short and may cause lag when frequently used.");
                    }

                    cmd.setDelay((long) Float.parseFloat(args[3]));

                    StringBuilder s = new StringBuilder();
                    for (int i=4;i<=args.length-1;i++){
                        s.append(args[i]+" ");
                    }

                    cmd.setCommande(s.toString());

                    cmd.setID(plugin.getcommandList().size());

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Prefix"))+" Command "+cmd.getName()+" succesfully saved !");
                    //player.sendMessage(plugin.getConfig().getString("Prefix")+" please reload the plugin with /acmdreload to make this change effective");


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
        }
        return true;
    }
}