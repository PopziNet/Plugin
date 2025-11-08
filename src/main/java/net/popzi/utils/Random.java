package net.popzi.utils;

public class Random {

    /**
     * Returns a random number between two provided integers
     * @param min int
     * @param max int
     * @return a random integer between the min and max
     */
    public static int number(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
