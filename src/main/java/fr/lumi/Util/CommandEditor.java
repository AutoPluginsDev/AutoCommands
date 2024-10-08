package fr.lumi.Util;

import fr.lumi.CommandPatternObject.CreateACMDCommand;
import fr.lumi.CommandPatternObject.DeleteACMDCommand;
import fr.lumi.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class CommandEditor implements Listener {
    private int LastOpened;
    private Inventory GUI_ChooseACMD;
    private Inventory GUI_Triggers;
    private ArrayList<Inventory> GUI_Commands;
    private String waitForChat;
    private ArrayList<Inventory> editorsListe;
    private final Main plugin;
    private int commandToModify;

    public CommandEditor(Main plg) {
        plugin = plg;
        commandToModify = -1;


        createGUI_ChoosingACMD();
        reloadGUI_ChoosingACMD();

        createTriggerMenu();
        reloadTriggerMenu();

        createEditGui();
        reloadAllEditGUI();
        waitForChat = "";
    }


    public void createEditGui() {
        editorsListe = new ArrayList<>();
        Bukkit.getServer().getConsoleSender().sendMessage(plugin.getUt().replacePlaceHoldersForConsolePlgVar("&2indexing the menus.."));
        for (autocommand acmd : plugin.getcommandList()) {

            editorsListe.add(createGUI_EditACMD(acmd));
        }
    }

    /*
     * Opening the main acmd inventory
     *  making sure that the lock is free before opening the inventory
     */
    public void openchoosing(Player p) {
        createEditGui();
        reloadGUI_ChoosingACMD();

        // enables a lock with the player's UUID to prevent other players from using the command editor
        boolean authorized = plugin.getModificationLock().lock(p.getUniqueId().toString());

        if (!authorized) {
            p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar(plugin.getLangConfig().getString("onEditorIsLockedError")));
            return;
        }
        p.openInventory(GUI_ChooseACMD);
    }

    /*
     * Close the inventory of the player
     *  making sure that if an inventory is closed, the lock is removed
     */
    public void closeInventory(Player p) {
        p.closeInventory();
        clearLock(p);
    }

    public void closeLastTennantInventory() {
        // get player by uuid and not by name
        Player p = Bukkit.getPlayer(UUID.fromString(plugin.getModificationLock().getLastTennant()));


        if (p != null) {

            if (p.getOpenInventory().equals(GUI_ChooseACMD)) {
                closeInventory(p);
            }
            if (editorsListe.contains(p.getOpenInventory())) {
                closeInventory(p);
            }


        }
    }

    public void openACMDEditor(Player p,int nb) {
        //createGUI_EditACMD(acmd);

        p.openInventory(editorsListe.get(nb));
        LastOpened = nb;
    }

    /*
     * Event handler for the inventory click event
     * Responsible of the click event in the inventory and the actions to be taken
     */
    @EventHandler
    public void GuiClickEvent(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;

        Player p = (Player) e.getWhoClicked();
        int slot = e.getSlot();

        //menu choose
        if (e.getClickedInventory().equals(GUI_ChooseACMD)) {
            e.setCancelled(true);
            if (slot < plugin.getcommandList().size()) {
                closeInventory(p);
                openACMDEditor(p, slot);
            } else if (slot == 53) {

                // Creating a new acmd using the CreateCommand.
                CreateACMDCommand cmd = new CreateACMDCommand(plugin);
                cmd.setacmdName("myNewAcmd");
                cmd.setPlayer(p);
                cmd.setacmdID("acmd" + plugin.getcommandList().size());

                // sending to execution
                plugin.executeCommand(cmd);
                closeInventory(p);
                createEditGui();
                reloadGUI_ChoosingACMD();
                openchoosing(p);
            }
        }else if ( e.getClickedInventory().equals(GUI_Triggers)){
            e.setCancelled(true);
            switch (slot){
                case 3:
                    plugin.getcommandList().get(LastOpened).setTrigger("onPlayerJoin");
                    openLast(((Player) e.getWhoClicked()).getPlayer());
                    break;
                case 4:
                    plugin.getcommandList().get(LastOpened).setTrigger("onPlayerQuit");
                    openLast(((Player) e.getWhoClicked()).getPlayer());
                    break;
                case 5:
                    plugin.getcommandList().get(LastOpened).setTrigger("onServerEnable");
                    openLast(((Player) e.getWhoClicked()).getPlayer());
                    break;
            }
            plugin.getcommandList().get(LastOpened).saveInConfig(plugin.getCommandsConfig(),plugin);
            closeInventory(p);
            reloadAllEditGUI();
            openACMDEditor(p,LastOpened);
        } /*else if ( e.getClickedInventory().equals(GUI_Commands)){
            e.setCancelled(true);

            if( slot < 53){
                if (e.isRightClick()){
                    autocommand acmd = plugin.getcommandList().get(LastOpened);
                    acmd.removeCommand(slot);
                    acmd.saveInConfig(plugin.getCommandsConfig(),plugin);
                    closeInventory(p);
                    openACMDEditor(p,LastOpened);
                }else if (e.isLeftClick()){
                    waitForChat = "command";
                    commandToModify = slot;

                    closeInventory(p);
                }

            }

            switch (slot){
                case 53:
                    waitForChat = "command";
                    commandToModify = -1;
                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Type the command in the chat (type exit to exit) :"));
                    closeInventory(p);
                    break;
            }
            plugin.getcommandList().get(LastOpened).saveInConfig(plugin.getCommandsConfig(),plugin);
            closeInventory(p);
            openACMDEditor(p,LastOpened);
        } */else if (editorsListe.contains(e.getClickedInventory())) {
            //menu edit
            e.setCancelled(true);

            autocommand acmd = plugin.getcommandList().get(editorsListe.indexOf(e.getClickedInventory()));

            switch (slot) {
                case 53:
                    if (acmd == null) return;
                    DeleteACMDCommand cmd = new DeleteACMDCommand(plugin);
                    cmd.setAcmdToDelete(acmd);
                    cmd.setPlayer(p);

                    // sending to execution
                    plugin.executeCommand(cmd);

                    closeInventory(p);

                    createEditGui();
                    reloadGUI_ChoosingACMD();
                    openchoosing(p);
                    return;

                case 44:
                    closeInventory(p);
                    p.openInventory(GUI_ChooseACMD);
                    break;

                case 0:
                    //waitForChat = "ID";
                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&bNothing to do yet with this button"));
                    //p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Type the ID in the chat (type exit to exit) :"));
                    //closeInventory(p);
                    break;
                case 1:
                    acmd.setActive(!acmd.isActive());
                    break;
                case 2:
                    acmd.setRunning(!acmd.isRunning(), plugin.getCommandsConfig());
                    break;
                case 3:
                    waitForChat = "period";

                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Type the period in the chat in tick (format : integer, type exit to exit) :"));
                    closeInventory(p);
                    break;

                case 4:
                    waitForChat = "delay";

                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Type the delay in the chat in tick (format : integer, type exit to exit) :"));
                    closeInventory(p);
                    break;
                case 5:
                    waitForChat = "hour";

                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Type the hour in the chat (example : 18H02, type exit to exit) :"));
                    closeInventory(p);
                    break;
                case 6:
                    waitForChat = "command";

                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Type the command in the chat (type exit to exit) :"));
                    closeInventory(p);
                    break;
                case 7:
                    waitForChat = "repetition";

                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Type the repetition in the chat (format : integer , type exit to exit) :"));
                    closeInventory(p);
                    break;
                case 8:
                    waitForChat = "message";

                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Type the hour in the chat (tips : use & + color , type \"exit\" to exit) :"));
                    closeInventory(p);
                    break;
                case 9:
                    waitForChat = "name";

                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Type the name in the chat (type exit to exit) :"));
                    closeInventory(p);
                    break;
                case 10:

                    //p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Type the trigger in the chat (possible values: onPlayerJoin, onPlayerQuit, onServerEnable)(type exit to exit) :"));

                    closeInventory(p);
                    p.openInventory(GUI_Triggers);
                    break;

            }
            acmd.saveInConfig(plugin.getCommandsConfig(), plugin);
            reloadAllEditGUI();
            reloadGUI_ChoosingACMD();

        }

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        if (Objects.equals(e.getPlayer().getUniqueId().toString(), plugin.getModificationLock().getLastTennant()) && !Objects.equals(waitForChat, "")) {
            e.setCancelled(true);
            String message = e.getMessage();

            updateACMDWithValue(message, e.getPlayer());

            waitForChat = "";
            reloadAllEditGUI();
            reloadGUI_ChoosingACMD();

            openLast(e.getPlayer());
        }
    }


    private void openLast(Player p ){
        plugin.getServer().getScheduler().callSyncMethod(plugin, () -> {
            openACMDEditor(p, LastOpened);
            return null;
        });
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory() == null) return;
        if (e.getInventory().equals(GUI_ChooseACMD)) {
            clearLock((Player) e.getPlayer());
        }
        if (editorsListe.contains(e.getInventory())) {
            clearLock((Player) e.getPlayer());
        }
    }

    private void clearLock(Player p) {
        plugin.getModificationLock().unlock(p.getUniqueId().toString());
    }

    public void updateACMDWithValue(String val, Player p) {
        if (Objects.equals(val, "exit")){
            p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&aExit with succes"));
            clearLock(p);
            return;
        }

        autocommand acmd = plugin.getcommandList().get(LastOpened);

        switch (waitForChat) {

            case "ID":
                acmd.setName(val);
                acmd.saveInConfig(plugin.getCommandsConfig(), plugin);//sauvegarde de la commande dans le fichier de commands
                //p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&aName modified with succes"));
                //p.openInventory(editorsListe.get(LastOpened));
                clearLock(p);
                break;
            case "period":
                if (StringNumberVerif.isDigit(val)) {
                    acmd.setCycle(Integer.parseInt(val));
                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&aPeriod modified with succes"));
                } else {
                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Period must be integer"));
                }
                clearLock(p);
                break;

            case "delay":
                if (StringNumberVerif.isDigit(val)) {
                    acmd.setDelay(Integer.parseInt(val));
                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&aDelay modified with succes"));
                } else {
                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Delay must be integer"));
                }
                clearLock(p);
                break;
            case "hour":

                acmd.setTime(val);
                p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&aDaily execution time modified with succes"));

                //p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Daily execution time must be like 18H02"));
                clearLock(p);
                break;

            case "command":

                acmd.addCommand(val);
                p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&aCommand modified with succes"));
                clearLock(p);
                break;

            case "repetition":

                if (StringNumberVerif.isDigit(val)) {
                    acmd.setRepetition(Integer.parseInt(val));
                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&aRepetition modified with succes"));
                } else {
                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Repetition must be integer"));
                }
                clearLock(p);
                break;

            case "message":

                acmd.setmessage(val);
                p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&aMessage modified with succes"));
                clearLock(p);
                break;
            case "name":
                acmd.setName(val);
                p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&aName modified with succes"));
                clearLock(p);


        }
        acmd.saveInConfig(plugin.getCommandsConfig(), plugin);
    }
    public void reloadGUI_ChoosingACMD() {
        GUI_ChooseACMD.clear();
        fillMainMenu();
    }

    public void createGUI_ChoosingACMD() {
        GUI_ChooseACMD = Bukkit.createInventory(null, 54, "§2§l§oACMD editor");
        fillMainMenu();
    }

    public void createTriggerMenu() {
        GUI_Triggers = Bukkit.createInventory(null, 9, "§2§l§oSelect a new trigger");
        fillTriggerMenu();
    }

    public void reloadTriggerMenu(){
        GUI_Triggers.clear();
        fillTriggerMenu();
    }

    /*public void createCommandMenu(autocommand acmd) {
        GUI_Commands = Bukkit.createInventory(null, 54, "§2§l§oSelect a new command");
        fillCommandMenu(acmd);
    }*/

    /*public void reloadCommandMenu(autocommand acmd){
        GUI_Commands.clear();
        fillCommandMenu(acmd);
    }*/

    /*public void fillCommandMenu(autocommand acmd){
        for (int i = 0; i < 54; i++) {
            new UIItem.ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, "§8").setItem(GUI_Commands, i);
        }

        for (int i = 0; i < acmd.getCommands().size(); i++) {
            ArrayList<String> lore = new ArrayList<String>();
            lore.add("§c§lleft Click to delete the command");
            lore.add("§c§lright Click to modify the command");
            new UIItem.ItemBuilder(Material.COMMAND_BLOCK, "§6§l" + acmd.getCommands().get(i)).setItem(GUI_Commands, i);
        }

        new UIItem.ItemBuilder(Material.COMMAND_BLOCK, "§6§lAdd a minecraft command")
                .lore("§dAdd a new command to the list")
                .setItem(GUI_Commands, 53);
    }*/


    public void fillTriggerMenu(){
        for (int i = 0; i < 8; i++) {
            new UIItem.ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, "§8").setItem(GUI_Triggers, i);
        }
        new UIItem.ItemBuilder(Material.GREEN_CONCRETE, "§6§lOnPlayerJoin")
                .lore("§dTriggers when a player joins the server")
                .setItem(GUI_Triggers, 3);
        new UIItem.ItemBuilder(Material.GREEN_CONCRETE, "§6§lOnPlayerQuit")
                .lore("§dTriggers when a player leaves the server")
                .setItem(GUI_Triggers, 4);
        new UIItem.ItemBuilder(Material.GREEN_CONCRETE, "§6§lOnServerEnable")
                .lore("§dTriggers when the server is enabled")
                .setItem(GUI_Triggers, 5);
    }


    public void fillMainMenu(){
        for (int i = 0; i < 54; i++) {
            new UIItem.ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, "§8").setItem(GUI_ChooseACMD, i);
        }
        int index = 0;
        for (autocommand acmd : plugin.getcommandList()) {

            ArrayList<String> lore = new ArrayList<String>();
            int index2 = 0;
            for (String s : acmd.getCommands()) {
                lore.add("§9-" + index2 + " -> §d" + s);
                index2++;
            }
            lore.add("§7(§aclick to open the editor§7)");

            new UIItem.ItemBuilder(Material.CHAIN_COMMAND_BLOCK, "§6§l" + acmd.getName()).lore(lore).setItem(GUI_ChooseACMD, index);
            index++;
        }
        new UIItem.ItemBuilder(Material.GREEN_CONCRETE, "§6§lNew ACMD")
                .lore("§dCreate a new auto command")
                .setItem(GUI_ChooseACMD, 53);

    }

    public Inventory fillGUI_EditACMD(autocommand acmd, Inventory gui) {

        List<String> lore = new ArrayList<String>();
        lore.add("§d" + acmd.getID());
        lore.add("§7(§aYou can modify it in the commands.yml file§7)");
        new UIItem.ItemBuilder(Material.PAPER, "§eID").lore(lore).setItem(gui, 0);

        // Active
        lore = new ArrayList<String>();
        lore.add(acmd.isActive() ? "§aEnabled" : "§cDisabled");

        new UIItem.ItemBuilder(Material.LEVER, "§eActivate the autoCommand").lore(lore).setItem(gui, 1);

        // Running
        lore = new ArrayList<String>();
        lore.add(acmd.isRunning() ? "§aRunning" : "§cStopped");

        new UIItem.ItemBuilder(Material.STONE_BUTTON, "§eRun the autoCommand").lore(lore).setItem(gui, 2);

        // Period
        lore = new ArrayList<String>();
        if (acmd.getCycleInSec() < 10 && acmd.getCycleInSec() > 0) {
            lore.add("§c" + acmd.getCycle() + "(short cycle)");
            lore.add("§c" + acmd.getCycleInSec() + "(short cycle)");
        } else if (acmd.getCycleInSec() == 0) {
            lore.add("§c" + acmd.getCycle() + "(very short cycle)");
            lore.add("§c" + acmd.getCycleInSec() + "(very short cycle )");
        } else {
            lore.add("§a" + acmd.getCycle());
            lore.add("§a" + acmd.getCycleInSec());
        }

        new UIItem.ItemBuilder(Material.CLOCK, "§eChange the Period").lore(lore).setItem(gui, 3);

        // Delay
        new UIItem.ItemBuilder(Material.CLOCK, "§eChange the Delay")
                .lore("§a" + acmd.getDelay() + " tick").
                setItem(gui, 4);

        // Daily execution
        new UIItem.ItemBuilder(Material.SUNFLOWER, "§eChange the Daily execution hour").lore("§a" + acmd.getTime()).setItem(gui, 5);


        // Commands
        lore = new ArrayList<String>();
        int index = 0;
        for (String s : acmd.getCommands()) {
            lore.add("§9ID " + index + " -> §6" + s);
            index++;
        }

        lore.add("§7( §aUse in-game commands or commands.yml ");
        lore.add("§afile to remove or modify the commands easily");
        lore.add("§e ->You can use :");

        lore.add("§e     -/acmd edit acmd0 removeCommand CommandID");

        lore.add("§a      to remove a command with its CommandID §7)");

        new UIItem.ItemBuilder(Material.COMMAND_BLOCK, "§eAdd a minecraft command").lore(lore).setItem(gui, 6);


        // RepeatTime
        new UIItem.ItemBuilder(Material.COMPARATOR, "§eModify number of Repetition").lore("§a" + acmd.getRepetition()).setItem(gui, 7);

        // Message
        new UIItem.ItemBuilder(Material.WRITABLE_BOOK, "§eModify the message").lore("§a" + acmd.getmessage()).setItem(gui, 8);


        // Name
        new UIItem.ItemBuilder(Material.ITEM_FRAME, "§eModify Name").lore("§a" + acmd.getName()).setItem(gui, 9);

        new UIItem.ItemBuilder(Material.BELL, "§eSelect a new trigger").lore("§a" + acmd.getTrigger()).setItem(gui, 10);

        // Delete button
        new UIItem.ItemBuilder(Material.RED_CONCRETE, "§4§lX §4DELETE THIS ACMD")
                .lore("§cThis action is irreversible !")
                .setItem(gui, 53);

        // Back button
        new UIItem.ItemBuilder(Material.ORANGE_CONCRETE, "§6<- Return")
                .lore("§eBack to the main page")
                .setItem(gui, 44);

        //for all empty slots in the inventory we put a glass pane. Test if the slot is empty
        for (int i = 0; i < 54; i++) {
            if (gui.getItem(i) == null) {
                new UIItem.ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, "§8").setItem(gui, i);
            }
        }

        //reloadCommandMenu(acmd);

        return gui;
    }

    public Inventory createGUI_EditACMD(autocommand acmd) {
        Inventory gui = Bukkit.createInventory(null, 54, "§8§oEditing " + acmd.getID());
        return fillGUI_EditACMD(acmd, gui);
    }

    public void reloadAllEditGUI() {
        int index = 0;
        for (Inventory gui : editorsListe) {
            RefreshGUI_EditACMD(plugin.getcommandList().get(index), gui);
            index++;
        }
    }

    public void RefreshGUI_EditACMD(autocommand acmd, Inventory gui) {
        gui.clear();
        fillGUI_EditACMD(acmd, gui);
    }


}
