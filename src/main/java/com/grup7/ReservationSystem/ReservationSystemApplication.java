// Reservation System Main Application Class
package com.grup7.ReservationSystem;

import com.formdev.flatlaf.FlatLightLaf;
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

// Logging using Lombok annotation
@Slf4j
// Spring Boot application annotation
@SpringBootApplication
// Package to scan for Spring components
@ComponentScan(basePackages = {"com.grup7"})
// Package containing JPA entities
@EntityScan(basePackages = {"com.grup7"})
// Package containing JPA repositories
@EnableJpaRepositories(basePackages = {"com.grup7"})
public class ReservationSystemApplication {
    public static void main(String[] args) {
        // Disable headless mode for GUI application
        System.setProperty("java.awt.headless", "false");
        
        // Set modern FlatLaf look and feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            log.info("FlatLaf modern UI theme applied successfully");
        } catch (UnsupportedLookAndFeelException e) {
            try {
                // Fallback to system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                log.warn("FlatLaf not available, using system look and feel");
            } catch (Exception ex) {
                log.error("Error setting look and feel", ex);
            }
        }
        
        // Start Spring application
        ConfigurableApplicationContext context = SpringApplication.run(ReservationSystemApplication.class, args);

        // Start GUI on Event Dispatch Thread
        EventQueue.invokeLater(() -> {
            try {
                UserRegistrationGUI.startGUI();
                log.info("UserRegistrationGUI started successfully");
            } catch (Exception e) {
                log.error("Error starting GUI", e);
                JOptionPane.showMessageDialog(null, 
                    "Error starting GUI: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}