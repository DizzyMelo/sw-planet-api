package com.dicedev.swplannetapi.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
}
