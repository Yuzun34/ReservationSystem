/**
 * Kullanıcı yönetimi işlemlerini gerçekleştiren servis sınıfı.
 * Kullanıcı kayıt, güncelleme, silme ve listeleme işlemlerini yönetir.
 * Ayrıca kullanıcıların masa rezervasyonlarını da bu servis üzerinden gerçekleştirir.
 * Giriş verilerinin validasyonunu sağlar.
 */

package com.grup7.Service;

import com.grup7.Dto.UserDto;
import com.grup7.Dto.TableDto;
import com.grup7.Entity.User;
import com.grup7.Entity.Table;
import com.grup7.Exception.OrderLogException;
import com.grup7.Exception.ValidationException;
import com.grup7.Repository.IUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private TableService tableService;

    // İsim ve soyisim validasyonu için yeni metod
    private void validateNameAndSurname(String name, String surname) {
        log.debug("İsim ve soyisim doğrulaması başlatıldı: İsim={}, Soyisim={}", name, surname);
        if (name == null || name.trim().isEmpty()) {
            log.warn("İsim alanı boş bırakıldı");
            throw new ValidationException("İsim alanı boş bırakılamaz");
        }
        if (surname == null || surname.trim().isEmpty()) {
            log.warn("Soyisim alanı boş bırakıldı");
            throw new ValidationException("Soyisim alanı boş bırakılamaz");
        }

        // İsim ve soyisimde sadece harf kontrolü
        if (!name.matches("^[a-zA-ZçÇğĞıİöÖşŞüÜ\\s]+$")) {
            log.warn("İsim geçersiz karakterler içeriyor: {}", name);
            throw new ValidationException("İsim sadece harflerden oluşmalıdır");
        }
        if (!surname.matches("^[a-zA-ZçÇğĞıİöÖşŞüÜ\\s]+$")) {
            log.warn("Soyisim geçersiz karakterler içeriyor: {}", surname);
            throw new ValidationException("Soyisim sadece harflerden oluşmalıdır");
        }
        log.debug("İsim ve soyisim doğrulaması başarılı");
    }

    // Tarih validasyonu
    private void validateDate(LocalDate date) {
        log.debug("Tarih doğrulaması başlatıldı: {}", date);
        if (date == null) {
            log.warn("Rezervasyon tarihi boş bırakıldı");
            throw new ValidationException("Rezervasyon tarihi boş bırakılamaz");
        }

        if (date.isBefore(LocalDate.now())) {
            log.warn("Geçersiz rezervasyon tarihi (geçmiş tarih): {}", date);
            throw new ValidationException("Rezervasyon için geçmiş tarih seçilemez");
        }
        log.debug("Tarih doğrulaması başarılı");
    }

    // Table entity'sini TableDto'ya dönüştüren yardımcı metod
    private TableDto convertToTableDto(Table table, LocalDate reservationDate) {
        TableDto dto = new TableDto();
        dto.setId(table.getId());
        dto.setTableNumber(table.getTableNumber());
        dto.setReservationDate(reservationDate);
        return dto;
    }

    // UserDto'yu User entity'sine dönüştüren yardımcı metod
    private User convertToUser(UserDto userDto) {
        // Validasyon kontrolü
        validateNameAndSurname(userDto.getName(), userDto.getSurname());
        validateDate(userDto.getDate());

        User user = new User();
        user.setName(userDto.getName().trim()); // Baştaki ve sondaki boşlukları temizle
        user.setSurname(userDto.getSurname().trim()); // Baştaki ve sondaki boşlukları temizle
        user.setDate(userDto.getDate());
        log.debug("UserDto'dan User'a dönüştürme işlemi başarılı: {}", user);
        return user;
    }

    // User entity'sini UserDto'ya dönüştüren yardımcı metod
    private UserDto convertToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setDate(user.getDate());
        dto.setReservationCode(user.getReservationCode());
        if (user.getReservedTable() != null) {
            dto.setReservedTable(convertToTableDto(user.getReservedTable(), user.getDate()));
        }
        return dto;
    }

    public List<UserDto> getAllUsers() {
        log.info("Tüm kullanıcılar listeleniyor");
        List<UserDto> users = userRepository.findAll().stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());
        log.info("Toplam {} kullanıcı listelendi", users.size());
        return users;
    }

    public UserDto addUser(UserDto userDto) {
        log.info("Yeni kullanıcı ekleme işlemi başlatıldı: Ad={}, Soyad={}, Tarih={}",
                userDto.getName(), userDto.getSurname(), userDto.getDate());

        // Validasyon kontrolü
        validateNameAndSurname(userDto.getName(), userDto.getSurname());
        validateDate(userDto.getDate());

        log.debug("Uygun masa aranıyor: Tarih={}", userDto.getDate());
        List<Table> availableTables = tableService.getAvailableTables(userDto.getDate());

        if (availableTables.isEmpty()) {
            log.warn("Tarih için uygun masa bulunamadı: {}", userDto.getDate());
            throw new ValidationException("Belirtilen tarih için uygun masa bulunmamaktadır");
        }

        Table selectedTable = availableTables.get(0);
        log.debug("Masa seçildi: ID={}, Masa Numarası={}", selectedTable.getId(), selectedTable.getTableNumber());

        boolean reserved = tableService.reserveTable(selectedTable.getId(), userDto.getDate());

        if (!reserved) {
            log.error("Masa rezervasyonu yapılamadı: MasaID={}, Tarih={}", selectedTable.getId(), userDto.getDate());
            throw new ValidationException("Masa rezervasyonu yapılamadı");
        }
        log.debug("Masa rezervasyonu başarılı: MasaID={}, Tarih={}", selectedTable.getId(), userDto.getDate());

        User newUser = convertToUser(userDto);
        newUser.setReservedTable(selectedTable);

        User savedUser = userRepository.save(newUser);
        log.info("Kullanıcı başarıyla kaydedildi: ID={}, Ad={}, Soyad={}, RezervasyonKodu={}",
                savedUser.getId(), savedUser.getName(), savedUser.getSurname(), savedUser.getReservationCode());

        return convertToUserDto(savedUser);
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        log.info("Kullanıcı güncelleme işlemi başlatıldı: ID={}, Ad={}, Soyad={}, Tarih={}",
                id, userDto.getName(), userDto.getSurname(), userDto.getDate());

        // Validasyon kontrolü
        validateNameAndSurname(userDto.getName(), userDto.getSurname());
        validateDate(userDto.getDate());

        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User updatedUser = userOptional.get();
            log.debug("Kullanıcı bulundu: ID={}, Mevcut Ad={}, Mevcut Soyad={}, Mevcut Tarih={}",
                    updatedUser.getId(), updatedUser.getName(), updatedUser.getSurname(), updatedUser.getDate());

            if (!updatedUser.getDate().equals(userDto.getDate())) {
                log.debug("Tarih değiştirildi: Eski={}, Yeni={}", updatedUser.getDate(), userDto.getDate());

                if (updatedUser.getReservedTable() != null) {
                    log.debug("Önceki masa rezervasyonu iptal ediliyor: MasaID={}, Tarih={}",
                            updatedUser.getReservedTable().getId(), updatedUser.getDate());

                    tableService.cancelReservation(
                            updatedUser.getReservedTable().getId(),
                            updatedUser.getDate()
                    );
                }

                log.debug("Yeni tarih için uygun masa aranıyor: {}", userDto.getDate());
                List<Table> availableTables = tableService.getAvailableTables(userDto.getDate());

                if (availableTables.isEmpty()) {
                    log.warn("Yeni tarih için uygun masa bulunamadı: {}", userDto.getDate());
                    throw new ValidationException("Yeni tarih için uygun masa bulunmamaktadır");
                }

                Table newTable = availableTables.get(0);
                log.debug("Yeni masa seçildi: ID={}, Masa Numarası={}", newTable.getId(), newTable.getTableNumber());

                tableService.reserveTable(newTable.getId(), userDto.getDate());
                updatedUser.setReservedTable(newTable);
            }

            updatedUser.setName(userDto.getName().trim());
            updatedUser.setSurname(userDto.getSurname().trim());
            updatedUser.setDate(userDto.getDate());

            User savedUser = userRepository.save(updatedUser);
            log.info("Kullanıcı başarıyla güncellendi: ID={}, Ad={}, Soyad={}, Tarih={}",
                    savedUser.getId(), savedUser.getName(), savedUser.getSurname(), savedUser.getDate());

            return convertToUserDto(savedUser);
        }

        log.warn("Güncellenecek kullanıcı bulunamadı: ID={}", id);
        throw new ValidationException("Kullanıcı bulunamadı");
    }

    public void deleteUser(Long id) {
        log.info("Kullanıcı silme işlemi başlatıldı: ID={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Silinecek kullanıcı bulunamadı: ID={}", id);
                    return new ValidationException("Kullanıcı bulunamadı");
                });

        if (user.getDate() == null) {
            log.error("Kullanıcının rezervasyon tarihi bulunamadı: ID={}", id);
            throw new OrderLogException("Rezervasyon tarihi bulunamadı");
        }

        if (user.getReservedTable() != null) {
            log.debug("Masa rezervasyonu iptal ediliyor: MasaID={}, Tarih={}",
                    user.getReservedTable().getId(), user.getDate());

            tableService.cancelReservation(user.getReservedTable().getId(), user.getDate());
        }

        userRepository.deleteById(id);
        log.info("Kullanıcı başarıyla silindi: ID={}, Ad={}, Soyad={}",
                id, user.getName(), user.getSurname());
    }
}