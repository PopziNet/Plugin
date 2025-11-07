package net.popzi.records;

/**
 * The Tour record, per our database
 * @param ID of the database record
 * @param name of the tour
 * @param world name of the world the tour resides in
 * @param X location as int
 * @param Y location as int
 * @param Z location as int
 * @param Distance distance the player is allowed to explore as int
 * @param IconItemStackB64 icon for the menu
 */
public record Tour(
        int ID,
        String name,
        String world,
        int X,
        int Y,
        int Z,
        int Distance,
        String IconItemStackB64
) {}
