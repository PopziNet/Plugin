package net.popzi.core;

import org.bukkit.inventory.ItemStack;

import java.util.Base64;

public class Serializer {

    /**
     * Serializes any Paper/Bukkit object to Base64
     * @param object The object to serialize
     * @return Base64 encoded string representation
     */
    public static String itemToBase64(ItemStack object) {
        if (object == null) return null;
        return Base64.getEncoder().encodeToString(object.serializeAsBytes());
    }

    /**
     * Deserializes base64 into a Paper/Bukkit object.
     * @param base64 string of the serialized object
     * @return ItemStack from the base64 data
     */
    public static ItemStack itemFromBase64(String base64) {
        if (base64 == null) return null;
        byte[] data = Base64.getDecoder().decode(base64);
        return ItemStack.deserializeBytes(data);
    }
}