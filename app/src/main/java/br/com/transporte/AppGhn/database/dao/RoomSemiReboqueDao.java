package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import br.com.transporte.AppGhn.model.SemiReboque;

@Dao
public interface RoomSemiReboqueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adiciona(SemiReboque sr);

    @Delete
    void deleta(SemiReboque sr);

    @Query("SELECT *FROM semireboque")
    List<SemiReboque> todos();

    @Query("SELECT * FROM semireboque WHERE refCavaloId = :cavaloId")
    List<SemiReboque> listaPorCavaloId(int cavaloId);

    @Query("SELECT * FROM semireboque WHERE id = :srId")
    SemiReboque localizaPeloId(int srId);
}
