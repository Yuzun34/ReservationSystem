package com.grup7.Controller;

import com.grup7.Service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/rest/api/reports")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;

    // GET /rest/api/reports/occupancy/{year}/{month} endpoint'i
    // Belirli bir ay için doluluk raporunu PDF olarak döndürür
    @GetMapping("/{year}/{month}")
    public ResponseEntity<Resource> getMonthlyOccupancyReport(
            @PathVariable int year,        // URL'den alınacak yıl parametresi
            @PathVariable int month) {     // URL'den alınacak ay parametresi
            
        // İstek loglanır
        log.info("API İsteği: Aylık doluluk raporu isteniyor - Yıl: {}, Ay: {}", year, month);

        try {
            // Rapor servisinden PDF dosyası oluşturulur
            String filePath = reportService.generateMonthlyReport(year, month);
            File reportFile = new File(filePath);

            // Dosya oluşturma kontrolü
            if (!reportFile.exists()) {
                log.error("Rapor dosyası oluşturulamadı: {}", filePath);
                return ResponseEntity.notFound().build();  // 404 Not Found
            }

            // İndirilecek dosya adı formatlanır
            String fileName = String.format("doluluk_raporu_%d_%02d.pdf", year, month);

            // Dosya sistem kaynağı olarak hazırlanır
            Resource resource = new FileSystemResource(reportFile);

            // Başarılı oluşturma loglanır
            log.info("Rapor başarıyla oluşturuldu: {}", fileName);

            // Dosya indirilmesi için gerekli header'lar ile response hazırlanır
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);

        } catch (Exception e) {
            // Hata durumu loglanır
            log.error("Rapor oluşturulurken hata: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();  // 500 Internal Server Error
        }
    }
}