package com.winelab.test.entity;

import lombok.Data;

@Data
public class Wine {
    private String name;
    private String brand;
    private String color;
    private String region;
    private String volume;
    private String strength;
    private String sugar;
    private String price;
    private String grape;

    @Override
    public String toString(){
        return name + " "
                + color + " "
                + grape + " "
                + brand + " "
                + region + " "
                + volume + " "
                + strength + " "
                + sugar + " "
                + price;
    }
}
