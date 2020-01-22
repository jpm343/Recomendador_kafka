package com.grupo.uno.backend.controller;

import com.grupo.uno.backend.model.Drink;
import com.grupo.uno.backend.repository.DrinkRepository;
import com.datastax.driver.core.utils.UUIDs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Drink")
public class DrinkController {
    //dependency injection
    @Autowired
    DrinkRepository drinkRepository;

    //get drinks
    @GetMapping("/")
    public ResponseEntity<List<Drink>> getDrinks(){
        return ResponseEntity.ok(drinkRepository.findAll());
    }

    //post drinks
    @PostMapping("/")
    public Drink createDrink(Drink drink){
        drink.setId(UUIDs.timeBased());
        return drinkRepository.save(drink);
    }
}
