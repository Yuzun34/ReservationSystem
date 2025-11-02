package com.grup7.Exception;

// Bir masa zaten rezerve edilmiş olduğunda fırlatılan özel exception sınıfı.
// Throwable'dan türetilmiştir, bu nedenle try-catch bloğu ile yakalanması gerekir.
public class TableAlreadyReservedException extends Throwable {
    // Hata mesajını üst sınıfa ileten constructor
    public TableAlreadyReservedException(String message) {
        super(message);
    }
}