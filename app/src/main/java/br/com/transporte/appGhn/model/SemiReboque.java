package br.com.transporte.appGhn.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import br.com.transporte.appGhn.model.abstracts.Frota;

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


    public SemiReboque() {
    }

    public Long getRefCavaloId() {
        return refCavaloId;
    }

    public void setRefCavaloId(Long refCavaloId) {
        this.refCavaloId = refCavaloId;
    }
}
