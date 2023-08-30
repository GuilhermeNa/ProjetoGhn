package br.com.transporte.AppGhn.model.custos;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.model.abstracts.Custos;

@Entity(foreignKeys =
@ForeignKey(
        entity = Motorista.class,
        parentColumns = "id",
        childColumns = "refMotoristaId",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
))
public class CustosDeSalario extends Custos {
    private Integer refMotoristaId;
    private final List<Integer> refAdiantamentos = new ArrayList<>();
    private final List<Long> refReembolsos = new ArrayList<>();
    private final List<Long> refFretes = new ArrayList<>();

    //---------------------------------- Getters Setters -------------------------------------------

    public void setRefMotoristaId(Integer refMotorista) {
        this.refMotoristaId = refMotorista;
    }

    public Integer getRefMotoristaId(){
        return this.refMotoristaId;
    }

    public List<Integer> getRefAdiantamentos() {
        return refAdiantamentos;
    }

    public List<Long> getRefReembolsos() {
        return refReembolsos;
    }

    public List<Long> getRefFretes() {
        return refFretes;
    }

    //---------------------------------- Outros Metodos ---------------------------------------------

    public void listaFretesAdiciona(int i){
        refFretes.add(i);
    }

    public void listaAdiantamentosAdiciona(int i){
        refAdiantamentos.add(i);
    }

    public void listaReembolsosAdiciona(int i){
        refReembolsos.add(i);
    }

}
