package fr.lumi.Util;

import fr.lumi.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CommandEditor implements Listener {
    private int LastOpened;
    private Inventory GUI_ChooseACMD;
    private Player user;
    private String waitForChat;
    private  ArrayList<Inventory> editorsListe;
    private final Main plugin;
    public CommandEditor(Main plg){
        plugin = plg;


        createGUI_ChoosingACMD();
        reloadGUI_ChoosingACMD();

        createEditGui();
        reloadAllEditGUI();
        waitForChat = "";
    }


    public void createEditGui(){
        editorsListe = new ArrayList<>();
        Bukkit.getServer().getConsoleSender().sendMessage(plugin.getUt().replacePlaceHoldersForConsolePlgVar("&2indexing the menus.."));
        for(autocommand acmd : plugin.getcommandList()){

            editorsListe.add(createGUI_EditACMD(acmd));
        }
    }

    public void openchoosing(Player p ){
        createEditGui();
        reloadGUI_ChoosingACMD();
        p.openInventory(GUI_ChooseACMD);
    }

    public void openACMDEditor(Player p , autocommand acmd,int nb ){
        //createGUI_EditACMD(acmd);
        user = p;
        p.openInventory(editorsListe.get(nb));
        LastOpened = nb;
    }


    @EventHandler
    public void GuiClickEvent(InventoryClickEvent e){
        if(e.getClickedInventory() == null) return;

        Player p = (Player) e.getWhoClicked();
        int slot =e.getSlot();
        //menu choose
        if(e.getClickedInventory().equals(GUI_ChooseACMD)){
            e.setCancelled(true);
            if(slot < plugin.getcommandList().size()){
                //p.sendMessage("opening editor for the acmd "+plugin.getcommandList().get(slot).getName());
                p.closeInventory();
                openACMDEditor(p,plugin.getcommandList().get(slot),slot);
            }

            else if(slot == 53){//create a new acmd

                autocommand acmd = new autocommand(plugin);
                acmd.setName("myNewAcmd");
                acmd.setID("acmd" + plugin.getcommandList().size());

                int index=0;

                while(plugin.acmdIdExist(acmd.getID())){
                    acmd.setID("acmd" +index);
                    index++;
                }

                acmd.saveInConfig(plugin.getCommandsConfig(), plugin);//sauvegarde de la commande dans le fichier de commands
                acmd.setRunning(acmd.isRunning(), plugin.getCommandsConfig(), plugin);
                plugin.getcommandList().add(acmd);

                createEditGui();
                reloadGUI_ChoosingACMD();
                openchoosing(p);
                p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayer(plugin.getLangConfig().getString("onAddingANewCommand"), acmd));
            }
        }
        //menu edit
        if(editorsListe.contains(e.getClickedInventory())){
            e.setCancelled(true);

            autocommand acmd = plugin.getcommandList().get(editorsListe.indexOf(e.getClickedInventory()));

            switch (slot){
                case 53 :
                    if(acmd == null) return ;
                    //desactivation of the command

                    acmd.setRunning(false, plugin.getCommandsConfig(), plugin);

                    acmd.delete(plugin.getCommandsConfig());

                    p.closeInventory();

                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayer(plugin.getLangConfig().getString("onDeleteAcmd"), acmd));
                    createEditGui();
                    reloadGUI_ChoosingACMD();
                    openchoosing(p);
                    return;

                case 44 :
                    p.closeInventory();
                    p.openInventory(GUI_ChooseACMD);
                    break;

                case 0 :
                    //waitForChat = "ID";
                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&bNothing to do yet with this button"));
                    //p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Type the ID in the chat (type exit to exit) :"));
                    //p.closeInventory();
                    break;
                case 1 :
                    acmd.setActive(!acmd.isActive());
                    break;
                case 2 :
                    acmd.setRunning(!acmd.isRunning(), plugin.getCommandsConfig(), plugin);
                    break;
                case 3 :
                    waitForChat = "period";

                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Type the period in the chat in tick (format : integer , type exit to exit) :"));
                    p.closeInventory();
                    break;

                case 4 :
                    waitForChat = "delay";

                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Type the delay in the chat in tick (format : integer , type exit to exit) :"));
                    p.closeInventory();
                    break;
                case 5 :
                    waitForChat = "hour";

                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Type the hour in the chat (format : 18H02 , type exit to exit) :"));
                    p.closeInventory();
                    break;
                case 6 :
                    waitForChat = "command";

                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Type the command in the chat (type exit to exit) :"));
                    p.closeInventory();
                    break;
                case 7 :
                    waitForChat = "repetition";

                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Type the repetition in the chat (format : integer , type exit to exit) :"));
                    p.closeInventory();
                    break;
                case 8 :
                    waitForChat = "message";

                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Type the hour in the chat (format : & usables , type exit to exit) :"));
                    p.closeInventory();
                    break;
                case 9 :
                    waitForChat = "name";

                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Type the name in the chat (type exit to exit) :"));
                    p.closeInventory();
            }
            acmd.saveInConfig(plugin.getCommandsConfig(),plugin);
            reloadAllEditGUI();
            reloadGUI_ChoosingACMD();

        }

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        if(e.getPlayer() == user && ! Objects.equals(waitForChat, "")){
            e.setCancelled(true);
            String message = e.getMessage();

            updateACMDWithValue(message, e.getPlayer());

            waitForChat = "";
            reloadAllEditGUI();
            reloadGUI_ChoosingACMD();
        }
    }

    public void updateACMDWithValue(String val,Player p){
        autocommand acmd = plugin.getcommandList().get(LastOpened);
        switch (waitForChat) {
            case "exit" :

                p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&aExit with succes"));
                break;
            case "ID" :
                acmd.setName(val);
                acmd.saveInConfig(plugin.getCommandsConfig(), plugin);//sauvegarde de la commande dans le fichier de commands
                //p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&aName modified with succes"));
                //p.openInventory(editorsListe.get(LastOpened));
                break;
            case "period" :
                if (StringNumberVerif.isDigit(val)){
                    acmd.setCycle(Integer.parseInt(val));
                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&aPeriod modified with succes"));
                }
                else {
                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Period must be integer"));
                }
                break;

            case "delay" :
                if (StringNumberVerif.isDigit(val)){
                    acmd.setDelay(Integer.parseInt(val));
                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&aDelay modified with succes"));
                }
                else {
                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Delay must be integer"));
                }
                break;
            case "hour" :

                acmd.setTime(val);
                p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&aDaily execution time modified with succes"));

                //p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Daily execution time must be like 18H02"));

                break;

            case "command" :

                acmd.addCommand(val);
                p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&aCommand modified with succes"));

                break;

            case "repetition" :

                if (StringNumberVerif.isDigit(val)){
                    acmd.setRepetition(Integer.parseInt(val));
                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&aRepetition modified with succes"));
                }
                else {
                    p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&4Repetition must be integer"));
                }

                break;

            case "message" :

                acmd.setmessage(val);
                p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&aMessage modified with succes"));

                break;
            case "name" :
                acmd.setName(val);
                p.sendMessage(plugin.getUt().replacePlaceHoldersForPlayerPlgVar("&aName modified with succes"));

        }
        acmd.saveInConfig(plugin.getCommandsConfig(),plugin);


    }

    public void reloadGUI_ChoosingACMD(){
        GUI_ChooseACMD.clear();
        int index=0;
        for(autocommand acmd : plugin.getcommandList()){

            ItemStack item = new ItemStack(Material.CHAIN_COMMAND_BLOCK,1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§6§l"+acmd.getName());
            ArrayList<String> lore = new ArrayList<String>();
            int index2 = 0;
            for(String s : acmd.getCommands()){
                lore.add("§9-"+index2+" -> §d"+s);
                index2++;
            }
            lore.add("§7(§aclick to open the editor§7)");

            meta.setLore(lore);
            item.setItemMeta(meta);

            GUI_ChooseACMD.setItem(index,item);
            index++;
        }
        ItemStack item = new ItemStack(Material.GREEN_CONCRETE,1);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<String>();
        lore.add( "§dCreate a new acmd");
        meta.setDisplayName("§6§lNew ACMD");
        meta.setLore(lore);
        item.setItemMeta(meta);
        GUI_ChooseACMD.setItem(53,item);
    }


    public void createGUI_ChoosingACMD(){
        GUI_ChooseACMD = Bukkit.createInventory(null, 54,"§2§l§oACMD editor");
        int index=0;
        for(autocommand acmd : plugin.getcommandList()){

            ItemStack item = new ItemStack(Material.CHAIN_COMMAND_BLOCK,1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§6§l"+acmd.getName());
            ArrayList<String> lore = new ArrayList<String>();
            int index2 = 0;
            for(String s : acmd.getCommands()){
                lore.add("§9-"+index2+" -> §d"+s);
                index2++;
            }
            lore.add("§7(§aclick to open the editor§7)");

            meta.setLore(lore);
            item.setItemMeta(meta);

            GUI_ChooseACMD.setItem(index,item);
            index++;
        }
        ItemStack item = new ItemStack(Material.GREEN_CONCRETE,1);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<String>();
        lore.add( "§dCreate a new acmd");
        meta.setDisplayName("§6§lNew ACMD");
        meta.setLore(lore);
        item.setItemMeta(meta);
        GUI_ChooseACMD.setItem(53,item);

    }

    public Inventory fillGUI_EditACMD(autocommand acmd, Inventory gui){
        ItemStack item = new ItemStack(Material.PAPER,1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§eID");

        List<String> lore = new ArrayList<String>();
        lore.add( "§d"+acmd.getID());
        lore.add("§7(§aYou can modify it in the commands.yml file§7)");
        meta.setLore(lore);
        item.setItemMeta(meta);
        gui.setItem(0,item);


        //active
        item = new ItemStack(Material.LEVER,1);
        meta = item.getItemMeta();
        meta.setDisplayName("§eActive");

        lore = new ArrayList<String>();
        if(acmd.isActive()){
            lore.add( "§aEnabled");
        }

        else{
            lore.add( "§cDisabled");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        gui.setItem(1,item);


        //running
        item = new ItemStack(Material.STONE_BUTTON,1);
        meta = item.getItemMeta();
        meta.setDisplayName("§eRun");

        lore = new ArrayList<String>();
        if(acmd.isRunning()){
            lore.add( "§aRunning");
        }
        else{
            lore.add( "§cStopped");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        gui.setItem(2,item);


        //period
        item = new ItemStack(Material.CLOCK,1);
        meta = item.getItemMeta();
        meta.setDisplayName("§ePeriod");

        lore = new ArrayList<String>();
        if (acmd.getCycleInSec() < 10 && acmd.getCycleInSec() > 0 ){
            lore.add("§c"+acmd.getCycle()+"(short cycle)");
            lore.add("§c"+acmd.getCycleInSec()+"(short cycle)");
        }
        else if(acmd.getCycleInSec() == 0){
            lore.add("§c"+acmd.getCycle()+"(very short cycle)");
            lore.add("§c"+acmd.getCycleInSec()+"(very short cycle )");
        }
        else{
            lore.add("§a"+acmd.getCycle());
            lore.add("§a"+ acmd.getCycleInSec());
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        gui.setItem(3,item);


        //delay
        item = new ItemStack(Material.CLOCK,1);
        meta = item.getItemMeta();
        meta.setDisplayName("§eDelay");

        lore = new ArrayList<String>();
        lore.add( "§a"+acmd.getDelay()+" tick");

        meta.setLore(lore);
        item.setItemMeta(meta);
        gui.setItem(4,item);


        //Daily execution
        item = new ItemStack(Material.SUNFLOWER,1);
        meta = item.getItemMeta();
        meta.setDisplayName("§eDaily execution");

        lore = new ArrayList<String>();
        lore.add( "§a"+acmd.getTime());

        meta.setLore(lore);
        item.setItemMeta(meta);
        gui.setItem(5,item);


        //commands
        item = new ItemStack(Material.COMMAND_BLOCK,1);
        meta = item.getItemMeta();
        meta.setDisplayName("§eCommands");

        lore = new ArrayList<String>();
        int index = 0;
        for(String s : acmd.getCommands()){
            lore.add("§9ID "+index+" -> §6"+s);
            index++;
        }

        lore.add("§7( §aUse in-game commands or commands.yml ");
        lore.add("§afile to remove or modify the commands easily");
        lore.add("§e ->You can use :");

        lore.add("§e     -/acmd edit acmd0 removeCommand CommandID");

        lore.add("§a      to remove a command with its CommandID §7)");


        meta.setLore(lore);

        item.setItemMeta(meta);
        gui.setItem(6,item);


        //RepeatTime
        item = new ItemStack(Material.COMPARATOR,1);
        meta = item.getItemMeta();
        meta.setDisplayName("§eRepeatTask");

        lore = new ArrayList<String>();
        lore.add( "§a"+acmd.getRepetition());

        meta.setLore(lore);
        item.setItemMeta(meta);
        gui.setItem(7,item);

        //Message
        item = new ItemStack(Material.WRITABLE_BOOK,1);
        meta = item.getItemMeta();
        meta.setDisplayName("§eMessage");

        lore = new ArrayList<String>();
        lore.add( "§a"+acmd.getmessage());

        meta.setLore(lore);
        item.setItemMeta(meta);
        gui.setItem(8,item);


        //Name
        item = new ItemStack(Material.ITEM_FRAME,1);
        meta = item.getItemMeta();
        meta.setDisplayName("§eName");

        lore = new ArrayList<String>();
        lore.add( "§a"+acmd.getName());

        meta.setLore(lore);
        item.setItemMeta(meta);
        gui.setItem(9,item);



        //delet button
        item = new ItemStack(Material.RED_CONCRETE,1);
        meta = item.getItemMeta();
        meta.setDisplayName("§4X DELETE ACMD");

        lore = new ArrayList<String>();
        lore.add( "§cThis is irreversible");

        meta.setLore(lore);
        item.setItemMeta(meta);
        gui.setItem(53,item);

        //back button
        item = new ItemStack(Material.ORANGE_CONCRETE,1);
        meta = item.getItemMeta();
        meta.setDisplayName("§6<- Return");

        lore = new ArrayList<String>();
        lore.add( "§eBack to the main page");

        meta.setLore(lore);
        item.setItemMeta(meta);
        gui.setItem(44,item);



        return gui;
    }

    public Inventory createGUI_EditACMD(autocommand acmd){
        Inventory gui = Bukkit.createInventory(null, 54,"§8§oEditing "+acmd.getName());
        return fillGUI_EditACMD(acmd,gui);

    }

    public void reloadAllEditGUI(){
        int index = 0;
        for( Inventory gui : editorsListe){
            RefreshGUI_EditACMD(plugin.getcommandList().get(index),gui);
            index++;
        }
    }

    public void RefreshGUI_EditACMD(autocommand acmd,Inventory gui){
        gui.clear();
        fillGUI_EditACMD(acmd,gui);

    }


}
