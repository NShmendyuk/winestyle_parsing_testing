package com.winelab.test.service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.winelab.test.dto.WineDto;
import com.winelab.test.model.Wine;
import com.winelab.test.repository.WineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
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
        try{
            wineRepository.save(wine);
        } catch(Exception ex){
            System.out.println(wine.toString() +"\n Error:\n"); //TODO: log.error();
            ex.printStackTrace();
        }

        return wine;
    }

    @Override
    public void toCsvFile() throws IOException {
        List<Wine> wines = wineRepository.findAll();
        PrintWriter writer = new PrintWriter("data.csv");

        ColumnPositionMappingStrategy<Wine> mapStrategy
                = new ColumnPositionMappingStrategy<>();

        mapStrategy.setType(Wine.class);

        String[] columns = new String[]{"id", "brand", "color",
                "grape", "name", "price", "region", "strength", "sugar", "volume"};
        mapStrategy.setColumnMapping(columns);

        StatefulBeanToCsv<Wine> winecsv = new StatefulBeanToCsvBuilder<Wine>(writer)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withMappingStrategy(mapStrategy)
                .withSeparator(';')
                .build();
        try {
            winecsv.write(wines);
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
        }
    }
}
