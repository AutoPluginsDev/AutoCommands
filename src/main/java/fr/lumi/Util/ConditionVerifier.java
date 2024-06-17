package fr.lumi.Util;

import fr.lumi.ConditionsFolder.Condition;
import fr.lumi.Main;

public class ConditionVerifier {

    Main main;


    public ConditionVerifier(Main _main) {
        main = _main;
    }

    public static boolean verify(autocommand acmd) {
        boolean verified = acmd.isActive();
        for (Condition c : acmd.getConditions()) {
            verified = verified && c.verify();
        }
        return verified;
    }


}
