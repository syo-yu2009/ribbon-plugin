package io.github.oceanio.ribbonplugin.feature.tuning;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;
import java.util.Map;

public class TuningService {

    // ここで確実に初期化する
    private final EnchantPool pool = new EnchantPool();

    public Map<Enchantment, Integer> rollEnchantment(ItemStack item) {
        Map<Enchantment, Integer> result = new HashMap<>();

        // 最大10回試行（アイテムに付与できないエンチャントが選ばれた時のため）
        for (int i = 0; i < 10; i++) {
            EnchantPool.EnchantEntry entry = pool.getRandomEntry();
            if (entry == null) break;

            // そのアイテム（剣や防具など）に付与可能かチェック
            if (entry.getEnchantment().canEnchantItem(item)) {
                result.put(entry.getEnchantment(), entry.getLevel());
                break;
            }
        }

        return result;
    }

    public void consumeIngredients(CraftingInventory inv) {
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item == null || item.getType() == Material.AIR) continue;

            // クラフト枠内の全アイテムを1個ずつ減らす
            int amount = item.getAmount() - 1;
            if (amount <= 0) {
                inv.setItem(i, null);
            } else {
                item.setAmount(amount);
            }
        }
    }
}