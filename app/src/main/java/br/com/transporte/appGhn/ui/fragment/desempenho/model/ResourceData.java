package br.com.transporte.appGhn.ui.fragment.desempenho.model;

import java.util.List;

import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.Frete;
import br.com.transporte.appGhn.model.Motorista;
import br.com.transporte.appGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.appGhn.model.custos.CustosDeManutencao;
import br.com.transporte.appGhn.model.custos.CustosDePercurso;
import br.com.transporte.appGhn.model.despesas.DespesaAdm;
import br.com.transporte.appGhn.model.despesas.DespesaCertificado;
import br.com.transporte.appGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.appGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.appGhn.model.parcelas.Parcela_seguroVida;

public class ResourceData {
    private List<Cavalo> dataSetCavalo;
    private List<Motorista> dataSetMotorista;
    private List<Frete> dataSetFrete;
    private List<CustosDeAbastecimento> dataSetAbastecimento;
    private List<CustosDePercurso> dataSetCustoPercurso;
    private List<CustosDeManutencao> dataSetCustoManutencao;
    private List<DespesaCertificado> dataSetDespesaCertificado;
    private List<DespesasDeImposto> dataSetDespesaImposto;
    private List<DespesaAdm> dataSetDespesaAdm;
    private List<Parcela_seguroFrota> dataSetDespesaSeguroFrota;
    private List<Parcela_seguroVida> dataSetDespesaSeguroVida;



    //----------------------------------------------------------------------------------------------

    public List<Frete> getDataSetFrete() {
        return dataSetFrete;
    }

    public void setDataSetFrete(List<Frete> dataSetFrete) {
        this.dataSetFrete = dataSetFrete;
    }

    public List<Cavalo> getDataSetCavalo() {
        return dataSetCavalo;
    }

    public void setDataSetCavalo(List<Cavalo> dataSetCavalo) {
        this.dataSetCavalo = dataSetCavalo;
    }

    public List<Motorista> getDataSetMotorista() {
        return dataSetMotorista;
    }

    public void setDataSetMotorista(List<Motorista> dataSetMotorista) {
        this.dataSetMotorista = dataSetMotorista;
    }

    public List<CustosDeAbastecimento> getDataSetAbastecimento() {
        return dataSetAbastecimento;
    }

    public void setDataSetAbastecimento(List<CustosDeAbastecimento> dataSetAbastecimento) {
        this.dataSetAbastecimento = dataSetAbastecimento;
    }

    public List<CustosDePercurso> getDataSetCustoPercurso() {
        return dataSetCustoPercurso;
    }

    public void setDataSetCustoPercurso(List<CustosDePercurso> dataSetCustoPercurso) {
        this.dataSetCustoPercurso = dataSetCustoPercurso;
    }

    public List<CustosDeManutencao> getDataSetCustoManutencao() {
        return dataSetCustoManutencao;
    }

    public void setDataSetCustoManutencao(List<CustosDeManutencao> dataSetCustoManutencao) {
        this.dataSetCustoManutencao = dataSetCustoManutencao;
    }

    public List<DespesaCertificado> getDataSetDespesaCertificado() {
        return dataSetDespesaCertificado;
    }

    public void setDataSetDespesaCertificado(List<DespesaCertificado> dataSetDespesaCertificado) {
        this.dataSetDespesaCertificado = dataSetDespesaCertificado;
    }

    public List<DespesasDeImposto> getDataSetDespesaImposto() {
        return dataSetDespesaImposto;
    }

    public void setDataSetDespesaImposto(List<DespesasDeImposto> dataSetDespesaImposto) {
        this.dataSetDespesaImposto = dataSetDespesaImposto;
    }

    public List<DespesaAdm> getDataSetDespesaAdm() {
        return dataSetDespesaAdm;
    }

    public void setDataSetDespesaAdm(List<DespesaAdm> dataSetDespesaAdm) {
        this.dataSetDespesaAdm = dataSetDespesaAdm;
    }

    public List<Parcela_seguroFrota> getDataSetDespesaSeguroFrota() {
        return dataSetDespesaSeguroFrota;
    }

    public void setDataSetDespesaSeguroFrota(List<Parcela_seguroFrota> dataSetDespesaSeguroFrota) {
        this.dataSetDespesaSeguroFrota = dataSetDespesaSeguroFrota;
    }

    public List<Parcela_seguroVida> getDataSetDespesaSeguroVida() {
        return dataSetDespesaSeguroVida;
    }

    public void setDataSetDespesaSeguroVida(List<Parcela_seguroVida> dataSetDespesaSeguroVida) {
        this.dataSetDespesaSeguroVida = dataSetDespesaSeguroVida;
    }
}
