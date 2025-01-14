package net.popzi.mechanics;

import net.popzi.plugin.Main;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import java.util.Objects;
import static org.bukkit.Tag.ITEMS_SWORDS;

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
    public void HandleSwordDeath(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity(); // Living, apparently
        Player killer = entity.getKiller(); // Null if not killed by a player

        assert killer != null;
        assert entity instanceof Player;

        ItemStack weapon = killer.getInventory().getItemInMainHand();

        if (ITEMS_SWORDS.isTagged(weapon.getType())) {
            double number = Math.random(); // 0.0 -- to -- 1.0
            double dropchance = 0.007;

            if (weapon.getEnchantments().get(Enchantment.SWEEPING_EDGE) != null)
                dropchance = 0.04;

            if (dropchance > number) { // A drop is to occur.
                ItemStack head = ItemStack.of(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta) head.getItemMeta();
                assert meta != null;
                meta.setOwningPlayer((OfflinePlayer) entity);
                head.setItemMeta(meta);
                Objects.requireNonNull(entity.getLocation().getWorld()).dropItemNaturally(entity.getLocation(), head);
            }
        }
    }

    /**
     * Handles zombie deaths and provides a 5% chance to drop phantom membranes
     * when they die. This is because we have insomnia / phantoms turned off.
     * @param e The event type
     */
    public void HandleZombieDeath(EntityDeathEvent e) {
        if (e.getEntity().getType().equals(EntityType.ZOMBIE)) {
            double number = Math.random();
            Player killer = e.getEntity().getKiller();
            ItemStack weapon = null;

            if (killer != null)
                weapon = killer.getInventory().getItemInMainHand();

            if (0.05 > number) {
                e.getDrops().add(ItemStack.of(Material.PHANTOM_MEMBRANE, 1));

                if (weapon != null && weapon.getEnchantments().get(Enchantment.LOOTING) != null)
                    e.getDrops().add(ItemStack.of(Material.PHANTOM_MEMBRANE, weapon.getEnchantmentLevel(Enchantment.LOOTING)));
            }
        }
    }

    /**
     * Handles shooting of bows to consume torches and place them on the interacted surface.
     */
    public void HandleBowShoot() {}

    /**
     * The main handler for game mechanics events.
     * @param e The event that took place.
     */
    public void Handle(Event e) {
        if (e instanceof PlayerDeathEvent) {
            this.HandleSwordDeath((PlayerDeathEvent) e);
        }

        if (e instanceof EntityDeathEvent) {
            this.HandleZombieDeath((EntityDeathEvent) e);
        }
    }
}
