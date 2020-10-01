package com.winelab.test.controller;

import com.winelab.test.model.Wine;
import com.winelab.test.repository.WineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер, возвращающий результаты парсинга
 */
@RestController
@RequestMapping("api")
public class MainController {
    private final WineRepository wineRepository;

    @Autowired
    public MainController(WineRepository wineRepository) {
        this.wineRepository = wineRepository;
    }

    //TODO: возвращать распаршенные записи по конкретной ссылке
    //TODO: возвращать только запрашиваемые столбцы
    @GetMapping("/wine")
    public ResponseEntity<List<Wine>> getParsedWine() {
        List<Wine> parsedWine = wineRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(parsedWine);
    }
}