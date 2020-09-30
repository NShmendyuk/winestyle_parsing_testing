package com.winelab.test.service;

import com.winelab.test.dto.WineDto;
import com.winelab.test.model.Wine;
import com.winelab.test.repository.WineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WineService implements IWineService {

    WineRepository wineRepository;

    public WineService(WineRepository wineRepository){
        this.wineRepository = wineRepository;
    }

    @Override
    public Wine getWineByName(String name){
        return wineRepository.findByName(name);
    }

    @Override
    public List<Wine> getAllWines(){
        return wineRepository.findAll();
    }

    @Override
    public Wine add(WineDto wineDto){
        Wine wine = new Wine();
        wine.setUrl(wineDto.getUrl());
        wine.setImageUrl(wineDto.getImageUrl());
        wine.setTastingNotes(wineDto.getTastingNotes());
        if (wineDto.getName() == null) {
            wine.setName("noName");
        } else
            wine.setName(wineDto.getName());
        if (wineDto.getBrand() == null) {
            wine.setBrand("noBrand");
        } else
            wine.setBrand(wineDto.getBrand());
        if (wineDto.getColor() == null) {
            wine.setColor("noColor");
        } else
            wine.setColor(wineDto.getColor());
        if (wineDto.getRegion() == null) {
            wine.setRegion("noRegion");
        } else
            wine.setRegion(wineDto.getRegion());
        if (wineDto.getVolume() == null) {
            wine.setVolume("noVolume");
        } else
            wine.setVolume(wineDto.getVolume());
        if (wineDto.getStrength() == null) {
            wine.setStrength("noStrength");
        } else
            wine.setStrength(wineDto.getStrength());
        if (wineDto.getSugar() == null) {
            wine.setSugar("noSugar");
        } else
            wine.setSugar(wineDto.getSugar());
        if (wineDto.getPrice() == null) {
            wine.setPrice("noPrice");
        } else
            wine.setPrice(wineDto.getPrice());
        if (wineDto.getGrape() == null) {
            wine.setGrape("noGrape");
        } else
        wine.setGrape(wineDto.getGrape());
        if (wineDto.getYear() == null){
            wine.setYear(0L);
        } else
            wine.setYear(wineDto.getYear());
        try{
            wineRepository.save(wine);
        } catch(Exception ex){
            System.out.println(wine.toString() +"\n Error:\n"); //TODO: log.error();
            ex.printStackTrace();
        }

        return wine;
    }
}
