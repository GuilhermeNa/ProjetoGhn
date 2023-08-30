package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;

@Dao
public interface RoomDespesaSeguroVidaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adiciona(DespesaComSeguroDeVida seguroDeVida);

    @Delete
    void deleta(DespesaComSeguroDeVida seguroDeVida);

    @Query("SELECT * FROM despesaComSeguroDeVida WHERE id = :seguroId")
    DespesaComSeguroDeVida localizaPeloId(Long seguroId);

}
