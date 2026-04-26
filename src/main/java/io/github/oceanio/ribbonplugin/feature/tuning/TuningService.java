package io.github.oceanio.ribbonplugin.feature.tuning;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * チューニングのビジネスロジックを集約するサービスクラス。
 *
 * リスナーはイベント受け取りと通知のみを担当し、
 * ロール・適用・素材消費の処理はすべてここに委譲する。
 */
public class TuningService {

    private final Random random = new Random();

    // ---------------------------------------------------------------
    // エンチャントのロールと適用
    // ---------------------------------------------------------------

    /**
     * アイテムに対してエンチャントをランダムにロールして返す。
     * 対象外アイテムの場合は空の Map を返す。
     */
    public Map<Enchantment, Integer> rollEnchants(ItemStack item) {
        return EnchantPool.roll(item, random);
    }

    /**
     * アイテムの既存エンチャントをリセットし、指定したエンチャントを付与した
     * 新しい ItemStack を返す（元のアイテムは変更しない）。
     */
    public ItemStack applyEnchants(ItemStack item, Map<Enchantment, Integer> enchants) {
        ItemStack result = item.clone();
        // 既存エンチャントを全削除
        new ArrayList<>(result.getEnchantments().keySet())
                .forEach(result::removeEnchantment);
        // 新エンチャントを付与（互換性チェックなし）
        enchants.forEach((e, lvl) -> result.addUnsafeEnchantment(e, lvl));
        return result;
    }

    // ---------------------------------------------------------------
    // 素材の消費
    // ---------------------------------------------------------------

    /**
     * クラフトグリッドから腐肉・エメラルド・対象アイテムを
     * それぞれ1個ずつ消費してグリッドに書き戻す。
     */
    public void consumeIngredients(CraftingInventory inv) {
        ItemStack[] matrix = inv.getMatrix();
        boolean fleshConsumed   = false;
        boolean emeraldConsumed = false;
        boolean targetConsumed  = false;

        for (int i = 0; i < matrix.length; i++) {
            ItemStack slot = matrix[i];
            if (slot == null || slot.getType() == Material.AIR) continue;

            if (!fleshConsumed && slot.getType() == Material.ROTTEN_FLESH) {
                decrementOrNull(matrix, i, slot);
                fleshConsumed = true;
            } else if (!emeraldConsumed && slot.getType() == Material.EMERALD) {
                decrementOrNull(matrix, i, slot);
                emeraldConsumed = true;
            } else if (!targetConsumed && TuningInput.isTunable(slot)) {
                matrix[i] = null;
                targetConsumed = true;
            }
        }
        inv.setMatrix(matrix);
    }

    // ---------------------------------------------------------------
    // 内部ヘルパー
    // ---------------------------------------------------------------

    private void decrementOrNull(ItemStack[] matrix, int i, ItemStack slot) {
        if (slot.getAmount() <= 1) {
            matrix[i] = null;
        } else {
            slot.setAmount(slot.getAmount() - 1);
        }
    }
}
