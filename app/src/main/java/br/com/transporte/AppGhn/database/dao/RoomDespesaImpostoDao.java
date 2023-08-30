package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;

@Dao
public interface RoomDespesaImpostoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adiciona(DespesasDeImposto imposto);

    @Delete
    void deleta(DespesasDeImposto imposto);

    @Query("SELECT * FROM despesasDeImposto WHERE id = :impostoId")
    DespesasDeImposto localizaPeloId(Long impostoId);


}
