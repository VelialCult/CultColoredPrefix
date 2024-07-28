package ru.velialcult.coloredprefix.providers.tab;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.event.player.PlayerLoadEvent;
import me.neznamy.tab.api.tablist.TabListFormatManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import ru.velialcult.coloredprefix.providers.ProvidersManager;

public class TabProvider {

    private final ProvidersManager providersManager;

    public TabProvider(ProvidersManager providersManager) {
        this.providersManager = providersManager;
    }

    public void changePlayerPrefix(Player player, String prefix) {
        if (providersManager.useTAB()) {
            TabListFormatManager tabListFormatManager = TabAPI.getInstance().getTabListFormatManager();
            TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(player.getUniqueId());

            if (tabListFormatManager != null && tabPlayer != null) {
                tabListFormatManager.setPrefix(tabPlayer, prefix);
            }
        }
    }

    public String getPrefix(Player player) {
        if (providersManager.useLuckPerms()) {
            return providersManager.getLuckPermsProvider().getPrefix(player);
        } else if (providersManager.useVault()) {
            return providersManager.getVaultProvider().getChat().getPlayerPrefix(player);
        } else {
            return "";
        }
    }

    public void registerEvent() {
        TabAPI.getInstance().getEventBus().register(PlayerLoadEvent.class, e -> {
            TabPlayer tabPlayer = e.getPlayer();
            OfflinePlayer offlinePlayer = (OfflinePlayer) tabPlayer.getPlayer();
            if (offlinePlayer.isOnline()) {
                Player player = offlinePlayer.getPlayer();
                String prefix = getPrefix(player);
                changePlayerPrefix(player, prefix);
            }
        });
    }
}
