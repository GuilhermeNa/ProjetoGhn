package br.com.transporte.appGhn.model.despesas;

import androidx.room.Entity;

import java.time.LocalDate;

import br.com.transporte.appGhn.model.abstracts.Despesas;
import br.com.transporte.appGhn.model.enums.TipoCertificado;

@Entity
public class DespesaCertificado extends Despesas {
    private LocalDate dataDeEmissao, dataDeVencimento;
    private String ano;
    private TipoCertificado tipoCertificado;
    private long numeroDoDocumento;
    private boolean valido;

    public DespesaCertificado() {

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
