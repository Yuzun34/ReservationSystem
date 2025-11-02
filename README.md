# Rezervasyon Sistemi

Bu README dosyası, Grup 7 tarafından geliştirilen Rezervasyon Sistemi projesinin genel yapısını, kurulumunu ve kullanımını açıklamaktadır.

## Proje Açıklaması

Rezervasyon Sistemi, restoran ve kafelerin rezervasyon yönetimini sağlayan bir uygulamadır. Sistem, masaların rezervasyonunu yönetmek, siparişleri takip etmek, menü kategorilerini dış API'den çekmek, raporlama yapmak ve e-posta göndermek gibi çeşitli işlevlere sahiptir.

## Özellikler

- **Kullanıcı Yönetimi**: Kullanıcı ekleme, listeleme, güncelleme ve silme işlemleri
- **Masa Yönetimi**: Masaların durumunu görüntüleme
- **Menü Entegrasyonu**: Harici bir API'den (themealdb.com) menü kategorilerini çekme
- **Sipariş İşlemleri**: Sipariş oluşturma ve listeleme
- **Raporlama**: Aylık Doluluk Raporları
- **E-posta Bildirimleri**: Sistem loglarını e-posta ile gönderme
- **Kullanıcı Arayüzü**: Java Swing ile oluşturulmuş grafiksel kullanıcı arayüzü

## Bağımlılıklar

- **Java**: 17
- **Spring Boot**: 3.4.5
- **Spring Data JPA**: Veritabanı işlemleri için
- **Spring MVC**: REST API için
- **Lombok**: Kod tekrarını azaltmak için
- **H2 Database**: Geliştirme ortamı için yerel veritabanı
- **JUnit 5**: Birim testleri
- **Mockito**: Test dublörü (mocking) kütüphanesi
- **Swing**: Grafiksel kullanıcı arayüzü için
- **iText**: PDF raporları oluşturmak için
- **Apache POI**: Excel raporları oluşturmak için
- **SpringDoc OpenAPI**: API dokümantasyonu için

## Proje Yapısı

Proje aşağıdaki ana paketlerden oluşmaktadır:

- **Controller**: REST API endpoint'lerini içeren sınıflar
- **Service**: İş mantığını içeren servis sınıfları
- **Repository**: Veritabanı erişimi için repository arayüzleri
- **Entity**: Veritabanı tablolarını temsil eden JPA varlıkları
- **DTO**: Veri transfer nesneleri
- **Exception**: Özel istisna sınıfları
- **GUI**: Kullanıcı arayüzü bileşenleri
- **Util**: Yardımcı sınıflar ve metotlar
- **Config**: Yapılandırma sınıfları

# API Kullanımı

Sistem, REST API üzerinden dış entegrasyonlara olanak sağlamaktadır. Tüm API endpointleri aşağıdaki gruplar altında toplanmıştır:

### Kullanıcı İşlemleri
- `POST /rest/api/users/add`: Yeni bir kullanıcı ekler
- `GET /rest/api/users/list`: Tüm kullanıcıları listeler
- `PUT /rest/api/users/update/{id}`: Belirtilen ID'ye sahip kullanıcıyı günceller
- `DELETE /rest/api/users/delete/{id}`: Belirtilen ID'ye sahip kullanıcıyı siler

### Kategori İşlemleri
- `GET /rest/api/menu`: Tüm menü kategorilerini getirir

### E-posta İşlemleri
- `POST /rest/api/send-email`: Log dosyasını belirtilen e-posta adresine gönderir

### Sipariş İşlemleri
- `POST /rest/api/orders/add`: Yeni bir sipariş oluşturur
- `GET /rest/api/orders/{Reservationİd}`: İd ile eşleşen Siparişi listeler

### Sipariş Log İşlemleri
- `GET /rest/api/orders/close/{id}`: Belirtilen İd ile eşleşen kullanıcıyı loglar

### Masa İşlemleri
- `GET /rest/api/tables`: Tüm masaları listeler

### Rapor İşlemleri
- `GET /rest/api/reports/year/month`: Tüm masaları listeler

## Grafiksel Kullanıcı Arayüzü (GUI)

Sistem ayrıca Java Swing ile oluşturulmuş bir grafiksel kullanıcı arayüzüne sahiptir. Bu arayüz, kullanıcıların rezervasyon oluşturması, siparişleri yönetmesi ve raporları görüntülemesi için kullanılabilir.

GUI, aşağıdaki ana ekranlardan oluşur:
- Kullanıcı Kaydı Ekranı
- Sipariş Yönetimi Ekranı

## Test Senaryoları

Sistem, JUnit 5 ve Mockito kullanılarak kapsamlı birim testleriyle test edilmiştir. Özellikle `UserService` ve `ExternalMenuService` sınıfları için detaylı test senaryoları bulunmaktadır.

### UserService Test Senaryoları
- Geçerli bilgilerle kullanıcı ekleme
- Boş isimle kullanıcı ekleme girişimi
- Geçmiş tarihle kullanıcı ekleme girişimi
- Uygun masa olmadan kullanıcı ekleme girişimi

### ExternalMenuService Test Senaryoları
- Dış API'den başarılı şekilde kategori verilerinin çekilmesi
- Dış API'den boş kategori listesi dönmesi
- Dış API'den null yanıt dönmesi
- Dış API'den istisna (exception) fırlatılması

