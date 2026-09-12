package com.zzsmp.zzscript;

import com.zzsmp.zzscript.gui.AdminMainMenu;
import com.zzsmp.zzscript.listener.MenuListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ZZScript extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new MenuListener(this), this);
        getLogger().info("ZZScript aktif - Z-Z SMP yönetici paneli hazır.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Bu komut sadece oyun içinden çalıştırılabilir.");
            return true;
        }
        if (!player.hasPermission("zzscript.admin")) {
            player.sendMessage("§cBu komutu kullanma yetkin yok.");
            return true;
        }
        AdminMainMenu.open(player);
        return true;
    }
}
