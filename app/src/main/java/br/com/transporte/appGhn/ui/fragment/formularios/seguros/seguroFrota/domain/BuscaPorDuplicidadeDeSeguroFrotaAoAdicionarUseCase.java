package br.com.transporte.appGhn.ui.fragment.formularios.seguros.seguroFrota.domain;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.List;
import java.util.Optional;

import br.com.transporte.appGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.appGhn.repository.SeguroFrotaRepository;
import br.com.transporte.appGhn.util.DevolveResultado;

public class BuscaPorDuplicidadeDeSeguroFrotaAoAdicionarUseCase {
    private final SeguroFrotaRepository repository;
    private final LifecycleOwner lifecycleOwner;

    public BuscaPorDuplicidadeDeSeguroFrotaAoAdicionarUseCase(
            Context context,
            LifecycleOwner lifecycleOwner
    ) {
        this.repository = new SeguroFrotaRepository(context);
        this.lifecycleOwner = lifecycleOwner;
    }

    //---------------------------------------------------------------------------------------------

    public void solicitaBusca(
            final long idDoCavaloRelacionadoAoSeguro,
            final DevolveResultado<Boolean> callback
    ) {
        buscaPorTodosOsSegurosFrotaValidosEAtivosNoBd(
                segurosAtivos -> {
                    final boolean temSeguroDuplicado =
                            verificarSeJaTemosUmSeguroAtivoParaOCavaloQueEstaSendoInserido(segurosAtivos, idDoCavaloRelacionadoAoSeguro);
                    devolveResultado(temSeguroDuplicado, callback);
                });
    }

    private void buscaPorTodosOsSegurosFrotaValidosEAtivosNoBd(
            @NonNull final DevolveResultado<List<DespesaComSeguroFrota>> callback
    ) {
        final LiveData<List<DespesaComSeguroFrota>> observer =
                repository.buscaPorStatus(true);
        observer.observe(lifecycleOwner, new Observer<List<DespesaComSeguroFrota>>() {
                    @Override
                    public void onChanged(List<DespesaComSeguroFrota> listaSeguro) {
                        observer.removeObserver(this);
                        callback.devolveResultado(listaSeguro);
                    }
                });
    }

    private boolean verificarSeJaTemosUmSeguroAtivoParaOCavaloQueEstaSendoInserido(
            @NonNull final List<DespesaComSeguroFrota> listaDeSegurosValidos,
            final long cavaloId
    ) {
        final Optional<DespesaComSeguroFrota> seguroDuplicado = listaDeSegurosValidos.stream()
                .filter(s -> s.isValido() && s.getRefCavaloId() == cavaloId).findAny();
        return seguroDuplicado.isPresent();
    }

    private void devolveResultado(
            final boolean seguroDuplicado,
            @NonNull final DevolveResultado<Boolean> callback
    ) {
        callback.devolveResultado(seguroDuplicado);
    }

}
