package com.zzsmp.zzscript.gui;

import com.zzsmp.zzscript.session.AdminSession;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AdminMainMenu {

    public static final String TITLE = "§6§lZ-Z SMP §fYönetici Paneli";

    public static Inventory build() {
        Inventory inv = Bukkit.createInventory(null, 27, TITLE);

        fillBorder(inv);

        inv.setItem(11, namedItem(Material.PLAYER_HEAD, "§a§lOyuncu Bilgisi",
                List.of("§7Online/offline tüm oyuncuları gör", "§7Skin, bakiye, evler ve daha fazlası", "", "§eTıkla ve seç")));

        inv.setItem(13, namedItem(Material.RED_WOOL, "§c§lBanla",
                List.of("§7Bir oyuncuyu sunucudan banla", "", "§eTıkla ve oyuncu seç")));

        inv.setItem(15, namedItem(Material.YELLOW_WOOL, "§e§lAt (Kick)",
                List.of("§7Bir oyuncuyu sunucudan at", "", "§eTıkla ve oyuncu seç")));

        return inv;
    }

    public static void open(Player admin) {
        AdminSession.clear(admin.getUniqueId());
        admin.openInventory(build());
    }

    private static void fillBorder(Inventory inv) {
        ItemStack filler = namedItem(Material.GRAY_STAINED_GLASS_PANE, " ", List.of());
        for (int i = 0; i < inv.getSize(); i++) {
            if (i < 9 || i >= inv.getSize() - 9 || i % 9 == 0 || i % 9 == 8) {
                inv.setItem(i, filler);
            }
        }
    }

    static ItemStack namedItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(new ArrayList<>(lore));
            item.setItemMeta(meta);
        }
        return item;
    }
}
