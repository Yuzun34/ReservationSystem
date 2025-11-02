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

    // Modern renkler
    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private static final Color SECONDARY_COLOR = new Color(155, 89, 182);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color BACKGROUND_START = new Color(236, 240, 241);
    private static final Color BACKGROUND_END = new Color(189, 195, 199);

    public UserView() {
        setTitle("🍽️ Rezervasyon Sistemi");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Modern gradyan arka plan paneli
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout(10, 10));
        setContentPane(backgroundPanel);

        // Başlık paneli
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        JLabel titleLabel = new JLabel("Rezervasyon Oluştur", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(44, 62, 80));
        headerPanel.add(titleLabel);
        backgroundPanel.add(headerPanel, BorderLayout.NORTH);

        // Form paneli
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        formPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // İsim alanı
        JLabel nameLabel = new JLabel("📝 İsim:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        formPanel.add(nameLabel, gbc);

        nameField = new JTextField(20);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        nameField.setPreferredSize(new Dimension(250, 35));
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(nameField, gbc);

        // Soyisim alanı
        JLabel surnameLabel = new JLabel("📝 Soyisim:");
        surnameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        surnameLabel.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(surnameLabel, gbc);

        surnameField = new JTextField(20);
        surnameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        surnameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        surnameField.setPreferredSize(new Dimension(250, 35));
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(surnameField, gbc);

        // Tarih seçimi
        JLabel dateLabel = new JLabel("📅 Tarih:");
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dateLabel.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(dateLabel, gbc);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        datePanel.setOpaque(false);

        // ComboBox'lar için veri hazırlığı
        String[] days = new String[31];
        for (int i = 1; i <= 31; i++) {
            days[i - 1] = String.format("%02d", i);
        }
        dayBox = new JComboBox<>(days);
        dayBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dayBox.setPreferredSize(new Dimension(70, 35));
        styleComboBox(dayBox);

        String[] months = {"01", "02", "03", "04", "05", "06",
                "07", "08", "09", "10", "11", "12"};
        monthBox = new JComboBox<>(months);
        monthBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        monthBox.setPreferredSize(new Dimension(70, 35));
        styleComboBox(monthBox);

        String[] years = {"2024", "2025", "2026"};
        yearBox = new JComboBox<>(years);
        yearBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        yearBox.setPreferredSize(new Dimension(80, 35));
        styleComboBox(yearBox);

        datePanel.add(dayBox);
        datePanel.add(new JLabel("/") {{
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setForeground(new Color(44, 62, 80));
        }});
        datePanel.add(monthBox);
        datePanel.add(new JLabel("/") {{
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setForeground(new Color(44, 62, 80));
        }});
        datePanel.add(yearBox);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(datePanel, gbc);

        // Rezervasyon kodu ve kopyalama butonu paneli
        JLabel reservationLabel = new JLabel("🎫 Rezervasyon Kodunuz:");
        reservationLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        reservationLabel.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(reservationLabel, gbc);

        JPanel reservationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        reservationPanel.setOpaque(false);

        reservationCodeLabel = new JLabel("Henüz rezervasyon yapılmadı");
        reservationCodeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        reservationCodeLabel.setForeground(new Color(52, 152, 219));
        reservationCodeLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        reservationPanel.add(reservationCodeLabel);

        // Kopyalama butonu
        copyButton = new JButton("📋 Kopyala");
        copyButton.setEnabled(false);
        copyButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        copyButton.setBackground(SECONDARY_COLOR);
        copyButton.setForeground(Color.WHITE);
        copyButton.setFocusPainted(false);
        copyButton.setBorderPainted(false);
        copyButton.setPreferredSize(new Dimension(120, 40));
        copyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        reservationPanel.add(copyButton);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(reservationPanel, gbc);

        // Ana form paneli
        backgroundPanel.add(formPanel, BorderLayout.CENTER);

        // Butonlar paneli
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        
        submitButton = new JButton("✅ Rezervasyon Yap");
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        submitButton.setBackground(PRIMARY_COLOR);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setBorderPainted(false);
        submitButton.setPreferredSize(new Dimension(250, 50));
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonPanel.add(submitButton);

        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    // ComboBox stil metodu
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                if (isSelected) {
                    label.setBackground(PRIMARY_COLOR);
                    label.setForeground(Color.WHITE);
                }
                return label;
            }
        });
    }

    // Modern gradyan arka plan panel sınıfı
    static class BackgroundPanel extends JPanel {
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
        copyButton.setBackground(SUCCESS_COLOR);
        copyButton.setText("✅ Kopyalandı!");

        // 2 saniye sonra butonu sıfırla
        Timer timer = new Timer(2000, e -> {
            copyButton.setBackground(SECONDARY_COLOR);
            copyButton.setText("📋 Kopyala");
        });
        timer.setRepeats(false);
        timer.start();
    }
}