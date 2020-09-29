package com.winelab.test.service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.winelab.test.model.Wine;
import com.winelab.test.repository.WineRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CSVService implements ICSVService {
    public void toCsvFile(WineRepository wineRepository) throws IOException {
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
