package com.grup7.GUI;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// Loglama için Slf4j ve zorunlu constructor için RequiredArgsConstructor kullanıyoruz
@Slf4j
@RequiredArgsConstructor
public class UserGUIController {
    // MVC yapısı için gerekli model ve view referansları
    private final UserModel model;
    private final UserView view;
    
    // Mevcut rezervasyon kodu ve kullanıcı ID'si için setter'lı alanlar
    @Setter private String currentReservationCode;
    @Setter private Long currentUserId;

    // Controller başlatma metodu - Event listener'ları ekler
    public void initController() {
        view.addSubmitButtonListener(e -> addUser());
        view.addCopyButtonListener(e -> {
            copyReservationCode();
            openOrderGUI();
        });
    }

    // Sipariş GUI'sini açan metod
    private void openOrderGUI() {
        EventQueue.invokeLater(() -> {
            try {
                OrderGUI.startGUI();
                // Rezervasyon kodunu aktarmak için 500ms bekleyen zamanlayıcı
                Timer timer = new Timer(500, e -> {
                    OrderView orderView = OrderGUI.getOrderView();
                    if (orderView != null && currentReservationCode != null) {
                        orderView.setReservationCode(currentReservationCode);
                        log.info("Sipariş ekranına rezervasyon kodu aktarıldı: {}", currentReservationCode);
                    }
                });
                timer.setRepeats(false);
                timer.start();
            } catch (Exception e) {
                log.error("Sipariş ekranı açılırken hata", e);
                view.showErrorMessage("Sipariş ekranı açılırken bir hata oluştu: " + e.getMessage());
            }
        });
    }

    // Yeni kullanıcı ekleme metodu
    private void addUser() {
        try {
            // Form verilerini al
            String name = view.getName();
            String surname = view.getSurname();
            String date = view.getSelectedDate();

            // Model'i güncelle
            model.setName(name);
            model.setSurname(surname);
            model.setDate(date);

            log.info("Yeni rezervasyon isteği: {} {} için {}", name, surname, date);

            // JSON isteği oluştur
            String jsonBody = String.format("""
                {
                    "name": "%s",
                    "surname": "%s",
                    "date": "%s"
                }""",
                    name,
                    surname,
                    date
            );

            // HTTP isteği gönder
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/rest/api/users/add"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Yanıtı işle
            if (response.statusCode() == 201 || response.statusCode() == 200) {
                String responseBody = response.body();
                currentReservationCode = extractReservationCode(responseBody);
                currentUserId = extractUserId(responseBody);

                if (currentReservationCode != null) {
                    // Rezervasyon başarılı
                    model.setReservationCode(currentReservationCode);
                    view.setReservationCode(currentReservationCode);
                    view.showSuccessMessage(
                            "Rezervasyon başarıyla oluşturuldu!\nRezarvasyon Kodunuz: " + currentReservationCode +
                                    "\n(Kopyalamak için 'Kopyala' butonunu kullanabilirsiniz)"
                    );
                    log.info("Rezervasyon başarıyla oluşturuldu. Kod: {}", currentReservationCode);
                } else {
                    // Rezervasyon kodu alınamadı
                    view.resetReservationCode();
                    view.showWarningMessage("Rezervasyon oluşturuldu fakat kod alınamadı.");
                    log.warn("Rezervasyon oluşturuldu fakat kod alınamadı");
                }
                view.clearForm();
            } else {
                // Hata durumu
                view.showErrorMessage(
                        "Hata: Rezervasyon oluşturulamadı!\nStatus Code: " + response.statusCode() +
                                "\nResponse: " + response.body()
                );
                log.error("Rezervasyon oluşturulamadı. Status: {}, Response: {}",
                        response.statusCode(), response.body());
            }
        } catch (Exception ex) {
            log.error("Rezervasyon oluşturulurken hata", ex);
            view.showErrorMessage("Hata: " + ex.getMessage());
        }
    }

    // Rezervasyon kodunu panoya kopyalama
    private void copyReservationCode() {
        if (currentReservationCode != null && !currentReservationCode.isEmpty()) {
            view.copyToClipboard(currentReservationCode);
            view.showCopyAnimation();
            log.info("Rezervasyon kodu panoya kopyalandı: {}", currentReservationCode);
        }
    }

    // JSON yanıtından rezervasyon kodunu çıkarma
    private String extractReservationCode(String responseBody) {
        try {
            int startIndex = responseBody.indexOf("\"reservationCode\":\"") + "\"reservationCode\":\"".length();
            int endIndex = responseBody.indexOf("\"", startIndex);
            return responseBody.substring(startIndex, endIndex);
        } catch (Exception e) {
            log.error("Rezervasyon kodu çıkarılırken hata", e);
            return null;
        }
    }

    // JSON yanıtından kullanıcı ID'sini çıkarma
    private Long extractUserId(String responseBody) {
        try {
            int startIndex = responseBody.indexOf("\"id\":") + "\"id\":".length();
            int endIndex = responseBody.indexOf(",", startIndex);
            String idStr = responseBody.substring(startIndex, endIndex).trim();
            return Long.parseLong(idStr);
        } catch (Exception e) {
            log.error("Kullanıcı ID'si çıkarılırken hata", e);
            return null;
        }
    }
}