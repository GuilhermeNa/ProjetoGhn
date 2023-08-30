package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroFrota;

@Dao
public interface RoomDespesaComSeguroFrotaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adiciona(DespesaComSeguroFrota seguroFrota);

    @Delete
    void deleta(DespesaComSeguroFrota seguroFrota);

    @Query("SELECT * FROM despesaComSeguroFrota WHERE id = :seguroId")
    DespesaComSeguroFrota localizaPeloId(Long seguroId);

}
