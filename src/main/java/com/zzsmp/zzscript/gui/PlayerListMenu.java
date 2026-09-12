package com.zzsmp.zzscript.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.SimpleDateFormat;
import java.util.*;

public class PlayerListMenu {

    // Bir sayfada kaç oyuncu kafası gösterilecek (45 = 5 satır, alt satır kontrol butonları için ayrıldı)
    private static final int PER_PAGE = 45;

    public static String titleFor(int page) {
        return "§6§lOyuncular §7(Sayfa " + (page + 1) + ")";
    }

    /**
     * Sunucuya en az bir kez girmiş tüm oyuncuları, en son görülene göre sıralı döner.
     */
    public static List<OfflinePlayer> getAllKnownPlayers() {
        List<OfflinePlayer> players = new ArrayList<>();
        for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
            if (op.hasPlayedBefore() || op.isOnline()) {
                players.add(op);
            }
        }
        players.sort((a, b) -> Long.compare(b.getLastSeen(), a.getLastSeen()));
        return players;
    }

    public static Inventory build(int page) {
        List<OfflinePlayer> all = getAllKnownPlayers();
        Inventory inv = Bukkit.createInventory(null, 54, titleFor(page));

        int from = page * PER_PAGE;
        int to = Math.min(from + PER_PAGE, all.size());

        int slot = 0;
        SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for (int i = from; i < to; i++) {
            OfflinePlayer op = all.get(i);
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            if (meta != null) {
                meta.setOwningPlayer(op);
                String status = op.isOnline() ? "§aOnline" : "§cOffline";
                meta.setDisplayName((op.isOnline() ? "§a" : "§7") + op.getName());
                meta.setLore(List.of(
                        "§7Durum: " + status,
                        "§7Son görülme: §f" + fmt.format(new Date(op.getLastSeen())),
                        "",
                        "§eSeçmek için tıkla"
                ));
                head.setItemMeta(meta);
            }
            inv.setItem(slot, head);
            slot++;
        }

        // Alt satır: geri / sayfa kontrolleri
        if (page > 0) {
            inv.setItem(45, AdminMainMenu.namedItem(Material.ARROW, "§e« Önceki Sayfa", List.of()));
        }
        if (to < all.size()) {
            inv.setItem(53, AdminMainMenu.namedItem(Material.ARROW, "§eSonraki Sayfa »", List.of()));
        }
        inv.setItem(49, AdminMainMenu.namedItem(Material.BARRIER, "§cAna Menüye Dön", List.of()));

        return inv;
    }
}
