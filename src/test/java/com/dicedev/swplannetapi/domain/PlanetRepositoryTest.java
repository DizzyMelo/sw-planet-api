package com.dicedev.swplannetapi.domain;

import static com.dicedev.swplannetapi.common.PlanetConstants.EMPTY_PLANET;
import static com.dicedev.swplannetapi.common.PlanetConstants.INVALID_PLANET;
import static com.dicedev.swplannetapi.common.PlanetConstants.PLANET;
import static com.dicedev.swplannetapi.common.PlanetConstants.TATOOINE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Example;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
public class PlanetRepositoryTest {

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @AfterEach
    void teadDown() {
        PLANET.setId(null);
    }

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {
        Planet planet = planetRepository.save(PLANET);
        Planet sut = testEntityManager.find(Planet.class, planet.getId());

        assertThat(sut).isNotNull();
        assertThat(sut.getName()).isEqualTo(PLANET.getName());
        assertThat(sut.getClimate()).isEqualTo(PLANET.getClimate());
        assertThat(sut.getTerrain()).isEqualTo(PLANET.getTerrain());
    }

    @Test
    public void createPlanet_WithInvalidData_ThrowsException() {
        assertThatThrownBy(() -> planetRepository.save(EMPTY_PLANET)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> planetRepository.save(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void createPlanet_WithExistingName_ThrowsException() {
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        testEntityManager.detach(planet);
        planet.setId(null);

        assertThatThrownBy(
                () -> planetRepository.save(planet))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanetById_ByExistingId_ReturnsPlanet() {
        Planet planet = testEntityManager.persistFlushFind(PLANET);

        Optional<Planet> sut = planetRepository.findById(planet.getId());
        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(planet);
    }

    @Test
    public void getPlanetById_ByUnexistingId_ReturnsEmpty() {
        Optional<Planet> sut = planetRepository.findById(1L);
        assertThat(sut).isEmpty();
    }

    @Test
    public void getPlanetByName_ByExistingId_ReturnsPlanet() {
        Planet planet = testEntityManager.persistFlushFind(PLANET);

        Optional<Planet> sut = planetRepository.findByName(planet.getName());
        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(planet);
    }

    @Test
    public void getPlanetByName_ByUnexistingId_ReturnsEmpty() {
        Optional<Planet> sut = planetRepository.findById(1L);
        assertThat(sut).isEmpty();
    }

    @Test
    @Sql("/import_planets.sql")
    public void getPlanets_ReturnsFilteredPlanets() {
        Example<Planet> queryWithoutFilters = QueryBuilder.makeQuery(new Planet());
        Example<Planet> queryWithFilters = QueryBuilder.makeQuery(new Planet(TATOOINE.getClimate(), TATOOINE.getTerrain()));

        List<Planet> sutWithoutFilters = planetRepository.findAll(queryWithoutFilters);
        List<Planet> sutWithFilters = planetRepository.findAll(queryWithFilters);

        assertThat(sutWithoutFilters).isNotEmpty();
        assertThat(sutWithoutFilters).hasSize(3);

        assertThat(sutWithFilters).isNotEmpty();
        assertThat(sutWithFilters).hasSize(1);
        assertThat(sutWithFilters.get(0)).isEqualTo(TATOOINE);
    }

    @Test
    public void getPlanets_ReturnsNoPlanet() {
        Example<Planet> query = QueryBuilder.makeQuery(new Planet("unexisting climate", "unexisting desert"));

        Iterable<Planet> sut = planetRepository.findAll(query);

        assertThat(sut).isNotEmpty();
        assertThat(sut).hasSize(1);
    }
}
