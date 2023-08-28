package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import br.com.transporte.AppGhn.model.Cavalo;

@Dao
public interface RoomCavaloDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adiciona(Cavalo cavalo);

    @Delete
    void deleta(Cavalo cavalo);

    @Query("SELECT * FROM cavalo")
    List<Cavalo> todos();

    @Query("SELECT * FROM cavalo WHERE id = :cavaloId")
    Cavalo localizaPeloId(int cavaloId);
}
