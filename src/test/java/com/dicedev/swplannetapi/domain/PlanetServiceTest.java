package com.dicedev.swplannetapi.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.dicedev.swplannetapi.common.PlanetConstants.PLANET;
import static com.dicedev.swplannetapi.common.PlanetConstants.INVALID_PLANET;;

@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {

    @InjectMocks
    private PlanetService planetService;

    @Mock
    private PlanetRepository planetRepositoryMock;

    // operation_state_return
    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {
        when(planetRepositoryMock.save(any(Planet.class))).thenReturn(new Planet(PLANET.getName(), PLANET.getClimate(), PLANET.getTerrain()));

        // System under test
        Planet sut = planetService.createPlanet(PLANET);

        assertThat(sut).isEqualTo(PLANET);
    }

    @Test
    public void createPlanet_WithInvalidData_ThrowsException() {
        when(planetRepositoryMock.save(INVALID_PLANET)).thenThrow(RuntimeException.class);
        assertThatThrownBy(() -> planetService.createPlanet(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanetById_ByExistingId_ReturnsPlanet() {
        when(planetRepositoryMock.findById(anyLong())).thenReturn(Optional.of(PLANET));

        Optional<Planet> sut = planetService.getPlanetById(1L);

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanetById_ByNonExistingId_ReturnsEmpty() {
        when(planetRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Planet> sut = planetService.getPlanetById(10L);

        assertThat(sut).isEmpty();
    }

    @Test
    public void getPlanetByName_ByExistingName_ReturnsPlanet() {
        when(planetRepositoryMock.findByName(anyString())).thenReturn(Optional.of(PLANET));

        Optional<Planet> sut = planetService.getPlanetByName("name");

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanetByName_ByNonExistingName_ReturnsEmpty() {
        when(planetRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());

        Optional<Planet> sut = planetService.getPlanetByName("no-name");

        assertThat(sut).isEmpty();
    }

    @Test
    public void getPlanets_WithNullClimateAndTerrain_ReturnsListOfPlanets() {
        Example<Planet> query = QueryBuilder.makeQuery(new Planet(PLANET.getClimate(), PLANET.getTerrain()));
        when(planetRepositoryMock.findAll(query)).thenReturn(List.of(PLANET));

        Iterable<Planet> sut = planetService.getPlanets(PLANET.getClimate(), PLANET.getTerrain());

        assertThat(sut).hasSize(1);
        assertThat(sut).contains(PLANET);
    }

    @Test
    public void getPlanets_WithUnexistingClimateAndTerrain_ReturnsEmpty() {
        when(planetRepositoryMock.findAll(any())).thenReturn(Collections.emptyList());

        Iterable<Planet> sut = planetService.getPlanets(PLANET.getClimate(), PLANET.getTerrain());

        assertThat(sut).isEmpty();
    }
}
