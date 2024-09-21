package fr.lumi.events;
import fr.lumi.Main;
import fr.lumi.Util.autocommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;

public class ACMDEventWatcher implements Listener {
    Main plugin;

    public ACMDEventWatcher(Main plg) {
        this.plugin = plg;
    }

    public void runAcmd(String event) {
        plugin.getLogger().info("Event triggered by acmd: " + event);
        for (autocommand acmd : plugin.getcommandList()) {
            plugin.getLogger().info("Comparing with: " + acmd.getTrigger());
            if (acmd.getTrigger().equalsIgnoreCase(event)) {
                acmd.setRunning(true, plugin.getCommandsConfig());
                plugin.getLogger().info("Command triggered by event: " + event);
            }
        }
    }

    public void runAcmdOnPlayer(String event, Player p) {
        for (autocommand acmd : plugin.getcommandList()) {
            if (acmd.isActive() && acmd.getTrigger().equalsIgnoreCase(event)) {
                acmd.setRunning(true, plugin.getCommandsConfig());
                plugin.getLogger().info(plugin.getUt().replacePlaceHolders("Command triggered by event: " + event,acmd));
            }
        }
    }

    @EventHandler
    public void onPlayerJoin( PlayerJoinEvent event) {
        runAcmdOnPlayer("onPlayerJoin", event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit( PlayerQuitEvent event) {
        runAcmdOnPlayer("onPlayerQuit", event.getPlayer());
    }

    //when the server is enabled
    @EventHandler
    public void onEnable( ServerLoadEvent event) {
        runAcmd("onServerEnable");
    }

}
