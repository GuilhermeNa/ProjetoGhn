package br.com.transporte.AppGhn.model.custos;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.transporte.AppGhn.model.abstracts.Custos;
import br.com.transporte.AppGhn.model.enums.TipoCustoManutencao;

public class CustosDeManutencao extends Custos {
    private String empresa;
    private String descricao;
    private String nNota;
    private TipoCustoManutencao tipoCustoManutencao;

    public CustosDeManutencao(LocalDate data, BigDecimal valor, String empresa, String descricao, String nNota) {
        super.setData(data);
        super.setValorCusto(valor);
        this.empresa = empresa;
        this.descricao = descricao;
        this.nNota = nNota;
    }

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

    public String getnNota() {
        return nNota;
    }

    public void setnNota(String nNota) {
        this.nNota = nNota;
    }

    public boolean temIdValido() {
        return super.getId() > 0;
    }

    public TipoCustoManutencao getTipoCustoManutencao() {
        return tipoCustoManutencao;
    }

    public void setTipoCustoManutencao(TipoCustoManutencao tipoCustoManutencao) {
        this.tipoCustoManutencao = tipoCustoManutencao;
    }
}
