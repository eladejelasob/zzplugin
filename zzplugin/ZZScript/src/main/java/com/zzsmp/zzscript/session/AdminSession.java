package com.zzsmp.zzscript.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Bir yöneticinin panel içinde yaptığı işlemi (ör: "kick için oyuncu seçiyor")
 * hatırlamak için basit bir oturum deposu. Menüler arası geçişte state taşımak
 * için kullanılıyor - kalıcı bir veri değil, sunucu kapanınca sıfırlanır.
 */
public class AdminSession {

    public enum Mode {
        VIEW_INFO,
        BAN,
        KICK
    }

    private static final Map<UUID, Mode> pendingMode = new HashMap<>();

    private AdminSession() {
    }

    public static void setMode(UUID admin, Mode mode) {
        pendingMode.put(admin, mode);
    }

    public static Mode getMode(UUID admin) {
        return pendingMode.getOrDefault(admin, Mode.VIEW_INFO);
    }

    public static void clear(UUID admin) {
        pendingMode.remove(admin);
    }
}
