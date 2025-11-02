package com.grup7.Entity;

import lombok.Data;

import java.util.List;

// API'den gelen kategori listesi yanıtını sarmalayan sınıf
// Dış servisten gelen JSON yanıtının yapısına uygun şekilde tasarlanmıştır
@Data
public class CategoryResponse {
    // Kategori nesnelerini içeren liste
    // API'den dönen tüm kategorileri tutar
    private List<Category> categories;
}