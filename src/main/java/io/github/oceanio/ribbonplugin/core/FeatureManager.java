package io.github.oceanio.ribbonplugin.core;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class FeatureManager {
    private final Map<String,Feature> features =new HashMap<>();
    private final JavaPlugin plugin;

    public FeatureManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Feature feature) {
        features.put(feature.getName(), feature);
    }

    public void enable(String name){
        Feature f = features.get(name);
        if (f != null) {
            f.enable();
        }
    }

    public void disable(String name){
        Feature f = features.get(name);
        if (f != null) {
            f.disable();
        }
    }

    public boolean exists(String name){
        return features.containsKey(name);
    }
}
