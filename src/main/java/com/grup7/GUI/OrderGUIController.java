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
        // Initialize RestTemplate with converters
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new MappingJackson2HttpMessageConverter());
        restTemplate.setMessageConverters(converters);

        // Add event listeners
        view.addAddButtonListener(e -> addSelectedCategories());
        view.addRemoveButtonListener(e -> removeSelectedCategories());
        view.addSubmitButtonListener(e -> submitOrder());

        // Load categories on startup
        loadCategories();

        log.info("OrderController initialized successfully");
    }

    private void loadCategories() {
        SwingWorker<List<Category>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Category> doInBackground() {
                try {
                    view.setStatusMessage("Loading categories...");
                    log.info("Loading categories...");

                    // ResponseEntity kullanarak kategorileri Category[] dizisi olarak al
                    ResponseEntity<Category[]> response = restTemplate.getForEntity(
                            "http://localhost:8080/rest/api/menu",
                            Category[].class
                    );

                    return Arrays.asList(response.getBody());
                } catch (Exception e) {
                    log.error("Error loading categories", e);
                    SwingUtilities.invokeLater(() ->
                            view.showErrorMessage("Error loading categories: " + e.getMessage())
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
                        // Add category names and IDs to model map
                        for (Category category : categories) {
                            if (category != null && category.getStrCategory() != null) {
                                model.getCategoryNameToIdMap().put(
                                        category.getStrCategory(),
                                        category.getIdCategory()
                                );
                            }
                        }

                        // Update view with category names
                        List<String> categoryNames = categories.stream()
                                .filter(c -> c != null && c.getStrCategory() != null)
                                .map(Category::getStrCategory)
                                .collect(Collectors.toList());

                        view.updateCategoryList(categoryNames);
                        view.setStatusMessage("Categories loaded");
                        log.info("Categories loaded successfully: {} categories", categoryNames.size());
                    } else {
                        view.setStatusMessage("No categories found");
                        log.warn("No categories found");
                    }
                } catch (Exception e) {
                    view.setStatusMessage("Error: Categories could not be loaded");
                    log.error("Error processing categories", e);
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
        log.debug("Selected categories added: {}", selectedCategories);
    }

    private void removeSelectedCategories() {
        List<String> selectedCategoriesBefore = view.getSelectedCategoriesFromModel();
        view.removeSelectedCategories();
        List<String> selectedCategoriesAfter = view.getSelectedCategoriesFromModel();

        // Update model
        model.setSelectedCategories(selectedCategoriesAfter);
        log.debug("Categories removed. Remaining: {}", selectedCategoriesAfter.size());
    }

    private void submitOrder() {
        String reservationCode = view.getReservationCode();
        if (reservationCode.isEmpty()) {
            view.showErrorMessage("Please enter reservation code");
            return;
        }

        if (model.getSelectedCategories().isEmpty()) {
            view.showErrorMessage("Please select at least one category");
            return;
        }

        // Create order
        OrderDto orderDto = new OrderDto();
        orderDto.setReservationCode(reservationCode);

        // Get category IDs from category names
        List<String> selectedCategoryIds = new ArrayList<>();
        for (String categoryName : model.getSelectedCategories()) {
            String categoryId = model.getCategoryNameToIdMap().get(categoryName);
            if (categoryId != null) {
                selectedCategoryIds.add(categoryId);
            }
        }
        orderDto.setCategoryIds(selectedCategoryIds);

        // Save order
        saveOrder(orderDto);
    }

    private void saveOrder(OrderDto orderDto) {
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                try {
                    view.setStatusMessage("Saving order...");
                    log.info("Saving order: {} categories for reservation code {}",
                            orderDto.getCategoryIds().size(), orderDto.getReservationCode());

                    // POST request with RestTemplate
                    ResponseEntity<OrderDto> response = restTemplate.postForEntity(
                            "http://localhost:8080/rest/api/orders/save",
                            orderDto,
                            OrderDto.class
                    );

                    return response.getStatusCode().is2xxSuccessful();
                } catch (Exception e) {
                    log.error("Error saving order", e);
                    SwingUtilities.invokeLater(() ->
                            view.showErrorMessage("Error saving order: " + e.getMessage())
                    );
                    return false;
                }
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        view.setStatusMessage("Order saved successfully");
                        view.showSuccessMessage("Order created successfully");
                        view.clearForm();
                        model.getSelectedCategories().clear();
                        log.info("Order saved successfully");
                    } else {
                        view.setStatusMessage("Order could not be saved");
                        log.warn("Order could not be saved");
                    }
                } catch (Exception e) {
                    view.setStatusMessage("An error occurred");
                    log.error("Unexpected error in order processing", e);
                }
            }
        };
        worker.execute();
    }
}