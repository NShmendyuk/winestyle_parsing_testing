package com.winelab.test.service;

import com.opencsv.CSVWriter;
import com.winelab.test.dto.WineDto;
import com.winelab.test.model.Wine;
import com.winelab.test.repository.WineRepository;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    @Override
    public void toCsvFile() throws IOException {
        String csv = "data.csv";
        CSVWriter writer = new CSVWriter(new FileWriter(csv));
        List<Wine> wines = getAllWines();
        boolean includeHeaders = true;
        try {
            writer.writeAll((ResultSet) wines, includeHeaders); //writer is instance of CSVWriter
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
