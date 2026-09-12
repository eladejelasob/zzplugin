package com.zzsmp.zzscript.util;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {

    private static Economy economy;
    private static boolean checked = false;

    private static void setup() {
        if (checked) return;
        checked = true;
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            economy = rsp.getProvider();
        }
    }

    public static boolean isAvailable() {
        setup();
        return economy != null;
    }

    /**
     * @return oyuncunun bakiyesi, Vault yoksa -1
     */
    public static double getBalance(OfflinePlayer player) {
        setup();
        if (economy == null) return -1;
        return economy.getBalance(player);
    }
}
