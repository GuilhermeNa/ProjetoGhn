package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import br.com.transporte.AppGhn.model.Adiantamento;

@Dao
public interface RoomAdiantamentoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adiciona(Adiantamento adiantamento);

    @Delete
    void deleta(Adiantamento adiantamento);

    @Query("SELECT * FROM adiantamento")
    List<Adiantamento> todos();

    @Query("SELECT * FROM adiantamento WHERE id = :adiantamentoId")
    Adiantamento localizaPeloId(int adiantamentoId);

}
