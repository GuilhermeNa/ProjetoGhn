package br.com.transporte.AppGhn.ui.fragment.manutencao;

import androidx.lifecycle.LifecycleOwner;

import java.util.List;

import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.ui.viewmodel.ManutencaoDetalhesViewModel;

public class ManutencaoViewModelObserverHelper {
    private final ManutencaoDetalhesViewModel viewModel;
    private final LifecycleOwner lifecycleOwner;

    public ManutencaoViewModelObserverHelper(ManutencaoDetalhesViewModel viewModel, LifecycleOwner lifecycleOwner) {
        this.viewModel = viewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    public interface ObserverCallback {
        void getCavalo(Cavalo cavalo);
        void getMotorista(Motorista motorista);
        void dataSet_primeiraExibicaoDeDados(List<CustosDeManutencao> dataSet);
        void dataSet_falhaAoCarregar(String erro);

    }

    //----------------------------------------------------------------------------------------------

    public void run(
            final long cavaloId,
            final ObserverCallback callback
    ) {
        buscaCavaloSelecionado(cavaloId, callback);
    }

    private void buscaCavaloSelecionado(
            long cavaloId,
            final ObserverCallback callback
    ) {
        viewModel.localizaCavalo(cavaloId).observe(lifecycleOwner,
                cavalo -> {
                    if (cavalo != null) {
                        callback.getCavalo(cavalo);
                        buscaMotorista(cavalo, callback);
                        buscaManutencaoRelacionadaAEsteCavalo(cavalo.getId(), callback);
                    }
                });
    }

    private void buscaMotorista(
            final Cavalo cavalo,
            final ObserverCallback callback
    ) {
        if(cavalo.getRefMotoristaId() != null){
            viewModel.localizaMotorista(cavalo.getRefMotoristaId()).observe(lifecycleOwner,
                    motorista -> {
                        if (motorista != null) {
                            callback.getMotorista(motorista);
                        }
                    });
        } else callback.getMotorista(null);
    }


    private void buscaManutencaoRelacionadaAEsteCavalo(
            final Long id,
            final ObserverCallback callback
    ) {
        viewModel.buscaManutencaoPorCavaloId(id).observe(lifecycleOwner,
                resource -> {
                    if (resource.getDado() != null) {
                        viewModel.setDataSet_base(resource.getDado());
                        List<CustosDeManutencao> listaFiltrada = viewModel.filtraPorData();
                        callback.dataSet_primeiraExibicaoDeDados(listaFiltrada);
                    } else {
                        callback.dataSet_falhaAoCarregar(resource.getErro());
                    }
                });
    }

}
