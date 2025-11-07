package net.popzi.records;

/**
 * The record for death entries in the database
 * @param UUID unique ID
 * @param Created date as string of creation
 * @param World UUID of the world the death occurred at
 * @param X location as int
 * @param Y location as int
 * @param Z location as int
 * @param Inventory inventory as base64
 */
@SuppressWarnings("unused")
public record Death (
        String UUID,
        String Created,
        String World,
        int X,
        int Y,
        int Z,
        String Inventory
) {}
