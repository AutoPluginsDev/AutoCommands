package fr.lumi.Util;


public class StringNumberVerif {

    public static boolean isDigit(String s) {
        return s.chars().allMatch(Character::isDigit) || s.equals("-1");
    }
}