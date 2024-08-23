package com.impact.pokemon.service;

import com.impact.pokemon.model.PokemonModel;
import com.impact.pokemon.model.SimulationModel;
import org.springframework.stereotype.Service;

@Service
public interface SimulationService {

    public SimulationModel startSimulation(final PokemonModel pokemon1, final PokemonModel pokemon2);

}
