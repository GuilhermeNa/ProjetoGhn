package br.com.transporte.AppGhn.ui.fragment.media.helpers;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.ui.adapter.MediaAdapter_Cavalos;

public class MediaAdapterCavaloHelper {
    private final Context context;
    private final List<Cavalo> listaDeCavalosDoAdapter;
    private final Cavalo primeiroCavaloExibido;
    private final RecyclerView recyclerDeCavalos;
    private AdapterCallback adapterCallback;
    private MediaAdapter_Cavalos adapter;

    public void setAdapterCallback(AdapterCallback adapterCallback) {
        this.adapterCallback = adapterCallback;
    }

    public MediaAdapterCavaloHelper(Context context, List<Cavalo> listaDeCavalosDoAdapter, Cavalo primeiroCavaloExibido, RecyclerView recyclerDeCavalos) {
        this.context = context;
        this.listaDeCavalosDoAdapter = listaDeCavalosDoAdapter;
        this.primeiroCavaloExibido = primeiroCavaloExibido;
        this.recyclerDeCavalos = recyclerDeCavalos;
    }

    //----------------------------------------------------------------------------------------------
    //                                          Configuracao                                      ||
    //----------------------------------------------------------------------------------------------

    public void configuraRecyclerDeCavalos() {
        configuraAdapter();
        configuraLayoutManager();
        configuraAdapterClickListener();
        adapter.setCavaloArmazenado(primeiroCavaloExibido);
    }

    private void configuraAdapter() {
        adapter = new MediaAdapter_Cavalos(context, listaDeCavalosDoAdapter);
        recyclerDeCavalos.setAdapter(adapter);
    }

    private void configuraLayoutManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerDeCavalos.setLayoutManager(layoutManager);
    }

    private void configuraAdapterClickListener() {
        adapter.setOnItemClickListener((cavalo, posicao) -> {
            adapter.atualizaRecyclerACadaNovaBusca(posicao);
            adapterCallback.onItemClick(cavalo);
        });
    }

    //----------------------------------------------------------------------------------------------
    //                                          Metodos Publicos                                  ||
    //----------------------------------------------------------------------------------------------

    public List<Cavalo> getAdapterDataSet(){
        return adapter.getDataSet();
    }

    public void atualizaAdapterDataSetPorSearch (List<Cavalo> dataSet_SearchView){
        adapter.atualiza(dataSet_SearchView);
    }

    public interface AdapterCallback {
        void onItemClick(Cavalo cavaloClicado);
    }

}
