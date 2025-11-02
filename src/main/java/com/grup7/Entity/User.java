package com.grup7.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.UUID;

// Kullanıcı/müşteri bilgilerini tutan entity sınıfı
@Entity
@jakarta.persistence.Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Slf4j(topic = "UserDAO")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Kullanıcının adı
    private String name;

    // Kullanıcının soyadı
    private String surname;

    // Rezervasyon tarihi
    private LocalDate date;

    // Otomatik oluşturulan benzersiz rezervasyon kodu
    // "RZ53-" prefix'i ve UUID'nin ilk 10 karakteri ile oluşturulur
    private String reservationCode = "RZ53-"+ UUID.randomUUID().toString().substring(0,10);

    // Rezerve edilen masa bilgisi
    @ManyToOne                        // Çok-a-bir ilişki (Birden çok kullanıcı aynı masayı farklı zamanlarda rezerve edebilir)
    @JoinColumn(name = "table_id")    // İlişki için kullanılacak foreign key kolonu
    private Table reservedTable;
}