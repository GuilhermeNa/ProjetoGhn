package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.FreteEAdm;

@Dao
public interface RoomFreteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adiciona(Frete frete);

    @Delete
    void deleta(Frete frete);

    @Transaction
    @Query("SELECT * FROM frete WHERE id = :freteId")
    Frete localizaPeloId(long freteId);

    @Transaction
    @Query("SELECT * FROM frete")
    List<FreteEAdm> freteEAdm();

}
