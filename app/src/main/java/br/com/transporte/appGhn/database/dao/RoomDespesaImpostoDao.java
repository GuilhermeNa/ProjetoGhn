package br.com.transporte.appGhn.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.transporte.appGhn.model.despesas.DespesasDeImposto;

@Dao
public interface RoomDespesaImpostoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long adiciona(DespesasDeImposto imposto);

    @Delete
    void deleta(DespesasDeImposto imposto);

    @Update
    void edita(DespesasDeImposto imposto);

    @Query("SELECT * FROM despesasDeImposto WHERE id = :impostoId")
    LiveData<DespesasDeImposto> localizaPeloId(Long impostoId);

    @Query("SELECT * FROM despesasDeImposto")
    LiveData<List<DespesasDeImposto>> todos();

    @Query("SELECT * FROM despesasDeImposto")
    List<DespesasDeImposto> buscaTodosParaTask();

    @Query("SELECT * FROM despesasDeImposto WHERE refCavaloId = :cavaloId")
    List<DespesasDeImposto> buscaPorCavaloIdParaTask(Long cavaloId);

}
