package br.com.transporte.AppGhn.model.abstracts;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.time.LocalDate;

import br.com.transporte.AppGhn.model.Cavalo;

@Entity(foreignKeys =
@ForeignKey(
        entity = Cavalo.class,
        parentColumns = "id",
        childColumns = "refCavaloId",
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
))
public abstract class CustosEDespesas {

    private Integer refCavaloId;
    private LocalDate data;

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public int getRefCavalo() {
        return refCavaloId;
    }

    public void setRefCavalo(int refCavalo) {
        this.refCavaloId = refCavalo;
    }
}
