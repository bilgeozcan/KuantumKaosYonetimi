using System;
using System.Collections.Generic;

namespace KuantumAmbari
{
    // ==================== D. ÖZEL HATA YÖNETİMİ ====================
    // KuantumCokusuException: Özel hata sınıfı
    public class KuantumCokusuException : Exception
    {
        public KuantumCokusuException(string message) : base(message) { }
    }

    // ==================== B. ARAYÜZ SEGREGATION ====================
    // IKritik: Sadece tehlikeli nesneler için arayüz
    public interface IKritik
    {
        void AcilDurumSogutmasi();
    }

    // ==================== A. TEMEL YAPI (Abstract Class & Encapsulation) ====================
    // KuantumNesnesi: Tüm nesnelerin atası olan soyut sınıf
    public abstract class KuantumNesnesi
    {
        // ÖZELLİKLER (Properties) - Kapsülleme (Encapsulation)
        private string id;
        private double stabilite;
        private int tehlikeSeviyesi;

        // ID property - Kapsülleme
        public string ID
        {
            get => id;
            set => id = value;
        }

        // Stabilite property - Kapsülleme ile 0-100 arası kontrol
        public double Stabilite
        {
            get => stabilite;
            set
            {
                // 100'den büyük girilmesini engelleme
                if (value > 100)
                    stabilite = 100;
                // 0'dan küçük olunca özel hata fırlatma
                else if (value < 0)
                    throw new KuantumCokusuException($"{ID} kimlikli nesne patladı!");
                else
                    stabilite = value;
            }
        }

        // TehlikeSeviyesi property - Kapsülleme ile 1-10 arası kontrol
        public int TehlikeSeviyesi
        {
            get => tehlikeSeviyesi;
            set
            {
                if (value >= 1 && value <= 10)
                    tehlikeSeviyesi = value;
                else
                    throw new ArgumentException("Tehlike seviyesi 1-10 arasında olmalıdır!");
            }
        }

        // ==================== A. SOYUT METOT ====================
        // AnalizEt(): Soyut (abstract) metot
        public abstract void AnalizEt();

        // ==================== A. NORMAL METOT ====================
        // DurumBilgisi(): Sanal (virtual) metot
        public virtual string DurumBilgisi()
        {
            return $"ID: {ID}, Stabilite: {Stabilite:F2}%";
        }

        // Constructor
        public KuantumNesnesi(string id, double stabilite, int tehlikeSeviyesi)
        {
            ID = id;
            TehlikeSeviyesi = tehlikeSeviyesi;
            Stabilite = stabilite;
        }
    }

    // ==================== C. NESNE ÇEŞİTLERİ (Inheritance & Polymorphism) ====================
    // 1. VeriPaketi: Sıradan, güvenli veri (IKritik DEĞİL)
    public class VeriPaketi : KuantumNesnesi
    {
        public VeriPaketi(string id) : base(id, 100, 1) { }

        // AnalizEt() override - Stabilite 5 birim düşer
        public override void AnalizEt()
        {
            Stabilite -= 5;
            Console.WriteLine("Veri içeriği okundu.");
        }
    }

    // 2. KaranlikMadde: Tehlikeli madde (IKritik uygular)
    public class KaranlikMadde : KuantumNesnesi, IKritik
    {
        public KaranlikMadde(string id) : base(id, 100, 8) { }

        // AnalizEt() override - Stabilite 15 birim düşer
        public override void AnalizEt()
        {
            Stabilite -= 15;
            Console.WriteLine("Karanlık Madde analiz ediliyor...");
        }

        // ==================== B. ARAYÜZ METODU ====================
        // AcilDurumSogutmasi(): Stabiliteyi +50 artırır
        public void AcilDurumSogutmasi()
        {
            Stabilite += 50;
            Console.WriteLine($"{ID} için acil soğutma yapıldı!");
        }
    }

    // 3. AntiMadde: Çok tehlikeli madde (IKritik uygular)
    public class AntiMadde : KuantumNesnesi, IKritik
    {
        public AntiMadde(string id) : base(id, 100, 10) { }

        // AnalizEt() override - Stabilite 25 birim düşer
        public override void AnalizEt()
        {
            Stabilite -= 25;
            Console.WriteLine("Evrenin dokusu titriyor... Anti Madde analiz ediliyor!");
        }

        // ==================== B. ARAYÜZ METODU ====================
        // AcilDurumSogutmasi(): Stabiliteyi +50 artırır
        public void AcilDurumSogutmasi()
        {
            Stabilite += 50;
            Console.WriteLine($"{ID} için acil soğutma yapıldı! Kritik durum atlatıldı.");
        }

        // ==================== C. POLYMORPHISM ====================
        // DurumBilgisi() override - Özel durum bilgisi
        public override string DurumBilgisi()
        {
            return $"ID: {ID}, Stabilite: {Stabilite:F2}% - ÇOK TEHLİKELİ!";
        }
    }

    class Program
    {
        // ==================== 1. GENERIC LIST ====================
        // List<KuantumNesnesi>: Nesneleri saklamak için generic list
        static List<KuantumNesnesi> envanter = new List<KuantumNesnesi>();
        static Random random = new Random();
        static int nesneSayaci = 1;

        static void Main(string[] args)
        {
            try
            {
                Console.Title = "Kuantum Kaos Yönetimi - Omega Sektörü";

                // ==================== 3. OYNANIŞ DÖNGÜSÜ (MAIN LOOP) ====================
                while (true)
                {
                    try
                    {
                        MenuGoster();
                        int secim = int.Parse(Console.ReadLine());

                        switch (secim)
                        {
                            case 1:
                                YeniNesneEkle();
                                break;
                            case 2:
                                EnvanteriListele();
                                break;
                            case 3:
                                NesneAnalizEt();
                                break;
                            case 4:
                                AcilSoğutmaYap();
                                break;
                            case 5:
                                Console.WriteLine("Sistem kapatılıyor...");
                                return;
                            default:
                                Console.WriteLine("Geçersiz seçim!");
                                break;
                        }
                    }
                    catch (FormatException)
                    {
                        Console.WriteLine("Lütfen sayı giriniz!");
                    }
                    catch (KuantumCokusuException ex)
                    {
                        throw ex; // Ana try-catch'e gönder
                    }
                    catch (Exception ex)
                    {
                        Console.WriteLine($"Hata: {ex.Message}");
                    }

                    Console.WriteLine("\nDevam etmek için bir tuşa basın...");
                    Console.ReadKey();
                    Console.Clear();
                }
            }
            // ==================== 4. GAME OVER ====================
            catch (KuantumCokusuException ex)
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine("\n\n=========================================");
                Console.WriteLine("  SİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR...");
                Console.WriteLine($"  Sebep: {ex.Message}");
                Console.WriteLine("=========================================");
                Console.ResetColor();
            }
        }

        static void MenuGoster()
        {
            Console.WriteLine("========================================");
            Console.WriteLine("   KUANTUM AMBARI KONTROL PANELİ");
            Console.WriteLine("========================================");
            Console.WriteLine("1. Yeni Nesne Ekle");
            Console.WriteLine("2. Tüm Envanteri Listele");
            Console.WriteLine("3. Nesneyi Analiz Et");
            Console.WriteLine("4. Acil Durum Soğutması Yap");
            Console.WriteLine("5. Çıkış");
            Console.WriteLine("========================================");
            Console.Write("Seçiminiz: ");
        }

        static void YeniNesneEkle()
        {
            Console.WriteLine("\nNesne Türü Seçin:");
            Console.WriteLine("1. Veri Paketi");
            Console.WriteLine("2. Karanlık Madde");
            Console.WriteLine("3. Anti Madde");
            Console.WriteLine("4. Rastgele Üret");
            Console.Write("Seçim: ");

            int secim = int.Parse(Console.ReadLine());
            string id = "OBJ-" + nesneSayaci.ToString("D3");
            nesneSayaci++;

            KuantumNesnesi yeniNesne;

            if (secim == 4)
            {
                secim = random.Next(1, 4);
            }

            switch (secim)
            {
                case 1:
                    yeniNesne = new VeriPaketi(id);
                    Console.WriteLine($"Yeni Veri Paketi eklendi: {id}");
                    break;
                case 2:
                    yeniNesne = new KaranlikMadde(id);
                    Console.WriteLine($"Yeni Karanlık Madde eklendi: {id}");
                    break;
                case 3:
                    yeniNesne = new AntiMadde(id);
                    Console.WriteLine($"Yeni Anti Madde eklendi: {id} - DİKKAT!");
                    break;
                default:
                    Console.WriteLine("Geçersiz seçim!");
                    return;
            }

            envanter.Add(yeniNesne);
            Console.WriteLine($"Toplam nesne sayısı: {envanter.Count}");
        }

        static void EnvanteriListele()
        {
            if (envanter.Count == 0)
            {
                Console.WriteLine("Envanter boş!");
                return;
            }

            Console.WriteLine("\n=== ENVANTER DURUM RAPORU ===");
            Console.WriteLine($"Toplam Nesne: {envanter.Count}");
            Console.WriteLine("----------------------------");

            // ==================== 2. POLIMORFİZM ====================
            // Listeleme yaparken hepsinin DurumBilgisi() metodunu çağırma
            for (int i = 0; i < envanter.Count; i++)
            {
                Console.WriteLine($"{i + 1}. {envanter[i].DurumBilgisi()}");
            }

            // Stabilite istatistikleri
            int kritikDurum = 0;
            int tehlikeliDurum = 0;

            foreach (var nesne in envanter)
            {
                if (nesne.Stabilite < 20) kritikDurum++;
                else if (nesne.Stabilite < 50) tehlikeliDurum++;
            }

            Console.WriteLine("----------------------------");
            Console.WriteLine($"Kritik Stabilite (<%20): {kritikDurum} nesne");
            Console.WriteLine($"Tehlikeli Stabilite (<%50): {tehlikeliDurum} nesne");
        }

        static void NesneAnalizEt()
        {
            if (envanter.Count == 0)
            {
                Console.WriteLine("Analiz edilecek nesne yok!");
                return;
            }

            Console.Write("Analiz edilecek nesnenin ID'sini girin: ");
            string arananID = Console.ReadLine();

            KuantumNesnesi nesne = envanter.Find(n => n.ID == arananID);

            if (nesne == null)
            {
                Console.WriteLine("Nesne bulunamadı!");
                return;
            }

            Console.WriteLine($"Analiz başlıyor: {nesne.DurumBilgisi()}");
            nesne.AnalizEt();
            Console.WriteLine($"Güncel durum: {nesne.DurumBilgisi()}");
        }

        static void AcilSoğutmaYap()
        {
            if (envanter.Count == 0)
            {
                Console.WriteLine("Soğutulacak nesne yok!");
                return;
            }

            Console.Write("Soğutulacak nesnenin ID'sini girin: ");
            string arananID = Console.ReadLine();

            KuantumNesnesi nesne = envanter.Find(n => n.ID == arananID);

            if (nesne == null)
            {
                Console.WriteLine("Nesne bulunamadı!");
                return;
            }

            // ==================== 3. TYPE CHECKING ====================
            // is anahtar kelimesi ile IKritik kontrolü
            if (nesne is IKritik kritikNesne)
            {
                kritikNesne.AcilDurumSogutmasi();
                Console.WriteLine($"Güncel durum: {nesne.DurumBilgisi()}");
            }
            else
            {
                Console.WriteLine("Bu nesne soğutulamaz! (Sadece kritik nesneler soğutulabilir)");
            }
        }
    }
}