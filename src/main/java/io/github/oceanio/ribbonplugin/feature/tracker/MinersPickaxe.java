package io.github.oceanio.ribbonplugin.feature.tracker;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class MinersPickaxe implements Listener {
    // List
    private static final Set<Material> TOOLS = Set.of(
            Material.WOODEN_PICKAXE,
            Material.WOODEN_AXE,
            Material.WOODEN_SHOVEL,
            Material.STONE_PICKAXE,
            Material.STONE_AXE,
            Material.STONE_SHOVEL,
            Material.COPPER_PICKAXE,
            Material.COPPER_AXE,
            Material.COPPER_SHOVEL,
            Material.GOLDEN_PICKAXE,
            Material.GOLDEN_AXE,
            Material.IRON_PICKAXE,
            Material.IRON_AXE,
            Material.IRON_SHOVEL,
            Material.DIAMOND_PICKAXE,
            Material.DIAMOND_AXE,
            Material.DIAMOND_SHOVEL,
            Material.NETHERITE_PICKAXE,
            Material.NETHERITE_AXE,
            Material.NETHERITE_SHOVEL
    );





    private static final String PREFIX = "§a使用回数: ";

    //Private Method
    private boolean isTools (Player p) {
        Material item = p.getInventory().getItemInMainHand().getType();
        return TOOLS.contains(item);
    }

    private void doProcess(BlockBreakEvent event) {
        Player p = event.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if (meta==null) return;

        List<String> lore = meta.getLore();
        int uses = 0;
        if (lore != null && !lore.isEmpty()) {
            String line = lore.get(0);
            try {
                uses = Integer.parseInt(line.replace(PREFIX, ""));
            } catch (NumberFormatException ignored) {}

        }
        uses++;
        List<String> newLore = new ArrayList<>();
        newLore.add(PREFIX + uses);
        meta.setLore(newLore);
        item.setItemMeta(meta);
    }

    //Event Method
    @EventHandler
    public void onBrokeBlock (BlockBreakEvent event) {
        if (!isTools(event.getPlayer())) return;
        doProcess(event);
    }
}
