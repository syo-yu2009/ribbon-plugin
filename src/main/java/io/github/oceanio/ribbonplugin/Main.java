package io.github.oceanio.ribbonplugin;

import io.github.oceanio.ribbonplugin.core.FeatureManager;
import io.github.oceanio.ribbonplugin.feature.banenchant.BanEnchantFeature;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private FeatureManager featureManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        featureManager = new FeatureManager(this);

        //ここでfeature登録
        featureManager.register(new BanEnchantFeature());

        //要素許可
        featureManager.enableAll();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
