package com.example.caminante135.demo01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.caminante135.demo01.models.Pokemon;
import com.example.caminante135.demo01.models.PokemonRespuesta;
import com.example.caminante135.demo01.pokeapi.PokeapiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//Gson se utilizar para que devuelva la información en Objetos
// Retrofit es una libreria que nos permite consumir api's rest
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "POKEDEX";
    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private ListaPokemonAdapter listaPokemonAdapter;
    private int offset;

    private boolean aptoParaCargar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        listaPokemonAdapter = new ListaPokemonAdapter(this);
        recyclerView.setAdapter(listaPokemonAdapter);
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy>0){
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstCompletelyVisibleItemPosition();

                    if(aptoParaCargar){
                        if((visibleItemCount + pastVisibleItems) >= totalItemCount){
                            Log.i(TAG,"Llegamos al final.");
                            aptoParaCargar = false;
                            offset += 20;
                            obtenerDatos(offset);
                        }
                    }
                }
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl("http://pokeapi.co/api/v2/") // URL base de Api
                .addConverterFactory(GsonConverterFactory.create()) // Como formatear Respuestar Que lleguen en JSON
                .build();
        aptoParaCargar = true;
        offset =0;
        obtenerDatos(offset);
    }

    private void obtenerDatos(int offset) {
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<PokemonRespuesta> pokemonRespuestaCall = service.obtenerListaPokemon(20, offset);

        pokemonRespuestaCall.enqueue(new Callback<PokemonRespuesta>() {
            @Override
            // onResponse cuando llega la respuesta a la consulta que se realiza
            public void onResponse(Call<PokemonRespuesta> call, Response<PokemonRespuesta> response) {
                aptoParaCargar = true ;
                if(response.isSuccessful()){

                    PokemonRespuesta pokemonRespuesta = response.body();
                    ArrayList<Pokemon> listaPokemon = pokemonRespuesta.getResults();

                    listaPokemonAdapter.adicionarListaPokemon(listaPokemon);

                }else{
                    Log.e(TAG, "onResponse" + response.body());
                }
            }

            @Override
            //onFailure cuando ocurre algun tipo de problema (no hay conexion de internet, cumplio tiempo de espera u otro)
            public void onFailure(Call<PokemonRespuesta> call, Throwable t) {
                aptoParaCargar = true ;
                Log.e(TAG, "onFailure" + t.getMessage());
            }
        });
    }


}
