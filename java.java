import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

// ==================== D. ÖZEL HATA YÖNETİMİ ====================
// KuantumCokusuException: Özel hata sınıfı
class KuantumCokusuException extends Exception {
    public KuantumCokusuException(String message) {
        super(message);
    }
}

// ==================== B. ARAYÜZ SEGREGATION ====================
// IKritik: Sadece tehlikeli nesneler için arayüz
interface IKritik {
    void AcilDurumSogutmasi() throws KuantumCokusuException;
}

// ==================== A. TEMEL YAPI (Abstract Class & Encapsulation) ====================
// KuantumNesnesi: Tüm nesnelerin atası olan soyut sınıf
abstract class KuantumNesnesi {
    // ÖZELLİKLER (Properties) - Kapsülleme (Encapsulation)
    private String id;
    private double stabilite;
    private int tehlikeSeviyesi;

    // Constructor
    public KuantumNesnesi(String id, double stabilite, int tehlikeSeviyesi) throws KuantumCokusuException {
        this.id = id;
        setTehlikeSeviyesi(tehlikeSeviyesi);
        setStabilite(stabilite);
    }

    // Getter ve Setter metotları - Kapsülleme
    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    // Stabilite getter/setter - Kapsülleme ile 0-100 arası kontrol
    public double getStabilite() {
        return stabilite;
    }

    public void setStabilite(double stabilite) throws KuantumCokusuException {
        // 100'den büyük girilmesini engelleme
        if (stabilite > 100) {
            this.stabilite = 100;
        } 
        // 0'dan küçük olunca özel hata fırlatma
        else if (stabilite < 0) {
            throw new KuantumCokusuException(id + " kimlikli nesne patladı!");
        } 
        else {
            this.stabilite = stabilite;
        }
    }

    // TehlikeSeviyesi getter/setter - Kapsülleme ile 1-10 arası kontrol
    public int getTehlikeSeviyesi() {
        return tehlikeSeviyesi;
    }

    public void setTehlikeSeviyesi(int tehlikeSeviyesi) {
        if (tehlikeSeviyesi >= 1 && tehlikeSeviyesi <= 10) {
            this.tehlikeSeviyesi = tehlikeSeviyesi;
        } else {
            throw new IllegalArgumentException("Tehlike seviyesi 1-10 arasında olmalıdır!");
        }
    }

    // ==================== A. SOYUT METOT ====================
    // AnalizEt(): Soyut (abstract) metot
    public abstract void AnalizEt() throws KuantumCokusuException;

    // ==================== A. NORMAL METOT ====================
    // DurumBilgisi(): Sanal metot
    public String DurumBilgisi() {
        return String.format("ID: %s, Stabilite: %.2f%%", id, stabilite);
    }
}

// ==================== C. NESNE ÇEŞİTLERİ (Inheritance & Polymorphism) ====================
// 1. VeriPaketi: Sıradan, güvenli veri (IKritik DEĞİL)
class VeriPaketi extends KuantumNesnesi {
    public VeriPaketi(String id) throws KuantumCokusuException {
        super(id, 100, 1);
    }

    // AnalizEt() override - Stabilite 5 birim düşer
    @Override
    public void AnalizEt() throws KuantumCokusuException {
        setStabilite(getStabilite() - 5);
        System.out.println("Veri içeriği okundu.");
    }
}

// 2. KaranlikMadde: Tehlikeli madde (IKritik uygular)
class KaranlikMadde extends KuantumNesnesi implements IKritik {
    public KaranlikMadde(String id) throws KuantumCokusuException {
        super(id, 100, 8);
    }

    // AnalizEt() override - Stabilite 15 birim düşer
    @Override
    public void AnalizEt() throws KuantumCokusuException {
        setStabilite(getStabilite() - 15);
        System.out.println("Karanlık Madde analiz ediliyor...");
    }

    // ==================== B. ARAYÜZ METODU ====================
    // AcilDurumSogutmasi(): Stabiliteyi +50 artırır
    @Override
    public void AcilDurumSogutmasi() throws KuantumCokusuException {
        setStabilite(getStabilite() + 50);
        System.out.println(getID() + " için acil soğutma yapıldı!");
    }
}

// 3. AntiMadde: Çok tehlikeli madde (IKritik uygular)
class AntiMadde extends KuantumNesnesi implements IKritik {
    public AntiMadde(String id) throws KuantumCokusuException {
        super(id, 100, 10);
    }

    // AnalizEt() override - Stabilite 25 birim düşer
    @Override
    public void AnalizEt() throws KuantumCokusuException {
        setStabilite(getStabilite() - 25);
        System.out.println("Evrenin dokusu titriyor... Anti Madde analiz ediliyor!");
    }

    // ==================== B. ARAYÜZ METODU ====================
    // AcilDurumSogutmasi(): Stabiliteyi +50 artırır
    @Override
    public void AcilDurumSogutmasi() throws KuantumCokusuException {
        setStabilite(getStabilite() + 50);
        System.out.println(getID() + " için acil soğutma yapıldı! Kritik durum atlatıldı.");
    }

    // ==================== C. POLYMORPHISM ====================
    // DurumBilgisi() override - Özel durum bilgisi
    @Override
    public String DurumBilgisi() {
        return String.format("ID: %s, Stabilite: %.2f%% - ÇOK TEHLİKELİ!", getID(), getStabilite());
    }
}

public class KuantumAmbari {
    // ==================== 1. GENERIC LIST ====================
    // List<KuantumNesnesi>: Nesneleri saklamak için generic list
    static List<KuantumNesnesi> envanter = new ArrayList<>();
    static int nesneSayaci = 1;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            System.out.println("KUANTUM KAOS YÖNETİMİ - OMEGA SEKTÖRÜ\n");

            // ==================== 3. OYNANIŞ DÖNGÜSÜ (MAIN LOOP) ====================
            while (true) {
                try {
                    menuGoster();
                    int secim = scanner.nextInt();
                    scanner.nextLine(); // Newline karakterini temizle

                    switch (secim) {
                        case 1:
                            yeniNesneEkle();
                            break;
                        case 2:
                            envanteriListele();
                            break;
                        case 3:
                            nesneAnalizEt();
                            break;
                        case 4:
                            acilSoğutmaYap();
                            break;
                        case 5:
                            System.out.println("Sistem kapatılıyor...");
                            scanner.close();
                            return;
                        default:
                            System.out.println("Geçersiz seçim!");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Lütfen sayı giriniz!");
                    scanner.nextLine(); // Geçersiz girişi temizle
                } catch (KuantumCokusuException e) {
                    throw e; // Ana try-catch'e gönder
                } catch (Exception e) {
                    System.out.println("Hata: " + e.getMessage());
                }

                System.out.println("\nDevam etmek için bir tuşa basın...");
                scanner.nextLine();
                System.out.print("\033[H\033[2J"); // Ekranı temizle
                System.out.flush();
            }
        } 
        // ==================== 4. GAME OVER ====================
        catch (KuantumCokusuException e) {
            System.out.println("\n\n=========================================");
            System.out.println("  SİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR...");
            System.out.println("  Sebep: " + e.getMessage());
            System.out.println("=========================================");
        }
    }

    static void menuGoster() {
        System.out.println("========================================");
        System.out.println("   KUANTUM AMBARI KONTROL PANELİ");
        System.out.println("========================================");
        System.out.println("1. Yeni Nesne Ekle");
        System.out.println("2. Tüm Envanteri Listele");
        System.out.println("3. Nesneyi Analiz Et");
        System.out.println("4. Acil Durum Soğutması Yap");
        System.out.println("5. Çıkış");
        System.out.println("========================================");
        System.out.print("Seçiminiz: ");
    }

    static void yeniNesneEkle() throws KuantumCokusuException {
        System.out.println("\nNesne Türü Seçin:");
        System.out.println("1. Veri Paketi");
        System.out.println("2. Karanlık Madde");
        System.out.println("3. Anti Madde");
        System.out.println("4. Rastgele Üret");
        System.out.print("Seçim: ");
        
        int secim = scanner.nextInt();
        scanner.nextLine();
        String id = "OBJ-" + String.format("%03d", nesneSayaci);
        nesneSayaci++;

        KuantumNesnesi yeniNesne;

        if (secim == 4) {
            secim = ThreadLocalRandom.current().nextInt(1, 4);
        }

        switch (secim) {
            case 1:
                yeniNesne = new VeriPaketi(id);
                System.out.println("Yeni Veri Paketi eklendi: " + id);
                break;
            case 2:
                yeniNesne = new KaranlikMadde(id);
                System.out.println("Yeni Karanlık Madde eklendi: " + id);
                break;
            case 3:
                yeniNesne = new AntiMadde(id);
                System.out.println("Yeni Anti Madde eklendi: " + id + " - DİKKAT!");
                break;
            default:
                System.out.println("Geçersiz seçim!");
                return;
        }

        envanter.add(yeniNesne);
        System.out.println("Toplam nesne sayısı: " + envanter.size());
    }

    static void envanteriListele() {
        if (envanter.isEmpty()) {
            System.out.println("Envanter boş!");
            return;
        }

        System.out.println("\n=== ENVANTER DURUM RAPORU ===");
        System.out.println("Toplam Nesne: " + envanter.size());
        System.out.println("----------------------------");

        // ==================== 2. POLIMORFİZM ====================
        // Listeleme yaparken hepsinin DurumBilgisi() metodunu çağırma
        for (int i = 0; i < envanter.size(); i++) {
            System.out.println((i + 1) + ". " + envanter.get(i).DurumBilgisi());
        }

        // Stabilite istatistikleri
        int kritikDurum = 0;
        int tehlikeliDurum = 0;
        
        for (KuantumNesnesi nesne : envanter) {
            if (nesne.getStabilite() < 20) kritikDurum++;
            else if (nesne.getStabilite() < 50) tehlikeliDurum++;
        }

        System.out.println("----------------------------");
        System.out.println("Kritik Stabilite (<%20): " + kritikDurum + " nesne");
        System.out.println("Tehlikeli Stabilite (<%50): " + tehlikeliDurum + " nesne");
    }

    static void nesneAnalizEt() throws KuantumCokusuException {
        if (envanter.isEmpty()) {
            System.out.println("Analiz edilecek nesne yok!");
            return;
        }

        System.out.print("Analiz edilecek nesnenin ID'sini girin: ");
        String arananID = scanner.nextLine();

        KuantumNesnesi nesne = null;
        for (KuantumNesnesi n : envanter) {
            if (n.getID().equals(arananID)) {
                nesne = n;
                break;
            }
        }

        if (nesne == null) {
            System.out.println("Nesne bulunamadı!");
            return;
        }

        System.out.println("Analiz başlıyor: " + nesne.DurumBilgisi());
        nesne.AnalizEt();
        System.out.println("Güncel durum: " + nesne.DurumBilgisi());
    }

    static void acilSoğutmaYap() throws KuantumCokusuException {
        if (envanter.isEmpty()) {
            System.out.println("Soğutulacak nesne yok!");
            return;
        }

        System.out.print("Soğutulacak nesnenin ID'sini girin: ");
        String arananID = scanner.nextLine();

        KuantumNesnesi nesne = null;
        for (KuantumNesnesi n : envanter) {
            if (n.getID().equals(arananID)) {
                nesne = n;
                break;
            }
        }

        if (nesne == null) {
            System.out.println("Nesne bulunamadı!");
            return;
        }

        // ==================== 3. TYPE CHECKING ====================
        // instanceof anahtar kelimesi ile IKritik kontrolü
        if (nesne instanceof IKritik) {
            IKritik kritikNesne = (IKritik) nesne;
            kritikNesne.AcilDurumSogutmasi();
            System.out.println("Güncel durum: " + nesne.DurumBilgisi());
        } else {
            System.out.println("Bu nesne soğutulamaz! (Sadece kritik nesneler soğutulabilir)");
        }
    }
}