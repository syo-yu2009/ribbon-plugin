package io.github.oceanio.ribbonplugin.feature.tuning;

import io.github.oceanio.ribbonplugin.feature.tuner.TunerService;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * クラフト台イベントを受け取り、TunerService に処理を委譲するリスナー。
 * イベントの受け取り・プレビュー表示・プレイヤー通知のみを担当する。
 */
public class TuningListener implements Listener {

    private final JavaPlugin plugin;
    private final TunerService tunerService = new TunerService();

    public TuningListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // ---------------------------------------------------------------
    // クラフトプレビュー
    // ---------------------------------------------------------------

    @EventHandler(priority = EventPriority.HIGH)
    public void onPrepare(PrepareItemCraftEvent event) {
        CraftingInventory inv = event.getInventory();
        TuningInput input = TuningInput.parse(plugin, inv);
        if (input == null) return;

        ItemStack preview = input.targetItem().clone();
        ItemMeta meta = preview.getItemMeta();
        if (meta != null) {
            List<String> lore = meta.hasLore()
                    ? new ArrayList<>(Objects.requireNonNull(meta.getLore()))
                    : new ArrayList<>();
            lore.add("");
            lore.add(ChatColor.LIGHT_PURPLE + "▶ クリックでエンチャントを振り直す");
            lore.add(ChatColor.GRAY + "エンチャントスロット: 5〜7（ランダム）");
            meta.setLore(lore);
            preview.setItemMeta(meta);
        }
        event.getInventory().setResult(preview);
    }

    // ---------------------------------------------------------------
    // クラフト確定 → TunerService に委譲
    // ---------------------------------------------------------------

    @EventHandler(priority = EventPriority.HIGH)
    public void onCraft(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        CraftingInventory inv = event.getInventory();
        TuningInput input = TuningInput.parse(plugin, inv);
        if (input == null) return;

        event.setCancelled(true);

        // ロール
        Map<Enchantment, Integer> enchants = tunerService.rollEnchants(input.targetItem());
        if (enchants.isEmpty()) {
            player.sendMessage(ChatColor.RED + "このアイテムはチューニングできません。");
            return;
        }

        // エンチャント適用 & 素材消費
        ItemStack result = tunerService.applyEnchants(input.targetItem(), enchants);
        tunerService.consumeIngredients(inv);

        // プレイヤーに渡す（満杯なら足元にドロップ）
        player.getInventory().addItem(result)
                .values()
                .forEach(drop -> player.getWorld().dropItemNaturally(player.getLocation(), drop));

        // 通知
        player.sendMessage(ChatColor.LIGHT_PURPLE + "✦ チューニング完了！");
        enchants.forEach((e, lvl) ->
                player.sendMessage(ChatColor.GRAY + "  " + formatEnchant(e, lvl))
        );
    }

    // ---------------------------------------------------------------
    // 表示ヘルパー（通知専用のため Listener に残す）
    // ---------------------------------------------------------------

    private String formatEnchant(Enchantment e, int level) {
        String name = e.getKey().getKey().replace("_", " ");
        String[] roman = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        String lvlStr = (level > 0 && level < roman.length) ? roman[level] : String.valueOf(level);
        return ChatColor.WHITE + capitalize(name) + " " + ChatColor.YELLOW + lvlStr;
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}