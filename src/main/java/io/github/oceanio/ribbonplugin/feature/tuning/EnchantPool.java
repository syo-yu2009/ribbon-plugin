package io.github.oceanio.ribbonplugin.feature.tuning;

import org.bukkit.enchantments.Enchantment;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EnchantPool {

    // Java 8-11でも動作するようにrecordではなくクラスで定義
    public static class EnchantEntry {
        private final Enchantment enchantment;
        private final int level;
        private final int weight;

        public EnchantEntry(Enchantment enchantment, int level, int weight) {
            this.enchantment = enchantment;
            this.level = level;
            this.weight = weight;
        }

        public Enchantment getEnchantment() { return enchantment; }
        public int getLevel() { return level; }
        public int getWeight() { return weight; }
    }

    private final List<EnchantEntry> entries = new ArrayList<>();
    private int totalWeight = 0;

    public EnchantPool() {
        // デフォルトのエンチャントリスト
        addEntry(Enchantment.SHARPNESS, 5, 10);
        addEntry(Enchantment.PROTECTION, 4, 20);
        addEntry(Enchantment.EFFICIENCY, 5, 15);
        addEntry(Enchantment.MENDING, 1, 5);
    }

    public void addEntry(Enchantment ench, int level, int weight) {
        entries.add(new EnchantEntry(ench, level, weight));
        totalWeight += weight;
    }

    public EnchantEntry getRandomEntry() {
        if (entries.isEmpty() || totalWeight <= 0) return null;

        int randomIndex = ThreadLocalRandom.current().nextInt(totalWeight);
        int currentWeight = 0;

        for (EnchantEntry entry : entries) {
            currentWeight += entry.getWeight();
            if (randomIndex < currentWeight) {
                return entry;
            }
        }
        return entries.get(0);
    }
}