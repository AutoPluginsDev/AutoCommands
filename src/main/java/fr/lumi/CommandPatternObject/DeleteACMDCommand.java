package fr.lumi.CommandPatternObject;

import fr.lumi.Main;
import fr.lumi.Util.autocommand;
import org.bukkit.entity.Player;

public class DeleteACMDCommand extends ACMDRelatedCommand{

    autocommand acmdToDelete;

    Player player;

    public void setAcmdToDelete(autocommand acmd) {
        acmdToDelete = acmd;
    }

    public void setPlayer(Player p) {
        player = p;
    }

    public DeleteACMDCommand(Main plg) {
        super(plg);
    }

    @Override
    public void execute() {

        acmdToDelete.setRunning(false, plugin.getCommandsConfig());

        acmdToDelete.delete(plugin.getCommandsConfig());

        player.sendMessage(plugin.getUt().replacePlaceHoldersForPlayer(plugin.getLangConfig().getString("onDeleteAcmd"), acmdToDelete, player));


    }
}
