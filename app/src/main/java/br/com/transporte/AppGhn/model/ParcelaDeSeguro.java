package br.com.transporte.AppGhn.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.transporte.AppGhn.model.abstracts.Parcela;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;

@Entity(foreignKeys =
@ForeignKey(
        entity = ParcelaDeSeguro.class,
        parentColumns = "id",
        childColumns = "refSeguroId",
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
))
public class ParcelaDeSeguro extends Parcela {

    private Long refSeguroId;

    public ParcelaDeSeguro(int numeroDaParcela, LocalDate data, BigDecimal valor, boolean valido, long refSeguroId, boolean paga, TipoDespesa tipo, int refCavalo) {
        super();
        super.setNumeroDaParcela(numeroDaParcela);
        super.setData(data);
        super.setValor(valor);
        super.setValido(valido);
        this.setRefSeguro(refSeguroId);
        super.setPaga(paga);
        super.setTipoDespesa(tipo);
        super.setRefCavaloId(refCavalo);
    }

    public ParcelaDeSeguro() {

    }

    public long getRefSeguro() {
        return refSeguroId;
    }

    public void setRefSeguro(long refChaveIdEstrangeira) {
        this.refSeguroId = refChaveIdEstrangeira;
    }
}
