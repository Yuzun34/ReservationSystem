package com.grup7.Controller;

import com.grup7.Entity.Table;
import com.grup7.Service.TableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/tables")
@Slf4j
public class TableController {

    @Autowired
    private TableService tableService;

    // GET /rest/api/tables endpoint'i
    // Sistemdeki tüm masaları listeler
    @GetMapping
    public List<Table> getAllTables() {
        // İstek başlangıcı loglanır
        log.info("API İsteği: Tüm masaları listeleme");

        // Servis üzerinden tüm masalar alınır
        List<Table> tables = tableService.getAllTables();

        // Sonuç loglanır
        log.info("API Yanıtı: {} masa listelendi", tables.size());

        // Masa listesi döndürülür
        return tables;
    }
}