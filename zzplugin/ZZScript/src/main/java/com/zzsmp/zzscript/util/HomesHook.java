package com.zzsmp.zzscript.util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * Essentials kurulu değilse veya API'si farklı bir sürümdeyse patlamasın diye
 * her şey reflection ile, try/catch içinde yapılıyor.
 */
public class HomesHook {

    public static boolean isAvailable() {
        return Bukkit.getPluginManager().getPlugin("Essentials") != null;
    }

    @SuppressWarnings("unchecked")
    public static List<String> getHomes(OfflinePlayer player) {
        try {
            Plugin essentials = Bukkit.getPluginManager().getPlugin("Essentials");
            if (essentials == null) return Collections.emptyList();

            Method getUser = essentials.getClass().getMethod("getUser", org.bukkit.entity.Player.class);
            Object user;
            if (player.isOnline()) {
                user = getUser.invoke(essentials, player.getPlayer());
            } else {
                // Essentials'ın OfflinePlayer alan sürümü de var, o da denenir
                Method getUserOffline = essentials.getClass().getMethod("getUser", OfflinePlayer.class);
                user = getUserOffline.invoke(essentials, player);
            }
            if (user == null) return Collections.emptyList();

            Method getHomes = user.getClass().getMethod("getHomes");
            Object result = getHomes.invoke(user);
            if (result instanceof List) {
                return (List<String>) result;
            }
            if (result instanceof java.util.Set) {
                return List.copyOf((java.util.Set<String>) result);
            }
            return Collections.emptyList();
        } catch (Exception e) {
            // Essentials yok, sürüm uyuşmuyor, ya da oyuncu offline ve API desteklemiyor
            return Collections.emptyList();
        }
    }
}
