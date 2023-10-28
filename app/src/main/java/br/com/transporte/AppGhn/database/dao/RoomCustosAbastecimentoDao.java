package br.com.transporte.AppGhn.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;

@Dao
public interface RoomCustosAbastecimentoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long adiciona(CustosDeAbastecimento abastecimento);

    @Delete
    void deleta(CustosDeAbastecimento abastecimento);

    @Update
    void substitui(CustosDeAbastecimento abastecimento);

    @Query("SELECT * FROM custosDeAbastecimento WHERE id = :abastecimentoId")
    LiveData<CustosDeAbastecimento> localizaPeloId(Long abastecimentoId);

    @Query("SELECT * FROM custosDeAbastecimento")
    LiveData<List<CustosDeAbastecimento>> todos();

    @Query("SELECT * FROM custosDeAbastecimento WHERE refCavaloId = :cavaloId")
    LiveData<List<CustosDeAbastecimento>> listaPorCavaloId(Long cavaloId);

    @Query("SELECT * FROM CustosDeAbastecimento WHERE refCavaloId = :cavaloId AND flagAbastecimentoTotal = :isAbastecimentoTotal")
    LiveData<List<CustosDeAbastecimento>> listaDeAbastecimentoTotal(Long cavaloId, boolean isAbastecimentoTotal);

    @Query("SELECT * FROM CustosDeAbastecimento WHERE refCavaloId = :cavaloId")
    List<CustosDeAbastecimento> buscaPorCavaloIdParaTask(Long cavaloId);

    @Query("SELECT * FROM CustosDeAbastecimento")
    List<CustosDeAbastecimento> buscaTodosParaTask();
}
