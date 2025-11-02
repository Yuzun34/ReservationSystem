package com.grup7.Exception;

// Sipariş loglama işlemleri sırasında oluşabilecek hataları yönetmek için özel exception sınıfı.
// RuntimeException'dan türetilmiştir, böylece try-catch zorunluluğu olmadan kullanılabilir.
public class OrderLogException extends RuntimeException {
    // Hata mesajını üst sınıfa ileten constructor
    public OrderLogException(String message) {
        super(message);
    }
}