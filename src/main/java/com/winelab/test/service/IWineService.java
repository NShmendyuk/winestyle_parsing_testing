package com.winelab.test.service;

import com.winelab.test.dto.WineDto;
import com.winelab.test.model.Wine;

public interface IWineService {
    Wine add(WineDto wineDto);
    Wine getWineById(Long id);
    Wine getWineByName(Long id);
    Wine getAllWine();
}
