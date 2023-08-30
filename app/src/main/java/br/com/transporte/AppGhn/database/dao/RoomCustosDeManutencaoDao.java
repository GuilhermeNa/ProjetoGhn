package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;

@Dao
public interface RoomCustosDeManutencaoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adiciona(CustosDeManutencao custosDeManutencao);

    @Delete
    void delete(CustosDeManutencao custosDeManutencao);

    @Query("SELECT * FROM custosDeManutencao WHERE id = :manutencaoId")
    CustosDeManutencao localizaPeloId(Long manutencaoId);


}
