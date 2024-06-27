package fr.lumi.CommandPatternObject;

import fr.lumi.Main;
import fr.lumi.Util.autocommand;
import fr.lumi.Util.autocommandDataPattern;

public abstract class ACMDRelatedCommand extends autocommandDataPattern implements Command{
    Main plugin;
    public ACMDRelatedCommand(Main plg) {
        plugin = plg;
    }

    public abstract void execute();
    protected void setID(autocommand acmd) {
        acmd.setID(ID);
        int index = 0;
        while (plugin.acmdIdExist(acmd.getID())) {
            acmd.setID("acmd" + index);
            index++;
        }
    }
}
