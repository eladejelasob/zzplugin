package com.zzsmp.zzscript.gui;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.List;

/**
 * "/ban " veya "/kick " ile başlayan bir metin kutusu açar (gerçek bir chat mesajı
 * değil, bir örs (anvil) arayüzü - oyuncu klavye ile yazabiliyor).
 * Oyuncu ismi listeden seçilip metnin sonuna eklenebiliyor.
 * Sağdaki kağıda (output slot) tıklayınca metindeki komut konsoldan çalıştırılır.
 */
public class ActionAnvilMenu {

    public static void open(Plugin plugin, Player admin, String commandPrefix, String prefillName) {
        String initialText = commandPrefix + " " + (prefillName == null ? "" : prefillName + " ");

        new AnvilGUI.Builder()
                .plugin(plugin)
                .title("Komutu düzenle, sonra onayla")
                .text(initialText)
                .itemLeft(new org.bukkit.inventory.ItemStack(org.bukkit.Material.PAPER))
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }
                    String finalText = stateSnapshot.getText().trim();
                    if (!finalText.isEmpty()) {
                        // Başındaki "/" varsa temizle, konsoldan komut çalıştırılacak
                        String cmd = finalText.startsWith("/") ? finalText.substring(1) : finalText;
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                        admin.sendMessage("§a[ZZScript] §fKomut çalıştırıldı: §7/" + cmd);
                    }
                    return List.of(AnvilGUI.ResponseAction.close());
                })
                .open(admin);
    }

    /**
     * Ban/Kick akışında ikinci adım: hedef oyuncuyu seçmek için oyuncu listesini açar.
     * Liste ekranında bir isme tıklandığında bu isim otomatik olarak komutun içine eklenir
     * (bkz. MenuListener - PlayerListMenu tıklaması, mode == BAN/KICK olduğunda
     * doğrudan bu sınıfın open() metodunu çağırıyor).
     */
}
