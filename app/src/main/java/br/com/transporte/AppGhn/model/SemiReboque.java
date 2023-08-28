package br.com.transporte.AppGhn.model;

import androidx.room.Entity;
import androidx.room.Ignore;

import br.com.transporte.AppGhn.model.abstracts.Frota;

@Entity
public class SemiReboque extends Frota {

    private int referenciaCavalo;

    @Ignore
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
