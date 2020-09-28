package com.winelab.test.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Wine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String brand;

    @Column
    private String color;

    @Column
    private String region;

    @Column
    private String volume;

    @Column
    private String strength;

    @Column
    private String sugar;

    @Column
    private String price;

    @Column
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
