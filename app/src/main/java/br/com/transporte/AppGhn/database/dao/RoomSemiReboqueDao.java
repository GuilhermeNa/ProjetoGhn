package br.com.transporte.AppGhn.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.transporte.AppGhn.model.SemiReboque;

@Dao
public interface RoomSemiReboqueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long adiciona(SemiReboque sr);

    @Delete
    void deleta(SemiReboque sr);

    @Query("SELECT *FROM semireboque")
    LiveData<List<SemiReboque>> todos();

    @Query("SELECT * FROM semireboque WHERE refCavaloId = :cavaloId")
    LiveData<SemiReboque> listaPorCavaloId(Long cavaloId);

    @Query("SELECT * FROM semireboque WHERE id = :srId")
    LiveData<SemiReboque> localizaPeloId(Long srId);

    @Update
    void substitui(SemiReboque reboque);
}
