package fr.lumi.Util;

public class autocommandDataPattern {
    protected String acmdName;
    protected Long acmdCycle;
    protected Long acmdDelay;
    protected Integer acmdRepetitions;
    protected String acmdcommand;
    protected String ID;

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

    public void FillDefaults() {
        if (acmdName == null) {
            acmdName = "acmd";
        }
        if (acmdCycle == null) {
            acmdCycle = 0L;
        }
        if (acmdDelay == null) {
            acmdDelay = 0L;
        }
        if (acmdRepetitions == null) {
            acmdRepetitions = 0;
        }
        if (acmdcommand == null) {
            acmdcommand = "say Hello World!";
        }
        if (ID == null) {
            ID = "acmd";
        }
    }

    public void applyToAcmd(autocommand acmd) {
        acmd.setName(acmdName);
        acmd.setCycle(acmdCycle);
        acmd.setDelay(acmdDelay);
        acmd.setRepetition(acmdRepetitions);
        acmd.addCommand(acmdcommand);
        acmd.setID(ID);
    }

}
