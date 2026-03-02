/**
 * Rapor oluşturma işlemlerini gerçekleştiren servis sınıfı.
 * Aylık doluluk oranları gibi istatistiksel raporları PDF formatında
 * oluşturur ve kaydeder. iText kütüphanesini kullanarak
 * PDF dosyalarının formatlanmasını sağlar.
 */

package com.grup7.Service;

import com.grup7.Entity.Table;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class ReportService {

    @Autowired
    private TableService tableService;

    public String generateMonthlyReport(int year, int month) {
        try {
            // "reports" klasörünün varlığını kontrol et, yoksa oluştur
            File reportsDir = new File("reports");
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
                log.info("Reports dizini oluşturuldu: {}", reportsDir.getAbsolutePath());
            }

            String fileName = String.format("reports/occupancy_report_%d_%02d.pdf", year, month);
            log.info("Rapor oluşturuluyor: {}", fileName);

            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                Document document = new Document();
                PdfWriter.getInstance(document, fos);
                document.open();

                // Başlık ekleme
                Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
                Paragraph title = new Paragraph(String.format("%d - %d Monthly Occupancy Report", year, month), titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                document.add(new Paragraph("\n"));

                // Tablo oluşturma
                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(100);

                // Tablo başlıkları
                Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
                PdfPCell dateHeader = new PdfPCell(new Phrase("Date", headerFont));
                PdfPCell occupancyHeader = new PdfPCell(new Phrase("Occupancy Rate (%)", headerFont));

                dateHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                occupancyHeader.setHorizontalAlignment(Element.ALIGN_CENTER);

                table.addCell(dateHeader);
                table.addCell(occupancyHeader);

                // Her gün için doluluk bilgisi ekleme
                YearMonth yearMonth = YearMonth.of(year, month);
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
                    LocalDate date = LocalDate.of(year, month, day);
                    double occupancyRate = calculateDailyOccupancy(date);

                    PdfPCell dateCell = new PdfPCell(new Phrase(date.format(dateFormatter)));
                    PdfPCell occupancyCell = new PdfPCell(new Phrase(String.format("%.1f", occupancyRate)));

                    dateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    occupancyCell.setHorizontalAlignment(Element.ALIGN_CENTER);

                    table.addCell(dateCell);
                    table.addCell(occupancyCell);
                }

                document.add(table);
                document.close();
            }

            log.info("Rapor başarıyla oluşturuldu: {}", fileName);
            return fileName;

        } catch (Exception e) {
            log.error("Rapor oluşturulurken hata oluştu: {}", e.getMessage(), e);
            throw new RuntimeException("Rapor oluşturulurken hata oluştu: " + e.getMessage());
        }
    }

    private double calculateDailyOccupancy(LocalDate date) {
        List<Table> allTables = tableService.getAllTables();
        List<Table> availableTables = tableService.getAvailableTables(date);
        int availableTablesCount = availableTables.size();

        if (allTables.isEmpty()) {
            return 0.0;
        }

        int totalTables = allTables.size();
        int occupiedTables = totalTables - availableTablesCount;

        return (occupiedTables * 100.0) / totalTables;
    }
}