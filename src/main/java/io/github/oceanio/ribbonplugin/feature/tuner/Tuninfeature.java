package io.github.oceanio.ribbonplugin.feature.tuning;

import io.github.oceanio.ribbonplugin.core.Feature;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * チューニングフィーチャー。
 *
 * 腐肉 + エメラルド + 武器/ツール/防具 をクラフト台に入れると
 * エンチャントを 5〜7 個ランダムに振り直す。
 * カスタムアイテム・専用レシピ登録は不要。
 */
public class TuningFeature implements Feature {

    private static final String NAME = "tuning";
    private JavaPlugin plugin;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void enable(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(new TuningListener(plugin), plugin);
        plugin.getLogger().info("[Tuning] チューニングフィーチャーを有効化しました。");
    }

    @Override
    public void disable() {
        if (plugin != null) {
            plugin.getLogger().info("[Tuning] チューニングフィーチャーを無効化しました。");
        }
    }
}

