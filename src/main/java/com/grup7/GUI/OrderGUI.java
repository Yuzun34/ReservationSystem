package com.grup7.GUI;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import javax.swing.*;

// Sipariş arayüzünü yöneten ana GUI sınıfı
// Loglama için Lombok annotasyonu
@Slf4j
public class OrderGUI {
    // Sipariş görünümüne dışarıdan erişim için getter
    @Getter
    private static OrderView orderView;

    /**
     * Sipariş arayüzünü başlatan metod
     * MVC (Model-View-Controller) tasarım desenini kullanır:
     * - Model: Veri katmanı
     * - View: Görünüm katmanı
     * - Controller: Kontrol katmanı
     */
    public static void startGUI() {
        // Model oluştur - Veri katmanı
        OrderModel model = new OrderModel();
        // View oluştur - Görünüm katmanı
        orderView = new OrderView();
        // HTTP istekleri için RestTemplate oluştur
        RestTemplate restTemplate = new RestTemplate();
        // Controller oluştur ve bağımlılıkları enjekte et
        OrderGUIController controller = new OrderGUIController(model, orderView, restTemplate);

        // Controller'ı başlat
        controller.initController();
        // Arayüzü görünür yap
        orderView.setVisible(true);
    }
}