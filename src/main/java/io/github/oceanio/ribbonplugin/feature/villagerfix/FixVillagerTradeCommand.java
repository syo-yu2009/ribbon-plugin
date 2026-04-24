package io.github.oceanio.ribbonplugin.feature.villagerfix;

import org.bukkit.World;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.Material;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.util.List;
import java.util.ArrayList;

public class FixVillagerTradeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("fixstart");
        for (World world : Bukkit.getWorlds()) {
            for (Villager villager : world.getEntitiesByClass(Villager.class)) {

                List<MerchantRecipe> newRecipes = new ArrayList<>();

                for (MerchantRecipe recipe : villager.getRecipes()) {
                    if (recipe.getResult().getType() == Material.ENCHANTED_BOOK) {
                        continue;
                    }
                    newRecipes.add(recipe);
                }

                villager.setRecipes(newRecipes);
            }
        }

        sender.sendMessage("fixend");
        return true;
    }
}