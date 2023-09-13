package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;

@Dao
public interface RoomCustosDeManutencaoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adiciona(CustosDeManutencao custosDeManutencao);

    @Delete
    void deleta(CustosDeManutencao custosDeManutencao);

    @Query("SELECT * FROM custosDeManutencao WHERE id = :manutencaoId")
    CustosDeManutencao localizaPeloId(Long manutencaoId);

    @Query("SELECT * FROM custosDeManutencao WHERE refCavaloId = :cavaloId")
    List<CustosDeManutencao> listaPeloCavaloId(Long cavaloId);

    @Query("SELECT * FROM CustosDeManutencao")
    List<CustosDeManutencao> todos();

}
