package com.grup7.Entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_logs")
public class OrderLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Müşterinin adı
    private String customerName;
    
    // Müşterinin soyadı
    private String customerSurname;
    
    // Rezervasyon yapılan masa numarası
    private String tableNumber;
    
    // Rezervasyon takip kodu
    private String reservationCode;
    
    // Rezervasyonun yapıldığı tarih ve saat
    private LocalDateTime reservationDate;
    
    // Siparişin kapatıldığı/tamamlandığı tarih ve saat
    private LocalDateTime closedAt;
}