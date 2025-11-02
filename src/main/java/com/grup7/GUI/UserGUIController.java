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

// Logging using Slf4j and RequiredArgsConstructor for required constructor
@Slf4j
@RequiredArgsConstructor
public class UserGUIController {
    // Model and view references required for MVC structure
    private final UserModel model;
    private final UserView view;
    
    // Setter fields for current reservation code and user ID
    @Setter private String currentReservationCode;
    @Setter private Long currentUserId;

    // Controller initialization method - adds event listeners
    public void initController() {
        view.addSubmitButtonListener(e -> addUser());
        view.addCopyButtonListener(e -> {
            copyReservationCode();
            openOrderGUI();
        });
    }

    // Open order GUI method
    private void openOrderGUI() {
        EventQueue.invokeLater(() -> {
            try {
                OrderGUI.startGUI();
                // Timer to transfer reservation code after 500ms
                Timer timer = new Timer(500, e -> {
                    OrderView orderView = OrderGUI.getOrderView();
                    if (orderView != null && currentReservationCode != null) {
                        orderView.setReservationCode(currentReservationCode);
                        log.info("Reservation code transferred to order screen: {}", currentReservationCode);
                    }
                });
                timer.setRepeats(false);
                timer.start();
            } catch (Exception e) {
                log.error("Error opening order screen", e);
                view.showErrorMessage("Error opening order screen: " + e.getMessage());
            }
        });
    }

    // Add new user method
    private void addUser() {
        // Run in background thread to avoid blocking UI
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    // Get form data
                    String name = view.getName();
                    String surname = view.getSurname();
                    String date = view.getSelectedDate();

                    // Validate input
                    if (name == null || name.trim().isEmpty()) {
                        SwingUtilities.invokeLater(() -> 
                            view.showErrorMessage("Please enter your name"));
                        return null;
                    }
                    if (surname == null || surname.trim().isEmpty()) {
                        SwingUtilities.invokeLater(() -> 
                            view.showErrorMessage("Please enter your surname"));
                        return null;
                    }

                    // Update model
                    model.setName(name);
                    model.setSurname(surname);
                    model.setDate(date);

                    log.info("New reservation request: {} {} for {}", name, surname, date);

                    // Create JSON request
                    String jsonBody = String.format("""
                        {
                            "name": "%s",
                            "surname": "%s",
                            "date": "%s"
                        }""",
                            name.trim(),
                            surname.trim(),
                            date
                    );

                    // Send HTTP request
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:8080/rest/api/users/add"))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                            .build();

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    // Process response
                    final int statusCode = response.statusCode();
                    final String responseBody = response.body();
                    
                    SwingUtilities.invokeLater(() -> {
                        if (statusCode == 201 || statusCode == 200) {
                            String reservationCode = extractReservationCode(responseBody);
                            Long userId = extractUserId(responseBody);

                            if (reservationCode != null && !reservationCode.isEmpty()) {
                                // Reservation successful
                                currentReservationCode = reservationCode;
                                currentUserId = userId;
                                model.setReservationCode(reservationCode);
                                view.setReservationCode(reservationCode);
                                view.showSuccessMessage(
                                        "Reservation created successfully!\nYour Reservation Code: " + reservationCode +
                                                "\n(You can use the 'Copy' button to copy it)"
                                );
                                log.info("Reservation created successfully. Code: {}", reservationCode);
                                // Don't clear form - let user see the reservation code
                            } else {
                                // Reservation code could not be retrieved
                                view.resetReservationCode();
                                view.showWarningMessage("Reservation created but code could not be retrieved.");
                                log.warn("Reservation created but code could not be retrieved");
                                view.clearForm();
                            }
                        } else {
                            // Error state
                            view.showErrorMessage(
                                    "Error: Reservation could not be created!\nStatus Code: " + statusCode +
                                            "\nResponse: " + responseBody
                            );
                            log.error("Reservation could not be created. Status: {}, Response: {}",
                                    statusCode, responseBody);
                        }
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    SwingUtilities.invokeLater(() -> {
                        view.showErrorMessage("Request was interrupted. Please try again.");
                        log.error("Request interrupted", e);
                    });
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        log.error("Error creating reservation", ex);
                        String errorMsg = ex.getMessage();
                        if (errorMsg == null || errorMsg.isEmpty()) {
                            errorMsg = "Unable to connect to server. Please ensure the server is running on port 8080.";
                        }
                        view.showErrorMessage("Error: " + errorMsg);
                    });
                }
                return null;
            }
        };
        worker.execute();
    }

    // Copy reservation code to clipboard
    private void copyReservationCode() {
        if (currentReservationCode != null && !currentReservationCode.isEmpty()) {
            view.copyToClipboard(currentReservationCode);
            view.showCopyAnimation();
            log.info("Reservation code copied to clipboard: {}", currentReservationCode);
        }
    }

    // Extract reservation code from JSON response
    private String extractReservationCode(String responseBody) {
        try {
            int startIndex = responseBody.indexOf("\"reservationCode\":\"") + "\"reservationCode\":\"".length();
            int endIndex = responseBody.indexOf("\"", startIndex);
            if (startIndex > "\"reservationCode\":\"".length() && endIndex > startIndex) {
                return responseBody.substring(startIndex, endIndex);
            }
            return null;
        } catch (Exception e) {
            log.error("Error extracting reservation code", e);
            return null;
        }
    }

    // Extract user ID from JSON response
    private Long extractUserId(String responseBody) {
        try {
            int startIndex = responseBody.indexOf("\"id\":") + "\"id\":".length();
            int endIndex = responseBody.indexOf(",", startIndex);
            if (endIndex == -1) {
                endIndex = responseBody.indexOf("}", startIndex);
            }
            if (startIndex > "\"id\":".length() && endIndex > startIndex) {
                String idStr = responseBody.substring(startIndex, endIndex).trim();
                return Long.parseLong(idStr);
            }
            return null;
        } catch (Exception e) {
            log.error("Error extracting user ID", e);
            return null;
        }
    }
}