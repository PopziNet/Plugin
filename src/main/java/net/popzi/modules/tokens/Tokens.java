package net.popzi.modules.tokens;

import org.bukkit.entity.Player;

/**
 * The Tokens module is the PopziNet virtual currency.
 * Somewhat re-inventing the wheel, since other plugins like Essentials have
 * this built in. But we can't guarantee they won't get reset somehow in the years to come.
 */
public class Tokens {

    /**
     * Gets the number of tokens for a given player
     * @param player to set get the number of tokens for
     * @return value of tokens
     */
    public int getTokens(Player player) {
        return 0;
    }

    /**
     * Adds tokens to a certain players account
     * @param player to add the tokens to
     * @param numTokens int of tokens to add
     * @return boolean if successful
     */
    public boolean addTokens(Player player, int numTokens) {
        return false;
    }

    /**
     * Deducts the amount of tokens for a given player
     * @param player to remove tokens from
     * @param numTokens to remove from that player
     * @return boolean if successful
     */
    public boolean removeTokens(Player player, int numTokens) {
        return false;
    }

    /**
     * Sets the tokens of a given player to a specific amount
     * @param player to set the tokens for
     * @param numTokens to set for the player
     * @return boolean if successful
     */
    public boolean setTokens(Player player, int numTokens) {
        return false;
    }

}
