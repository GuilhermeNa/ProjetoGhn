package br.com.transporte.appGhn.ui.fragment.seguros.seguroInfo.viewmodel;

import androidx.lifecycle.ViewModel;

import br.com.transporte.appGhn.model.abstracts.DespesaComSeguro;
import br.com.transporte.appGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.appGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.appGhn.ui.fragment.seguros.TipoDeSeguro;

public class SegurosInformacoesGeraisViewModel extends ViewModel {

    private Long seguroId;
    private TipoDeSeguro tipoDeSeguro;
    private DespesaComSeguroFrota seguroFrota;

    private DespesaComSeguroDeVida seguroVida;

    private String placa;


    public void setSeguroFrota(DespesaComSeguroFrota seguroFrota) {
        this.seguroFrota = seguroFrota;
    }

    public DespesaComSeguroDeVida getSeguroVida() {
        return seguroVida;
    }

    public void setSeguroVida(DespesaComSeguroDeVida seguroVida) {
        this.seguroVida = seguroVida;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public DespesaComSeguro getSeguroFrota() {
        return seguroFrota;
    }

    public TipoDeSeguro getTipoDeSeguro() {
        return tipoDeSeguro;
    }

    public void setTipoDeSeguro(TipoDeSeguro tipoDeSeguro) {
        this.tipoDeSeguro = tipoDeSeguro;
    }

    public Long getSeguroId() {
        return seguroId;
    }

    public void setSeguroId(Long seguroId) {
        this.seguroId = seguroId;
    }
}
