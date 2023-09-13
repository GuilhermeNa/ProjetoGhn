package br.com.transporte.AppGhn.model.parcelas;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import br.com.transporte.AppGhn.model.abstracts.Parcela;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroFrota;

@Entity(foreignKeys =
@ForeignKey(
        entity = DespesaComSeguroFrota.class,
        parentColumns = "id",
        childColumns = "refSeguroId",
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
))
public class Parcela_seguroFrota extends Parcela {
    private Long refSeguroId;

    public Parcela_seguroFrota() {}

    public long getRefSeguro() {
        return refSeguroId;
    }

    public void setRefSeguro(long refChaveIdEstrangeira) {
        this.refSeguroId = refChaveIdEstrangeira;
    }

    public Long getRefSeguroId() {
        return refSeguroId;
    }

    public void setRefSeguroId(Long refSeguroId) {
        this.refSeguroId = refSeguroId;
    }
}
