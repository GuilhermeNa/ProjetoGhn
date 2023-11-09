package br.com.transporte.appGhn.model.parcelas;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import br.com.transporte.appGhn.model.abstracts.Parcela;
import br.com.transporte.appGhn.model.despesas.DespesaComSeguroDeVida;

@Entity(foreignKeys =
@ForeignKey(
        entity = DespesaComSeguroDeVida.class,
        parentColumns = "id",
        childColumns = "refSeguroId",
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
))
public class Parcela_seguroVida extends Parcela {
    private Long refSeguroId;

    public Parcela_seguroVida() {}

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
