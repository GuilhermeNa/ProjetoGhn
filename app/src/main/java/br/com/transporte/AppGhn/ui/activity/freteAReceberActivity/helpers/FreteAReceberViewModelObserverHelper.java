package br.com.transporte.AppGhn.ui.activity.freteAReceberActivity.helpers;

import androidx.lifecycle.LifecycleOwner;

import br.com.transporte.AppGhn.ui.viewmodel.FreteAReceberActViewModel;

public class FreteAReceberViewModelObserverHelper {
    private boolean aguardandoPrimeiraAtualizacaoDeUi = true;
    private final LifecycleOwner lifecycleOwner;
    private final FreteAReceberActViewModel viewModel;
    private ObserverHelperCallback callback;
    private PrimeiraAtualizacao primeiraAtt;
    private DemaisAtualizacoes demaisAtt;

    public FreteAReceberViewModelObserverHelper(LifecycleOwner lifecycleOwner, FreteAReceberActViewModel viewModel) {
        this.lifecycleOwner = lifecycleOwner;
        this.viewModel = viewModel;
    }

    public void setCallback(ObserverHelperCallback callback) {
        this.callback = callback;
    }

    public interface ObserverHelperCallback {
        void notificaAtualizacao();

        void dadosPreparadosParaInicializacao();
    }

    //----------------------------------------------------------------------------------------------

    public void configuraObservers() {
        primeiraAtt = new PrimeiraAtualizacao();
        demaisAtt = new DemaisAtualizacoes();
        observerDataCavalo();
        observerDataFrete();
        observerDataRecebimentoFrete();
    }

    //----------------------------------------------------------------------------------------------

    private void observerDataCavalo() {
        viewModel.buscaCavalos().observe(lifecycleOwner,
                resourceCavalo -> {
                    if (resourceCavalo.getDado() != null) {
                        viewModel.setDataSet_cavalo(resourceCavalo.getDado());
                        primeiraAtt_notificaQueCavaloEstaCarregado();
                    }
                });
    }

    private void primeiraAtt_notificaQueCavaloEstaCarregado() {
        if (aguardandoPrimeiraAtualizacaoDeUi) {
            primeiraAtt.setCavaloCarregado(true);
            if (primeiraAtt.todosOsDadosProntos()) {
                callback.dadosPreparadosParaInicializacao();
                aguardandoPrimeiraAtualizacaoDeUi = false;
            }
        }
    }

    //----------------------------------------------------------------------------------------------

    private void observerDataFrete() {
        viewModel.buscaFretes().observe(lifecycleOwner,
                resourceFrete -> {
                    if (resourceFrete.getDado() != null) {
                        viewModel.defineDataSetbase(resourceFrete.getDado());
                        viewModel.separaDataSet_pagos_E_emAberto();
                        if (aguardandoPrimeiraAtualizacaoDeUi) {
                            primeiraAtt_notificaQueFreteEstaCarregado();
                        } else {
                            demaisAtts_notificaQueFreteEstaCarregado();
                        }
                    }
                });
    }

    private void demaisAtts_notificaQueFreteEstaCarregado() {
        demaisAtt.setFreteCarregado(true);
        if (demaisAtt.todosOsDadosProntos()) {
            callback.notificaAtualizacao();
            demaisAtt.resetaBooleanos();
        }
    }

    private void primeiraAtt_notificaQueFreteEstaCarregado() {
        primeiraAtt.setFreteCarregado(true);
        if (primeiraAtt.todosOsDadosProntos()) {
            callback.dadosPreparadosParaInicializacao();
            aguardandoPrimeiraAtualizacaoDeUi = false;
        }
    }

    //----------------------------------------------------------------------------------------------

    private void observerDataRecebimentoFrete() {
        viewModel.buscaRecebimentos().observe(lifecycleOwner,
                resourceRecebimento -> {
                    if (resourceRecebimento.getDado() != null) {
                        viewModel.setDataSet_Recebimento(resourceRecebimento.getDado());
                        if (aguardandoPrimeiraAtualizacaoDeUi) {
                            primeiraAtt_notificaQueRecebimentoEstaCarregado();
                        } else {
                            demaisAtt_notificaQueRecebimentoEstaCarregado();
                        }
                    }
                });
    }

    private void demaisAtt_notificaQueRecebimentoEstaCarregado() {
        demaisAtt.setRecebimentoCarregado(true);
        if (demaisAtt.todosOsDadosProntos()) {
            callback.notificaAtualizacao();
            demaisAtt.resetaBooleanos();
        }
    }

    private void primeiraAtt_notificaQueRecebimentoEstaCarregado() {
        primeiraAtt.setRecebimentoCarregado(true);
        if (primeiraAtt.todosOsDadosProntos()) {
            callback.dadosPreparadosParaInicializacao();
            aguardandoPrimeiraAtualizacaoDeUi = false;
        }
    }


}

class PrimeiraAtualizacao {
    private boolean cavaloCarregado = false;
    private boolean freteCarregado = false;
    private boolean recebimentoCarregado = false;

    public void setCavaloCarregado(boolean cavaloCarregado) {
        this.cavaloCarregado = cavaloCarregado;
    }

    public void setFreteCarregado(boolean freteCarregado) {
        this.freteCarregado = freteCarregado;
    }

    public void setRecebimentoCarregado(boolean recebimentoCarregado) {
        this.recebimentoCarregado = recebimentoCarregado;
    }

    public boolean todosOsDadosProntos() {
        boolean isReady = true;
        if (!cavaloCarregado) isReady = false;
        if (!freteCarregado) isReady = false;
        if (!recebimentoCarregado) isReady = false;
        return isReady;
    }
}

class DemaisAtualizacoes {
    private boolean freteCarregado = false;
    private boolean recebimentoCarregado = false;

    public void setFreteCarregado(boolean freteCarregado) {
        this.freteCarregado = freteCarregado;
    }

    public void setRecebimentoCarregado(boolean recebimentoCarregado) {
        this.recebimentoCarregado = recebimentoCarregado;
    }

    public boolean todosOsDadosProntos() {
        boolean isReady = true;
        if (!freteCarregado) isReady = false;
        if (!recebimentoCarregado) isReady = false;
        return isReady;
    }

    public void resetaBooleanos() {
        //Resetado para que possa aguardar por uma nova atualizacao
        freteCarregado = false;
        recebimentoCarregado = false;
    }

}