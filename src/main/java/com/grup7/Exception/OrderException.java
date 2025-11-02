package com.grup7.Exception;

// Sipariş işlemleri sırasında oluşabilecek hataları yönetmek için özel exception sınıfı.
// RuntimeException'dan türetilmiştir, böylece try-catch zorunluluğu olmadan kullanılabilir.
public class OrderException extends RuntimeException {
    // Hata mesajını üst sınıfa ileten constructor
    public OrderException(String message) {
        super(message);
    }
}