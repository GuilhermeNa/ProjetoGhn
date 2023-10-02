package br.com.transporte.AppGhn.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;

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
}
