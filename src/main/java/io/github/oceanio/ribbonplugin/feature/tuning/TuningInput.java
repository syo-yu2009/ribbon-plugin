package io.github.oceanio.ribbonplugin.feature.tuning;

import org.bukkit.Material;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * クラフトグリッドを解析して「対象アイテム」を保持する値クラス。
 * 有効な組み合わせでない場合は null を返す。
 *
 * 有効な組み合わせ（順不同）:
 *   - 腐肉 × 1以上
 *   - エメラルド × 1以上
 *   - 武器/ツール/防具 × 1
 *   - それ以外のアイテムは不可
 */
public record TuningInput(ItemStack targetItem) {

    /**
     * グリッドを解析して TuningInput を返す。
     * 無効な配置なら null。
     */
    public static TuningInput parse(JavaPlugin plugin, CraftingInventory inv) {
        ItemStack[] matrix = inv.getMatrix();

        ItemStack target = null;
        boolean hasFlesh = false;
        boolean hasEmerald = false;

        for (ItemStack slot : matrix) {
            if (slot == null || slot.getType() == Material.AIR) continue;

            if (slot.getType() == Material.ROTTEN_FLESH) {
                hasFlesh = true;
            } else if (slot.getType() == Material.EMERALD) {
                hasEmerald = true;
            } else if (isTunable(slot)) {
                if (target != null) return null; // 装備は1個だけ
                target = slot;
            } else {
                return null; // 想定外のアイテム
            }
        }

        if (target == null || !hasFlesh || !hasEmerald) return null;
        return new TuningInput(target);
    }

    /** エンチャント対象として有効なアイテムか判定。 */
    static boolean isTunable(ItemStack item) {
        String type = item.getType().name();
        return type.endsWith("_SWORD")
                || type.endsWith("_AXE")
                || type.endsWith("_PICKAXE")
                || type.endsWith("_SHOVEL")
                || type.endsWith("_HOE")
                || type.endsWith("_HELMET")
                || type.endsWith("_CHESTPLATE")
                || type.endsWith("_LEGGINGS")
                || type.endsWith("_BOOTS")
                || type.equals("BOW")
                || type.equals("CROSSBOW")
                || type.equals("TRIDENT");
    }
}

