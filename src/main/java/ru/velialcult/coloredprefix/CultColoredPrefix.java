package ru.velialcult.coloredprefix;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import ru.velialcult.coloredprefix.command.ColoredPrefixCommand;
import ru.velialcult.coloredprefix.file.InventoriesFile;
import ru.velialcult.coloredprefix.manager.PrefixManager;
import ru.velialcult.coloredprefix.providers.ProvidersManager;
import ru.velialcult.library.bukkit.file.FileRepository;
import ru.velialcult.library.bukkit.utils.ConfigurationUtil;
import ru.velialcult.library.update.UpdateChecker;

public class CultColoredPrefix extends JavaPlugin {

    private static CultColoredPrefix instance;

    private ProvidersManager providersManager;
    private PrefixManager prefixManager;

    private InventoriesFile inventoriesFile;

    @Override
    public void onEnable() {
        instance = this;
        long mills = System.currentTimeMillis();

        try {

            providersManager = new ProvidersManager(this);
            providersManager.load();

            UpdateChecker updateChecker = new UpdateChecker(this, "CultColoredPrefix");
            updateChecker.check();

            loadConfigs();

            if (providersManager.useTAB()) {
                providersManager.getTabProvider().registerEvent();
            }

            prefixManager = new PrefixManager(providersManager, getConfig());

            Bukkit.getPluginCommand("coloredprefix").setExecutor(new ColoredPrefixCommand());

            getLogger().info("Плагин был загружен за " + ChatColor.YELLOW + (System.currentTimeMillis() - mills) + "ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadConfigs() {
        this.saveDefaultConfig();
        ConfigurationUtil.loadConfigurations(this, "translation.yml", "inventories.yml");
        FileRepository.load(this);
        inventoriesFile = new InventoriesFile(this);
        inventoriesFile.load();
    }

    public static CultColoredPrefix getInstance() {
        return instance;
    }

    public ProvidersManager getProvidersManager() {
        return providersManager;
    }

    public PrefixManager getPrefixManager() {
        return prefixManager;
    }

    public InventoriesFile getInventoriesFile() {
        return inventoriesFile;
    }
}
