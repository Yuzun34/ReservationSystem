package com.grup7.Exception;

// Restoran yönetim sisteminde masa bulunamadığı durumları işlemek için özel exception sınıfı.
// Örnek kullanım: Masa ID'si ile sorgu yapıldığında ilgili masa bulunamadığında fırlatılır.
// RuntimeException sınıfından türetilmiştir, bu sayede try-catch bloğu olmadan kullanılabilir.
public class TableNotFoundException extends RuntimeException {
    // Exception oluşturulduğunda kullanıcıya gösterilecek hata mesajını alan constructor
    public TableNotFoundException(String message) {
        super(message);
    }
}