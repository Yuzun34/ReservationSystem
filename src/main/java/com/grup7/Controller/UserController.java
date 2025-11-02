package com.grup7.Controller;

import com.grup7.Dto.UserDto;
import com.grup7.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/rest/api/users")
@Slf4j
public class UserController {


    @Autowired
    private UserService userService;

    // POST /rest/api/users/add endpoint'i
    // Yeni bir kullanıcı ekler
    @PostMapping("/add")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        // Gelen istek loglanır
        log.info("API İsteği: Kullanıcı ekleme - Ad={}, Soyad={}, Tarih={}",
                userDto.getName(), userDto.getSurname(), userDto.getDate());

        // Kullanıcı kaydedilir
        UserDto savedUser = userService.addUser(userDto);

        // Başarılı kayıt loglanır
        log.info("API Yanıtı: Kullanıcı eklendi - ID={}, RezervasyonKodu={}",
                savedUser.getId(), savedUser.getReservationCode());

        // HTTP 200 OK ile kaydedilen kullanıcı döndürülür
        return ResponseEntity.ok(savedUser);
    }

    // GET /rest/api/users/list endpoint'i
    // Tüm kullanıcıları listeler
    @GetMapping("/list")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        // İstek loglanır
        log.info("API İsteği: Tüm kullanıcıları listeleme");

        // Tüm kullanıcılar alınır
        List<UserDto> users = userService.getAllUsers();

        // Sonuç loglanır
        log.info("API Yanıtı: {} kullanıcı listelendi", users.size());

        // HTTP 200 OK ile kullanıcı listesi döndürülür
        return ResponseEntity.ok(users);
    }

    // PUT /rest/api/users/update/{id} endpoint'i
    // Belirtilen ID'ye sahip kullanıcıyı günceller
    @PutMapping("/update/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        // Güncelleme isteği loglanır
        log.info("API İsteği: Kullanıcı güncelleme - ID={}, Ad={}, Soyad={}, Tarih={}",
                id, userDto.getName(), userDto.getSurname(), userDto.getDate());

        // Kullanıcı güncellenir
        UserDto updatedUser = userService.updateUser(id, userDto);

        if (updatedUser != null) {
            // Başarılı güncelleme loglanır
            log.info("API Yanıtı: Kullanıcı güncellendi - ID={}", updatedUser.getId());
            return ResponseEntity.ok(updatedUser);  // HTTP 200 OK
        }

        // Kullanıcı bulunamadı durumu loglanır
        log.warn("API Yanıtı: Kullanıcı bulunamadı - ID={}", id);
        return ResponseEntity.notFound().build();  // HTTP 404 Not Found
    }

    // DELETE /rest/api/users/delete/{id} endpoint'i
    // Belirtilen ID'ye sahip kullanıcıyı siler
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        // Silme isteği loglanır
        log.info("API İsteği: Kullanıcı silme - ID={}", id);

        // Kullanıcı silinir
        userService.deleteUser(id);

        // Başarılı silme loglanır
        log.info("API Yanıtı: Kullanıcı silindi - ID={}", id);

        // HTTP 200 OK döndürülür
        return ResponseEntity.ok().build();
    }
}