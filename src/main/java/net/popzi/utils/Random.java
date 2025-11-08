package net.popzi.utils;

public class Random {

    public static int number(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
