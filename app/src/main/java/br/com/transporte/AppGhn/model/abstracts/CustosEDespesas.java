package br.com.transporte.AppGhn.model.abstracts;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

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

    @PrimaryKey(autoGenerate = true)
    private Long id;
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

    public void setRefCavalo(Integer refCavalo) {
        this.refCavaloId = refCavalo;
    }

    public Integer getRefCavaloId() {
        return refCavaloId;
    }

    public void setRefCavaloId(Integer refCavaloId) {
        this.refCavaloId = refCavaloId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean temIdValido() {
        return id > 0;
    }
}
