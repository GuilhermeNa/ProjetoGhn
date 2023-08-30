package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;

@Dao
public interface RoomDespesaCertificadoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adiciona(DespesaCertificado despesaCertificado);

    @Delete
    void deleta(DespesaCertificado despesaCertificado);

    @Query("SELECT * FROM despesacertificado WHERE id = :despesaId")
    DespesaCertificado localizaPeloId(Long despesaId);

}
