package com.grup7.GUI;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import javax.swing.*;

// Loglama için Slf4j kullanıyoruz
@Slf4j
public class UserRegistrationGUI {
    // UserView nesnesine dışarıdan erişim için Getter annotasyonu
    @Getter
    private static UserView userView;

    // GUI'yi başlatan ana metod
    public static void startGUI() {
        // MVC (Model-View-Controller) bileşenlerini oluştur
        UserModel model = new UserModel();
        userView = new UserView();
        UserGUIController controller = new UserGUIController(model, userView);
        
        // Controller'ı başlat ve GUI'yi görünür yap
        controller.initController();
        userView.setVisible(true);
    }
}