// ==================== D. ÖZEL HATA YÖNETİMİ ====================
// KuantumCokusuException: Özel hata sınıfı
class KuantumCokusuException extends Error {
    constructor(message) {
        super(message);
        this.name = "KuantumCokusuException";
    }
}

// ==================== B. ARAYÜZ SEGREGATION ====================
// IKritik: Sadece tehlikeli nesneler için arayüz (JavaScript'te interface yok, bu yüzden soyut sınıf)
class IKritik {
    AcilDurumSogutmasi() {
        throw new Error("Bu metod alt sınıflarda tanımlanmalıdır!");
    }
}

// ==================== A. TEMEL YAPI (Abstract Class & Encapsulation) ====================
// KuantumNesnesi: Tüm nesnelerin atası olan soyut sınıf
class KuantumNesnesi {
    constructor(id, stabilite, tehlikeSeviyesi) {
        if (new.target === KuantumNesnesi) {
            throw new Error("KuantumNesnesi abstract sınıfından doğrudan nesne oluşturulamaz!");
        }
        
        // ÖZELLİKLER (Properties) - Kapsülleme (Encapsulation)
        this._id = id;
        this.tehlikeSeviyesi = tehlikeSeviyesi;
        this.stabilite = stabilite; // Setter üzerinden atama yap
    }

    // Getter ve Setter metotları - Kapsülleme
    get id() {
        return this._id;
    }

    set id(value) {
        this._id = value;
    }

    // Stabilite getter/setter - Kapsülleme ile 0-100 arası kontrol
    get stabilite() {
        return this._stabilite;
    }

    set stabilite(value) {
        // 100'den büyük girilmesini engelleme
        if (value > 100) {
            this._stabilite = 100;
        } 
        // 0'dan küçük olunca özel hata fırlatma
        else if (value < 0) {
            throw new KuantumCokusuException(`${this._id} kimlikli nesne patladı!`);
        } 
        else {
            this._stabilite = value;
        }
    }

    // TehlikeSeviyesi getter/setter - Kapsülleme ile 1-10 arası kontrol
    get tehlikeSeviyesi() {
        return this._tehlikeSeviyesi;
    }

    set tehlikeSeviyesi(value) {
        if (value >= 1 && value <= 10) {
            this._tehlikeSeviyesi = value;
        } else {
            throw new Error("Tehlike seviyesi 1-10 arasında olmalıdır!");
        }
    }

    // ==================== A. SOYUT METOT ====================
    // AnalizEt(): Soyut metot
    AnalizEt() {
        throw new Error("Bu metod alt sınıflarda tanımlanmalıdır!");
    }

    // ==================== A. NORMAL METOT ====================
    // DurumBilgisi(): Normal metot
    DurumBilgisi() {
        return `ID: ${this._id}, Stabilite: ${this._stabilite.toFixed(2)}%`;
    }
}

// ==================== C. NESNE ÇEŞİTLERİ (Inheritance & Polymorphism) ====================
// 1. VeriPaketi: Sıradan, güvenli veri (IKritik DEĞİL)
class VeriPaketi extends KuantumNesnesi {
    constructor(id) {
        super(id, 100, 1);
    }

    // AnalizEt() override - Stabilite 5 birim düşer
    AnalizEt() {
        this.stabilite -= 5;
        console.log("Veri içeriği okundu.");
    }
}

// 2. KaranlikMadde: Tehlikeli madde (IKritik uygular)
class KaranlikMadde extends KuantumNesnesi {
    constructor(id) {
        super(id, 100, 8);
    }

    // AnalizEt() override - Stabilite 15 birim düşer
    AnalizEt() {
        this.stabilite -= 15;
        console.log("Karanlık Madde analiz ediliyor...");
    }

    // ==================== B. ARAYÜZ METODU ====================
    // AcilDurumSogutmasi(): Stabiliteyi +50 artırır
    AcilDurumSogutmasi() {
        this.stabilite += 50;
        console.log(`${this.id} için acil soğutma yapıldı!`);
    }
}

// 3. AntiMadde: Çok tehlikeli madde (IKritik uygular)
class AntiMadde extends KuantumNesnesi {
    constructor(id) {
        super(id, 100, 10);
    }

    // AnalizEt() override - Stabilite 25 birim düşer
    AnalizEt() {
        this.stabilite -= 25;
        console.log("Evrenin dokusu titriyor... Anti Madde analiz ediliyor!");
    }

    // ==================== B. ARAYÜZ METODU ====================
    // AcilDurumSogutmasi(): Stabiliteyi +50 artırır
    AcilDurumSogutmasi() {
        this.stabilite += 50;
        console.log(`${this.id} için acil soğutma yapıldı! Kritik durum atlatıldı.`);
    }

    // ==================== C. POLYMORPHISM ====================
    // DurumBilgisi() override - Özel durum bilgisi
    DurumBilgisi() {
        return `ID: ${this.id}, Stabilite: ${this.stabilite.toFixed(2)}% - ÇOK TEHLİKELİ!`;
    }
}

// IKritik arayüzü kontrolü için yardımcı fonksiyon
function isIKritik(nesne) {
    return nesne && typeof nesne.AcilDurumSogutmasi === 'function';
}

// ==================== OYNANIŞ DÖNGÜSÜ (MAIN LOOP) ====================
class KuantumAmbari {
    constructor() {
        // ==================== 1. GENERIC LIST ====================
        // Array: Nesneleri saklamak için array
        this.envanter = [];
        this.nesneSayaci = 1;
        this.readline = require('readline').createInterface({
            input: process.stdin,
            output: process.stdout
        });
    }

    menuGoster() {
        console.log("=".repeat(40));
        console.log("   KUANTUM AMBARI KONTROL PANELİ");
        console.log("=".repeat(40));
        console.log("1. Yeni Nesne Ekle");
        console.log("2. Tüm Envanteri Listele");
        console.log("3. Nesneyi Analiz Et");
        console.log("4. Acil Durum Soğutması Yap");
        console.log("5. Çıkış");
        console.log("=".repeat(40));
    }

    async soruSor(soru) {
        return new Promise((resolve) => {
            this.readline.question(soru, resolve);
        });
    }

    async yeniNesneEkle() {
        console.log("\nNesne Türü Seçin:");
        console.log("1. Veri Paketi");
        console.log("2. Karanlık Madde");
        console.log("3. Anti Madde");
        console.log("4. Rastgele Üret");
        
        const secim = await this.soruSor("Seçim: ");
        const id = `OBJ-${this.nesneSayaci.toString().padStart(3, '0')}`;
        this.nesneSayaci++;

        let yeniNesne;
        let secimNum = parseInt(secim);

        if (secimNum === 4) {
            secimNum = Math.floor(Math.random() * 3) + 1;
        }

        switch (secimNum) {
            case 1:
                yeniNesne = new VeriPaketi(id);
                console.log(`Yeni Veri Paketi eklendi: ${id}`);
                break;
            case 2:
                yeniNesne = new KaranlikMadde(id);
                console.log(`Yeni Karanlık Madde eklendi: ${id}`);
                break;
            case 3:
                yeniNesne = new AntiMadde(id);
                console.log(`Yeni Anti Madde eklendi: ${id} - DİKKAT!`);
                break;
            default:
                console.log("Geçersiz seçim!");
                return;
        }

        this.envanter.push(yeniNesne);
        console.log(`Toplam nesne sayısı: ${this.envanter.length}`);
    }

    envanteriListele() {
        if (this.envanter.length === 0) {
            console.log("Envanter boş!");
            return;
        }

        console.log("\n=== ENVANTER DURUM RAPORU ===");
        console.log(`Toplam Nesne: ${this.envanter.length}`);
        console.log("-".repeat(30));

        // ==================== 2. POLIMORFİZM ====================
        // Listeleme yaparken hepsinin DurumBilgisi() metodunu çağırma
        this.envanter.forEach((nesne, index) => {
            console.log(`${index + 1}. ${nesne.DurumBilgisi()}`);
        });

        // Stabilite istatistikleri
        const kritikDurum = this.envanter.filter(n => n.stabilite < 20).length;
        const tehlikeliDurum = this.envanter.filter(n => n.stabilite >= 20 && n.stabilite < 50).length;

        console.log("-".repeat(30));
        console.log(`Kritik Stabilite (<%20): ${kritikDurum} nesne`);
        console.log(`Tehlikeli Stabilite (<%50): ${tehlikeliDurum} nesne`);
    }

    async nesneAnalizEt() {
        if (this.envanter.length === 0) {
            console.log("Analiz edilecek nesne yok!");
            return;
        }

        const arananID = await this.soruSor("Analiz edilecek nesnenin ID'sini girin: ");
        const nesne = this.envanter.find(n => n.id === arananID);

        if (!nesne) {
            console.log("Nesne bulunamadı!");
            return;
        }

        console.log(`Analiz başlıyor: ${nesne.DurumBilgisi()}`);
        nesne.AnalizEt();
        console.log(`Güncel durum: ${nesne.DurumBilgisi()}`);
    }

    async acilSogutmaYap() {
        if (this.envanter.length === 0) {
            console.log("Soğutulacak nesne yok!");
            return;
        }

        const arananID = await this.soruSor("Soğutulacak nesnenin ID'sini girin: ");
        const nesne = this.envanter.find(n => n.id === arananID);

        if (!nesne) {
            console.log("Nesne bulunamadı!");
            return;
        }

        // ==================== 3. TYPE CHECKING ====================
        // isIKritik fonksiyonu ile IKritik kontrolü
        if (isIKritik(nesne)) {
            nesne.AcilDurumSogutmasi();
            console.log(`Güncel durum: ${nesne.DurumBilgisi()}`);
        } else {
            console.log("Bu nesne soğutulamaz! (Sadece kritik nesneler soğutulabilir)");
        }
    }

    async bekle() {
        await this.soruSor("\nDevam etmek için Enter tuşuna basın...");
        console.clear();
    }

    async calistir() {
        try {
            console.log("KUANTUM KAOS YÖNETİMİ - OMEGA SEKTÖRÜ\n");

            // ==================== 3. OYNANIŞ DÖNGÜSÜ (MAIN LOOP) ====================
            while (true) {
                try {
                    this.menuGoster();
                    const secim = await this.soruSor("Seçiminiz: ");

                    switch (secim) {
                        case '1':
                            await this.yeniNesneEkle();
                            break;
                        case '2':
                            this.envanteriListele();
                            break;
                        case '3':
                            await this.nesneAnalizEt();
                            break;
                        case '4':
                            await this.acilSogutmaYap();
                            break;
                        case '5':
                            console.log("Sistem kapatılıyor...");
                            this.readline.close();
                            return;
                        default:
                            console.log("Geçersiz seçim!");
                    }
                } catch (error) {
                    if (error instanceof KuantumCokusuException) {
                        throw error;
                    } else if (error.name === 'Error') {
                        console.log(`Hata: ${error.message}`);
                    } else {
                        console.log(`Beklenmeyen hata: ${error}`);
                    }
                }

                await this.bekle();
            }
        } 
        // ==================== 4. GAME OVER ====================
        catch (error) {
            if (error instanceof KuantumCokusuException) {
                console.log("\n\n" + "=".repeat(40));
                console.log("  SİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR...");
                console.log(`  Sebep: ${error.message}`);
                console.log("=".repeat(40));
            }
            this.readline.close();
        }
    }
}

// ==================== PROGRAM BAŞLANGICI ====================
if (require.main === module) {
    const ambari = new KuantumAmbari();
    ambari.calistir();
}