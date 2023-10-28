package br.com.transporte.AppGhn.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;

@Dao
public interface RoomCustosDeManutencaoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long adiciona(CustosDeManutencao custosDeManutencao);

    @Update
    void atualiza(CustosDeManutencao manutencao);

    @Delete
    void deleta(CustosDeManutencao custosDeManutencao);

    @Query("SELECT * FROM custosDeManutencao WHERE id = :manutencaoId")
    LiveData<CustosDeManutencao> localizaPeloId(Long manutencaoId);

    @Query("SELECT * FROM custosDeManutencao WHERE refCavaloId = :cavaloId")
    LiveData<List<CustosDeManutencao>> listaPeloCavaloId(Long cavaloId);

    @Query("SELECT * FROM CustosDeManutencao")
    LiveData<List<CustosDeManutencao>> todos();

    @Query("SELECT * FROM CustosDeManutencao")
    List<CustosDeManutencao> buscaTodosParaTask();

    @Query("SELECT * FROM CustosDeManutencao WHERE refCavaloId = :cavaloId")
    List<CustosDeManutencao> buscaPorCavaloIdParaTask(Long cavaloId);

}
