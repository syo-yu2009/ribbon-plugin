package io.github.oceanio.ribbonplugin.feature.villagerfix;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.inventory.MerchantRecipe;


public class FixNewVillagerTrade implements Listener {
    @EventHandler
    public void villagerEnchantedBooksDeleter(VillagerAcquireTradeEvent event) {
        MerchantRecipe Recipe = event.getRecipe();
        if (Recipe.getResult().getType() == Material.ENCHANTED_BOOK) {
            event.setCancelled(true);
        }
    }
}
