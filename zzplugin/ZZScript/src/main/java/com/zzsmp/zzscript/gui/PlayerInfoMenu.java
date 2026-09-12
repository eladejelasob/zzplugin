package com.zzsmp.zzscript.gui;

import com.zzsmp.zzscript.util.HomesHook;
import com.zzsmp.zzscript.util.VaultHook;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerInfoMenu {

    // Bu menüyü açan bir sonraki tıklamada hangi UUID hedef alınacak, burada tutuluyor
    private static final java.util.Map<UUID, UUID> viewingTarget = new java.util.HashMap<>();

    public static String TITLE_PREFIX = "§6§lOyuncu: §f";

    public static UUID getTarget(UUID admin) {
        return viewingTarget.get(admin);
    }

    public static void open(org.bukkit.entity.Player admin, OfflinePlayer target) {
        viewingTarget.put(admin.getUniqueId(), target.getUniqueId());
        admin.openInventory(build(target));
    }

    private static Inventory build(OfflinePlayer target) {
        Inventory inv = Bukkit.createInventory(null, 27, TITLE_PREFIX + target.getName());

        // Skin / kafa
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        if (headMeta != null) {
            headMeta.setOwningPlayer(target);
            headMeta.setDisplayName("§e" + target.getName());
            List<String> lore = new ArrayList<>();
            lore.add("§7UUID: §f" + target.getUniqueId());
            lore.add("§7Durum: " + (target.isOnline() ? "§aOnline" : "§cOffline"));
            headMeta.setLore(lore);
            head.setItemMeta(headMeta);
        }
        inv.setItem(4, head);

        // Bakiye
        double balance = VaultHook.getBalance(target);
        List<String> moneyLore = VaultHook.isAvailable()
                ? List.of("§7Bakiye: §a$" + String.format("%,.2f", balance))
                : List.of("§cVault bulunamadı");
        inv.setItem(11, AdminMainMenu.namedItem(Material.GOLD_INGOT, "§6§lPara", moneyLore));

        // Evler
        List<String> homes = HomesHook.getHomes(target);
        List<String> homesLore = new ArrayList<>();
        if (!HomesHook.isAvailable()) {
            homesLore.add("§cEssentials bulunamadı");
        } else if (homes.isEmpty()) {
            homesLore.add("§7Kayıtlı ev yok");
        } else {
            for (String h : homes) homesLore.add("§7- §f" + h);
        }
        inv.setItem(13, AdminMainMenu.namedItem(Material.RED_BED, "§d§lEvler", homesLore));

        // Oyun modu bilgisi (sadece online ise)
        String gm = target.isOnline() && target.getPlayer() != null
                ? target.getPlayer().getGameMode().name()
                : "§7(offline)";
        inv.setItem(15, AdminMainMenu.namedItem(Material.GRASS_BLOCK, "§a§lOyun Modu",
                List.of("§7Şu an: §f" + gm, "", "§eTıkla: Survival/Creative değiştir (online ise)")));

        // Işınlan
        inv.setItem(16, AdminMainMenu.namedItem(Material.ENDER_PEARL, "§b§lYanına Işınlan",
                List.of("§7Oyuncunun yanına ışınlanır (online olmalı)")));

        // Ban / Kick
        inv.setItem(20, AdminMainMenu.namedItem(Material.RED_WOOL, "§c§lBanla", List.of("§7Bu oyuncuyu banla")));
        inv.setItem(24, AdminMainMenu.namedItem(Material.YELLOW_WOOL, "§e§lAt (Kick)", List.of("§7Bu oyuncuyu sunucudan at")));

        inv.setItem(22, AdminMainMenu.namedItem(Material.BARRIER, "§7« Oyuncu Listesine Dön", List.of()));

        return inv;
    }
}
