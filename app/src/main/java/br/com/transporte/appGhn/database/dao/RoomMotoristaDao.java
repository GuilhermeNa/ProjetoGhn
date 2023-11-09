package br.com.transporte.appGhn.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.transporte.appGhn.model.Motorista;

@Dao
public interface RoomMotoristaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long adiciona(Motorista motorista);

    @Delete
    void deleta(Motorista motorista);

    @Update
    void substitui(Motorista motorista);

    @Query("SELECT * FROM motorista")
    LiveData<List<Motorista>> todos();

    @Query("SELECT * FROM motorista WHERE id = :motoristaId")
    LiveData<Motorista> localizaPeloId(Long motoristaId);

    @Query("SELECT * FROM motorista WHERE nome = :nome")
    Motorista localizaPeloNome(String nome);

}
