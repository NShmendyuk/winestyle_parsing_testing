package com.winelab.test.service;

import com.winelab.test.dto.WineDto;
import com.winelab.test.model.Wine;
import com.winelab.test.repository.WineRepository;
import org.springframework.stereotype.Service;

@Service
public class WineService implements IWineService {

    WineRepository wineRepository;

    public WineService(WineRepository wineRepository){
        this.wineRepository = wineRepository;
    }

    @Override
    public Wine getWineById(Long id){
        return null;
    }

    @Override
    public Wine getWineByName(Long id){
        return null;
    }

    @Override
    public Wine getAllWine(){
        return null;
    }

    @Override
    public Wine add(WineDto wineDto){
        Wine wine = new Wine();
        wine.setName(wineDto.getName());
        wine.setBrand(wineDto.getBrand());
        wine.setColor(wineDto.getColor());
        wine.setRegion(wineDto.getRegion());
        wine.setVolume(wineDto.getVolume());
        wine.setStrength(wineDto.getStrength());
        wine.setSugar(wineDto.getSugar());
        wine.setPrice(wineDto.getPrice());
        wine.setGrape(wineDto.getGrape());
        wineRepository.save(wine);
        return wine;
    }
}
