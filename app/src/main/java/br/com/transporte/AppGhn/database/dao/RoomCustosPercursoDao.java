package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import br.com.transporte.AppGhn.model.custos.CustosDePercurso;

@Dao
public interface RoomCustosPercursoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adiciona(CustosDePercurso custo);

    @Delete
    void deleta(CustosDePercurso custo);

    @Query("SELECT * FROM custosdepercurso WHERE id = :custoId" )
    CustosDePercurso localizaPeloId(Long custoId);

}
