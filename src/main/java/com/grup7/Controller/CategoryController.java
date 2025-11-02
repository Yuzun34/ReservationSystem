package com.grup7.Controller;

import com.grup7.Entity.Category;
import com.grup7.Service.ExternalMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/rest/api")
@RequiredArgsConstructor
public class CategoryController {
    @Autowired
    private ExternalMenuService externalMenuService;

    // GET /rest/api/menu endpoint'i - Tüm kategorileri getirir
    @GetMapping("/menu")
    public List<Category> getCategory() {
        return externalMenuService.getCategories();  // Harici servisten kategorileri alır ve döndürür
    }
}