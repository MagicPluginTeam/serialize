package io.github.magicpluginteam.serialize.utils;

public class NumberUtils {

    public static Number integerIf(double input) {
        int i = (int) input;
        if (i == input) return i;
        else return input;
    }

}
