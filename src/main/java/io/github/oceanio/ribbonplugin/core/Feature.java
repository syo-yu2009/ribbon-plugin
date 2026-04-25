package io.github.oceanio.ribbonplugin.core;

import org.bukkit.plugin.java.JavaPlugin;

public interface Feature {
    String getName();
    void enable(JavaPlugin plugin);
    void disable();
}
