package br.com.transporte.AppGhn.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import br.com.transporte.AppGhn.dataSource.local.LocalSeguroVidaDataSource;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;

public class SeguroVidaRepository {
    private final LocalSeguroVidaDataSource localSource;

    public SeguroVidaRepository(Context context) {
        this.localSource = new LocalSeguroVidaDataSource(context);
    }

    //----------------------------------------------------------------------------------------------

    public LiveData<List<DespesaComSeguroDeVida>> buscaPorStatus(boolean isValido) {
        return localSource.buscaPorStatus(isValido);
    }


    public LiveData<DespesaComSeguroDeVida> localizaPeloId(final Long id) {
        return localSource.localizaPeloId(id);
    }
}
