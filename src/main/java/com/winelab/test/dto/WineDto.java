package com.winelab.test.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class WineDto implements Serializable {
    private String name;
    private String brand;
    private String color;
    private String region;
    private String volume;
    private String strength;
    private String sugar;
    private String price;
    private String grape;
}
