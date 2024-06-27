package fr.lumi.CommandPatternObject;

import fr.lumi.Main;
import fr.lumi.Util.autocommand;
import org.bukkit.entity.Player;

public class CreateACMDCommand extends ACMDRelatedCommand {

    Player player;

    public CreateACMDCommand(Main plg) {
        super(plg);
    }

    public void setPlayer(Player p) {
        player = p;
    }


    @Override
    public void execute() {

        autocommand acmd = new autocommand(plugin);

        if (acmdName != null) {
            acmd.setName(acmdName);
        }

        if (acmdCycle != null) {
            acmd.setCycle(acmdCycle);
            if (acmd.getCycle() < 200) {
                player.sendMessage(plugin.getUt().replacePlaceHoldersForPlayer(plugin.getLangConfig().getString("AlertShortCycle"), acmd, (Player) player));
            }
        }

        if (acmdDelay != null) {
            acmd.setDelay(acmdDelay);
        }

        if (acmdRepetitions != null) {
            acmd.setRepetition(acmdRepetitions);
        }

        if (acmdcommand != null) {
            acmd.addCommand(acmdcommand);
        }

        if (ID == null) {
            ID = "acmd";
        }

        setID(acmd);

        player.sendMessage(plugin.getUt().replacePlaceHoldersForPlayer(plugin.getLangConfig().getString("onAddingANewCommand"), acmd, (Player) player));
        acmd.saveInConfig(plugin.getCommandsConfig(), plugin); //sauvegarde de la commande dans le fichier de commands
        acmd.setRunning(acmd.isRunning(), plugin.getCommandsConfig());
        plugin.getcommandList().add(acmd);

    }




}
