package io.github.oceanio.ribbonplugin.feature.tracker;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ToolTrackerService {
    private static final String PREFIX = "§a使用回数: ";

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

    public boolean isTools (ItemStack item) {
        return TOOLS.contains(item);
    }

    public void addUse(ItemStack item) {
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
}
