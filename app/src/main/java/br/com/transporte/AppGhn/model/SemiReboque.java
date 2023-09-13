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

    private Long refCavaloId;

    @Ignore
    public SemiReboque(String placa, String marcaModelo, String ano, String modelo, String cor,
                       String renavam, String chassi, Long referenciaCavalo) {
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

    public Long getRefCavaloId() {
        return refCavaloId;
    }

    public void setRefCavaloId(Long refCavaloId) {
        this.refCavaloId = refCavaloId;
    }
}
