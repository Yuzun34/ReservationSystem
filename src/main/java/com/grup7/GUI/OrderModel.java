// Sipariş verilerini tutan model sınıfı
package com.grup7.GUI;

import lombok.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
public class OrderModel {
    // Rezervasyon kodu
    private String reservationCode;
    
    // Seçili kategorilerin listesi
    private List<String> selectedCategories;
    
    // Kategori isimlerini ID'lere eşleyen harita
    private Map<String, String> categoryNameToIdMap;

    // Constructor - Başlangıç değerlerini oluşturur
    public OrderModel() {
        this.selectedCategories = new ArrayList<>();
        this.categoryNameToIdMap = new HashMap<>();
    }
}