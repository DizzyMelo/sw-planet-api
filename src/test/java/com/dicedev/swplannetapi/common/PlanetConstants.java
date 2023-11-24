package com.dicedev.swplannetapi.common;

import java.util.List;

import com.dicedev.swplannetapi.domain.Planet;

public class PlanetConstants {

    public static final Planet PLANET = new Planet("name", "climate", "terrain");
    public static final Planet INVALID_PLANET = new Planet("", "", "");
    public static final Planet EMPTY_PLANET = new Planet();
    public static final Planet TATOOINE = new Planet("Tatooine", "arid", "desert");
    public static final Planet ALDERAAN = new Planet("Alderaan", "temperate", "grassland, mountains");
    public static final Planet YAVIN_IV = new Planet("Yavin IV", "temperate", "jungle, rainforest");

    public static final List<Planet> PLANETS = List.of(TATOOINE, ALDERAAN, YAVIN_IV);
    
}
