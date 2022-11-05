package com.soychristian.unlimitedhomes;

import com.soychristian.unlimitedhomes.commands.UnlimitedHomesCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class UnlimitedHomes extends JavaPlugin {

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        getCommand("unlimitedhomes").setExecutor(new UnlimitedHomesCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
