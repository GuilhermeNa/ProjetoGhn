package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;

@Dao
public interface RoomCustosAbastecimentoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adiciona(CustosDeAbastecimento abastecimento);

    @Delete
    void deleta(CustosDeAbastecimento abastecimento);

    @Query("SELECT * FROM custosDeAbastecimento WHERE id = :abastecimentoId")
    CustosDeAbastecimento localizaPeloId(Long abastecimentoId);



}
