package com.grup7.Controller;

import com.grup7.Dto.OrderDto;
import com.grup7.Entity.Order;
import com.grup7.Entity.OrderLog;
import com.grup7.Service.OrderLogService;
import com.grup7.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/rest/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // POST /rest/api/orders/save endpoint'i
    // Yeni bir siparişi kategorileriyle birlikte kaydeder
    @PostMapping("/save")
    public ResponseEntity<Order> saveOrderWithCategories(@RequestBody OrderDto orderDto) {
        Order savedOrder = orderService.saveOrderWithCategories(orderDto);
        return ResponseEntity.ok(savedOrder);  // Kaydedilen siparişi HTTP 200 OK durum koduyla döndürür
    }

    // GET /rest/api/orders/{reservationCode} endpoint'i
    // Verilen rezervasyon koduna ait siparişin kategori isimlerini getirir
    @GetMapping("/{reservationCode}")
    public ResponseEntity<List<String>> getCategoryNamesByReservationCode(@PathVariable String reservationCode) {
        List<String> categoryNames = orderService.getCategoryNamesByReservationCode(reservationCode);
        return ResponseEntity.ok(categoryNames);  // Kategori isimlerini HTTP 200 OK durum koduyla döndürür
    }
}