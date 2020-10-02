package com.winelab.test.service;

import com.winelab.test.dto.WineDto;
import com.winelab.test.model.Wine;

import java.util.List;

public interface IWineService {
    Wine add(WineDto wineDto);
    Wine getWineByName(String name);
    Wine getWineByUrl(String url);
    Wine updatePrice(String price, String url);
    List<Wine> getAllWines();
}
