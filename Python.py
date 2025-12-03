import random
import os

# ==================== D. ÖZEL HATA YÖNETİMİ ====================
# KuantumCokusuException: Özel hata sınıfı
class KuantumCokusuException(Exception):
    def __init__(self, message):
        super().__init__(message)

# ==================== B. ARAYÜZ SEGREGATION ====================
# IKritik: Sadece tehlikeli nesneler için arayüz
class IKritik:
    def AcilDurumSogutmasi(self):
        raise NotImplementedError("Bu metod alt sınıflarda tanımlanmalıdır!")

# ==================== A. TEMEL YAPI (Abstract Class & Encapsulation) ====================
# KuantumNesnesi: Tüm nesnelerin atası olan soyut sınıf
class KuantumNesnesi:
    def __init__(self, id, stabilite, tehlike_seviyesi):
        self._id = id
        self._tehlike_seviyesi = tehlike_seviyesi
        self.stabilite = stabilite  # Property üzerinden atama yap
    
    # ÖZELLİKLER (Properties) - Kapsülleme (Encapsulation)
    @property
    def id(self):
        return self._id
    
    @id.setter
    def id(self, value):
        self._id = value
    
    # Stabilite property - Kapsülleme ile 0-100 arası kontrol
    @property
    def stabilite(self):
        return self._stabilite
    
    @stabilite.setter
    def stabilite(self, value):
        # 100'den büyük girilmesini engelleme
        if value > 100:
            self._stabilite = 100
        # 0'dan küçük olunca özel hata fırlatma
        elif value < 0:
            raise KuantumCokusuException(f"{self._id} kimlikli nesne patladı!")
        else:
            self._stabilite = value
    
    # TehlikeSeviyesi property - Kapsülleme ile 1-10 arası kontrol
    @property
    def tehlike_seviyesi(self):
        return self._tehlike_seviyesi
    
    @tehlike_seviyesi.setter
    def tehlike_seviyesi(self, value):
        if 1 <= value <= 10:
            self._tehlike_seviyesi = value
        else:
            raise ValueError("Tehlike seviyesi 1-10 arasında olmalıdır!")
    
    # ==================== A. SOYUT METOT ====================
    # AnalizEt(): Soyut metot
    def AnalizEt(self):
        raise NotImplementedError("Bu metod alt sınıflarda tanımlanmalıdır!")
    
    # ==================== A. NORMAL METOT ====================
    # DurumBilgisi(): Normal metot
    def DurumBilgisi(self):
        return f"ID: {self._id}, Stabilite: {self._stabilite:.2f}%"

# ==================== C. NESNE ÇEŞİTLERİ (Inheritance & Polymorphism) ====================
# 1. VeriPaketi: Sıradan, güvenli veri (IKritik DEĞİL)
class VeriPaketi(KuantumNesnesi):
    def __init__(self, id):
        super().__init__(id, 100, 1)
    
    # AnalizEt() override - Stabilite 5 birim düşer
    def AnalizEt(self):
        self.stabilite -= 5
        print("Veri içeriği okundu.")

# 2. KaranlikMadde: Tehlikeli madde (IKritik uygular)
class KaranlikMadde(KuantumNesnesi, IKritik):
    def __init__(self, id):
        super().__init__(id, 100, 8)
    
    # AnalizEt() override - Stabilite 15 birim düşer
    def AnalizEt(self):
        self.stabilite -= 15
        print("Karanlık Madde analiz ediliyor...")
    
    # ==================== B. ARAYÜZ METODU ====================
    # AcilDurumSogutmasi(): Stabiliteyi +50 artırır
    def AcilDurumSogutmasi(self):
        self.stabilite += 50
        print(f"{self.id} için acil soğutma yapıldı!")

# 3. AntiMadde: Çok tehlikeli madde (IKritik uygular)
class AntiMadde(KuantumNesnesi, IKritik):
    def __init__(self, id):
        super().__init__(id, 100, 10)
    
    # AnalizEt() override - Stabilite 25 birim düşer
    def AnalizEt(self):
        self.stabilite -= 25
        print("Evrenin dokusu titriyor... Anti Madde analiz ediliyor!")
    
    # ==================== B. ARAYÜZ METODU ====================
    # AcilDurumSogutmasi(): Stabiliteyi +50 artırır
    def AcilDurumSogutmasi(self):
        self.stabilite += 50
        print(f"{self.id} için acil soğutma yapıldı! Kritik durum atlatıldı.")
    
    # ==================== C. POLYMORPHISM ====================
    # DurumBilgisi() override - Özel durum bilgisi
    def DurumBilgisi(self):
        return f"ID: {self.id}, Stabilite: {self.stabilite:.2f}% - ÇOK TEHLİKELİ!"

# ==================== OYNANIŞ DÖNGÜSÜ (MAIN LOOP) ====================
class KuantumAmbari:
    def __init__(self):
        # ==================== 1. GENERIC LIST ====================
        # List: Nesneleri saklamak için list
        self.envanter = []
        self.nesne_sayaci = 1
    
    def menu_goster(self):
        print("=" * 40)
        print("   KUANTUM AMBARI KONTROL PANELİ")
        print("=" * 40)
        print("1. Yeni Nesne Ekle")
        print("2. Tüm Envanteri Listele")
        print("3. Nesneyi Analiz Et")
        print("4. Acil Durum Soğutması Yap")
        print("5. Çıkış")
        print("=" * 40)
    
    def yeni_nesne_ekle(self):
        print("\nNesne Türü Seçin:")
        print("1. Veri Paketi")
        print("2. Karanlık Madde")
        print("3. Anti Madde")
        print("4. Rastgele Üret")
        
        try:
            secim = int(input("Seçim: "))
        except ValueError:
            print("Geçersiz giriş!")
            return
        
        id = f"OBJ-{self.nesne_sayaci:03d}"
        self.nesne_sayaci += 1
        
        if secim == 4:
            secim = random.randint(1, 3)
        
        if secim == 1:
            yeni_nesne = VeriPaketi(id)
            print(f"Yeni Veri Paketi eklendi: {id}")
        elif secim == 2:
            yeni_nesne = KaranlikMadde(id)
            print(f"Yeni Karanlık Madde eklendi: {id}")
        elif secim == 3:
            yeni_nesne = AntiMadde(id)
            print(f"Yeni Anti Madde eklendi: {id} - DİKKAT!")
        else:
            print("Geçersiz seçim!")
            return
        
        self.envanter.append(yeni_nesne)
        print(f"Toplam nesne sayısı: {len(self.envanter)}")
    
    def envanteri_listele(self):
        if not self.envanter:
            print("Envanter boş!")
            return
        
        print("\n=== ENVANTER DURUM RAPORU ===")
        print(f"Toplam Nesne: {len(self.envanter)}")
        print("-" * 30)
        
        # ==================== 2. POLIMORFİZM ====================
        # Listeleme yaparken hepsinin DurumBilgisi() metodunu çağırma
        for i, nesne in enumerate(self.envanter, 1):
            print(f"{i}. {nesne.DurumBilgisi()}")
        
        # Stabilite istatistikleri
        kritik_durum = sum(1 for n in self.envanter if n.stabilite < 20)
        tehlikeli_durum = sum(1 for n in self.envanter if 20 <= n.stabilite < 50)
        
        print("-" * 30)
        print(f"Kritik Stabilite (<%20): {kritik_durum} nesne")
        print(f"Tehlikeli Stabilite (<%50): {tehlikeli_durum} nesne")
    
    def nesne_analiz_et(self):
        if not self.envanter:
            print("Analiz edilecek nesne yok!")
            return
        
        aranan_id = input("Analiz edilecek nesnenin ID'sini girin: ")
        
        nesne = None
        for n in self.envanter:
            if n.id == aranan_id:
                nesne = n
                break
        
        if not nesne:
            print("Nesne bulunamadı!")
            return
        
        print(f"Analiz başlıyor: {nesne.DurumBilgisi()}")
        nesne.AnalizEt()
        print(f"Güncel durum: {nesne.DurumBilgisi()}")
    
    def acil_sogutma_yap(self):
        if not self.envanter:
            print("Soğutulacak nesne yok!")
            return
        
        aranan_id = input("Soğutulacak nesnenin ID'sini girin: ")
        
        nesne = None
        for n in self.envanter:
            if n.id == aranan_id:
                nesne = n
                break
        
        if not nesne:
            print("Nesne bulunamadı!")
            return
        
        # ==================== 3. TYPE CHECKING ====================
        # isinstance() fonksiyonu ile IKritik kontrolü
        if isinstance(nesne, IKritik):
            nesne.AcilDurumSogutmasi()
            print(f"Güncel durum: {nesne.DurumBilgisi()}")
        else:
            print("Bu nesne soğutulamaz! (Sadece kritik nesneler soğutulabilir)")
    
    def calistir(self):
        try:
            print("KUANTUM KAOS YÖNETİMİ - OMEGA SEKTÖRÜ\n")
            
            # ==================== 3. OYNANIŞ DÖNGÜSÜ (MAIN LOOP) ====================
            while True:
                try:
                    self.menu_goster()
                    secim = input("Seçiminiz: ")
                    
                    if secim == '1':
                        self.yeni_nesne_ekle()
                    elif secim == '2':
                        self.envanteri_listele()
                    elif secim == '3':
                        self.nesne_analiz_et()
                    elif secim == '4':
                        self.acil_sogutma_yap()
                    elif secim == '5':
                        print("Sistem kapatılıyor...")
                        break
                    else:
                        print("Geçersiz seçim!")
                
                except ValueError as e:
                    print(f"Hata: {e}")
                except KuantumCokusuException as e:
                    raise e
                
                input("\nDevam etmek için Enter tuşuna basın...")
                os.system('cls' if os.name == 'nt' else 'clear')
        
        # ==================== 4. GAME OVER ====================
        except KuantumCokusuException as e:
            print("\n\n" + "=" * 40)
            print("  SİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR...")
            print(f"  Sebep: {e}")
            print("=" * 40)

# ==================== PROGRAM BAŞLANGICI ====================
if __name__ == "__main__":
    ambari = KuantumAmbari()
    ambari.calistir()