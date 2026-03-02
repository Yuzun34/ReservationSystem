package com.grup7.Exception;

// Bir masa zaten rezerve edilmiş olduğunda fırlatılan özel exception sınıfı.
// RuntimeException'dan türetilmiştir, bu sayede try-catch bloğu olmadan kullanılabilir.
public class TableAlreadyReservedException extends RuntimeException {
    // Hata mesajını üst sınıfa ileten constructor
    public TableAlreadyReservedException(String message) {
        super(message);
    }
}