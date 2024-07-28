package ru.velialcult.coloredprefix.file;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import ru.velialcult.coloredprefix.CultColoredPrefix;
import ru.velialcult.library.bukkit.file.FileRepository;

public class TranslationsFile {

    private static final FileConfiguration config = FileRepository.getByName(CultColoredPrefix.getInstance(), "translation.yml").getConfiguration();

    public static String getTranslationColor(ChatColor color) {
        return getTranslation("colors." + color.name());
    }

    private static String getTranslation(String path) {
        if (!config.contains(path)) {
            return "&cПеревод не найден";
        } else {
            return config.getString(path);
        }
    }
}
