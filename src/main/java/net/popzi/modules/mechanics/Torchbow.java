package net.popzi.modules.mechanics;

import net.kyori.adventure.util.TriState;
import net.popzi.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockSupport;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public class Torchbow {

    private final Main main;

    public Torchbow(Main main) {
        this.main = main;
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

        if (
                inventory.contains(Material.TORCH) ||
                inventory.contains(Material.SOUL_TORCH) ||
                inventory.contains(Material.REDSTONE_TORCH) ||
                inventory.contains(Material.COPPER_TORCH)
        ) {

            // Here we can control the order of preference as to which torch type to use.
            // .first() returns -1 if the item cannot be found.
            int torch_index = inventory.first(Material.TORCH);
            if (torch_index == -1)
                torch_index = inventory.first(Material.SOUL_TORCH);
            if (torch_index == -1)
                torch_index = inventory.first(Material.REDSTONE_TORCH);
            if (torch_index == -1)
                torch_index = inventory.first(Material.COPPER_TORCH);

            // Remove the torch from the inventory
            ItemStack torch = inventory.getItem(torch_index);
            if (torch == null)
                return;
            torch.setAmount(torch.getAmount() - 1);
            inventory.setItem(torch_index, torch);

            // Begin the event. Set the projectile on fire and tag is, so we know how to process it when
            // It hits a block on the block hitting event
            event.getProjectile().setVisualFire(TriState.TRUE);
            event.getProjectile().addScoreboardTag(torch.getType().toString());
        }
    }

    /**
     * Handles what happens when a projectile hits something.
     * @param event of the projectile hitting something
     */
    public void HandleBowShootHit(ProjectileHitEvent event) {
        // Handle the projectile
        Entity projectile = event.getEntity();
        Material torch_type;
        if (projectile.getScoreboardTags().contains("TORCH"))
            torch_type = Material.TORCH;
        else if (projectile.getScoreboardTags().contains("REDSTONE_TORCH"))
            torch_type = Material.REDSTONE_TORCH;
        else if (projectile.getScoreboardTags().contains("SOUL_TORCH"))
            torch_type = Material.SOUL_TORCH;
        else if (projectile.getScoreboardTags().contains("COPPER_TORCH"))
            torch_type = Material.COPPER_TORCH;
        else {
            torch_type = null;
        }

        if (torch_type == null)
            return;

        // If we've hit an entity, set it on fire
        if (event.getHitEntity() != null) {
            Entity e = event.getHitEntity();
            if (event.getEntity().getVisualFire() == TriState.TRUE)
                e.setFireTicks(60);
        }

        // Handle the block
        Block block = event.getHitBlock();
        BlockFace face = event.getHitBlockFace();
        if (block == null | face == null)
            return;

        // Get the block we need to think about altering
        Block alterationBlock = block.getRelative(face);

        // Ensure the target block can handle a torch, and is replaceable (air, water, etc.)
        if (!canSupportTorch(block, face)) {
            // Drop a torch item at the hit block location
            Bukkit.getScheduler().runTask(this.main, () -> {
                alterationBlock.getWorld().dropItemNaturally(alterationBlock.getLocation(), new ItemStack(torch_type, 1));
            });
            return;
        }
        if (!alterationBlock.isEmpty() && !alterationBlock.isLiquid())
            return;

        // Now handle direction: floor vs. wall torches
        Material placeType = torch_type;

        // Wall torches are different block types
        if (face != BlockFace.UP && face != BlockFace.DOWN) {
            switch (torch_type) {
                case TORCH:
                    placeType = Material.WALL_TORCH;
                    break;
                case REDSTONE_TORCH:
                    placeType = Material.REDSTONE_WALL_TORCH;
                    break;
                case SOUL_TORCH:
                    placeType = Material.SOUL_WALL_TORCH;
                    break;
                case COPPER_TORCH:
                    placeType = Material.COPPER_WALL_TORCH;
                    break;
            }
        } else if (face == BlockFace.DOWN) {
            return; // Can't place torches upside down
        }

        // Set the block
        Material finalPlaceType = placeType;
        Bukkit.getScheduler().runTask(this.main, () -> {
            alterationBlock.setType(finalPlaceType, false);
            BlockData data = alterationBlock.getBlockData();
            if (data instanceof Directional directional) {
                // Wall torches face opposite of the hit face
                directional.setFacing(face);
                alterationBlock.setBlockData(directional);
            }
            projectile.remove();
            this.main.LOGGER.log(Level.INFO, "Placed torch type: " + finalPlaceType + " facing " + face);
        });
    }

    /**
     * Checks if a block face can support a torch.
     * Torches can only be placed on solid, full faces (no glass, leaves, etc.).
     */
    private boolean canSupportTorch(Block baseBlock, BlockFace face) {

        // Torches require a sturdy face (Minecraft logic)
        return baseBlock.getBlockData().isFaceSturdy(face, BlockSupport.FULL);
    }
}
