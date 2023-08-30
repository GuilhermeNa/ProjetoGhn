package br.com.transporte.AppGhn.model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class FreteEAdm {
    @Embedded
    public Frete frete;
    @Relation(
            parentColumn = "id",
            entityColumn = "refFreteId"
    )
    public Frete.AdmFinanceiroFrete admFrete;
}
