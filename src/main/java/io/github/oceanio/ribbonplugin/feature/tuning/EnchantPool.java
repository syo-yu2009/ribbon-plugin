package io.github.oceanio.ribbonplugin.feature.tuning;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * アイテム種別ごとのエンチャントプール。
 * レアリティなし — 毎回 5〜7 スロットをフルレベル範囲でランダム付与。
 */
public class EnchantPool {

    private static final int SLOT_MIN = 5;
    private static final int SLOT_MAX = 7;

    /**
     * アイテムに合ったエンチャントマップをランダム生成して返す。
     * キー = Enchantment, 値 = レベル（1〜maxLevel）
     */
    public static Map<Enchantment, Integer> roll(ItemStack item, Random random) {
        List<EnchantEntry> pool = buildPool(item);
        if (pool.isEmpty()) return Collections.emptyMap();

        // 5〜7 スロットをランダムに決定
        int slots = SLOT_MIN + random.nextInt(SLOT_MAX - SLOT_MIN + 1);

        // プールをシャッフルして先頭 slots 個を選ぶ
        List<EnchantEntry> shuffled = new ArrayList<>(pool);
        Collections.shuffle(shuffled, random);

        Map<Enchantment, Integer> result = new LinkedHashMap<>();
        for (int i = 0; i < Math.min(slots, shuffled.size()); i++) {
            EnchantEntry e = shuffled.get(i);
            int maxLevel = e.enchantment().getMaxLevel();
            int level = 1 + (maxLevel > 1 ? random.nextInt(maxLevel) : 0);
            result.put(e.enchantment(), level);
        }
        return result;
    }

    // ---------------------------------------------------------------
    // 内部実装
    // ---------------------------------------------------------------

    private record EnchantEntry(Enchantment enchantment) {}

    /** アイテムの素材から対応プールを返す。 */
    private static List<EnchantEntry> buildPool(ItemStack item) {
        String type = item.getType().name();

        if (isSword(type) || isAxe(type)) return swordPool();
        if (type.equals("BOW") || type.equals("CROSSBOW")) return bowPool();
        if (isPickaxe(type) || isShovel(type) || isHoe(type)) return toolPool();
        if (isHelmet(type))     return helmetPool();
        if (isChestplate(type)) return chestPool();
        if (isLeggings(type))   return leggingsPool();
        if (isBoots(type))      return bootsPool();
        if (type.equals("TRIDENT")) return tridentPool();

        return Collections.emptyList();
    }

    private static List<EnchantEntry> swordPool() {
        return Arrays.asList(
                new EnchantEntry(Enchantment.SHARPNESS),
                new EnchantEntry(Enchantment.SMITE),
                new EnchantEntry(Enchantment.BANE_OF_ARTHROPODS),
                new EnchantEntry(Enchantment.FIRE_ASPECT),
                new EnchantEntry(Enchantment.KNOCKBACK),
                new EnchantEntry(Enchantment.LOOTING),
                new EnchantEntry(Enchantment.SWEEPING_EDGE),
                new EnchantEntry(Enchantment.UNBREAKING)

        );
    }

    private static List<EnchantEntry> bowPool() {
        return Arrays.asList(
                new EnchantEntry(Enchantment.POWER),
                new EnchantEntry(Enchantment.FLAME),
                new EnchantEntry(Enchantment.PUNCH),
                new EnchantEntry(Enchantment.INFINITY),
                new EnchantEntry(Enchantment.UNBREAKING),
                new EnchantEntry(Enchantment.MULTISHOT),
                new EnchantEntry(Enchantment.PIERCING),
                new EnchantEntry(Enchantment.QUICK_CHARGE)
        );
    }

    private static List<EnchantEntry> toolPool() {
        return Arrays.asList(
                new EnchantEntry(Enchantment.EFFICIENCY),
                new EnchantEntry(Enchantment.FORTUNE),
                new EnchantEntry(Enchantment.SILK_TOUCH),
                new EnchantEntry(Enchantment.UNBREAKING)
        );
    }

    private static List<EnchantEntry> armorBaseEnchantPool() {
        return Arrays.asList(
                new EnchantEntry(Enchantment.PROTECTION),
                new EnchantEntry(Enchantment.FIRE_PROTECTION),
                new EnchantEntry(Enchantment.PROJECTILE_PROTECTION),
                new EnchantEntry(Enchantment.BLAST_PROTECTION),
                new EnchantEntry(Enchantment.UNBREAKING),
                new EnchantEntry(Enchantment.THORNS)
        );
    }

    private static List<EnchantEntry> helmetPool() {
        List<EnchantEntry> list = new ArrayList<>(armorBaseEnchantPool());
        list.add(new EnchantEntry(Enchantment.FEATHER_FALLING));
        list.add(new EnchantEntry(Enchantment.FROST_WALKER));
        list.add(new EnchantEntry(Enchantment.DEPTH_STRIDER));
        list.add(new EnchantEntry(Enchantment.SOUL_SPEED));
        return list;
    }

    private static List<EnchantEntry> chestPool() {
        List<EnchantEntry> list = new ArrayList<>(armorBaseEnchantPool());
        return list;
    }

    private static List<EnchantEntry> leggingsPool() {
        List<EnchantEntry> list = new ArrayList<>(armorBaseEnchantPool());
        list.add(new EnchantEntry(Enchantment.SWIFT_SNEAK));
        return list;
    }

    private static List<EnchantEntry> bootsPool() {
        List<EnchantEntry> list = new ArrayList<>(armorBaseEnchantPool());
        list.add(new EnchantEntry(Enchantment.DEPTH_STRIDER));
        list.add(new EnchantEntry(Enchantment.FEATHER_FALLING));
        list.add(new EnchantEntry(Enchantment.SOUL_SPEED));
        list.add(new EnchantEntry(Enchantment.FROST_WALKER));
        return list;
    }

    private static List<EnchantEntry> tridentPool() {
        return Arrays.asList(
                new EnchantEntry(Enchantment.SHARPNESS),
                new EnchantEntry(Enchantment.IMPALING),
                new EnchantEntry(Enchantment.RIPTIDE),
                new EnchantEntry(Enchantment.CHANNELING),
                new EnchantEntry(Enchantment.LOYALTY),
                new EnchantEntry(Enchantment.UNBREAKING)
        );
    }

    private static boolean isSword(String t)      { return t.endsWith("_SWORD"); }
    private static boolean isAxe(String t)         { return t.endsWith("_AXE"); }
    private static boolean isPickaxe(String t)     { return t.endsWith("_PICKAXE"); }
    private static boolean isShovel(String t)      { return t.endsWith("_SHOVEL"); }
    private static boolean isHoe(String t)         { return t.endsWith("_HOE"); }
    private static boolean isHelmet(String t)      { return t.endsWith("_HELMET"); }
    private static boolean isChestplate(String t)  { return t.endsWith("_CHESTPLATE"); }
    private static boolean isLeggings(String t)    { return t.endsWith("_LEGGINGS"); }
    private static boolean isBoots(String t)       { return t.endsWith("_BOOTS"); }
}
