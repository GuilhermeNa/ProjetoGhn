package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.transporte.AppGhn.model.Motorista;

@Dao
public interface RoomMotoristaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adiciona(Motorista motorista);

    @Delete
    void deleta(Motorista motorista);

    @Update
    void substitui(Motorista motorista);

    @Query("SELECT * FROM motorista")
    List<Motorista> todos();

    @Query("SELECT * FROM motorista WHERE id = :motoristaId")
    Motorista localizaPeloId(Long motoristaId);

    @Query("SELECT * FROM motorista WHERE nome = :nome")
    Motorista localizaPeloNome(String nome);

}
