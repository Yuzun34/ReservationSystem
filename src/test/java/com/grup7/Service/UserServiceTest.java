package com.grup7.Service;

import com.grup7.Dto.UserDto;
import com.grup7.Entity.Table;
import com.grup7.Entity.User;
import com.grup7.Exception.ValidationException;
import com.grup7.Repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * UserService sınıfı için unit testler.
 * Bu test sınıfı, kullanıcı ekleme işleminin doğru çalıştığını
 * doğrulamak için oluşturulmuştur.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private TableService tableService;

    @InjectMocks
    private UserService userService;

    private UserDto validUserDto;
    private Table availableTable;
    private LocalDate validDate;

    @BeforeEach
    void setUp() {
        // Geçerli bir tarih (bugünden bir gün sonrası)
        validDate = LocalDate.now().plusDays(1);

        // Geçerli bir kullanıcı DTO'su oluştur
        validUserDto = new UserDto();
        validUserDto.setName("Ahmet");
        validUserDto.setSurname("Yılmaz");
        validUserDto.setDate(validDate);

        // Kullanılabilir bir masa oluştur
        availableTable = new Table();
        availableTable.setId(1L);
        availableTable.setTableNumber(String.valueOf(5));
        availableTable.setReservedDates(new ArrayList<>());
    }

    /**
     * Geçerli bilgilerle kullanıcı ekleme testini gerçekleştirir.
     * Başarılı bir kullanıcı ekleme senaryosunu test eder.
     */
    @Test
    void testAddUserSuccess() {
        // Mock davranışlarını ayarla
        List<Table> availableTables = List.of(availableTable);
        when(tableService.getAvailableTables(eq(validDate))).thenReturn(availableTables);
        when(tableService.reserveTable(eq(1L), eq(validDate))).thenReturn(true);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Ahmet");
        savedUser.setSurname("Yılmaz");
        savedUser.setDate(validDate);
        savedUser.setReservationCode("RZ53-123456789a");
        savedUser.setReservedTable(availableTable);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Test
        UserDto result = userService.addUser(validUserDto);

        // Doğrulama
        assertNotNull(result);
        assertEquals("Ahmet", result.getName());
        assertEquals("Yılmaz", result.getSurname());
        assertEquals(validDate, result.getDate());
        assertNotNull(result.getReservationCode());
        assertNotNull(result.getReservedTable());
        assertEquals(String.valueOf(5), result.getReservedTable().getTableNumber());
    }

    /**
     * Boş isimle kullanıcı ekleme testini gerçekleştirir.
     * Validasyon hatası beklenir.
     */
    @Test
    void testAddUserWithEmptyName() {
        // Geçersiz kullanıcı DTO'su oluştur (boş isim)
        UserDto invalidUserDto = new UserDto();
        invalidUserDto.setName("");
        invalidUserDto.setSurname("Yılmaz");
        invalidUserDto.setDate(validDate);

        // Test ve doğrulama
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userService.addUser(invalidUserDto);
        });

        assertEquals("İsim alanı boş bırakılamaz", exception.getMessage());
    }

    /**
     * Geçmiş tarihle kullanıcı ekleme testini gerçekleştirir.
     * Validasyon hatası beklenir.
     */
    @Test
    void testAddUserWithPastDate() {
        // Geçersiz kullanıcı DTO'su oluştur (geçmiş tarih)
        UserDto invalidUserDto = new UserDto();
        invalidUserDto.setName("Ahmet");
        invalidUserDto.setSurname("Yılmaz");
        invalidUserDto.setDate(LocalDate.now().minusDays(1));

        // Test ve doğrulama
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userService.addUser(invalidUserDto);
        });

        assertEquals("Rezervasyon için geçmiş tarih seçilemez", exception.getMessage());
    }

    /**
     * Uygun masa bulunmadığı durumda kullanıcı ekleme testini gerçekleştirir.
     * Validasyon hatası beklenir.
     */
    @Test
    void testAddUserWithNoAvailableTables() {
        // Mock davranışı ayarla: Boş masa listesi döndür
        when(tableService.getAvailableTables(eq(validDate))).thenReturn(new ArrayList<>());

        // Test ve doğrulama
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userService.addUser(validUserDto);
        });

        assertEquals("Belirtilen tarih için uygun masa bulunmamaktadır", exception.getMessage());
    }
}