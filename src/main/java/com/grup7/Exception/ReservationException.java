package com.grup7.Exception;

// Rezervasyon işlemleri sırasında oluşabilecek hataları yönetmek için özel exception sınıfı.
// RuntimeException'dan türetilmiştir, böylece try-catch zorunluluğu olmadan kullanılabilir.
public class ReservationException extends RuntimeException {
    // Hata mesajını üst sınıfa ileten constructor
    public ReservationException(String message) {
        super(message);
    }
}