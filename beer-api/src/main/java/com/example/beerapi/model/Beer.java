package com.example.beerapi.model;

import lombok.Data;

@Data
public class Beer {
    private String price;
    private String name;
    private Rating rating;
    private String image;
    private int id;
}
