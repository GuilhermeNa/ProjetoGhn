package br.com.transporte.AppGhn.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.model.abstracts.Frota;

//@Entity
public class Cavalo extends Frota {
    private String versao;
    private Motorista motorista;
    private BigDecimal comissaoBase;
    private boolean valido;
    private List<SemiReboque> listaSemiReboque = new ArrayList<>();

    //@Ignore
    public Cavalo(String placa, String renavam, String chassi, String ano, String modelo,
                  String marcaModelo, String cor, String versao, Motorista motorista,
                  BigDecimal comissaoBase, boolean valido) {
        super.setPlaca(placa);
        super.setRenavam(renavam);
        super.setChassi(chassi);
        super.setAno(ano);
        super.setModelo(modelo);
        super.setMarcaModelo(marcaModelo);
        super.setCor(cor);
        this.versao = versao;
        this.motorista = motorista;
        this.comissaoBase = comissaoBase;
        this.valido = valido;
    }


    public Cavalo() {}

    //---------------------------------- Getters and Setters ----------------------------------------------

    public BigDecimal getComissaoBase() {
        return comissaoBase;
    }

    public void setComissaoBase(BigDecimal comissaoBase) {
        this.comissaoBase = comissaoBase;
    }

    public String getVersao() {
        return versao;
    }

    public void setVersao(String versao) {
        this.versao = versao;
    }

    public List<SemiReboque> getSemiReboque() {
        return listaSemiReboque;
    }

    public void adicionaSemiReboque(SemiReboque sr) {
        listaSemiReboque.add(sr);
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public List<SemiReboque> getListaSemiReboque() {
        return listaSemiReboque;
    }


    //---------------------------------- Outros Metodos ----------------------------------------------

    public int getPosicaoSr(SemiReboque sr) {
        SemiReboque cavaloEncontrado = null;
        int posicaoNaLista = 0;
        for (SemiReboque s: listaSemiReboque) {
            if(s.getId() == sr.getId()){
                cavaloEncontrado = s;
            }
        } if(cavaloEncontrado != null){
            posicaoNaLista = listaSemiReboque.indexOf(cavaloEncontrado);
        }
        return posicaoNaLista;
    }

    public void removeSemiReboque(SemiReboque sr ){
        SemiReboque srEncontrado = null;
        for (SemiReboque s: listaSemiReboque) {
            if(s.getId() == sr.getId()){
                srEncontrado = s;
            }
        }
        if(srEncontrado != null){
            listaSemiReboque.remove(srEncontrado);
        }
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }
}