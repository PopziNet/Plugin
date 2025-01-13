package net.popzi.mechanics;

import net.popzi.plugin.Main;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import java.util.Objects;

public class Mechanics {

    private final Main main;

    /**
     * Constructor
     * @param main instance of the plugin
     */
    public Mechanics(Main main) {
        this.main = main;
    }

    /**
     * Checks if the player was killed by another player holding a sword.
     * If so, 0.7% chance to drop a head. Increased to 4% if the sword was enchanted with sweeping edge.
     * @param e PlayerDeathEvent event.
     */
    public void WasKilledBySword(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Player k = p.getKiller();

        if (k != null && k.getInventory().getItemInMainHand().getType().toString().toUpperCase().contains("SWORD")) {

            ItemStack sword = k.getInventory().getItemInMainHand();
            double number = Math.random(); // 0.0 -- to -- 1.0
            double dropchance = 0.007;

            if (sword.getEnchantments().get(Enchantment.SWEEPING_EDGE) != null)
                dropchance = 0.04;

            if (dropchance > number) {
                ItemStack head = ItemStack.of(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta) head.getItemMeta();
                assert meta != null;
                meta.setOwningPlayer(p);
                head.setItemMeta(meta);
                Objects.requireNonNull(p.getLocation().getWorld()).dropItemNaturally(p.getLocation(), head);
            }
        }
    }


    /**
     * The main handler for game mechanics events.
     * @param e The event that took place.
     */
    public void Handle(Event e) {
        if (e instanceof PlayerDeathEvent) {
            this.WasKilledBySword((PlayerDeathEvent) e);
        }
    }
}
