package com.zzsmp.zzscript.listener;

import com.zzsmp.zzscript.gui.*;
import com.zzsmp.zzscript.session.AdminSession;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuListener implements Listener {

    private final Plugin plugin;
    // Sayfa numarasını isim başına saklamak yerine, oyuncu listesi başlığından okuyoruz (basit ve yeterli)
    private static final Pattern PAGE_PATTERN = Pattern.compile("Sayfa (\\d+)");

    public MenuListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player admin)) return;
        String title = event.getView().getTitle();

        // Panel dışındaki hiçbir envanterle ilgilenmiyoruz
        boolean isOurMenu = title.equals(AdminMainMenu.TITLE)
                || title.startsWith("§6§lOyuncular §7(Sayfa")
                || title.startsWith(PlayerInfoMenu.TITLE_PREFIX);

        if (!isOurMenu) return;

        event.setCancelled(true);
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        if (title.equals(AdminMainMenu.TITLE)) {
            handleMainMenu(admin, event.getSlot());
        } else if (title.startsWith("§6§lOyuncular")) {
            handlePlayerList(admin, clicked, event.getSlot(), title);
        } else if (title.startsWith(PlayerInfoMenu.TITLE_PREFIX)) {
            handlePlayerInfo(admin, event.getSlot());
        }
    }

    private void handleMainMenu(Player admin, int slot) {
        switch (slot) {
            case 11 -> {
                AdminSession.setMode(admin.getUniqueId(), AdminSession.Mode.VIEW_INFO);
                admin.openInventory(PlayerListMenu.build(0));
            }
            case 13 -> {
                AdminSession.setMode(admin.getUniqueId(), AdminSession.Mode.BAN);
                admin.openInventory(PlayerListMenu.build(0));
            }
            case 15 -> {
                AdminSession.setMode(admin.getUniqueId(), AdminSession.Mode.KICK);
                admin.openInventory(PlayerListMenu.build(0));
            }
            default -> {
            }
        }
    }

    private void handlePlayerList(Player admin, ItemStack clicked, int slot, String title) {
        int currentPage = 0;
        Matcher m = PAGE_PATTERN.matcher(title);
        if (m.find()) {
            currentPage = Integer.parseInt(m.group(1)) - 1;
        }

        if (slot == 45) {
            admin.openInventory(PlayerListMenu.build(currentPage - 1));
            return;
        }
        if (slot == 53) {
            admin.openInventory(PlayerListMenu.build(currentPage + 1));
            return;
        }
        if (slot == 49) {
            AdminMainMenu.open(admin);
            return;
        }

        // Bir oyuncu kafasına tıklandı mı?
        if (clicked.getType() != Material.PLAYER_HEAD) return;
        SkullMeta meta = (SkullMeta) clicked.getItemMeta();
        if (meta == null || meta.getOwningPlayer() == null) return;

        OfflinePlayer target = meta.getOwningPlayer();
        AdminSession.Mode mode = AdminSession.getMode(admin.getUniqueId());

        switch (mode) {
            case VIEW_INFO -> {
                admin.closeInventory();
                PlayerInfoMenu.open(admin, target);
            }
            case BAN -> {
                admin.closeInventory();
                ActionAnvilMenu.open(plugin, admin, "/ban", target.getName());
            }
            case KICK -> {
                admin.closeInventory();
                ActionAnvilMenu.open(plugin, admin, "/kick", target.getName());
            }
        }
    }

    private void handlePlayerInfo(Player admin, int slot) {
        var targetUuid = PlayerInfoMenu.getTarget(admin.getUniqueId());
        if (targetUuid == null) return;
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetUuid);

        switch (slot) {
            case 15 -> { // oyun modu değiştir
                if (target.isOnline() && target.getPlayer() != null) {
                    Player online = target.getPlayer();
                    GameMode next = online.getGameMode() == GameMode.SURVIVAL ? GameMode.CREATIVE : GameMode.SURVIVAL;
                    online.setGameMode(next);
                    admin.sendMessage("§a[ZZScript] §f" + target.getName() + " artık " + next.name());
                    admin.closeInventory();
                    PlayerInfoMenu.open(admin, target);
                } else {
                    admin.sendMessage("§c[ZZScript] Oyuncu offline, oyun modu değiştirilemez.");
                }
            }
            case 16 -> { // ışınlan
                if (target.isOnline() && target.getPlayer() != null) {
                    admin.teleport(target.getPlayer());
                    admin.closeInventory();
                } else {
                    admin.sendMessage("§c[ZZScript] Oyuncu offline, ışınlanamazsın.");
                }
            }
            case 20 -> { // banla
                admin.closeInventory();
                ActionAnvilMenu.open(plugin, admin, "/ban", target.getName());
            }
            case 24 -> { // at
                admin.closeInventory();
                ActionAnvilMenu.open(plugin, admin, "/kick", target.getName());
            }
            case 22 -> { // geri
                admin.closeInventory();
                admin.openInventory(PlayerListMenu.build(0));
            }
            default -> {
            }
        }
    }
}
