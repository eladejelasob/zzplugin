# ZZScript

Z-Z SMP için özel yönetici paneli eklentisi.

## Ne yapıyor?

`/zzscript` (kısayollar: `/zz`, `/panel`) komutuyla açılan bir GUI panel:

- **Oyuncu Bilgisi** → sunucuya girmiş herkesin (online/offline) listesi, birine
  tıklayınca: skin, bakiye (Vault), evler (Essentials), ışınlanma ve
  ban/kick butonları
- **Banla / At (Kick)** → tıklayınca bir "örs" (anvil) ekranı açılır,
  `/ban ` ya da `/kick ` otomatik yazılmış gelir; sonra oyuncu listesinden
  bir isme tıklarsın, isim metne eklenir, sağdaki kağıda tıklayınca komut
  konsoldan çalıştırılır

Sadece `zzscript.admin` iznine (varsayılan: op) sahip olanlar kullanabilir.

## Nasıl derlenir?

Bende (bu sohbette) internet erişimi olmadığı için hazır .jar dosyasını
sana veremiyorum, ama internet olan herhangi bir bilgisayarda 2 dakikalık iş:

1. [Java 17+](https://adoptium.net/) ve [Maven](https://maven.apache.org/download.cgi) kur
2. Bu klasörde (pom.xml'in olduğu yerde) bir terminal aç
3. Şunu çalıştır:
   ```
   mvn clean package
   ```
4. `target/ZZScript.jar` dosyası oluşacak - bunu sunucunun `plugins/` klasörüne at

Alternatif: internetin olmadığı bir ortamdaysan, bu klasörü bir
[GitHub reposuna](https://github.com) yükleyip, GitHub Actions ile otomatik
derletebilirsin, ya da IntelliJ IDEA / Eclipse gibi bir IDE'de açıp
"Build Artifact" diyebilirsin.

## Gerekenler

- Paper veya Spigot 1.20+
- (Opsiyonel ama önerilir) **Vault** + bir ekonomi eklentisi → bakiye göstermek için
- (Opsiyonel) **Essentials** → ev listesini göstermek için
- İkisi de yoksa panel yine çalışır, sadece o alanlarda "bulunamadı" yazar

## Sonraki adımlar için fikirler

- Oyuncunun son bağlandığı IP'sini (moderasyon amaçlı) panelde gösterme
- Mute/warn sistemi ekleme
- Envanter görüntüleme ("ender chest'ine bak" gibi)
- IP'den ülke tespiti ile oyuncu bazlı saat dilimi (ayrı, daha büyük bir özellik)

Bunlardan istediğini söyle, ekleyelim.
