package com.grup7.GUI;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionListener;

public class UserView extends JFrame {
    @Getter private JTextField nameField;
    @Getter private JTextField surnameField;
    @Getter private JComboBox<String> dayBox;
    @Getter private JComboBox<String> monthBox;
    @Getter private JComboBox<String> yearBox;
    @Getter private JLabel reservationCodeLabel;
    @Getter private JButton copyButton;
    @Getter private JButton submitButton;

    public UserView() {
        setTitle("Rezervasyon Sistemi");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Resimli arka plan paneli
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout(10, 10));
        setContentPane(backgroundPanel);

        // Form paneli
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setOpaque(false); // Paneli şeffaf yap

        // İsim ve soyisim alanları
        JLabel nameLabel = new JLabel("İsim:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Daha büyük ve kalın
        formPanel.add(nameLabel);

        nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 18)); // Metin alanları için daha büyük yazı tipi
        formPanel.add(nameField);

        JLabel surnameLabel = new JLabel("Soyisim:");
        surnameLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Daha büyük ve kalın
        formPanel.add(surnameLabel);

        surnameField = new JTextField();
        surnameField.setFont(new Font("Arial", Font.PLAIN, 18)); // Metin alanları için daha büyük yazı tipi
        formPanel.add(surnameField);

        // Tarih seçimi
        JLabel dateLabel = new JLabel("Tarih:");
        dateLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Daha büyük ve kalın
        formPanel.add(dateLabel);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.setOpaque(false); // Paneli şeffaf yap

        // ComboBox'lar için veri hazırlığı
        String[] days = new String[31];
        for (int i = 1; i <= 31; i++) {
            days[i - 1] = String.format("%02d", i);
        }
        dayBox = new JComboBox<>(days);
        dayBox.setFont(new Font("Arial", Font.PLAIN, 18)); // ComboBox'lar için daha büyük yazı tipi

        String[] months = {"01", "02", "03", "04", "05", "06",
                "07", "08", "09", "10", "11", "12"};
        monthBox = new JComboBox<>(months);
        monthBox.setFont(new Font("Arial", Font.PLAIN, 18)); // ComboBox'lar için daha büyük yazı tipi

        String[] years = {"2024", "2025", "2026"};
        yearBox = new JComboBox<>(years);
        yearBox.setFont(new Font("Arial", Font.PLAIN, 18)); // ComboBox'lar için daha büyük yazı tipi

        datePanel.add(dayBox);
        datePanel.add(new JLabel("/"));
        datePanel.add(monthBox);
        datePanel.add(new JLabel("/"));
        datePanel.add(yearBox);
        formPanel.add(datePanel);

        // Rezervasyon kodu ve kopyalama butonu paneli
        JPanel reservationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        reservationPanel.setOpaque(false); // Paneli şeffaf yap
        JLabel reservationLabel = new JLabel("Rezervasyon Kodunuz:");
        reservationLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Daha büyük ve kalın
        formPanel.add(reservationLabel);

        reservationCodeLabel = new JLabel("Henüz rezervasyon yapılmadı");
        reservationCodeLabel.setFont(new Font("Arial", Font.BOLD, 22)); // Daha büyük ve kalın
        reservationCodeLabel.setForeground(Color.BLUE);
        reservationPanel.add(reservationCodeLabel);

        // Kopyalama butonu
        copyButton = new JButton("Kopyala");
        copyButton.setEnabled(false); // Başlangıçta devre dışı
        copyButton.setIcon(UIManager.getIcon("FileView.floppyDriveIcon")); // Sistem ikonu
        reservationPanel.add(copyButton);

        formPanel.add(reservationPanel);

        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false); // Paneli şeffaf yap
        submitButton = new JButton("Rezervasyon Yap");
        submitButton.setFont(new Font("Arial", Font.BOLD, 18)); // Butonlar için daha büyük yazı tipi
        buttonPanel.add(submitButton);

        // Ana pencereye ekle
        backgroundPanel.add(formPanel, BorderLayout.CENTER);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Arka plan panel sınıfı
    static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            // Kahvaltı resmini yükle
            backgroundImage = new ImageIcon("src/main/java/com/grup7/IMG/burger.png").getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Resmi tüm paneli dolduracak şekilde çiz
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void addSubmitButtonListener(ActionListener listener) {
        submitButton.addActionListener(listener);
    }

    public void addCopyButtonListener(ActionListener listener) {
        copyButton.addActionListener(listener);
    }

    public String getName() {
        return nameField.getText();
    }

    public String getSurname() {
        return surnameField.getText();
    }

    public String getSelectedDate() {
        return String.format("%s/%s/%s",
                dayBox.getSelectedItem(),
                monthBox.getSelectedItem(),
                yearBox.getSelectedItem()
        );
    }

    public void setReservationCode(String code) {
        reservationCodeLabel.setText(code);
        copyButton.setEnabled(true);
    }

    public void resetReservationCode() {
        reservationCodeLabel.setText("Kod alınamadı");
        copyButton.setEnabled(false);
    }

    public void clearForm() {
        nameField.setText("");
        surnameField.setText("");
        dayBox.setSelectedIndex(0);
        monthBox.setSelectedIndex(0);
        yearBox.setSelectedIndex(0);
    }

    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Başarılı", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Hata", JOptionPane.ERROR_MESSAGE);
    }

    public void showWarningMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Uyarı", JOptionPane.WARNING_MESSAGE);
    }

    public boolean showConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Onay", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public void copyToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    public void showCopyAnimation() {
        copyButton.setBackground(Color.GREEN);
        copyButton.setText("Kopyalandı!");

        // 2 saniye sonra butonu sıfırla
        Timer timer = new Timer(2000, e -> {
            copyButton.setBackground(null);
            copyButton.setText("Kopyala");
        });
        timer.setRepeats(false);
        timer.start();
    }
}