# KuantumKaosYonetimi
Kuantum Kaos Yönetimi - Omega Sektörü Kuantum Veri Ambarı Simülasyonu 
# 🌌 Kuantum Kaos Yönetimi - Omega Sektörü

<div align="center">

![Kuantum Simülasyon](https://img.shields.io/badge/Status-Active-success)
![OOP](https://img.shields.io/badge/OOP-4%20Languages-blue)
![License](https://img.shields.io/badge/License-MIT-green)
![Version](https://img.shields.io/badge/Version-1.0.0-orange)

**"Stabiliteyi koru, evreni kurtar!"** ⚛️

[🚀 Projeyi İncele](#-proje-hakkında) • [🎮 Oynanış](#-oynanış) • [💻 Teknik Detaylar](#-teknik-detaylar) • [📁 Kurulum](#-kurulum) • [👨‍💻 Geliştirici](#-geliştirici)

</div>

## 🚀 Proje Hakkında

Tebrikler! **"Omega Sektörü"ndeki Kuantum Veri Ambarı'nın yeni vardiya amirisiniz.** Bu ambar, evrenin en kararsız ve tehlikeli maddelerini dijital ortamda saklar. Göreviniz basit ama stresli: Depoya gelen maddeleri kabul etmek, onları analiz etmek ve patlamadan gün sonunu getirmek.

> ⚠️ **UYARI:** Maddelerin "Stabilite" seviyesi her geçen dakika düşmektedir. Eğer bir madde %0 stabilitenin altına düşerse, **Kuantum Çöküşü (Quantum Collapse)** gerçekleşir ve simülasyon biter!

## 🎯 Özellikler

- ✅ **4 Farklı Programlama Dili**: C#, Java, Python, JavaScript
- ✅ **Tam OOP Uygulaması**: Tüm OOP prensipleri uygulandı
- ✅ **Gerçek Zamanlı Simülasyon**: Dinamik stabilite yönetimi
- ✅ **Özel Hata Yönetimi**: Kuantum çöküşü exception'ları
- ✅ **Cross-Platform**: Tüm işletim sistemlerinde çalışır

## 🎮 Oynanış


### 🧪 Nesne Tipleri:
| Nesne | Tehlike | Stabilite Kaybı | Soğutma | Özel Mesaj |
|-------|---------|----------------|---------|------------|
| **Veri Paketi** | 🔵 Düşük | -5% | ❌ Yok | "Veri içeriği okundu." |
| **Karanlık Madde** | 🟠 Yüksek | -15% | ✅ +50% | "Karanlık Madde analiz ediliyor..." |
| **Anti Madde** | 🔴 ÇOK YÜKSEK | -25% | ✅ +50% | "Evrenin dokusu titriyor..." |

## 💻 Teknik Detaylar

### 🏗️ Mimarı
Bu proje, **4 farklı programlama dilinde** aynı OOP prensiplerini uygular:

| Dil | Dosya | Versiyon | Çalıştırma Komutu |
|-----|-------|----------|-------------------|
| **C#** | `CSharp/Program.cs` | .NET 6.0+ | `dotnet run` |
| **Java** | `Java/KuantumAmbari.java` | JDK 8+ | `java KuantumAmbari` |
| **Python** | `Python/kuantum_ambari.py` | Python 3.8+ | `python kuantum_ambari.py` |
| **JavaScript** | `JavaScript/kuantum_ambari.js` | Node.js 14+ | `node kuantum_ambari.js` |

### 🎯 Uygulanan OOP Prensipleri:

#### 1. **Abstract Class & Encapsulation**
```csharp
public abstract class KuantumNesnesi
{
    private double stabilite;
    public double Stabilite
    {
        get => stabilite;
        set
        {
            if (value > 100) stabilite = 100;
            else if (value < 0) throw new KuantumCokusuException();
            else stabilite = value;
        }
    }
}
