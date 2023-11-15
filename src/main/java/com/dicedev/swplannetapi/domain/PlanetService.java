package com.dicedev.swplannetapi.domain;

import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class PlanetService {
    
    private PlanetRepository planetRepository;

    public PlanetService(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    public Iterable<Planet> getPlanets(String climate, String terrain) {
        Example<Planet> query = QueryBuilder.makeQuery(new Planet(climate, terrain));
        return planetRepository.findAll(query);
    }

    public Optional<Planet> getPlanetById(Long id) {
        return planetRepository.findById(id);
    }

    public Optional<Planet> getPlanetByName(String name) {
        return planetRepository.findByName(name);
    }

    public Planet createPlanet(Planet planet) {
        return planetRepository.save(planet);
    }
}
