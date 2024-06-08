package fr.lumi.Util;

import fr.lumi.Main;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class dailyCommandExecuter implements Runnable{

    List<autocommand> m_commandList;
    Main plugin;

    public dailyCommandExecuter(Main plg, List<autocommand> acmds){
        m_commandList = acmds;
        plugin = plg;
    }

    public int getrefreshRate(){
        return 1200;
    }

    @Override
    public void run() {
        Date dateInput = new Date();
        int Hours = dateInput.toInstant().atZone(ZoneId.systemDefault()).getHour();

        int minutes = dateInput.toInstant().atZone(ZoneId.systemDefault()).getMinute();

        String hour;
        String minute;

        if(Hours < 10 ) hour = "0"+Hours;
        else hour = ""+Hours;

        if(minutes < 10 ) minute = "0"+minutes;
        else minute = ""+minutes;


        String HourString = hour+"H"+minute;

        for(autocommand acmd : m_commandList){

            if (Objects.equals(acmd.getTime(), HourString) && acmd.isActive()){
                acmd.setRunning(true,plugin.getCommandsConfig());

            }
        }
    }
}
