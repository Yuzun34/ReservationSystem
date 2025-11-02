# 🍽️ Rezervasyon Sistemi

Modern ve kullanıcı dostu bir restoran rezervasyon yönetim sistemi. Spring Boot ve Java Swing kullanılarak geliştirilmiş, kullanıcıların kolayca rezervasyon yapmasına ve sipariş vermesine olanak sağlayan kapsamlı bir uygulamadır.

## 📋 İçindekiler

- [Özellikler](#-özellikler)
- [Teknolojiler](#-teknolojiler)
- [Kurulum](#-kurulum)
- [Kullanım](#-kullanım)
- [API Dokümantasyonu](#-api-dokümantasyonu)
- [Proje Yapısı](#-proje-yapısı)
- [GUI Kullanımı](#-gui-kullanımı)
- [Test Senaryoları](#-test-senaryoları)
- [Katkıda Bulunma](#-katkıda-bulunma)
- [Lisans](#-lisans)

## ✨ Özellikler

### Temel Özellikler
- ✅ **Kullanıcı Yönetimi**: Kullanıcı ekleme, listeleme, güncelleme ve silme işlemleri
- ✅ **Masa Yönetimi**: Masaların durumunu görüntüleme ve yönetme
- ✅ **Rezervasyon Sistemi**: Otomatik rezervasyon kodu oluşturma ve yönetimi
- ✅ **Sipariş Yönetimi**: Sipariş oluşturma, listeleme ve takip etme
- ✅ **Menü Entegrasyonu**: Harici API'den (themealdb.com) menü kategorilerini çekme
- ✅ **Raporlama**: Aylık doluluk raporları (PDF/Excel formatında)
- ✅ **E-posta Bildirimleri**: Sistem loglarını e-posta ile gönderme
- ✅ **Modern GUI**: Java Swing ile oluşturulmuş gradyan renkli ve kullanıcı dostu arayüz

### GUI Özellikleri
- 🎨 Modern gradyan arka plan tasarımı
- 📱 Responsive ve kullanıcı dostu arayüz
- 🎯 Sezgisel navigasyon
- 💾 Rezervasyon kodunu panoya kopyalama
- 🔄 Otomatik form temizleme
- ✅ Anlık durum bildirimleri

## 🛠️ Teknolojiler

### Backend
- **Java**: 17
- **Spring Boot**: 3.4.5
- **Spring Data JPA**: Veritabanı işlemleri için
- **Spring MVC**: REST API için
- **Lombok**: Kod tekrarını azaltmak için
- **H2 Database**: Geliştirme ortamı için yerel veritabanı
- **PostgreSQL**: Production veritabanı desteği

### Frontend
- **Java Swing**: Grafiksel kullanıcı arayüzü için
- **Graphics2D**: Modern gradyan arka planlar için

### Diğer Kütüphaneler
- **iText**: PDF raporları oluşturmak için (v5.5.13.3)
- **Apache POI**: Excel raporları oluşturmak için (v5.2.5)
- **SpringDoc OpenAPI**: API dokümantasyonu için (v2.3.0)
- **Spring Boot Mail**: E-posta göndermek için

### Test
- **JUnit 5**: Birim testleri (v5.10.2)
- **Mockito**: Test dublörü (mocking) kütüphanesi (v5.2.0)
- **AssertJ**: Test assertion'ları için (v3.24.2)

## 📦 Kurulum

### Gereksinimler
- Java 17 veya üzeri
- Maven 3.6+ 
- (Opsiyonel) PostgreSQL (production için)

### Adımlar

1. **Projeyi klonlayın**
```bash
git clone https://github.com/kullaniciadi/ReservationSystem.git
cd ReservationSystem
```

2. **Maven bağımlılıklarını yükleyin**
```bash
mvn clean install
```

3. **Uygulamayı çalıştırın**
```bash
mvn spring-boot:run
```

Veya IDE'nizde `ReservationSystemApplication` sınıfını çalıştırın.

4. **Uygulamaya erişim**
- Backend API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- GUI otomatik olarak açılacaktır

### Yapılandırma

`src/main/resources/application.properties` dosyasında ayarları yapılandırabilirsiniz:

```properties
# Sunucu ayarları
server.port=8080

# Veritabanı ayarları (H2 - geliştirme için)
spring.datasource.url=jdbc:h2:mem:reservationdb
spring.datasource.driverClassName=org.h2.Driver
spring.h2.console.enabled=true

# E-posta ayarları
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Loglama
logging.file.name=reservation-system.log
```

## 🚀 Kullanım

### GUI Kullanımı

1. **Rezervasyon Oluşturma**
   - Uygulama başladığında otomatik olarak rezervasyon ekranı açılır
   - İsim, soyisim ve tarih bilgilerini girin
   - "✅ Rezervasyon Yap" butonuna tıklayın
   - Rezervasyon kodunuz otomatik olarak oluşturulur
   - "📋 Kopyala" butonu ile kodu panoya kopyalayın

2. **Sipariş Oluşturma**
   - Rezervasyon kodunu kopyaladıktan sonra sipariş ekranı otomatik açılır
   - Veya manuel olarak rezervasyon kodunu girin
   - Mevcut kategorilerden seçim yapın
   - "➡️ Ekle" butonu ile kategorileri seçilenler listesine ekleyin
   - "✅ Siparişi Oluştur" butonuna tıklayın

### API Kullanımı

#### Kullanıcı İşlemleri

**Yeni Kullanıcı Ekleme**
```bash
POST /rest/api/users/add
Content-Type: application/json

{
  "name": "Ahmet",
  "surname": "Yılmaz",
  "date": "15/01/2025"
}
```

**Tüm Kullanıcıları Listeleme**
```bash
GET /rest/api/users/list
```

**Kullanıcı Güncelleme**
```bash
PUT /rest/api/users/update/{id}
Content-Type: application/json

{
  "name": "Mehmet",
  "surname": "Demir",
  "date": "20/01/2025"
}
```

**Kullanıcı Silme**
```bash
DELETE /rest/api/users/delete/{id}
```

#### Kategori İşlemleri

**Menü Kategorilerini Getirme**
```bash
GET /rest/api/menu
```

#### Sipariş İşlemleri

**Sipariş Oluşturma**
```bash
POST /rest/api/orders/save
Content-Type: application/json

{
  "reservationCode": "RES-2025-001",
  "categoryIds": ["1", "2", "3"]
}
```

**Rezervasyon Koduna Göre Sipariş Getirme**
```bash
GET /rest/api/orders/{reservationCode}
```

**Siparişi Kapatma**
```bash
POST /rest/api/orders/close/{id}
```

#### Masa İşlemleri

**Tüm Masaları Listeleme**
```bash
GET /rest/api/tables
```

#### Rapor İşlemleri

**Aylık Doluluk Raporu**
```bash
GET /rest/api/reports/{year}/{month}
```

#### E-posta İşlemleri

**Log Dosyasını E-posta ile Gönderme**
```bash
POST /rest/api/send-email
Content-Type: application/json

{
  "to": "destinasyon@example.com",
  "subject": "Rezervasyon Sistemi Logları"
}
```

## 📚 API Dokümantasyonu

Detaylı API dokümantasyonu için Swagger UI'ı kullanabilirsiniz:
- URL: `http://localhost:8080/swagger-ui.html`

## 📁 Proje Yapısı

```
ReservationSystem/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/grup7/
│   │   │       ├── Config/          # Yapılandırma sınıfları
│   │   │       ├── Controller/     # REST API endpoint'leri
│   │   │       ├── Dto/             # Veri transfer nesneleri
│   │   │       ├── Entity/          # JPA varlıkları
│   │   │       ├── Exception/       # Özel istisna sınıfları
│   │   │       ├── GUI/             # Kullanıcı arayüzü bileşenleri
│   │   │       ├── Repository/      # Veritabanı repository'leri
│   │   │       ├── Service/         # İş mantığı servisleri
│   │   │       ├── Util/            # Yardımcı sınıflar
│   │   │       └── ReservationSystem/
│   │   │           └── ReservationSystemApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   └── test/
│       └── java/
│           └── com/grup7/
│               └── Service/          # Test sınıfları
├── pom.xml                          # Maven yapılandırması
├── README.md                        # Bu dosya
└── .gitignore
```

## 🖥️ GUI Kullanımı

### Rezervasyon Ekranı
1. İsim ve soyisim bilgilerinizi girin
2. Rezervasyon tarihini seçin (Gün/Ay/Yıl)
3. "✅ Rezervasyon Yap" butonuna tıklayın
4. Oluşturulan rezervasyon kodunuz ekranda görünecektir
5. "📋 Kopyala" butonu ile kodu panoya kopyalayın

### Sipariş Ekranı
1. Rezervasyon kodunuzu girin (otomatik yapıştırılabilir)
2. Sol listeden menü kategorilerini seçin
3. "➡️ Ekle" butonu ile kategorileri seçilenler listesine ekleyin
4. "⬅️ Çıkar" butonu ile kategorileri listeden çıkarabilirsiniz
5. "✅ Siparişi Oluştur" butonu ile siparişinizi tamamlayın

## 🧪 Test Senaryoları

Proje kapsamlı birim testleri içermektedir. Testleri çalıştırmak için:

```bash
mvn test
```

### UserService Test Senaryoları
- ✅ Geçerli bilgilerle kullanıcı ekleme
- ❌ Boş isimle kullanıcı ekleme girişimi
- ❌ Geçmiş tarihle kullanıcı ekleme girişimi
- ❌ Uygun masa olmadan kullanıcı ekleme girişimi

### ExternalMenuService Test Senaryoları
- ✅ Dış API'den başarılı şekilde kategori verilerinin çekilmesi
- ⚠️ Dış API'den boş kategori listesi dönmesi
- ⚠️ Dış API'den null yanıt dönmesi
- ❌ Dış API'den istisna (exception) fırlatılması

## 🔧 Geliştirme

### Projeyi Geliştirme Ortamında Çalıştırma

1. IDE'nizde projeyi açın (IntelliJ IDEA, Eclipse, VS Code vb.)
2. Maven bağımlılıklarının yüklendiğinden emin olun
3. `ReservationSystemApplication` sınıfını çalıştırın
4. GUI otomatik olarak açılacaktır

### Yeni Özellik Ekleme

1. Yeni bir branch oluşturun: `git checkout -b feature/yeni-ozellik`
2. Değişikliklerinizi yapın
3. Testleri yazın ve çalıştırın
4. Commit yapın: `git commit -m "Yeni özellik eklendi"`
5. Branch'i push edin: `git push origin feature/yeni-ozellik`
6. Pull Request oluşturun

## 🤝 Katkıda Bulunma

Katkılarınızı bekliyoruz! Lütfen şu adımları izleyin:

1. Fork yapın
2. Feature branch oluşturun (`git checkout -b feature/AmazingFeature`)
3. Değişikliklerinizi commit edin (`git commit -m 'Add some AmazingFeature'`)
4. Branch'inizi push edin (`git push origin feature/AmazingFeature`)
5. Pull Request açın

### Katkı Kuralları
- Kod standartlarına uyun
- Unit testler yazın
- README.md'yi güncelleyin
- Açıklayıcı commit mesajları kullanın

## 📝 Changelog

### v0.0.1-SNAPSHOT
- ✨ İlk sürüm
- ✅ Kullanıcı yönetimi
- ✅ Rezervasyon sistemi
- ✅ Sipariş yönetimi
- ✅ Modern GUI arayüzü
- ✅ REST API endpoint'leri
- ✅ Raporlama özellikleri
- ✅ E-posta bildirimleri

## 📄 Lisans

Bu proje açık kaynak kodludur ve MIT lisansı altında lisanslanmıştır.

## 👥 Ekip

Grup 7 tarafından geliştirilmiştir.

## 📧 İletişim

Sorularınız için lütfen issue açın veya pull request gönderin.

## 🙏 Teşekkürler

- [Spring Boot](https://spring.io/projects/spring-boot)
- [TheMealDB API](https://www.themealdb.com/)
- Tüm açık kaynak kütüphane geliştiricilerine

---

⭐ Bu projeyi beğendiyseniz yıldız vermeyi unutmayın!
