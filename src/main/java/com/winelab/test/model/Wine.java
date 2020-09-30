package com.winelab.test.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Data
public class Wine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String url;

    @Column
    private String imageUrl;

    @Column
    private String name;

    @Column
    private Long year;

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

    @Column(columnDefinition="text")
    private String tastingNotes;

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
