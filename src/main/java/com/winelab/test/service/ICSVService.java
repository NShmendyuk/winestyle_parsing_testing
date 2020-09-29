package com.winelab.test.service;

import com.winelab.test.repository.WineRepository;

import java.io.IOException;

public interface ICSVService {
    void toCsvFile(WineRepository wineRepository) throws IOException;
}