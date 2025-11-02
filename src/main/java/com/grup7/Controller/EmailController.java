package com.grup7.Controller;

import com.grup7.Service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/rest/api")
@RequiredArgsConstructor
public class EmailController {

    @Autowired
    private final EmailService emailService;
    @PostMapping("/send-email")
    public ResponseEntity<String> sendLogsByEmail(@RequestParam String email) {
        try {
            // Log dosyasının yolu
            String logFilePath = "log.txt";

            // Dosyanın varlığını kontrol et
            File logFile = new File(logFilePath);
            if (!logFile.exists()) {
                return ResponseEntity.badRequest().body("Log dosyası bulunamadı!");
            }

            // E-posta gönderimi
            emailService.sendEmailWithAttachment(
                    email,
                    "Log Dosyası",
                    "İstediğiniz log dosyası ektedir.",
                    logFilePath,
                    "log.txt"
            );

            return ResponseEntity.ok("Log dosyası başarıyla e-posta adresine gönderildi: " + email);
        } catch (MessagingException e) {
            return ResponseEntity.internalServerError().body("E-posta gönderilirken bir hata oluştu: " + e.getMessage());
        }
    }
}