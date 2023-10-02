package br.com.transporte.AppGhn.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.transporte.AppGhn.model.custos.CustosDePercurso;

@Dao
public interface RoomCustosPercursoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long adiciona(CustosDePercurso custo);

    @Delete
    void deleta(CustosDePercurso custo);

    @Update
    void atualiza(CustosDePercurso custo);

    @Query("SELECT * FROM custosDePercurso")
    LiveData<List<CustosDePercurso>> todos();

    @Query("SELECT * FROM custosdepercurso WHERE id = :custoId" )
    LiveData<CustosDePercurso> localizaPeloId(Long custoId);

    @Query("SELECT * FROM custosdepercurso WHERE refCavaloId = :cavaloId")
    LiveData<List<CustosDePercurso>> listaPorCavaloId(Long cavaloId);

}
