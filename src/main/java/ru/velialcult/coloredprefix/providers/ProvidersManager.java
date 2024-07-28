package ru.velialcult.coloredprefix.providers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import ru.velialcult.coloredprefix.providers.luckperms.LuckPermsProvider;
import ru.velialcult.coloredprefix.providers.tab.TabProvider;
import ru.velialcult.coloredprefix.providers.vault.VaultProvider;


import java.util.HashMap;
import java.util.Map;

public class ProvidersManager {

    private final Map<String, Boolean> providers = new HashMap<>();
    private final Plugin plugin;

    public ProvidersManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        loadProvider("Vault", "1.7");
        loadProvider("TAB", "4.1.2");
        loadProvider("LuckPerms", "5.4.102");
    }

    private void loadProvider(String pluginName, String minVersion) {
        boolean isPluginLoaded = false;
        if (Bukkit.getPluginManager().isPluginEnabled(pluginName)) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
            String version = plugin.getDescription().getVersion();

            if (version.compareTo(minVersion) >= 0) {
                this.plugin.getLogger().info(pluginName + " найден, использую " + pluginName + " API");
                isPluginLoaded = true;
            }
            else {
                this.plugin.getLogger().warning("Версия " + pluginName + " < " + minVersion + " не поддерживается. Игнорирую данную зависимость");
            }
        }
        providers.put(pluginName, isPluginLoaded);
    }
    public boolean useVault() {
        return providers.getOrDefault("Vault", false);
    }

    public boolean useTAB() {
        return providers.getOrDefault("TAB", false);
    }

    public boolean useLuckPerms() { return providers.getOrDefault("LuckPerms", false);}

    public LuckPermsProvider getLuckPermsProvider() {
        if (useLuckPerms()) {
            return new LuckPermsProvider();
        }
        return null;
    }

    public VaultProvider getVaultProvider() {
        if (useVault()) {
            return new VaultProvider();
        }

        return null;
    }

    public TabProvider getTabProvider() {
        return new TabProvider(this);
    }
}
