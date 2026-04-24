package io.github.oceanio.ribbonplugin.feature.repair;

import org.bukkit.Bukkit;
import org.bukkit.block.ShulkerBox;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class BanEnchant implements Listener,CommandExecutor {
    private final Set<Enchantment> ban_enchant = Set.of(
            Enchantment.MENDING,
            Enchantment.BINDING_CURSE
    );
    private final JavaPlugin plugin;

    private boolean onFlagChest = false;

    public BanEnchant(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private boolean isBanned(Enchantment enchantment) {
        return ban_enchant.contains(enchantment);
    }
    private void removeBanEnchants(ItemStack item){
        if (item == null || !item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof BlockStateMeta blockMeta) {
            if (blockMeta.getBlockState() instanceof ShulkerBox shulker) {
                for (ItemStack inside : shulker.getInventory().getContents()) {
                    removeBanEnchants(inside);
                }
                blockMeta.setBlockState(shulker);
                meta = blockMeta;
            }
        }
        for (Enchantment enchant : ban_enchant) {
            if (meta.hasEnchant(enchant)) {
                meta.removeEnchant(enchant);
            }
        }
        item.setItemMeta(meta);
    }
    private void inventoryRemove(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (ItemStack item : player.getInventory().getContents()) {
                removeBanEnchants(item);
            }
            for (ItemStack item : player.getEnderChest().getContents()){
                removeBanEnchants(item);
            }
            for (ItemStack item : player.getInventory().getArmorContents()){
                removeBanEnchants(item);
            }
            removeBanEnchants(player.getInventory().getItemInOffHand());
        }
    }

    @Override
    public  boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (!sender.hasPermission("banenchant_use")) {
            sender.sendMessage("権限がありません");
            return true;
        }
        if (args.length < 1){
            sender.sendMessage(
                    "/ban_enchant inventory\n" +
                            "/ban_enchant chest <on|off>"
            );
            return true;
        }
        if (args[0].equalsIgnoreCase("chest")){
            if (args.length < 2) {
                sender.sendMessage(
                        "/ban_enchant inventory\n" +
                                "/ban_enchant chest <on|off>"
                );
            }else if(args[1].equalsIgnoreCase("on")){
                onFlagChest = true;
            } else if (args[1].equalsIgnoreCase("off")) {
                onFlagChest = false;
            } else {
                sender.sendMessage(
                        "/ban_enchant inventory\n" +
                                "/ban_enchant chest <on|off>"
                );
            }
            return true;
        } else if (args[0].equalsIgnoreCase("inventory")){
            inventoryRemove();
            sender.sendMessage("removed");
            return true;
        } else {
            sender.sendMessage(
                    "/ban_enchant inventory\n" +
                    "/ban_enchant chest <on|off>"
            );
            return true;
        }
    }

    @EventHandler
    public void onEnchantTable(EnchantItemEvent event){
        event.getEnchantsToAdd().entrySet().removeIf(e -> isBanned(e.getKey()));
    }
    @EventHandler
    public void onLootTable(LootGenerateEvent event){
        for (ItemStack item : event.getLoot()){
            if (item == null || !item.hasItemMeta()) continue;
            ItemMeta meta = item.getItemMeta();
            meta.getEnchants().keySet().removeIf(this::isBanned);
            item.setItemMeta(meta);
        }
    }
    @EventHandler
    public void onChestOpen(InventoryOpenEvent event){
        if (!onFlagChest) return;
        Inventory inventory = event.getInventory();
        if (inventory.getType() !=  InventoryType.CHEST) return;
        for (ItemStack item : inventory.getContents()) {
            removeBanEnchants(item);
        }
    }
}
