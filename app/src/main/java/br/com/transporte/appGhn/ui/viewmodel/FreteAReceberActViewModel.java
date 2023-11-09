package br.com.transporte.appGhn.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.transporte.appGhn.filtros.FiltraFrete;
import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.Frete;
import br.com.transporte.appGhn.model.RecebimentoDeFrete;
import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.FreteRepository;
import br.com.transporte.appGhn.repository.RecebimentoDeFreteRepository;
import br.com.transporte.appGhn.repository.Resource;
import br.com.transporte.appGhn.util.DataUtil;

public class FreteAReceberActViewModel extends ViewModel {
    private final FreteRepository freteRepository;
    private final CavaloRepository cavaloRepository;
    private final RecebimentoDeFreteRepository recebimentoRepository;
    public FreteAReceberActViewModel(FreteRepository freteRepository, CavaloRepository cavaloRepository, RecebimentoDeFreteRepository recebimentoRepository) {
        this.freteRepository = freteRepository;
        this.cavaloRepository = cavaloRepository;
        this.recebimentoRepository = recebimentoRepository;
    }

    //----------------------------------------------------------------------------------------------

    private LocalDate dataInicial = DataUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
    private LocalDate dataFinal = DataUtil.capturaDataDeHojeParaConfiguracaoInicial();
    private List<Frete> dataSet_base;
    private List<Frete> dataSet_freteRecebido;
    private List<Frete> dataSet_freteEmAberto;
    private List<Cavalo> dataSet_cavalo;
    private List<RecebimentoDeFrete> dataSet_Recebimento;
    public List<Frete> getDataSet_freteRecebido() {
        return dataSet_freteRecebido;
    }
    public List<Frete> getDataSet_freteEmAberto() {
        return dataSet_freteEmAberto;
    }
    public List<Cavalo> getDataSet_cavalo() {
        return dataSet_cavalo;
    }
    public void setDataSet_cavalo(List<Cavalo> dataSet_cavalo) {
        this.dataSet_cavalo = dataSet_cavalo;
    }
    public List<RecebimentoDeFrete> getDataSet_Recebimento() {
        return dataSet_Recebimento;
    }

    public void setDataSet_Recebimento(List<RecebimentoDeFrete> dataSet_Recebimento) {
        this.dataSet_Recebimento = dataSet_Recebimento;
    }

    public LocalDate getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(LocalDate dataInicial) {
        this.dataInicial = dataInicial;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDate dataFinal) {
        this.dataFinal = dataFinal;
    }

    //----------------------------------------------------------------------------------------------

    public LiveData<Resource<List<Frete>>> buscaFretes(){
        return freteRepository.buscaFretes();
    }

    public LiveData<Resource<List<Cavalo>>> buscaCavalos() {
        return cavaloRepository.buscaCavalos();
    }

    public LiveData<Resource<List<RecebimentoDeFrete>>> buscaRecebimentos(){
        return recebimentoRepository.buscaRecebimentos();
    }

    public LiveData<Long> salvaFrete(@NonNull final Frete frete) {
        if(frete.getId() != null){
            return freteRepository.editaFrete(frete);
        }
        return null;
    }

    public LiveData<Frete> localizaFrete(final long id){
        return freteRepository.localizaFrete(id);
    }

    public LiveData<List<RecebimentoDeFrete>> buscaRecebimentosPorFreteId(Long freteId) {
        return recebimentoRepository.buscaRecebimentosPorFreteId(freteId);
    }

    public void separaDataSet_pagos_E_emAberto(){
        List<Frete> listaFiltradaPorData = FiltraFrete.listaPorData(dataSet_base, dataInicial, dataFinal);
        dataSet_freteEmAberto = FiltraFrete.listaPorStatusDeRecebimentoDoFrete(listaFiltradaPorData, false);
        if(dataSet_freteEmAberto == null) dataSet_freteEmAberto = new ArrayList<>();
        dataSet_freteRecebido = FiltraFrete.listaPorStatusDeRecebimentoDoFrete(listaFiltradaPorData, true);
        if(dataSet_freteRecebido == null) dataSet_freteRecebido = new ArrayList<>();
    }

    public void defineDataSetbase(final List<Frete> dataSet){
        this.dataSet_base = dataSet;
    }



    //----------------------------------------------------------------------------------------------

}
