package com.example.beerapi.controller;

import com.example.beerapi.model.Beer;
import com.example.beerapi.service.BeerService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/beers")
public class BeerController {

    private final BeerService beerService;

    public BeerController(BeerService beerService) {
        this.beerService = beerService;
    }

    @GetMapping
    public List<Beer> getAllBeers() {
        return beerService.getAllBeers();
    }

    @GetMapping("/top-rated")
    public List<Beer> getTopRatedBeers() {
        return beerService.getBeersWithRatingAbove(4.5);
    }
}
