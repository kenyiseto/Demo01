package com.example.caminante135.demo01.pokeapi;

import com.example.caminante135.demo01.models.PokemonRespuesta;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// La forma como usar retrofit es crear una Interface de JAVA en la cual pondremos todos los metodos
// que podemos llegar a usar para interactual con API

public interface PokeapiService {

    @GET("pokemon")
    Call<PokemonRespuesta> obtenerListaPokemon(@Query("limit")int limit, @Query("offset")int offset);
}
