package br.com.transporte.AppGhn.ui.database;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import br.com.transporte.AppGhn.model.Cavalo;

//@Dao
public interface RoomCavaloDAO {
    @Insert
    void adiciona(Cavalo cavalo);

    @Delete
    void deleta(Cavalo cavalo);

   /* @Query("SELECT * FROM cavalo")
    List<Cavalo> todos();*/

}
