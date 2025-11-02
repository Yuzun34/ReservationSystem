package com.grup7.Util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LogUtil {
    // Log kayıtlarının yazılacağı dosya adı
    private static final String LOG_FILE = "log.txt";
    
    // Log kayıtları için kullanılacak tarih-saat formatı
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Rezervasyon kapatma işleminin detaylarını log dosyasına yazar
    // @param customerName - Müşterinin adı
    // @param customerSurname - Müşterinin soyadı
    // @param tableNumber - Rezerve edilen masa numarası
    // @param reservationCode - Rezervasyon kodu
    public static void logToFile(String customerName, String customerSurname, String tableNumber, String reservationCode) {
        // Log dosyasını açma ve yazma işlemi (append modunda)
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            // İstanbul zaman diliminde şu anki zamanı al
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Istanbul"));
            
            // Log bilgilerini dosyaya yaz
            writer.println("----------------------------------------");
            writer.println("Rezervasyon Kapandı");
            writer.println("Current Date and Time (UTC): " + now.format(formatter));
            writer.println("Müşteri: " + customerName + " " + customerSurname);
            writer.println("Masa Numarası: " + tableNumber);
            writer.println("Rezervasyon Kodu: " + reservationCode);
            writer.println("----------------------------------------\n");
            
        } catch (IOException e) {
            // Dosya işlemleri sırasında hata oluşursa stack trace'i yazdır
            e.printStackTrace();
        }
    }
}