package br.com.transporte.AppGhn.model;

import br.com.transporte.AppGhn.model.abstracts.Frota;

public class SemiReboque extends Frota {

    private int referenciaCavalo;

    public SemiReboque(String placa, String marcaModelo, String ano, String modelo, String cor,
                       String renavam, String chassi, int referenciaCavalo) {
        setPlaca(placa);
        setMarcaModelo(marcaModelo);
        setAno(ano);
        setModelo(modelo);
        setCor(cor);
        setRenavam(renavam);
        setChassi(chassi);
        this.referenciaCavalo = referenciaCavalo;
    }

    public SemiReboque() {

    }

    public int getReferenciaCavalo() {
        return referenciaCavalo;
    }

    public void setReferenciaCavalo(int referenciaCavalo) {
        this.referenciaCavalo = referenciaCavalo;
    }
}
