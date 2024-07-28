package ru.velialcult.coloredprefix.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.velialcult.coloredprefix.CultColoredPrefix;
import ru.velialcult.coloredprefix.file.InventoriesFile;
import ru.velialcult.coloredprefix.file.TranslationsFile;
import ru.velialcult.coloredprefix.manager.PrefixManager;
import ru.velialcult.coloredprefix.menu.buttons.BackButton;
import ru.velialcult.coloredprefix.menu.buttons.ForwardButton;
import ru.velialcult.library.bukkit.utils.InventoryUtil;
import ru.velialcult.library.core.VersionAdapter;
import ru.velialcult.library.java.text.ReplaceData;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.impl.SuppliedItem;
import xyz.xenondevs.invui.window.Window;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChangePrefixColorMenu {

    public static void generateInventory(Player player) {

        InventoriesFile inventoriesFile = CultColoredPrefix.getInstance().getInventoriesFile();
        PrefixManager prefixManager = CultColoredPrefix.getInstance().getPrefixManager();

        char itemsChar = inventoriesFile.getFileOperations().getString("inventories.main.items.default.symbol").charAt(0);

        List<Item> items = Arrays.stream(ChatColor.values())
                .filter(ChatColor::isColor)
                .map(color -> new SuppliedItem(() ->
                                                          s -> {
                    if (player.hasPermission("cultcoloredprefix.changecolor")) {
                        return VersionAdapter.getItemBuilder().setType(prefixManager.getGlassBlockByColor(color.name()))
                                .setDisplayName(inventoriesFile.getFileOperations().getString(
                                        "inventories.main.items.default.displayName",
                                        new ReplaceData("{prefix}", prefixManager.getColoredPrefix(player, color)),
                                        new ReplaceData("{color}", color + TranslationsFile.getTranslationColor(color))
                                ))
                                .setLore(inventoriesFile.getFileOperations().getList(
                                        "inventories.main.items.default.lore",
                                        new ReplaceData("{prefix}", prefixManager.getColoredPrefix(player, color)),
                                        new ReplaceData("{color}", color + TranslationsFile.getTranslationColor(color))
                                ))
                                .build();
                    } else {
                        return InventoryUtil.createItem(inventoriesFile.getConfig(), "inventories.buttons.dont-have-permission");
                    }
                                                          },
                                                  click -> {
                                                      if (player.hasPermission("cultcoloredprefix.changecolor")) {
                                                          prefixManager.changePrefixColor(player, color);
                                                          player.closeInventory();
                                                      }
                                                      return true;
                                                  })).collect(Collectors.toList());

        String[] structure =  inventoriesFile.getFileOperations().getList("inventories.main.structure").toArray(new String[0]);

        Gui.Builder<PagedGui<Item>, PagedGui.Builder<Item>> builder = PagedGui.items()
                .setStructure(structure)
                .addIngredient(itemsChar, Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('b', new BackButton())
                .addIngredient('n', new ForwardButton())
                .setContent(items);

        Map<Character, SuppliedItem> customItemList = InventoryUtil.createItems(player, inventoriesFile.getConfig(),
                                                                     "inventories.main.items",
                                                                                (event, path) -> {
            List<String> commands = inventoriesFile.getConfig().getStringList(path + ".actionOnClick");
            for (String command : commands) {
                        if (command.startsWith("[message]")) {
                            String message = command.replace("[message]", "");
                            VersionAdapter.MessageUtils().sendMessage(player, message);
                        }

                        if (command.startsWith("[execute]")) {
                            String executeCommand = command.replace("[execute]", "");
                            Bukkit.dispatchCommand(player, executeCommand);
                        }

                        if (command.equals("[reset]")) {
                            prefixManager.resetPrefix(player);
                        }

                        if (command.equals("[close]")) {
                            player.closeInventory();
                        }
                    }
                });

        InventoryUtil.setItems(builder, customItemList);


        Window window = Window.single()
                .setViewer(player)
                .setTitle(inventoriesFile.getFileOperations().getString("inventories.main.title"))
                .setGui(builder.build())
                .build();
        window.open();
    }
}
