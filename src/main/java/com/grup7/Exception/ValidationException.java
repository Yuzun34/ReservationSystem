package com.grup7.Exception;

// Veri doğrulama işlemleri sırasında oluşan hataları yönetmek için özel exception sınıfı.
// Örneğin: geçersiz tarih formatı, boş alan kontrolü gibi durumlarda kullanılır.
// RuntimeException'dan türetilmiştir, böylece try-catch zorunluluğu olmadan kullanılabilir.
public class ValidationException extends RuntimeException {
    // Doğrulama hatası mesajını üst sınıfa ileten constructor
    public ValidationException(String message) {
        super(message);
    }
}