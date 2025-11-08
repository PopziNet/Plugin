package net.popzi.records;

/**
 * The ToursPLayers record
 * @param uuid of the player
 * @param world of the world
 * @param x of the player position
 * @param y of the player position
 * @param z of the player position
 */
public record ToursPlayers(
        String uuid,
        String world,
        int x,
        int y,
        int z,
        int tourID
) {}
