package io.github.oceanio.ribbonplugin.feature.tracker;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public final class ToolTrackerListener implements Listener {
    private final ToolTrackerService service;

    public ToolTrackerListener(ToolTrackerService service) {
        this.service = service;
    }

    //Event Method
    @EventHandler
    public void onBrokeBlock (BlockBreakEvent event) {
        Player p = event.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        if (!service.isTools(item)) return;
        service.addUse(item);
    }
}
