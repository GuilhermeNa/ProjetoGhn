package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;

@Dao
public interface RoomCustosAbastecimentoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adiciona(CustosDeAbastecimento abastecimento);

    @Delete
    void deleta(CustosDeAbastecimento abastecimento);

    @Query("SELECT * FROM custosDeAbastecimento WHERE id = :abastecimentoId")
    CustosDeAbastecimento localizaPeloId(Long abastecimentoId);


    @Query("SELECT * FROM custosDeAbastecimento")
    List<CustosDeAbastecimento> todos();

    @Query("SELECT * FROM custosDeAbastecimento WHERE refCavaloId = :cavaloId")
    List<CustosDeAbastecimento> listaPorCavaloId(Long cavaloId);
}
