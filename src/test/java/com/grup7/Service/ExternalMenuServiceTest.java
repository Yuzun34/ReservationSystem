package com.grup7.Service;

import com.grup7.Entity.Category;
import com.grup7.Entity.CategoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * ExternalMenuService sınıfı için unit testler.
 * Bu test sınıfı, dış API'den menü kategorilerinin doğru şekilde
 * çekildiğini doğrulamak için oluşturulmuştur.
 */
@ExtendWith(MockitoExtension.class)
public class ExternalMenuServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExternalMenuService externalMenuService;

    private final String apiUrl = "https://www.themealdb.com/api/json/v1/1/categories.php";

    @BeforeEach
    void setUp() {
        // RestTemplate nesnesini mock'lanmış olan ile değiştir
        ReflectionTestUtils.setField(externalMenuService, "restTemplate", restTemplate);
        // API URL'ini set et
        ReflectionTestUtils.setField(externalMenuService, "apiUrl", apiUrl);
    }

    /**
     * Dış API'den başarılı şekilde kategorilerin çekilmesini test eder.
     */
    @Test
    void testGetCategoriesSuccess() {
        // Mock kategorileri hazırla
        List<Category> mockCategories = new ArrayList<>();

        Category category1 = new Category();
        category1.setIdCategory("1");
        category1.setStrCategory("Beef");
        mockCategories.add(category1);

        Category category2 = new Category();
        category2.setIdCategory("2");
        category2.setStrCategory("Chicken");
        mockCategories.add(category2);

        // Mock yanıtını hazırla
        CategoryResponse mockResponse = new CategoryResponse();
        mockResponse.setCategories(mockCategories);

        // RestTemplate davranışını ayarla
        when(restTemplate.getForObject(eq(apiUrl), eq(CategoryResponse.class)))
                .thenReturn(mockResponse);

        // Test
        List<Category> result = externalMenuService.getCategories();

        // Doğrulama
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getIdCategory());
        assertEquals("Beef", result.get(0).getStrCategory());
        assertEquals("2", result.get(1).getIdCategory());
        assertEquals("Chicken", result.get(1).getStrCategory());
    }

    /**
     * Dış API'den boş kategori listesi dönmesi durumunu test eder.
     */
    @Test
    void testGetCategoriesEmpty() {
        // Boş kategori listesi ile yanıt hazırla
        CategoryResponse mockResponse = new CategoryResponse();
        mockResponse.setCategories(new ArrayList<>());

        // RestTemplate davranışını ayarla
        when(restTemplate.getForObject(eq(apiUrl), eq(CategoryResponse.class)))
                .thenReturn(mockResponse);

        // Test
        List<Category> result = externalMenuService.getCategories();

        // Doğrulama
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Dış API'den null yanıt dönmesi durumunu test eder.
     * Bu durumda servis boş bir liste döndürmelidir.
     */
    @Test
    void testGetCategoriesWithNullResponse() {
        // RestTemplate davranışını ayarla - null döndür
        when(restTemplate.getForObject(anyString(), eq(CategoryResponse.class)))
                .thenReturn(null);

        // Test
        List<Category> result = externalMenuService.getCategories();

        // Doğrulama
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Dış API'den exception fırlatılması durumunu test eder.
     */
    @Test
    void testGetCategoriesWithException() {
        // RestTemplate davranışını ayarla - exception fırlat
        when(restTemplate.getForObject(anyString(), eq(CategoryResponse.class)))
                .thenThrow(new RuntimeException("API bağlantı hatası"));

        // Test ve doğrulama
        Exception exception = assertThrows(RuntimeException.class, () -> {
            externalMenuService.getCategories();
        });

        assertEquals("API bağlantı hatası", exception.getMessage());
    }
}