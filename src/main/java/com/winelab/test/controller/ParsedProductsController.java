package com.winelab.test.controller;

import com.winelab.test.model.Wine;
import com.winelab.test.repository.WineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller of the service
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/parse/json")
public class ParsedProductsController {
    public final WineRepository wineRepository;

    //TODO: возвращать распаршенные записи по конкретной ссылке
    //TODO: возвращать только запрашиваемые столбцы
    @GetMapping
    public ResponseEntity<List<Wine>> getParsedWine() {
        List<Wine> parsedWine = wineRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(parsedWine);
    }
}