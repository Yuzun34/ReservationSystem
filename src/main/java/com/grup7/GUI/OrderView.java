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

    public OrderView() {
        // Form ayarları
        setTitle("Sipariş Oluştur");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Kahvaltı resmi ile arka plan paneli
        BackgroundPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Üst panel - Rezervasyon kodu
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setOpaque(false); // Şeffaf panel
        JLabel lblReservationCode = new JLabel("Rezervasyon Kodu:");
        lblReservationCode.setFont(new Font("Arial", Font.BOLD, 18)); // Büyük ve kalın yazı tipi
        txtReservationCode = new JTextField(20);
        txtReservationCode.setFont(new Font("Arial", Font.PLAIN, 16)); // Giriş metni için büyük yazı tipi
        topPanel.add(lblReservationCode, BorderLayout.WEST);
        topPanel.add(txtReservationCode, BorderLayout.CENTER);

        // Orta panel - Kategori listeleri
        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        centerPanel.setOpaque(false); // Şeffaf panel

        // Sol liste - Mevcut kategoriler
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false); // Şeffaf panel
        categoryListModel = new DefaultListModel<>();
        categoryList = new JList<>(categoryListModel);
        categoryList.setFont(new Font("Arial", Font.PLAIN, 16)); // Liste öğeleri için büyük yazı tipi
        categoryList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane leftScroll = new JScrollPane(categoryList);
        JLabel lblAvailableCategories = new JLabel("Mevcut Kategoriler:", SwingConstants.CENTER);
        lblAvailableCategories.setFont(new Font("Arial", Font.BOLD, 18)); // Büyük ve kalın yazı tipi
        leftPanel.add(lblAvailableCategories, BorderLayout.NORTH);
        leftPanel.add(leftScroll, BorderLayout.CENTER);

        // Sağ liste - Seçilen kategoriler
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false); // Şeffaf panel
        selectedListModel = new DefaultListModel<>();
        selectedList = new JList<>(selectedListModel);
        selectedList.setFont(new Font("Arial", Font.PLAIN, 16)); // Liste öğeleri için büyük yazı tipi
        JScrollPane rightScroll = new JScrollPane(selectedList);
        JLabel lblSelectedCategories = new JLabel("Seçilen Kategoriler:", SwingConstants.CENTER);
        lblSelectedCategories.setFont(new Font("Arial", Font.BOLD, 18)); // Büyük ve kalın yazı tipi
        rightPanel.add(lblSelectedCategories, BorderLayout.NORTH);
        rightPanel.add(rightScroll, BorderLayout.CENTER);

        // Orta butonlar
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false); // Şeffaf panel

        btnAdd = new JButton("Ekle >>");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 16)); // Büyük ve kalın yazı tipi
        btnRemove = new JButton("<< Çıkar");
        btnRemove.setFont(new Font("Arial", Font.BOLD, 16)); // Büyük ve kalın yazı tipi

        // Butonları ortala
        btnAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRemove.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(btnAdd);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(btnRemove);
        buttonPanel.add(Box.createVerticalGlue());

        centerPanel.add(leftPanel);
        centerPanel.add(buttonPanel);
        centerPanel.add(rightPanel);

        // Alt panel
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.setOpaque(false); // Şeffaf panel
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Büyük ve kalın yazı tipi
        btnSubmit = new JButton("Siparişi Oluştur");
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 16)); // Büyük ve kalın yazı tipi
        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        bottomPanel.add(btnSubmit, BorderLayout.EAST);

        // Ana panele ekle
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        log.info("Sipariş arayüzü oluşturuldu");
    }

    // Kahvaltı resmini eklemek için arka plan panel sınıfı
    static class BackgroundPanel extends JPanel {
        private final Image backgroundImage;

        public BackgroundPanel() {
            // Kahvaltı resmini yükle
            backgroundImage = new ImageIcon("src/main/java/com/grup7/IMG/chicken.png").getImage(); // Resim yolunuzu buraya ekleyin
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Resmi tüm paneli dolduracak şekilde çiz
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
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