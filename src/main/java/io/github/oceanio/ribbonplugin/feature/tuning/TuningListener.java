package io.github.oceanio.ribbonplugin.feature.tuning;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Map;

public class TuningListener implements Listener {
    private final JavaPlugin plugin;
    private final TuningService service;

    public TuningListener(JavaPlugin plugin, TuningService service) {
        this.plugin = plugin;
        this.service = service;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPrepare(PrepareItemCraftEvent event) {
        TuningInput input = TuningInput.parse(plugin, event.getInventory());
        if (input == null) return;

        // 上限5つチェック
        if (input.targetItem().getEnchantments().size() >= 5) {
            return;
        }

        ItemStack result = input.targetItem().clone();
        var meta = result.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.YELLOW + "??? エンチャント付与");
            result.setItemMeta(meta);
        }
        event.getInventory().setResult(result);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCraft(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        CraftingInventory inv = event.getInventory();
        TuningInput input = TuningInput.parse(plugin, inv);
        if (input == null) return;

        if (input.targetItem().getEnchantments().size() >= 5) {
            player.sendMessage(ChatColor.RED + "このアイテムには既に5つのエンチャントが付いています！");
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true); // バニラのクラフトをキャンセルして独自処理

        // ランダムエンチャント決定
        Map<Enchantment, Integer> enchants = service.rollEnchantment(input.targetItem());
        ItemStack result = input.targetItem().clone();

        enchants.forEach((ench, lvl) -> result.addUnsafeEnchantment(ench, lvl));

        // 素材消費とアイテム付与
        service.consumeIngredients(inv);
        player.getInventory().addItem(result).values().forEach(drop ->
                player.getWorld().dropItemNaturally(player.getLocation(), drop));

        player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
        player.sendMessage(ChatColor.GREEN + "エンチャントが完了しました！");
    }
}
