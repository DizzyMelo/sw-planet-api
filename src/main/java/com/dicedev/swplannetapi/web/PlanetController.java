package com.dicedev.swplannetapi.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dicedev.swplannetapi.domain.Planet;
import com.dicedev.swplannetapi.domain.PlanetService;

@RestController
@RequestMapping("/planets")
public class PlanetController {

    @Autowired
    private PlanetService planetService;

    @GetMapping("/")
    public ResponseEntity<Iterable<Planet>> getPlanets() {
        Iterable<Planet> planets = planetService.getPlanets();
        return ResponseEntity.ok(planets);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Planet> getPlanetById(@PathVariable(value = "id") Long id) {
        return planetService.getPlanetById(id).map(planet -> ResponseEntity.ok(planet))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Planet> getPlanetByName(@PathVariable(value = "name") String name) {
        return planetService.getPlanetByName(name).map(planet -> ResponseEntity.ok(planet))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Planet> create(@RequestBody Planet planet) {
        Planet planetCreated = planetService.createPlanet(planet);

        return ResponseEntity.status(HttpStatus.CREATED).body(planetCreated);
    }

}
