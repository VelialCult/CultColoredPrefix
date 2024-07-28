package ru.velialcult.coloredprefix.providers.vault;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultProvider {
    private Chat chat;
    private Economy economy;
    protected Permission permission;

    public Economy getEconomy() {
        if (economy == null) {
            setupEconomy();
        }

        return economy;
    }

    public Permission getPermission() {
        if (permission == null) {
            setupPermission();
        }

        return permission;
    }

    public void setupPermission() {
        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }

    }

    public void setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

    }

    public boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return chat != null;
    }

    public Chat getChat() {
        if (chat == null) {
            setupChat();
        }

        return chat;
    }
}
