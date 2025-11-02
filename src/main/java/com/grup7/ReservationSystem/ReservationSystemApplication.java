// Rezervasyon sisteminin ana uygulama sınıfı
package com.grup7.ReservationSystem;

import com.grup7.GUI.UserRegistrationGUI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.swing.*;
import java.awt.*;

// Loglama için Lombok annotasyonu
@Slf4j
// Spring Boot uygulaması olduğunu belirtir
@SpringBootApplication
// Spring bileşenlerinin taranacağı paket
@ComponentScan(basePackages = {"com.grup7"})
// JPA varlıklarının bulunduğu paket
@EntityScan(basePackages = {"com.grup7"})
// JPA repository'lerinin bulunduğu paket
@EnableJpaRepositories(basePackages = {"com.grup7"})
public class ReservationSystemApplication {
    public static void main(String[] args) {
        // GUI uygulaması için headless modu kapatılıyor
        System.setProperty("java.awt.headless", "false");
        
        // Sistemin görünümünü native görünüme ayarla
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            log.error("Look and feel ayarlanırken hata oluştu", e);
        }
        
        // Spring uygulamasını başlat
        ConfigurableApplicationContext context = SpringApplication.run(ReservationSystemApplication.class, args);

        // GUI'yi Event Dispatch Thread üzerinde başlat
        EventQueue.invokeLater(() -> {
            try {
                UserRegistrationGUI.startGUI();
                log.info("UserRegistrationGUI başlatıldı");
            } catch (Exception e) {
                log.error("GUI başlatılırken hata oluştu", e);
            }
        });
    }
}