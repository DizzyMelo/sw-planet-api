package com.dicedev.swplannetapi.domain;

import org.springframework.stereotype.Service;

@Service
public class PlanetService {
    
    private PlanetRepository planetRepository;

    public PlanetService(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    public Planet createPlanet(Planet planet) {
        return planetRepository.save(planet);
    }
}
