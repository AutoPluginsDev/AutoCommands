package fr.lumi.CommandPatternObject;

import fr.lumi.Main;
import fr.lumi.Util.autocommandDataPattern;

public abstract class ACMDRelatedCommand extends autocommandDataPattern {
    Main plugin;
    public ACMDRelatedCommand(Main plg) {
        plugin = plg;
    }

    public abstract void execute();

}
