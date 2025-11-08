package net.popzi.records;

/**
 * The record for death entries in the database
 * @param UUID unique ID
 * @param created date as string of creation
 * @param world UUID of the world the death occurred at
 * @param x location as int
 * @param y location as int
 * @param z location as int
 * @param inventory inventory as base64
 */
@SuppressWarnings("unused")
public record Death (
        String UUID,
        String created,
        String world,
        int x,
        int y,
        int z,
        String inventory
) {}
