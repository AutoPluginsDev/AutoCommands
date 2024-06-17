package fr.lumi.Util;

import fr.lumi.Main;


public class ModificationLock {
    Main plg;
    boolean locked = false;


    /*
     * lastUser: UUID this is the last user that modified the plugin, it is used to prevent conflicts.
     */
    String lastTennant = "";

    public String getLastTennant() {
        return lastTennant;
    }

    public void setLastTennant(String tennant) {
        lastTennant = tennant;
    }

    public ModificationLock(Main plugin) {
        plg = plugin;
    }

    /*
     * Locks the modification lock if it is not already locked
     * specified tennant is the one who is locking the lock
     */
    public boolean lock(String tennant) {
        // If the lock is already locked, return false,
        // it is up to the caller to decide what to do in this case
        if (locked) {
            return false;
        }
        setLastTennant(tennant);
        locked = true;
        return true;
    }

    /*
     * Unlocks the modification lock if it is locked
     * Throws an IllegalStateException if the lock is already unlocked
     *
     */
    public boolean unlock(String lastTennant) {
        if (locked && !lastTennant.equals(getLastTennant())) {
            throw new IllegalStateException("Unlocking an already unlocked lock");
        }
        locked = false;
        return true;
    }

}
