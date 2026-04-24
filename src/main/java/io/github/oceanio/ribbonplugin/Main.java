package io.github.oceanio.ribbonplugin;

import io.github.oceanio.ribbonplugin.feature.tracker.MinersPickaxe;
import io.github.oceanio.ribbonplugin.feature.repair.RepairItems;
import io.github.oceanio.ribbonplugin.feature.repair.BanEnchant;
import io.github.oceanio.ribbonplugin.feature.villagerfix.FixNewVillagerTrade;
import io.github.oceanio.ribbonplugin.feature.villagerfix.FixVillagerTradeCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new MinersPickaxe(), this);
        getServer().getPluginManager().registerEvents(new FixNewVillagerTrade(), this);
        getServer().getPluginManager().registerEvents(new RepairItems(), this);
        BanEnchant banEnchant = new BanEnchant(this);
        getServer().getPluginManager().registerEvents(banEnchant, this);
        getCommand("ban_enchant").setExecutor(banEnchant);
        getCommand("villager_fix").setExecutor(new FixVillagerTradeCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
