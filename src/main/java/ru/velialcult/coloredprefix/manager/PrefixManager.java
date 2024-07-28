package ru.velialcult.coloredprefix.manager;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.velialcult.coloredprefix.providers.ProvidersManager;
import ru.velialcult.coloredprefix.providers.luckperms.LuckPermsProvider;
import ru.velialcult.coloredprefix.providers.tab.TabProvider;
import ru.velialcult.coloredprefix.providers.vault.VaultProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrefixManager {

    private final FileConfiguration config;

    private final Map<String, String> closestGlassColors = new HashMap<>() {{
        put("DARK_BLUE", "BLUE");
        put("DARK_GREEN", "GREEN");
        put("DARK_GRAY", "GRAY");
        put("DARK_AQUA", "CYAN");
        put("AQUA", "CYAN");
        put("DARK_PURPLE", "PURPLE");
        put("DARK_RED", "RED");
        put("GOLD", "ORANGE");
        put("LIGHT_PURPLE", "PURPLE");
    }};

    private final ProvidersManager providersManager;

    public PrefixManager(ProvidersManager providersManager,
                         FileConfiguration config) {
        this.providersManager = providersManager;
        this.config = config;
    }

    public void resetPrefix(Player player) {
        String prefix = "";
        if (providersManager.useLuckPerms()) {
            LuckPermsProvider luckPermsProvider = providersManager.getLuckPermsProvider();
            prefix = luckPermsProvider.getGroupPrefix(player);
            luckPermsProvider.setPrefix(player, prefix);
        } else if (providersManager.useVault()) {
            VaultProvider vaultProvider = providersManager.getVaultProvider();
            String group = vaultProvider.getPermission().getPrimaryGroup(player);
            prefix = vaultProvider.getChat().getGroupPrefix("world", group);
        }

        if (!prefix.isEmpty()) {
            if (providersManager.useTAB()) {
                TabProvider tabProvider = providersManager.getTabProvider();
                tabProvider.changePlayerPrefix(player, prefix);
            }
        }
    }

    public String getPrefix(Player player) {
        String prefix = "Не найден";
        if (providersManager.useLuckPerms()) {
            prefix = providersManager.getLuckPermsProvider().getPrefix(player);
        }
        else if (providersManager.useVault()) {
            prefix = providersManager.getVaultProvider().getChat().getPlayerPrefix(player);
        }
        return prefix;
    }

    public void changePrefixColor(Player player, ChatColor color) {
        String prefix = getPrefix(player);

        StringBuilder formattingCodes = new StringBuilder();
        Matcher m = Pattern.compile("(&[k-o])").matcher(prefix);
        while (m.find()) {
            formattingCodes.append(m.group());
        }

        System.out.println(prefix);
        System.out.println(formattingCodes);
        String strippedPrefix = ChatColor.stripColor(prefix).replaceAll(config.getString("settings.prefix-color-regex"), "");
        System.out.println(strippedPrefix);
        String newPrefix = "&" + color.getChar() + formattingCodes + strippedPrefix + config.getString("settings.symbols-after-prefix");
        System.out.println(newPrefix);

        if (providersManager.useLuckPerms()) {
            providersManager.getLuckPermsProvider().setPrefix(player, newPrefix);
        }
        else if (providersManager.useVault()) {
            providersManager.getVaultProvider().getChat().setPlayerPrefix(player, newPrefix);
        }

        if (providersManager.useTAB()) {
            TabProvider tabProvider = providersManager.getTabProvider();
            tabProvider.changePlayerPrefix(player, newPrefix);
        }
    }

    public String getColoredPrefix(Player player, ChatColor color) {
        String prefix = getPrefix(player);

        StringBuilder formattingCodes = new StringBuilder();
        Matcher m = Pattern.compile("(&[k-o])").matcher(prefix);
        while (m.find()) {
            formattingCodes.append(m.group());
        }

        String strippedPrefix = ChatColor.stripColor(prefix).replaceAll(config.getString("settings.prefix-color-regex"), "");

        return "&" + color.getChar() + formattingCodes + strippedPrefix;
    }

    public Material getGlassBlockByColor(String color) {
        color = color.toUpperCase();

        if (!isValidColor(color)) {
            return null;
        }

        if (closestGlassColors.containsKey(color)) {
            color = closestGlassColors.get(color);
        }

        return XMaterial.valueOf(color + "_STAINED_GLASS").parseMaterial();
    }

    private boolean isValidColor(String color) {
        String[] validColors = {
                "WHITE", "ORANGE", "MAGENTA", "LIGHT_BLUE", "YELLOW",
                "LIME", "PINK", "GRAY", "LIGHT_GRAY", "AQUA",
                "PURPLE", "BLUE", "BROWN", "GREEN", "RED", "BLACK",
                "DARK_BLUE", "DARK_GREEN", "DARK_GRAY", "DARK_PURPLE",
                "DARK_RED", "GOLD", "DARK_AQUA", "LIGHT_PURPLE"
        };

        for (String validColor : validColors) {
            if (color.equals(validColor)) {
                return true;
            }
        }
        return false;
    }
}
