package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import br.com.transporte.AppGhn.model.despesas.DespesaAdm;

@Dao
public interface RoomDespesaAdmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adiciona(DespesaAdm despesa);

    @Delete
    void deleta(DespesaAdm despesa);

    @Query("SELECT * FROM despesaAdm WHERE id = :despesaId")
    DespesaAdm localizaPeloId(Long despesaId);

}
