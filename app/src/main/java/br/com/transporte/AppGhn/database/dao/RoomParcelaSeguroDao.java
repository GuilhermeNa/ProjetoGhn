package br.com.transporte.AppGhn.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import br.com.transporte.AppGhn.model.ParcelaDeSeguro;

@Dao
public interface RoomParcelaSeguroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adiciona(ParcelaDeSeguro parcela);

    @Delete
    void deleta(ParcelaDeSeguro parcela);

    @Query("SELECT * FROM parcelaDeSeguro WHERE id = :parcelaId")
    ParcelaDeSeguro localizaPeloId(long parcelaId);

    @Query("SELECT * FROM ParcelaDeSeguro WHERE refSeguroId = :seguroId")
    List<ParcelaDeSeguro> listaPeloSeguroId(Long seguroId);


}
