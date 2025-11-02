package com.grup7.GUI;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderView extends JFrame {
    // Controller'dan kolay erişim için Lombok @Getter annotation'ı ile UI Bileşenleri
    @Getter private JTextField txtReservationCode;
    @Getter private JList<String> categoryList;
    @Getter private DefaultListModel<String> categoryListModel;
    @Getter private JList<String> selectedList;
    @Getter private DefaultListModel<String> selectedListModel;
    @Getter private JLabel statusLabel;
    @Getter private JButton btnAdd;
    @Getter private JButton btnRemove;
    @Getter private JButton btnSubmit;

    // Modern renkler
    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private static final Color SECONDARY_COLOR = new Color(155, 89, 182);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color BACKGROUND_START = new Color(236, 240, 241);
    private static final Color BACKGROUND_END = new Color(189, 195, 199);

    public OrderView() {
        // Form ayarları
        setTitle("🛒 Sipariş Oluştur");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Modern gradyan arka plan paneli
        BackgroundPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Başlık paneli
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        JLabel titleLabel = new JLabel("Sipariş Oluştur", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(44, 62, 80));
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Üst panel - Rezervasyon kodu
        JPanel topPanel = new JPanel(new BorderLayout(10, 5));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                "  Rezervasyon Bilgisi  ",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                PRIMARY_COLOR
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblReservationCode = new JLabel("🎫 Rezervasyon Kodu:");
        lblReservationCode.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblReservationCode.setForeground(new Color(44, 62, 80));
        
        txtReservationCode = new JTextField(25);
        txtReservationCode.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtReservationCode.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        txtReservationCode.setPreferredSize(new Dimension(300, 40));
        
        JPanel codeInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        codeInputPanel.setOpaque(false);
        codeInputPanel.add(lblReservationCode);
        codeInputPanel.add(txtReservationCode);
        
        topPanel.add(codeInputPanel, BorderLayout.CENTER);

        // Orta panel - Kategori listeleri
        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Sol liste - Mevcut kategoriler
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            "  Mevcut Kategoriler  ",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 16),
            PRIMARY_COLOR
        ));
        
        categoryListModel = new DefaultListModel<>();
        categoryList = new JList<>(categoryListModel);
        categoryList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        categoryList.setBackground(Color.WHITE);
        categoryList.setSelectionBackground(PRIMARY_COLOR);
        categoryList.setSelectionForeground(Color.WHITE);
        
        JScrollPane leftScroll = new JScrollPane(categoryList);
        leftScroll.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        leftScroll.setPreferredSize(new Dimension(0, 300));
        leftPanel.add(leftScroll, BorderLayout.CENTER);

        // Sağ liste - Seçilen kategoriler
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 2),
            "  Seçilen Kategoriler  ",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 16),
            SECONDARY_COLOR
        ));
        
        selectedListModel = new DefaultListModel<>();
        selectedList = new JList<>(selectedListModel);
        selectedList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        selectedList.setBackground(Color.WHITE);
        selectedList.setSelectionBackground(SECONDARY_COLOR);
        selectedList.setSelectionForeground(Color.WHITE);
        
        JScrollPane rightScroll = new JScrollPane(selectedList);
        rightScroll.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        rightScroll.setPreferredSize(new Dimension(0, 300));
        rightPanel.add(rightScroll, BorderLayout.CENTER);

        // Orta butonlar
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        btnAdd = new JButton("➡️ Ekle");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAdd.setBackground(PRIMARY_COLOR);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setPreferredSize(new Dimension(120, 45));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnRemove = new JButton("⬅️ Çıkar");
        btnRemove.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnRemove.setBackground(new Color(231, 76, 60));
        btnRemove.setForeground(Color.WHITE);
        btnRemove.setFocusPainted(false);
        btnRemove.setBorderPainted(false);
        btnRemove.setPreferredSize(new Dimension(120, 45));
        btnRemove.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Butonları ortala
        btnAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRemove.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(btnAdd);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(btnRemove);
        buttonPanel.add(Box.createVerticalGlue());

        centerPanel.add(leftPanel);
        centerPanel.add(buttonPanel);
        centerPanel.add(rightPanel);

        // Alt panel
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        statusLabel.setForeground(new Color(127, 140, 141));
        
        btnSubmit = new JButton("✅ Siparişi Oluştur");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnSubmit.setBackground(SUCCESS_COLOR);
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFocusPainted(false);
        btnSubmit.setBorderPainted(false);
        btnSubmit.setPreferredSize(new Dimension(220, 50));
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setOpaque(false);
        statusPanel.add(statusLabel);
        
        bottomPanel.add(statusPanel, BorderLayout.CENTER);
        bottomPanel.add(btnSubmit, BorderLayout.EAST);

        // Ana panele ekle
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        log.info("Sipariş arayüzü oluşturuldu");
    }

    // Modern gradyan arka plan panel sınıfı
    static class BackgroundPanel extends JPanel {
        private static final Color BACKGROUND_START = new Color(236, 240, 241);
        private static final Color BACKGROUND_END = new Color(189, 195, 199);
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            int w = getWidth();
            int h = getHeight();
            
            // Modern gradyan arka plan
            GradientPaint gp = new GradientPaint(
                0, 0, BACKGROUND_START,
                w, h, BACKGROUND_END
            );
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }

    // Controller'dan aksiyon dinleyicileri eklemek için metodlar
    public void addAddButtonListener(ActionListener listener) {
        btnAdd.addActionListener(listener);
    }

    public void addRemoveButtonListener(ActionListener listener) {
        btnRemove.addActionListener(listener);
    }

    public void addSubmitButtonListener(ActionListener listener) {
        btnSubmit.addActionListener(listener);
    }

    // Arayüz güncelleme metodları
    public void updateCategoryList(List<String> categories) {
        categoryListModel.clear();
        for (String category : categories) {
            categoryListModel.addElement(category);
        }
        log.info("Kategori listesi güncellendi: {} adet kategori yüklendi", categories.size());
    }

    public void addSelectedCategory(String category) {
        if (!selectedListModel.contains(category)) {
            selectedListModel.addElement(category);
        }
    }

    public void clearSelection() {
        categoryList.clearSelection();
    }

    public void removeSelectedCategories() {
        int[] selectedIndices = selectedList.getSelectedIndices();
        // İndeks kaymasını önlemek için sondan başa doğru sil
        for (int i = selectedIndices.length - 1; i >= 0; i--) {
            selectedListModel.remove(selectedIndices[i]);
        }
    }

    public List<String> getSelectedCategories() {
        return selectedList.getSelectedValuesList();
    }

    public List<String> getSelectedCategoriesFromModel() {
        List<String> categories = new ArrayList<>();
        for (int i = 0; i < selectedListModel.size(); i++) {
            categories.add(selectedListModel.getElementAt(i));
        }
        return categories;
    }

    public void setReservationCode(String code) {
        if (txtReservationCode != null) {
            txtReservationCode.setText(code);
        }
    }

    public String getReservationCode() {
        return txtReservationCode.getText().trim();
    }

    public void setStatusMessage(String message) {
        statusLabel.setText(message);
    }

    public void clearForm() {
        txtReservationCode.setText("");
        selectedListModel.clear();
        categoryList.clearSelection();
        log.info("Form temizlendi");
    }

    // Mesaj pencereleri
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Hata", JOptionPane.ERROR_MESSAGE);
        log.error("Hata mesajı gösterildi: {}", message);
    }

    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Başarılı", JOptionPane.INFORMATION_MESSAGE);
        log.info("Başarı mesajı gösterildi: {}", message);
    }
}