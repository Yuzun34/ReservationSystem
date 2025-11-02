/**
 * Dış API ile iletişim kurarak menü kategorilerini çeken servis sınıfı.
 * TheMealDB API'sini kullanarak yemek kategorilerini getirir.
 * RestTemplate aracılığıyla HTTP isteklerini gerçekleştirir.
 */

package com.grup7.Service;

import com.grup7.Entity.Category;
import com.grup7.Entity.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service // Spring service bileşeni olduğunu belirtir
@RequiredArgsConstructor // Lombok ile zorunlu alanlar için constructor oluşturur
public class ExternalMenuService {
    // HTTP istekleri için RestTemplate nesnesi
    private final RestTemplate restTemplate = new RestTemplate();
    
    // TheMealDB API endpoint URL'i
    @Value("https://www.themealdb.com/api/json/v1/1/categories.php")
    private String apiUrl;
    
    /**
     * Dış API'den kategori listesini çeker
     * @return Kategori listesi, hata durumunda boş liste döner
     */
    public List<Category> getCategories() {
        // API'den kategori bilgilerini al
        CategoryResponse categoryResponse = restTemplate.getForObject(apiUrl, CategoryResponse.class);
        // Null kontrolü yaparak kategori listesini veya boş liste döndür
        return categoryResponse != null ? categoryResponse.getCategories() : new ArrayList<>();
    }
}