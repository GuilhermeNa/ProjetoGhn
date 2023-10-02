package br.com.transporte.AppGhn.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.transporte.AppGhn.model.Frete;

@Dao
public interface RoomFreteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long adiciona(Frete frete);

    @Delete
    void deleta(Frete frete);

    @Update
    void substitui(Frete frete);

    @Query("SELECT * FROM frete")
    LiveData<List<Frete>> todos();

    @Query("SELECT * FROM frete WHERE id = :freteId")
    LiveData<Frete> localizaPeloId(Long freteId);

    @Query("SELECT * FROM frete WHERE refCavaloId = :cavaloId")
    LiveData<List<Frete>> listaPorCavaloId(Long cavaloId);

    @Query("SELECT * FROM frete WHERE freteJaFoiPago LIKE :isPago")
    LiveData<List<Frete>> listaPorStatusDeRecebimento(boolean isPago);

}
