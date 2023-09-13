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
    private Long refMotoristaId;
    public List<Long> refAdiantamentos = new ArrayList<>();
    public List<Long> refReembolsos = new ArrayList<>();
    public List<Long> refFretes = new ArrayList<>();

    //---------------------------------- Getters Setters -------------------------------------------

    public void setRefMotoristaId(Long refMotorista) {
        this.refMotoristaId = refMotorista;
    }

    public Long getRefMotoristaId(){
        return this.refMotoristaId;
    }

    public List<Long> getRefAdiantamentos() {
        return new ArrayList<>(refAdiantamentos);
    }

    public List<Long> getRefReembolsos() {
        return new ArrayList<>(refReembolsos);
    }

    public List<Long> getRefFretes() {
        return new ArrayList<>(refFretes);
    }

    public void setRefAdiantamentos(){}
    public void setRefReembolsos(){}
    public void setRefFretes(){}

    //---------------------------------- Outros Metodos ---------------------------------------------

    public void listaFretesAdiciona(Long i){
        refFretes.add(i);
    }

    public void listaAdiantamentosAdiciona(Long i){
        refAdiantamentos.add(i);
    }

    public void listaReembolsosAdiciona(Long i){
        refReembolsos.add(i);
    }

}
