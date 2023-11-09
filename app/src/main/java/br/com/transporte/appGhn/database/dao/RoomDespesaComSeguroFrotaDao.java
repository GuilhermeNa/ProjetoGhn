package br.com.transporte.appGhn.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.transporte.appGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.appGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.appGhn.model.enums.TipoDespesa;

@Dao
public interface RoomDespesaComSeguroFrotaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long adiciona(DespesaComSeguroFrota seguroFrota);

    @Delete
    void deleta(DespesaComSeguroFrota seguroFrota);

    @Update
    void substitui(DespesaComSeguroFrota seguroFrota);

    @Query("SELECT * FROM despesaComSeguroFrota WHERE id = :seguroId")
    LiveData<DespesaComSeguroFrota> localizaPeloId(Long seguroId);

    @Query("SELECT * FROM DespesaComSeguroFrota")
    LiveData<List<DespesaComSeguroFrota>> todos();

    @Query("SELECT * FROM DespesaComSeguroFrota WHERE valido = :isValido")
    LiveData<List<DespesaComSeguroFrota>> listaPorValidade(boolean isValido);

    @Query("SELECT * FROM DespesaComSeguroFrota WHERE tipoDespesa = :tipo")
    LiveData<List<DespesaComSeguroFrota>> buscaPorTipo(TipoDespesa tipo);


    @Query("SELECT * FROM DespesaComSeguroFrota " +
            "WHERE ID = (SELECT MAX(ID) FROM DespesaComSeguroFrota) ")
    LiveData<DespesaComSeguroDeVida> ultimoAdicionado();

    @Query("SELECT * FROM DespesaComSeguroFrota WHERE valido = :isValido")
    LiveData<List<DespesaComSeguroFrota>> buscaPorStatus(boolean isValido);

}
