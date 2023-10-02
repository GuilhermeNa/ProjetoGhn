package br.com.transporte.AppGhn.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.transporte.AppGhn.model.Adiantamento;

@Dao
public interface RoomAdiantamentoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long adiciona(Adiantamento adiantamento);

    @Delete
    void deleta(Adiantamento adiantamento);

    @Update
    void altera(Adiantamento adiantamento);

    @Query("SELECT * FROM adiantamento")
    LiveData<List<Adiantamento>> todos();

    @Query("SELECT * FROM adiantamento WHERE id = :adiantamentoId")
    LiveData<Adiantamento> localizaPeloId(Long adiantamentoId);

    @Query("SELECT * FROM adiantamento WHERE refCavaloId = :cavaloId")
    LiveData<List<Adiantamento>> listaPorCavaloId(Long cavaloId);

}
