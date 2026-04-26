package io.github.oceanio.ribbonplugin.feature.tuning;

import io.github.oceanio.ribbonplugin.core.Feature;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * チューニングフィーチャー。
 * <p>
 * 腐肉 + エメラルド + 武器/ツール/防具 をクラフト台に入れると
 * エンチャントを 5〜7 個ランダムに振り直す。
 * カスタムアイテム・専用レシピ登録は不要。
 */
public class TuningFeature implements Feature {
    private static final String NAME = "tuning";
    private final TuningService service;
    private TuningListener listener;

    public TuningFeature(TuningService service) {
        this.service = service;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void enable(JavaPlugin plugin) {
        //DI
        this.listener = new TuningListener(plugin, service);
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        plugin.getLogger().info("[ribbon-plugin][Tuning] チューニングフィーチャーを有効化しました。");
    }

    @Override
    public void disable() {
        org.bukkit.event.HandlerList.unregisterAll(listener);
    }
}

