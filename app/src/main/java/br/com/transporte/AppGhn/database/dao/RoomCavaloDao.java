package br.com.transporte.AppGhn.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.transporte.AppGhn.model.Cavalo;

@Dao
public interface RoomCavaloDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long adiciona(Cavalo cavalo);

    @Delete
    void deleta(Cavalo cavalo);

    @Update
    void substitui(Cavalo cavalo);

    @Query("SELECT * FROM cavalo")
    LiveData<List<Cavalo>> todos();

    @Query("SELECT * FROM cavalo WHERE id = :cavaloId")
    LiveData<Cavalo> localizaPeloId(Long cavaloId);

    @Query("SELECT * FROM cavalo WHERE id = :cavaloId")
    Cavalo localizaPeloIdParaTask(Long cavaloId);

    @Query("SELECT * FROM cavalo WHERE placa = :placa")
    Cavalo localizaPelaPlaca(String placa);

    @Query("SELECT placa FROM cavalo")
    LiveData<List<String>> pegaPlacas();

}
