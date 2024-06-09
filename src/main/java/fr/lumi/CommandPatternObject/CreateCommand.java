package fr.lumi.CommandPatternObject;

import fr.lumi.Main;
import fr.lumi.Util.StringNumberVerif;
import fr.lumi.Util.autocommand;
import org.bukkit.entity.Player;

public class CreateCommand extends Command{

    Player player;
    String acmdName;
    long acmdCycle;
    long acmdDelay;
    int acmdRepetitions;

    String acmdcommand;

    public CreateCommand(Main plg) {
        super(plg);
    }

    public void setPlayer(Player p) {
        player = p;
    }

    public void setacmdName(String n) {
        acmdName = n;
    }

    @Override
    public void execute() {


        autocommand cmd = new autocommand(plugin);
        cmd.setName(acmdName);


        cmd.setCycle(acmdCycle);
        if (cmd.getCycle() < 200) {
            player.sendMessage(plugin.getUt().replacePlaceHoldersForPlayer(plugin.getLangConfig().getString("AlertShortCycle"), cmd, (Player) player));
        }

        cmd.setDelay(acmdDelay);

        cmd.setRepetition(acmdRepetitions);

        cmd.addCommand(acmdcommand);

        cmd.setID("acmd" + plugin.getcommandList().size());
        int index = 0;

        while (plugin.acmdIdExist(cmd.getID())) {
            cmd.setID("acmd" + index);
            index++;
        }

        player.sendMessage(plugin.getUt().replacePlaceHoldersForPlayer(plugin.getLangConfig().getString("onAddingANewCommand"), cmd, (Player) player));
        cmd.saveInConfig(plugin.getCommandsConfig(), plugin); //sauvegarde de la commande dans le fichier de commands
        cmd.setRunning(cmd.isRunning(), plugin.getCommandsConfig());
        plugin.getcommandList().add(cmd);

    }
}
