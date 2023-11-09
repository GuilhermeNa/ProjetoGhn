package br.com.transporte.appGhn.model.custos;

import androidx.room.Entity;

import java.io.Serializable;

import br.com.transporte.appGhn.model.abstracts.Custos;
import br.com.transporte.appGhn.model.enums.TipoCustoManutencao;

@Entity
public class CustosDeManutencao extends Custos implements Serializable {
    private String empresa, descricao, nNota;
    private TipoCustoManutencao tipoCustoManutencao;

    public CustosDeManutencao() {
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNNota() {
        return nNota;
    }

    public void setNNota(String nNota) {
        this.nNota = nNota;
    }

    public TipoCustoManutencao getTipoCustoManutencao() {
        return tipoCustoManutencao;
    }

    public void setTipoCustoManutencao(TipoCustoManutencao tipoCustoManutencao) {
        this.tipoCustoManutencao = tipoCustoManutencao;
    }
}
