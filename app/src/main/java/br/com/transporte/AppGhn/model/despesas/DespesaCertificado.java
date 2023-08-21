package br.com.transporte.AppGhn.model.despesas;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.transporte.AppGhn.model.abstracts.Despesas;
import br.com.transporte.AppGhn.model.enums.TipoCertificado;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;

public class DespesaCertificado extends Despesas {
    private LocalDate dataDeEmissao, dataDeVencimento;
    private String ano;
    private TipoCertificado tipoCertificado;
    private long numeroDoDocumento;
    private boolean valido;
    private int id;

    public DespesaCertificado(TipoCertificado tipoCertificado, String ano, long numeroDoDocumento, LocalDate dataDeEmissao,
                              LocalDate dataDeVencimento, int refCaminhao, BigDecimal valorDespesa,
                              TipoDespesa tipoDespesa, boolean valido) {
        this.tipoCertificado = tipoCertificado;
        this.ano = ano;
        this.numeroDoDocumento = numeroDoDocumento;
        this.dataDeEmissao = dataDeEmissao;
        this.dataDeVencimento = dataDeVencimento;
        super.setValorDespesa(valorDespesa);
        super.setRefCavalo(refCaminhao);
        super.setTipoDespesa(tipoDespesa);
        this.valido = valido;
    }

    public DespesaCertificado() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public long getNumeroDoDocumento() {
        return numeroDoDocumento;
    }

    public void setNumeroDoDocumento(long numeroDoDocumento) {
        this.numeroDoDocumento = numeroDoDocumento;
    }

    public LocalDate getDataDeEmissao() {
        return dataDeEmissao;
    }

    public void setDataDeEmissao(LocalDate dataDeEmissao) {
        this.dataDeEmissao = dataDeEmissao;
        super.setData(dataDeEmissao);
    }

    @Override
    public void setData(LocalDate data){
        this.dataDeEmissao = data;
        super.setData(data);
    }

    public LocalDate getDataDeVencimento() {
        return dataDeVencimento;
    }

    public void setDataDeVencimento(LocalDate dataDeVencimento) {
        this.dataDeVencimento = dataDeVencimento;
    }

    public boolean temIdValido() {
        return id > 0;
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public TipoCertificado getTipoCertificado() {
        return tipoCertificado;
    }

    public void setTipoCertificado(TipoCertificado tipoCertificado) {
        this.tipoCertificado = tipoCertificado;
    }
}
