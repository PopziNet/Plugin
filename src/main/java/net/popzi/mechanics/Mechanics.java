package net.popzi.mechanics;

import net.popzi.plugin.Main;
import net.popzi.plugin.ModuleManager.Module;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.RedstoneWallTorch;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;

import static org.bukkit.Tag.ITEMS_SWORDS;


public class Mechanics implements Module {

    private final Main main;

    /**
     * Constructor
     * @param main instance of the plugin
     */
    public Mechanics(Main main) {
        this.main = main;
    }

    @Override
    public String getName() {
        return "MODULE_MECHANICS";
    }

    @Override
    public void handleEvent(Event event) {
        // TODO: Switch may be better here
        if (event instanceof PlayerDeathEvent) {
            this.HandleSwordDeath((PlayerDeathEvent) event);
        }
        if (event instanceof EntityDeathEvent) {
            this.HandleZombieDeath((EntityDeathEvent) event);
        }
        if (event instanceof ChunkLoadEvent) {
            this.HandleZombieHorseEntities((ChunkLoadEvent) event);
        }
        if (event instanceof EntityShootBowEvent) {
            this.HandleBowShoot((EntityShootBowEvent) event);
        }
        if (event instanceof ProjectileHitEvent) {
            this.HandleBowShootHit((ProjectileHitEvent) event);
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
                double number = Math.random(); // 0.0 -- to -- 1.0
                double dropChance = 0.007;

                if (weapon.getEnchantments().get(Enchantment.SWEEPING_EDGE) != null)
                    dropChance = 0.04;

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
     * Handles Zombie Horses. An event caused these to spawn everywhere.
     * So, delete them unless they're named.
     * @param event the chunk population event
     */
    public void HandleZombieHorseEntities(ChunkLoadEvent event) {
        for (Entity entity : event.getChunk().getEntities()) {
            if (entity.getType() == EntityType.ZOMBIE_HORSE) {
                if (entity.getName().equalsIgnoreCase("zombie horse")) {
                    this.main.getServer().getScheduler().runTaskLater(this.main, new Runnable() {
                        @Override
                        public void run() {
                            entity.remove();
                        }
                    }, 1L);
                }
            }
        }
    }

    /**
     * Handles shooting of bows to consume torches and place them on the interacted surface.
     */
    public void HandleBowShoot(EntityShootBowEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER)
            return;

        Player player = (Player) event.getEntity();
        Inventory inventory = player.getInventory();
        ItemStack bow = event.getBow();

        if (bow == null || !bow.displayName().toString().toLowerCase().contains("torch"))
            return;

        if (inventory.contains(Material.TORCH) ||
            inventory.contains(Material.SOUL_TORCH) ||
            inventory.contains(Material.REDSTONE_TORCH)) {

            // Here we can control the order of preference as to which torch type to use.
            // .first() returns -1 if the item cannot be found.
            int torch_index = inventory.first(Material.TORCH);
            if (torch_index == -1)
                torch_index = inventory.first(Material.SOUL_TORCH);
            if (torch_index == -1)
                torch_index = inventory.first(Material.REDSTONE_TORCH);

            // Remove the torch from the inventory
            ItemStack torch = inventory.getItem(torch_index);
            if (torch == null)
                return;
            torch.setAmount(torch.getAmount() - 1);
            inventory.setItem(torch_index, torch);

            // Begin the event. Set the projectile on fire and tag is, so we know how to process it when
            // It hits a block on the block hitting event
            event.getProjectile().setVisualFire(true);
            event.getProjectile().addScoreboardTag(torch.getType().toString());
        }
    }

    /**
     * Handles what happens when a projectile hits something.
     * @param event of the projectile hitting something
     */
    public void HandleBowShootHit(ProjectileHitEvent event) {
        if (event.getHitBlock() == null)
            return;

        // Handle the projectile
        Entity projectile = event.getEntity();
        Material torch_type = null;
        if (projectile.getScoreboardTags().contains("TORCH"))
            torch_type = Material.TORCH;
        else if (projectile.getScoreboardTags().contains("REDSTONE_TORCH"))
            torch_type = Material.REDSTONE_TORCH;
        else if (projectile.getScoreboardTags().contains("SOUL_TORCH"))
            torch_type = Material.SOUL_TORCH;

        // Handle the block
        Block block = event.getHitBlock();
        Set<Material> validBlocks = EnumSet.of(
            Material.GRASS_BLOCK,
            Material.STONE,
            Material.DEEPSLATE,
            Material.DIRT,
            Material.SAND
        );

        if (!validBlocks.contains(block.getType()))
            return;
        if (event.getHitBlockFace() == null)
            return;

        // Place the torch on the block face

        // Find the block we need to alter
        BlockFace face = event.getHitBlockFace();
        Location mod = new Location(block.getWorld(), face.getModX(), face.getModY(), face.getModZ());
        Location alteration = block.getLocation().add(mod); // The block to change

        // Check that the alteration block is able to support a torch
        Block alterationBlock = projectile.getWorld().getBlockAt(alteration);
        this.main.LOGGER.log(Level.INFO, "Hit: " + block.getLocation() + " projectile hit: " + alteration);

        // TODO: Figure out how the hell paper now handles block directions and torches >_>
    }


    /**
     * Hey look at you, you found your first secret :-)
     * @param event The wind charge event
     */
    public void HandleWindCharge(ProjectileHitEvent event) {
        if (event.getEntity().getShooter() instanceof Player p) {

            Entity e = event.getHitEntity();
            if (e == null)
                return;

            if (p.getInventory().getItemInMainHand().displayName().toString().toLowerCase().contains("super"))
                e.setVelocity(e.getVelocity().multiply(100));
        }
    }
}
