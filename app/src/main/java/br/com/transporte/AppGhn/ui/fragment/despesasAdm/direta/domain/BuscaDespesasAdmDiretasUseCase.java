package br.com.transporte.AppGhn.ui.fragment.despesasAdm.direta.domain;

import static br.com.transporte.AppGhn.model.enums.TipoDespesa.DIRETA;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.TAG;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import java.time.LocalDate;
import java.util.List;

import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.DespesaAdmRepository;
import br.com.transporte.AppGhn.ui.fragment.despesasAdm.direta.domain.model.DespesaAdmDiretaObject;

public class BuscaDespesasAdmDiretasUseCase {
    private final CavaloRepository cavaloRepository;
    private final DespesaAdmRepository despesaAdmRepository;
    private final LifecycleOwner lifecycleOwner;

    public BuscaDespesasAdmDiretasUseCase(
            Context context,
            LifecycleOwner lifecycleOwner
    ) {
        this.cavaloRepository = new CavaloRepository(context);
        this.despesaAdmRepository = new DespesaAdmRepository(context);
        this.lifecycleOwner = lifecycleOwner;
    }

    public interface DespesaAdmDiretaObjectsUseCaseCallback {

        void finalizaMapper(List<DespesaAdmDiretaObject> dataSet);
    }

    //----------------------------------------------------------------------------------------------
    public void buscaDataRelacionadaAsDespesasAdmDiretas(
            final LocalDate dataInicial,
            final LocalDate dataFinal,
            final DespesaAdmDiretaObjectsUseCaseCallback callback
    ) {
        cavaloRepository.buscaCavalos().observe(lifecycleOwner,
                cavalos -> {
                    if (cavalos.getDado() != null) {
                        buscaListaDeDespesasAdmDiretamenteLigadasAosCavalos(cavalos.getDado(), dataInicial, dataFinal, callback);
                    }
                });
    }

    private void buscaListaDeDespesasAdmDiretamenteLigadasAosCavalos(
            final List<Cavalo> cavalos,
            final LocalDate dataInicial,
            final LocalDate dataFinal,
            final DespesaAdmDiretaObjectsUseCaseCallback callback
    ) {
        despesaAdmRepository.buscaPorTipoEData(
                DIRETA,
                dataInicial,
                dataFinal
        ).observe(lifecycleOwner,
                despesasAdm -> {
                    if (despesasAdm != null) {
                        mapeiaListaDeDespesaAdmObjetosComDataRelevanteParaUi(cavalos, despesasAdm, callback);
                    }
                });
    }

    private static void mapeiaListaDeDespesaAdmObjetosComDataRelevanteParaUi(
            final List<Cavalo> cavalos,
            final List<DespesaAdm> despesasAdm,
            @NonNull final DespesaAdmDiretaObjectsUseCaseCallback callback
    ) {
        final DespesaAdmDiretaMapper mapper =
                new DespesaAdmDiretaMapper(cavalos, despesasAdm);

        final List<DespesaAdmDiretaObject> listaDespesaAdmObjetos
                = mapper.geraListaDeDespesaAdmObjetos();

        callback.finalizaMapper(listaDespesaAdmObjetos);
    }


    public void solicitaAtualizacaoDeDados(
            final LocalDate dataInicial,
            final LocalDate dataFinal
    ) {
        despesaAdmRepository.buscaPorTipoEData(DIRETA, dataInicial, dataFinal);
    }
}


