package fr.lumi.CommandPatternObject;

import fr.lumi.Main;
import fr.lumi.Util.autocommand;
import org.bukkit.entity.Player;

public class CreateCommand extends Command{

    Player player;
    String acmdName;
    Long acmdCycle;
    Long acmdDelay;
    Integer acmdRepetitions;
    String acmdcommand;
    String ID;

    public CreateCommand(Main plg) {
        super(plg);
    }

    public void setPlayer(Player p) {
        player = p;
    }

    public void setacmdName(String n) {
        acmdName = n;
    }

    public void setacmdCycle(long c) {
        acmdCycle = c;
    }

    public void setacmdDelay(long d) {
        acmdDelay = d;
    }

    public void setacmdRepetitions(int r) {
        acmdRepetitions = r;
    }

    public void setacmdcommand(String c) {
        acmdcommand = c;
    }

    public void setacmdID(String id) {
        ID = id;
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

    private void setID(autocommand acmd) {
        acmd.setID(ID);
        int index = 0;
        while (plugin.acmdIdExist(acmd.getID())) {
            acmd.setID("acmd" + index);
            index++;
        }
    }


}
