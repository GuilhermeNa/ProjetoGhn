package br.com.transporte.AppGhn.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

import br.com.transporte.AppGhn.model.abstracts.Frota;

@Entity(foreignKeys =
@ForeignKey(
        entity = Cavalo.class,
        parentColumns = "id",
        childColumns = "refCavaloId",
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.SET_NULL
))
public class SemiReboque extends Frota {

    private int refCavaloId;

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
        this.refCavaloId = referenciaCavalo;
    }

    public SemiReboque() {

    }

    public int getRefCavaloId() {
        return refCavaloId;
    }

    public void setRefCavaloId(int refCavaloId) {
        this.refCavaloId = refCavaloId;
    }
}
