package net.popzi.modules.mechanics;

import io.papermc.paper.event.player.PlayerArmSwingEvent;
import net.popzi.Main;
import net.popzi.modules.BaseModule;
import net.popzi.utils.Boundary;
import net.popzi.utils.Random;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.*;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import static org.bukkit.Tag.ITEMS_SWORDS;


public class Mechanics extends BaseModule {

    private final Torchbow torchbow;
    public boolean insomniaActive;

    /**
     * Constructor
     * @param main instance of the plugin
     */
    public Mechanics(Main main) {
        super(main);
        this.torchbow = new Torchbow(main);
        this.insomniaActive = false;
        this.startCronTimer();
    }

    @Override
    public String getName() {
        return "MODULE_MECHANICS";
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof PlayerDeathEvent)
            this.HandleSwordDeath((PlayerDeathEvent) event);
        if (event instanceof EntityDeathEvent)
            this.HandleZombieDeath((EntityDeathEvent) event);
        if (event instanceof ChunkLoadEvent)
            this.HandleZombieHorseEntities((ChunkLoadEvent) event);
        if (event instanceof EntityShootBowEvent)
            this.torchbow.HandleBowShoot((EntityShootBowEvent) event);
        if (event instanceof EntityPickupItemEvent)
            this.HandleEntityPickup((EntityPickupItemEvent) event);
        if (event instanceof PlayerArmSwingEvent)
            this.handleArmSwingEvent((PlayerArmSwingEvent) event);
        if (event instanceof ProjectileHitEvent) {
            this.torchbow.HandleBowShootHit((ProjectileHitEvent) event);
            this.HandleWindCharge((ProjectileHitEvent) event);
        }
    }

    /**
     * Checks if the player was killed by another player holding a sword.
     * If so, 0.7% chance to drop a head. Increased to 4% if the sword was enchanted with sweeping edge.
     * @param e PlayerDeathEvent event.
     */
    public void HandleSwordDeath(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity(); // Living, apparently
        Player killer = entity.getKiller(); // Null if not killed by a player

        if (killer != null && entity instanceof Player) {
            ItemStack weapon = killer.getInventory().getItemInMainHand();

            if (ITEMS_SWORDS.isTagged(weapon.getType())) {
                if (!this.main.CFG.getData().getBoolean("PLAYER_HEADS_ENABLE")) return;
                double number = Math.random(); // 0.0 -- to -- 1.0
                double dropChance = this.main.CFG.getData().getDouble("PLAYER_HEADS_DROP_CHANCE");

                if (weapon.getEnchantments().get(Enchantment.SWEEPING_EDGE) != null) {
                    if (this.main.CFG.getData().getBoolean("PLAYER_HEADS_SWEEP_ENABLE"))
                        dropChance = this.main.CFG.getData().getDouble("PLAYER_HEADS_SWEEP_DROP_CHANCE");
                }

                if (dropChance > number) { // A drop is to occur.
                    ItemStack head = ItemStack.of(Material.PLAYER_HEAD);
                    SkullMeta meta = (SkullMeta) head.getItemMeta();
                    assert meta != null;
                    meta.setOwningPlayer((OfflinePlayer) entity);
                    head.setItemMeta(meta);
                    Objects.requireNonNull(entity.getLocation().getWorld()).dropItemNaturally(entity.getLocation(), head);
                }
            }
        }
    }

    /**
     * Handles zombie deaths and provides a 5% chance to drop phantom membranes
     * when they die. This is because we have insomnia / phantoms turned off.
     * @param e The event type
     */
    public void HandleZombieDeath(EntityDeathEvent e) {
        if (!main.CFG.getData().getBoolean("ZOMBIE_DROP_MEMBRANES"))
            return;

        double dropChance = main.CFG.getData().getDouble("ZOMBIE_DROP_MEMBRANES_CHANCE");

        if (e.getEntity().getType().equals(EntityType.ZOMBIE)) {
            double number = Math.random();
            Player killer = e.getEntity().getKiller();
            ItemStack weapon = null;

            if (killer != null)
                weapon = killer.getInventory().getItemInMainHand();

            if (dropChance > number) {
                e.getDrops().add(ItemStack.of(Material.PHANTOM_MEMBRANE, 1));

                if (weapon != null && weapon.getEnchantments().get(Enchantment.LOOTING) != null)
                    e.getDrops().add(ItemStack.of(Material.PHANTOM_MEMBRANE, weapon.getEnchantmentLevel(Enchantment.LOOTING)));
            }
        }
    }

    /**
     * Handles Zombie Horses. An event caused these to spawn everywhere.
     * So, delete them unless they're named.
     * @param event the chunk population event
     */
    public void HandleZombieHorseEntities(ChunkLoadEvent event) {
        for (Entity entity : event.getChunk().getEntities()) {
            if (entity.getType() == EntityType.ZOMBIE_HORSE) {
                if (entity.getName().equalsIgnoreCase("zombie horse")) {
                    this.main.getServer().getScheduler().runTaskLater(this.main, entity::remove, 1L);
                }
            }
        }
    }

    /**
     * Hey look at you, you found a secret :-)
     * @param event The wind charge event
     */
    public void HandleWindCharge(ProjectileHitEvent event) {
        if (event.getEntity().isEmpty()) return;
        if (event.getEntity().getShooter() == null) return;
        if (event.getEntity().getShooter() instanceof Player p) {
            Entity e = event.getHitEntity();
            if (e == null) return;
            if (p.getInventory().getItemInMainHand().displayName().toString().toLowerCase().contains("super"))
                e.setVelocity(e.getVelocity().multiply(10));
        }
    }

    /**
     * Hey look at you, you found your second secret!
     * @param event The entity pickup event
     */
    public void HandleEntityPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Zombie))
            return;

        if (event.getItem().getItemStack().getType() != Material.GOLDEN_APPLE)
            return;

        Location l = event.getEntity().getLocation();
        this.main.LOGGER.log(Level.INFO, "Entity picked up: " + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ() + ", Type: " + event.getEntity().getType());

        // Spawn a giant zombie
        this.main.getServer().getScheduler().runTaskLater(this.main, () -> {
            event.getItem().getWorld().spawnEntity(event.getEntity().getLocation(), EntityType.GIANT);
            event.getEntity().remove();
        }, 1L);
    }

    /**
     * Possibly another s3cr3t?
     * @param event of the arm swing event
     */
    public void handleArmSwingEvent(PlayerArmSwingEvent event) {
        ItemStack itmHand = event.getPlayer().getInventory().getItemInMainHand();

        if (itmHand.getType() != Material.LIGHTNING_ROD) return;

        if (itmHand.displayName().toString().toLowerCase().contains("zeus")) {
            Player player = event.getPlayer();
            Boundary box = new Boundary(player.getLocation(), 50);

            for (int i = 0; i < 6; i++) {

                int delay = Random.number(0, 20);

                // Todo: If the name also contains an online players name....?
                Location strikeLocation = box.getRandomBlockInBoundary();

                // Correct the highest possible block
                strikeLocation.setY(
                        strikeLocation.getWorld().getHighestBlockYAt(
                                strikeLocation.getBlockX(),
                                strikeLocation.getBlockZ()
                        )
                );

                this.main.getServer().getScheduler().runTaskLater(this.main, () -> {
                    strikeLocation.getWorld().strikeLightningEffect(strikeLocation);
                }, delay);
            }
        }
    }

    /**
     * Setups up a bukkit timer to run every hour for miscellaneous things like what day it currently is,
     * permitting things like phantoms to spawn.
     */
    public void startCronTimer() {
        this.main.getServer().getScheduler().runTaskTimer(this.main, () -> {

            // Get the current day.
            // 0 = Sunday, 7 = Saturday
            Calendar c = Calendar.getInstance();
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

            // Get world
            World world = this.main.getServer().getWorld("world"); // TODO: Hardcoded
            if (world == null) return;

            // Get config
            @SuppressWarnings("unchecked") // We're 95% sure this will be a list of ints! User error if not.
            List<Integer> x = (List<Integer>) this.main.CFG.getData().getList("PHANTOMS_ENABLED_DAYS");
            if (x == null) return;

            // Turn Phantoms on for configured days
            if (x.contains(dayOfWeek)) {
                world.setGameRule(GameRule.DO_INSOMNIA, true);
            } else {
                world.setGameRule(GameRule.DO_INSOMNIA, false);
            }
        }, 20*10L,7200L); // 1H intervals
    }
}
