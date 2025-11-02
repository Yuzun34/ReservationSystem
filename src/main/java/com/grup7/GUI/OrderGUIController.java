package com.grup7.GUI;

import com.grup7.Entity.Category;
import com.grup7.Dto.OrderDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class OrderGUIController {
    private final OrderModel model;
    private final OrderView view;
    private final RestTemplate restTemplate;

    public void initController() {
        // RestTemplate'i dönüştürücülerle başlat
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new MappingJackson2HttpMessageConverter());
        restTemplate.setMessageConverters(converters);

        // Olay dinleyicilerini ekle
        view.addAddButtonListener(e -> addSelectedCategories());
        view.addRemoveButtonListener(e -> removeSelectedCategories());
        view.addSubmitButtonListener(e -> submitOrder());

        // Başlangıçta kategorileri yükle
        loadCategories();

        log.info("OrderController başlatıldı");
    }

    private void loadCategories() {
        SwingWorker<List<Category>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Category> doInBackground() {
                try {
                    view.setStatusMessage("Kategoriler yükleniyor...");
                    log.info("Kategoriler yükleniyor...");

                    // ResponseEntity kullanarak kategorileri Category[] dizisi olarak al
                    ResponseEntity<Category[]> response = restTemplate.getForEntity(
                            "http://localhost:8080/rest/api/menu",
                            Category[].class
                    );

                    return Arrays.asList(response.getBody());
                } catch (Exception e) {
                    log.error("Kategoriler yüklenirken hata oluştu", e);
                    SwingUtilities.invokeLater(() ->
                            view.showErrorMessage("Kategoriler yüklenirken hata oluştu: " + e.getMessage())
                    );
                    return new ArrayList<>();
                }
            }

            @Override
            protected void done() {
                try {
                    List<Category> categories = get();
                    model.getCategoryNameToIdMap().clear();

                    if (categories != null && !categories.isEmpty()) {
                        // Kategori isimlerini ve ID'lerini model haritasına ekle
                        for (Category category : categories) {
                            if (category != null && category.getStrCategory() != null) {
                                model.getCategoryNameToIdMap().put(
                                        category.getStrCategory(),
                                        category.getIdCategory()
                                );
                            }
                        }

                        // Görünümü kategori isimleriyle güncelle
                        List<String> categoryNames = categories.stream()
                                .filter(c -> c != null && c.getStrCategory() != null)
                                .map(Category::getStrCategory)
                                .collect(Collectors.toList());

                        view.updateCategoryList(categoryNames);
                        view.setStatusMessage("Kategoriler yüklendi");
                        log.info("Kategoriler başarıyla yüklendi: {} adet kategori", categoryNames.size());
                    } else {
                        view.setStatusMessage("Kategori bulunamadı");
                        log.warn("Kategori bulunamadı");
                    }
                } catch (Exception e) {
                    view.setStatusMessage("Hata: Kategoriler yüklenemedi");
                    log.error("Kategoriler işlenirken hata oluştu", e);
                }
            }
        };
        worker.execute();
    }

    private void addSelectedCategories() {
        List<String> selectedCategories = view.getCategoryList().getSelectedValuesList();
        for (String category : selectedCategories) {
            view.addSelectedCategory(category);
            if (!model.getSelectedCategories().contains(category)) {
                model.getSelectedCategories().add(category);
            }
        }
        view.clearSelection();
        log.debug("Seçilen kategoriler eklendi: {}", selectedCategories);
    }

    private void removeSelectedCategories() {
        List<String> selectedCategoriesBefore = view.getSelectedCategoriesFromModel();
        view.removeSelectedCategories();
        List<String> selectedCategoriesAfter = view.getSelectedCategoriesFromModel();

        // Modeli güncelle
        model.setSelectedCategories(selectedCategoriesAfter);
        log.debug("Kategoriler kaldırıldı. Kalan: {}", selectedCategoriesAfter.size());
    }

    private void submitOrder() {
        String reservationCode = view.getReservationCode();
        if (reservationCode.isEmpty()) {
            view.showErrorMessage("Lütfen rezervasyon kodunu giriniz");
            return;
        }

        if (model.getSelectedCategories().isEmpty()) {
            view.showErrorMessage("Lütfen en az bir kategori seçiniz");
            return;
        }

        // Siparişi oluştur
        OrderDto orderDto = new OrderDto();
        orderDto.setReservationCode(reservationCode);

        // Kategori isimlerinden ID'leri al
        List<String> selectedCategoryIds = new ArrayList<>();
        for (String categoryName : model.getSelectedCategories()) {
            String categoryId = model.getCategoryNameToIdMap().get(categoryName);
            if (categoryId != null) {
                selectedCategoryIds.add(categoryId);
            }
        }
        orderDto.setCategoryIds(selectedCategoryIds);

        // Siparişi kaydet
        saveOrder(orderDto);
    }

    private void saveOrder(OrderDto orderDto) {
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                try {
                    view.setStatusMessage("Sipariş kaydediliyor...");
                    log.info("Sipariş kaydediliyor: {} rezervasyon kodu için {} kategori",
                            orderDto.getReservationCode(), orderDto.getCategoryIds().size());

                    // RestTemplate ile POST isteği
                    ResponseEntity<OrderDto> response = restTemplate.postForEntity(
                            "http://localhost:8080/rest/api/orders/save",
                            orderDto,
                            OrderDto.class
                    );

                    return response.getStatusCode().is2xxSuccessful();
                } catch (Exception e) {
                    log.error("Sipariş kaydedilirken hata oluştu", e);
                    SwingUtilities.invokeLater(() ->
                            view.showErrorMessage("Sipariş kaydedilirken hata oluştu: " + e.getMessage())
                    );
                    return false;
                }
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        view.setStatusMessage("Sipariş başarıyla kaydedildi");
                        view.showSuccessMessage("Sipariş başarıyla oluşturuldu");
                        view.clearForm();
                        model.getSelectedCategories().clear();
                        log.info("Sipariş başarıyla kaydedildi");
                    } else {
                        view.setStatusMessage("Sipariş kaydedilemedi");
                        log.warn("Sipariş kaydedilemedi");
                    }
                } catch (Exception e) {
                    view.setStatusMessage("Hata oluştu");
                    log.error("Sipariş işleminde beklenmeyen hata", e);
                }
            }
        };
        worker.execute();
    }
}