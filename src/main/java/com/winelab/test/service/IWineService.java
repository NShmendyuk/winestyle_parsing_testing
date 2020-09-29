package com.winelab.test.service;

import com.winelab.test.dto.WineDto;
import com.winelab.test.model.Wine;

import java.util.List;

public interface IWineService {
    Wine add(WineDto wineDto);
    Wine getWineByName(String name);
    List<Wine> getAllWines();
}
