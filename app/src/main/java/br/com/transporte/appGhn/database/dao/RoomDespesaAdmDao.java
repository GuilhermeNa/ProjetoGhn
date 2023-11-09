package br.com.transporte.appGhn.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.transporte.appGhn.model.despesas.DespesaAdm;
import br.com.transporte.appGhn.model.enums.TipoDespesa;

@Dao
public interface RoomDespesaAdmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long adiciona(DespesaAdm despesa);

    @Delete
    void deleta(DespesaAdm despesa);

    @Update
    void atualiza(DespesaAdm despesa);

    @Query("SELECT * FROM despesaAdm WHERE id = :despesaId")
    LiveData<DespesaAdm> localizaPeloId(Long despesaId);

    @Query("SELECT * FROM DespesaAdm")
    LiveData<List<DespesaAdm>> buscaTodos();

    @Query("SELECT * FROM DespesaAdm WHERE tipoDespesa = :tipo")
    LiveData<List<DespesaAdm>> buscaPorTipo(final TipoDespesa tipo);

    @Query("SELECT * FROM DespesaAdm")
    List<DespesaAdm> buscaTodosParaTask();

    @Query("SELECT * FROM DespesaAdm WHERE refCavaloId = :cavaloId")
    List<DespesaAdm> buscaPorCavaloIdParaTask(Long cavaloId);

}
