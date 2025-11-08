package net.popzi.records;

/**
 * The Tour record, per our database
 * @param ID of the database record
 * @param name of the tour
 * @param world name of the world the tour resides in
 * @param x location as int
 * @param y location as int
 * @param z location as int
 * @param distance distance the player is allowed to explore as int
 * @param iconItemStackB64 icon for the menu
 */
public record Tour(
        int ID,
        String name,
        String world,
        int x,
        int y,
        int z,
        int distance,
        String iconItemStackB64
) {}
