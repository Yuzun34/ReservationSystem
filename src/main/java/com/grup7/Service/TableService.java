/**
 * Masa yönetimi işlemlerini gerçekleştiren servis sınıfı.
 * Masaların rezervasyonu, rezervasyon iptali ve mevcut masa durumlarının
 * sorgulanması gibi operasyonları yönetir.
 */

package com.grup7.Service;

import com.grup7.Entity.Table;
import com.grup7.Repository.ITableRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class TableService {
    @Autowired
    private ITableRepository tableRepository;

    // Belirli bir tarih için boş masa bulma
    public List<Table> getAvailableTables(LocalDate date) {
        log.info("Tarih için uygun masalar aranıyor: {}", date);
        List<Table> availableTables = tableRepository.findByReservedDatesNotContaining(date);
        log.info("Bulunan uygun masa sayısı: {} (Tarih: {})", availableTables.size(), date);
        return availableTables;
    }

    // Masa rezervasyonu yapma
    public boolean reserveTable(Long tableId, LocalDate date) {
        log.info("Masa rezervasyon işlemi başlatıldı: MasaID={}, Tarih={}", tableId, date);

        Table table = tableRepository.findById(tableId)
                .orElseThrow(() -> {
                    log.error("Rezervasyon yapılacak masa bulunamadı: MasaID={}", tableId);
                    return new RuntimeException("Masa bulunamadı");
                });

        if (table.getReservedDates().contains(date)) {
            log.warn("Masa zaten rezerve edilmiş: MasaID={}, Tarih={}", tableId, date);
            return false;
        }

        table.getReservedDates().add(date);
        tableRepository.save(table);
        log.info("Masa başarıyla rezerve edildi: MasaID={}, Masa Numarası={}, Tarih={}",
                tableId, table.getTableNumber(), date);
        return true;
    }

    // Rezervasyon iptali
    public void cancelReservation(Long tableId, LocalDate date) {
        log.info("Masa rezervasyon iptali başlatıldı: MasaID={}, Tarih={}", tableId, date);

        Table table = tableRepository.findById(tableId)
                .orElseThrow(() -> {
                    log.error("İptal edilecek rezervasyon için masa bulunamadı: MasaID={}", tableId);
                    return new RuntimeException("Masa bulunamadı");
                });

        boolean removed = table.getReservedDates().remove(date);
        tableRepository.save(table);

        if (removed) {
            log.info("Masa rezervasyonu başarıyla iptal edildi: MasaID={}, Masa Numarası={}, Tarih={}",
                    tableId, table.getTableNumber(), date);
        } else {
            log.warn("İptal edilecek rezervasyon bulunamadı: MasaID={}, Tarih={}", tableId, date);
        }
    }

    public List<Table> getAllTables() {
        log.info("Tüm masalar listeleniyor");
        List<Table> tables = tableRepository.findAll();
        log.info("Toplam {} masa listelendi", tables.size());
        return tables;
    }
}