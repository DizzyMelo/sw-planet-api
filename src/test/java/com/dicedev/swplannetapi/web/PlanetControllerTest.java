package com.dicedev.swplannetapi.web;

import static com.dicedev.swplannetapi.common.PlanetConstants.EMPTY_PLANET;
import static com.dicedev.swplannetapi.common.PlanetConstants.INVALID_PLANET;
import static com.dicedev.swplannetapi.common.PlanetConstants.PLANET;
import static com.dicedev.swplannetapi.common.PlanetConstants.PLANETS;
import static com.dicedev.swplannetapi.common.PlanetConstants.TATOOINE;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.dicedev.swplannetapi.domain.PlanetService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PlanetController.class)
public class PlanetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlanetService planetServiceMock;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createPlanet_WithValidData_ReturnsCreated() throws Exception {
        when(planetServiceMock.createPlanet(PLANET)).thenReturn(PLANET);
        mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(PLANET)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    public void createPlanet_WithInvalidData_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(EMPTY_PLANET))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());

        mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(INVALID_PLANET))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createPlanet_WithExistingName_ReturnsConflic() throws Exception{
        when(planetServiceMock.createPlanet(any())).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(PLANET)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict());
    }

    @Test
    public void getPlanetById_WithExistingId_ReturnsPlanet() throws Exception {
        when(planetServiceMock.getPlanetById(1L)).thenReturn(Optional.of(PLANET));

        mockMvc.perform(get("/planets/{id}", 1L))
            .andExpect(jsonPath("$").value(PLANET))
            .andExpect(status().isOk());
    }

    @Test
    public void getPlanetById_WithExistingId_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/planets/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPlanetByName_ByExistingName_ReturnsPlanet() throws Exception {
        when(planetServiceMock.getPlanetByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

        mockMvc.perform(get("/planets/name/{name}", PLANET.getName()))
            .andExpect(jsonPath("$").value(PLANET))
            .andExpect(status().isOk());
    }

    @Test
    public void getPlanetByName_ByUnexistingName_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/planets/name/{name}", ""))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPlanets_ReturnsFilteredPlanets() throws Exception {
        when(planetServiceMock.getPlanets(null, null)).thenReturn(PLANETS);
        when(planetServiceMock.getPlanets(TATOOINE.getClimate(), TATOOINE.getTerrain())).thenReturn(List.of(TATOOINE));

        mockMvc.perform(get("/planets/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        mockMvc.perform(get("/planets/").param("climate", TATOOINE.getClimate()).param("terrain", TATOOINE.getTerrain()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").value(TATOOINE));
    }

    @Test
    public void getPlanets_ReturnsNoPlanet() throws Exception {
        when(planetServiceMock.getPlanets(anyString(), anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/planets/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void removePlanet_WithExistingId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/planets/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void removePlanet_WithUnexistingId_ReturnsNotFound() throws Exception {
        doThrow(InvalidDataAccessApiUsageException.class).when(planetServiceMock).removePlanet(anyLong());
        mockMvc.perform(delete("/planets/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}