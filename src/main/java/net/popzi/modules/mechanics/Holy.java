package net.popzi.modules.mechanics;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.popzi.Main;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Holy {

    private final Main main;

    public Holy (Main main) {
        this.main = main;
    }

    public void handleBlockPlace(BlockPlaceEvent event) {
        Block placed = event.getBlock();
        if (!placed.getType().equals(Material.LAPIS_BLOCK)) return;

        if (isCrossPattern(placed, placed.getType())) {
            String p = event.getPlayer().getName().toUpperCase();
            for (int i = 0; i < 5; i++) {
                this.main.getServer().broadcast(Component.text(p + " WIELDS THE HOLY LAPIS!").color(NamedTextColor.DARK_BLUE));
            }
        }
    }

    private boolean isCrossPattern(Block block, Material type) {
        return isCrossFromCenter(block.getRelative(0, -1, 0), type) || // Top to center
                isCrossFromCenter(block.getRelative(0, 0, 0), type) || // Center to center
                isCrossFromCenter(block.getRelative(1, 0, 0), type) || // XLeft to center
                isCrossFromCenter(block.getRelative(-1, 0, 0), type) || // XRight to center
                isCrossFromCenter(block.getRelative(0, 0, 1), type) || // ZLeft to center
                isCrossFromCenter(block.getRelative(0, 0, -1), type) || // ZRight to center
                isCrossFromCenter(block.getRelative(0, -1, 0), type) || // 2nd from bottom to center
                isCrossFromCenter(block.getRelative(0, -2, 0), type); // bottom to center
    }

    private boolean isCrossFromCenter(Block centerBlock, Material type) {
        // Stick
        if (!isSameType(centerBlock.getRelative(0, 1, 0), type)) return false; // Top
        if (!isSameType(centerBlock.getRelative(0 , 0, 0), type)) return false; // Us
        if (!isSameType(centerBlock.getRelative(0 , -1, 0), type)) return false; // Bottom
        if (!isSameType(centerBlock.getRelative(0 , -2, 0), type)) return false; // Very Bottom

        // X/Z Arms
        if (!isSameType(centerBlock.getRelative(1, 0, 0), type)) { // XLeft
            if (!isSameType(centerBlock.getRelative(0, 0, 1), type)) return false; // ZLeft
        }
        if (!isSameType(centerBlock.getRelative(-1, 0, 0), type)) { // XRight
            if (!isSameType(centerBlock.getRelative(0, 0, -1), type)) return false; // ZRight
        }

        // We are a cross! Change the middle to red
        this.fireCrossEvent(centerBlock);
        return true;
    }

    private void fireCrossEvent(Block centerBlock) {
        // Set all the blocks to air
        centerBlock.getRelative(0, 1, 0).setType(Material.AIR);
        centerBlock.getRelative(0, 0, 0).setType(Material.AIR);
        centerBlock.getRelative(0, -1, 0).setType(Material.AIR);
        centerBlock.getRelative(0, -2, 0).setType(Material.AIR);
        centerBlock.getRelative(1, 0, 0).setType(Material.AIR);
        centerBlock.getRelative(-1, 0, 0).setType(Material.AIR);
        centerBlock.getRelative(0, 0, 1).setType(Material.AIR);
        centerBlock.getRelative(0, 0, -1).setType(Material.AIR);

        // Set floor
        Block floor = centerBlock.getRelative(0, -3, 0);
        floor.setType(Material.LAPIS_BLOCK);

        // Strike
        floor.getLocation().getWorld().strikeLightningEffect(floor.getLocation());

        // Drop a reward
        ItemStack reward = new ItemStack(Material.LAPIS_BLOCK, 1);
        ItemMeta rewardMeta = reward.getItemMeta();
        rewardMeta.displayName(Component.text("HOLY LAPIS").color(NamedTextColor.DARK_BLUE));
        List<Component> rewardLore = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            rewardLore.add(Component.text("I AM THE BEHOLDER OF THE HOLY LAPIS").color(NamedTextColor.DARK_BLUE));
            rewardLore.add(Component.text("WIELD SUCH POWER I SHALL RULE ABOVE ALL").color(NamedTextColor.DARK_BLUE));
        }
        rewardMeta.lore(rewardLore);
        reward.setItemMeta(rewardMeta);
        floor.getLocation().getWorld().dropItemNaturally(floor.getRelative(0, 1, 0).getLocation(), reward);
    }

    private boolean isSameType(Block block, Material type) {
        return block.getType() == type && block.getType() != Material.AIR;
    }
}
