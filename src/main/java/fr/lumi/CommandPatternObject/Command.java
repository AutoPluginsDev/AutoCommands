package fr.lumi.CommandPatternObject;
import fr.lumi.Main;
public abstract class Command {
    Main plugin;
    public Command(Main plg) {
        plugin = plg;
    }

    public abstract void execute();


}
